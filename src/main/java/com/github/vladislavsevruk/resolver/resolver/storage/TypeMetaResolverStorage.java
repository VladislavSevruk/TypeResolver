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
package com.github.vladislavsevruk.resolver.resolver.storage;

import com.github.vladislavsevruk.resolver.context.TypeMetaResolvingContextManager;
import com.github.vladislavsevruk.resolver.resolver.annotated.AnnotatedTypeBaseResolver;
import com.github.vladislavsevruk.resolver.resolver.annotated.AnnotatedTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.annotated.array.AnnotatedArrayTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.annotated.parameterized.AnnotatedParameterizedTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.picker.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.resolver.simple.TypeResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.clazz.ClassTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.generic.GenericArrayTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.parameterized.ParameterizedTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.variable.TypeVariableResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.wildcard.WildcardTypeResolver;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of <code>TypeResolverStorage</code> for TypeMeta.
 *
 * @see TypeResolverStorage
 * @see TypeMeta
 */
@Getter
@EqualsAndHashCode
public final class TypeMetaResolverStorage implements TypeResolverStorage<TypeMeta<?>> {

    private final List<AnnotatedTypeResolver<TypeMeta<?>>> annotatedTypeResolvers;
    private final List<TypeResolver<TypeMeta<?>>> typeResolvers;

    public TypeMetaResolverStorage() {
        this(TypeMetaResolvingContextManager.getContext().getTypeResolverPicker());
    }

    public TypeMetaResolverStorage(TypeResolverPicker<TypeMeta<?>> typeResolverPicker) {
        annotatedTypeResolvers = createAnnotatedTypeResolversList(typeResolverPicker);
        typeResolvers = createTypeResolversList(typeResolverPicker);
    }

    private List<AnnotatedTypeResolver<TypeMeta<?>>> createAnnotatedTypeResolversList(
            TypeResolverPicker<TypeMeta<?>> typeResolverPicker) {
        return Arrays.asList(new AnnotatedArrayTypeResolver(typeResolverPicker),
                new AnnotatedParameterizedTypeResolver(typeResolverPicker),
                new AnnotatedTypeBaseResolver<>(typeResolverPicker));
    }

    private List<TypeResolver<TypeMeta<?>>> createTypeResolversList(
            TypeResolverPicker<TypeMeta<?>> typeResolverPicker) {
        return Arrays
                .asList(new ClassTypeResolver(typeResolverPicker), new GenericArrayTypeResolver(typeResolverPicker),
                        new ParameterizedTypeResolver(typeResolverPicker), new TypeVariableResolver(),
                        new WildcardTypeResolver(typeResolverPicker));
    }
}
