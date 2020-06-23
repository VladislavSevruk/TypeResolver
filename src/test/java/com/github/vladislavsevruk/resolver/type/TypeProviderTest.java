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
package com.github.vladislavsevruk.resolver.type;

import com.github.vladislavsevruk.resolver.type.mapper.TypeVariableMapper;
import com.github.vladislavsevruk.resolver.type.mapper.TypeVariableMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class TypeProviderTest {

    private TypeVariableMapper realTypeVariableMapper = new TypeVariableMapperImpl();

    @Test
    void compareTest() {
        TypeProvider<Short> provider1 = new TypeProvider<Short>() {};
        TypeProvider<Short> provider2 = new TypeProvider<Short>() {};
        Assertions.assertEquals(0, provider1.compareTo(provider2));
    }

    @Test
    void equalsNullTest() {
        TypeProvider<HashMap<String, List<Integer>>> provider1 = new TypeProvider<HashMap<String, List<Integer>>>() {};
        Assertions.assertNotEquals(null, provider1);
    }

    @Test
    void equalsTest() {
        TypeProvider<HashMap<String, List<Integer>>> provider1 = new TypeProvider<HashMap<String, List<Integer>>>() {};
        TypeProvider<HashMap<String, List<Integer>>> provider2 = new TypeProvider<HashMap<String, List<Integer>>>() {};
        Assertions.assertEquals(provider1, provider2);
    }

    @Test
    void getTypeMetaCacheTest() {
        TypeProvider<List<Double>> provider = new TypeProvider<List<Double>>() {};
        Assertions.assertSame(provider.getTypeMeta(), provider.getTypeMeta());
    }

    @Test
    void getTypeMetaWithMapperCacheTest() {
        TypeProvider<List<Double>> provider = new TypeProvider<List<Double>>() {};
        Assertions.assertSame(provider.getTypeMeta(), provider.getTypeMeta(realTypeVariableMapper));
    }

    @Test
    void hashCodeTest() {
        TypeProvider<Map<Byte, Boolean[]>> provider = new TypeProvider<Map<Byte, Boolean[]>>() {};
        Assertions.assertEquals(provider.hashCode(), provider.getTypeMeta().hashCode());
    }

    @Test
    void mockMapperInnerParameterizedClassTest() {
        TypeMeta<?> listMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) });
        TypeMeta<?> expectedMeta = new TypeMeta<>(HashMap.class,
                new TypeMeta<?>[]{ new TypeMeta<>(String.class), listMeta });
        TypeVariableMapper mockTypeVariableMapper = mockTypeVariableMapper(expectedMeta);
        TypeMeta<?> actualMeta = new TypeProvider<HashMap<String, List<Integer>>>() {}
                .getTypeMeta(mockTypeVariableMapper);
        Assertions.assertEquals(expectedMeta, actualMeta);
    }

    @Test
    void mockMapperParameterizedClassTest() {
        TypeMeta<?> expectedMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ new TypeMeta<>(Date.class) });
        TypeVariableMapper mockTypeVariableMapper = mockTypeVariableMapper(expectedMeta);
        TypeMeta<?> actualMeta = new TypeProvider<Set<Date>>() {}.getTypeMeta(mockTypeVariableMapper);
        Assertions.assertEquals(expectedMeta, actualMeta);
    }

    @Test
    void mockMapperSimpleClassTest() {
        TypeMeta<?> expectedMeta = new TypeMeta<>(Long.class);
        TypeVariableMapper mockTypeVariableMapper = mockTypeVariableMapper(expectedMeta);
        TypeMeta<?> actualMeta = new TypeProvider<Long>() {}.getTypeMeta(mockTypeVariableMapper);
        Assertions.assertEquals(expectedMeta, actualMeta);
    }

    @Test
    void notEqualsTest() {
        TypeProvider<HashMap<String, Set<Number>>> provider1 = new TypeProvider<HashMap<String, Set<Number>>>() {};
        TypeProvider<HashMap<String, Set<Double>>> provider2 = new TypeProvider<HashMap<String, Set<Double>>>() {};
        Assertions.assertNotEquals(provider1, provider2);
    }

    @Test
    void realMapperInnerParameterizedClassTest() {
        TypeMeta<?> listMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) });
        TypeMeta<?> expectedMeta = new TypeMeta<>(HashMap.class,
                new TypeMeta<?>[]{ new TypeMeta<>(String.class), listMeta });
        TypeMeta<?> actualMeta = new TypeProvider<HashMap<String, List<Integer>>>() {}
                .getTypeMeta(realTypeVariableMapper);
        Assertions.assertEquals(expectedMeta, actualMeta);
    }

    @Test
    void realMapperParameterizedClassTest() {
        TypeMeta<?> expectedMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ new TypeMeta<>(Date.class) });
        TypeMeta<?> actualMeta = new TypeProvider<Set<Date>>() {}.getTypeMeta(realTypeVariableMapper);
        Assertions.assertEquals(expectedMeta, actualMeta);
    }

    @Test
    void realMapperSimpleClassTest() {
        TypeMeta<?> expectedMeta = new TypeMeta<>(Long.class);
        TypeMeta<?> actualMeta = new TypeProvider<Long>() {}.getTypeMeta(realTypeVariableMapper);
        Assertions.assertEquals(expectedMeta, actualMeta);
    }

    @Test
    void withoutArgumentMapperInnerParameterizedClassTest() {
        TypeMeta<?> listMeta = new TypeMeta<>(List.class, new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) });
        TypeMeta<?> expectedMeta = new TypeMeta<>(HashMap.class,
                new TypeMeta<?>[]{ new TypeMeta<>(String.class), listMeta });
        TypeMeta<?> actualMeta = new TypeProvider<HashMap<String, List<Integer>>>() {}.getTypeMeta();
        Assertions.assertEquals(expectedMeta, actualMeta);
    }

    @Test
    void withoutArgumentMapperParameterizedClassTest() {
        TypeMeta<?> expectedMeta = new TypeMeta<>(Set.class, new TypeMeta<?>[]{ new TypeMeta<>(Date.class) });
        TypeMeta<?> actualMeta = new TypeProvider<Set<Date>>() {}.getTypeMeta();
        Assertions.assertEquals(expectedMeta, actualMeta);
    }

    @Test
    void withoutArgumentMapperSimpleClassTest() {
        TypeMeta<?> expectedMeta = new TypeMeta<>(Long.class);
        TypeMeta<?> actualMeta = new TypeProvider<Long>() {}.getTypeMeta();
        Assertions.assertEquals(expectedMeta, actualMeta);
    }

    @SuppressWarnings("unchecked")
    private TypeVariableMapper mockTypeVariableMapper(TypeMeta metaToReturn) {
        TypeVariableMap mockTypeVariableMap = Mockito.mock(TypeVariableMap.class);
        Mockito.when(mockTypeVariableMap.getActualType(ArgumentMatchers.any())).thenReturn(metaToReturn);
        MappedVariableHierarchy mockMappedVariableHierarchy = Mockito.mock(MappedVariableHierarchy.class);
        Mockito.when(mockMappedVariableHierarchy.getTypeVariableMap(ArgumentMatchers.any()))
                .thenReturn(mockTypeVariableMap);
        TypeVariableMapper mockTypeVariableMapper = Mockito.mock(TypeVariableMapper.class);
        Mockito.when(mockTypeVariableMapper.mapTypeVariables(ArgumentMatchers.any(Class.class)))
                .thenReturn(mockMappedVariableHierarchy);
        return mockTypeVariableMapper;
    }
}
