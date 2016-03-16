package com.yahoo.android.designer;

import android.app.Activity;
import android.os.Bundle;

import com.yahoo.android.designer.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_layout_02);
    }
}
