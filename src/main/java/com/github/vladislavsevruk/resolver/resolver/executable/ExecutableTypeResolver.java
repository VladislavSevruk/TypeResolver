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
package com.github.vladislavsevruk.resolver.resolver.executable;

import com.github.vladislavsevruk.resolver.type.TypeMeta;
import com.github.vladislavsevruk.resolver.type.TypeProvider;

import java.lang.reflect.Executable;
import java.util.List;

/**
 * Resolves actual types for generic parameters of executables parameter, return and exception types.
 *
 * @param <T> type of mapped value for type variable.
 */
public interface ExecutableTypeResolver<T> {

    /**
     * Resolves actual types for generic exceptions of executables exception types.
     *
     * @param clazz      <code>Class</code> where executable was declared.
     * @param executable <code>Executable</code> to resolve actual parameter types for.
     * @return <code>List</code> with actual exception types for received executable.
     */
    List<T> getExceptionTypes(Class<?> clazz, Executable executable);

    /**
     * Resolves actual types for generic exceptions of executables exception types.
     *
     * @param typeMeta   <code>TypeMeta</code> with actual types for class where executable was declared.
     * @param executable <code>Executable</code> to resolve actual parameter types for.
     * @return <code>List</code> with actual exception types for received executable.
     */
    List<T> getExceptionTypes(TypeMeta<?> typeMeta, Executable executable);

    /**
     * Resolves actual types for generic exceptions of executables exception types.
     *
     * @param typeProvider <code>TypeProvider</code> with class where executable was declared as type parameter.
     * @param executable   <code>Executable</code> to resolve actual parameter types for.
     * @return <code>List</code> with actual exception types for received executable.
     */
    List<T> getExceptionTypes(TypeProvider<?> typeProvider, Executable executable);

    /**
     * Resolves actual types for generic parameters of executables parameter types.
     *
     * @param clazz      <code>Class</code> where executable was declared.
     * @param executable <code>Executable</code> to resolve actual parameter types for.
     * @return <code>List</code> with actual parameter types for received executable.
     */
    List<T> getParameterTypes(Class<?> clazz, Executable executable);

    /**
     * Resolves actual types for generic parameters of executables parameter types.
     *
     * @param typeMeta   <code>TypeMeta</code> with actual types for class where executable was declared.
     * @param executable <code>Executable</code> to resolve actual parameter types for.
     * @return <code>List</code> with actual parameter types for received executable.
     */
    List<T> getParameterTypes(TypeMeta<?> typeMeta, Executable executable);

    /**
     * Resolves actual types for generic parameters of executables parameter types.
     *
     * @param typeProvider <code>TypeProvider</code> with class where executable was declared as type parameter.
     * @param executable   <code>Executable</code> to resolve actual parameter types for.
     * @return <code>List</code> with actual parameter types for received executable.
     */
    List<T> getParameterTypes(TypeProvider<?> typeProvider, Executable executable);

    /**
     * Resolves actual types for generic parameters of executable return type.
     *
     * @param clazz      <code>Class</code> where executable was declared.
     * @param executable <code>Executable</code> to resolve actual return type for.
     * @return actual return type for received executable.
     */
    T getReturnType(Class<?> clazz, Executable executable);

    /**
     * Resolves actual types for generic parameters of executables return type.
     *
     * @param typeMeta   <code>TypeMeta</code> with actual types for class where executable was declared.
     * @param executable <code>Executable</code> to resolve actual return type for.
     * @return actual return type for received executable.
     */
    T getReturnType(TypeMeta<?> typeMeta, Executable executable);

    /**
     * Resolves actual types for generic parameters of executables return type.
     *
     * @param typeProvider <code>TypeProvider</code> with class where executable was declared as type parameter.
     * @param executable   <code>Executable</code> to resolve actual return type for.
     * @return actual return type for received executable.
     */
    T getReturnType(TypeProvider<?> typeProvider, Executable executable);
}
