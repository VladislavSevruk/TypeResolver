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
package com.github.vladislavsevruk.resolver.resolver.annotated;

import com.github.vladislavsevruk.resolver.resolver.AnnotatedTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Resolves actual types for annotated parameterized types.
 */
@EqualsAndHashCode(exclude = "typeResolverPicker")
public final class AnnotatedParameterizedTypeResolver implements AnnotatedTypeResolver {

    private static final Logger logger = LogManager.getLogger(AnnotatedParameterizedTypeResolver.class);

    private TypeResolverPicker typeResolverPicker;

    public AnnotatedParameterizedTypeResolver(TypeResolverPicker typeResolverPicker) {
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
    public TypeMeta<?> resolve(TypeVariableMap typeVariableMap, AnnotatedType type) {
        logger.debug(() -> "Resolving AnnotatedParameterizedType.");
        AnnotatedParameterizedType annotatedType = (AnnotatedParameterizedType) type;
        AnnotatedType[] actualArguments = annotatedType.getAnnotatedActualTypeArguments();
        Type rawReturnType = ((ParameterizedType) annotatedType.getType()).getRawType();
        TypeMeta<?>[] actualParameters = resolveParameterizedTypes(typeVariableMap, actualArguments);
        return new TypeMeta<>((Class<?>) rawReturnType, actualParameters);
    }

    private TypeMeta<?>[] resolveParameterizedTypes(TypeVariableMap typeVariableMap, AnnotatedType[] actualArguments) {
        TypeMeta<?>[] argumentTypes = new TypeMeta<?>[actualArguments.length];
        for (int i = 0; i < actualArguments.length; ++i) {
            Type actualType = actualArguments[i].getType();
            argumentTypes[i] = typeResolverPicker.pickTypeResolver(actualType).resolve(typeVariableMap, actualType);
        }
        return argumentTypes;
    }
}
