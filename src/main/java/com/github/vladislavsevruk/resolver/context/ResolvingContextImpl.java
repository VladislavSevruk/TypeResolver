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
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementation of <code>ResolvingContext</code>.
 *
 * @see ResolvingContext
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode
final class ResolvingContextImpl implements ResolvingContext {

    private static final Logger logger = LogManager.getLogger(ResolvingContextImpl.class);

    MappedVariableHierarchyStorage mappedVariableHierarchyStorage;
    TypeResolverPicker typeResolverPicker;
    TypeVariableMapper typeVariableMapper;

    /**
     * Creates new instance using received modules or default implementations for nulls.
     *
     * @param mappedVariableHierarchyStorageFactoryMethod factory method for <code>MappedVariableHierarchyStorageImpl</code>
     *                                                    module implementation.
     * @param typeResolverPickerFactoryMethod             factory method for <code>TypeResolverPicker</code> module
     *                                                    implementation.
     * @param typeVariableMapperFactoryMethod             factory method for <code>TypeVariableMapper</code> module
     *                                                    implementation.
     */
    ResolvingContextImpl(
            ResolvingModuleFactoryMethod<MappedVariableHierarchyStorage> mappedVariableHierarchyStorageFactoryMethod,
            ResolvingModuleFactoryMethod<TypeResolverPicker> typeResolverPickerFactoryMethod,
            ResolvingModuleFactoryMethod<TypeVariableMapper> typeVariableMapperFactoryMethod) {
        this.typeResolverPicker = orDefault(typeResolverPickerFactoryMethod, context -> new TypeResolverPickerImpl());
        logger.debug(
                () -> String.format("Using '%s' as type resolver picker.", typeResolverPicker.getClass().getName()));
        this.typeVariableMapper = orDefault(typeVariableMapperFactoryMethod,
                context -> new TypeVariableMapperImpl(typeResolverPicker));
        logger.debug(
                () -> String.format("Using '%s' as type variable mapper.", typeVariableMapper.getClass().getName()));
        this.mappedVariableHierarchyStorage = orDefault(mappedVariableHierarchyStorageFactoryMethod,
                context -> new MappedVariableHierarchyStorageImpl(typeVariableMapper));
        logger.debug(() -> String.format("Using '%s' as mapped variable hierarchy storage.",
                mappedVariableHierarchyStorage.getClass().getName()));
    }

    private <T> T orDefault(ResolvingModuleFactoryMethod<T> factoryMethod,
            ResolvingModuleFactoryMethod<T> defaultFactoryMethod) {
        if (factoryMethod != null) {
            T module = factoryMethod.get(this);
            if (module != null) {
                return module;
            }
        }
        return defaultFactoryMethod.get(this);
    }
}
