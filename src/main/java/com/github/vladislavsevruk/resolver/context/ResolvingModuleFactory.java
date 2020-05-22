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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Provides replaceable modules schemas required for resolving mechanism.
 */
public final class ResolvingModuleFactory {

    private static final ReadWriteLock MAPPED_VARIABLE_HIERARCHY_STORAGE_LOCK = new ReentrantReadWriteLock();
    private static final ReadWriteLock TYPE_RESOLVER_PICKER_LOCK = new ReentrantReadWriteLock();
    private static final ReadWriteLock TYPE_VARIABLE_MAPPER_LOCK = new ReentrantReadWriteLock();
    private static final Logger logger = LogManager.getLogger(ResolvingModuleFactory.class);
    private static ResolvingModuleFactoryMethod<MappedVariableHierarchyStorage> mappedVariableHierarchyStorage;
    private static ResolvingModuleFactoryMethod<TypeResolverPicker> typeResolverPicker;
    private static ResolvingModuleFactoryMethod<TypeVariableMapper> typeVariableMapper;

    private ResolvingModuleFactory() {
    }

    /**
     * Returns current instance of <code>ResolvingModuleFactoryMethod</code> for <code>MappedVariableHierarchyStorage</code>.
     */
    public static ResolvingModuleFactoryMethod<MappedVariableHierarchyStorage> mappedVariableHierarchyStorage() {
        MAPPED_VARIABLE_HIERARCHY_STORAGE_LOCK.readLock().lock();
        ResolvingModuleFactoryMethod<MappedVariableHierarchyStorage> storageToReturn
                = ResolvingModuleFactory.mappedVariableHierarchyStorage;
        MAPPED_VARIABLE_HIERARCHY_STORAGE_LOCK.readLock().unlock();
        return storageToReturn;
    }

    /**
     * Replaces instance of <code>ResolvingModuleFactoryMethod</code> for <code>MappedVariableHierarchyStorage</code>.
     * All further resolves will use new instance.
     *
     * @param storage new instance of <code>ResolvingModuleFactoryMethod</code> for <code>MappedVariableHierarchyStorage</code>.
     */
    public static void replaceMappedVariableHierarchyStorage(
            ResolvingModuleFactoryMethod<MappedVariableHierarchyStorage> storage) {
        MAPPED_VARIABLE_HIERARCHY_STORAGE_LOCK.writeLock().lock();
        logger.info(() -> String.format("Replacing MappedVariableHierarchyStorage by '%s'.",
                storage == null ? null : storage.getClass().getName()));
        ResolvingModuleFactory.mappedVariableHierarchyStorage = storage;
        MAPPED_VARIABLE_HIERARCHY_STORAGE_LOCK.writeLock().unlock();
        if (ResolvingContextManager.isAutoRefreshContext()) {
            ResolvingContextManager.refreshContext();
        }
    }

    /**
     * Replaces instance of <code>ResolvingModuleFactoryMethod</code> for <code>TypeResolverPicker</code>. All further
     * resolves will use new instance.
     *
     * @param picker new instance of <code>ResolvingModuleFactoryMethod</code> for <code>TypeResolverPicker</code>.
     */
    public static void replaceTypeResolverPicker(ResolvingModuleFactoryMethod<TypeResolverPicker> picker) {
        TYPE_RESOLVER_PICKER_LOCK.writeLock().lock();
        logger.info(() -> String
                .format("Replacing TypeResolverPicker by '%s'.", picker == null ? null : picker.getClass().getName()));
        ResolvingModuleFactory.typeResolverPicker = picker;
        TYPE_RESOLVER_PICKER_LOCK.writeLock().unlock();
        if (ResolvingContextManager.isAutoRefreshContext()) {
            ResolvingContextManager.refreshContext();
        }
    }

    /**
     * Replaces instance of <code>ResolvingModuleFactoryMethod</code> for <code>TypeVariableMapper</code>. All further
     * resolves will use new instance.
     *
     * @param mapper new instance of <code>ResolvingModuleFactoryMethod</code> for <code>TypeVariableMapper</code>.
     */
    public static void replaceTypeVariableMapper(ResolvingModuleFactoryMethod<TypeVariableMapper> mapper) {
        TYPE_VARIABLE_MAPPER_LOCK.writeLock().lock();
        logger.info(() -> String
                .format("Replacing TypeVariableMapper by '%s'.", mapper == null ? null : mapper.getClass().getName()));
        ResolvingModuleFactory.typeVariableMapper = mapper;
        TYPE_VARIABLE_MAPPER_LOCK.writeLock().unlock();
        if (ResolvingContextManager.isAutoRefreshContext()) {
            ResolvingContextManager.refreshContext();
        }
    }

    /**
     * Returns current instance of <code>ResolvingModuleFactoryMethod</code> for <code>TypeResolverPicker</code>.
     */
    public static ResolvingModuleFactoryMethod<TypeResolverPicker> typeResolverPicker() {
        TYPE_RESOLVER_PICKER_LOCK.readLock().lock();
        ResolvingModuleFactoryMethod<TypeResolverPicker> pickerToReturn = ResolvingModuleFactory.typeResolverPicker;
        TYPE_RESOLVER_PICKER_LOCK.readLock().unlock();
        return pickerToReturn;
    }

    /**
     * Returns current instance of <code>ResolvingModuleFactoryMethod</code> for <code>TypeVariableMapper</code>.
     */
    public static ResolvingModuleFactoryMethod<TypeVariableMapper> typeVariableMapper() {
        TYPE_VARIABLE_MAPPER_LOCK.readLock().lock();
        ResolvingModuleFactoryMethod<TypeVariableMapper> mapperToReturn = ResolvingModuleFactory.typeVariableMapper;
        TYPE_VARIABLE_MAPPER_LOCK.readLock().unlock();
        return mapperToReturn;
    }
}
