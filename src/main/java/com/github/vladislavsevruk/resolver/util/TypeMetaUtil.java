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
package com.github.vladislavsevruk.resolver.util;

import com.github.vladislavsevruk.resolver.context.TypeMetaResolvingContextManager;
import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeVariableMap;
import com.github.vladislavsevruk.resolver.type.WildcardBound;

/**
 * Utility methods for TypeMeta.
 */
public final class TypeMetaUtil {

    private TypeMetaUtil() {
    }

    /**
     * Checks if acceptor method parameter type is the same as donor method return type.
     *
     * @param acceptorParameterTypeMeta <code>TypeMeta</code> with actual types for acceptor method parameter type.
     * @param donorReturnTypeMeta       <code>TypeMeta</code> with actual types for donor method return type.
     * @return <code>true</code> if acceptor type and its generic types equals donor type and its generic
     * types, <code>false</code> otherwise.
     */
    public static boolean isSameTypes(TypeMeta<?> acceptorParameterTypeMeta, TypeMeta<?> donorReturnTypeMeta) {
        if (!PrimitiveWrapperUtil.wrap(acceptorParameterTypeMeta.getType())
                .equals(PrimitiveWrapperUtil.wrap(donorReturnTypeMeta.getType()))) {
            return false;
        }
        return isSameGenericTypes(acceptorParameterTypeMeta.getGenericTypes(), donorReturnTypeMeta.getGenericTypes());
    }

    /**
     * Checks if acceptor method parameter type can be cast from donor method return type without additional type
     * conversion.
     *
     * @param acceptorParameterTypeMeta <code>TypeMeta</code> with actual types for acceptor method parameter type.
     * @param donorReturnTypeMeta       <code>TypeMeta</code> with actual types for donor method return type.
     * @return <code>true</code> if acceptor type and its generic types can be cast from donor type and its generic
     * types, <code>false</code> otherwise.
     */
    public static boolean isTypesMatch(TypeMeta<?> acceptorParameterTypeMeta, TypeMeta<?> donorReturnTypeMeta) {
        if (TypeMeta.OBJECT_META.equals(acceptorParameterTypeMeta)) {
            return true;
        }
        if (!PrimitiveWrapperUtil.wrap(acceptorParameterTypeMeta.getType())
                .isAssignableFrom(PrimitiveWrapperUtil.wrap(donorReturnTypeMeta.getType()))) {
            return false;
        }
        TypeMeta<?>[] acceptorGenericTypes = acceptorParameterTypeMeta.getGenericTypes();
        if (acceptorGenericTypes.length == 0) {
            return true;
        }
        TypeMeta<?>[] donorGenericTypes = getDonorGenericTypes(acceptorParameterTypeMeta, donorReturnTypeMeta);
        return isGenericTypesMatch(acceptorGenericTypes, donorGenericTypes);
    }

    private static TypeMeta<?>[] getDonorGenericTypes(TypeMeta<?> acceptorParameterTypeMeta,
            TypeMeta<?> donorReturnTypeMeta) {
        Class<?> acceptorClass = acceptorParameterTypeMeta.getType();
        if (donorReturnTypeMeta.getType().equals(acceptorClass)) {
            return donorReturnTypeMeta.getGenericTypes();
        }
        TypeVariableMap<TypeMeta<?>> typeVariableMap = TypeMetaResolvingContextManager.getContext()
                .getMappedVariableHierarchyStorage().get(donorReturnTypeMeta).getTypeVariableMap(acceptorClass);
        return TypeMetaResolvingContextManager.getContext().getTypeResolverPicker().pickTypeResolver(acceptorClass)
                .resolve(typeVariableMap, acceptorClass).getGenericTypes();
    }

    private static boolean isGenericTypesMatch(TypeMeta<?> acceptorGenericTypeMeta, TypeMeta<?> donorGenericTypeMeta) {
        if (acceptorGenericTypeMeta.isWildcard()) {
            return TypeMeta.WILDCARD_META.equals(acceptorGenericTypeMeta) || isWildcardTypeMatch(
                    acceptorGenericTypeMeta, donorGenericTypeMeta);
        } else {
            if (!PrimitiveWrapperUtil.wrap(acceptorGenericTypeMeta.getType())
                    .equals(PrimitiveWrapperUtil.wrap(donorGenericTypeMeta.getType()))) {
                return false;
            }
            return isGenericTypesMatch(acceptorGenericTypeMeta.getGenericTypes(),
                    donorGenericTypeMeta.getGenericTypes());
        }
    }

    private static boolean isGenericTypesMatch(TypeMeta<?>[] acceptorGenericTypes, TypeMeta<?>[] donorGenericTypes) {
        if (acceptorGenericTypes.length != donorGenericTypes.length) {
            return false;
        }
        for (int i = 0; i < acceptorGenericTypes.length; ++i) {
            if (!isGenericTypesMatch(acceptorGenericTypes[i], donorGenericTypes[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSameGenericTypes(TypeMeta<?>[] acceptorGenericTypes, TypeMeta<?>[] donorGenericTypes) {
        if (acceptorGenericTypes.length != donorGenericTypes.length) {
            return false;
        }
        for (int i = 0; i < acceptorGenericTypes.length; ++i) {
            if (!isSameTypes(acceptorGenericTypes[i], donorGenericTypes[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isWildcardTypeMatch(TypeMeta<?> acceptorGenericTypeMeta, TypeMeta<?> donorGenericTypeMeta) {
        if (donorGenericTypeMeta.isWildcard() && !acceptorGenericTypeMeta.getWildcardBound()
                .equals(donorGenericTypeMeta.getWildcardBound())) {
            return false;
        }
        return WildcardBound.UPPER.equals(acceptorGenericTypeMeta.getWildcardBound()) ? isTypesMatch(
                acceptorGenericTypeMeta, donorGenericTypeMeta)
                : isTypesMatch(donorGenericTypeMeta, acceptorGenericTypeMeta);
    }
}
