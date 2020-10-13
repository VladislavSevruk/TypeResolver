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
package com.github.vladislavsevruk.resolver.resolver.simple.clazz;

import com.github.vladislavsevruk.resolver.resolver.picker.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.resolver.simple.TypeResolver;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * Contains common logic for resolving actual types for class types.
 *
 * @param <T> type of mapped value for type variable.
 */
@Log4j2
@EqualsAndHashCode(exclude = "typeResolverPicker")
public abstract class AbstractClassTypeResolver<T> implements TypeResolver<T> {

    private TypeResolverPicker<T> typeResolverPicker;

    public AbstractClassTypeResolver(TypeResolverPicker<T> typeResolverPicker) {
        this.typeResolverPicker = typeResolverPicker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canResolve(Type argumentType) {
        return Class.class.isAssignableFrom(argumentType.getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T resolve(TypeVariableMap<T> typeVariableMap, Type type) {
        Class<?> actualClass = (Class<?>) type;
        if (actualClass.isArray()) {
            return resolveArray(typeVariableMap, actualClass);
        }
        return resolveClassTypeParameters(typeVariableMap, actualClass);
    }

    protected abstract T[] createComponentsArray(int length);

    protected abstract T createResolvedArray(Class<?> actualClass, T resolvedComponentType);

    protected abstract T createResolvedItem(Class<?> actualClass);

    protected abstract T createResolvedParameterizedType(Class<?> rawType, T[] resolvedArgumentTypes);

    private T resolveArray(TypeVariableMap<T> typeVariableMap, Class<?> actualClass) {
        Class<?> componentType = actualClass.getComponentType();
        log.debug(() -> String
                .format("'%s' represents array of '%s'.", actualClass.getTypeName(), componentType.getName()));
        T resolvedComponentType = typeResolverPicker.pickTypeResolver(componentType)
                .resolve(typeVariableMap, componentType);
        return createResolvedArray(actualClass, resolvedComponentType);
    }

    private T resolveClassTypeParameters(TypeVariableMap<T> typeVariableMap, Class<?> actualClass) {
        log.debug(() -> String.format("'%s' already represents real class.", actualClass.getTypeName()));
        TypeVariable<? extends Class<?>>[] classTypeParameters = actualClass.getTypeParameters();
        if (classTypeParameters.length == 0) {
            return createResolvedItem(actualClass);
        }
        T[] resolvedComponentTypes = createComponentsArray(classTypeParameters.length);
        for (int i = 0; i < classTypeParameters.length; ++i) {
            TypeVariable<? extends Class<?>> classTypeParameter = classTypeParameters[i];
            resolvedComponentTypes[i] = typeResolverPicker.pickTypeResolver(classTypeParameter)
                    .resolve(typeVariableMap, classTypeParameter);
        }
        return createResolvedParameterizedType(actualClass, resolvedComponentTypes);
    }
}