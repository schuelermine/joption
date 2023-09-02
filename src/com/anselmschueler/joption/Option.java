package com.anselmschueler.joption;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.Iterator;
import java.util.NoSuchElementException;

public sealed interface Option<T> extends Iterable<T> permits Option.None, Option.Some {
    public enum Never {}
    public enum Variant {
        SOME,
        NONE
    }
    public class NoneOptionException extends Exception {
        public NoneOptionException() {
            super();
        }
        public NoneOptionException(String reason) {
            super(reason + " - attempt to get underlying value of Option variant None");
        }
    }
    record Zip<T, U>(T left, U right) {}
    record Unzip<T, U>(Option<T> left, Option<U> right) {}

    public T unwrap() throws NoneOptionException;
    public T unwrapOrNull();
    public T unwrapOr(T fallback);
    public T unwrapOrElse(Supplier<? extends T> fallback);
    public T expect(String reason) throws NoneOptionException;
    public Variant variant();
    public boolean isNone();
    public boolean isSome();
    public boolean isSomeAnd(Predicate<? super T> predicate);
    public Object[] asArray();
    public<U> Option<U> map(Function<? super T, ? extends U> function);
    public<U> U mapOr(U fallback, Function<? super T, ? extends U> function);
    public<U> U mapOrElse(Supplier<U> fallback, Function<? super T, ? extends U> function);
    public Option<T> inspect(Consumer<? super T> inspector);
    public<U> Option<U> and(Option<U> other);
    public<U> Option<U> andAndCopy(Option<? extends U> other);
    public<U> Option<U> andThen(Supplier<Option<U>> other);
    public<U> Option<U> andThenAndCopy(Supplier<Option<? extends U>> other);
    public Option<T> filter(Predicate<? super T> predicate);
    public Option<T> filterCopying(Predicate<? super T> predicate);
    public Option<T> or(Option<T> other);
    public Option<T> orCopying(Option<T> other);
    public Option<T> orAndCopy(Option<? extends T> other);
    public Option<T> orElse(Supplier<Option<T>> other);
    public Option<T> orElseCopying(Supplier<Option<T>> other);
    public Option<T> orElseAndCopy(Supplier<Option<? extends T>> other);
    public Option<T> xor(Option<T> other);
    public Option<T> xorCopying(Option<T> other);
    public Option<T> xorAndCopy(Option<? extends T> other);
    public Option<T> replace(T value);
    public<U> Option<Zip<T, U>> zip(Option<U> other);
    public<U, V> Option<V> zipWith(BiFunction<? super T, U, V> function, Option<U> other);

    public static<T> Option<T> copy(Option<? extends T> other) {
        if (other instanceof None) {
            return new None<>();
        } else {
            return new Some<>(other.unwrapOrNull());
        }
    }
    public static<T> Option<T> flatten(Option<Option<T>> nested) {
        return nested.unwrapOrElse(() -> new None<>());
    }
    public static<T, U> Unzip<T, U> unzip(Option<Zip<T, U>> zipped) {
        if (zipped.isSome()) {
            var value = zipped.unwrapOrNull();
            return new Unzip<>(new Some<>(value.left), new Some<>(value.right));
        } else {
            return new Unzip<>(new None<>(), new None<>());
        }
    }
    public static<T> Option<T> getOrNewSome(Option<T> that, T value) {
        if (that.isSome()) {
            return that;
        } else {
            return new Some<>(value);
        }
    }
    public static<T> Option<T> getOrNewSomeWith(Option<T> that, Supplier<T> value) {
        if (that.isSome()) {
            return that;
        } else {
            return new Some<T>(value.get());
        }
    }

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
        public<U> None<U> map(Function<? super T, ? extends U> function) {
            return new None<>();
        }
        public<U> U mapOr(U fallback, Function<? super T, ? extends U> function) {
            return fallback;
        }
        public<U> U mapOrElse(Supplier<U> fallback, Function<? super T, ? extends U> function) {
            return fallback.get();
        }
        public None<T> inspect(Consumer<? super T> inspector) {
            return this;
        }
        public<U> None<U> and(Option<U> other) {
            return new None<>();
        }
        public<U> None<U> andAndCopy(Option<? extends U> other) {
            return new None<>();
        }
        public<U> None<U> andThen(Supplier<Option<U>> other) {
            return new None<>();
        }
        public<U> None<U> andThenAndCopy(Supplier<Option<? extends U>> other) {
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
        public<U> Option<Zip<T, U>> zip(Option<U> other) {
            return new None<>();
        }
        public<U, V> None<V> zipWith(BiFunction<? super T, U, V> function, Option<U> other) {
            return new None<>();
        }
        public Iter iterator() {
            return new Iter();
        }
        @Override
        public Spliter spliterator() {
            return new Spliter();
        }

        class Iter implements Iterator<T> {
            public boolean hasNext() {
                return false;
            }
            public T next() {
                throw new NoSuchElementException();
            }
        }

        class Spliter implements Spliterator<T> {
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
            return new Object[] {value};
        }
        public<U> Some<U> map(Function<? super T, ? extends U> function) {
            return new Some<>(function.apply(value));
        }
        public<U> U mapOr(U fallback, Function<? super T, ? extends U> function) {
            return function.apply(value);
        }
        public<U> U mapOrElse(Supplier<U> fallback, Function<? super T, ? extends U> function) {
            return function.apply(value);
        }
        public Some<T> inspect(Consumer<? super T> inspector) {
            inspector.accept(value);
            return this;
        }
        public<U> Option<U> and(Option<U> other) {
            return other;
        }
        public<U> Option<U> andAndCopy(Option<? extends U> other) {
            return Option.copy(other);
        }
        public<U> Option<U> andThen(Supplier<Option<U>> other) {
            return other.get();
        }
        public<U> Option<U> andThenAndCopy(Supplier<Option<? extends U>> other) {
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
        public<U> Option<Zip<T, U>> zip(Option<U> other) {
            if (other.isSome()) {
                return new Some<>(new Zip<>(value, other.unwrapOrNull()));
            } else {
                return new None<>();
            }
        }
        public<U, V> Option<V> zipWith(BiFunction<? super T, U, V> function, Option<U> other) {
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

        class Iter implements Iterator<T> {
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

        class Spliter implements Spliterator<T> {
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
}
