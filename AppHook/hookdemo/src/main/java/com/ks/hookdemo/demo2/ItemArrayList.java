package com.ks.hookdemo.demo2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ItemArrayList<T extends Item> extends ArrayList<T> {

    @Override
    public boolean add(T t) {
        if (!t.isValid()) {
            return false;
        }
        return super.add(t);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        removeInvalidItem(c);
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        removeInvalidItem(c);
        return super.addAll(index, c);
    }

    private void removeInvalidItem(Collection<? extends T> c) {
        Iterator<T> iterator = (Iterator<T>) c.iterator();
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (!t.isValid()) {
                iterator.remove();
            }
        }
    }
}
