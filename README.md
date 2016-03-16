## Dynamic Layout Loader

`LayoutInflater` cannot load layouts that are plain XML. It can also not load layouts that are not loaded from resources (`R.layout.{blah}`).

Dynamic Layout Loader uses a non-public API (available and unchanged since API Level 1) to help load compiled layouts from any source - local filesystem, network etc. All it needs is `InputStream` to load the data or raw data as `byte[]`.

## Repository Structure

* `dynamic-layout-loader`: Provides the loader. See [`LayoutLoader`](dynamic-layout-loader/src/main/java/com/yahoo/android/dlayout/LayoutLoader.java) class.
* `app`: Sample app demonstrating the use of the loader.
* `layout-designer`: Accompanying app to create layouts independent of the target app - to demonstrate full capabilities.

## Constraints

1. The resource references do not work. For example, you cannot use `@drawable/icon` or `@string/name` etc in the layout.
    1. As a corollary, the ids do not work. You cannot use `@id/another_element` in layout or `findViewById` in code. Use `android:tag` and `findViewWithTag`.
1. The current implementation does not support styles and theme. You cannot use `style` attribute in the layout.


## License

The use and distribution terms for this software are covered by the Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0.html).

See file [LICENSE](LICENSE) accompanying the code.
