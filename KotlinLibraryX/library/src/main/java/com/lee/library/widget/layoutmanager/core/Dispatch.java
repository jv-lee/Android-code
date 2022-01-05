package com.lee.library.widget.layoutmanager.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

public final class Dispatch<T> {

    private static final Dispatch<?> EMPTY = new Dispatch<>();

    private static <T> T requireNonNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    private static <T> Dispatch<T> empty() {
        @SuppressWarnings("unchecked")
        Dispatch<T> t = (Dispatch<T>) EMPTY;
        return t;
    }

    public static <R> Dispatch<R> of(R value) {
        return new Dispatch<R>(value);
    }

    public static <T> Dispatch<T> ofNullable(T value) {
        return value == null ? (Dispatch<T>) empty() : of(value);
    }

    public static <R> DispatchImpl<R> stream(Collection<R> collection) {
        return new DispatchImpl<>(new ArrayList<R>(collection));
    }

    public static <R> DispatchImpl<R> stream(R... values) {
        List<R> list = new ArrayList<>();
        for (R r : values) {
            list.add(r);
        }
        return new DispatchImpl<>(list);
    }

    public static <T> DispatchImpl<T> iterate(T seed, final Function<T, T> mapper, int maxSize) {
        List<T> list = new ArrayList<>();
        list.add(seed);
        if (maxSize - 1 > 0) {
            for (int i = 0, len = maxSize - 1; i < len; i++) {
                list.add(seed = mapper.apply(seed));
            }
        }
        return new DispatchImpl<>(list);
    }

    public interface Supplier<T> {

        /**
         * Gets a result.
         *
         * @return a result
         */
        T get();
    }

    public interface BiConsumer<T, U> {

        /**
         * Performs this operation on the given arguments.
         *
         * @param t the first input argument
         * @param u the second input argument
         */
        void accept(T t, U u);
    }

    public interface BiFunction<T, U, R> {

        /**
         * Applies this function to the given arguments.
         *
         * @param t the first function argument
         * @param u the second function argument
         * @return the function result
         */
        R apply(T t, U u);
    }

    public interface Consumer<T> {
        /**
         * Performs this operation on the given argument.
         *
         * @param t the input argument
         */
        void accept(T t);
    }

    public interface Predicate<T> {
        /**
         * Evaluates this predicate on the given argument.
         *
         * @param t the input argument
         * @return {@code true} if the input argument matches the predicate,
         * otherwise {@code false}
         */
        boolean test(T t);

    }

    public interface Function<T, R> {

        /**
         * Applies this function to the given argument.
         *
         * @param t the function argument
         * @return the function result
         */
        R apply(T t);
    }

    private final T value;

    private Dispatch() {
        this.value = null;
    }

    private Dispatch(T value) {
        this.value = requireNonNull(value);
    }

    public final T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public final boolean isPresent() {
        return value != null;
    }

    public final void ifPresent(Consumer<? super T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    public final Dispatch<T> filter(Predicate<? super T> predicate) {
        requireNonNull(predicate);
        if (!isPresent()) {
            return this;
        } else {
            return predicate.test(value) ? this : (Dispatch<T>) empty();
        }
    }

    public final <U> Dispatch<U> map(Function<? super T, ? extends U> mapper) {
        requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        } else {
            return Dispatch.ofNullable(mapper.apply(value));
        }
    }

    public final <U> Dispatch<U> flatMap(Function<? super T, Dispatch<U>> mapper) {
        requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        } else {
            return requireNonNull(mapper.apply(value));
        }
    }

    public final T orElse(T other) {
        return value != null ? value : other;
    }

    public final T orElseGet(Supplier<? extends T> other) {
        return value != null ? value : other.get();
    }

    public final <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Dispatch)) {
            return false;
        }
        Dispatch<?> other = (Dispatch<?>) obj;

        return (value == other.value) || (value != null && value.equals(other.value));
    }

    @Override
    public final int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public final String toString() {
        return value != null
                ? String.format("Dispatch[%s]", value)
                : "Dispatch.empty";
    }

    public final static class DispatchImpl<E> {


        private final List<E> value;

        private DispatchImpl() {
            this.value = null;
        }

        private DispatchImpl(List<E> value) {
            this.value = value;
        }

        public final boolean isPresent() {
            return value != null;
        }

        public final DispatchImpl<E> filter(Predicate<? super E> predicate) {
            requireNonNull(predicate);
            if (isPresent()) {
                forEach(e -> {
                    if (!predicate.test(e)) {
                        this.value.remove(e);
                    }
                });
            }
            return this;
        }

        public final <R> DispatchImpl<R> map(Function<? super E, ? extends R> mapper) {
            requireNonNull(mapper);

            List<R> target = new ArrayList<>();
            if (isPresent()) {
                for (E e : value) {
                    target.add(mapper.apply(e));
                }
            }
            return new DispatchImpl<>(target);
        }

        /**
         * 多个数组数据源转换成一个数组数据源
         *
         * @param mapper
         * @param <R>
         * @return
         */
        public final <R> DispatchImpl<R> flatMap(Function<? super E, ? extends DispatchImpl<? extends R>> mapper) {
            requireNonNull(mapper);

            List<R> dispatchStream = new ArrayList<>();
            if (isPresent()) {
                this.forEach(e -> {
                    dispatchStream.addAll(((DispatchImpl<R>) mapper.apply(e)).value);
                });
            }
            return new DispatchImpl<>(dispatchStream);
        }

        public final DispatchImpl<E> distinct() {
            List<E> target = new ArrayList<>();
            if (isPresent()) {
                for (E e : value) {
                    if (!target.contains(e)) {
                        target.add(e);
                    }
                }
            }
            return this;
        }

        public final DispatchImpl<E> forEach(Consumer<? super E> action) {
            requireNonNull(action);
            if (isPresent()) {
                for (E e : value) {
                    action.accept(e);
                }
            }
            return this;
        }

        public final DispatchImpl<E> forEach(BiConsumer<Integer, E> action) {
            requireNonNull(action);
            if (isPresent()) {
                for (int i = 0, len = value.size(); i < len; i++) {
                    action.accept(i, value.get(i));
                }
            }
            return this;
        }

        public final DispatchImpl<E> limit(int maxSize) {
            if (this.isPresent() && maxSize > 0) {
                return new DispatchImpl<>(this.value.subList(0, maxSize > this.count() ? this.count() : maxSize));
            }
            return this;
        }

        public final DispatchImpl<E> skip(int n) {
            if (this.isPresent() && n > 0 && n < count()) {
                return new DispatchImpl<>(this.value.subList(n, this.count()));
            }
            return this;
        }

        public final DispatchImpl<E> peek(Consumer<? super E> action) {
            if (this.isPresent()) {
                forEach(action);
            }
            forEach(action);
            return this;
        }

        public final DispatchImpl<E> sorted() {
            return sorted(Object::hashCode);
        }

        public final <R extends Comparable<? super R>> DispatchImpl<E> sorted(Function<E, R> function) {
            requireNonNull(function);
            return sorted((Comparator<E> & Serializable) (c1, c2) -> function.apply(c1).compareTo(function.apply(c2)));
        }

        public final DispatchImpl<E> sorted(Comparator<? super E> comparator) {
            requireNonNull(comparator);
            if (isPresent()) {
                Collections.sort(value, comparator);
            }
            return this;
        }

        public final int count() {
            if (isPresent()) {
                return value.size();
            }
            return 0;
        }

        public final Dispatch<E> findFirst() {
            if (isPresent() && value.size() > 0) {
                return Dispatch.ofNullable(value.get(0));
            }
            return Dispatch.empty();
        }

        public final Dispatch<E> findAny() {
            if (isPresent() && value.size() > 0) {
                return Dispatch.ofNullable(value.get(new Random().nextInt(value.size())));
            }
            return Dispatch.empty();
        }

        public final Dispatch<E> findAny(Predicate<E> predicate) {
            if (isPresent() && value.size() > 0) {
                for (E e : value) {
                    if (predicate.test(e)) {
                        return Dispatch.ofNullable(e);
                    }
                }
            }
            return Dispatch.empty();
        }

        public final E[] toArray() {
            if (isPresent()) {
                return (E[]) this.value.toArray();
            }
            return null;
        }


        /**
         * 输出需要的数据类型
         *
         * @param supplier
         * @param accumulator
         * @return
         */
        public final <R> R toCollect(Supplier<R> supplier, BiConsumer<R, ? super E> accumulator) {
            requireNonNull(supplier);
            requireNonNull(accumulator);
            R r = supplier.get();
            forEach(t -> {
                accumulator.accept(r, t);
            });
            return r;
        }


        public final <R extends Collection> R toCollection(Supplier<R> supplier) {
            return toCollect(supplier, Collection::add);
        }

        public final List<E> toList() {
            return toCollection((Supplier<List<E>>) ArrayList::new);
        }

        public final Set<E> toSet() {
            return toCollection((Supplier<Set<E>>) HashSet::new);
        }


        public final <K, U> Map<K, U> toMap(Function<? super E, ? extends K> keyMapper, Function<? super E, ? extends U> valueMapper, BiFunction<U, U, U> mergeFunction, Supplier<Map<K, U>> mapSupplier) {
            BiConsumer<Map<K, U>, E> accumulator = (map, element) -> {
                K key = keyMapper.apply(element);
                U value = valueMapper.apply(element);

                U oldValue = map.get(key);
                U newValue = (oldValue == null) ? value : mergeFunction.apply(oldValue, value);
                if (newValue == null) {
                    map.remove(key);
                } else {
                    map.put(key, newValue);
                }
            };
            return this.toCollect(HashMap::new, accumulator);
        }

        public final <K, U> Map<K, U> toMap(Function<? super E, ? extends K> keyMapper, Function<? super E, ? extends U> valueMapper, BiFunction<U, U, U> mergeFunction) {
            return this.toMap(keyMapper, valueMapper, (u, v) -> mergeFunction.apply(u, v), (Supplier<Map<K, U>>) HashMap::new);
        }

        public final <K, U> Map<K, U> toMap(Function<? super E, ? extends K> keyMapper, Function<? super E, ? extends U> valueMapper) {
            return this.toMap(keyMapper, valueMapper, (u, v) -> {
                throw new IllegalStateException(String.format("Duplicate key %s", u));
            });
        }


        public final <K> Map<K, List<E>> toGroupingBy(Function<? super E, ? extends K> classifier) {
            return toGroupingBy(classifier, (Supplier<Map<K, List<E>>>) HashMap::new);
        }

        public final <K> Map<K, List<E>> toGroupingBy(Function<? super E, ? extends K> classifier, Supplier<Map<K, List<E>>> mapFactory) {

            BiConsumer<Map<K, List<E>>, E> accumulator = (map, element) -> {
                K key = classifier.apply(element);
                if (map.containsKey(key)) {
                    List<E> temp = map.get(key);
                    temp.add(element);
                } else {
                    List<E> list = new ArrayList<>();
                    list.add(element);
                    map.put(key, list);
                }
            };
            return toCollect(mapFactory, accumulator);
        }


        public final E toJoin(BiFunction<E, E, E> biFunction) {
            requireNonNull(biFunction);
            BiFunction<E, E, E> temp = (t, u) -> {
                if (t == null) {
                    return u;
                } else {
                    return biFunction.apply(t, u);
                }
            };
            E apply = null;
            if (isPresent()) {
                for (int i = 0, len = value.size() - 1; i <= len; i++) {
                    apply = temp.apply(apply, value.get(i));
                }
            }
            return apply;
        }


        @Override
        public final String toString() {
            return isPresent() ? value.toString() : super.toString();
        }
    }
}