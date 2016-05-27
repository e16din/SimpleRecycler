package com.e16din.simplerecycleradapter;


import android.support.annotation.LayoutRes;

public class Insertion {

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER = 2;

    @LayoutRes
    private int layoutId;
    private Object data;
    private int type = TYPE_DEFAULT;

    public Insertion() {
    }

    public Insertion(int layoutId, Object data) {
        this(layoutId, data, TYPE_DEFAULT);
    }

    public Insertion(int layoutId, Object data, int type) {
        this.layoutId = layoutId;
        this.data = data;
        this.type = type;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
