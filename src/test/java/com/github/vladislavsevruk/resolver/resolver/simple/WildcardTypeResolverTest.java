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

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class WildcardTypeResolverTest {

    private WildcardTypeResolver realWildcardTypeResolver = new WildcardTypeResolver(new TypeResolverPickerImpl());

    @ParameterizedTest
    @MethodSource("canResolveProvider")
    void canResolveTest(Type type, Boolean expectedValue) {
        Assertions.assertEquals(expectedValue, realWildcardTypeResolver.canResolve(type));
    }

    @Test
    void resolveWildcardRealContextType() {
        Class<?> boundType = Boolean.class;
        WildcardType wildcardType = Mockito.mock(WildcardType.class);
        Mockito.when(wildcardType.getUpperBounds()).thenReturn(new Type[]{ boundType });
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta<?> result = realWildcardTypeResolver.resolve(typeVariableMap, wildcardType);
        Assertions.assertEquals(new TypeMeta<>(boundType, true), result);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveWildcardTypeTest() {
        Type boundType = Mockito.mock(Type.class);
        WildcardType wildcardType = Mockito.mock(WildcardType.class);
        Mockito.when(wildcardType.getUpperBounds()).thenReturn(new Type[]{ boundType });
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeResolver typeResolver = Mockito.mock(TypeResolver.class);
        TypeMeta typeMeta = new TypeMeta<>(Double.class, true);
        Mockito.when(typeResolver.resolve(typeVariableMap, boundType)).thenReturn(typeMeta);
        TypeResolverPicker typeResolverPicker = Mockito.mock(TypeResolverPicker.class);
        Mockito.when(typeResolverPicker.pickTypeResolver(boundType)).thenReturn(typeResolver);
        WildcardTypeResolver wildcardTypeResolver = new WildcardTypeResolver(typeResolverPicker);
        TypeMeta<?> result = wildcardTypeResolver.resolve(typeVariableMap, wildcardType);
        Assertions.assertEquals(typeMeta, result);
    }

    private static Stream<Arguments> canResolveProvider() {
        return Stream.of(Arguments.of(TestTypeProvider.wildcardType(), Boolean.TRUE),
                Arguments.of(TestTypeProvider.arrayType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.classType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.genericArrayType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.parameterizedType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.typeVariable(), Boolean.FALSE));
    }
}
