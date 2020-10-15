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
package com.github.vladislavsevruk.resolver.resolver.simple;

import com.github.vladislavsevruk.resolver.type.TypeVariableMap;

import java.lang.reflect.Type;

/**
 * Resolves actual types for generic parameters.
 *
 * @param <T> type of mapped value for type variable.
 */
public interface TypeResolver<T> {

    /**
     * Checks if type resolver can deal with specific type.
     *
     * @param type <code>Type</code> to check.
     * @return <code>true</code> if type resolver can resolve actual types for received type, <code>false</code>
     * otherwise.
     */
    boolean canResolve(Type type);

    /**
     * Resolves actual type for received type and its generic parameters.
     *
     * @param typeVariableMap <code>TypeVariableMap</code> with declared class type variables.
     * @param type            <code>Type</code> to resolve.
     * @return actual type for received type.
     */
    T resolve(TypeVariableMap<T> typeVariableMap, Type type);
}
