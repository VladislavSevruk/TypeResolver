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
import com.github.vladislavsevruk.resolver.type.storage.MappedVariableHierarchyStorage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ResolvingModuleFactoryTest {

    private static boolean initialAutoRefreshContext;

    @Mock
    private MappedVariableHierarchyStorage mappedVariableHierarchyStorage;
    @Mock
    private TypeResolverPicker typeResolverPicker;
    @Mock
    private TypeVariableMapper typeVariableMapper;

    @BeforeAll
    public static void disableContextRefresh() {
        initialAutoRefreshContext = ResolvingContextManager.isAutoRefreshContext();
        ResolvingContextManager.disableContextAutoRefresh();
    }

    @AfterAll
    public static void setInitialAutoContextRefresh() {
        if (initialAutoRefreshContext) {
            ResolvingContextManager.enableContextAutoRefresh();
        }
    }

    @Test
    public void defaultModulesTest() {
        Assertions.assertNull(ResolvingModuleFactory.mappedVariableHierarchyStorage());
        Assertions.assertNull(ResolvingModuleFactory.typeResolverPicker());
        Assertions.assertNull(ResolvingModuleFactory.typeVariableMapper());
    }

    @Test
    public void replaceTypeConverterStorageTest() {
        ResolvingModuleFactoryMethod<MappedVariableHierarchyStorage> factoryMethod
                = context -> mappedVariableHierarchyStorage;
        ResolvingModuleFactory.replaceMappedVariableHierarchyStorage(factoryMethod);
        Assertions.assertEquals(factoryMethod, ResolvingModuleFactory.mappedVariableHierarchyStorage());
    }

    @Test
    public void replaceTypeResolverPickerTest() {
        ResolvingModuleFactoryMethod<TypeResolverPicker> factoryMethod = context -> typeResolverPicker;
        ResolvingModuleFactory.replaceTypeResolverPicker(factoryMethod);
        Assertions.assertEquals(factoryMethod, ResolvingModuleFactory.typeResolverPicker());
    }

    @Test
    public void replaceTypeVariableMapperTest() {
        ResolvingModuleFactoryMethod<TypeVariableMapper> factoryMethod = context -> typeVariableMapper;
        ResolvingModuleFactory.replaceTypeVariableMapper(factoryMethod);
        Assertions.assertEquals(factoryMethod, ResolvingModuleFactory.typeVariableMapper());
    }
}
