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
import java.lang.reflect.TypeVariable;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class TypeVariableResolverTest {

    private TypeVariableResolver resolver = new TypeVariableResolver();

    @ParameterizedTest
    @MethodSource("canResolveProvider")
    void canResolveTest(Type type, Boolean expectedValue) {
        Assertions.assertEquals(expectedValue, resolver.canResolve(type));
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveTypeVariableTest() {
        TypeVariable<? extends Class<?>> typeVariable = Mockito.mock(TypeVariable.class);
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta typeMeta = new TypeMeta<>(Boolean.class);
        Mockito.when(typeVariableMap.getActualType(typeVariable)).thenReturn(typeMeta);
        TypeMeta<?> result = resolver.resolve(typeVariableMap, typeVariable);
        Assertions.assertEquals(typeMeta, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveUnknownTypeVariableTest() {
        TypeVariable<? extends Class<?>> typeVariable = Mockito.mock(TypeVariable.class);
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        Mockito.when(typeVariableMap.getActualType(typeVariable)).thenReturn(null);
        TypeMeta<?> result = resolver.resolve(typeVariableMap, typeVariable);
        Assertions.assertEquals(TypeMeta.WILDCARD_META, result);
    }

    private static Stream<Arguments> canResolveProvider() {
        return Stream.of(Arguments.of(TestTypeProvider.typeVariable(), Boolean.TRUE),
                Arguments.of(TestTypeProvider.arrayType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.classType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.genericArrayType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.parameterizedType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.wildcardType(), Boolean.FALSE));
    }
}
