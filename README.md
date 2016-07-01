#SimpleRecycler
Набор для удобной работы со списками, расширяет RecyclerView + RecyclerAdapter.

Добавлен часто используемый функционал, например вставка хедеров, футеров и произвольных элементов, автоматическое добавление ripple effect'а элементам, callback для обработки клика на элемент.


* [SimpleAdapter](https://github.com/e16din/SimpleRecycler#simpleadapter)
* [SimplePagingAdapter](https://github.com/e16din/SimpleRecycler#simplepagingadapter)
* [SimpleInsertsAdapter](https://github.com/e16din/SimpleRecycler#simpleinsertsadapter)
* [SimpleRecyclerView](https://github.com/e16din/SimpleRecycler#simplerecyclerview)
* [SimpleListView](https://github.com/e16din/SimpleRecycler#simplelistview)

[![Release](https://jitpack.io/v/e16din/SimpleRecycler.svg)](https://jitpack.io/#e16din/SimpleRecycler)

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
    compile 'com.github.e16din:SimpleRecycler:0.+'
}
```

#SimpleAdapter
Простой в использовании адаптер.
Автоматически добавляет ripple effect (см. SimpleRecyclerAdapter).

Включает функционал базовых адаптеров(SimplePagingAdapter, SimpleInsertsAdapter, SimpleRecyclerAdapter), каждый из которых можно использовать самостоятельно.

## Добавляем элементы и вставки:
```java
//add header
mAdapter.addHeader(R.layout.header);

//add footer
mAdapter.addFooter(R.layout.footer);

//add item after headers and before footers
mAdapter.add(item);
mAdapter.add(0, item);

//add insertion after headers and before footers
mAdapter.addInsertion(new Insertion(R.layout.insertion, anyData));
mAdapter.addInsertion(0, new Insertion(R.layout.insertion, anyData));

//add item or insertion after headers and before footers
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


## Наследуем SimpleAdapter:

```java
public class RecyclerAdapter extends SimpleAdapter<String> {

    public RecyclerAdapter(@NonNull Context context, @NonNull List<Object> items) {
        super(context, items, R.layout.item_simple_recycler);
        setNeedShowBottomProgress(true);
    }

    @Override
    protected void onBindItemViewHolder(SimpleViewHolder holder, int position) {
        super.onBindItemViewHolder(holder, position);
        ItemViewHolder h = (ItemViewHolder) holder;

        String item = getItem(position);

        h.vItemText.setText(item);
    }

    @Override
    protected void onBindInsertionViewHolder(SimpleViewHolder holder, int position) {
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
    protected ItemViewHolder newViewHolder(View v) {
        return new ItemViewHolder(v);
    }

    @Override
    public InsertionViewHolder newInsertionViewHolder(View v) {
        return new InsertionViewHolder(v);
    }

    static class ItemViewHolder extends SimpleViewHolder {
        TextView vItemText;

        public ItemViewHolder(View view) {
            super(view);
            vItemText = (TextView) view.findViewById(R.id.vItemText);
        }
    }

    static class InsertionViewHolder extends SimpleViewHolder {
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

## Активируем footer подгрузки:
```java

mAdapter.setNeedShowBottomProgress(true);
```

#SimplePagingAdapter
Добавляет элемент загрузки в конец ленты, по умолчанию это progress bar.
Работает только в паре с SimpleRecyclerView и его наследниками.
Бар подгрузки скрывается автоматически если добавленных элементов меньше чем getPageSize()

```java
//Устанавливаем размер страницы
setPageSize(20);

//Изменяем макет закрузки:
mAdapter.setBottomProgressLayoutId(R.layout.footer_progress);

//Скрываем загрузку:
mAdapter.hideBottomProgress();

//Показываем загрузку:
mAdapter.showBottomProgress();
```

#SimpleInsertsAdapter
Включает функционал вставки произвольных View в список элементов.
Вставка footer'ов и header'ов.
Имеет отдельный от основного ViewHandler и обрабатывается на onBindInsertionViewHolder.

============================

#SimpleRecyclerView
Наследуется от RecyclerView, добавляет логику взаимодействия с SimpleRecyclerAdapter и SimplePagingAdapter.
Сейчас это отслеживание последнего элемента при скролле.

#SimpleListView
Наследуется от SimpleRecyclerView, автоматически устанавливает LinearLayoutManager при создании.
