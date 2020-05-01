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

import com.github.vladislavsevruk.resolver.exception.TypeResolvingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.TypeVariable;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Properties;

@ExtendWith(MockitoExtension.class)
public class MappedVariableHierarchyTest {

    private TypeMeta<?> typeMeta1 = new TypeMeta<>(String.class);
    private TypeMeta<?> typeMeta2 = new TypeMeta<>(Byte.class);
    @Mock
    private TypeVariable<? extends Class<?>> typeVariable1;
    @Mock
    private TypeVariable<? extends Class<?>> typeVariable2;

    @Test
    public void addSeveralTypeVariablesTest() {
        MappedVariableHierarchy hierarchy = new MappedVariableHierarchy(HashSet.class);
        hierarchy.addTypeVariable(HashSet.class, typeVariable1, typeMeta1);
        hierarchy.addTypeVariable(HashSet.class, typeVariable2, typeMeta2);
        TypeVariableMap typeVariableMap = hierarchy.getTypeVariableMap(HashSet.class);
        Assertions.assertEquals(typeMeta1, typeVariableMap.getActualType(typeVariable1));
        Assertions.assertEquals(typeMeta2, typeVariableMap.getActualType(typeVariable2));
        TypeVariableMap emptyMap = new TypeVariableMap();
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(AbstractSet.class));
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(AbstractCollection.class));
    }

    @Test
    public void addSeveralTypeVariablesToSuperclassTest() {
        MappedVariableHierarchy hierarchy = new MappedVariableHierarchy(HashSet.class);
        hierarchy.addTypeVariable(AbstractSet.class, typeVariable1, typeMeta1);
        hierarchy.addTypeVariable(AbstractSet.class, typeVariable2, typeMeta2);
        TypeVariableMap emptyMap = new TypeVariableMap();
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(HashSet.class));
        TypeVariableMap typeVariableMap = hierarchy.getTypeVariableMap(AbstractSet.class);
        Assertions.assertEquals(typeMeta1, typeVariableMap.getActualType(typeVariable1));
        Assertions.assertEquals(typeMeta2, typeVariableMap.getActualType(typeVariable2));
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(AbstractCollection.class));
    }

    @Test
    public void addTypeVariableTest() {
        MappedVariableHierarchy hierarchy = new MappedVariableHierarchy(HashSet.class);
        hierarchy.addTypeVariable(HashSet.class, typeVariable1, typeMeta1);
        TypeVariableMap typeVariableMap = hierarchy.getTypeVariableMap(HashSet.class);
        Assertions.assertEquals(typeMeta1, typeVariableMap.getActualType(typeVariable1));
        TypeVariableMap emptyMap = new TypeVariableMap();
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(AbstractSet.class));
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(AbstractCollection.class));
    }

    @Test
    public void addTypeVariableToSuperclassTest() {
        MappedVariableHierarchy hierarchy = new MappedVariableHierarchy(HashSet.class);
        hierarchy.addTypeVariable(AbstractSet.class, typeVariable1, typeMeta1);
        TypeVariableMap emptyMap = new TypeVariableMap();
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(HashSet.class));
        TypeVariableMap typeVariableMap = hierarchy.getTypeVariableMap(AbstractSet.class);
        Assertions.assertEquals(typeMeta1, typeVariableMap.getActualType(typeVariable1));
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(AbstractCollection.class));
    }

    @Test
    public void addTypeVariablesToSeveralClassesAtHierarchyTest() {
        MappedVariableHierarchy hierarchy = new MappedVariableHierarchy(LinkedHashSet.class);
        hierarchy.addTypeVariable(HashSet.class, typeVariable1, typeMeta1);
        hierarchy.addTypeVariable(AbstractSet.class, typeVariable2, typeMeta2);
        TypeVariableMap emptyMap = new TypeVariableMap();
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(LinkedHashSet.class));
        TypeVariableMap hashSetTypeVariableMap = hierarchy.getTypeVariableMap(HashSet.class);
        Assertions.assertEquals(typeMeta1, hashSetTypeVariableMap.getActualType(typeVariable1));
        Assertions.assertNull(hashSetTypeVariableMap.getActualType(typeVariable2));
        TypeVariableMap abstractSetTypeVariableMap = hierarchy.getTypeVariableMap(AbstractSet.class);
        Assertions.assertNull(abstractSetTypeVariableMap.getActualType(typeVariable1));
        Assertions.assertEquals(typeMeta2, abstractSetTypeVariableMap.getActualType(typeVariable2));
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(AbstractCollection.class));
    }

    @Test
    public void exceptionForAddingTypeVariableForClassNotInHierarchyTest() {
        MappedVariableHierarchy hierarchy = new MappedVariableHierarchy(Long.class);
        Assertions.assertThrows(TypeResolvingException.class,
                () -> hierarchy.addTypeVariable(HashSet.class, typeVariable1, typeMeta1));
    }

    @Test
    public void exceptionForClassNotInHierarchyTest() {
        MappedVariableHierarchy hierarchy = new MappedVariableHierarchy(HashSet.class);
        Assertions.assertThrows(TypeResolvingException.class, () -> hierarchy.getTypeVariableMap(AbstractList.class));
    }

    @Test
    public void exceptionForGettingObjectClassTypeVariableMapTest() {
        MappedVariableHierarchy hierarchy = new MappedVariableHierarchy(Long.class);
        Assertions.assertThrows(TypeResolvingException.class, () -> hierarchy.getTypeVariableMap(Object.class));
    }

    @Test
    public void newInstanceBuildHierarchyClassWithSeveralSuperclassesTest() {
        MappedVariableHierarchy hierarchy = new MappedVariableHierarchy(Properties.class);
        TypeVariableMap emptyMap = new TypeVariableMap();
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(Properties.class));
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(Hashtable.class));
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(Dictionary.class));
    }

    @Test
    public void newInstanceBuildHierarchyObjectClassTest() {
        MappedVariableHierarchy hierarchy = new MappedVariableHierarchy(Object.class);
        Assertions.assertEquals(new TypeVariableMap(), hierarchy.getTypeVariableMap(Object.class));
    }

    @Test
    public void newInstanceBuildHierarchyObjectHeirClassTest() {
        MappedVariableHierarchy hierarchy = new MappedVariableHierarchy(Date.class);
        Assertions.assertEquals(new TypeVariableMap(), hierarchy.getTypeVariableMap(Date.class));
    }

    @Test
    public void newInstanceBuildHierarchyObjectHeirParameterizedClassTest() {
        MappedVariableHierarchy hierarchy = new MappedVariableHierarchy(AbstractCollection.class);
        Assertions.assertEquals(new TypeVariableMap(), hierarchy.getTypeVariableMap(AbstractCollection.class));
    }

    @Test
    public void newInstanceBuildHierarchyParameterizedClassWithSeveralSuperclassesTest() {
        MappedVariableHierarchy hierarchy = new MappedVariableHierarchy(HashSet.class);
        TypeVariableMap emptyMap = new TypeVariableMap();
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(HashSet.class));
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(AbstractSet.class));
        Assertions.assertEquals(emptyMap, hierarchy.getTypeVariableMap(AbstractCollection.class));
    }
}
