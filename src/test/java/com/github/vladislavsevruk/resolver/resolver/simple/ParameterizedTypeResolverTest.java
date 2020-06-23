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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class ParameterizedTypeResolverTest {

    private ParameterizedTypeResolver realParameterizedTypeResolver = new ParameterizedTypeResolver(
            new TypeResolverPickerImpl());

    @ParameterizedTest
    @MethodSource("canResolveProvider")
    void canResolveTest(Type type, Boolean expectedValue) {
        Assertions.assertEquals(expectedValue, realParameterizedTypeResolver.canResolve(type));
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveSeveralArgumentTypesParameterizedTypeRealContextTest() {
        TypeVariable typeVariable1 = Mockito.mock(TypeVariable.class);
        TypeVariable typeVariable2 = Mockito.mock(TypeVariable.class);
        ParameterizedType parameterizedType = Mockito.mock(ParameterizedType.class);
        Mockito.when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[]{ typeVariable1, typeVariable2 });
        Mockito.when(parameterizedType.getRawType()).thenReturn(Integer.class);
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta typeMeta1 = new TypeMeta<>(Short.class);
        TypeMeta typeMeta2 = new TypeMeta<>(Float.class);
        Mockito.when(typeVariableMap.getActualType(typeVariable1)).thenReturn(typeMeta1);
        Mockito.when(typeVariableMap.getActualType(typeVariable2)).thenReturn(typeMeta2);
        TypeMeta<?> result = realParameterizedTypeResolver.resolve(typeVariableMap, parameterizedType);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Integer.class, new TypeMeta<?>[]{ typeMeta1, typeMeta2 });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveSeveralArgumentTypesParameterizedTypeTest() {
        TypeVariable typeVariable1 = Mockito.mock(TypeVariable.class);
        TypeVariable typeVariable2 = Mockito.mock(TypeVariable.class);
        ParameterizedType parameterizedType = Mockito.mock(ParameterizedType.class);
        Mockito.when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[]{ typeVariable1, typeVariable2 });
        Mockito.when(parameterizedType.getRawType()).thenReturn(Short.class);
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta typeMeta1 = new TypeMeta<>(Double.class);
        TypeMeta typeMeta2 = new TypeMeta<>(Float.class);
        TypeResolver typeResolver = Mockito.mock(TypeResolver.class);
        Mockito.when(typeResolver.resolve(typeVariableMap, typeVariable1)).thenReturn(typeMeta1);
        Mockito.when(typeResolver.resolve(typeVariableMap, typeVariable2)).thenReturn(typeMeta2);
        TypeResolverPicker typeResolverPicker = Mockito.mock(TypeResolverPicker.class);
        Mockito.when(typeResolverPicker.pickTypeResolver(typeVariable1)).thenReturn(typeResolver);
        Mockito.when(typeResolverPicker.pickTypeResolver(typeVariable2)).thenReturn(typeResolver);
        ParameterizedTypeResolver parameterizedTypeResolver = new ParameterizedTypeResolver(typeResolverPicker);
        TypeMeta<?> result = parameterizedTypeResolver.resolve(typeVariableMap, parameterizedType);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Short.class, new TypeMeta<?>[]{ typeMeta1, typeMeta2 });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveSingleArgumentTypeParameterizedTypeRealContextTest() {
        TypeVariable typeVariable = Mockito.mock(TypeVariable.class);
        ParameterizedType parameterizedType = Mockito.mock(ParameterizedType.class);
        Mockito.when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[]{ typeVariable });
        Mockito.when(parameterizedType.getRawType()).thenReturn(Boolean.class);
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta typeMeta = new TypeMeta<>(Float.class);
        Mockito.when(typeVariableMap.getActualType(typeVariable)).thenReturn(typeMeta);
        TypeMeta<?> result = realParameterizedTypeResolver.resolve(typeVariableMap, parameterizedType);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Boolean.class, new TypeMeta<?>[]{ typeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveSingleArgumentTypeParameterizedTypeTest() {
        TypeVariable typeVariable = Mockito.mock(TypeVariable.class);
        ParameterizedType parameterizedType = Mockito.mock(ParameterizedType.class);
        Mockito.when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[]{ typeVariable });
        Mockito.when(parameterizedType.getRawType()).thenReturn(String.class);
        TypeVariableMap typeVariableMap = Mockito.mock(TypeVariableMap.class);
        TypeMeta typeMeta = new TypeMeta<>(Long.class);
        TypeResolver typeResolver = Mockito.mock(TypeResolver.class);
        Mockito.when(typeResolver.resolve(typeVariableMap, typeVariable)).thenReturn(typeMeta);
        TypeResolverPicker typeResolverPicker = Mockito.mock(TypeResolverPicker.class);
        Mockito.when(typeResolverPicker.pickTypeResolver(typeVariable)).thenReturn(typeResolver);
        ParameterizedTypeResolver parameterizedTypeResolver = new ParameterizedTypeResolver(typeResolverPicker);
        TypeMeta<?> result = parameterizedTypeResolver.resolve(typeVariableMap, parameterizedType);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(String.class, new TypeMeta<?>[]{ typeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    private static Stream<Arguments> canResolveProvider() {
        return Stream.of(Arguments.of(TestTypeProvider.parameterizedType(), Boolean.TRUE),
                Arguments.of(TestTypeProvider.arrayType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.classType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.genericArrayType(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.typeVariable(), Boolean.FALSE),
                Arguments.of(TestTypeProvider.wildcardType(), Boolean.FALSE));
    }
}
