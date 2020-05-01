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

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.lang.reflect.Type;

/**
 * Resolves actual types for annotated array types.
 */
@EqualsAndHashCode(exclude = "typeResolverPicker")
public final class AnnotatedArrayTypeResolver implements AnnotatedTypeResolver {

    private static final Logger logger = LogManager.getLogger(AnnotatedArrayTypeResolver.class);

    private TypeResolverPicker typeResolverPicker;

    public AnnotatedArrayTypeResolver(TypeResolverPicker typeResolverPicker) {
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
    public TypeMeta<?> resolve(TypeVariableMap typeVariableMap, AnnotatedType type) {
        logger.debug(() -> "Resolving AnnotatedArrayType.");
        AnnotatedArrayType annotatedType = (AnnotatedArrayType) type;
        AnnotatedType componentType = annotatedType.getAnnotatedGenericComponentType();
        TypeMeta<?> actualParameters = resolveParameterizedComponentType(typeVariableMap, componentType);
        return new TypeMeta<>(resolveArrayType(annotatedType, actualParameters), new TypeMeta<?>[]{ actualParameters });
    }

    private Class<?> getArrayTypeByComponent(TypeMeta<?> actualParameters) {
        return Array.newInstance(actualParameters.getType(), 0).getClass();
    }

    private Class<?> resolveArrayType(AnnotatedType annotatedType, TypeMeta<?> actualParameters) {
        Type arrayType = annotatedType.getType();
        return Class.class.isAssignableFrom(arrayType.getClass()) ? (Class<?>) arrayType
                : getArrayTypeByComponent(actualParameters);
    }

    private TypeMeta<?> resolveParameterizedComponentType(TypeVariableMap typeVariableMap,
            AnnotatedType componentType) {
        Type actualType = componentType.getType();
        return typeResolverPicker.pickTypeResolver(actualType).resolve(typeVariableMap, actualType);
    }
}
