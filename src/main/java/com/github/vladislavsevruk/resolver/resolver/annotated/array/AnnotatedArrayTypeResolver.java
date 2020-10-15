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
package com.github.vladislavsevruk.resolver.resolver.annotated.array;

import com.github.vladislavsevruk.resolver.resolver.picker.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.type.TypeMeta;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.lang.reflect.Type;

/**
 * Resolves actual types for annotated array types.
 */
public final class AnnotatedArrayTypeResolver extends AbstractAnnotatedArrayTypeResolver<TypeMeta<?>> {

    public AnnotatedArrayTypeResolver(TypeResolverPicker<TypeMeta<?>> typeResolverPicker) {
        super(typeResolverPicker);
    }

    @Override
    protected TypeMeta<?> createResolvedArray(AnnotatedType annotatedType, TypeMeta<?> resolvedComponentType) {
        return new TypeMeta<>(resolveArrayType(annotatedType, resolvedComponentType),
                new TypeMeta<?>[]{ resolvedComponentType });
    }

    private Class<?> getArrayTypeByComponent(TypeMeta<?> actualParameters) {
        return Array.newInstance(actualParameters.getType(), 0).getClass();
    }

    private Class<?> resolveArrayType(AnnotatedType annotatedType, TypeMeta<?> actualParameters) {
        Type arrayType = annotatedType.getType();
        return Class.class.isAssignableFrom(arrayType.getClass()) ? (Class<?>) arrayType
                : getArrayTypeByComponent(actualParameters);
    }
}
