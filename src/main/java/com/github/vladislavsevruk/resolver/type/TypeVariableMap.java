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

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents type variables that were used in generic class declaration and their actual values.
 */
@ToString
@EqualsAndHashCode
public class TypeVariableMap {

    private final Map<TypeVariable<? extends Class<?>>, TypeMeta<?>> typeVariableMappings = new HashMap<>();

    /**
     * Returns actual type for type variable that was used at class declaration.
     *
     * @param typeVariable <code>TypeVariable</code> to get matching actual <code>Type</code> for.
     * @return actual <code>Type</code> that matches received <code>TypeVariable</code>.
     */
    @SuppressWarnings("java:S1452")
    public TypeMeta<?> getActualType(TypeVariable<? extends Class<?>> typeVariable) {
        return typeVariableMappings.get(typeVariable);
    }

    /**
     * Adds mapped type variable.
     *
     * @param typeVariable <code>TypeVariable</code> that was used in generic class declaration.
     * @param actualType   actual <code>TypeMeta</code> that matches received <code>TypeVariable</code>.
     */
    void addTypeVariable(TypeVariable<? extends Class<?>> typeVariable, TypeMeta<?> actualType) {
        typeVariableMappings.put(typeVariable, actualType);
    }
}
