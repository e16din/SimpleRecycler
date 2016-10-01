# SimpleRecycler

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-SimpleRecycler-green.svg?style=true)](https://android-arsenal.com/details/1/4223)
[![Release](https://jitpack.io/v/e16din/SimpleRecycler.svg)](https://jitpack.io/#e16din/SimpleRecycler)

Use it to comfortable work with lists. 

SimpleRecycler includes [HandyHolder](https://github.com/e16din/HandyHolder) library for comfortable creating and updating holders. 



## Out of the box:

* Asynchronous layout inflation
* Ripple-effect to items
* OnClickListener and handy OnViewsClickListener
* Headers and Footers like in the ListView
* Custom Insertions between items
* Paging with inner logic to load more data
* Implemented List interface (use adapter as list)



## Usage
### Add items
```java
//add header
mAdapter.addHeader(R.layout.layout_header);

//add footer
mAdapter.addFooter(R.layout.layout_footer);

//add insertion after headers and before footers
mAdapter.addInsertion(new Insertion(R.layout.layout_insertion, anyData));

//add item after headers and before footers
mAdapter.add(item);
```

### Set click listeners
```java
mAdapter.setOnItemClickListener(new OnItemClickListener<String>() {
    @Override
    public void onClick(String item, int position) {
        // do something
    }
});

mAdapter.setOnItemViewsClickListener(new int[]{R.id.vName, R.id.vClickableImage},
    new OnItemViewsClickListener<String>() {
        @Override
        public void onClick(@IdRes int childViewId, String item, int position) {
            switch (childViewId) {
                case R.id.vName:
                    // on vName click!
                    break;
                case R.id.vClickableImage:
                    // on vClickableImage click!
                    break;
            }
        }
});
```

### Implement adapter
```java
public class MyAdapter extends StrongSimpleAdapter<MyHolder, String> {

    public MyAdapter(@NonNull Context context, @NonNull List<String> items) {
        super(context, items, R.layout.item);
    }
    
    @Override
    protected void onBindItemViewHolder(MyHolder holder, int position) {
        super.onBindItemViewHolder(holder, position);
        
        String item = get(position);
        
        //... 
    }

    @Override
    protected MyHolder<String> newViewHolder(View v, int viewType) {
        return new MyHolder(v);
    }
    
    static class MyHolder extends ViewHolder {
        //...
    }
}
```

### Asynchronous layout inflation
```java
mAdapter.setAsyncInflating(true);
```

### Disable ripple effect
```java
mAdapter.setRippleEffect(false);
```
### Customize ripple effect color
```xml
<color name="simplePressedColor">color</color>
```
or
```java
mAdapter.setRippleColor(color)
```

### Several view holders
```java
public class MyAdapter extends SimpleAdapter<MODEL> {

    @Override
    protected ItemViewHolder<MODEL> newViewHolder(View v, int viewType) {
        switch (viewType) {
            case TYPE_FIRST:
                return new FirstHolder(v);
            case TYPE_SECOND:
                return new SecondHolder(v);
            case TYPE_THIRD:
                return new ThirdHolder(v);
        }

        return null;//no way
    }

    @Override
    public int getItemViewType(int position) {
        if (isInsertion(position)) return TYPE_INSERTION;

        if (condition1) return TYPE_FIRST;
        if (condition2) return TYPE_SECOND;
        if (condition3) return TYPE_THIRD;
    }
}
```

## Download
Step 1. Add it in your root build.gradle at the end of repositories:
```groovy
    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
```
Step 2. Add the dependency
```groovy
    dependencies {
        compile("com.github.e16din:SimpleRecycler:0.7.4@aar") {
            transitive = true
        }
    }
```

## License MIT
Copyright (c) 2016 [Александр Кундрюков (e16din)](http://goo.gl/pzjc8x)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
