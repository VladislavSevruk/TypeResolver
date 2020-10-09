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

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class AnnotatedArrayTypeResolverTest {

    private AnnotatedArrayTypeResolver realAnnotatedArrayTypeResolver = new AnnotatedArrayTypeResolver(
            new TypeMetaResolverPicker());

    @ParameterizedTest
    @MethodSource("canResolveProvider")
    void canResolveTest(AnnotatedType type, Boolean expectedValue) {
        Assertions.assertEquals(expectedValue, realAnnotatedArrayTypeResolver.canResolve(type));
    }

    @Test
    void resolveAnnotatedArrayTypeRealContextTest() {
        AnnotatedType annotatedInnerType = Mockito.mock(AnnotatedType.class);
        Mockito.when(annotatedInnerType.getType()).thenReturn(Short.class);
        AnnotatedArrayType annotatedArrayType = Mockito.mock(AnnotatedArrayType.class);
        Mockito.when(annotatedArrayType.getAnnotatedGenericComponentType()).thenReturn(annotatedInnerType);
        Mockito.when(annotatedArrayType.getType()).thenReturn(Short[].class);
        TypeVariableMap<TypeMeta<?>> typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta innerTypeMeta = new TypeMeta<>(Short.class);
        TypeMeta<?> result = realAnnotatedArrayTypeResolver.resolve(typeVariableMap, annotatedArrayType);
        TypeMeta<?> expectedMeta = new TypeMeta<>(Short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedMeta, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveAnnotatedArrayTypeTest() {
        Type innerType = Mockito.mock(Type.class);
        AnnotatedType annotatedInnerType = Mockito.mock(AnnotatedType.class);
        Mockito.when(annotatedInnerType.getType()).thenReturn(innerType);
        AnnotatedArrayType annotatedArrayType = Mockito.mock(AnnotatedArrayType.class);
        Mockito.when(annotatedArrayType.getAnnotatedGenericComponentType()).thenReturn(annotatedInnerType);
        Mockito.when(annotatedArrayType.getType()).thenReturn(Character[].class);
        TypeResolver typeResolver = Mockito.mock(TypeResolver.class);
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta innerTypeMeta = new TypeMeta<>(Character.class);
        Mockito.when(typeResolver.resolve(typeVariableMap, innerType)).thenReturn(innerTypeMeta);
        TypeResolverPicker typeResolverPicker = Mockito.mock(TypeResolverPicker.class);
        Mockito.when(typeResolverPicker.pickTypeResolver(innerType)).thenReturn(typeResolver);
        AnnotatedArrayTypeResolver annotatedArrayTypeResolver = new AnnotatedArrayTypeResolver(typeResolverPicker);
        TypeMeta<?> result = annotatedArrayTypeResolver.resolve(typeVariableMap, annotatedArrayType);
        TypeMeta<?> expectedMeta = new TypeMeta<>(Character[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedMeta, result);
    }

    private static Stream<Arguments> canResolveProvider() {
        return Stream.of(Arguments.of(TestTypeProvider.annotatedArrayType(), Boolean.TRUE),
                Arguments.of(TestTypeProvider.annotatedParameterizedType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.annotatedTypeVariable(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.annotatedWildcardType(), Boolean.FALSE));
    }
}
