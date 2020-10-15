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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class PrimitiveWrapperUtilTest {

    @Test
    void checkBooleanPrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Boolean.TYPE));
    }

    @Test
    void checkBooleanWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Boolean.class));
    }

    @Test
    void checkBytePrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Byte.TYPE));
    }

    @Test
    void checkByteWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Byte.class));
    }

    @Test
    void checkCharPrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Character.TYPE));
    }

    @Test
    void checkCharWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Character.class));
    }

    @Test
    void checkDoublePrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Double.TYPE));
    }

    @Test
    void checkDoubleWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Double.class));
    }

    @Test
    void checkFloatPrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Float.TYPE));
    }

    @Test
    void checkFloatWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Float.class));
    }

    @Test
    void checkIntegerPrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Integer.TYPE));
    }

    @Test
    void checkIntegerWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Integer.class));
    }

    @Test
    void checkLongPrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Long.TYPE));
    }

    @Test
    void checkLongWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Long.class));
    }

    @Test
    void checkNonWrapperClassTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(ArrayList.class));
    }

    @Test
    void checkShortPrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Short.TYPE));
    }

    @Test
    void checkShortWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Short.class));
    }

    @Test
    void checkVoidPrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Void.TYPE));
    }

    @Test
    void checkVoidWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Void.class));
    }

    @Test
    void unwrapBooleanPrimitiveTest() {
        Assertions.assertEquals(Boolean.TYPE, PrimitiveWrapperUtil.unwrap(Boolean.TYPE));
    }

    @Test
    void unwrapBooleanWrapperTest() {
        Assertions.assertEquals(Boolean.TYPE, PrimitiveWrapperUtil.unwrap(Boolean.class));
    }

    @Test
    void unwrapBytePrimitiveTest() {
        Assertions.assertEquals(Byte.TYPE, PrimitiveWrapperUtil.unwrap(Byte.TYPE));
    }

    @Test
    void unwrapByteWrapperTest() {
        Assertions.assertEquals(Byte.TYPE, PrimitiveWrapperUtil.unwrap(Byte.class));
    }

    @Test
    void unwrapCharPrimitiveTest() {
        Assertions.assertEquals(Character.TYPE, PrimitiveWrapperUtil.unwrap(Character.TYPE));
    }

    @Test
    void unwrapCharWrapperTest() {
        Assertions.assertEquals(Character.TYPE, PrimitiveWrapperUtil.unwrap(Character.class));
    }

    @Test
    void unwrapDoublePrimitiveTest() {
        Assertions.assertEquals(Double.TYPE, PrimitiveWrapperUtil.unwrap(Double.TYPE));
    }

    @Test
    void unwrapDoubleWrapperTest() {
        Assertions.assertEquals(Double.TYPE, PrimitiveWrapperUtil.unwrap(Double.class));
    }

    @Test
    void unwrapFloatPrimitiveTest() {
        Assertions.assertEquals(Float.TYPE, PrimitiveWrapperUtil.unwrap(Float.TYPE));
    }

    @Test
    void unwrapFloatWrapperTest() {
        Assertions.assertEquals(Float.TYPE, PrimitiveWrapperUtil.unwrap(Float.class));
    }

    @Test
    void unwrapIntegerPrimitiveTest() {
        Assertions.assertEquals(Integer.TYPE, PrimitiveWrapperUtil.unwrap(Integer.TYPE));
    }

    @Test
    void unwrapIntegerWrapperTest() {
        Assertions.assertEquals(Integer.TYPE, PrimitiveWrapperUtil.unwrap(Integer.class));
    }

    @Test
    void unwrapLongPrimitiveTest() {
        Assertions.assertEquals(Long.TYPE, PrimitiveWrapperUtil.unwrap(Long.TYPE));
    }

    @Test
    void unwrapLongWrapperTest() {
        Assertions.assertEquals(Long.TYPE, PrimitiveWrapperUtil.unwrap(Long.class));
    }

    @Test
    void unwrapNonWrapperClassTest() {
        Assertions.assertEquals(HashMap.class, PrimitiveWrapperUtil.unwrap(HashMap.class));
    }

    @Test
    void unwrapShortPrimitiveTest() {
        Assertions.assertEquals(Short.TYPE, PrimitiveWrapperUtil.unwrap(Short.TYPE));
    }

    @Test
    void unwrapShortWrapperTest() {
        Assertions.assertEquals(Short.TYPE, PrimitiveWrapperUtil.unwrap(Short.class));
    }

    @Test
    void unwrapVoidPrimitiveTest() {
        Assertions.assertEquals(Void.TYPE, PrimitiveWrapperUtil.unwrap(Void.TYPE));
    }

    @Test
    void unwrapVoidWrapperTest() {
        Assertions.assertEquals(Void.TYPE, PrimitiveWrapperUtil.unwrap(Void.class));
    }

    @Test
    void wrapBooleanPrimitiveTest() {
        Assertions.assertEquals(Boolean.class, PrimitiveWrapperUtil.wrap(Boolean.TYPE));
    }

    @Test
    void wrapBooleanWrapperTest() {
        Assertions.assertEquals(Boolean.class, PrimitiveWrapperUtil.wrap(Boolean.class));
    }

    @Test
    void wrapBytePrimitiveTest() {
        Assertions.assertEquals(Byte.class, PrimitiveWrapperUtil.wrap(Byte.TYPE));
    }

    @Test
    void wrapByteWrapperTest() {
        Assertions.assertEquals(Byte.class, PrimitiveWrapperUtil.wrap(Byte.class));
    }

    @Test
    void wrapCharPrimitiveTest() {
        Assertions.assertEquals(Character.class, PrimitiveWrapperUtil.wrap(Character.TYPE));
    }

    @Test
    void wrapCharWrapperTest() {
        Assertions.assertEquals(Character.class, PrimitiveWrapperUtil.wrap(Character.class));
    }

    @Test
    void wrapDoublePrimitiveTest() {
        Assertions.assertEquals(Double.class, PrimitiveWrapperUtil.wrap(Double.TYPE));
    }

    @Test
    void wrapDoubleWrapperTest() {
        Assertions.assertEquals(Double.class, PrimitiveWrapperUtil.wrap(Double.class));
    }

    @Test
    void wrapFloatPrimitiveTest() {
        Assertions.assertEquals(Float.class, PrimitiveWrapperUtil.wrap(Float.TYPE));
    }

    @Test
    void wrapFloatWrapperTest() {
        Assertions.assertEquals(Float.class, PrimitiveWrapperUtil.wrap(Float.class));
    }

    @Test
    void wrapIntegerPrimitiveTest() {
        Assertions.assertEquals(Integer.class, PrimitiveWrapperUtil.wrap(Integer.TYPE));
    }

    @Test
    void wrapIntegerWrapperTest() {
        Assertions.assertEquals(Integer.class, PrimitiveWrapperUtil.wrap(Integer.class));
    }

    @Test
    void wrapLongPrimitiveTest() {
        Assertions.assertEquals(Long.class, PrimitiveWrapperUtil.wrap(Long.TYPE));
    }

    @Test
    void wrapLongWrapperTest() {
        Assertions.assertEquals(Long.class, PrimitiveWrapperUtil.wrap(Long.class));
    }

    @Test
    void wrapNonWrapperClassTest() {
        Assertions.assertEquals(HashSet.class, PrimitiveWrapperUtil.wrap(HashSet.class));
    }

    @Test
    void wrapShortPrimitiveTest() {
        Assertions.assertEquals(Short.class, PrimitiveWrapperUtil.wrap(Short.TYPE));
    }

    @Test
    void wrapShortWrapperTest() {
        Assertions.assertEquals(Short.class, PrimitiveWrapperUtil.wrap(Short.class));
    }

    @Test
    void wrapVoidPrimitiveTest() {
        Assertions.assertEquals(Void.class, PrimitiveWrapperUtil.wrap(Void.TYPE));
    }

    @Test
    void wrapVoidWrapperTest() {
        Assertions.assertEquals(Void.class, PrimitiveWrapperUtil.wrap(Void.class));
    }
}
