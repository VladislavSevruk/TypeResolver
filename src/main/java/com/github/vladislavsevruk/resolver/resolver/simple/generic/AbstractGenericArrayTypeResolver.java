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
package com.github.vladislavsevruk.resolver.resolver.simple.generic;

import com.github.vladislavsevruk.resolver.resolver.picker.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.resolver.simple.TypeResolver;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/**
 * Contains common logic for resolving actual types for generic array types.
 *
 * @param <T> type of mapped value for type variable.
 */
@Log4j2
@EqualsAndHashCode(exclude = "typeResolverPicker")
public abstract class AbstractGenericArrayTypeResolver<T> implements TypeResolver<T> {

    private TypeResolverPicker<T> typeResolverPicker;

    public AbstractGenericArrayTypeResolver(TypeResolverPicker<T> typeResolverPicker) {
        this.typeResolverPicker = typeResolverPicker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canResolve(Type type) {
        return GenericArrayType.class.isAssignableFrom(type.getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T resolve(TypeVariableMap<T> typeVariableMap, Type type) {
        GenericArrayType genericArrayType = (GenericArrayType) type;
        Type componentType = genericArrayType.getGenericComponentType();
        log.debug(
                () -> String.format("'%s' represents array of '%s'.", type.getTypeName(), componentType.getTypeName()));
        T resolvedType = typeResolverPicker.pickTypeResolver(componentType).resolve(typeVariableMap, componentType);
        return createResolvedArray(resolvedType);
    }

    protected abstract T createResolvedArray(T resolvedType);
}
