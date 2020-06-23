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

import com.github.vladislavsevruk.resolver.test.data.TestModel;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

class FieldTypeResolverImplTest {

    private FieldTypeResolverImpl fieldTypeResolver = new FieldTypeResolverImpl();

    @Test
    void getGenericFieldTypeClassTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("genericField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(TestModel.class, field);
        Assertions.assertEquals(TypeMeta.OBJECT_META, result);
    }

    @Test
    void getGenericFieldTypeTypeMetaTest() throws NoSuchFieldException {
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Double.class);
        TypeMeta<?> secondClassParameterTypeMeta = new TypeMeta<>(Short.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ expectedTypeMeta, secondClassParameterTypeMeta });
        Field field = TestModel.class.getDeclaredField("genericField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeMeta, field);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getGenericFieldTypeTypeProviderTest() throws NoSuchFieldException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Double, Short>>() {};
        Field field = TestModel.class.getDeclaredField("genericField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeProvider, field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Double.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedArrayFieldTypeClassTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("parameterizedArrayField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(TestModel.class, field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Object[].class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedArrayFieldTypeTypeMetaTest() throws NoSuchFieldException {
        TypeMeta<?> firstClassParameterTypeMeta = new TypeMeta<>(Float.class);
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Long.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ firstClassParameterTypeMeta, innerTypeMeta });
        Field field = TestModel.class.getDeclaredField("parameterizedArrayField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeMeta, field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Long[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedArrayFieldTypeTypeProviderTest() throws NoSuchFieldException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Float, Long>>() {};
        Field field = TestModel.class.getDeclaredField("parameterizedArrayField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeProvider, field);
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Long.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Long[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeInnerArrayTypeClassTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("parameterizedFieldInnerArray");
        TypeMeta<?> result = fieldTypeResolver.resolveField(TestModel.class, field);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Long[].class, new TypeMeta<?>[]{ new TypeMeta<>(Long.class) });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeInnerArrayTypeTypeMetaTest() throws NoSuchFieldException {
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Character.class);
        TypeMeta<?> secondClassParameterTypeMeta = new TypeMeta<>(String.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ innerTypeMeta, secondClassParameterTypeMeta });
        Field field = TestModel.class.getDeclaredField("parameterizedFieldInnerArray");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeMeta, field);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Long[].class, new TypeMeta<?>[]{ new TypeMeta<>(Long.class) });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeInnerArrayTypeTypeProviderTest() throws NoSuchFieldException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Character, String>>() {};
        Field field = TestModel.class.getDeclaredField("parameterizedFieldInnerArray");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeProvider, field);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Long[].class, new TypeMeta<?>[]{ new TypeMeta<>(Long.class) });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeInnerParameterTypeClassTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("parameterizedFieldInnerParameter");
        TypeMeta<?> result = fieldTypeResolver.resolveField(TestModel.class, field);
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ listTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeInnerParameterTypeTypeMetaTest() throws NoSuchFieldException {
        TypeMeta<?> firstClassParameterTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Boolean.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ firstClassParameterTypeMeta, innerTypeMeta });
        Field field = TestModel.class.getDeclaredField("parameterizedFieldInnerParameter");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeMeta, field);
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ listTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeInnerParameterTypeTypeProviderTest() throws NoSuchFieldException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Integer, Boolean>>() {};
        Field field = TestModel.class.getDeclaredField("parameterizedFieldInnerParameter");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeProvider, field);
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Boolean.class);
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ listTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeInnerParameterizedArrayTypeClassTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("parameterizedFieldInnerParameterizedArray");
        TypeMeta<?> result = fieldTypeResolver.resolveField(TestModel.class, field);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Object[].class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeInnerParameterizedArrayTypeTypeMetaTest() throws NoSuchFieldException {
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Long.class);
        TypeMeta<?> secondClassParameterTypeMeta = new TypeMeta<>(Boolean.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ innerTypeMeta, secondClassParameterTypeMeta });
        Field field = TestModel.class.getDeclaredField("parameterizedFieldInnerParameterizedArray");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeMeta, field);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Long[].class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeInnerParameterizedArrayTypeTypeProviderTest() throws NoSuchFieldException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Long, Boolean>>() {};
        Field field = TestModel.class.getDeclaredField("parameterizedFieldInnerParameterizedArray");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeProvider, field);
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Long.class);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Long[].class, new TypeMeta<?>[]{ innerTypeMeta });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeSeveralInnerParameterTypesClassTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("parameterizedFieldSeveralInnerParameters");
        TypeMeta<?> result = fieldTypeResolver.resolveField(TestModel.class, field);
        TypeMeta<?> setTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ setTypeMeta, listTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeSeveralInnerParameterTypesTypeMetaTest() throws NoSuchFieldException {
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Short.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Byte.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        Field field = TestModel.class.getDeclaredField("parameterizedFieldSeveralInnerParameters");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeMeta, field);
        TypeMeta<?> setTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ innerTypeMeta2 });
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ innerTypeMeta1 });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ setTypeMeta, listTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeSeveralInnerParameterTypesTypeProviderTest() throws NoSuchFieldException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Short, Byte>>() {};
        Field field = TestModel.class.getDeclaredField("parameterizedFieldSeveralInnerParameters");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeProvider, field);
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Short.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Byte.class);
        TypeMeta<?> setTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ innerTypeMeta2 });
        TypeMeta<?> listTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ innerTypeMeta1 });
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ setTypeMeta, listTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeSeveralParameterTypesClassTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("parameterizedFieldSeveralParameters");
        TypeMeta<?> result = fieldTypeResolver.resolveField(TestModel.class, field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class,
                new TypeMeta<?>[]{ TypeMeta.OBJECT_META, TypeMeta.OBJECT_META });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeSeveralParameterTypesTypeMetaTest() throws NoSuchFieldException {
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Byte.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(String.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        Field field = TestModel.class.getDeclaredField("parameterizedFieldSeveralParameters");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeMeta, field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta2, innerTypeMeta1 });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeSeveralParameterTypesTypeProviderTest() throws NoSuchFieldException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Byte, String>>() {};
        Field field = TestModel.class.getDeclaredField("parameterizedFieldSeveralParameters");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeProvider, field);
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Byte.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(String.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Map.class, new TypeMeta<?>[]{ innerTypeMeta2, innerTypeMeta1 });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeSingleParameterTypeClassTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("parameterizedFieldSingleParameter");
        TypeMeta<?> result = fieldTypeResolver.resolveField(TestModel.class, field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeSingleParameterTypeTypeMetaTest() throws NoSuchFieldException {
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Integer.class);
        TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(Float.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class, new TypeMeta<?>[]{ innerTypeMeta1, innerTypeMeta2 });
        Field field = TestModel.class.getDeclaredField("parameterizedFieldSingleParameter");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeMeta, field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ innerTypeMeta1 });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getParameterizedFieldTypeSingleParameterTypeTypeProviderTest() throws NoSuchFieldException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Integer, Float>>() {};
        Field field = TestModel.class.getDeclaredField("parameterizedFieldSingleParameter");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeProvider, field);
        TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(Integer.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ innerTypeMeta1 });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getPrimitiveArrayFieldTypeClassTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("primitiveArrayField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(TestModel.class, field);
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(int.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getPrimitiveArrayFieldTypeTypeMetaTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("primitiveArrayField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(new TypeMeta<>(TestModel.class), field);
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(int.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getPrimitiveArrayFieldTypeTypeProviderTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("primitiveArrayField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(new TypeProvider<TestModel>() {}, field);
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(int.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(int[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getPrimitiveFieldTypeClassTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("primitiveField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(TestModel.class, field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(long.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getPrimitiveFieldTypeTypeMetaTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("primitiveField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(new TypeMeta<>(TestModel.class), field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(long.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getPrimitiveFieldTypeTypeProviderTest() throws NoSuchFieldException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        Field field = TestModel.class.getDeclaredField("primitiveField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeProvider, field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(long.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getSimpleFieldTypeClassTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("simpleField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(TestModel.class, field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(String.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getSimpleFieldTypeTypeMetaTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("simpleField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(new TypeMeta<>(TestModel.class), field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(String.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getSimpleFieldTypeTypeProviderTest() throws NoSuchFieldException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        Field field = TestModel.class.getDeclaredField("simpleField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeProvider, field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(String.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getWildcardFieldTypeClassTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("wildcardField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(TestModel.class, field);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Object[].class, new TypeMeta<?>[]{ TypeMeta.OBJECT_META }, true);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getWildcardFieldTypeTypeMetaTest() throws NoSuchFieldException {
        TypeMeta<?> firstClassParameterTypeMeta = new TypeMeta<>(Character.class);
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Double.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ firstClassParameterTypeMeta, innerTypeMeta });
        Field field = TestModel.class.getDeclaredField("wildcardField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeMeta, field);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Double[].class, new TypeMeta<?>[]{ innerTypeMeta }, true);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getWildcardFieldTypeTypeProviderTest() throws NoSuchFieldException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel<Character, Double>>() {};
        Field field = TestModel.class.getDeclaredField("wildcardField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeProvider, field);
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Double.class);
        TypeMeta<?> arrayTypeMeta = new TypeMeta<>(Double[].class, new TypeMeta<?>[]{ innerTypeMeta }, true);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ arrayTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getWrapperArrayFieldTypeClassTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("wrapperArrayField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(TestModel.class, field);
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getWrapperArrayFieldTypeTypeMetaTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("wrapperArrayField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(new TypeMeta<>(TestModel.class), field);
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getWrapperArrayFieldTypeTypeProviderTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("wrapperArrayField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(new TypeProvider<TestModel>() {}, field);
        TypeMeta<?> innerTypeMeta = new TypeMeta<>(Integer.class);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Integer[].class, new TypeMeta<?>[]{ innerTypeMeta });
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getWrapperFieldTypeClassTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("wrapperField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(TestModel.class, field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Short.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getWrapperFieldTypeTypeMetaTest() throws NoSuchFieldException {
        Field field = TestModel.class.getDeclaredField("wrapperField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(new TypeMeta<>(TestModel.class), field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Short.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }

    @Test
    void getWrapperFieldTypeTypeProviderTest() throws NoSuchFieldException {
        TypeProvider<?> typeProvider = new TypeProvider<TestModel>() {};
        Field field = TestModel.class.getDeclaredField("wrapperField");
        TypeMeta<?> result = fieldTypeResolver.resolveField(typeProvider, field);
        TypeMeta<?> expectedTypeMeta = new TypeMeta<>(Short.class);
        Assertions.assertEquals(expectedTypeMeta, result);
    }
}
