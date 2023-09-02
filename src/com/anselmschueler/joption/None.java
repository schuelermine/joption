package com.anselmschueler.joption;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class None<T> implements Option<T> {
    @Override
    public boolean equals(Object other) {
        return other instanceof None<?>;
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

    @Override
    public String toString() {
        return "None[]";
    }

    public T unwrap() throws NoneOptionException {
        throw new NoneOptionException();
    }

    public T unwrapOrNull() {
        return null;
    }

    public T unwrapOr(T fallback) {
        return fallback;
    }

    public T unwrapOrElse(Supplier<? extends T> fallback) {
        return fallback.get();
    }

    public T expect(String reason) throws NoneOptionException {
        throw new NoneOptionException(reason);
    }

    public Variant variant() {
        return Variant.NONE;
    }

    public boolean isNone() {
        return true;
    }

    public boolean isSome() {
        return false;
    }

    public boolean isSomeAnd(Predicate<? super T> predicate) {
        return false;
    }

    public Object[] asArray() {
        return new Object[] {};
    }

    public <U> None<U> map(Function<? super T, ? extends U> function) {
        return new None<>();
    }

    public <U> U mapOr(U fallback, Function<? super T, ? extends U> function) {
        return fallback;
    }

    public <U> U mapOrElse(Supplier<U> fallback, Function<? super T, ? extends U> function) {
        return fallback.get();
    }

    public None<T> inspect(Consumer<? super T> inspector) {
        return this;
    }

    public <U> None<U> and(Option<U> other) {
        return new None<>();
    }

    public <U> None<U> andAndCopy(Option<? extends U> other) {
        return new None<>();
    }

    public <U> None<U> andThen(Supplier<Option<U>> other) {
        return new None<>();
    }

    public <U> None<U> andThenAndCopy(Supplier<Option<? extends U>> other) {
        return new None<>();
    }

    public None<T> filter(Predicate<? super T> predicate) {
        return this;
    }

    public None<T> filterCopying(Predicate<? super T> predicate) {
        return new None<>();
    }

    public Option<T> or(Option<T> other) {
        return other;
    }

    public Option<T> orCopying(Option<T> other) {
        return other;
    }

    public Option<T> orAndCopy(Option<? extends T> other) {
        return Option.copy(other);
    }

    public Option<T> orElse(Supplier<Option<T>> other) {
        return other.get();
    }

    public Option<T> orElseCopying(Supplier<Option<T>> other) {
        return other.get();
    }

    public Option<T> orElseAndCopy(Supplier<Option<? extends T>> other) {
        return Option.copy(other.get());
    }

    public Option<T> xor(Option<T> other) {
        if (other.isSome()) {
            return other;
        } else {
            return this;
        }
    }

    public Option<T> xorCopying(Option<T> other) {
        if (other.isSome()) {
            return other;
        } else {
            return new None<>();
        }
    }

    public Option<T> xorAndCopy(Option<? extends T> other) {
        if (other.isSome()) {
            return Option.copy(other);
        } else {
            return new None<>();
        }
    }

    public None<T> replace(T value) {
        return new None<>();
    }

    public <U> Option<Zip<T, U>> zip(Option<U> other) {
        return new None<>();
    }

    public <U, V> None<V> zipWith(BiFunction<? super T, U, V> function, Option<U> other) {
        return new None<>();
    }

    public Iter iterator() {
        return new Iter();
    }

    @Override
    public Spliter spliterator() {
        return new Spliter();
    }

    public class Iter implements Iterator<T> {
        public boolean hasNext() {
            return false;
        }

        public T next() {
            throw new NoSuchElementException();
        }
    }

    private class Spliter implements Spliterator<T> {
        public int characteristics() {
            return Spliterator.CONCURRENT
                    | Spliterator.DISTINCT
                    | Spliterator.SIZED
                    | Spliterator.ORDERED
                    | Spliterator.SUBSIZED;
        }

        public long estimateSize() {
            return 0;
        }

        @Override
        public long getExactSizeIfKnown() {
            return 0;
        }

        public boolean tryAdvance(Consumer<? super T> action) {
            return false;
        }

        public Spliter trySplit() {
            return null;
        }
    }
}
