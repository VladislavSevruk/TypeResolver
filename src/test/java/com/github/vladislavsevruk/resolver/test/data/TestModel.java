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
package com.github.vladislavsevruk.resolver.test.data;

import java.text.ParseException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test model with dummy fields and methods.
 */
public class TestModel<T, U> extends LinkedList<T> /* extend class with single type parameter */ {

    private T genericField;
    private Set<? super U[]> lowerWildcardField;
    private U[] parameterizedArrayField;
    private List<Long[]> parameterizedFieldInnerArray;
    private Set<List<U>> parameterizedFieldInnerParameter;
    private Set<T[]> parameterizedFieldInnerParameterizedArray;
    private Map<Set<U>, List<T>> parameterizedFieldSeveralInnerParameters;
    private Map<U, T> parameterizedFieldSeveralParameters;
    private List<T> parameterizedFieldSingleParameter;
    private int[] primitiveArrayField;
    private long primitiveField;
    private String simpleField;
    private Set<? extends U[]> upperWildcardField;
    private Integer[] wrapperArrayField;
    private Short wrapperField;

    public Double getExceptionType() throws ParseException {
        // dummy
        throw new ParseException("Test", 0);
    }

    public T getGenericReturnType() {
        // dummy
        return null;
    }

    public int getNoParameterType() {
        // dummy
        return 0;
    }

    public T[] getParameterizedArrayReturnType() {
        // dummy
        return null;
    }

    public List<Integer[]> getParameterizedReturnTypeInnerArrayType() {
        // dummy
        return null;
    }

    public List<Set<T>> getParameterizedReturnTypeInnerParameterType() {
        // dummy
        return null;
    }

    public Set<T[]> getParameterizedReturnTypeInnerParameterizedArrayType() {
        // dummy
        return null;
    }

    public Set<Map<T, U>> getParameterizedReturnTypeSeveralInnerParameterTypes() {
        // dummy
        return null;
    }

    public Map<T, U> getParameterizedReturnTypeSeveralParameterTypes() {
        // dummy
        return null;
    }

    public List<T> getParameterizedReturnTypeSingleParameterTypes() {
        // dummy
        return null;
    }

    public void getPrimitiveArrayParameterType(short[] parameter) {
        // dummy
    }

    public int[] getPrimitiveArrayReturnType() {
        // dummy
        return null;
    }

    public int getPrimitiveReturnType() {
        // dummy
        return 0;
    }

    public void getSeveralGenericParameterTypes(T parameter1, U parameter2) {
        // dummy
    }

    public void getSeveralParameterizedParameterTypeSeveralInnerParameterTypes(List<Map<T, U>> parameter1,
            Set<Map.Entry<T, U>> parameter2) {
        // dummy
    }

    public void getSeveralParameterizedParameterTypes(List<T> parameter1, Set<U> parameter2) {
        // dummy
    }

    public void getSeveralSimpleParameterTypes(Character parameter1, int parameter2) {
        // dummy
    }

    public String getSimpleReturnType() {
        // dummy
        return null;
    }

    public void getSingleGenericParameterType(T parameter1) {
        // dummy
    }

    public void getSingleLowerWildcardArrayParameterType(List<? super Number>[] parameter1) {
        // dummy
    }

    public void getSingleParameterizedArrayParameterType(U[] parameter1) {
        // dummy
    }

    public void getSingleParameterizedParameterTypeInnerArrayType(List<Integer[]> parameter1) {
        // dummy
    }

    public void getSingleParameterizedParameterTypeInnerParameterType(Set<List<T>> parameter1) {
        // dummy
    }

    public void getSingleParameterizedParameterTypeInnerParameterizedArrayType(List<T[]> parameter1) {
        // dummy
    }

    public void getSingleParameterizedParameterTypeSeveralInnerParameterTypes(List<Map<T, U>> parameter1) {
        // dummy
    }

    public void getSingleParameterizedParameterTypeSeveralParameterTypes(Map<T, U> parameter1) {
        // dummy
    }

    public void getSingleParameterizedParameterTypeSingleParameterTypes(Collection<T> parameter1) {
        // dummy
    }

    public void getSinglePrimitiveParameterType(int parameter1) {
        // dummy
    }

    public void getSingleSimpleParameterType(Object parameter1) {
        // dummy
    }

    public void getSingleUpperWildcardArrayParameterType(List<? extends Number>[] parameter1) {
        // dummy
    }

    public void getSingleWrapperParameterType(Long parameter1) {
        // dummy
    }

    public void getVoidReturnType() {
        // dummy
    }

    public void getWrapperArrayParameterType(Short[] parameter) {
        // dummy
    }

    public Integer[] getWrapperArrayReturnType() {
        // dummy
        return null;
    }

    public Double getWrapperReturnType() {
        // dummy
        return null;
    }
}
