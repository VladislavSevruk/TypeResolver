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

import com.github.vladislavsevruk.resolver.resolver.picker.TypeResolverPicker;
import com.github.vladislavsevruk.resolver.resolver.storage.TypeMetaResolverStorage;
import com.github.vladislavsevruk.resolver.resolver.storage.TypeResolverStorage;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.mapper.TypeMetaVariableMapper;
import com.github.vladislavsevruk.resolver.type.mapper.TypeVariableMapper;
import com.github.vladislavsevruk.resolver.type.storage.MappedVariableHierarchyStorage;
import com.github.vladislavsevruk.resolver.type.storage.TypeMetaMappedVariableHierarchyStorage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TypeMetaResolvingContextManagerTest {

    @Mock
    private MappedVariableHierarchyStorage<TypeMeta<?>> mappedVariableHierarchyStorage;
    @Mock
    private TypeResolverPicker<TypeMeta<?>> typeResolverPicker;
    @Mock
    private TypeResolverStorage<TypeMeta<?>> typeResolverStorage;
    @Mock
    private TypeVariableMapper<TypeMeta<?>> typeVariableMapper;

    @AfterAll
    static void setInitialAutoContextRefresh() {
        resetModulesAndContext();
    }

    @Test
    void autoRefreshContextAfterAllModulesUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.enableContextAutoRefresh();
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        TypeMetaResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        TypeMetaResolvingModuleFactory.replaceTypeResolverStorage(context -> typeResolverStorage);
        TypeMetaResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotSame(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(mappedVariableHierarchyStorage, resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(typeResolverPicker, resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(typeResolverStorage, resolvingContext2.getTypeResolverStorage());
        Assertions.assertEquals(typeVariableMapper, resolvingContext2.getTypeVariableMapper());
    }

    @Test
    void autoRefreshContextAfterMappedVariableHierarchyStorageUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.enableContextAutoRefresh();
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotSame(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(mappedVariableHierarchyStorage, resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertNotSame(resolvingContext1.getTypeResolverPicker(), resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(resolvingContext1.getTypeResolverPicker().getClass(),
                resolvingContext2.getTypeResolverPicker().getClass());
        Assertions
                .assertNotSame(resolvingContext1.getTypeResolverStorage(), resolvingContext2.getTypeResolverStorage());
        Assertions.assertEquals(resolvingContext1.getTypeResolverStorage().getClass(),
                resolvingContext2.getTypeResolverStorage().getClass());
        Assertions.assertNotSame(resolvingContext1.getTypeVariableMapper(), resolvingContext2.getTypeVariableMapper());
        Assertions.assertEquals(resolvingContext1.getTypeVariableMapper().getClass(),
                resolvingContext2.getTypeVariableMapper().getClass());
    }

    @Test
    void autoRefreshContextAfterTypeResolverPickerUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.enableContextAutoRefresh();
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotSame(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(typeResolverPicker, resolvingContext2.getTypeResolverPicker());
        Assertions.assertNotSame(resolvingContext1.getTypeVariableMapper(), resolvingContext2.getTypeVariableMapper());
        Assertions.assertEquals(TypeMetaVariableMapper.class, resolvingContext2.getTypeVariableMapper().getClass());
        Assertions
                .assertNotSame(resolvingContext1.getTypeResolverStorage(), resolvingContext2.getTypeResolverStorage());
        Assertions.assertEquals(TypeMetaResolverStorage.class, resolvingContext2.getTypeResolverStorage().getClass());
        Assertions.assertNotSame(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(TypeMetaMappedVariableHierarchyStorage.class,
                resolvingContext2.getMappedVariableHierarchyStorage().getClass());
    }

    @Test
    void autoRefreshContextAfterTypeResolverStorageUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.enableContextAutoRefresh();
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceTypeResolverStorage(context -> typeResolverStorage);
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotSame(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(typeResolverStorage, resolvingContext2.getTypeResolverStorage());
        Assertions.assertNotSame(resolvingContext1.getTypeResolverPicker(), resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(resolvingContext1.getTypeResolverPicker().getClass(),
                resolvingContext2.getTypeResolverPicker().getClass());
        Assertions.assertNotSame(resolvingContext1.getTypeVariableMapper(), resolvingContext2.getTypeVariableMapper());
        Assertions.assertEquals(resolvingContext1.getTypeVariableMapper().getClass(),
                resolvingContext2.getTypeVariableMapper().getClass());
        Assertions.assertNotSame(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(resolvingContext1.getMappedVariableHierarchyStorage().getClass(),
                resolvingContext2.getMappedVariableHierarchyStorage().getClass());
    }

    @Test
    void autoRefreshContextAfterTypeVariableMapperUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.enableContextAutoRefresh();
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotSame(resolvingContext1, resolvingContext2);
        Assertions.assertNotSame(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(TypeMetaMappedVariableHierarchyStorage.class,
                resolvingContext2.getMappedVariableHierarchyStorage().getClass());
        Assertions.assertNotSame(resolvingContext1.getTypeResolverPicker(), resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(resolvingContext1.getTypeResolverPicker().getClass(),
                resolvingContext2.getTypeResolverPicker().getClass());
        Assertions
                .assertNotSame(resolvingContext1.getTypeResolverStorage(), resolvingContext2.getTypeResolverStorage());
        Assertions.assertEquals(resolvingContext1.getTypeResolverStorage().getClass(),
                resolvingContext2.getTypeResolverStorage().getClass());
        Assertions.assertEquals(typeVariableMapper, resolvingContext2.getTypeVariableMapper());
    }

    @Test
    void equalContextAfterRefreshWithoutUpdatesTest() {
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingContextManager.refreshContext();
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotSame(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(resolvingContext1.getClass(), resolvingContext2.getClass());
    }

    @Test
    void newContextAfterRefreshAfterAllModulesUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        TypeMetaResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        TypeMetaResolvingModuleFactory.replaceTypeResolverStorage(context -> typeResolverStorage);
        TypeMetaResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        TypeMetaResolvingContextManager.refreshContext();
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotSame(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(mappedVariableHierarchyStorage, resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(typeResolverPicker, resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(typeResolverStorage, resolvingContext2.getTypeResolverStorage());
        Assertions.assertEquals(typeVariableMapper, resolvingContext2.getTypeVariableMapper());
    }

    @Test
    void newContextAfterRefreshAfterMappedVariableHierarchyStorageUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        TypeMetaResolvingContextManager.refreshContext();
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotSame(resolvingContext1, resolvingContext2);
        Assertions.assertNotSame(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(mappedVariableHierarchyStorage, resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertNotSame(resolvingContext1.getTypeResolverPicker(), resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(resolvingContext1.getTypeResolverPicker().getClass(),
                resolvingContext2.getTypeResolverPicker().getClass());
        Assertions
                .assertNotSame(resolvingContext1.getTypeResolverStorage(), resolvingContext2.getTypeResolverStorage());
        Assertions.assertEquals(resolvingContext1.getTypeResolverStorage().getClass(),
                resolvingContext2.getTypeResolverStorage().getClass());
        Assertions.assertNotSame(resolvingContext1.getTypeVariableMapper(), resolvingContext2.getTypeVariableMapper());
        Assertions.assertEquals(resolvingContext1.getTypeVariableMapper().getClass(),
                resolvingContext2.getTypeVariableMapper().getClass());
    }

    @Test
    void newContextAfterRefreshAfterTypeResolverPickerUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        TypeMetaResolvingContextManager.refreshContext();
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotSame(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(typeResolverPicker, resolvingContext2.getTypeResolverPicker());
        Assertions.assertNotSame(resolvingContext1.getTypeVariableMapper(), resolvingContext2.getTypeVariableMapper());
        Assertions.assertEquals(TypeMetaVariableMapper.class, resolvingContext2.getTypeVariableMapper().getClass());
        Assertions
                .assertNotSame(resolvingContext1.getTypeResolverStorage(), resolvingContext2.getTypeResolverStorage());
        Assertions.assertEquals(TypeMetaResolverStorage.class, resolvingContext2.getTypeResolverStorage().getClass());
        Assertions.assertNotSame(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(TypeMetaMappedVariableHierarchyStorage.class,
                resolvingContext2.getMappedVariableHierarchyStorage().getClass());
    }

    @Test
    void newContextAfterRefreshAfterTypeResolverStorageUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceTypeResolverStorage(context -> typeResolverStorage);
        TypeMetaResolvingContextManager.refreshContext();
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotSame(resolvingContext1, resolvingContext2);
        Assertions
                .assertNotSame(resolvingContext1.getTypeResolverStorage(), resolvingContext2.getTypeResolverStorage());
        Assertions.assertEquals(typeResolverStorage, resolvingContext2.getTypeResolverStorage());
        Assertions.assertNotSame(resolvingContext1.getTypeResolverPicker(), resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(resolvingContext1.getTypeResolverPicker().getClass(),
                resolvingContext2.getTypeResolverPicker().getClass());
        Assertions.assertNotSame(resolvingContext1.getTypeVariableMapper(), resolvingContext2.getTypeVariableMapper());
        Assertions.assertEquals(resolvingContext1.getTypeVariableMapper().getClass(),
                resolvingContext2.getTypeVariableMapper().getClass());
        Assertions.assertNotSame(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(resolvingContext1.getMappedVariableHierarchyStorage().getClass(),
                resolvingContext2.getMappedVariableHierarchyStorage().getClass());
    }

    @Test
    void newContextAfterRefreshAfterTypeVariableMapperUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        TypeMetaResolvingContextManager.refreshContext();
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotSame(resolvingContext1, resolvingContext2);
        Assertions.assertNotSame(resolvingContext1.getTypeResolverPicker(), resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(resolvingContext1.getTypeResolverPicker().getClass(),
                resolvingContext2.getTypeResolverPicker().getClass());
        Assertions
                .assertNotSame(resolvingContext1.getTypeResolverStorage(), resolvingContext2.getTypeResolverStorage());
        Assertions.assertEquals(resolvingContext1.getTypeResolverStorage().getClass(),
                resolvingContext2.getTypeResolverStorage().getClass());
        Assertions.assertEquals(typeVariableMapper, resolvingContext2.getTypeVariableMapper());
        Assertions.assertNotSame(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(TypeMetaMappedVariableHierarchyStorage.class,
                resolvingContext2.getMappedVariableHierarchyStorage().getClass());
    }

    @Test
    void sameContextIsReturnedIfAutoRefreshDisabledAfterMappedVariableHierarchyStorageUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertSame(resolvingContext1, resolvingContext2);
    }

    @Test
    void sameContextIsReturnedIfAutoRefreshDisabledAfterRefreshAfterAllModulesUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        TypeMetaResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        TypeMetaResolvingModuleFactory.replaceTypeResolverStorage(context -> typeResolverStorage);
        TypeMetaResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertEquals(resolvingContext1, resolvingContext2);
    }

    @Test
    void sameContextIsReturnedIfAutoRefreshDisabledAfterTypeResolverPickerUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertSame(resolvingContext1, resolvingContext2);
    }

    @Test
    void sameContextIsReturnedIfAutoRefreshDisabledAfterTypeResolverStorageUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceTypeResolverStorage(context -> typeResolverStorage);
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertSame(resolvingContext1, resolvingContext2);
    }

    @Test
    void sameContextIsReturnedIfAutoRefreshDisabledAfterTypeVariableMapperUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertSame(resolvingContext1, resolvingContext2);
    }

    @Test
    void sameContextIsReturnedTest() {
        ResolvingContext<TypeMeta<?>> resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        ResolvingContext<TypeMeta<?>> resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertSame(resolvingContext1, resolvingContext2);
    }

    private static void resetModulesAndContext() {
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        TypeMetaResolvingModuleFactory.replaceMappedVariableHierarchyStorage(null);
        TypeMetaResolvingModuleFactory.replaceTypeResolverPicker(null);
        TypeMetaResolvingModuleFactory.replaceTypeResolverStorage(null);
        TypeMetaResolvingModuleFactory.replaceTypeVariableMapper(null);
        TypeMetaResolvingContextManager.refreshContext();
    }
}
