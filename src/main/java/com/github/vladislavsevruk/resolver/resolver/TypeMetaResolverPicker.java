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
package com.github.vladislavsevruk.resolver.resolver;

import com.github.vladislavsevruk.resolver.exception.TypeResolvingException;
import com.github.vladislavsevruk.resolver.resolver.annotated.AnnotatedArrayTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.annotated.AnnotatedParameterizedTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.annotated.AnnotatedTypeBaseResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.ClassTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.GenericArrayTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.ParameterizedTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.TypeVariableResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.WildcardTypeResolver;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import lombok.EqualsAndHashCode;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of <code>TypeResolverPicker</code> for TypeMeta.
 *
 * @see TypeResolverPicker
 * @see TypeMeta
 */
@EqualsAndHashCode
public final class TypeMetaResolverPicker implements TypeResolverPicker<TypeMeta<?>> {

    private final List<AnnotatedTypeResolver<TypeMeta<?>>> annotatedTypeResolvers;
    private final List<TypeResolver<TypeMeta<?>>> typeResolvers;

    public TypeMetaResolverPicker() {
        annotatedTypeResolvers = Arrays
                .asList(new AnnotatedArrayTypeResolver(this), new AnnotatedParameterizedTypeResolver(this),
                        new AnnotatedTypeBaseResolver(this));
        typeResolvers = Arrays.asList(new ClassTypeResolver(this), new GenericArrayTypeResolver(this),
                new ParameterizedTypeResolver(this), new TypeVariableResolver(), new WildcardTypeResolver(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotatedTypeResolver<TypeMeta<?>> pickAnnotatedTypeResolver(AnnotatedType annotatedType) {
        for (AnnotatedTypeResolver<TypeMeta<?>> annotatedTypeResolver : annotatedTypeResolvers) {
            if (annotatedTypeResolver.canResolve(annotatedType)) {
                return annotatedTypeResolver;
            }
        }
        throw new TypeResolvingException("Cannot resolve annotated type: " + annotatedType.getClass().getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeResolver<TypeMeta<?>> pickTypeResolver(Type type) {
        for (TypeResolver<TypeMeta<?>> typeResolver : typeResolvers) {
            if (typeResolver.canResolve(type)) {
                return typeResolver;
            }
        }
        throw new TypeResolvingException("Cannot resolve type: " + type.getTypeName());
    }
}
