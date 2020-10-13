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
package com.github.vladislavsevruk.resolver.resolver.simple.parameterized;

import com.github.vladislavsevruk.resolver.resolver.picker.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.resolver.simple.TypeResolver;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Contains common logic for resolving actual types for parameterized types.
 *
 * @param <T> type of mapped value for type variable.
 */
@Log4j2
@EqualsAndHashCode(exclude = "typeResolverPicker")
public abstract class AbstractParameterizedTypeResolver<T> implements TypeResolver<T> {

    private TypeResolverPicker<T> typeResolverPicker;

    protected AbstractParameterizedTypeResolver(TypeResolverPicker<T> typeResolverPicker) {
        this.typeResolverPicker = typeResolverPicker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canResolve(Type argumentType) {
        return ParameterizedType.class.isAssignableFrom(argumentType.getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T resolve(TypeVariableMap<T> typeVariableMap, Type argumentType) {
        log.debug(() -> String.format("'%s' is parameterized type.", argumentType.getTypeName()));
        ParameterizedType parameterizedArgumentType = (ParameterizedType) argumentType;
        Type[] actualTypes = parameterizedArgumentType.getActualTypeArguments();
        T[] resolvedArgumentTypes = createArgumentsArray(actualTypes.length);
        for (int i = 0; i < actualTypes.length; ++i) {
            Type actualType = actualTypes[i];
            resolvedArgumentTypes[i] = typeResolverPicker.pickTypeResolver(actualType)
                    .resolve(typeVariableMap, actualType);
        }
        Class<?> rawType = (Class<?>) parameterizedArgumentType.getRawType();
        return createResolvedParameterizedType(rawType, resolvedArgumentTypes);
    }

    protected abstract T[] createArgumentsArray(int length);

    protected abstract T createResolvedParameterizedType(Class<?> rawType, T[] resolvedArgumentTypes);
}
