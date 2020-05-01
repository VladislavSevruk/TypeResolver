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
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * Resolves actual types for type variables.
 */
@EqualsAndHashCode
public final class TypeVariableResolver implements TypeResolver {

    private static final Logger logger = LogManager.getLogger(TypeVariableResolver.class);

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
    public TypeMeta<?> resolve(TypeVariableMap typeVariableMap, Type type) {
        logger.debug(() -> String.format("'%s' is type variable.", type.getTypeName()));
        TypeMeta<?> typeMeta = typeVariableMap.getActualType((TypeVariable<? extends Class<?>>) type);
        if (typeMeta != null) {
            logger.debug(() -> String
                    .format("Actual type for '%s' is '%s'.", type.getTypeName(), typeMeta.getType().getName()));
            return typeMeta;
        }
        logger.info(() -> String
                .format("Failed to find type variable '%s' in class declaration. Probably generic type used in "
                                + "method declaration directly. Using 'java.lang.Object' for '%<s' type variable.",
                        type.getTypeName()));
        return TypeMeta.WILDCARD_META;
    }
}
