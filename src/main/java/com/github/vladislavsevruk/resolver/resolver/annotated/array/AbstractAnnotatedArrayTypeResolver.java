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

import com.github.vladislavsevruk.resolver.resolver.annotated.AnnotatedTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.picker.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

/**
 * Contains common logic for resolving actual types for annotated array types.
 *
 * @param <T> type of mapped value for type variable.
 */
@Log4j2
@EqualsAndHashCode(exclude = "typeResolverPicker")
public abstract class AbstractAnnotatedArrayTypeResolver<T> implements AnnotatedTypeResolver<T> {

    private TypeResolverPicker<T> typeResolverPicker;

    protected AbstractAnnotatedArrayTypeResolver(TypeResolverPicker<T> typeResolverPicker) {
        this.typeResolverPicker = typeResolverPicker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canResolve(AnnotatedType annotatedType) {
        return AnnotatedArrayType.class.isAssignableFrom(annotatedType.getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T resolve(TypeVariableMap<T> typeVariableMap, AnnotatedType type) {
        log.debug(() -> "Resolving AnnotatedArrayType.");
        AnnotatedArrayType annotatedType = (AnnotatedArrayType) type;
        AnnotatedType componentType = annotatedType.getAnnotatedGenericComponentType();
        T resolvedComponentType = resolveParameterizedComponentType(typeVariableMap, componentType);
        return createResolvedArray(annotatedType, resolvedComponentType);
    }

    protected abstract T createResolvedArray(AnnotatedType annotatedType, T resolvedComponentType);

    private T resolveParameterizedComponentType(TypeVariableMap<T> typeVariableMap, AnnotatedType componentType) {
        Type actualType = componentType.getType();
        return typeResolverPicker.pickTypeResolver(actualType).resolve(typeVariableMap, actualType);
    }
}
