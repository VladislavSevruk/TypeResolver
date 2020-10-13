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
package com.github.vladislavsevruk.resolver.resolver.executable;

import com.github.vladislavsevruk.resolver.exception.TypeResolvingException;
import com.github.vladislavsevruk.resolver.test.data.TestModel;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ExecutableTypeMetaResolverTest {

    private ExecutableTypeMetaResolver executableTypeResolver = new ExecutableTypeMetaResolver();

    @Test
    void getExceptionTypeClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver
                .getExceptionTypes(TestModel.class, TestModel.class.getMethod("getExceptionType"));
        List<TypeMeta<?>> expectedTypeMetas = Collections.singletonList(new TypeMeta<>(ParseException.class));
        Assertions.assertEquals(expectedTypeMetas, result);
    }

    @Test
    void getExceptionTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> firstClassParameterTypeMeta = new TypeMeta<>(Float.class);
        TypeMeta<?> secondClassParameterTypeMeta = new TypeMeta<>(Long.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ firstClassParameterTypeMeta, secondClassParameterTypeMeta });
        List<TypeMeta<?>> result = executableTypeResolver
                .getExceptionTypes(typeMeta, TestModel.class.getMethod("getExceptionType"));
        List<TypeMeta<?>> expectedTypeMetas = Collections.singletonList(new TypeMeta<>(ParseException.class));
        Assertions.assertEquals(expectedTypeMetas, result);
    }

    @Test
    void getExceptionTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Float, Long>>() {};
        List<TypeMeta<?>> result = executableTypeResolver
                .getExceptionTypes(typeProvider, TestModel.class.getMethod("getExceptionType"));
        List<TypeMeta<?>> expectedTypeMetas = Collections.singletonList(new TypeMeta<>(ParseException.class));
        Assertions.assertEquals(expectedTypeMetas, result);
    }

    @Test
    void getGenericReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getGenericReturnType"));
        Assertions.assertEquals(TypeMeta.OBJECT_META, result);
    }

    @Test
    void getGenericReturnTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Float.class);
        TypeMeta<?> secondClassParameterTypeMeta = new TypeMeta<>(Long.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ expectedTypeMeta, secondClassParameterTypeMeta });
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeMeta, TestModel.class.getMethod("getGenericReturnType"));
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getGenericReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Float, Long>>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getGenericReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Float.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getNoParameterTypeClassTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver
                .getParameterTypes(TestModel.class, TestModel.class.getMethod("getNoParameterType"));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getNoParameterTypeTypeMetaTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver
                .getParameterTypes(new TypeMeta<>(TestModel.class), TestModel.class.getMethod("getNoParameterType"));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getNoParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<?> result = executableTypeResolver
                .getParameterTypes(typeProvider, TestModel.class.getMethod("getNoParameterType"));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getParameterTypeNotOverridenObjectMethodThrowsExceptionClassTest()
            throws NoSuchMethodException, SecurityException {
        Method method = TestModel.class.getMethod("wait");
        Assertions.assertThrows(TypeResolvingException.class,
                () -> executableTypeResolver.getParameterTypes(TestModel.class, method));
    }

    @Test
    void getParameterTypeNotOverridenObjectMethodThrowsExceptionTypeMetaTest()
            throws NoSuchMethodException, SecurityException {
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class);
        Method method = TestModel.class.getMethod("wait");
        Assertions.assertThrows(TypeResolvingException.class,
                () -> executableTypeResolver.getParameterTypes(typeMeta, method));
    }

    @Test
    void getParameterTypeNotOverridenObjectMethodThrowsExceptionTypeProviderTest()
            throws NoSuchMethodException, SecurityException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        Method method = TestModel.class.getMethod("wait");
        Assertions.assertThrows(TypeResolvingException.class,
                () -> executableTypeResolver.getParameterTypes(typeProvider, method));
    }

    @Test
    void getParameterizedArrayReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getParameterizedArrayReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Object[].class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedArrayReturnTypeTypeMetaTest() throws NoSuchMethodException {
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
    void getParameterizedArrayReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Character, Integer>>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getParameterizedArrayReturnType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Character.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Character[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedReturnTypeInnerArrayTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getParameterizedReturnTypeInnerArrayType"));
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedReturnTypeInnerArrayTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver.getReturnType(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getParameterizedReturnTypeInnerArrayType"));
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedReturnTypeInnerArrayTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getParameterizedReturnTypeInnerArrayType"));
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedReturnTypeInnerParameterTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver.getReturnType(TestModel.class,
                TestModel.class.getMethod("getParameterizedReturnTypeInnerParameterType"));
        TypeMeta<?> setTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ setTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedReturnTypeInnerParameterTypeTypeMetaTest() throws NoSuchMethodException {
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
    void getParameterizedReturnTypeInnerParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Short, Long>>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getParameterizedReturnTypeInnerParameterType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Short.class);
        TypeMeta<?> setTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ setTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedReturnTypeInnerParameterizedArrayTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver.getReturnType(TestModel.class,
                TestModel.class.getMethod("getParameterizedReturnTypeInnerParameterizedArrayType"));
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Object[].class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedReturnTypeInnerParameterizedArrayTypeTypeMetaTest() throws NoSuchMethodException {
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
    void getParameterizedReturnTypeInnerParameterizedArrayTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Short, String>>() {};
        TypeMeta<?> result = executableTypeResolver.getReturnType(typeProvider,
                TestModel.class.getMethod("getParameterizedReturnTypeInnerParameterizedArrayType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Short.class);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedReturnTypeSeveralInnerParameterTypesClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver.getReturnType(TestModel.class,
                TestModel.class.getMethod("getParameterizedReturnTypeSeveralInnerParameterTypes"));
        TypeMeta<?> mapTypeMeta = new TypeMeta<>(Map.class,
                new TypeMeta<?>[]{ TypeMeta.OBJECT_META, TypeMeta.OBJECT_META });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ mapTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedReturnTypeSeveralInnerParameterTypesTypeMetaTest() throws NoSuchMethodException {
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
    void getParameterizedReturnTypeSeveralInnerParameterTypesTypeProviderTest() throws NoSuchMethodException {
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
    void getParameterizedReturnTypeSeveralParameterTypesClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver.getReturnType(TestModel.class,
                TestModel.class.getMethod("getParameterizedReturnTypeSeveralParameterTypes"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class,
                new TypeMeta<?>[]{ TypeMeta.OBJECT_META, TypeMeta.OBJECT_META });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedReturnTypeSeveralParameterTypesTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(String.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Boolean.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeMeta, TestModel.class.getMethod("getParameterizedReturnTypeSeveralParameterTypes"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedReturnTypeSeveralParameterTypesTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<String, Boolean>>() {};
        TypeMeta<?> result = executableTypeResolver.getReturnType(typeProvider,
                TestModel.class.getMethod("getParameterizedReturnTypeSeveralParameterTypes"));
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(String.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Boolean.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedReturnTypeSingleParameterTypesClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver.getReturnType(TestModel.class,
                TestModel.class.getMethod("getParameterizedReturnTypeSingleParameterTypes"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedReturnTypeSingleParameterTypesTypeMetaTest() throws NoSuchMethodException {
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
    void getParameterizedReturnTypeSingleParameterTypesTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Character, Byte>>() {};
        TypeMeta<?> result = executableTypeResolver.getReturnType(typeProvider,
                TestModel.class.getMethod("getParameterizedReturnTypeSingleParameterTypes"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Character.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getPrimitiveArrayParameterTypeClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getPrimitiveArrayParameterType", short[].class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(short.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getPrimitiveArrayParameterTypeTypeMetaTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getPrimitiveArrayParameterType", short[].class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(short.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getPrimitiveArrayParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getPrimitiveArrayParameterType", short[].class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(short.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getPrimitiveArrayReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getPrimitiveArrayReturnType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(int.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getPrimitiveArrayReturnTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver.getReturnType(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getPrimitiveArrayReturnType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(int.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getPrimitiveArrayReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getPrimitiveArrayReturnType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(int.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getPrimitiveReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getPrimitiveReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getPrimitiveReturnTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(new TypeMeta<>(TestModel.class), TestModel.class.getMethod("getPrimitiveReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getPrimitiveReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getPrimitiveReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getReturnTypeNotOverridenObjectMethodThrowsExceptionClassTest()
            throws NoSuchMethodException, SecurityException {
        Method method = TestModel.class.getMethod("wait");
        Assertions.assertThrows(TypeResolvingException.class,
                () -> executableTypeResolver.getReturnType(TestModel.class, method));
    }

    @Test
    void getReturnTypeNotOverridenObjectMethodThrowsExceptionTypeMetaTest()
            throws NoSuchMethodException, SecurityException {
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class);
        Method method = TestModel.class.getMethod("wait");
        Assertions.assertThrows(TypeResolvingException.class,
                () -> executableTypeResolver.getReturnType(typeMeta, method));
    }

    @Test
    void getReturnTypeNotOverridenObjectMethodThrowsExceptionTypeProviderTest()
            throws NoSuchMethodException, SecurityException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        Method method = TestModel.class.getMethod("wait");
        Assertions.assertThrows(TypeResolvingException.class,
                () -> executableTypeResolver.getReturnType(typeProvider, method));
    }

    @Test
    void getSeveralGenericParameterTypesClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSeveralGenericParameterTypes", Object.class, Object.class));
        Assertions.assertEquals(Arrays.asList(TypeMeta.OBJECT_META, TypeMeta.OBJECT_META), result);
    }

    @Test
    void getSeveralGenericParameterTypesTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Long.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Integer.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeMeta,
                TestModel.class.getMethod("getSeveralGenericParameterTypes", Object.class, Object.class));
        Assertions.assertEquals(Arrays.asList(innerTypeMeta1, innerTypeMeta2), result);
    }

    @Test
    void getSeveralGenericParameterTypesTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Long, Integer>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSeveralGenericParameterTypes", Object.class, Object.class));
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Long.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Integer.class);
        Assertions.assertEquals(Arrays.asList(innerTypeMeta1, innerTypeMeta2), result);
    }

    @Test
    void getSeveralParameterizedParameterTypeSeveralInnerParameterTypesClassTest() throws NoSuchMethodException {
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
    void getSeveralParameterizedParameterTypeSeveralInnerParameterTypesTypeMetaTest() throws NoSuchMethodException {
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
    void getSeveralParameterizedParameterTypeSeveralInnerParameterTypesTypeProviderTest() throws NoSuchMethodException {
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
    void getSeveralParameterizedParameterTypesClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSeveralParameterizedParameterTypes", List.class, Set.class));
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        TypeMeta<?> setTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        Assertions.assertEquals(Arrays.asList(listTypeMeta, setTypeMeta), result);
    }

    @Test
    void getSeveralParameterizedParameterTypesTypeMetaTest() throws NoSuchMethodException {
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
    void getSeveralParameterizedParameterTypesTypeProviderTest() throws NoSuchMethodException {
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
    void getSeveralSimpleParameterTypesClassTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSeveralSimpleParameterTypes", Character.class, int.class));
        TypeMeta<?> expectedTypeMeta1 = new TypeMeta<>(Character.class);
        TypeMeta<?> expectedTypeMeta2 = new TypeMeta<>(int.class);
        Assertions.assertEquals(Arrays.asList(expectedTypeMeta1, expectedTypeMeta2), result);
    }

    @Test
    void getSeveralSimpleParameterTypesTypeMetaTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getSeveralSimpleParameterTypes", Character.class, int.class));
        TypeMeta<?> expectedTypeMeta1 = new TypeMeta<>(Character.class);
        TypeMeta<?> expectedTypeMeta2 = new TypeMeta<>(int.class);
        Assertions.assertEquals(Arrays.asList(expectedTypeMeta1, expectedTypeMeta2), result);
    }

    @Test
    void getSeveralSimpleParameterTypesTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<?> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSeveralSimpleParameterTypes", Character.class, int.class));
        TypeMeta<?> expectedTypeMeta1 = new TypeMeta<>(Character.class);
        TypeMeta<?> expectedTypeMeta2 = new TypeMeta<>(int.class);
        Assertions.assertEquals(Arrays.asList(expectedTypeMeta1, expectedTypeMeta2), result);
    }

    @Test
    void getSimpleReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getSimpleReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(String.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getSimpleReturnTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(new TypeMeta<>(TestModel.class), TestModel.class.getMethod("getSimpleReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(String.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getSimpleReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getSimpleReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(String.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getSingleGenericParameterTypeClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleGenericParameterType", Object.class));
        Assertions.assertEquals(Collections.singletonList(TypeMeta.OBJECT_META), result);
    }

    @Test
    void getSingleGenericParameterTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Character.class);
        TypeMeta<?> secondClassParameterTypeMeta = new TypeMeta<>(String.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ expectedTypeMeta, secondClassParameterTypeMeta });
        List<TypeMeta<?>> result = executableTypeResolver
                .getParameterTypes(typeMeta, TestModel.class.getMethod("getSingleGenericParameterType", Object.class));
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleGenericParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Character, String>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleGenericParameterType", Object.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Character.class);
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleParameterizedArrayParameterTypeClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleParameterizedArrayParameterType", Object[].class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Object[].class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleParameterizedArrayParameterTypeTypeMetaTest() throws NoSuchMethodException {
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
    void getSingleParameterizedArrayParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Boolean, String>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleParameterizedArrayParameterType", Object[].class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(String.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(String[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleParameterizedParameterTypeInnerArrayTypeClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeInnerArrayType", List.class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleParameterizedParameterTypeInnerArrayTypeTypeMetaTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getSingleParameterizedParameterTypeInnerArrayType", List.class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleParameterizedParameterTypeInnerArrayTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeInnerArrayType", List.class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleParameterizedParameterTypeInnerParameterTypeClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeInnerParameterType", Set.class));
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ listTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleParameterizedParameterTypeInnerParameterTypeTypeMetaTest() throws NoSuchMethodException {
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
    void getSingleParameterizedParameterTypeInnerParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Float, Character>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeInnerParameterType", Set.class));
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Float.class);
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ innerTypeMeta1 });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ listTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleParameterizedParameterTypeInnerParameterizedArrayTypeClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class, TestModel.class
                .getMethod("getSingleParameterizedParameterTypeInnerParameterizedArrayType", List.class));
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Object[].class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleParameterizedParameterTypeInnerParameterizedArrayTypeTypeMetaTest() throws NoSuchMethodException {
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
    void getSingleParameterizedParameterTypeInnerParameterizedArrayTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Double, Boolean>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider, TestModel.class
                .getMethod("getSingleParameterizedParameterTypeInnerParameterizedArrayType", List.class));
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Double.class);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Double[].class, new TypeMeta<?>[]{ innerTypeMeta1 });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleParameterizedParameterTypeSeveralInnerParameterTypesClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeSeveralInnerParameterTypes", List.class));
        TypeMeta<?> mapTypeMeta = new TypeMeta<>(Map.class,
                new TypeMeta<?>[]{ TypeMeta.OBJECT_META, TypeMeta.OBJECT_META });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ mapTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleParameterizedParameterTypeSeveralInnerParameterTypesTypeMetaTest() throws NoSuchMethodException {
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
    void getSingleParameterizedParameterTypeSeveralInnerParameterTypesTypeProviderTest() throws NoSuchMethodException {
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
    void getSingleParameterizedParameterTypeSeveralParameterTypesClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeSeveralParameterTypes", Map.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class,
                new TypeMeta<?>[]{ TypeMeta.OBJECT_META, TypeMeta.OBJECT_META });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleParameterizedParameterTypeSeveralParameterTypesTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(String.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Boolean.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeMeta,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeSeveralParameterTypes", Map.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleParameterizedParameterTypeSeveralParameterTypesTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<String, Boolean>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeSeveralParameterTypes", Map.class));
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(String.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Boolean.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleParameterizedParameterTypeSingleParameterTypesClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeSingleParameterTypes", Collection.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Collection.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleParameterizedParameterTypeSingleParameterTypesTypeMetaTest() throws NoSuchMethodException {
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
    void getSingleParameterizedParameterTypeSingleParameterTypesTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Byte, Boolean>>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleParameterizedParameterTypeSingleParameterTypes", Collection.class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Byte.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Collection.class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSinglePrimitiveParameterTypeClassTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSinglePrimitiveParameterType", int.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int.class);
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSinglePrimitiveParameterTypeTypeMetaTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getSinglePrimitiveParameterType", int.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int.class);
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSinglePrimitiveParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<?> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSinglePrimitiveParameterType", int.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int.class);
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleSimpleParameterTypeClassTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleSimpleParameterType", Object.class));
        Assertions.assertEquals(Collections.singletonList(TypeMeta.OBJECT_META), result);
    }

    @Test
    void getSingleSimpleParameterTypeTypeMetaTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getSingleSimpleParameterType", Object.class));
        Assertions.assertEquals(Collections.singletonList(TypeMeta.OBJECT_META), result);
    }

    @Test
    void getSingleSimpleParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<?> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleSimpleParameterType", Object.class));
        Assertions.assertEquals(Collections.singletonList(TypeMeta.OBJECT_META), result);
    }

    @Test
    void getSingleWildcardArrayParameterTypeClassTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleWildcardArrayParameterType", List[].class));
        TypeMeta<?> numberTypeMeta = new TypeMeta<>(Number.class, true);
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ numberTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List[].class, new TypeMeta<?>[]{ listTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleWildcardArrayParameterTypeTypeMetaTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getSingleWildcardArrayParameterType", List[].class));
        TypeMeta<?> numberTypeMeta = new TypeMeta<>(Number.class, true);
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ numberTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List[].class, new TypeMeta<?>[]{ listTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleWildcardArrayParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<?> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleWildcardArrayParameterType", List[].class));
        TypeMeta<?> numberTypeMeta = new TypeMeta<>(Number.class, true);
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ numberTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List[].class, new TypeMeta<?>[]{ listTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleWrapperParameterTypeClassTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getSingleWrapperParameterType", Long.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Long.class);
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleWrapperParameterTypeTypeMetaTest() throws NoSuchMethodException {
        List<?> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getSingleWrapperParameterType", Long.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Long.class);
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getSingleWrapperParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<?> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getSingleWrapperParameterType", Long.class));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Long.class);
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getVoidReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getVoidReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(void.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getVoidReturnTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(new TypeMeta<>(TestModel.class), TestModel.class.getMethod("getVoidReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(void.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getVoidReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getVoidReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(void.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getWrapperArrayParameterTypeClassTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(TestModel.class,
                TestModel.class.getMethod("getWrapperArrayParameterType", Short[].class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Short.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getWrapperArrayParameterTypeTypeMetaTest() throws NoSuchMethodException {
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(new TypeMeta<>(TestModel.class),
                TestModel.class.getMethod("getWrapperArrayParameterType", Short[].class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Short.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getWrapperArrayParameterTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        List<TypeMeta<?>> result = executableTypeResolver.getParameterTypes(typeProvider,
                TestModel.class.getMethod("getWrapperArrayParameterType", Short[].class));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Short.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Short[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(Collections.singletonList(expectedTypeMeta), result);
    }

    @Test
    void getWrapperArrayReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getWrapperArrayReturnType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getWrapperArrayReturnTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(new TypeMeta<>(TestModel.class), TestModel.class.getMethod("getWrapperArrayReturnType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getWrapperArrayReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getWrapperArrayReturnType"));
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getWrapperReturnTypeClassTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(TestModel.class, TestModel.class.getMethod("getWrapperReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Double.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getWrapperReturnTypeTypeMetaTest() throws NoSuchMethodException {
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(new TypeMeta<>(TestModel.class), TestModel.class.getMethod("getWrapperReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Double.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getWrapperReturnTypeTypeProviderTest() throws NoSuchMethodException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        TypeMeta<?> result = executableTypeResolver
                .getReturnType(typeProvider, TestModel.class.getMethod("getWrapperReturnType"));
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Double.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }
}
