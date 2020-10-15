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
package com.github.vladislavsevruk.resolver.resolver.annotated;

import com.github.vladislavsevruk.resolver.resolver.picker.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

/**
 * Resolves actual types for annotated types.
 *
 * @param <T> type of mapped value for type variable.
 */
@Log4j2
public final class AnnotatedTypeBaseResolver<T> implements AnnotatedTypeResolver<T> {

    private TypeResolverPicker<T> typeResolverPicker;

    public AnnotatedTypeBaseResolver(TypeResolverPicker<T> typeResolverPicker) {
        this.typeResolverPicker = typeResolverPicker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canResolve(AnnotatedType annotatedType) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T resolve(TypeVariableMap<T> typeVariableMap, AnnotatedType annotatedType) {
        log.debug(() -> "Resolving AnnotatedType.");
        Type typeToResolve = annotatedType.getType();
        return typeResolverPicker.pickTypeResolver(typeToResolve).resolve(typeVariableMap, typeToResolve);
    }
}
