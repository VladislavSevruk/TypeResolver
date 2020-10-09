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
package com.github.vladislavsevruk.resolver.resolver.executable;

import com.github.vladislavsevruk.resolver.context.ResolvingContext;
import com.github.vladislavsevruk.resolver.context.TypeMetaResolvingContextManager;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Executable;
import java.util.List;

/**
 * Implementation of <code>ExecutableTypeResolver</code> for TypeMeta.
 *
 * @see ExecutableTypeResolver
 * @see TypeMeta
 */
@Log4j2
@EqualsAndHashCode(callSuper = true)
public final class ExecutableTypeMetaResolver extends BaseExecutableTypeResolver<TypeMeta<?>> {

    public ExecutableTypeMetaResolver() {
        this(TypeMetaResolvingContextManager.getContext());
    }

    public ExecutableTypeMetaResolver(ResolvingContext<TypeMeta<?>> context) {
        super(context);
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
    public TypeMeta<?> getReturnType(TypeProvider<?> typeProvider, Executable executable) {
        return getReturnType(typeProvider.getTypeMeta(context.getTypeVariableMapper()), executable);
    }
}
