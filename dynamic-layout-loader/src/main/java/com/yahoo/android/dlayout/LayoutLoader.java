/*
 * Copyright 2014 Yahoo Inc.
 *
 * Licensed under the terms of the Apache License, Version 2.
 * Please see LICENSE.txt in the project root for terms.
 */
package com.yahoo.android.dlayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <code>LayoutLoader</code> helps you load compiled layouts from any source.
 *
 * <p>
 *     The layout can be loaded from either an {@link InputStream} or raw bytes.
 * </p>
 *
 * <pre>
 * class MyApplication extends Application {
 *     private LayoutLoader layoutLoader;
 *
 *     protectd void onCreate() {
 *         //
 *     }
 * }
 * </pre>
 *
 * @author Gaurav Vaish
 * @see #loadTemplate(InputStream, Context, ViewGroup, boolean)
 * @see #loadTemplate(byte[], Context, ViewGroup, boolean)
 */
public class LayoutLoader {

    private Constructor<?> xmlBlockCtor;
    private Method newParserMethod;
    private boolean ready;

    public LayoutLoader initialize() {
        if(!ready) {
            synchronized(this) {
                if(!ready) {
                    initializeImpl();
                    ready = true;
                }
            }
        }
        return this;
    }

    public LayoutLoader cleanup() {
        if(ready) {
            synchronized(this) {
                if(ready) {
                    xmlBlockCtor = null;
                    newParserMethod = null;
                    ready = false;
                }
            }
        }
        return this;
    }

    protected void initializeImpl() {
        try {
            Class<?> cls = Class.forName("android.content.res.XmlBlock");
            xmlBlockCtor = cls.getDeclaredConstructor(byte[].class);
            xmlBlockCtor.setAccessible(true);

            newParserMethod = cls.getDeclaredMethod("newParser");
            newParserMethod.setAccessible(true);
        } catch(RuntimeException e) {
            Log.e("LayoutLoader", "Failed initializing loader", e);
        } catch(Exception e) {
            Log.e("LayoutLoader", "Failed initializing loader", e);
        }
    }

    public View loadTemplate(InputStream input, Context context, ViewGroup root, boolean attachToRoot) {
        View rv = null;

        if(ready && xmlBlockCtor != null && newParserMethod != null) {
            byte[] data = readAll(input);
            rv = loadTemplate(data, context, root, attachToRoot);
        }

        return rv;
    }

    public View loadTemplate(byte[] data, Context context, ViewGroup root, boolean attachToRoot) {
        View rv = null;
        if(data != null) {
            try {
                Object xmlBlock = xmlBlockCtor.newInstance(data);
                XmlResourceParser parser = (XmlResourceParser) newParserMethod.invoke(xmlBlock);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rv = inflater.inflate(parser, root, attachToRoot);
            } catch(RuntimeException e) {
                Log.e("LayoutLoader", "Failed loading layout", e);
            } catch(Exception e) {
                Log.e("LayoutLoader", "Failed loading layout", e);
            }
        }
        return rv;
    }

    private byte[] readAll(InputStream input) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        byte[] rv = null;

        try {
            read = input.read(buffer, 0, buffer.length);
            while(read >= 0) {
                baos.write(buffer, 0, read);
                read = input.read(buffer, 0, buffer.length);
            }
            rv = baos.toByteArray();
        } catch(IOException e) {
        }

        return rv;
    }
}