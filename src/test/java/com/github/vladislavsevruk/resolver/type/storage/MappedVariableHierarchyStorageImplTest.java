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
package com.github.vladislavsevruk.resolver.type.storage;

import com.github.vladislavsevruk.resolver.type.MappedVariableHierarchy;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class MappedVariableHierarchyStorageImplTest {

    private TypeMetaMappedVariableHierarchyStorage realStorage = new TypeMetaMappedVariableHierarchyStorage();

    @Test
    void getAlreadyPresentHierarchyTest() {
        MappedVariableHierarchy expectedHierarchy = new MappedVariableHierarchy(Long.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(Long.class);
        expectedHierarchy.addTypeVariable(Comparable.class, Comparable.class.getTypeParameters()[0], typeMeta);
        MappedVariableHierarchy firstHierarchy = realStorage.get(typeMeta);
        MappedVariableHierarchy secondHierarchy = realStorage.get(typeMeta);
        Assertions.assertEquals(expectedHierarchy, secondHierarchy);
        Assertions.assertSame(firstHierarchy, secondHierarchy);
    }

    @Test
    void getHierarchyForTest() {
        MappedVariableHierarchy expectedHierarchy = new MappedVariableHierarchy(Short.class);
        TypeMeta<?> typeMeta = new TypeMeta<>(Short.class);
        expectedHierarchy.addTypeVariable(Comparable.class, Comparable.class.getTypeParameters()[0], typeMeta);
        Assertions.assertEquals(expectedHierarchy, realStorage.get(typeMeta));
    }

    @Test
    void hierarchyWithSameGenericClassTest() {
        TypeMeta<?> typeMeta1 = new TypeMeta<>(List.class, new TypeMeta<?>[]{ new TypeMeta<>(Integer.class) });
        TypeMeta<?> typeMeta2 = new TypeMeta<>(List.class, new TypeMeta<?>[]{ new TypeMeta<>(Number.class) });
        MappedVariableHierarchy firstHierarchy = realStorage.get(typeMeta1);
        MappedVariableHierarchy secondHierarchy = realStorage.get(typeMeta2);
        Assertions.assertNotSame(firstHierarchy, secondHierarchy);
    }
}
