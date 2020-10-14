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
package com.github.vladislavsevruk.resolver.resolver.picker;

import com.github.vladislavsevruk.resolver.exception.TypeResolvingException;
import com.github.vladislavsevruk.resolver.resolver.annotated.AnnotatedTypeBaseResolver;
import com.github.vladislavsevruk.resolver.resolver.annotated.AnnotatedTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.annotated.array.AnnotatedArrayTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.annotated.parameterized.AnnotatedParameterizedTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.TypeResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.clazz.ClassTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.generic.GenericArrayTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.parameterized.ParameterizedTypeResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.variable.TypeVariableResolver;
import com.github.vladislavsevruk.resolver.resolver.simple.wildcard.WildcardTypeResolver;
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
class TypeMetaResolverPickerTest {

    private TypeMetaResolverPicker typeResolverPicker = new TypeMetaResolverPicker();

    @Test
    void pickAnnotatedTypeResolverForUnknownAnnotatedTypeReturnsDefaultTypeResolvedTest() {
        AnnotatedType annotatedType = Mockito.mock(AnnotatedType.class);
        AnnotatedTypeResolver pickedAnnotatedTypeResolver = typeResolverPicker.pickAnnotatedTypeResolver(annotatedType);
        Assertions.assertEquals(AnnotatedTypeBaseResolver.class, pickedAnnotatedTypeResolver.getClass());
    }

    @ParameterizedTest
    @MethodSource("pickAnnotatedTypeResolverProvider")
    void pickAnnotatedTypeResolverTest(AnnotatedType type,
            Class<? extends AnnotatedTypeResolver> expectedAnnotatedTypeResolver) {
        AnnotatedTypeResolver pickedAnnotatedTypeResolver = typeResolverPicker.pickAnnotatedTypeResolver(type);
        Assertions.assertEquals(expectedAnnotatedTypeResolver, pickedAnnotatedTypeResolver.getClass());
    }

    @Test
    void pickTypeResolverForUnknownTypeThrowsExceptionTest() {
        Type type = Mockito.mock(Type.class);
        Assertions.assertThrows(TypeResolvingException.class, () -> typeResolverPicker.pickTypeResolver(type));
    }

    @ParameterizedTest
    @MethodSource("pickTypeResolverProvider")
    void pickTypeResolverTest(Type type, Class<? extends TypeResolver> expectedTypeResolver) {
        TypeResolver pickedTypeResolver = typeResolverPicker.pickTypeResolver(type);
        Assertions.assertEquals(expectedTypeResolver, pickedTypeResolver.getClass());
    }

    private static Stream<Arguments> pickAnnotatedTypeResolverProvider() {
        return Stream.of(Arguments.of(TestTypeProvider.annotatedArrayType(), AnnotatedArrayTypeResolver.class),
                Arguments.of(TestTypeProvider.annotatedParameterizedType(), AnnotatedParameterizedTypeResolver.class),
                Arguments.of(TestTypeProvider.annotatedTypeVariable(), AnnotatedTypeBaseResolver.class),
                Arguments.of(TestTypeProvider.annotatedWildcardType(), AnnotatedTypeBaseResolver.class));
    }

    private static Stream<Arguments> pickTypeResolverProvider() {
        return Stream.of(Arguments.of(TestTypeProvider.parameterizedType(), ParameterizedTypeResolver.class),
                Arguments.of(TestTypeProvider.arrayType(), ClassTypeResolver.class),
                Arguments.of(TestTypeProvider.classType(), ClassTypeResolver.class),
                Arguments.of(TestTypeProvider.genericArrayType(), GenericArrayTypeResolver.class),
                Arguments.of(TestTypeProvider.typeVariable(), TypeVariableResolver.class),
                Arguments.of(TestTypeProvider.wildcardType(), WildcardTypeResolver.class));
    }
}
