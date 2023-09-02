package com.anselmschueler.joption;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public sealed interface Option<T> extends Iterable<T> permits None, Some {
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

    public <U> Option<U> map(Function<? super T, ? extends U> function);

    public <U> U mapOr(U fallback, Function<? super T, ? extends U> function);

    public <U> U mapOrElse(Supplier<U> fallback, Function<? super T, ? extends U> function);

    public Option<T> inspect(Consumer<? super T> inspector);

    public <U> Option<U> and(Option<U> other);

    public <U> Option<U> andAndCopy(Option<? extends U> other);

    public <U> Option<U> andThen(Supplier<Option<U>> other);

    public <U> Option<U> andThenAndCopy(Supplier<Option<? extends U>> other);

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

    public <U> Option<Zip<T, U>> zip(Option<U> other);

    public <U, V> Option<V> zipWith(BiFunction<? super T, U, V> function, Option<U> other);

    public static <T> Option<T> copy(Option<? extends T> other) {
        if (other instanceof None) {
            return new None<>();
        } else {
            return new Some<>(other.unwrapOrNull());
        }
    }

    public static <T> Option<T> flatten(Option<Option<T>> nested) {
        return nested.unwrapOrElse(() -> new None<>());
    }

    public static <T, U> Unzip<T, U> unzip(Option<Zip<T, U>> zipped) {
        if (zipped.isSome()) {
            var value = zipped.unwrapOrNull();
            return new Unzip<>(new Some<>(value.left()), new Some<>(value.right()));
        } else {
            return new Unzip<>(new None<>(), new None<>());
        }
    }

    public static <T> Option<T> getOrNewSome(Option<T> that, T value) {
        if (that.isSome()) {
            return that;
        } else {
            return new Some<>(value);
        }
    }

    public static <T> Option<T> getOrNewSomeWith(Option<T> that, Supplier<T> value) {
        if (that.isSome()) {
            return that;
        } else {
            return new Some<>(value.get());
        }
    }
}
