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
package com.github.vladislavsevruk.resolver.resolver.annotated.parameterized;

import com.github.vladislavsevruk.resolver.resolver.annotated.AnnotatedTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.picker.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Contains common logic for resolving actual types for annotated parameterized types.
 *
 * @param <T> type of mapped value for type variable.
 */
@Log4j2
public abstract class AbstractAnnotatedParameterizedTypeResolver<T> implements AnnotatedTypeResolver<T> {

    private TypeResolverPicker<T> typeResolverPicker;

    protected AbstractAnnotatedParameterizedTypeResolver(TypeResolverPicker<T> typeResolverPicker) {
        this.typeResolverPicker = typeResolverPicker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canResolve(AnnotatedType annotatedType) {
        return AnnotatedParameterizedType.class.isAssignableFrom(annotatedType.getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T resolve(TypeVariableMap<T> typeVariableMap, AnnotatedType type) {
        log.debug(() -> "Resolving AnnotatedParameterizedType.");
        AnnotatedParameterizedType annotatedType = (AnnotatedParameterizedType) type;
        AnnotatedType[] actualArguments = annotatedType.getAnnotatedActualTypeArguments();
        Type rawType = ((ParameterizedType) annotatedType.getType()).getRawType();
        T[] actualParameters = resolveParameterizedTypes(typeVariableMap, actualArguments);
        return createResolvedParameterizedType((Class<?>) rawType, actualParameters);
    }

    protected abstract T[] createArgumentsArray(int length);

    protected abstract T createResolvedParameterizedType(Class<?> rawType, T[] resolvedArgumentTypes);

    private T[] resolveParameterizedTypes(TypeVariableMap<T> typeVariableMap, AnnotatedType[] actualArguments) {
        T[] argumentTypes = createArgumentsArray(actualArguments.length);
        for (int i = 0; i < actualArguments.length; ++i) {
            Type actualType = actualArguments[i].getType();
            argumentTypes[i] = typeResolverPicker.pickTypeResolver(actualType).resolve(typeVariableMap, actualType);
        }
        return argumentTypes;
    }
}
