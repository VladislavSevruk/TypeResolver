[![Build Status](https://travis-ci.org/VladislavSevruk/TypeResolver.svg?branch=develop)](https://travis-ci.com/VladislavSevruk/TypeResolver)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=VladislavSevruk_TypeResolver&metric=alert_status)](https://sonarcloud.io/dashboard?id=VladislavSevruk_TypeResolver)
[![Code Coverage](https://sonarcloud.io/api/project_badges/measure?project=VladislavSevruk_TypeResolver&metric=coverage)](https://sonarcloud.io/component_measures?id=VladislavSevruk_TypeResolver&metric=coverage)

# Java Type Resolver
This utility library helps to determine real types for generics, arrays, wildcards and other complex types on runtime. 
Receiving type for generics can be a problem because of Java doesn't store information about generic types parameters 
on runtime so this library helps to solve this problem.

* [Getting started](#getting-started)
  * [Maven](#maven)
  * [Gradle](#gradle)
* [Main entities](#main-entities)
* [Usage](#usage)
  * [Resolve field types](#resolve-field-types)
  * [Resolve methods arguments and return types](#resolve-methods-arguments-and-return-types)
* [License](#license)

## Getting started
### Maven
Add the following dependency to your pom.xml:
```
<dependency>
      <groupId>com.github.vladislavsevruk</groupId>
      <artifactId>type-resolver</artifactId>
      <version>1.0.0</version>
</dependency>
```
### Gradle
Add the following dependency to your build.gradle:
```
implementation 'com.github.vladislavsevruk:type-resolver:1.0.0'
```

## Main entities
* [TypeMeta](src/main/java/com/github/vladislavsevruk/resolver/type/TypeMeta.java) represents metadata about type and its 
type parameters for generic types and arrays.  
Examples of resulted __TypeMeta__ structure for different cases:
  - __List&lt;Long&gt;__:
  ```
  { type: List, genericTypes: [{ type: Long }]}
  ```
  - __long[]__:
  ```
  { type: long[], genericTypes: [{ type: long }]}
  ```
  - __Map&lt;String, Integer&gt;[]__:
  ```
  { type: Map[], genericTypes: [{ type: Map, genericTypes: [{ type: String }, { type: Integer }]}]}
  ```
  - etc.

* [TypeProvider](src/main/java/com/github/vladislavsevruk/resolver/type/TypeProvider.java) provides easy to use methods for 
generating __TypeMeta__ for generic types. 
  - with __TypeProvider__:
  ```
  TypeMeta<?> typeMeta = new TypeProvider<Map<String, List<Integer>>> () {}.getTypeMeta();
  ```
  - without __TypeProvider__:
  ```
  TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(String.class);
  TypeMeta<?> deepInnerTypeMeta = new TypeMeta<>(Integer.class);
  TypeMeta<?>[] deepInnerTypeMetas = new TypeMeta<?>[] { deepInnerTypeMeta };
  TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(List.class, deepInnerTypeMetas);
  TypeMeta<?>[] innerTypeMetas = new TypeMeta<?>[] { innerTypeMeta1, innerTypeMeta2 };
  TypeMeta<Map> typeMeta = new TypeMeta<>(Map.class, innerTypeMetas);
  ```

* [FieldTypeResolver](src/main/java/com/github/vladislavsevruk/resolver/resolver/FieldTypeResolver.java) can be used to 
determine __TypeMeta__ of field of provided class. Library has [default implementation](src/main/java/com/github/vladislavsevruk/resolver/resolver/FieldTypeResolverImpl.java) 
of this interface.
* [ExecutableTypeResolver](src/main/java/com/github/vladislavsevruk/resolver/resolver/ExecutableTypeResolver.java) can be 
used to determine __TypeMeta__ for return and arguments types of provided method. Library has [default implementation](src/main/java/com/github/vladislavsevruk/resolver/resolver/ExecutableTypeResolverImpl.java) 
of this interface.

## Usage
### Resolve field types
For resolving fields type can be used [FieldTypeResolverImpl](src/main/java/com/github/vladislavsevruk/resolver/resolver/FieldTypeResolverImpl.java).
```
FieldTypeResolver fieldTypeResolver = new FieldTypeResolverImpl();
Field fieldToResolve = ...; // get class field to determine it's type
Class<?> classWhereFieldDeclared = ...; // get class where field declared
TypeMeta<?> fieldTypeMeta = fieldTypeResolver.resolveField(classWhereFieldDeclared, fieldToResolve);
```
or with descendant of [TypeProvider](src/main/java/com/github/vladislavsevruk/resolver/type/TypeProvider.java) for classes 
that use generic parameters at its definition.
```
FieldTypeResolver fieldTypeResolver = new FieldTypeResolverImpl();
Field fieldToResolve = ...; // get class field to determine it's type
TypeProvider<?> typeProvider = new TypeProvider<GenericClass<Parameter>>() {}; // type provider with generic class where field declared
TypeMeta<?> fieldTypeMeta = fieldTypeResolver.resolveField(typeProvider, fieldToResolve);
```

### Resolve methods arguments and return types
For resolving arguments or return type of method can be used [ExecutableTypeResolverImpl](src/main/java/com/github/vladislavsevruk/resolver/resolver/ExecutableTypeResolverImpl.java).
```
ExecutableTypeResolver executableTypeResolver = new ExecutableTypeResolverImpl();
Method methodToResolve = ...; // get method to determine it's return and arguments types
Class<?> classWhereMethodDeclared = ...; // get class where method declared
TypeMeta<?> methodReturnTypeMeta = executableTypeResolver.getReturnType(classWhereMethodDeclared, methodToResolve);
List<TypeMeta<?>> methodArgumentsTypeMetaList = executableTypeResolver
        .getParameterTypes(classWhereMethodDeclared, methodToResolve);
```
or with descendant of [TypeProvider](src/main/java/com/github/vladislavsevruk/resolver/type/TypeProvider.java) for classes 
that use generic parameters at its definition.
```
ExecutableTypeResolver executableTypeResolver = new ExecutableTypeResolverImpl();
Method methodToResolve = ...; // get method to determine it's return and arguments types
TypeProvider<?> typeProvider = new TypeProvider<GenericClass<Parameter>>() {}; // type provider with generic class where method declared
TypeMeta<?> methodReturnTypeMeta = executableTypeResolver.getReturnType(typeProvider, methodToResolve);
List<TypeMeta<?>> methodArgumentsTypeMetaList = executableTypeResolver.getParameterTypes(typeProvider, methodToResolve);
```

## License
This project is licensed under the MIT License, you can read the full text [here](LICENSE).