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
package com.github.vladislavsevruk.resolver.context;

import com.github.vladislavsevruk.resolver.resolver.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.resolver.TypeResolverPickerImpl;
import com.github.vladislavsevruk.resolver.type.mapper.TypeVariableMapper;
import com.github.vladislavsevruk.resolver.type.mapper.TypeVariableMapperImpl;
import com.github.vladislavsevruk.resolver.type.storage.MappedVariableHierarchyStorage;
import com.github.vladislavsevruk.resolver.type.storage.MappedVariableHierarchyStorageImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResolvingContextImplTest {

    @Mock
    private MappedVariableHierarchyStorage mappedVariableHierarchyStorage;
    @Mock
    private TypeResolverPicker typeResolverPicker;
    @Mock
    private TypeVariableMapper typeVariableMapper;

    @Test
    void customMappedVariableHierarchyStorageFactoryMethodReturnsNullTest() {
        ResolvingContext resolvingContext = new ResolvingContextImpl(context -> null, null, null);
        Assertions.assertEquals(MappedVariableHierarchyStorageImpl.class,
                resolvingContext.getMappedVariableHierarchyStorage().getClass());
        Assertions.assertEquals(TypeResolverPickerImpl.class, resolvingContext.getTypeResolverPicker().getClass());
        Assertions.assertEquals(TypeVariableMapperImpl.class, resolvingContext.getTypeVariableMapper().getClass());
    }

    @Test
    void customMappedVariableHierarchyStorageTest() {
        ResolvingContext resolvingContext = new ResolvingContextImpl(context -> mappedVariableHierarchyStorage, null,
                null);
        Assertions.assertEquals(mappedVariableHierarchyStorage, resolvingContext.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(TypeResolverPickerImpl.class, resolvingContext.getTypeResolverPicker().getClass());
        Assertions.assertEquals(TypeVariableMapperImpl.class, resolvingContext.getTypeVariableMapper().getClass());
    }

    @Test
    void customModulesFactoryMethodReturnNullTest() {
        ResolvingContext resolvingContext = new ResolvingContextImpl(context -> null, context -> null, context -> null);
        Assertions.assertEquals(MappedVariableHierarchyStorageImpl.class,
                resolvingContext.getMappedVariableHierarchyStorage().getClass());
        Assertions.assertEquals(TypeResolverPickerImpl.class, resolvingContext.getTypeResolverPicker().getClass());
        Assertions.assertEquals(TypeVariableMapperImpl.class, resolvingContext.getTypeVariableMapper().getClass());
    }

    @Test
    void customModulesTest() {
        ResolvingContext resolvingContext = new ResolvingContextImpl(context -> mappedVariableHierarchyStorage,
                context -> typeResolverPicker, context -> typeVariableMapper);
        Assertions.assertEquals(mappedVariableHierarchyStorage, resolvingContext.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(typeResolverPicker, resolvingContext.getTypeResolverPicker());
        Assertions.assertEquals(typeVariableMapper, resolvingContext.getTypeVariableMapper());
    }

    @Test
    void customTypeResolverPickerFactoryMethodReturnsNullTest() {
        ResolvingContext resolvingContext = new ResolvingContextImpl(null, context -> null, null);
        Assertions.assertEquals(MappedVariableHierarchyStorageImpl.class,
                resolvingContext.getMappedVariableHierarchyStorage().getClass());
        Assertions.assertEquals(TypeResolverPickerImpl.class, resolvingContext.getTypeResolverPicker().getClass());
        Assertions.assertEquals(TypeVariableMapperImpl.class, resolvingContext.getTypeVariableMapper().getClass());
    }

    @Test
    void customTypeResolverPickerTest() {
        ResolvingContext resolvingContext = new ResolvingContextImpl(null, context -> typeResolverPicker, null);
        Assertions.assertEquals(MappedVariableHierarchyStorageImpl.class,
                resolvingContext.getMappedVariableHierarchyStorage().getClass());
        Assertions.assertEquals(typeResolverPicker, resolvingContext.getTypeResolverPicker());
        Assertions.assertEquals(TypeVariableMapperImpl.class, resolvingContext.getTypeVariableMapper().getClass());
    }

    @Test
    void customTypeVariableMapperFactoryMethodReturnsNullTest() {
        ResolvingContext resolvingContext = new ResolvingContextImpl(null, null, context -> null);
        Assertions.assertEquals(MappedVariableHierarchyStorageImpl.class,
                resolvingContext.getMappedVariableHierarchyStorage().getClass());
        Assertions.assertEquals(TypeResolverPickerImpl.class, resolvingContext.getTypeResolverPicker().getClass());
        Assertions.assertEquals(TypeVariableMapperImpl.class, resolvingContext.getTypeVariableMapper().getClass());
    }

    @Test
    void customTypeVariableMapperTest() {
        ResolvingContext resolvingContext = new ResolvingContextImpl(null, null, context -> typeVariableMapper);
        Assertions.assertEquals(MappedVariableHierarchyStorageImpl.class,
                resolvingContext.getMappedVariableHierarchyStorage().getClass());
        Assertions.assertEquals(TypeResolverPickerImpl.class, resolvingContext.getTypeResolverPicker().getClass());
        Assertions.assertEquals(typeVariableMapper, resolvingContext.getTypeVariableMapper());
    }

    @Test
    void defaultModulesTest() {
        ResolvingContext resolvingContext = new ResolvingContextImpl(null, null, null);
        Assertions.assertEquals(MappedVariableHierarchyStorageImpl.class,
                resolvingContext.getMappedVariableHierarchyStorage().getClass());
        Assertions.assertEquals(TypeResolverPickerImpl.class, resolvingContext.getTypeResolverPicker().getClass());
        Assertions.assertEquals(TypeVariableMapperImpl.class, resolvingContext.getTypeVariableMapper().getClass());
    }
}
