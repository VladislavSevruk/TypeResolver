/*
 * MIT License
 *
 * Copyright (c) 2020 Uladzislau Seuruk
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.vladislavsevruk.resolver.type;

import lombok.Value;

import java.util.Objects;

/**
 * Metadata with actual types values for generic types.
 *
 * @param <T> type of class.
 */
@Value
public class TypeMeta<T> {

    public static final TypeMeta<Object> OBJECT_META = new TypeMeta<>(Object.class);
    public static final TypeMeta<Object> WILDCARD_META = new TypeMeta<>(Object.class, WildcardBound.UPPER);
    TypeMeta<?>[] genericTypes;
    Class<T> type;
    WildcardBound wildcardBound;

    public TypeMeta(Class<T> type) {
        this(type, new TypeMeta<?>[0]);
    }

    public TypeMeta(Class<T> type, TypeMeta<?>[] genericTypes) {
        this(type, genericTypes, null);
    }

    public TypeMeta(Class<T> type, WildcardBound wildcardBound) {
        this(type, new TypeMeta<?>[0], wildcardBound);
    }

    public TypeMeta(Class<T> type, TypeMeta<?>[] genericTypes, WildcardBound wildcardBound) {
        this.type = type;
        this.genericTypes = genericTypes;
        this.wildcardBound = wildcardBound;
    }

    public boolean isWildcard() {
        return Objects.nonNull(wildcardBound);
    }
}
