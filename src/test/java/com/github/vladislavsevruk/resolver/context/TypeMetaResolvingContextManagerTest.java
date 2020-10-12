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
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.mapper.TypeMetaVariableMapper;
import com.github.vladislavsevruk.resolver.type.mapper.TypeVariableMapper;
import com.github.vladislavsevruk.resolver.type.storage.MappedVariableHierarchyStorage;
import com.github.vladislavsevruk.resolver.type.storage.TypeMetaMappedVariableHierarchyStorage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TypeMetaResolvingContextManagerTest {

    private static boolean initialAutoRefreshContext;
    private static ResolvingModuleFactoryMethod<TypeMeta<?>, MappedVariableHierarchyStorage<TypeMeta<?>>>
            initialMappedVariableHierarchyStorageFactoryMethod;
    private static ResolvingModuleFactoryMethod<TypeMeta<?>, TypeResolverPicker<TypeMeta<?>>>
            initialTypeResolverPickerFactoryMethod;
    private static ResolvingModuleFactoryMethod<TypeMeta<?>, TypeVariableMapper<TypeMeta<?>>>
            initialTypeVariableMapperFactoryMethod;

    @Mock
    private MappedVariableHierarchyStorage<TypeMeta<?>> mappedVariableHierarchyStorage;
    @Mock
    private TypeResolverPicker<TypeMeta<?>> typeResolverPicker;
    @Mock
    private TypeVariableMapper<TypeMeta<?>> typeVariableMapper;

    @BeforeAll
    static void disableContextRefresh() {
        initialAutoRefreshContext = TypeMetaResolvingContextManager.isAutoRefreshContext();
        initialMappedVariableHierarchyStorageFactoryMethod = TypeMetaResolvingModuleFactory
                .mappedVariableHierarchyStorage();
        initialTypeResolverPickerFactoryMethod = TypeMetaResolvingModuleFactory.typeResolverPicker();
        initialTypeVariableMapperFactoryMethod = TypeMetaResolvingModuleFactory.typeVariableMapper();
        TypeMetaResolvingContextManager.enableContextAutoRefresh();
    }

    @AfterAll
    static void setInitialAutoContextRefresh() {
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        TypeMetaResolvingModuleFactory
                .replaceMappedVariableHierarchyStorage(initialMappedVariableHierarchyStorageFactoryMethod);
        TypeMetaResolvingModuleFactory.replaceTypeResolverPicker(initialTypeResolverPickerFactoryMethod);
        TypeMetaResolvingModuleFactory.replaceTypeVariableMapper(initialTypeVariableMapperFactoryMethod);
        TypeMetaResolvingContextManager.refreshContext();
        if (initialAutoRefreshContext) {
            TypeMetaResolvingContextManager.enableContextAutoRefresh();
        }
    }

    @Test
    void autoRefreshContextAfterAllModulesUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.enableContextAutoRefresh();
        ResolvingContext resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        TypeMetaResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        TypeMetaResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        ResolvingContext resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(mappedVariableHierarchyStorage, resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(typeResolverPicker, resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(typeVariableMapper, resolvingContext2.getTypeVariableMapper());
    }

    @Test
    void autoRefreshContextAfterMappedVariableHierarchyStorageUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.enableContextAutoRefresh();
        ResolvingContext resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        ResolvingContext resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(mappedVariableHierarchyStorage, resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(resolvingContext1.getTypeResolverPicker(), resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(resolvingContext1.getTypeVariableMapper(), resolvingContext2.getTypeVariableMapper());
    }

    @Test
    void autoRefreshContextAfterTypeResolverPickerUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.enableContextAutoRefresh();
        ResolvingContext resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        ResolvingContext resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(typeResolverPicker, resolvingContext2.getTypeResolverPicker());
        Assertions
                .assertNotEquals(resolvingContext1.getTypeVariableMapper(), resolvingContext2.getTypeVariableMapper());
        TypeVariableMapper<TypeMeta<?>> expectedTypeVariableMapper = new TypeMetaVariableMapper(typeResolverPicker);
        Assertions.assertEquals(expectedTypeVariableMapper, resolvingContext2.getTypeVariableMapper());
        Assertions.assertNotEquals(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(new TypeMetaMappedVariableHierarchyStorage(expectedTypeVariableMapper),
                resolvingContext2.getMappedVariableHierarchyStorage());
    }

    @Test
    void autoRefreshContextAfterTypeVariableMapperUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.enableContextAutoRefresh();
        ResolvingContext resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        ResolvingContext resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertNotEquals(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(new TypeMetaMappedVariableHierarchyStorage(typeVariableMapper),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(resolvingContext1.getTypeResolverPicker(), resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(typeVariableMapper, resolvingContext2.getTypeVariableMapper());
    }

    @Test
    void equalContextAfterRefreshWithoutUpdatesTest() {
        ResolvingContext resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingContextManager.refreshContext();
        ResolvingContext resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotSame(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(resolvingContext1, resolvingContext2);
    }

    @Test
    void newContextAfterRefreshAfterAllModulesUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        TypeMetaResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        TypeMetaResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        TypeMetaResolvingContextManager.refreshContext();
        ResolvingContext resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(mappedVariableHierarchyStorage, resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(typeResolverPicker, resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(typeVariableMapper, resolvingContext2.getTypeVariableMapper());
    }

    @Test
    void newContextAfterRefreshAfterMappedVariableHierarchyStorageUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        TypeMetaResolvingContextManager.refreshContext();
        ResolvingContext resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertNotEquals(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(mappedVariableHierarchyStorage, resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(resolvingContext1.getTypeResolverPicker(), resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(resolvingContext1.getTypeVariableMapper(), resolvingContext2.getTypeVariableMapper());
    }

    @Test
    void newContextAfterRefreshAfterTypeResolverPickerUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        TypeMetaResolvingContextManager.refreshContext();
        ResolvingContext resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(typeResolverPicker, resolvingContext2.getTypeResolverPicker());
        Assertions
                .assertNotEquals(resolvingContext1.getTypeVariableMapper(), resolvingContext2.getTypeVariableMapper());
        TypeVariableMapper<TypeMeta<?>> expectedTypeVariableMapper = new TypeMetaVariableMapper(typeResolverPicker);
        Assertions.assertEquals(expectedTypeVariableMapper, resolvingContext2.getTypeVariableMapper());
        Assertions.assertNotEquals(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(new TypeMetaMappedVariableHierarchyStorage(expectedTypeVariableMapper),
                resolvingContext2.getMappedVariableHierarchyStorage());
    }

    @Test
    void newContextAfterRefreshAfterTypeVariableMapperUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        TypeMetaResolvingContextManager.refreshContext();
        ResolvingContext resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(resolvingContext1.getTypeResolverPicker(), resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(typeVariableMapper, resolvingContext2.getTypeVariableMapper());
        Assertions.assertNotEquals(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(new TypeMetaMappedVariableHierarchyStorage(typeVariableMapper),
                resolvingContext2.getMappedVariableHierarchyStorage());
    }

    @Test
    void sameContextIsReturnedIfAutoRefreshDisabledAfterMappedVariableHierarchyStorageUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        ResolvingContext resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertSame(resolvingContext1, resolvingContext2);
    }

    @Test
    void sameContextIsReturnedIfAutoRefreshDisabledAfterRefreshAfterAllModulesUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        TypeMetaResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        TypeMetaResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        ResolvingContext resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertEquals(resolvingContext1, resolvingContext2);
    }

    @Test
    void sameContextIsReturnedIfAutoRefreshDisabledAfterTypeResolverPickerUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        ResolvingContext resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertSame(resolvingContext1, resolvingContext2);
    }

    @Test
    void sameContextIsReturnedIfAutoRefreshDisabledAfterTypeVariableMapperUpdatesTest() {
        resetModulesAndContext();
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        TypeMetaResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        ResolvingContext resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertSame(resolvingContext1, resolvingContext2);
    }

    @Test
    void sameContextIsReturnedTest() {
        ResolvingContext resolvingContext1 = TypeMetaResolvingContextManager.getContext();
        ResolvingContext resolvingContext2 = TypeMetaResolvingContextManager.getContext();
        Assertions.assertSame(resolvingContext1, resolvingContext2);
    }

    private void resetModulesAndContext() {
        TypeMetaResolvingContextManager.disableContextAutoRefresh();
        TypeMetaResolvingModuleFactory.replaceMappedVariableHierarchyStorage(null);
        TypeMetaResolvingModuleFactory.replaceTypeResolverPicker(null);
        TypeMetaResolvingModuleFactory.replaceTypeVariableMapper(null);
        TypeMetaResolvingContextManager.refreshContext();
    }
}
