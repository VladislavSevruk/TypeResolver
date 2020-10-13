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
package com.github.vladislavsevruk.resolver.resolver.field;

import com.github.vladislavsevruk.resolver.context.ResolvingContext;
import com.github.vladislavsevruk.resolver.context.TypeMetaResolvingContextManager;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;

import java.lang.reflect.Field;

/**
 * Implementation of <code>FieldTypeResolver</code> for TypeMeta.
 *
 * @see FieldTypeResolver
 * @see TypeMeta
 */
public final class FieldTypeMetaResolver extends BaseFieldTypeResolver<TypeMeta<?>> {

    public FieldTypeMetaResolver() {
        this(TypeMetaResolvingContextManager.getContext());
    }

    public FieldTypeMetaResolver(ResolvingContext<TypeMeta<?>> context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeMeta<?> resolveField(TypeProvider<?> typeProvider, Field field) {
        return resolveField(typeProvider.getTypeMeta(context.getTypeVariableMapper()), field);
    }
}
