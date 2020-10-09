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

import com.github.vladislavsevruk.resolver.resolver.TypeMetaResolverPicker;
import com.github.vladislavsevruk.resolver.resolver.TypeResolver;
import com.github.vladislavsevruk.resolver.resolver.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.test.data.TestTypeProvider;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class AnnotatedTypeBaseResolverTest {

    private AnnotatedTypeBaseResolver realAnnotatedTypeBaseResolver = new AnnotatedTypeBaseResolver(
            new TypeMetaResolverPicker());

    @ParameterizedTest
    @MethodSource("canResolveProvider")
    void canResolveTest(AnnotatedType type, Boolean expectedValue) {
        Assertions.assertEquals(expectedValue, realAnnotatedTypeBaseResolver.canResolve(type));
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveAnnotatedTypeVariableRealContextTest() {
        TypeVariable<? extends Class<?>> typeVariable = Mockito.mock(TypeVariable.class);
        AnnotatedTypeVariable annotatedTypeVariable = Mockito.mock(AnnotatedTypeVariable.class);
        Mockito.when(annotatedTypeVariable.getType()).thenReturn(typeVariable);
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta typeMeta = new TypeMeta<>(String.class);
        Mockito.when(typeVariableMap.getActualType(typeVariable)).thenReturn(typeMeta);
        TypeMeta<?> result = realAnnotatedTypeBaseResolver.resolve(typeVariableMap, annotatedTypeVariable);
        Assertions.assertEquals(typeMeta, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveAnnotatedTypeVariableTest() {
        Type innerType = Mockito.mock(Type.class);
        AnnotatedTypeVariable annotatedTypeVariable = Mockito.mock(AnnotatedTypeVariable.class);
        Mockito.when(annotatedTypeVariable.getType()).thenReturn(innerType);
        TypeResolver typeResolver = Mockito.mock(TypeResolver.class);
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta typeMeta = new TypeMeta<>(Integer.class);
        Mockito.when(typeResolver.resolve(typeVariableMap, innerType)).thenReturn(typeMeta);
        TypeResolverPicker typeResolverPicker = Mockito.mock(TypeResolverPicker.class);
        Mockito.when(typeResolverPicker.pickTypeResolver(innerType)).thenReturn(typeResolver);
        AnnotatedTypeBaseResolver annotatedTypeBaseResolver = new AnnotatedTypeBaseResolver(typeResolverPicker);
        TypeMeta<?> result = annotatedTypeBaseResolver.resolve(typeVariableMap, annotatedTypeVariable);
        Assertions.assertEquals(typeMeta, result);
    }

    @Test
    void resolveAnnotatedWildcardTypeRealContextTest() {
        Class<?> boundType = Character[].class;
        WildcardType wildcardType = Mockito.mock(WildcardType.class);
        Mockito.when(wildcardType.getUpperBounds()).thenReturn(new Type[]{ boundType });
        AnnotatedTypeVariable annotatedWildcardType = Mockito.mock(AnnotatedTypeVariable.class);
        Mockito.when(annotatedWildcardType.getType()).thenReturn(wildcardType);
        TypeVariableMap<TypeMeta<?>> typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Character.class);
        TypeMeta typeMeta = new TypeMeta<>(Character[].class, new TypeMeta<?>[]{ innerTypeMeta }, true);
        TypeMeta<?> result = realAnnotatedTypeBaseResolver.resolve(typeVariableMap, annotatedWildcardType);
        Assertions.assertEquals(typeMeta, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveAnnotatedWildcardTypeTest() {
        Type innerType = Mockito.mock(Type.class);
        AnnotatedWildcardType annotatedWildcardType = Mockito.mock(AnnotatedWildcardType.class);
        Mockito.when(annotatedWildcardType.getType()).thenReturn(innerType);
        TypeResolver typeResolver = Mockito.mock(TypeResolver.class);
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta typeMeta = new TypeMeta<>(Float.class);
        Mockito.when(typeResolver.resolve(typeVariableMap, innerType)).thenReturn(typeMeta);
        TypeResolverPicker typeResolverPicker = Mockito.mock(TypeResolverPicker.class);
        Mockito.when(typeResolverPicker.pickTypeResolver(innerType)).thenReturn(typeResolver);
        AnnotatedTypeBaseResolver annotatedTypeBaseResolver = new AnnotatedTypeBaseResolver(typeResolverPicker);
        TypeMeta<?> result = annotatedTypeBaseResolver.resolve(typeVariableMap, annotatedWildcardType);
        Assertions.assertEquals(typeMeta, result);
    }

    private static Stream<Arguments> canResolveProvider() {
        return Stream.of(Arguments.of(TestTypeProvider.annotatedArrayType(), Boolean.TRUE),
                Arguments.of(TestTypeProvider.annotatedParameterizedType(), Boolean.TRUE),
                Arguments.of(TestTypeProvider.annotatedTypeVariable(), Boolean.TRUE),
                Arguments.of(TestTypeProvider.annotatedWildcardType(), Boolean.TRUE));
    }
}
