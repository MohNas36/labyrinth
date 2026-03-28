package labyrinth.util.adapter;

import labyrinth.util.observable.InvalidationListener;
import labyrinth.util.observable.Observable;
import labyrinth.util.observable.ObservableList;

import java.lang.ref.WeakReference;

public class ListInvalidationListener<T> implements InvalidationListener {
    private final ObservableList<? extends T> source;
    private final WeakReference<javafx.collections.ObservableList<T>> wrapperRef;

    public ListInvalidationListener(
        ObservableList<? extends T> source, javafx.collections.ObservableList<T> wrapper) {
        this.source = source;
        wrapperRef = new WeakReference<>(wrapper);
    }

    @Override
    public void invalidated(Observable observable) {
        javafx.collections.ObservableList<T> wrapper = wrapperRef.get();
        if (wrapper == null) {
            observable.removeListener(this);
        } else {
            wrapper.setAll(source);
        }
    }
}

