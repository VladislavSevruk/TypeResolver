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
package com.github.vladislavsevruk.resolver.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility methods for wrapping primitive class to wrapper class and vice versa.
 */
public final class PrimitiveWrapperUtil {

    private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS = getPrimitivesToWrappersMap();
    private static final Map<Class<?>, Class<?>> WRAPPERS_TO_PRIMITIVES = getWrappersToPrimitivesMap();

    private PrimitiveWrapperUtil() {
    }

    /**
     * Checks if received class is primitive class wrapper.
     *
     * @param clazz <code>Class</code> to check.
     * @return <code>true</code> if received class represents primitive class wrapper, <code>false</code> otherwise.
     */
    public static boolean isWrapper(Class<?> clazz) {
        return WRAPPERS_TO_PRIMITIVES.containsKey(clazz);
    }

    /**
     * Unwraps wrapper by matching primitive class.
     *
     * @param clazz <code>Class</code> to unwrap.
     * @param <T>   the type of the class.
     * @return matching primitive class if received class is wrapper or received class otherwise.
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> unwrap(Class<T> clazz) {
        return isWrapper(clazz) ? (Class<T>) WRAPPERS_TO_PRIMITIVES.get(clazz) : clazz;
    }

    /**
     * Wraps primitive class by matching wrapper.
     *
     * @param clazz <code>Class</code> to wrap.
     * @param <T>   the type of the class.
     * @return matching wrapper class if received class is primitive or received class otherwise.
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> wrap(Class<T> clazz) {
        return clazz.isPrimitive() ? (Class<T>) PRIMITIVES_TO_WRAPPERS.get(clazz) : clazz;
    }

    private static Map<Class<?>, Class<?>> getPrimitivesToWrappersMap() {
        Map<Class<?>, Class<?>> primitivesToWrappers = new HashMap<>();
        primitivesToWrappers.put(boolean.class, Boolean.class);
        primitivesToWrappers.put(byte.class, Byte.class);
        primitivesToWrappers.put(char.class, Character.class);
        primitivesToWrappers.put(double.class, Double.class);
        primitivesToWrappers.put(float.class, Float.class);
        primitivesToWrappers.put(int.class, Integer.class);
        primitivesToWrappers.put(long.class, Long.class);
        primitivesToWrappers.put(short.class, Short.class);
        primitivesToWrappers.put(void.class, Void.class);
        return Collections.unmodifiableMap(primitivesToWrappers);
    }

    private static Map<Class<?>, Class<?>> getWrappersToPrimitivesMap() {
        Map<Class<?>, Class<?>> wrappersToPrimitives = new HashMap<>();
        wrappersToPrimitives.put(Boolean.class, boolean.class);
        wrappersToPrimitives.put(Byte.class, byte.class);
        wrappersToPrimitives.put(Character.class, char.class);
        wrappersToPrimitives.put(Double.class, double.class);
        wrappersToPrimitives.put(Float.class, float.class);
        wrappersToPrimitives.put(Integer.class, int.class);
        wrappersToPrimitives.put(Long.class, long.class);
        wrappersToPrimitives.put(Short.class, short.class);
        wrappersToPrimitives.put(Void.class, void.class);
        return Collections.unmodifiableMap(wrappersToPrimitives);
    }
}
