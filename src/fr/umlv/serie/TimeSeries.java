package fr.umlv.serie;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TimeSeries <E> {

    private long lastTimeStamp;
    private final List<Data<E>> elements = new ArrayList<>();

    public void add(long timestamp, E element){
        Objects.requireNonNull(element);
        if(!elements.isEmpty() && lastTimeStamp > timestamp){
            throw new IllegalStateException("timestamp invalid");
        }
        lastTimeStamp = timestamp;
        elements.add(new Data<>(timestamp, element));
    }

    public Data<E> get (int index) {
        Objects.checkIndex(index, elements.size());
        return elements.get(index);
    }

    public int size () {
        return elements.size();
    }

    public Index index() {
        return index(value -> true);
    }

    public Index index(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        return new Index(IntStream.range(0, size()).filter(
                i -> filter.test(elements.get(i).element)).toArray(), this.hashCode());
    }

    record Data<T>(long timestamp, T element) {
        public Data {
            Objects.requireNonNull(element);
        }

        @Override
        public String toString() {
            return timestamp + " | " + element;
        }
    }

    public class Index implements Iterable<Data<E>>{

        private final int[] tableIndex;
        private final int currentTimeSerie;

        private Index(int[] tableIndex, int currentTimeSerie) {
            Objects.requireNonNull(tableIndex);
            this.tableIndex = tableIndex;
            this.currentTimeSerie = currentTimeSerie;
        }

        @Override
        public String toString(){
            var builder = new StringJoiner("\n");
            for (var index : tableIndex){
                builder.add(elements.get(index).toString());
            }
            return builder.toString();
        }

        public int size() {
            return tableIndex.length;
        }

        public void forEach(Consumer<? super Data<E>> consumer){
            Objects.requireNonNull(consumer);
            Arrays.stream(tableIndex).forEach( i -> consumer.accept(elements.get(i)));
        }

        public Index or(Index other){
            Objects.requireNonNull(other);
            if(currentTimeSerie != other.currentTimeSerie){
                throw new IllegalArgumentException("Can not build index with indexes from different TimeSeries");
            }
            var element = IntStream.concat(Arrays.stream(tableIndex), Arrays.stream(other.tableIndex))
                    .sorted().
                    distinct().
                    toArray();
            return new Index(element, currentTimeSerie);
        }

        public Index and(Index other){
            Objects.requireNonNull(other);
            if(currentTimeSerie != other.currentTimeSerie){
                throw new IllegalArgumentException("Can not build index with indexes from different TimeSeries");
            }
            Set<Integer> set = Arrays.stream(tableIndex).boxed().collect(Collectors.toSet());
            var content = Arrays.stream(other.tableIndex).filter(set::contains).sorted().toArray();
            return new Index(content, currentTimeSerie);
        }
        
        @Override
        public Iterator<Data<E>> iterator(){
            return new Iterator<>() {
                private int currentIndice = 0;

                @Override
                public boolean hasNext() {
                    return currentIndice != tableIndex.length;
                }

                @Override
                public Data<E> next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException("No more elements available");
                    }
                    var res = elements.get(tableIndex[currentIndice]);
                    currentIndice++;
                    return res;
                }
            };
        }
    }
}
