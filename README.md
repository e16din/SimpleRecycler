#SimpleRecycler
Набор для удобной работы со списками.  

* [SimpleAdapter](https://github.com/e16din/SimpleRecycler#simpleadapter)
* [SimplePagingAdapter](https://github.com/e16din/SimpleRecycler#simplepagingadapter)
* [SimpleInsertsAdapter](https://github.com/e16din/SimpleRecycler#simpleinsertsadapter)
* [SimpleRecyclerView](https://github.com/e16din/SimpleRecycler#simplerecyclerview)
* [SimpleListView](https://github.com/e16din/SimpleRecycler#simplelistview)


##Подключаем библиотеку в build.gradle: 
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
    compile 'com.github.e16din:SimpleRecycler:0.2.0'
}
```

#SimpleAdapter
Простой в использовании адаптер.
Включает функционал базовых адаптеров(SimplePagingAdapter, SimpleInsertsAdapter, SimpleRecyclerView), каждый из которых можно использовать самостоятельно.


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

## Активируем footer подгрузки:
```java

mAdapter.setNeedShowBottomProgress(true);
```

## Наследуем SimpleAdapter:
```java
public class RecyclerAdapter extends SimpleAdapter<String> {

    public RecyclerAdapter(@NonNull Context context, @NonNull List<Object> items) {
        super(context, items, R.layout.item_simple_recycler);
        setNeedShowBottomProgress(true);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindItemViewHolder(holder, position);
        ItemViewHolder h = (ItemViewHolder) holder;

        String item = getItem(position);

        h.vItemText.setText(item);
    }

    @Override
    protected void onBindInsertionViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindInsertionViewHolder(holder, position);
        InsertionViewHolder h = (InsertionViewHolder) holder;

        Insertion insertion = getInsertion(position);

        String text = null;
        switch (insertion.getType()) {
            case Insertion.TYPE_HEADER:
                text = "Header";
                break;
            case Insertion.TYPE_DEFAULT:
                text = position - getHeadersCount() + ". Default insertion";
                break;
            case Insertion.TYPE_FOOTER:
                text = "Footer";
                break;
        }

        h.vText.setText(text);
        h.vInsertionData.setText("" + insertion.getData());
    }

    @Override
    protected void addRippleEffect(ViewGroup vContainer, int position) {
        if (position != 1) {//for example, item(1) without ripple
            super.addRippleEffect(vContainer, position);
        }
    }

    @Override
    protected ItemViewHolder newViewHolder(View v) {
        return new ItemViewHolder(v);
    }

    @Override
    public InsertionViewHolder newInsertionViewHolder(View v) {
        return new InsertionViewHolder(v);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView vItemText;

        public ItemViewHolder(View view) {
            super(view);
            vItemText = (TextView) view.findViewById(R.id.vItemText);
        }
    }

    static class InsertionViewHolder extends RecyclerView.ViewHolder {
        TextView vText;
        TextView vInsertionData;

        public InsertionViewHolder(View view) {
            super(view);
            vText = (TextView) view.findViewById(R.id.vText);
            vInsertionData = (TextView) view.findViewById(R.id.vInsertionData);
        }
    }
}

```

#SimplePagingAdapter
Добавляет элемент загрузки в конец ленты, по умолчанию это progress bar.
Работает только в паре с SimpleRecyclerView и его наследниками.

## Изменяем макет закрузки:
```java

mAdapter.setBottomProgressLayoutId(R.layout.footer_progress);
```

## Скрываем загрузку:
```java

mAdapter.hideBottomProgress();
```

## Показываем загрузку:
```java

mAdapter.showBottomProgress();
```

#SimpleInsertsAdapter
Включает функционал вставки произвольных View в список элементов.
Вставка footer'ов и header'ов.

============================

#SimpleRecyclerView
Наследуется от RecyclerView, добавляет логику взаимодействия с SimpleRecyclerAdapter и SimplePagingAdapter.
Сейчас это отслеживание последнего элемента при скролле.

#SimpleListView
Наследуется от SimpleRecyclerView, автоматически устанавливает LinearLayoutManager при создании.



