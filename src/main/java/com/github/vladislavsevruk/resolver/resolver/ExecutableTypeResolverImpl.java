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
package com.github.vladislavsevruk.resolver.resolver;

import com.github.vladislavsevruk.resolver.context.ResolvingContext;
import com.github.vladislavsevruk.resolver.context.ResolvingContextManager;
import com.github.vladislavsevruk.resolver.type.MappedVariableHierarchy;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of <code>ExecutableTypeResolver</code>.
 *
 * @see ExecutableTypeResolver
 */
@EqualsAndHashCode
public final class ExecutableTypeResolverImpl implements ExecutableTypeResolver {

    private static final Logger logger = LogManager.getLogger(ExecutableTypeResolverImpl.class);

    private final ResolvingContext context;

    public ExecutableTypeResolverImpl() {
        this(ResolvingContextManager.getContext());
    }

    public ExecutableTypeResolverImpl(ResolvingContext context) {
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeMeta<?>> getParameterTypes(Class<?> clazz, Executable executable) {
        return getParameterTypes(new TypeMeta<>(clazz), executable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeMeta<?>> getParameterTypes(TypeMeta<?> typeMeta, Executable executable) {
        logger.debug(
                () -> String.format("Getting parameterized argument types for method '%s'.", executable.getName()));
        MappedVariableHierarchy hierarchy = context.getMappedVariableHierarchyStorage().get(typeMeta);
        TypeVariableMap typeVariableMap = hierarchy.getTypeVariableMap(executable.getDeclaringClass());
        AnnotatedType[] parameterTypes = executable.getAnnotatedParameterTypes();
        List<TypeMeta<?>> resolvedTypes = new ArrayList<>(parameterTypes.length);
        for (AnnotatedType parameterType : parameterTypes) {
            resolvedTypes.add(context.getTypeResolverPicker().pickAnnotatedTypeResolver(parameterType)
                    .resolve(typeVariableMap, parameterType));
        }
        return resolvedTypes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeMeta<?>> getParameterTypes(TypeProvider<?> typeProvider, Executable executable) {
        return getParameterTypes(typeProvider.getTypeMeta(context.getTypeVariableMapper()), executable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeMeta<?> getReturnType(Class<?> clazz, Executable executable) {
        return getReturnType(new TypeMeta<>(clazz), executable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeMeta<?> getReturnType(TypeMeta<?> typeMeta, Executable executable) {
        logger.debug(() -> String.format("Getting parameterized return type for method '%s'.", executable.getName()));
        MappedVariableHierarchy hierarchy = context.getMappedVariableHierarchyStorage().get(typeMeta);
        TypeVariableMap typeVariableMap = hierarchy.getTypeVariableMap(executable.getDeclaringClass());
        AnnotatedType annotatedReturnType = executable.getAnnotatedReturnType();
        return context.getTypeResolverPicker().pickAnnotatedTypeResolver(annotatedReturnType)
                .resolve(typeVariableMap, annotatedReturnType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeMeta<?> getReturnType(TypeProvider<?> typeProvider, Executable executable) {
        return getReturnType(typeProvider.getTypeMeta(context.getTypeVariableMapper()), executable);
    }
}
