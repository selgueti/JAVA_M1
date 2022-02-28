package fr.umlv.reversible;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Reversible<E> extends Iterable<E> {

    int size();
    E get(int index);
    Reversible<E> reversed();

    default Stream<E> stream(){
        return StreamSupport.stream(spliterator(), true);
    }

    private static <V> Reversible<V> fromList(List<? extends V> list, int size, boolean reversed, Reversible<V> clone){
        Objects.requireNonNull(list);
        return new Reversible<>() {
            private Reversible<V> mirror = clone;

            @Override
            public int size() {
                return size;
            }

            @Override
            public V get(int index) {
                Objects.checkIndex(index, size());
                if (list.size() < size()) {
                    throw new IllegalStateException();
                }
                return Objects.requireNonNull(list.get(reversed ? size() - index - 1 : index));
            }

            @Override
            public Reversible<V> reversed() {
                if(mirror == null){
                    mirror = Reversible.fromList(list, size, !reversed, this);
                }
                return mirror;
            }

            @Override
            public Iterator<V> iterator() {
                return new Iterator<>() {
                    int index = 0;
                    @Override
                    public boolean hasNext() {
                        if (list.size() < size) {
                            throw new ConcurrentModificationException();
                        }
                        return index < size();
                    }

                    @Override
                    public V next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return get(index++);
                    }
                };
            }

            private Spliterator<V> fromIndex(int start, int end){
                return new Spliterator<>() {
                    private int i = start;
                    @Override
                    public boolean tryAdvance(Consumer<? super V> action) {
                        if (list.size() < size) {
                            throw new ConcurrentModificationException();
                        }
                        if(i < end){
                            action.accept(get(i++));
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public Spliterator<V> trySplit() {
                        var middle = (i + end) >>> 1;
                        if (middle == i) {
                            return null;
                        }
                        var spliterator = fromIndex(i, middle);
                        i = middle;
                        return spliterator;
                    }

                    @Override
                    public long estimateSize() {
                        return end - i;
                    }

                    @Override
                    public int characteristics() {
                        return NONNULL | ORDERED | SIZED | SUBSIZED;
                    }
                };
            }

            @Override
            public Spliterator<V> spliterator() {
                return fromIndex(0, size());
            }
        };
    }

    @SafeVarargs
    static <T> Reversible<T> fromArray(T... elements) {
        for (T elem : elements) {
            Objects.requireNonNull(elem);
        }
        return fromList(Arrays.asList(elements));
    }

    static <U> Reversible<U> fromList(List<? extends U> list){
        for (var elem : list) {
            Objects.requireNonNull(elem);
        }
        return fromList(list, list.size(), false, null);
    }
}
