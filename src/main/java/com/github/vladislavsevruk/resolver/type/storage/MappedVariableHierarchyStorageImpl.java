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

import com.github.vladislavsevruk.resolver.context.ResolvingContextManager;
import com.github.vladislavsevruk.resolver.type.MappedVariableHierarchy;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.mapper.TypeVariableMapper;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of <code>MappedVariableHierarchyStorage</code>.
 *
 * @see MappedVariableHierarchyStorage
 */
@EqualsAndHashCode
public final class MappedVariableHierarchyStorageImpl implements MappedVariableHierarchyStorage {

    private Map<TypeMeta<?>, MappedVariableHierarchy> hierarchyMap = new ConcurrentHashMap<>();
    private TypeVariableMapper typeVariableMapper;

    public MappedVariableHierarchyStorageImpl() {
        this(ResolvingContextManager.getContext().getTypeVariableMapper());
    }

    public MappedVariableHierarchyStorageImpl(TypeVariableMapper typeVariableMapper) {
        this.typeVariableMapper = typeVariableMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MappedVariableHierarchy get(TypeMeta<?> typeMeta) {
        if (!hierarchyMap.containsKey(typeMeta)) {
            buildHierarchy(typeMeta);
        }
        return hierarchyMap.get(typeMeta);
    }

    private synchronized void buildHierarchy(TypeMeta<?> typeMeta) {
        // second check after synchronization
        if (!hierarchyMap.containsKey(typeMeta)) {
            hierarchyMap.put(typeMeta, typeVariableMapper.mapTypeVariables(typeMeta));
        }
    }
}
