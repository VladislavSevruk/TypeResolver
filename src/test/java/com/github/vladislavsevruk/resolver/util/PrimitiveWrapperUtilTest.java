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

public class PrimitiveWrapperUtilTest {

    @Test
    public void checkBooleanPrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Boolean.TYPE));
    }

    @Test
    public void checkBooleanWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Boolean.class));
    }

    @Test
    public void checkBytePrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Byte.TYPE));
    }

    @Test
    public void checkByteWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Byte.class));
    }

    @Test
    public void checkCharPrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Character.TYPE));
    }

    @Test
    public void checkCharWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Character.class));
    }

    @Test
    public void checkDoublePrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Double.TYPE));
    }

    @Test
    public void checkDoubleWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Double.class));
    }

    @Test
    public void checkFloatPrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Float.TYPE));
    }

    @Test
    public void checkFloatWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Float.class));
    }

    @Test
    public void checkIntegerPrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Integer.TYPE));
    }

    @Test
    public void checkIntegerWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Integer.class));
    }

    @Test
    public void checkLongPrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Long.TYPE));
    }

    @Test
    public void checkLongWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Long.class));
    }

    @Test
    public void checkNonWrapperClassTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(ArrayList.class));
    }

    @Test
    public void checkShortPrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Short.TYPE));
    }

    @Test
    public void checkShortWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Short.class));
    }

    @Test
    public void checkVoidPrimitiveTest() {
        Assertions.assertFalse(PrimitiveWrapperUtil.isWrapper(Void.TYPE));
    }

    @Test
    public void checkVoidWrapperTest() {
        Assertions.assertTrue(PrimitiveWrapperUtil.isWrapper(Void.class));
    }

    @Test
    public void unwrapBooleanPrimitiveTest() {
        Assertions.assertEquals(Boolean.TYPE, PrimitiveWrapperUtil.unwrap(Boolean.TYPE));
    }

    @Test
    public void unwrapBooleanWrapperTest() {
        Assertions.assertEquals(Boolean.TYPE, PrimitiveWrapperUtil.unwrap(Boolean.class));
    }

    @Test
    public void unwrapBytePrimitiveTest() {
        Assertions.assertEquals(Byte.TYPE, PrimitiveWrapperUtil.unwrap(Byte.TYPE));
    }

    @Test
    public void unwrapByteWrapperTest() {
        Assertions.assertEquals(Byte.TYPE, PrimitiveWrapperUtil.unwrap(Byte.class));
    }

    @Test
    public void unwrapCharPrimitiveTest() {
        Assertions.assertEquals(Character.TYPE, PrimitiveWrapperUtil.unwrap(Character.TYPE));
    }

    @Test
    public void unwrapCharWrapperTest() {
        Assertions.assertEquals(Character.TYPE, PrimitiveWrapperUtil.unwrap(Character.class));
    }

    @Test
    public void unwrapDoublePrimitiveTest() {
        Assertions.assertEquals(Double.TYPE, PrimitiveWrapperUtil.unwrap(Double.TYPE));
    }

    @Test
    public void unwrapDoubleWrapperTest() {
        Assertions.assertEquals(Double.TYPE, PrimitiveWrapperUtil.unwrap(Double.class));
    }

    @Test
    public void unwrapFloatPrimitiveTest() {
        Assertions.assertEquals(Float.TYPE, PrimitiveWrapperUtil.unwrap(Float.TYPE));
    }

    @Test
    public void unwrapFloatWrapperTest() {
        Assertions.assertEquals(Float.TYPE, PrimitiveWrapperUtil.unwrap(Float.class));
    }

    @Test
    public void unwrapIntegerPrimitiveTest() {
        Assertions.assertEquals(Integer.TYPE, PrimitiveWrapperUtil.unwrap(Integer.TYPE));
    }

    @Test
    public void unwrapIntegerWrapperTest() {
        Assertions.assertEquals(Integer.TYPE, PrimitiveWrapperUtil.unwrap(Integer.class));
    }

    @Test
    public void unwrapLongPrimitiveTest() {
        Assertions.assertEquals(Long.TYPE, PrimitiveWrapperUtil.unwrap(Long.TYPE));
    }

    @Test
    public void unwrapLongWrapperTest() {
        Assertions.assertEquals(Long.TYPE, PrimitiveWrapperUtil.unwrap(Long.class));
    }

    @Test
    public void unwrapNonWrapperClassTest() {
        Assertions.assertEquals(HashMap.class, PrimitiveWrapperUtil.unwrap(HashMap.class));
    }

    @Test
    public void unwrapShortPrimitiveTest() {
        Assertions.assertEquals(Short.TYPE, PrimitiveWrapperUtil.unwrap(Short.TYPE));
    }

    @Test
    public void unwrapShortWrapperTest() {
        Assertions.assertEquals(Short.TYPE, PrimitiveWrapperUtil.unwrap(Short.class));
    }

    @Test
    public void unwrapVoidPrimitiveTest() {
        Assertions.assertEquals(Void.TYPE, PrimitiveWrapperUtil.unwrap(Void.TYPE));
    }

    @Test
    public void unwrapVoidWrapperTest() {
        Assertions.assertEquals(Void.TYPE, PrimitiveWrapperUtil.unwrap(Void.class));
    }

    @Test
    public void wrapBooleanPrimitiveTest() {
        Assertions.assertEquals(Boolean.class, PrimitiveWrapperUtil.wrap(Boolean.TYPE));
    }

    @Test
    public void wrapBooleanWrapperTest() {
        Assertions.assertEquals(Boolean.class, PrimitiveWrapperUtil.wrap(Boolean.class));
    }

    @Test
    public void wrapBytePrimitiveTest() {
        Assertions.assertEquals(Byte.class, PrimitiveWrapperUtil.wrap(Byte.TYPE));
    }

    @Test
    public void wrapByteWrapperTest() {
        Assertions.assertEquals(Byte.class, PrimitiveWrapperUtil.wrap(Byte.class));
    }

    @Test
    public void wrapCharPrimitiveTest() {
        Assertions.assertEquals(Character.class, PrimitiveWrapperUtil.wrap(Character.TYPE));
    }

    @Test
    public void wrapCharWrapperTest() {
        Assertions.assertEquals(Character.class, PrimitiveWrapperUtil.wrap(Character.class));
    }

    @Test
    public void wrapDoublePrimitiveTest() {
        Assertions.assertEquals(Double.class, PrimitiveWrapperUtil.wrap(Double.TYPE));
    }

    @Test
    public void wrapDoubleWrapperTest() {
        Assertions.assertEquals(Double.class, PrimitiveWrapperUtil.wrap(Double.class));
    }

    @Test
    public void wrapFloatPrimitiveTest() {
        Assertions.assertEquals(Float.class, PrimitiveWrapperUtil.wrap(Float.TYPE));
    }

    @Test
    public void wrapFloatWrapperTest() {
        Assertions.assertEquals(Float.class, PrimitiveWrapperUtil.wrap(Float.class));
    }

    @Test
    public void wrapIntegerPrimitiveTest() {
        Assertions.assertEquals(Integer.class, PrimitiveWrapperUtil.wrap(Integer.TYPE));
    }

    @Test
    public void wrapIntegerWrapperTest() {
        Assertions.assertEquals(Integer.class, PrimitiveWrapperUtil.wrap(Integer.class));
    }

    @Test
    public void wrapLongPrimitiveTest() {
        Assertions.assertEquals(Long.class, PrimitiveWrapperUtil.wrap(Long.TYPE));
    }

    @Test
    public void wrapLongWrapperTest() {
        Assertions.assertEquals(Long.class, PrimitiveWrapperUtil.wrap(Long.class));
    }

    @Test
    public void wrapNonWrapperClassTest() {
        Assertions.assertEquals(HashSet.class, PrimitiveWrapperUtil.wrap(HashSet.class));
    }

    @Test
    public void wrapShortPrimitiveTest() {
        Assertions.assertEquals(Short.class, PrimitiveWrapperUtil.wrap(Short.TYPE));
    }

    @Test
    public void wrapShortWrapperTest() {
        Assertions.assertEquals(Short.class, PrimitiveWrapperUtil.wrap(Short.class));
    }

    @Test
    public void wrapVoidPrimitiveTest() {
        Assertions.assertEquals(Void.class, PrimitiveWrapperUtil.wrap(Void.TYPE));
    }

    @Test
    public void wrapVoidWrapperTest() {
        Assertions.assertEquals(Void.class, PrimitiveWrapperUtil.wrap(Void.class));
    }
}
