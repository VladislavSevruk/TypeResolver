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
package com.github.vladislavsevruk.resolver.util;

import com.github.vladislavsevruk.resolver.test.data.TestModel;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class TypeMetaUtilTest {

    @Test
    public void isSameTypesAssignableComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<ArrayList> typeMeta2 = new TypeMeta<>(ArrayList.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesAssignableDeepNestingTypeAtComplexTypeMetaTest() {
        TypeMeta<?>[] deepInnerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Number.class) };
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class, deepInnerTypeMeta1) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] deepInnerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class, deepInnerTypeMeta2) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesAssignableInnerTypeAtComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Number.class) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesAssignableSimpleTypeMetaTest() {
        TypeMeta<Number> typeMeta1 = new TypeMeta<>(Number.class);
        TypeMeta<Long> typeMeta2 = new TypeMeta<>(Long.class);
        Assertions.assertFalse(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesDifferentDeepNestingLevelTest() {
        TypeMeta<?>[] deepInnerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class, deepInnerTypeMeta1) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesDifferentNestingLevelMetaTest() {
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(String.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesEqualComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertTrue(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesEqualDeepNestingTypeAtComplexTypeMetaTest() {
        TypeMeta<?>[] deepInnerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class, deepInnerTypeMeta1) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] deepInnerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class, deepInnerTypeMeta2) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertTrue(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesEqualSimpleTypeMetaTest() {
        TypeMeta<Long> typeMeta1 = new TypeMeta<>(Long.class);
        TypeMeta<Long> typeMeta2 = new TypeMeta<>(Long.class);
        Assertions.assertTrue(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesNotAssignableComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<Set> typeMeta1 = new TypeMeta<>(Set.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesNotAssignableDeepNestingTypeAtComplexTypeMetaTest() {
        TypeMeta<?>[] deepInnerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(String.class) };
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class, deepInnerTypeMeta1) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] deepInnerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class, deepInnerTypeMeta2) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesNotAssignableInnerTypeAtComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(String.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesNotAssignableSimpleTypeMetaTest() {
        TypeMeta<String> typeMeta1 = new TypeMeta<>(String.class);
        TypeMeta<Long> typeMeta2 = new TypeMeta<>(Long.class);
        Assertions.assertFalse(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesPrimitiveAndWrapperTypeMetaTest() {
        TypeMeta<Long> primitiveTypeMeta = new TypeMeta<>(long.class);
        TypeMeta<Long> wrapperTypeMeta = new TypeMeta<>(Long.class);
        Assertions.assertTrue(TypeMetaUtil.isSameTypes(primitiveTypeMeta, wrapperTypeMeta));
    }

    @Test
    public void isSameTypesReverseAssignableComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<ArrayList> typeMeta1 = new TypeMeta<>(ArrayList.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesReverseAssignableInnerTypeAtComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Number.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesReverseAssignableSimpleTypeMetaTest() {
        TypeMeta<Long> typeMeta1 = new TypeMeta<>(Long.class);
        TypeMeta<Number> typeMeta2 = new TypeMeta<>(Number.class);
        Assertions.assertFalse(TypeMetaUtil.isSameTypes(typeMeta1, typeMeta2));
    }

    @Test
    public void isSameTypesSameComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta = new TypeMeta<>(List.class, innerTypeMeta);
        Assertions.assertTrue(TypeMetaUtil.isSameTypes(typeMeta, typeMeta));
    }

    @Test
    public void isSameTypesSameSimpleTypeMetaTest() {
        TypeMeta<Long> typeMeta = new TypeMeta<>(Long.class);
        Assertions.assertTrue(TypeMetaUtil.isSameTypes(typeMeta, typeMeta));
    }

    @Test
    public void isTypesMatchAssignableComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<ArrayList> typeMeta2 = new TypeMeta<>(ArrayList.class, innerTypeMeta2);
        Assertions.assertTrue(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchAssignableDeepNestingTypeAtComplexTypeMetaTest() {
        TypeMeta<?>[] deepInnerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Number.class) };
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class, deepInnerTypeMeta1) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] deepInnerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class, deepInnerTypeMeta2) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchAssignableDeepNestingWildcardTypeAtComplexTypeMetaTest() {
        TypeMeta<?>[] deepInnerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Number.class, true) };
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(List.class, deepInnerTypeMeta1) };
        TypeMeta<List[]> typeMeta1 = new TypeMeta<>(List[].class, innerTypeMeta1);
        TypeMeta<?>[] deepInnerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(List.class, deepInnerTypeMeta2) };
        TypeMeta<List[]> typeMeta2 = new TypeMeta<>(List[].class, innerTypeMeta2);
        Assertions.assertTrue(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchAssignableInnerTypeAtComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Number.class) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchAssignableInnerWildcardTypeAtComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Number.class, true) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertTrue(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchAssignableSimpleTypeMetaTest() {
        TypeMeta<Number> typeMeta1 = new TypeMeta<>(Number.class);
        TypeMeta<Long> typeMeta2 = new TypeMeta<>(Long.class);
        Assertions.assertTrue(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchDescendantHasMoreTypeParametersTypeMetaTest() {
        TypeMeta<LinkedList> typeMeta1 = new TypeMeta<>(LinkedList.class,
                new TypeMeta<?>[]{ new TypeMeta<>(Byte.class) });
        TypeMeta<TestModel> typeMeta2 = new TypeMeta<>(TestModel.class,
                new TypeMeta<?>[]{ new TypeMeta<>(Byte.class), new TypeMeta<>(Character.class) });
        Assertions.assertTrue(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchDifferentDeepNestingLevelTest() {
        TypeMeta<?>[] deepInnerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class, deepInnerTypeMeta1) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchDifferentNestingLevelMetaTest() {
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(String.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertTrue(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchEqualComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertTrue(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchEqualDeepNestingTypeAtComplexTypeMetaTest() {
        TypeMeta<?>[] deepInnerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class, deepInnerTypeMeta1) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] deepInnerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class, deepInnerTypeMeta2) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertTrue(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchEqualSimpleTypeMetaTest() {
        TypeMeta<Long> typeMeta1 = new TypeMeta<>(Long.class);
        TypeMeta<Long> typeMeta2 = new TypeMeta<>(Long.class);
        Assertions.assertTrue(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchNotAssignableComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<Set> typeMeta1 = new TypeMeta<>(Set.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchNotAssignableDeepNestingTypeAtComplexTypeMetaTest() {
        TypeMeta<?>[] deepInnerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(String.class) };
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class, deepInnerTypeMeta1) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] deepInnerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Set.class, deepInnerTypeMeta2) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchNotAssignableInnerTypeAtComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(String.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchNotAssignableSimpleTypeMetaTest() {
        TypeMeta<String> typeMeta1 = new TypeMeta<>(String.class);
        TypeMeta<Long> typeMeta2 = new TypeMeta<>(Long.class);
        Assertions.assertFalse(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchObjectAcceptorTypeMetaTest() {
        TypeMeta<Object> objectTypeMeta = new TypeMeta<>(Object.class);
        TypeMeta<Boolean> modelTypeMeta = new TypeMeta<>(Boolean.class);
        Assertions.assertTrue(TypeMetaUtil.isTypesMatch(objectTypeMeta, modelTypeMeta));
    }

    @Test
    public void isTypesMatchObjectDonorTypeMetaTest() {
        TypeMeta<String> modelTypeMeta = new TypeMeta<>(String.class);
        TypeMeta<Object> objectTypeMeta = new TypeMeta<>(Object.class);
        Assertions.assertFalse(TypeMetaUtil.isTypesMatch(modelTypeMeta, objectTypeMeta));
    }

    @Test
    public void isTypesMatchPrimitiveAndWrapperTypeMetaTest() {
        TypeMeta<Long> primitiveTypeMeta = new TypeMeta<>(long.class);
        TypeMeta<Long> wrapperTypeMeta = new TypeMeta<>(Long.class);
        Assertions.assertTrue(TypeMetaUtil.isTypesMatch(primitiveTypeMeta, wrapperTypeMeta));
    }

    @Test
    public void isTypesMatchReverseAssignableComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<ArrayList> typeMeta1 = new TypeMeta<>(ArrayList.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchReverseAssignableInnerTypeAtComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta1 = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta1 = new TypeMeta<>(List.class, innerTypeMeta1);
        TypeMeta<?>[] innerTypeMeta2 = new TypeMeta<?>[]{ new TypeMeta<>(Number.class) };
        TypeMeta<List> typeMeta2 = new TypeMeta<>(List.class, innerTypeMeta2);
        Assertions.assertFalse(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchReverseAssignableSimpleTypeMetaTest() {
        TypeMeta<Long> typeMeta1 = new TypeMeta<>(Long.class);
        TypeMeta<Number> typeMeta2 = new TypeMeta<>(Number.class);
        Assertions.assertFalse(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }

    @Test
    public void isTypesMatchSameComplexTypeMetaTest() {
        TypeMeta<?>[] innerTypeMeta = new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) };
        TypeMeta<List> typeMeta = new TypeMeta<>(List.class, innerTypeMeta);
        Assertions.assertTrue(TypeMetaUtil.isTypesMatch(typeMeta, typeMeta));
    }

    @Test
    public void isTypesMatchSameSimpleTypeMetaTest() {
        TypeMeta<Long> typeMeta = new TypeMeta<>(Long.class);
        Assertions.assertTrue(TypeMetaUtil.isTypesMatch(typeMeta, typeMeta));
    }

    @Test
    public void isTypesMatchSuperclassHasMoreTypeParametersTypeMetaTest() {
        TypeMeta<Hashtable> typeMeta1 = new TypeMeta<>(Hashtable.class,
                new TypeMeta<?>[]{ TypeMeta.OBJECT_META, TypeMeta.OBJECT_META });
        TypeMeta<Properties> typeMeta2 = new TypeMeta<>(Properties.class);
        Assertions.assertTrue(TypeMetaUtil.isTypesMatch(typeMeta1, typeMeta2));
    }
}
