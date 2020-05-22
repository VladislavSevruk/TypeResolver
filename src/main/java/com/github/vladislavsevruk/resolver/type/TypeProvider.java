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

import com.github.vladislavsevruk.resolver.context.ResolvingContextManager;
import com.github.vladislavsevruk.resolver.type.mapper.TypeVariableMapper;

import java.lang.reflect.TypeVariable;

/**
 * Class that used for generating type meta for generic classes. Should be overridden for providing values. Class has
 * similar principle as Jackson mapper lib and is based on ideas from
 * <a href="http://gafter.blogspot.com/2006/12/super-type-tokens.html">http://gafter.blogspot.com/2006/12/super-type-tokens.html</a>
 * <br>E.g., to provide meta data for list of strings properly anonymous descendant of <code>TypeProvider</code> can be
 * used: <br>
 * <code>new TypeProvider&lt;List&lt;String&gt;&gt;() {}</code>
 *
 * @param <T> actual type.
 */
public abstract class TypeProvider<T> implements Comparable<TypeProvider<T>> {

    private TypeMeta<?> typeMeta;

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(TypeProvider<T> o) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof TypeProvider) {
            return getTypeMeta().equals(((TypeProvider) o).getTypeMeta());
        }
        return false;
    }

    /**
     * Returns type meta for this instance.
     */
    @SuppressWarnings("java:S1452")
    public TypeMeta<?> getTypeMeta() {
        if (typeMeta == null) {
            typeMeta = resolveGenericParameter(ResolvingContextManager.getContext().getTypeVariableMapper());
        }
        return typeMeta;
    }

    /**
     * Returns type meta for this instance.
     *
     * @param typeVariableMapper <code>TypeVariableMapper</code> to use for resolving instance type variables.
     * @return resolved type meta for this instance.
     */
    @SuppressWarnings("java:S1452")
    public TypeMeta<?> getTypeMeta(TypeVariableMapper typeVariableMapper) {
        if (typeMeta == null) {
            typeMeta = resolveGenericParameter(typeVariableMapper);
        }
        return typeMeta;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getTypeMeta().hashCode();
    }

    private TypeMeta<?> resolveGenericParameter(TypeVariableMapper typeVariableMapper) {
        TypeVariable<? extends Class<?>> typeVariable = TypeProvider.class.getTypeParameters()[0];
        return typeVariableMapper.mapTypeVariables(getClass()).getTypeVariableMap(TypeProvider.class)
                .getActualType(typeVariable);
    }
}
