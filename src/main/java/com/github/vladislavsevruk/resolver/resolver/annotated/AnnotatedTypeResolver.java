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
package com.github.vladislavsevruk.resolver.resolver.annotated;

import com.github.vladislavsevruk.resolver.type.TypeVariableMap;

import java.lang.reflect.AnnotatedType;

/**
 * Resolves actual types for generic parameters of annotated type.
 *
 * @param <T> type of mapped value for type variable.
 */
public interface AnnotatedTypeResolver<T> {

    /**
     * Checks if type resolver can deal with specific annotated type.
     *
     * @param annotatedType <code>AnnotatedType</code> to check.
     * @return <code>true</code> if type resolver can resolve actual types for received annotated type,
     * <code>false</code> otherwise.
     */
    boolean canResolve(AnnotatedType annotatedType);

    /**
     * Resolves actual type for received annotated type and its generic parameters.
     *
     * @param typeVariableMap <code>TypeVariableMap</code> with declared class type variables.
     * @param annotatedType   <code>AnnotatedType</code> to resolve.
     * @return actual type for received type.
     */
    T resolve(TypeVariableMap<T> typeVariableMap, AnnotatedType annotatedType);
}
