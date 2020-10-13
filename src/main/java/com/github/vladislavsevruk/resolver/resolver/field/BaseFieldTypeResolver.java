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
package com.github.vladislavsevruk.resolver.resolver.field;

import com.github.vladislavsevruk.resolver.context.ResolvingContext;
import com.github.vladislavsevruk.resolver.type.MappedVariableHierarchy;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;

/**
 * Contains common logic for resolving actual types for generic generic fields.
 *
 * @param <T> type of mapped value for type variable.
 * @see FieldTypeResolver
 */
@Log4j2
@EqualsAndHashCode
public class BaseFieldTypeResolver<T> implements FieldTypeResolver<T> {

    protected final ResolvingContext<T> context;

    public BaseFieldTypeResolver(ResolvingContext<T> context) {
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T resolveField(Class<?> clazz, Field field) {
        return resolveField(new TypeMeta<>(clazz), field);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T resolveField(TypeMeta<?> typeMeta, Field field) {
        log.debug(() -> String.format("Getting parameterized type for field '%s'.", field.getName()));
        MappedVariableHierarchy<T> hierarchy = context.getMappedVariableHierarchyStorage().get(typeMeta);
        TypeVariableMap<T> typeVariableMap = hierarchy.getTypeVariableMap(field.getDeclaringClass());
        AnnotatedType annotatedType = field.getAnnotatedType();
        return context.getTypeResolverPicker().pickAnnotatedTypeResolver(annotatedType)
                .resolve(typeVariableMap, annotatedType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T resolveField(TypeProvider<?> typeProvider, Field field) {
        return resolveField(typeProvider.getTypeMeta(), field);
    }
}
