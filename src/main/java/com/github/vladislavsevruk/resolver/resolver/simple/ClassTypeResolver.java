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
package com.github.vladislavsevruk.resolver.resolver.simple;

import com.github.vladislavsevruk.resolver.resolver.TypeResolver;
import com.github.vladislavsevruk.resolver.resolver.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * Resolves actual types for class types.
 */
@EqualsAndHashCode(exclude = "typeResolverPicker")
public final class ClassTypeResolver implements TypeResolver {

    private static final Logger logger = LogManager.getLogger(ClassTypeResolver.class);

    private TypeResolverPicker typeResolverPicker;

    public ClassTypeResolver(TypeResolverPicker typeResolverPicker) {
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
    public TypeMeta<?> resolve(TypeVariableMap typeVariableMap, Type type) {
        Class<?> actualClass = (Class<?>) type;
        if (actualClass.isArray()) {
            return resolveArray(typeVariableMap, actualClass);
        }
        return resolveClassTypeParameters(typeVariableMap, actualClass);
    }

    private TypeMeta<?> resolveArray(TypeVariableMap typeVariableMap, Class<?> actualClass) {
        Class<?> componentType = actualClass.getComponentType();
        logger.debug(() -> String
                .format("'%s' represents array of '%s'.", actualClass.getTypeName(), componentType.getName()));
        TypeMeta<?> componentTypeMeta = typeResolverPicker.pickTypeResolver(componentType)
                .resolve(typeVariableMap, componentType);
        return new TypeMeta<>(actualClass, new TypeMeta<?>[]{ componentTypeMeta });
    }

    private TypeMeta<?> resolveClassTypeParameters(TypeVariableMap typeVariableMap, Class<?> actualClass) {
        logger.debug(() -> String.format("'%s' already represents real class.", actualClass.getTypeName()));
        TypeVariable<? extends Class<?>>[] classTypeParameters = actualClass.getTypeParameters();
        if (classTypeParameters.length == 0) {
            return new TypeMeta<>(actualClass);
        }
        TypeMeta<?>[] typeParameterMetas = new TypeMeta<?>[classTypeParameters.length];
        for (int i = 0; i < classTypeParameters.length; ++i) {
            TypeVariable<? extends Class<?>> classTypeParameter = classTypeParameters[i];
            typeParameterMetas[i] = typeResolverPicker.pickTypeResolver(classTypeParameter)
                    .resolve(typeVariableMap, classTypeParameter);
        }
        return new TypeMeta<>(actualClass, typeParameterMetas);
    }
}
