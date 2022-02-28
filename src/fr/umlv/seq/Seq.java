package fr.umlv.seq;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

public class Seq<E> implements Iterable<E> {

    private final List<?> elements;
    private final  Function<Object, E> mapping;

    private Seq(List<?> elements, Function<Object, E> mapping) {
        this.elements = requireNonNull(List.copyOf(elements));
        this.mapping = requireNonNull(mapping);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "<", ">");
        elements.forEach(e -> sj.add(mapping.apply(e).toString()));
        return sj.toString();
    }

    @SuppressWarnings("unchecked")
	public static <T> Seq<T> from(List<? extends T> elements){
        return new Seq<>(requireNonNull(elements),  e -> (T)e);
    }

    @SafeVarargs
    public static <U> Seq<U> of (U... elements){
        return from(List.of(requireNonNull(elements)));
    }

    public int size(){
        return elements.size();
    }

    public E get(int index){
        Objects.checkIndex(index, size());
        return mapping.apply(elements.get(index));
    }

    public void forEach(Consumer<? super E> consumer){
        requireNonNull(consumer);
        elements.forEach(e -> consumer.accept(mapping.apply(e)));
    }

    public <R> Seq<R> map(Function<? super E, ? extends R> mapping){
        return new Seq<>(elements,  this.mapping.andThen(mapping));
    }

    public Optional<E> findFirst(){

        if(size() <= 0){
            return Optional.empty();
        }
        return Optional.of(mapping.apply(elements.get(0)));
    }

    @Override
    public Iterator<E> iterator() {
        var size = size();
        return new Iterator<>() {
            private int current = 0;

            @Override
            public boolean hasNext() {
                return current < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                var next = get(current);
                current++;
                return next;
            }
        };
    }

    private Spliterator<E> spliterator(int start, int end) {
        return new Spliterator<>() {
            private int index = start;
            @Override
            public boolean tryAdvance(Consumer<? super E> action) {
                requireNonNull(action);
                if (index < end) {
                    action.accept(mapping.apply(elements.get(index)));
                    index++;
                    return true;
                }
                return false;
            }

            @Override
            public Spliterator<E> trySplit() {
                var left = index;
                index = index + (end - index) / 2 ;
                // var middle = (index + end) >>> 1
                return left < index ? spliterator(left, index) : null;
            }

            @Override
            public long estimateSize() {
                return end - index;
            }

            @Override
            public int characteristics() {
                return Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.SIZED;
            }
        };
    }

    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(0, size()), false);
    }
}
