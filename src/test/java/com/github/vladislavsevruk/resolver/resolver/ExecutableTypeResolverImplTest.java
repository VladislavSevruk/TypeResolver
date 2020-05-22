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
import com.github.vladislavsevruk.resolver.test.data.TestModel;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExecutableTypeResolverImplTest {

    private ExecutableTypeResolverImpl executableTypeResolver = new ExecutableTypeResolverImpl();

    @Test
    public void getGenericReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getGenericReturnType"));
        Assertions.assertEquals(TypeMeta.OBJECT_META, result);
    }

    @Test
    public void getGenericReturnTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Float.class);
        TypeMeta<?> secondClassParameterTypeMeta = new TypeMeta<>(Long.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ expectedTypeMeta, secondClassParameterTypeMeta });
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeMeta, TestModel.class.getMethod("getGenericReturnType"));
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getGenericReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Float, Long>>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getGenericReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Float.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getNoParameterTypeClassTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver
                .getParameterTypes(TestModel.class, TestModel.class.getMethod("getNoParameterType"));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void getNoParameterTypeTypeMetaTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver
                .getParameterTypes(new TypeMeta<>(TestModel.class), TestModel.class.getMethod("getNoParameterType"));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void getNoParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<?> result = executableTypeResolver
                .getParameterTypes(typeProvider, TestModel.class.getMethod("getNoParameterType"));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void getParameterTypeNotOverridenObjectMethodThrowsExceptionClassTest() {
        Assertions.assertThrows(TypeResolvingException.class,
                () -> executableTypeResolver.getParameterTypes(TestModel.class, TestModel.class.getMethod("wait")));
    }

    @Test
    public void getParameterTypeNotOverridenObjectMethodThrowsExceptionTypeMetaTest() {
        Assertions.assertThrows(TypeResolvingException.class, () -> executableTypeResolver
                .getParameterTypes(new TypeMeta<>(TestModel.class), TestModel.class.getMethod("wait")));
    }

    @Test
    public void getParameterTypeNotOverridenObjectMethodThrowsExceptionTypeProviderTest() {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        Assertions.assertThrows(TypeResolvingException.class,
                () -> executableTypeResolver.getParameterTypes(typeProvider, TestModel.class.getMethod("wait")));
    }

    @Test
    public void getParameterizedArrayReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getParameterizedArrayReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Object[].class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedArrayReturnTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Character.class);
        TypeMeta<?> secondClassParameterTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ innerTypeMeta, secondClassParameterTypeMeta });
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeMeta, TestModel.class.getMethod("getParameterizedArrayReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Character[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedArrayReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Character, Integer>>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getParameterizedArrayReturnType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Character.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Character[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeInnerArrayTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getParameterizedReturnTypeInnerArrayType"));
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeInnerArrayTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver.getReturnType(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getParameterizedReturnTypeInnerArrayType"));
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeInnerArrayTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getParameterizedReturnTypeInnerArrayType"));
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeInnerParameterTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver.getReturnType(TestModel.class,
                TestModel.class.getMethod("getParameterizedReturnTypeInnerParameterType"));
        TypeMeta<?> setTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ setTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeInnerParameterTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Short.class);
        TypeMeta<?> secondClassParameterTypeMeta = new TypeMeta<>(Long.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ innerTypeMeta, secondClassParameterTypeMeta });
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeMeta, TestModel.class.getMethod("getParameterizedReturnTypeInnerParameterType"));
        TypeMeta<?> setTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ setTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeInnerParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Short, Long>>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getParameterizedReturnTypeInnerParameterType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Short.class);
        TypeMeta<?> setTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ setTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeInnerParameterizedArrayTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver.getReturnType(TestModel.class,
                TestModel.class.getMethod("getParameterizedReturnTypeInnerParameterizedArrayType"));
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Object[].class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeInnerParameterizedArrayTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Short.class);
        TypeMeta<?> secondClassParameterTypeMeta = new TypeMeta<>(String.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ innerTypeMeta, secondClassParameterTypeMeta });
        TypeMeta<?> result = executableTypeResolver.getReturnType(typeMeta,
                TestModel.class.getMethod("getParameterizedReturnTypeInnerParameterizedArrayType"));
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeInnerParameterizedArrayTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Short, String>>() {};
        TypeMeta<?> result = executableTypeResolver.getReturnType(typeProvider,
                TestModel.class.getMethod("getParameterizedReturnTypeInnerParameterizedArrayType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Short.class);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeSeveralInnerParameterTypesClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver.getReturnType(TestModel.class,
                TestModel.class.getMethod("getParameterizedReturnTypeSeveralInnerParameterTypes"));
        TypeMeta<?> mapTypeMeta = new TypeMeta<>(Map.class,
                new TypeMeta<?>[]{ TypeMeta.OBJECT_META, TypeMeta.OBJECT_META });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ mapTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeSeveralInnerParameterTypesTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Short.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Byte.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        TypeMeta<?> result = executableTypeResolver.getReturnType(typeMeta,
                TestModel.class.getMethod("getParameterizedReturnTypeSeveralInnerParameterTypes"));
        TypeMeta<?> mapTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ mapTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeSeveralInnerParameterTypesTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Short, Byte>>() {};
        TypeMeta<?> result = executableTypeResolver.getReturnType(typeProvider,
                TestModel.class.getMethod("getParameterizedReturnTypeSeveralInnerParameterTypes"));
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Short.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Byte.class);
        TypeMeta<?> mapTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ mapTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeSeveralParameterTypesClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver.getReturnType(TestModel.class,
                TestModel.class.getMethod("getParameterizedReturnTypeSeveralParameterTypes"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class,
                new TypeMeta<?>[]{ TypeMeta.OBJECT_META, TypeMeta.OBJECT_META });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeSeveralParameterTypesTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(String.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Boolean.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeMeta, TestModel.class.getMethod("getParameterizedReturnTypeSeveralParameterTypes"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeSeveralParameterTypesTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<String, Boolean>>() {};
        TypeMeta<?> result = executableTypeResolver.getReturnType(typeProvider,
                TestModel.class.getMethod("getParameterizedReturnTypeSeveralParameterTypes"));
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(String.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Boolean.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeSingleParameterTypesClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver.getReturnType(TestModel.class,
                TestModel.class.getMethod("getParameterizedReturnTypeSingleParameterTypes"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeSingleParameterTypesTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Character.class);
        TypeMeta<?> secondClassParameterTypeMeta = new TypeMeta<>(Byte.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ innerTypeMeta, secondClassParameterTypeMeta });
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeMeta, TestModel.class.getMethod("getParameterizedReturnTypeSingleParameterTypes"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getParameterizedReturnTypeSingleParameterTypesTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Character, Byte>>() {};
        TypeMeta<?> result = executableTypeResolver.getReturnType(typeProvider,
                TestModel.class.getMethod("getParameterizedReturnTypeSingleParameterTypes"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Character.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getPrimitiveArrayParameterTypeClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getPrimitiveArrayParameterType", short[].class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(short.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getPrimitiveArrayParameterTypeTypeMetaTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getPrimitiveArrayParameterType", short[].class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(short.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getPrimitiveArrayParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getPrimitiveArrayParameterType", short[].class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(short.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getPrimitiveArrayReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getPrimitiveArrayReturnType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(int.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getPrimitiveArrayReturnTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver.getReturnType(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getPrimitiveArrayReturnType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(int.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getPrimitiveArrayReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getPrimitiveArrayReturnType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(int.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getPrimitiveReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getPrimitiveReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getPrimitiveReturnTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(new TypeMeta<>(TestModel.class), TestModel.class.getMethod("getPrimitiveReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getPrimitiveReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getPrimitiveReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getReturnTypeNotOverridenObjectMethodThrowsExceptionClassTest() {
        Assertions.assertThrows(TypeResolvingException.class, () -> executableTypeResolver
                .getReturnType(new TypeMeta<>(TestModel.class), TestModel.class.getMethod("wait")));
    }

    @Test
    public void getReturnTypeNotOverridenObjectMethodThrowsExceptionTypeMetaTest() {
        Assertions.assertThrows(TypeResolvingException.class, () -> executableTypeResolver
                .getReturnType(new TypeMeta<>(TestModel.class), TestModel.class.getMethod("wait")));
    }

    @Test
    public void getReturnTypeNotOverridenObjectMethodThrowsExceptionTypeProviderTest() {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        Assertions.assertThrows(TypeResolvingException.class,
                () -> executableTypeResolver.getReturnType(typeProvider, TestModel.class.getMethod("wait")));
    }

    @Test
    public void getSeveralGenericParameterTypesClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSeveralGenericParameterTypes", Object.class, Object.class));
        Assertions.assertEquals(Arrays.asList(TypeMeta.OBJECT_META, TypeMeta.OBJECT_META), result);
    }

    @Test
    public void getSeveralGenericParameterTypesTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Long.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Integer.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeMeta,
                TestModel.class.getMethod("getSeveralGenericParameterTypes", Object.class, Object.class));
        Assertions.assertEquals(Arrays.asList(innerTypeMeta1, innerTypeMeta2), result);
    }

    @Test
    public void getSeveralGenericParameterTypesTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Long, Integer>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSeveralGenericParameterTypes", Object.class, Object.class));
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Long.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Integer.class);
        Assertions.assertEquals(Arrays.asList(innerTypeMeta1, innerTypeMeta2), result);
    }

    @Test
    public void getSeveralParameterizedParameterTypeSeveralInnerParameterTypesClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class, TestModel.class
                .getMethod("getSeveralParameterizedParameterTypeSeveralInnerParameterTypes", List.class, Set.class));
        TypeMeta<?> mapTypeMeta = new TypeMeta<>(Map.class,
                new TypeMeta<?>[]{ TypeMeta.OBJECT_META, TypeMeta.OBJECT_META });
        TypeMeta<?> mapEntryTypeMeta = new TypeMeta<>(Map.Entry.class,
                new TypeMeta<?>[]{ TypeMeta.OBJECT_META, TypeMeta.OBJECT_META });
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ mapTypeMeta });
        TypeMeta<?> setTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ mapEntryTypeMeta });
        Assertions.assertEquals(Arrays.asList(listTypeMeta, setTypeMeta), result);
    }

    @Test
    public void getSeveralParameterizedParameterTypeSeveralInnerParameterTypesTypeMetaTest()
            throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Long.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Integer.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeMeta, TestModel.class
                .getMethod("getSeveralParameterizedParameterTypeSeveralInnerParameterTypes", List.class, Set.class));
        TypeMeta<?> mapTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        TypeMeta<?> mapEntryTypeMeta = new TypeMeta<>(Map.Entry.class,
                new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ mapTypeMeta });
        TypeMeta<?> setTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ mapEntryTypeMeta });
        Assertions.assertEquals(Arrays.asList(listTypeMeta, setTypeMeta), result);
    }

    @Test
    public void getSeveralParameterizedParameterTypeSeveralInnerParameterTypesTypeProviderTest()
            throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Long, Integer>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider, TestModel.class
                .getMethod("getSeveralParameterizedParameterTypeSeveralInnerParameterTypes", List.class, Set.class));
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Long.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Integer.class);
        TypeMeta<?> mapTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        TypeMeta<?> mapEntryTypeMeta = new TypeMeta<>(Map.Entry.class,
                new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ mapTypeMeta });
        TypeMeta<?> setTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ mapEntryTypeMeta });
        Assertions.assertEquals(Arrays.asList(listTypeMeta, setTypeMeta), result);
    }

    @Test
    public void getSeveralParameterizedParameterTypesClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSeveralParameterizedParameterTypes", List.class, Set.class));
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        TypeMeta<?> setTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        Assertions.assertEquals(Arrays.asList(listTypeMeta, setTypeMeta), result);
    }

    @Test
    public void getSeveralParameterizedParameterTypesTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Byte.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Float.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeMeta,
                TestModel.class.getMethod("getSeveralParameterizedParameterTypes", List.class, Set.class));
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ innerTypeMeta1 });
        TypeMeta<?> setTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ innerTypeMeta2 });
        Assertions.assertEquals(Arrays.asList(listTypeMeta, setTypeMeta), result);
    }

    @Test
    public void getSeveralParameterizedParameterTypesTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Byte, Float>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSeveralParameterizedParameterTypes", List.class, Set.class));
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Byte.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Float.class);
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ innerTypeMeta1 });
        TypeMeta<?> setTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ innerTypeMeta2 });
        Assertions.assertEquals(Arrays.asList(listTypeMeta, setTypeMeta), result);
    }

    @Test
    public void getSeveralSimpleParameterTypesClassTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSeveralSimpleParameterTypes", Character.class, int.class));
        TypeMeta<?> expectedTypeMeta1 = new TypeMeta<>(Character.class);
        TypeMeta<?> expectedTypeMeta2 = new TypeMeta<>(int.class);
        Assertions.assertEquals(Arrays.asList(expectedTypeMeta1, expectedTypeMeta2), result);
    }

    @Test
    public void getSeveralSimpleParameterTypesTypeMetaTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getSeveralSimpleParameterTypes", Character.class, int.class));
        TypeMeta<?> expectedTypeMeta1 = new TypeMeta<>(Character.class);
        TypeMeta<?> expectedTypeMeta2 = new TypeMeta<>(int.class);
        Assertions.assertEquals(Arrays.asList(expectedTypeMeta1, expectedTypeMeta2), result);
    }

    @Test
    public void getSeveralSimpleParameterTypesTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<?> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSeveralSimpleParameterTypes", Character.class, int.class));
        TypeMeta<?> expectedTypeMeta1 = new TypeMeta<>(Character.class);
        TypeMeta<?> expectedTypeMeta2 = new TypeMeta<>(int.class);
        Assertions.assertEquals(Arrays.asList(expectedTypeMeta1, expectedTypeMeta2), result);
    }

    @Test
    public void getSimpleReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getSimpleReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(String.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getSimpleReturnTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(new TypeMeta<>(TestModel.class), TestModel.class.getMethod("getSimpleReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(String.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getSimpleReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getSimpleReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(String.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getSingleGenericParameterTypeClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleGenericParameterType", Object.class));
        Assertions.assertEquals(Collections.singletonList(TypeMeta.OBJECT_META), result);
    }

    @Test
    public void getSingleGenericParameterTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Character.class);
        TypeMeta<?> secondClassParameterTypeMeta = new TypeMeta<>(String.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ expectedTypeMeta, secondClassParameterTypeMeta });
        List<TypeMeta<?>> result = executableTypeResolver
                .getParameterTypes(typeMeta, TestModel.class.getMethod("getSingleGenericParameterType", Object.class));
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleGenericParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Character, String>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleGenericParameterType", Object.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Character.class);
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedArrayParameterTypeClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleParameterizedArrayParameterType", Object[].class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Object[].class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedArrayParameterTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> firstClassParameterTypeMeta = new TypeMeta<>(Boolean.class);
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(String.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ firstClassParameterTypeMeta, innerTypeMeta });
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeMeta,
                TestModel.class.getMethod("getSingleParameterizedArrayParameterType", Object[].class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(String[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedArrayParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Boolean, String>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleParameterizedArrayParameterType", Object[].class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(String.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(String[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeInnerArrayTypeClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeInnerArrayType", List.class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeInnerArrayTypeTypeMetaTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getSingleParameterizedParameterTypeInnerArrayType", List.class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeInnerArrayTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeInnerArrayType", List.class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeInnerParameterTypeClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeInnerParameterType", Set.class));
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ listTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeInnerParameterTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Float.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Character.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeMeta,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeInnerParameterType", Set.class));
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ innerTypeMeta1 });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ listTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeInnerParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Float, Character>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeInnerParameterType", Set.class));
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Float.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Character.class);
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ innerTypeMeta1 });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ listTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeInnerParameterizedArrayTypeClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class, TestModel.class
                .getMethod("getSingleParameterizedParameterTypeInnerParameterizedArrayType", List.class));
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Object[].class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeInnerParameterizedArrayTypeTypeMetaTest()
            throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Double.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Boolean.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeMeta, TestModel.class
                .getMethod("getSingleParameterizedParameterTypeInnerParameterizedArrayType", List.class));
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Double[].class, new TypeMeta<?>[]{ innerTypeMeta1 });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeInnerParameterizedArrayTypeTypeProviderTest()
            throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Double, Boolean>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider, TestModel.class
                .getMethod("getSingleParameterizedParameterTypeInnerParameterizedArrayType", List.class));
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Double.class);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Double[].class, new TypeMeta<?>[]{ innerTypeMeta1 });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeSeveralInnerParameterTypesClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeSeveralInnerParameterTypes", List.class));
        TypeMeta<?> mapTypeMeta = new TypeMeta<>(Map.class,
                new TypeMeta<?>[]{ TypeMeta.OBJECT_META, TypeMeta.OBJECT_META });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ mapTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeSeveralInnerParameterTypesTypeMetaTest()
            throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Short.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Long.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeMeta,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeSeveralInnerParameterTypes", List.class));
        TypeMeta<?> mapTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ mapTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeSeveralInnerParameterTypesTypeProviderTest()
            throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Short, Long>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeSeveralInnerParameterTypes", List.class));
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Short.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Long.class);
        TypeMeta<?> mapTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ mapTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeSeveralParameterTypesClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeSeveralParameterTypes", Map.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class,
                new TypeMeta<?>[]{ TypeMeta.OBJECT_META, TypeMeta.OBJECT_META });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeSeveralParameterTypesTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(String.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Boolean.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeMeta,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeSeveralParameterTypes", Map.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeSeveralParameterTypesTypeProviderTest()
            throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<String, Boolean>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeSeveralParameterTypes", Map.class));
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(String.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Boolean.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeSingleParameterTypesClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeSingleParameterTypes", Collection.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Collection.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeSingleParameterTypesTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Byte.class);
        TypeMeta<?> secondClassParameterTypeMeta = new TypeMeta<>(Boolean.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ innerTypeMeta, secondClassParameterTypeMeta });
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeMeta,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeSingleParameterTypes", Collection.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Collection.class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleParameterizedParameterTypeSingleParameterTypesTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Byte, Boolean>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeSingleParameterTypes", Collection.class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Byte.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Collection.class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSinglePrimitiveParameterTypeClassTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSinglePrimitiveParameterType", int.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int.class);
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSinglePrimitiveParameterTypeTypeMetaTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getSinglePrimitiveParameterType", int.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int.class);
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSinglePrimitiveParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<?> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSinglePrimitiveParameterType", int.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int.class);
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleSimpleParameterTypeClassTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleSimpleParameterType", Object.class));
        Assertions.assertEquals(Collections.singletonList(TypeMeta.OBJECT_META), result);
    }

    @Test
    public void getSingleSimpleParameterTypeTypeMetaTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getSingleSimpleParameterType", Object.class));
        Assertions.assertEquals(Collections.singletonList(TypeMeta.OBJECT_META), result);
    }

    @Test
    public void getSingleSimpleParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<?> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleSimpleParameterType", Object.class));
        Assertions.assertEquals(Collections.singletonList(TypeMeta.OBJECT_META), result);
    }

    @Test
    public void getSingleWildcardArrayParameterTypeClassTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleWildcardArrayParameterType", List[].class));
        TypeMeta<?> numberTypeMeta = new TypeMeta<>(Number.class, true);
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ numberTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List[].class, new TypeMeta<?>[]{ listTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleWildcardArrayParameterTypeTypeMetaTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getSingleWildcardArrayParameterType", List[].class));
        TypeMeta<?> numberTypeMeta = new TypeMeta<>(Number.class, true);
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ numberTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List[].class, new TypeMeta<?>[]{ listTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleWildcardArrayParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<?> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleWildcardArrayParameterType", List[].class));
        TypeMeta<?> numberTypeMeta = new TypeMeta<>(Number.class, true);
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ numberTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List[].class, new TypeMeta<?>[]{ listTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleWrapperParameterTypeClassTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleWrapperParameterType", Long.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Long.class);
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleWrapperParameterTypeTypeMetaTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getSingleWrapperParameterType", Long.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Long.class);
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getSingleWrapperParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<?> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleWrapperParameterType", Long.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Long.class);
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getVoidReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getVoidReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(void.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getVoidReturnTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(new TypeMeta<>(TestModel.class), TestModel.class.getMethod("getVoidReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(void.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getVoidReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getVoidReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(void.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getWrapperArrayParameterTypeClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getWrapperArrayParameterType", Short[].class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Short.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getWrapperArrayParameterTypeTypeMetaTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getWrapperArrayParameterType", Short[].class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Short.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getWrapperArrayParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getWrapperArrayParameterType", Short[].class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Short.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    public void getWrapperArrayReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getWrapperArrayReturnType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getWrapperArrayReturnTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(new TypeMeta<>(TestModel.class), TestModel.class.getMethod("getWrapperArrayReturnType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getWrapperArrayReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getWrapperArrayReturnType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getWrapperReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getWrapperReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Double.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getWrapperReturnTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(new TypeMeta<>(TestModel.class), TestModel.class.getMethod("getWrapperReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Double.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    public void getWrapperReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getWrapperReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Double.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }
}
