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
package com.github.vladislavsevruk.resolver.type.mapper;

import com.github.vladislavsevruk.resolver.context.ResolvingContextManager;
import com.github.vladislavsevruk.resolver.exception.TypeResolvingException;
import com.github.vladislavsevruk.resolver.resolver.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.type.MappedVariableHierarchy;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

/**
 * Implementation of <code>TypeVariableMapper</code>.
 *
 * @see TypeVariableMapper
 */
@EqualsAndHashCode
public final class TypeVariableMapperImpl implements TypeVariableMapper {

    private static final Logger logger = LogManager.getLogger(TypeVariableMapperImpl.class);

    private TypeResolverPicker typeResolverPicker;

    public TypeVariableMapperImpl() {
        this(ResolvingContextManager.getContext().getTypeResolverPicker());
    }

    public TypeVariableMapperImpl(TypeResolverPicker typeResolverPicker) {
        this.typeResolverPicker = typeResolverPicker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MappedVariableHierarchy mapTypeVariables(Class<?> clazz) {
        if (clazz.getTypeParameters().length != 0) {
            logger.warn("Unresolved generic parameters detected - TypeProvider wasn't used or set properly. "
                    + "Some unexpected issues may appear.");
        }
        return mapTypeVariables(new TypeMeta<>(clazz));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MappedVariableHierarchy mapTypeVariables(TypeMeta<?> typeMeta) {
        Class<?> clazz = typeMeta.getType();
        logger.debug(() -> String.format("Getting mapped variable hierarchy for class '%s'.", clazz.getName()));
        MappedVariableHierarchy mappedVariableHierarchy = new MappedVariableHierarchy(clazz);
        mapTypeVariables(mappedVariableHierarchy, clazz, typeMeta.getGenericTypes());
        return mappedVariableHierarchy;
    }

    private void doMapTypeVariables(MappedVariableHierarchy mappedVariableHierarchy, Class<?> clazz,
            TypeMeta<?>[] actualTypes) {
        logger.debug(() -> String.format("Mapping type variables for class '%s'.", clazz.getName()));
        verifyNumberOfTypesMatch(clazz, actualTypes);
        TypeVariable<? extends Class<?>>[] typeVariables = clazz.getTypeParameters();
        for (int i = 0; i < typeVariables.length; ++i) {
            TypeVariable<? extends Class<?>> typeVariable = typeVariables[i];
            TypeMeta<?> actualType = actualTypes.length == 0 ? TypeMeta.OBJECT_META : actualTypes[i];
            mappedVariableHierarchy.addTypeVariable(clazz, typeVariable, actualType);
        }
    }

    private Type[] getActualTypeArguments(Type type) {
        if (!ParameterizedType.class.isAssignableFrom(type.getClass())) {
            return new Type[0];
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return parameterizedType.getActualTypeArguments();
    }

    private void mapTypeVariables(MappedVariableHierarchy mappedVariableHierarchy, Class<?> clazz,
            TypeMeta<?>[] actualTypes) {
        if (Object.class.equals(clazz)) {
            return;
        }
        doMapTypeVariables(mappedVariableHierarchy, clazz, actualTypes);
        TypeVariableMap typeVariableMap = mappedVariableHierarchy.getTypeVariableMap(clazz);
        AnnotatedType[] classInterfaces = clazz.getAnnotatedInterfaces();
        for (int i = 0; i < classInterfaces.length; ++i) {
            Type interfaceType = classInterfaces[i].getType();
            TypeMeta<?>[] actualTypeMetas = mapTypes(typeVariableMap, getActualTypeArguments(interfaceType));
            mapTypeVariables(mappedVariableHierarchy, clazz.getInterfaces()[i], actualTypeMetas);
        }
        if (clazz.getAnnotatedSuperclass() != null) {
            Type superclassType = clazz.getAnnotatedSuperclass().getType();
            TypeMeta<?>[] actualTypeMetas = mapTypes(typeVariableMap, getActualTypeArguments(superclassType));
            mapTypeVariables(mappedVariableHierarchy, clazz.getSuperclass(), actualTypeMetas);
        }
    }

    private TypeMeta<?>[] mapTypes(TypeVariableMap actualTypes, Type[] types) {
        return Arrays.stream(types).map(type -> typeResolverPicker.pickTypeResolver(type).resolve(actualTypes, type))
                .toArray(TypeMeta<?>[]::new);
    }

    private void verifyNumberOfTypesMatch(Class<?> clazz, TypeMeta<?>[] actualTypes) {
        TypeVariable<? extends Class<?>>[] typeVariables = clazz.getTypeParameters();
        if (actualTypes.length == 0) {
            // non-resolved types
            return;
        }
        if (clazz.isArray()) {
            if (actualTypes.length != 1) {
                String message = String
                        .format("Number of component types (1) doesn't match number of received actual types (%d).",
                                actualTypes.length);
                logger.error(message);
                throw new TypeResolvingException(message);
            }
        } else if (typeVariables.length != actualTypes.length) {
            String message = String
                    .format("Number of type variables (%d) doesn't match number of received actual types (%d).",
                            typeVariables.length, actualTypes.length);
            logger.error(message);
            throw new TypeResolvingException(message);
        }
    }
}
