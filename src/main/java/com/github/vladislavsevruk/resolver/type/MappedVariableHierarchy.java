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
package com.github.vladislavsevruk.resolver.type;

import com.github.vladislavsevruk.resolver.exception.TypeResolvingException;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents class hierarchy with type variables that were used in generic class declaration and their actual values
 * for this hierarchy.
 *
 * @param <T> type of mapped value for type variable.
 */
@Log4j2
@EqualsAndHashCode
public class MappedVariableHierarchy<T> {

    private final Map<Class<?>, TypeVariableMap<T>> classMappedVariables = new HashMap<>();

    /**
     * Builds superclasses hierarchy skeleton for received class.
     *
     * @param clazz <code>Class</code> to build hierarchy for.
     */
    public MappedVariableHierarchy(Class<?> clazz) {
        log.debug(() -> String.format("Creating class variable hierarchy for class '%s'.", clazz.getName()));
        buildHierarchy(clazz);
    }

    /**
     * Adds mapped type variable for specific class in hierarchy.
     *
     * @param clazz        <code>Class</code> to add mapped type variable for.
     * @param typeVariable <code>TypeVariable</code> that was used in generic class declaration.
     * @param actualType   actual value that matches received <code>TypeVariable</code>.
     * @throws TypeResolvingException provided received <code>Class</code> isn't present at this hierarchy.
     */
    public void addTypeVariable(Class<?> clazz, TypeVariable<? extends Class<?>> typeVariable, T actualType) {
        getTypeVariableMap(clazz).addTypeVariable(typeVariable, actualType);
    }

    /**
     * Returns mapped type variables for specific class in hierarchy.
     *
     * @param clazz <code>Class</code> to get <code>TypeVariableMap</code> for.
     * @return <code>TypeVariableMap</code> with mapped variables for received <code>Class</code>.
     * @throws TypeResolvingException provided received <code>Class</code> isn't present at this hierarchy.
     */
    public TypeVariableMap<T> getTypeVariableMap(Class<?> clazz) {
        TypeVariableMap<T> typeVariableMap = classMappedVariables.get(clazz);
        if (typeVariableMap == null) {
            throw new TypeResolvingException(
                    String.format("Class '%s' isn't present at this hierarchy.", clazz.getName()));
        }
        return typeVariableMap;
    }

    private void buildHierarchy(Class<?> clazz) {
        TypeVariableMap<T> typeVariableMap = new TypeVariableMap<>();
        classMappedVariables.put(clazz, typeVariableMap);
        for (Class<?> classInterface : clazz.getInterfaces()) {
            if (!classMappedVariables.containsKey(classInterface)) {
                buildHierarchy(classInterface);
            }
        }
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && !Object.class.equals(superclass)) {
            buildHierarchy(superclass);
        }
    }
}
