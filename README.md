# SimpleRecycler

[![Release](https://jitpack.io/v/e16din/SimpleRecycler.svg)](https://jitpack.io/#e16din/SimpleRecycler)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-SimpleRecycler-green.svg?style=true)](https://android-arsenal.com/details/1/4223)

This library extends RecyclerView + Recycler Adapter.

Use it to comfortable work with lists.

## Out of the box:
* Ripple-effect to items
* Headers and Footers like in the ListView
* Custom Insertions between items
* OnItemClickListener and handy OnItemViewsClickListener
* Paging with inner logic to load more data


## Usage
### Add items
```java
//add header
mAdapter.addHeader(R.layout.header);

//add footer
mAdapter.addFooter(R.layout.footer);

//add insertion after headers and before footers
mAdapter.addInsertion(new Insertion(R.layout.insertion, anyData));

//add item after headers and before footers
mAdapter.add(itemOrInsertion);
```

### Set click listeners
```java
mAdapter.setOnItemClickListener(new SimpleAdapter.OnItemClickListener<String>() {
    @Override
    public void onClick(String item, int position) {
        // do something
    }
});

mAdapter.setOnItemViewsClickListener(new int[]{R.id.vName, R.id.vClickableImage},
    new SimpleRecyclerAdapter.OnItemViewsClickListener<String>() {
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
public class MyAdapter extends SimpleAdapter<String, RecyclerAdapter.ItemViewHolder> {

    public MyAdapter(Context context, List<Object> items) {
        super(context, items, R.layout.item_simple_recycler);
        setNeedShowBottomProgress(true);
    }

    @Override
    protected ItemViewHolder newViewHolder(View v) {
        return new ItemViewHolder(v);
    }

    @Override
    protected void onBindItemViewHolder(ItemViewHolder holder, int position) {
        super.onBindItemViewHolder(holder, position);

        String item = getItem(position);

        holder.vName.setText(item);
    }

    @Override
    protected void onBindHeaderViewHolder(SimpleViewHolder holder, int position) {
        //do something with headers
    }

    static class ItemViewHolder extends SimpleViewHolder {
        TextView vName;

        public ItemViewHolder(View view) {
            super(view);
            vName = (TextView) view.findViewById(R.id.vName);
        }
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
        compile 'com.github.e16din:SimpleRecycler:0.4.6'
    }
```

## License MIT
Copyright (c) 2016 [Александр Кундрюков (e16din)](https://goo.gl/4xTCko)

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
