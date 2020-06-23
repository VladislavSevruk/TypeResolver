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
import com.github.vladislavsevruk.resolver.test.data.TestTypeProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class TypeResolverPickerImplTest {

    private TypeResolverPickerImpl typeResolverPicker = new TypeResolverPickerImpl();

    @Test
    void pickAnnotatedTypeResolverForUnknownAnnotatedTypeReturnsDefaultTypeResolvedTest() {
        AnnotatedType annotatedType = Mockito.mock(AnnotatedType.class);
        AnnotatedTypeResolver pickedAnnotatedTypeResolver = typeResolverPicker.pickAnnotatedTypeResolver(annotatedType);
        AnnotatedTypeResolver expectedAnnotatedTypeResolver = new AnnotatedTypeBaseResolver(typeResolverPicker);
        Assertions.assertEquals(expectedAnnotatedTypeResolver, pickedAnnotatedTypeResolver);
    }

    @ParameterizedTest
    @MethodSource("pickAnnotatedTypeResolverProvider")
    void pickAnnotatedTypeResolverTest(AnnotatedType type, AnnotatedTypeResolver expectedAnnotatedTypeResolver) {
        AnnotatedTypeResolver pickedAnnotatedTypeResolver = typeResolverPicker.pickAnnotatedTypeResolver(type);
        Assertions.assertEquals(expectedAnnotatedTypeResolver, pickedAnnotatedTypeResolver);
    }

    @Test
    void pickTypeResolverForUnknownTypeThrowsExceptionTest() {
        Type type = Mockito.mock(Type.class);
        Assertions.assertThrows(TypeResolvingException.class, () -> typeResolverPicker.pickTypeResolver(type));
    }

    @ParameterizedTest
    @MethodSource("pickTypeResolverProvider")
    void pickTypeResolverTest(Type type, TypeResolver expectedTypeResolver) {
        TypeResolver pickedTypeResolver = typeResolverPicker.pickTypeResolver(type);
        Assertions.assertEquals(expectedTypeResolver, pickedTypeResolver);
    }

    private static Stream<Arguments> pickAnnotatedTypeResolverProvider() {
        TypeResolverPicker typeResolverPicker = Mockito.mock(TypeResolverPicker.class);
        return Stream.of(Arguments
                        .of(TestTypeProvider.annotatedArrayType(), new AnnotatedArrayTypeResolver(typeResolverPicker)),
                Arguments.of(TestTypeProvider.annotatedParameterizedType(),
                        new AnnotatedParameterizedTypeResolver(typeResolverPicker)), Arguments
                        .of(TestTypeProvider.annotatedTypeVariable(),
                                new AnnotatedTypeBaseResolver(typeResolverPicker)), Arguments
                        .of(TestTypeProvider.annotatedWildcardType(),
                                new AnnotatedTypeBaseResolver(typeResolverPicker)));
    }

    private static Stream<Arguments> pickTypeResolverProvider() {
        TypeResolverPicker typeResolverPicker = Mockito.mock(TypeResolverPicker.class);
        return Stream.of(Arguments
                        .of(TestTypeProvider.parameterizedType(), new ParameterizedTypeResolver(typeResolverPicker)),
                Arguments.of(TestTypeProvider.arrayType(), new ClassTypeResolver(typeResolverPicker)),
                Arguments.of(TestTypeProvider.classType(), new ClassTypeResolver(typeResolverPicker)),
                Arguments.of(TestTypeProvider.genericArrayType(), new GenericArrayTypeResolver(typeResolverPicker)),
                Arguments.of(TestTypeProvider.typeVariable(), new TypeVariableResolver()),
                Arguments.of(TestTypeProvider.wildcardType(), new WildcardTypeResolver(typeResolverPicker)));
    }
}
