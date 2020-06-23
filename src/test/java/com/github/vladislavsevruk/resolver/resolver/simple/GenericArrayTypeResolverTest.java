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
package com.github.vladislavsevruk.resolver.resolver.simple;

import com.github.vladislavsevruk.resolver.resolver.TypeResolver;
import com.github.vladislavsevruk.resolver.resolver.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.resolver.TypeResolverPickerImpl;
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

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class GenericArrayTypeResolverTest {

    private GenericArrayTypeResolver realGenericArrayTypeResolver = new GenericArrayTypeResolver(
            new TypeResolverPickerImpl());

    @ParameterizedTest
    @MethodSource("canResolveProvider")
    void canResolveTest(Type type, Boolean expectedValue) {
        Assertions.assertEquals(expectedValue, realGenericArrayTypeResolver.canResolve(type));
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveGenericArrayTypeRealContextTest() {
        TypeVariable typeVariable = Mockito.mock(TypeVariable.class);
        ParameterizedType parameterizedType = Mockito.mock(ParameterizedType.class);
        Mockito.when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[]{ typeVariable });
        Mockito.when(parameterizedType.getRawType()).thenReturn(Boolean.class);
        GenericArrayType genericArrayType = Mockito.mock(GenericArrayType.class);
        Mockito.when(genericArrayType.getGenericComponentType()).thenReturn(parameterizedType);
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta typeVariableRealMeta = new TypeMeta<>(Character.class);
        Mockito.when(typeVariableMap.getActualType(typeVariable)).thenReturn(typeVariableRealMeta);
        TypeMeta<?> result = realGenericArrayTypeResolver.resolve(typeVariableMap, genericArrayType);
        TypeMeta<?> typeMeta = new TypeMeta<>(Boolean.class, new TypeMeta<?>[]{ typeVariableRealMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Boolean[].class, new TypeMeta<?>[]{ typeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveGenericArrayTypeTest() {
        GenericArrayType genericArrayType = Mockito.mock(GenericArrayType.class);
        Type type = Mockito.mock(Type.class);
        Mockito.when(genericArrayType.getGenericComponentType()).thenReturn(type);
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta typeMeta = new TypeMeta<>(Byte.class);
        TypeResolver typeResolver = Mockito.mock(TypeResolver.class);
        Mockito.when(typeResolver.resolve(typeVariableMap, type)).thenReturn(typeMeta);
        TypeResolverPicker typeResolverPicker = Mockito.mock(TypeResolverPicker.class);
        Mockito.when(typeResolverPicker.pickTypeResolver(type)).thenReturn(typeResolver);
        GenericArrayTypeResolver genericArrayTypeResolver = new GenericArrayTypeResolver(typeResolverPicker);
        TypeMeta<?> result = genericArrayTypeResolver.resolve(typeVariableMap, genericArrayType);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Byte[].class, new TypeMeta<?>[]{ typeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    private static Stream<Arguments> canResolveProvider() {
        return Stream.of(Arguments.of(TestTypeProvider.genericArrayType(), Boolean.TRUE),
                Arguments.of(TestTypeProvider.arrayType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.classType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.parameterizedType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.typeVariable(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.wildcardType(), Boolean.FALSE));
    }
}
