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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.TypeVariable;

@ExtendWith(MockitoExtension.class)
class TypeVariableMapTest {

    private TypeMeta<?> typeMeta1 = new TypeMeta<>(String.class);
    private TypeMeta<?> typeMeta2 = new TypeMeta<>(Byte.class);
    @Mock
    private TypeVariable<? extends Class<?>> typeVariable1;
    @Mock
    private TypeVariable<? extends Class<?>> typeVariable2;
    @Mock
    private TypeVariable<? extends Class<?>> typeVariable3;

    @Test
    void mapNotPresentTypeVariableEmptyMapTest() {
        Assertions.assertNull(new TypeVariableMap().getActualType(typeVariable1));
    }

    @Test
    void mapNotPresentTypeVariableNonEmptyMapTest() {
        TypeVariableMap typeVariableMap = new TypeVariableMap();
        typeVariableMap.addTypeVariable(typeVariable1, typeMeta1);
        Assertions.assertNull(new TypeVariableMap().getActualType(typeVariable2));
    }

    @Test
    void mapNotPresentTypeVariableSeveralItemsMapTest() {
        TypeVariableMap typeVariableMap = new TypeVariableMap();
        typeVariableMap.addTypeVariable(typeVariable1, typeMeta1);
        typeVariableMap.addTypeVariable(typeVariable2, typeMeta2);
        Assertions.assertNull(typeVariableMap.getActualType(typeVariable3));
    }

    @Test
    void mapNullTypeVariableEmptyMapTest() {
        Assertions.assertNull(new TypeVariableMap().getActualType(null));
    }

    @Test
    void mapNullTypeVariableNonEmptyMapTest() {
        TypeVariableMap typeVariableMap = new TypeVariableMap();
        typeVariableMap.addTypeVariable(typeVariable1, typeMeta1);
        Assertions.assertNull(typeVariableMap.getActualType(null));
    }

    @Test
    void mapNullTypeVariableSeveralItemsMapTest() {
        TypeVariableMap typeVariableMap = new TypeVariableMap();
        typeVariableMap.addTypeVariable(typeVariable1, typeMeta1);
        typeVariableMap.addTypeVariable(typeVariable2, typeMeta2);
        Assertions.assertNull(typeVariableMap.getActualType(null));
    }

    @Test
    void mapPresentTypeVariableNonEmptyMapTest() {
        TypeVariableMap typeVariableMap = new TypeVariableMap();
        typeVariableMap.addTypeVariable(typeVariable1, typeMeta1);
        Assertions.assertEquals(typeMeta1, typeVariableMap.getActualType(typeVariable1));
    }

    @Test
    void mapPresentTypeVariableSeveralItemsMapTest() {
        TypeVariableMap typeVariableMap = new TypeVariableMap();
        typeVariableMap.addTypeVariable(typeVariable1, typeMeta1);
        typeVariableMap.addTypeVariable(typeVariable2, typeMeta2);
        Assertions.assertEquals(typeMeta1, typeVariableMap.getActualType(typeVariable1));
        Assertions.assertEquals(typeMeta2, typeVariableMap.getActualType(typeVariable2));
    }
}
