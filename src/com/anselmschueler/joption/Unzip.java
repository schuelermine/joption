package com.anselmschueler.joption;

public record Unzip<T, U>(Option<T> left, Option<U> right) {
}
