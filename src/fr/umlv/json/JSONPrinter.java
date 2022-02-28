package fr.umlv.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JSONPrinter {

    private final static ClassValue<List<Function<Record, String>>> CACHE = new ClassValue<>() {
        @Override
        protected List<Function<Record, String>>  computeValue(Class<?> type) {
            return Arrays.stream(type.getRecordComponents()).map(JSONPrinter::getJSONMapper).toList();
        }
    };

    private static String escape(Object o) {
        return o instanceof String
                ? "\"" + o + "\""
                : "" + o;
    }

    private static Object invoke(Method accessor, Object obj, Object... args) {
        Objects.requireNonNull(accessor);
        Objects.requireNonNull(obj);
        Objects.requireNonNull(args);
        try {
            return accessor.invoke(obj);
        } catch (InvocationTargetException | IllegalAccessException e) {
            var cause = e.getCause();
            switch (cause) {
                case RuntimeException exception -> throw exception;
                case Error error -> throw error;
                default -> throw new UndeclaredThrowableException(e);
            }
        }
    }

    public static String toJSON(Record record) {
        return CACHE.get(record.getClass())
                .stream()
                .map(mapper -> mapper.apply(record))
                .collect(Collectors.joining(",\n", "{\n", "\n}"));
    }

    private static String toJSONOld(Record record) {
        var components = record.getClass().getRecordComponents();
        return Arrays.stream(components)
                .map(c -> "\t" + escape(c.getName()) + ": " + escape(invoke(c.getAccessor(), record)))
                .collect(Collectors.joining(",\n", "{\n", "\n}"));
    }

    private static String getJSONName(Method accessor){
        var name = accessor.getName();
        var prop = accessor.getAnnotation(JSONProperty.class);
        if(prop == null){
            return name;
        }
        var propName = prop.value();
        return propName.isEmpty() ? name.replaceAll("_", "-") : propName;
    }

    private static Function<Record, String> getJSONMapper(RecordComponent component){
        var accessor = component.getAccessor();
        var name = escape(getJSONName(accessor));
        return record -> "\t" + name + ": " + escape(invoke(accessor, record));
    }

    public static void main(String[] args) {
        record Book(@JSONProperty String book_title, @JSONProperty("an~author") String an_author, int price) { }

        var book = new Book("The Girl with The Dragon Tattoo", "Stieg Larsson", 100);
        var bookJSON = JSONPrinter.toJSON(book);

        System.out.println(bookJSON);
    }
}