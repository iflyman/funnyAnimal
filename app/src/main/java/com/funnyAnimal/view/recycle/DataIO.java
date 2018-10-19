package com.funnyAnimal.view.recycle;

import java.util.ArrayList;
import java.util.Collection;

interface DataIO<T> {
    void add(T item);

    void addAll(Collection<T> collection);

    void addAll(T[] t);

    void insertAll(Collection<T> collection);

    void remove(T item);

    void remove(int position);

    void clear();

    T getItem(int position);

    ArrayList<T> getAll();
}
