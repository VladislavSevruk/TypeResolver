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
package com.github.vladislavsevruk.resolver.resolver.simple.wildcard;

import com.github.vladislavsevruk.resolver.resolver.picker.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import lombok.EqualsAndHashCode;

/**
 * Resolves actual types for wildcard types.
 */
@EqualsAndHashCode(callSuper = true)
public final class WildcardTypeResolver extends AbstractWildcardTypeResolver<TypeMeta<?>> {

    public WildcardTypeResolver(TypeResolverPicker<TypeMeta<?>> typeResolverPicker) {
        super(typeResolverPicker);
    }

    @Override
    protected TypeMeta<?> createResolvedItemLowerBound(TypeMeta<?> resolvedType) {
        return new TypeMeta<>(resolvedType.getType(), resolvedType.getGenericTypes(), true);
    }

    @Override
    protected TypeMeta<?> createResolvedItemUpperBound(TypeMeta<?> resolvedType) {
        return new TypeMeta<>(resolvedType.getType(), resolvedType.getGenericTypes(), true);
    }
}
