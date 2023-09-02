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

public final class Some<T> implements Option<T> {
    private T value;

    public Some(T value) {
        this.value = value;
    }

    public T value() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Some<?> some) {
            return Objects.equals(value, some.value);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Some[value=" + value + "]";
    }

    public T unwrap() {
        return value;
    }

    public T unwrapOrNull() {
        return null;
    }

    public T unwrapOr(T fallback) {
        return value;
    }

    public T unwrapOrElse(Supplier<? extends T> fallback) {
        return value;
    }

    public T expect(String reason) {
        return value;
    }

    public Variant variant() {
        return Variant.SOME;
    }

    public boolean isNone() {
        return false;
    }

    public boolean isSome() {
        return true;
    }

    public boolean isSomeAnd(Predicate<? super T> predicate) {
        return predicate.test(value);
    }

    public Object[] asArray() {
        return new Object[] { value };
    }

    public <U> Some<U> map(Function<? super T, ? extends U> function) {
        return new Some<>(function.apply(value));
    }

    public <U> U mapOr(U fallback, Function<? super T, ? extends U> function) {
        return function.apply(value);
    }

    public <U> U mapOrElse(Supplier<U> fallback, Function<? super T, ? extends U> function) {
        return function.apply(value);
    }

    public Some<T> inspect(Consumer<? super T> inspector) {
        inspector.accept(value);
        return this;
    }

    public <U> Option<U> and(Option<U> other) {
        return other;
    }

    public <U> Option<U> andAndCopy(Option<? extends U> other) {
        return Option.copy(other);
    }

    public <U> Option<U> andThen(Supplier<Option<U>> other) {
        return other.get();
    }

    public <U> Option<U> andThenAndCopy(Supplier<Option<? extends U>> other) {
        return Option.copy(other.get());
    }

    public Option<T> filter(Predicate<? super T> predicate) {
        if (predicate.test(value)) {
            return this;
        } else {
            return new None<>();
        }
    }

    public Option<T> filterCopying(Predicate<? super T> predicate) {
        if (predicate.test(value)) {
            return Option.copy(this);
        } else {
            return new None<>();
        }
    }

    public Some<T> or(Option<T> other) {
        return this;
    }

    public Option<T> orCopying(Option<T> other) {
        return Option.copy(this);
    }

    public Option<T> orAndCopy(Option<? extends T> other) {
        return Option.copy(this);
    }

    public Some<T> orElse(Supplier<Option<T>> other) {
        return this;
    }

    public Option<T> orElseCopying(Supplier<Option<T>> other) {
        return Option.copy(this);
    }

    public Option<T> orElseAndCopy(Supplier<Option<? extends T>> other) {
        return Option.copy(this);
    }

    public Option<T> xor(Option<T> other) {
        if (other.isNone()) {
            return this;
        } else {
            return new None<>();
        }
    }

    public Option<T> xorCopying(Option<T> other) {
        if (other.isNone()) {
            return Option.copy(this);
        } else {
            return new None<>();
        }
    }

    public Option<T> xorAndCopy(Option<? extends T> other) {
        if (other.isNone()) {
            return Option.copy(this);
        } else {
            return new None<>();
        }
    }

    public Some<T> replace(T value) {
        var oldValue = this.value;
        this.value = value;
        return new Some<>(oldValue);
    }

    public <U> Option<Zip<T, U>> zip(Option<U> other) {
        if (other.isSome()) {
            return new Some<>(new Zip<>(value, other.unwrapOrNull()));
        } else {
            return new None<>();
        }
    }

    public <U, V> Option<V> zipWith(BiFunction<? super T, U, V> function, Option<U> other) {
        if (other.isSome()) {
            return new Some<>(function.apply(value, other.unwrapOrNull()));
        } else {
            return new None<>();
        }
    }

    public Iter iterator() {
        return new Iter(value);
    }

    @Override
    public Spliter spliterator() {
        return new Spliter(value);
    }

    private class Iter implements Iterator<T> {
        private T value;
        private boolean done;

        public Iter(T value) {
            this.value = value;
            done = false;
        }

        public boolean hasNext() {
            return !done;
        }

        public T next() {
            if (done) {
                throw new NoSuchElementException();
            } else {
                done = true;
                return value;
            }
        }
    }

    private class Spliter implements Spliterator<T> {
        private T value;
        private boolean done;

        public Spliter(T value) {
            this.value = value;
            done = false;
        }

        public int characteristics() {
            return Spliterator.CONCURRENT
                    | Spliterator.DISTINCT
                    | Spliterator.SIZED
                    | Spliterator.ORDERED
                    | Spliterator.SUBSIZED;
        }

        public long estimateSize() {
            return done ? 0 : 1;
        }

        @Override
        public long getExactSizeIfKnown() {
            return done ? 0 : 1;
        }

        public boolean tryAdvance(Consumer<? super T> action) {
            if (done) {
                return false;
            } else {
                done = true;
                action.accept(value);
                return true;
            }
        }

        public Spliter trySplit() {
            if (done) {
                return null;
            }
            done = true;
            return new Spliter(value);
        }
    }
}
