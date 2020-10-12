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

import com.github.vladislavsevruk.resolver.context.TypeMetaResolvingContextManager;
import com.github.vladislavsevruk.resolver.exception.TypeResolvingException;
import com.github.vladislavsevruk.resolver.resolver.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.type.MappedVariableHierarchy;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

/**
 * Implementation of <code>TypeVariableMapper</code> for matching TypeMeta of superclasses.
 *
 * @see TypeVariableMapper
 * @see TypeMeta
 */
@Log4j2
@EqualsAndHashCode
public final class TypeMetaVariableMapper implements TypeVariableMapper<TypeMeta<?>> {

    private TypeResolverPicker<TypeMeta<?>> typeResolverPicker;

    public TypeMetaVariableMapper() {
        this(TypeMetaResolvingContextManager.getContext().getTypeResolverPicker());
    }

    public TypeMetaVariableMapper(TypeResolverPicker<TypeMeta<?>> typeResolverPicker) {
        this.typeResolverPicker = typeResolverPicker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MappedVariableHierarchy<TypeMeta<?>> mapTypeVariables(Class<?> clazz) {
        if (clazz.getTypeParameters().length != 0) {
            log.warn("Unresolved generic parameters detected - TypeProvider wasn't used or set properly. "
                    + "Some unexpected issues may appear.");
        }
        return mapTypeVariables(new TypeMeta<>(clazz));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MappedVariableHierarchy<TypeMeta<?>> mapTypeVariables(TypeMeta<?> typeMeta) {
        Class<?> clazz = typeMeta.getType();
        log.debug(() -> String.format("Getting mapped variable hierarchy for class '%s'.", clazz.getName()));
        MappedVariableHierarchy<TypeMeta<?>> mappedVariableHierarchy = new MappedVariableHierarchy<>(clazz);
        mapTypeVariables(mappedVariableHierarchy, clazz, typeMeta.getGenericTypes());
        return mappedVariableHierarchy;
    }

    private void doMapTypeVariables(MappedVariableHierarchy<TypeMeta<?>> mappedVariableHierarchy, Class<?> clazz,
            TypeMeta<?>[] actualTypes) {
        log.debug(() -> String.format("Mapping type variables for class '%s'.", clazz.getName()));
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

    private void mapTypeVariables(MappedVariableHierarchy<TypeMeta<?>> mappedVariableHierarchy, Class<?> clazz,
            TypeMeta<?>[] actualTypes) {
        if (Object.class.equals(clazz)) {
            return;
        }
        doMapTypeVariables(mappedVariableHierarchy, clazz, actualTypes);
        TypeVariableMap<TypeMeta<?>> typeVariableMap = mappedVariableHierarchy.getTypeVariableMap(clazz);
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

    private TypeMeta<?>[] mapTypes(TypeVariableMap<TypeMeta<?>> typeVariableMap, Type[] types) {
        return Arrays.stream(types)
                .map(type -> typeResolverPicker.pickTypeResolver(type).resolve(typeVariableMap, type))
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
                log.error(message);
                throw new TypeResolvingException(message);
            }
        } else if (typeVariables.length != actualTypes.length) {
            String message = String
                    .format("Number of type variables (%d) doesn't match number of received actual types (%d).",
                            typeVariables.length, actualTypes.length);
            log.error(message);
            throw new TypeResolvingException(message);
        }
    }
}
