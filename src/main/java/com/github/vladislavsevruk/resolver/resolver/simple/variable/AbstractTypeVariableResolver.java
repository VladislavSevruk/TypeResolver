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
package com.github.vladislavsevruk.resolver.resolver.simple.variable;

import com.github.vladislavsevruk.resolver.resolver.simple.TypeResolver;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * Contains common logic for resolving actual types for type variables.
 *
 * @param <T> type of mapped value for type variable.
 */
@Log4j2
@EqualsAndHashCode
public abstract class AbstractTypeVariableResolver<T> implements TypeResolver<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canResolve(Type type) {
        return TypeVariable.class.isAssignableFrom(type.getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public T resolve(TypeVariableMap<T> typeVariableMap, Type type) {
        log.debug(() -> String.format("'%s' is type variable.", type.getTypeName()));
        T resolvedType = typeVariableMap.getActualType((TypeVariable<? extends Class<?>>) type);
        if (resolvedType != null) {
            log.debug(() -> String.format("Actual type for '%s' is '%s'.", type.getTypeName(), getName(resolvedType)));
            return resolvedType;
        }
        log.info(() -> String
                .format("Failed to find type variable '%s' in class declaration. Probably generic type used in "
                                + "method declaration directly. Using 'java.lang.Object' for '%<s' type variable.",
                        type.getTypeName()));
        return getDefaultType(type);
    }

    protected abstract T getDefaultType(Type type);

    protected abstract String getName(T resolvedType);
}
