# SimpleRecyclerAdapter
Простой в использовании адаптер, для быстрой разработки приложений.  

## Подключаем библиотеку в build.gradle: 
```groovy
repositories {
    maven { url "https://jitpack.io" }
}

buildscript {
    repositories {
        maven { url "https://jitpack.io" }
    }
}

dependencies {
    compile 'com.github.e16din:SimpleRecyclerAdapter:0.1.1'
}
```

## Добавляем элементы и вставки:
```java
//add header
mAdapter.addHeader(R.layout.header);

//add footer
mAdapter.addFooter(R.layout.footer);

//add item after footers and before headers
mAdapter.addItem(item);
mAdapter.addItem(0, item);

//add insertion after footers and before headers
mAdapter.addInsertion(new Insertion(R.layout.insertion, anyData));
mAdapter.addInsertion(0, new Insertion(R.layout.insertion, anyData));

//add item or insertion after footers and before headers
mAdapter.add(itemOrInsertion);
```

## Добавляем обработчики кликов:
```java
//items
mAdapter.setOnItemClickListener(new OnItemClickListener<M>() {
            @Override
            public void onClick(M item, int position) {
                //...
            }
        });

//inserts, headers, footers
setOnInsertionClickListener(new OnInsertionClickListener() {
            @Override
            public void onClick(Insertion insertion, int position) {
                //...
            }
        });
```

## Наследуем SimpleRecyclerAdapter:
```java
public class RecyclerAdapter<T extends ItemModel>
        extends SimpleRecyclerAdapter<RecyclerAdapter.MyViewHolder, T> {

    public RecyclerAdapter(@NonNull Context context, @NonNull List<T> items) {
        super(context, items, R.layout.item);
    }

    @Override
    protected MyViewHolder newViewHolder(View v) {
        return new MyViewHolder(v);
    }

    @Override
    protected void onBindItemViewHolder(MyViewHolder holder, int position) {
        super.onBindItemViewHolder(holder, position);

        final ItemModel item = getItem(position);

        //...
    }

	@Override
    protected void onBindInsertion(H holder, int position) {
    	final Insertion insertion = getInsertion(position);

        //...
    }

    public static class MyViewHolder extends SimpleViewHolder {
		//...

        MyViewHolder(View itemView) {
            super(itemView);
            //...
        }
    }
}
```

