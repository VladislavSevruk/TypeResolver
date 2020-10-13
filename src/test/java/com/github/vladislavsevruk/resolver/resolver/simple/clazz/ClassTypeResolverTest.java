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
package com.github.vladislavsevruk.resolver.resolver.simple.clazz;

import com.github.vladislavsevruk.resolver.resolver.picker.TypeMetaResolverPicker;
import com.github.vladislavsevruk.resolver.resolver.picker.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.resolver.simple.TypeResolver;
import com.github.vladislavsevruk.resolver.test.data.TestTypeProvider;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Type;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class ClassTypeResolverTest {

    @Mock
    private TypeResolverPicker<TypeMeta<?>> mockTypeResolverPicker;
    private ClassTypeResolver mockClassTypeResolver = new ClassTypeResolver(mockTypeResolverPicker);
    private ClassTypeResolver realClassTypeResolver = new ClassTypeResolver(new TypeMetaResolverPicker());

    @ParameterizedTest
    @MethodSource("canResolveProvider")
    void canResolveTest(Type type, Boolean expectedValue) {
        Assertions.assertEquals(expectedValue, realClassTypeResolver.canResolve(type));
    }

    @Test
    void resolveArrayTypeRealContextTest() {
        TypeVariableMap<TypeMeta<?>> typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta<?> result = realClassTypeResolver.resolve(typeVariableMap, String[].class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(String[].class,
                new TypeMeta<?>[]{ new TypeMeta<>(String.class) });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveArrayTypeTest() {
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta typeMeta = new TypeMeta<>(Long.class);
        TypeResolver typeResolver = Mockito.mock(TypeResolver.class);
        Mockito.when(typeResolver.resolve(typeVariableMap, String.class)).thenReturn(typeMeta);
        TypeResolverPicker typeResolverPicker = Mockito.mock(TypeResolverPicker.class);
        Mockito.when(typeResolverPicker.pickTypeResolver(String.class)).thenReturn(typeResolver);
        ClassTypeResolver classTypeResolver = new ClassTypeResolver(typeResolverPicker);
        TypeMeta<?> result = classTypeResolver.resolve(typeVariableMap, String[].class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(String[].class, new TypeMeta<?>[]{ typeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void resolveClassTypeTest() {
        TypeVariableMap<TypeMeta<?>> typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta<?> result = mockClassTypeResolver.resolve(typeVariableMap, String.class);
        Assertions.assertEquals(new TypeMeta<>(String.class), result);
    }

    @Test
    void resolvePrimitiveArrayTypeRealContextTest() {
        TypeVariableMap<TypeMeta<?>> typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta<?> result = realClassTypeResolver.resolve(typeVariableMap, byte[].class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(byte[].class, new TypeMeta<?>[]{ new TypeMeta<>(byte.class) });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void resolvePrimitiveClassTypeTest() {
        TypeVariableMap<TypeMeta<?>> typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta<?> result = mockClassTypeResolver.resolve(typeVariableMap, long.class);
        Assertions.assertEquals(new TypeMeta<>(long.class), result);
    }

    @Test
    void resolveWrapperArrayTypeRealContextTest() {
        TypeVariableMap<TypeMeta<?>> typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta<?> result = realClassTypeResolver.resolve(typeVariableMap, Short[].class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Short[].class, new TypeMeta<?>[]{ new TypeMeta<>(Short.class) });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void resolveWrapperClassTypeTest() {
        TypeVariableMap<TypeMeta<?>> typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta<?> result = mockClassTypeResolver.resolve(typeVariableMap, Integer.class);
        Assertions.assertEquals(new TypeMeta<>(Integer.class), result);
    }

    private static Stream<Arguments> canResolveProvider() {
        return Stream.of(Arguments.of(TestTypeProvider.arrayType(), Boolean.TRUE),
                Arguments.of(TestTypeProvider.classType(), Boolean.TRUE),
                Arguments.of(TestTypeProvider.genericArrayType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.parameterizedType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.typeVariable(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.wildcardType(), Boolean.FALSE));
    }
}
