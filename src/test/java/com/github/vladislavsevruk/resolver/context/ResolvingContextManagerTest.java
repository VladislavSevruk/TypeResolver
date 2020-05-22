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
import com.github.vladislavsevruk.resolver.type.mapper.TypeVariableMapper;
import com.github.vladislavsevruk.resolver.type.mapper.TypeVariableMapperImpl;
import com.github.vladislavsevruk.resolver.type.storage.MappedVariableHierarchyStorage;
import com.github.vladislavsevruk.resolver.type.storage.MappedVariableHierarchyStorageImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ResolvingContextManagerTest {

    private static boolean initialAutoRefreshContext;
    private static ResolvingModuleFactoryMethod<MappedVariableHierarchyStorage>
            initialMappedVariableHierarchyStorageFactoryMethod;
    private static ResolvingModuleFactoryMethod<TypeResolverPicker> initialTypeResolverPickerFactoryMethod;
    private static ResolvingModuleFactoryMethod<TypeVariableMapper> initialTypeVariableMapperFactoryMethod;

    @Mock
    private MappedVariableHierarchyStorage mappedVariableHierarchyStorage;
    @Mock
    private TypeResolverPicker typeResolverPicker;
    @Mock
    private TypeVariableMapper typeVariableMapper;

    @BeforeAll
    public static void disableContextRefresh() {
        initialAutoRefreshContext = ResolvingContextManager.isAutoRefreshContext();
        initialMappedVariableHierarchyStorageFactoryMethod = ResolvingModuleFactory.mappedVariableHierarchyStorage();
        initialTypeResolverPickerFactoryMethod = ResolvingModuleFactory.typeResolverPicker();
        initialTypeVariableMapperFactoryMethod = ResolvingModuleFactory.typeVariableMapper();
        ResolvingContextManager.enableContextAutoRefresh();
    }

    @AfterAll
    public static void setInitialAutoContextRefresh() {
        ResolvingContextManager.disableContextAutoRefresh();
        ResolvingModuleFactory
                .replaceMappedVariableHierarchyStorage(initialMappedVariableHierarchyStorageFactoryMethod);
        ResolvingModuleFactory.replaceTypeResolverPicker(initialTypeResolverPickerFactoryMethod);
        ResolvingModuleFactory.replaceTypeVariableMapper(initialTypeVariableMapperFactoryMethod);
        ResolvingContextManager.refreshContext();
        if (initialAutoRefreshContext) {
            ResolvingContextManager.enableContextAutoRefresh();
        }
    }

    @Test
    public void autoRefreshContextAfterAllModulesUpdatesTest() {
        resetModulesAndContext();
        ResolvingContextManager.enableContextAutoRefresh();
        ResolvingContext resolvingContext1 = ResolvingContextManager.getContext();
        ResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        ResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        ResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        ResolvingContext resolvingContext2 = ResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(mappedVariableHierarchyStorage, resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(typeResolverPicker, resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(typeVariableMapper, resolvingContext2.getTypeVariableMapper());
    }

    @Test
    public void autoRefreshContextAfterMappedVariableHierarchyStorageUpdatesTest() {
        resetModulesAndContext();
        ResolvingContextManager.enableContextAutoRefresh();
        ResolvingContext resolvingContext1 = ResolvingContextManager.getContext();
        ResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        ResolvingContext resolvingContext2 = ResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(mappedVariableHierarchyStorage, resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(resolvingContext1.getTypeResolverPicker(), resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(resolvingContext1.getTypeVariableMapper(), resolvingContext2.getTypeVariableMapper());
    }

    @Test
    public void autoRefreshContextAfterTypeResolverPickerUpdatesTest() {
        resetModulesAndContext();
        ResolvingContextManager.enableContextAutoRefresh();
        ResolvingContext resolvingContext1 = ResolvingContextManager.getContext();
        ResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        ResolvingContext resolvingContext2 = ResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(typeResolverPicker, resolvingContext2.getTypeResolverPicker());
        Assertions
                .assertNotEquals(resolvingContext1.getTypeVariableMapper(), resolvingContext2.getTypeVariableMapper());
        TypeVariableMapper expectedTypeVariableMapper = new TypeVariableMapperImpl(typeResolverPicker);
        Assertions.assertEquals(expectedTypeVariableMapper, resolvingContext2.getTypeVariableMapper());
        Assertions.assertNotEquals(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(new MappedVariableHierarchyStorageImpl(expectedTypeVariableMapper),
                resolvingContext2.getMappedVariableHierarchyStorage());
    }

    @Test
    public void autoRefreshContextAfterTypeVariableMapperUpdatesTest() {
        resetModulesAndContext();
        ResolvingContextManager.enableContextAutoRefresh();
        ResolvingContext resolvingContext1 = ResolvingContextManager.getContext();
        ResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        ResolvingContext resolvingContext2 = ResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertNotEquals(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(new MappedVariableHierarchyStorageImpl(typeVariableMapper),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(resolvingContext1.getTypeResolverPicker(), resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(typeVariableMapper, resolvingContext2.getTypeVariableMapper());
    }

    @Test
    public void equalContextAfterRefreshWithoutUpdatesTest() {
        ResolvingContext resolvingContext1 = ResolvingContextManager.getContext();
        ResolvingContextManager.refreshContext();
        ResolvingContext resolvingContext2 = ResolvingContextManager.getContext();
        Assertions.assertNotSame(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(resolvingContext1, resolvingContext2);
    }

    @Test
    public void newContextAfterRefreshAfterAllModulesUpdatesTest() {
        resetModulesAndContext();
        ResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = ResolvingContextManager.getContext();
        ResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        ResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        ResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        ResolvingContextManager.refreshContext();
        ResolvingContext resolvingContext2 = ResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(mappedVariableHierarchyStorage, resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(typeResolverPicker, resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(typeVariableMapper, resolvingContext2.getTypeVariableMapper());
    }

    @Test
    public void newContextAfterRefreshAfterMappedVariableHierarchyStorageUpdatesTest() {
        resetModulesAndContext();
        ResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = ResolvingContextManager.getContext();
        ResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        ResolvingContextManager.refreshContext();
        ResolvingContext resolvingContext2 = ResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertNotEquals(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(mappedVariableHierarchyStorage, resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(resolvingContext1.getTypeResolverPicker(), resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(resolvingContext1.getTypeVariableMapper(), resolvingContext2.getTypeVariableMapper());
    }

    @Test
    public void newContextAfterRefreshAfterTypeResolverPickerUpdatesTest() {
        resetModulesAndContext();
        ResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = ResolvingContextManager.getContext();
        ResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        ResolvingContextManager.refreshContext();
        ResolvingContext resolvingContext2 = ResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(typeResolverPicker, resolvingContext2.getTypeResolverPicker());
        Assertions
                .assertNotEquals(resolvingContext1.getTypeVariableMapper(), resolvingContext2.getTypeVariableMapper());
        TypeVariableMapper expectedTypeVariableMapper = new TypeVariableMapperImpl(typeResolverPicker);
        Assertions.assertEquals(expectedTypeVariableMapper, resolvingContext2.getTypeVariableMapper());
        Assertions.assertNotEquals(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(new MappedVariableHierarchyStorageImpl(expectedTypeVariableMapper),
                resolvingContext2.getMappedVariableHierarchyStorage());
    }

    @Test
    public void newContextAfterRefreshAfterTypeVariableMapperUpdatesTest() {
        resetModulesAndContext();
        ResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = ResolvingContextManager.getContext();
        ResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        ResolvingContextManager.refreshContext();
        ResolvingContext resolvingContext2 = ResolvingContextManager.getContext();
        Assertions.assertNotEquals(resolvingContext1, resolvingContext2);
        Assertions.assertEquals(resolvingContext1.getTypeResolverPicker(), resolvingContext2.getTypeResolverPicker());
        Assertions.assertEquals(typeVariableMapper, resolvingContext2.getTypeVariableMapper());
        Assertions.assertNotEquals(resolvingContext1.getMappedVariableHierarchyStorage(),
                resolvingContext2.getMappedVariableHierarchyStorage());
        Assertions.assertEquals(new MappedVariableHierarchyStorageImpl(typeVariableMapper),
                resolvingContext2.getMappedVariableHierarchyStorage());
    }

    @Test
    public void sameContextIsReturnedIfAutoRefreshDisabledAfterMappedVariableHierarchyStorageUpdatesTest() {
        resetModulesAndContext();
        ResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = ResolvingContextManager.getContext();
        ResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        ResolvingContext resolvingContext2 = ResolvingContextManager.getContext();
        Assertions.assertSame(resolvingContext1, resolvingContext2);
    }

    @Test
    public void sameContextIsReturnedIfAutoRefreshDisabledAfterRefreshAfterAllModulesUpdatesTest() {
        resetModulesAndContext();
        ResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = ResolvingContextManager.getContext();
        ResolvingModuleFactory.replaceMappedVariableHierarchyStorage(context -> mappedVariableHierarchyStorage);
        ResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        ResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        ResolvingContext resolvingContext2 = ResolvingContextManager.getContext();
        Assertions.assertEquals(resolvingContext1, resolvingContext2);
    }

    @Test
    public void sameContextIsReturnedIfAutoRefreshDisabledAfterTypeResolverPickerUpdatesTest() {
        resetModulesAndContext();
        ResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = ResolvingContextManager.getContext();
        ResolvingModuleFactory.replaceTypeResolverPicker(context -> typeResolverPicker);
        ResolvingContext resolvingContext2 = ResolvingContextManager.getContext();
        Assertions.assertSame(resolvingContext1, resolvingContext2);
    }

    @Test
    public void sameContextIsReturnedIfAutoRefreshDisabledAfterTypeVariableMapperUpdatesTest() {
        resetModulesAndContext();
        ResolvingContextManager.disableContextAutoRefresh();
        ResolvingContext resolvingContext1 = ResolvingContextManager.getContext();
        ResolvingModuleFactory.replaceTypeVariableMapper(context -> typeVariableMapper);
        ResolvingContext resolvingContext2 = ResolvingContextManager.getContext();
        Assertions.assertSame(resolvingContext1, resolvingContext2);
    }

    @Test
    public void sameContextIsReturnedTest() {
        ResolvingContext resolvingContext1 = ResolvingContextManager.getContext();
        ResolvingContext resolvingContext2 = ResolvingContextManager.getContext();
        Assertions.assertSame(resolvingContext1, resolvingContext2);
    }

    private void resetModulesAndContext() {
        ResolvingContextManager.disableContextAutoRefresh();
        ResolvingModuleFactory.replaceMappedVariableHierarchyStorage(null);
        ResolvingModuleFactory.replaceTypeResolverPicker(null);
        ResolvingModuleFactory.replaceTypeVariableMapper(null);
        ResolvingContextManager.refreshContext();
    }
}
