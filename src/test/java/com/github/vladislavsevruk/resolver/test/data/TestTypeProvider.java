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

import org.mockito.Mockito;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * Provides types for test.
 */
public class TestTypeProvider {

    private static final AnnotatedType ANNOTATED_ARRAY_TYPE = Mockito.mock(AnnotatedArrayType.class);
    private static final AnnotatedType ANNOTATED_PARAMETERIZED_TYPE = Mockito.mock(AnnotatedParameterizedType.class);
    private static final AnnotatedType ANNOTATED_TYPE_VARIABLE = Mockito.mock(AnnotatedTypeVariable.class);
    private static final AnnotatedType ANNOTATED_WILDCARD_TYPE = Mockito.mock(AnnotatedWildcardType.class);
    private static final Type ARRAY_TYPE = Boolean[].class;
    private static final Type CLASS_TYPE = String.class;
    private static final Type GENERIC_ARRAY_TYPE = Mockito.mock(GenericArrayType.class);
    private static final Type PARAMETERIZED_TYPE = Mockito.mock(ParameterizedType.class);
    private static final Type TYPE_VARIABLE = Mockito.mock(TypeVariable.class);
    private static final Type WILDCARD_TYPE = Mockito.mock(WildcardType.class);

    public static AnnotatedType annotatedArrayType() {
        return ANNOTATED_ARRAY_TYPE;
    }

    public static AnnotatedType annotatedParameterizedType() {
        return ANNOTATED_PARAMETERIZED_TYPE;
    }

    public static AnnotatedType annotatedTypeVariable() {
        return ANNOTATED_TYPE_VARIABLE;
    }

    public static AnnotatedType annotatedWildcardType() {
        return ANNOTATED_WILDCARD_TYPE;
    }

    public static Type arrayType() {
        return ARRAY_TYPE;
    }

    public static Type classType() {
        return CLASS_TYPE;
    }

    public static Type genericArrayType() {
        return GENERIC_ARRAY_TYPE;
    }

    public static Type parameterizedType() {
        return PARAMETERIZED_TYPE;
    }

    public static Type typeVariable() {
        return TYPE_VARIABLE;
    }

    public static Type wildcardType() {
        return WILDCARD_TYPE;
    }
}
