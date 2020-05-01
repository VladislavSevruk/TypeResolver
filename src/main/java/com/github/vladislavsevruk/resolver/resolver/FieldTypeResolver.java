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
package com.github.vladislavsevruk.resolver.resolver;

import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;

import java.lang.reflect.Field;

/**
 * Resolves actual types for generic fields.
 */
public interface FieldTypeResolver {

    /**
     * Resolves actual types for generic fields.
     *
     * @param clazz <code>Class</code> where field was declared.
     * @param field <code>Field</code> to resolve actual return type for.
     * @return <code>TypeMeta</code> with actual field type.
     */
    @SuppressWarnings("java:S1452")
    TypeMeta<?> resolveField(Class<?> clazz, Field field);

    /**
     * Resolves actual types for generic fields.
     *
     * @param typeMeta <code>TypeMeta</code> with actual types for class where field was declared.
     * @param field    <code>Field</code> to resolve actual return type for.
     * @return <code>TypeMeta</code> with actual field type.
     */
    @SuppressWarnings("java:S1452")
    TypeMeta<?> resolveField(TypeMeta<?> typeMeta, Field field);

    /**
     * Resolves actual types for generic fields.
     *
     * @param typeProvider <code>TypeProvider</code> with class where field was declared as type parameter.
     * @param field        <code>Field</code> to resolve actual return type for.
     * @return <code>TypeMeta</code> with actual field type.
     */
    @SuppressWarnings("java:S1452")
    TypeMeta<?> resolveField(TypeProvider<?> typeProvider, Field field);
}
