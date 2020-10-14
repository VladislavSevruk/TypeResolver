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

import com.github.vladislavsevruk.resolver.context.ResolvingContext;
import com.github.vladislavsevruk.resolver.context.TypeMetaResolvingContextManager;
import com.github.vladislavsevruk.resolver.type.TypeMeta;

import java.lang.reflect.TypeVariable;
import java.util.function.IntFunction;

/**
 * Implementation of <code>TypeVariableMapper</code> for matching TypeMeta of superclasses.
 *
 * @see TypeVariableMapper
 * @see TypeMeta
 */
public final class TypeMetaVariableMapper extends AbstractTypeVariableMapper<TypeMeta<?>> {

    public TypeMetaVariableMapper() {
        this(TypeMetaResolvingContextManager.getContext());
    }

    public TypeMetaVariableMapper(ResolvingContext<TypeMeta<?>> resolvingContext) {
        super(resolvingContext);
    }

    @Override
    protected TypeMeta<?>[] getActualTypes(TypeMeta<?> actualType) {
        return actualType.getGenericTypes();
    }

    @Override
    protected TypeMeta<?> getDefaultType(TypeVariable<? extends Class<?>> typeVariable) {
        return TypeMeta.OBJECT_META;
    }

    @Override
    protected IntFunction<TypeMeta<?>[]> getNewArrayFunction() {
        return TypeMeta<?>[]::new;
    }
}
