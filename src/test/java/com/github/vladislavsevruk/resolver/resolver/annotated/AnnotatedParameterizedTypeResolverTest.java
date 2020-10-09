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

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class AnnotatedParameterizedTypeResolverTest {

    private AnnotatedParameterizedTypeResolver realAnnotatedParameterizedTypeResolver
            = new AnnotatedParameterizedTypeResolver(new TypeMetaResolverPicker());

    @ParameterizedTest
    @MethodSource("canResolveProvider")
    void canResolveTest(AnnotatedType type, Boolean expectedValue) {
        Assertions.assertEquals(expectedValue, realAnnotatedParameterizedTypeResolver.canResolve(type));
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveSeveralArgumentsAnnotatedParameterizedTypeRealContextTest() {
        ParameterizedType parameterizedType = Mockito.mock(ParameterizedType.class);
        Mockito.when(parameterizedType.getRawType()).thenReturn(Double.class);
        AnnotatedType innerType1 = Mockito.mock(AnnotatedType.class);
        AnnotatedType innerType2 = Mockito.mock(AnnotatedType.class);
        TypeVariable<? extends Class<?>> typeVariable1 = Mockito.mock(TypeVariable.class);
        TypeVariable<? extends Class<?>> typeVariable2 = Mockito.mock(TypeVariable.class);
        Mockito.when(innerType1.getType()).thenReturn(typeVariable1);
        Mockito.when(innerType2.getType()).thenReturn(typeVariable2);
        AnnotatedType[] annotatedTypeArray = new AnnotatedType[]{ innerType1, innerType2 };
        AnnotatedParameterizedType annotatedParameterizedType = Mockito.mock(AnnotatedParameterizedType.class);
        Mockito.when(annotatedParameterizedType.getAnnotatedActualTypeArguments()).thenReturn(annotatedTypeArray);
        Mockito.when(annotatedParameterizedType.getType()).thenReturn(parameterizedType);
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta innerTypeMeta1 = new TypeMeta<>(Integer.class);
        TypeMeta innerTypeMeta2 = new TypeMeta<>(Long.class);
        Mockito.when(typeVariableMap.getActualType(typeVariable1)).thenReturn(innerTypeMeta1);
        Mockito.when(typeVariableMap.getActualType(typeVariable2)).thenReturn(innerTypeMeta2);
        TypeMeta<?> result = realAnnotatedParameterizedTypeResolver
                .resolve(typeVariableMap, annotatedParameterizedType);
        TypeMeta<?> expectedMeta = new TypeMeta<>(Double.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        Assertions.assertEquals(expectedMeta, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveSeveralArgumentsTypeAnnotatedParameterizedTypeTest() {
        ParameterizedType parameterizedType = Mockito.mock(ParameterizedType.class);
        Mockito.when(parameterizedType.getRawType()).thenReturn(Double.class);
        AnnotatedType innerType1 = Mockito.mock(AnnotatedType.class);
        AnnotatedType innerType2 = Mockito.mock(AnnotatedType.class);
        Type innerActualType1 = Mockito.mock(Type.class);
        Type innerActualType2 = Mockito.mock(Type.class);
        Mockito.when(innerType1.getType()).thenReturn(innerActualType1);
        Mockito.when(innerType2.getType()).thenReturn(innerActualType2);
        AnnotatedType[] annotatedTypeArray = new AnnotatedType[]{ innerType1, innerType2 };
        AnnotatedParameterizedType annotatedParameterizedType = Mockito.mock(AnnotatedParameterizedType.class);
        Mockito.when(annotatedParameterizedType.getAnnotatedActualTypeArguments()).thenReturn(annotatedTypeArray);
        Mockito.when(annotatedParameterizedType.getType()).thenReturn(parameterizedType);
        TypeResolver typeResolver = Mockito.mock(TypeResolver.class);
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta innerTypeMeta1 = new TypeMeta<>(Integer.class);
        TypeMeta innerTypeMeta2 = new TypeMeta<>(Long.class);
        Mockito.when(typeResolver.resolve(typeVariableMap, innerActualType1)).thenReturn(innerTypeMeta1);
        Mockito.when(typeResolver.resolve(typeVariableMap, innerActualType2)).thenReturn(innerTypeMeta2);
        TypeResolverPicker typeResolverPicker = Mockito.mock(TypeResolverPicker.class);
        Mockito.when(typeResolverPicker.pickTypeResolver(innerActualType1)).thenReturn(typeResolver);
        Mockito.when(typeResolverPicker.pickTypeResolver(innerActualType2)).thenReturn(typeResolver);
        AnnotatedParameterizedTypeResolver annotatedTypeBaseResolver = new AnnotatedParameterizedTypeResolver(
                typeResolverPicker);
        TypeMeta<?> result = annotatedTypeBaseResolver.resolve(typeVariableMap, annotatedParameterizedType);
        TypeMeta<?> expectedMeta = new TypeMeta<>(Double.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        Assertions.assertEquals(expectedMeta, result);
    }

    @Test
    void resolveSingleArgumentAnnotatedParameterizedTypeRealContextTest() {
        ParameterizedType parameterizedType = Mockito.mock(ParameterizedType.class);
        Mockito.when(parameterizedType.getRawType()).thenReturn(Byte.class);
        AnnotatedType innerType = Mockito.mock(AnnotatedType.class);
        Class<?> innerActualType = String.class;
        Mockito.when(innerType.getType()).thenReturn(innerActualType);
        AnnotatedType[] annotatedTypeArray = new AnnotatedType[]{ innerType };
        AnnotatedParameterizedType annotatedParameterizedType = Mockito.mock(AnnotatedParameterizedType.class);
        Mockito.when(annotatedParameterizedType.getAnnotatedActualTypeArguments()).thenReturn(annotatedTypeArray);
        Mockito.when(annotatedParameterizedType.getType()).thenReturn(parameterizedType);
        TypeVariableMap<TypeMeta<?>> typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta innerTypeMeta = new TypeMeta<>(String.class);
        TypeMeta<?> result = realAnnotatedParameterizedTypeResolver
                .resolve(typeVariableMap, annotatedParameterizedType);
        TypeMeta<?> expectedMeta = new TypeMeta<>(Byte.class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedMeta, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveSingleArgumentTypeAnnotatedParameterizedTypeTest() {
        ParameterizedType parameterizedType = Mockito.mock(ParameterizedType.class);
        Mockito.when(parameterizedType.getRawType()).thenReturn(Byte.class);
        AnnotatedType innerType = Mockito.mock(AnnotatedType.class);
        Type innerActualType = Mockito.mock(Type.class);
        Mockito.when(innerType.getType()).thenReturn(innerActualType);
        AnnotatedType[] annotatedTypeArray = new AnnotatedType[]{ innerType };
        AnnotatedParameterizedType annotatedParameterizedType = Mockito.mock(AnnotatedParameterizedType.class);
        Mockito.when(annotatedParameterizedType.getAnnotatedActualTypeArguments()).thenReturn(annotatedTypeArray);
        Mockito.when(annotatedParameterizedType.getType()).thenReturn(parameterizedType);
        TypeResolver typeResolver = Mockito.mock(TypeResolver.class);
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta innerTypeMeta = new TypeMeta<>(Float.class);
        Mockito.when(typeResolver.resolve(typeVariableMap, innerActualType)).thenReturn(innerTypeMeta);
        TypeResolverPicker typeResolverPicker = Mockito.mock(TypeResolverPicker.class);
        Mockito.when(typeResolverPicker.pickTypeResolver(innerActualType)).thenReturn(typeResolver);
        AnnotatedParameterizedTypeResolver annotatedTypeBaseResolver = new AnnotatedParameterizedTypeResolver(
                typeResolverPicker);
        TypeMeta<?> result = annotatedTypeBaseResolver.resolve(typeVariableMap, annotatedParameterizedType);
        TypeMeta<?> expectedMeta = new TypeMeta<>(Byte.class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedMeta, result);
    }

    private static Stream<Arguments> canResolveProvider() {
        return Stream.of(Arguments.of(TestTypeProvider.annotatedParameterizedType(), Boolean.TRUE),
                Arguments.of(TestTypeProvider.annotatedArrayType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.annotatedTypeVariable(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.annotatedWildcardType(), Boolean.FALSE));
    }
}
