[![Build Status](https://travis-ci.org/VladislavSevruk/TypeResolver.svg?branch=develop)](https://travis-ci.com/VladislavSevruk/TypeResolver)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=VladislavSevruk_TypeResolver&metric=alert_status)](https://sonarcloud.io/dashboard?id=VladislavSevruk_TypeResolver)
[![Code Coverage](https://sonarcloud.io/api/project_badges/measure?project=VladislavSevruk_TypeResolver&metric=coverage)](https://sonarcloud.io/component_measures?id=VladislavSevruk_TypeResolver&metric=coverage)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.vladislavsevruk/type-resolver/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.vladislavsevruk/type-resolver)

# Java Type Resolver
This utility library helps to determine real types for generics, arrays, wildcards and other complex types on runtime. 
Receiving type for generics can be a problem because of [Java doesn't store information about generic type parameters 
on runtime](https://docs.oracle.com/javase/tutorial/java/generics/erasure.html) so this library helps to solve this problem.

## Table of contents
* [Getting started](#getting-started)
  * [Maven](#maven)
  * [Gradle](#gradle)
* [Main entities](#main-entities)
  * [TypeMeta](#typemeta)
  * [TypeProvider](#typeprovider)
  * [FieldTypeResolver](#fieldtyperesolver)
  * [ExecutableTypeResolver](#executabletyperesolver)
* [Usage](#usage)
  * [Determine field type](#determine-field-type)
  * [Determine method argument and return types](#determine-method-argument-and-return-types)
* [License](#license)

## Getting started
To add library to your project perform next steps:

### Maven
Add the following dependency to your pom.xml:
```xml
<dependency>
      <groupId>com.github.vladislavsevruk</groupId>
      <artifactId>type-resolver</artifactId>
      <version>1.0.3</version>
</dependency>
```
### Gradle
Add the following dependency to your build.gradle:
```groovy
implementation 'com.github.vladislavsevruk:type-resolver:1.0.3'
```

## Main entities
### TypeMeta
[TypeMeta](src/main/java/com/github/vladislavsevruk/resolver/type/TypeMeta.java) represents metadata about type 
parameter(s) for generics and arrays.  
Examples of resulted __TypeMeta__ structure for different cases:
  - __List&lt;? extends Number&gt;__:
  ```javascript
  { type: List, wildcard: false, genericTypes: [
      { type: Number, wildcard: true, genericTypes:[] }
  ] }
  ```
  - __long[]__:
  ```javascript
  { type: long[], wildcard: false, genericTypes: [
      { type: long, wildcard: false, genericTypes:[] }
  ] }
  ```
  - __Map&lt;String, Integer&gt;[]__:
  ```javascript
  { type: Map[], wildcard: false, genericTypes: [
      { type: Map, wildcard: false, genericTypes: [
          { type: String, wildcard: false, genericTypes:[] },
          { type: Integer, wildcard: false, genericTypes:[] }
      ] }
  ] }
  ```

### TypeProvider
[TypeProvider](src/main/java/com/github/vladislavsevruk/resolver/type/TypeProvider.java) provides easy to use method 
for generating __TypeMeta__ for generics.
  - With __TypeProvider__:
  ```kotlin
  TypeMeta<?> typeMeta = new TypeProvider<Map<String, List<Integer>>>() {}.getTypeMeta();
  ```
  - Without __TypeProvider__:
  ```kotlin
  TypeMeta<?> innerTypeMeta1 = new TypeMeta<>(String.class);
  TypeMeta<?> deepInnerTypeMeta = new TypeMeta<>(Integer.class);
  TypeMeta<?>[] deepInnerTypeMetas = new TypeMeta<?>[] { deepInnerTypeMeta };
  TypeMeta<?> innerTypeMeta2 = new TypeMeta<>(List.class, deepInnerTypeMetas);
  TypeMeta<?>[] innerTypeMetas = new TypeMeta<?>[] { innerTypeMeta1, innerTypeMeta2 };
  TypeMeta<Map> typeMeta = new TypeMeta<>(Map.class, innerTypeMetas);
  ```

### FieldTypeResolver
[FieldTypeResolver](src/main/java/com/github/vladislavsevruk/resolver/resolver/field/FieldTypeResolver.java) can be used to 
determine __TypeMeta__ for field of provided class. Library has 
[default implementation](src/main/java/com/github/vladislavsevruk/resolver/resolver/field/FieldTypeMetaResolver.java) 
of this interface.

### ExecutableTypeResolver
[ExecutableTypeResolver](src/main/java/com/github/vladislavsevruk/resolver/resolver/executable/ExecutableTypeResolver.java) can be 
used to determine __TypeMeta__ for return and argument types of provided method. Library has 
[default implementation](src/main/java/com/github/vladislavsevruk/resolver/resolver/executable/ExecutableTypeMetaResolver.java) 
of this interface.

## Usage
### Determine field type
Let's assume that we have following generic class:
```java
public class Cake<T> {
    private List<String> ingredients;
    private T filling;
}
```

and we need to determine its fields type. We can use 
[FieldTypeMetaResolver](src/main/java/com/github/vladislavsevruk/resolver/resolver/field/FieldTypeMetaResolver.java)
for this purpose:
```kotlin
FieldTypeResolver<TypeMeta<?>> fieldTypeResolver = new FieldTypeMetaResolver();
// get class field to determine its type
Field fieldToResolve = Cake.class.getDeclaredField("ingredients");
TypeMeta<?> fieldTypeMeta = fieldTypeResolver.resolveField(Cake.class, fieldToResolve);
```

Resulted __TypeMeta__ will have following structure:
```javascript
{ type: List, wildcard: false, genericTypes: [
    { type: String, wildcard: false, genericTypes:[] }
] }
```

If we need to determine type of field that use generic parameter(s) we may use subclass of 
[TypeProvider](src/main/java/com/github/vladislavsevruk/resolver/type/TypeProvider.java):
```kotlin
FieldTypeResolver<TypeMeta<?>> fieldTypeResolver = new FieldTypeMetaResolver();
// get class field to determine its type
Field fieldToResolve = Cake.class.getDeclaredField("filling");
// create type provider with generic class where field declared
// we use String as Cake's type parameter for this example
TypeProvider<?> typeProvider = new TypeProvider<Cake<String>>() {};
TypeMeta<?> fieldTypeMeta = fieldTypeResolver.resolveField(typeProvider, fieldToResolve);
```

And as a result __TypeMeta__ will have following structure:
```javascript
{ type: String, wildcard: false, genericTypes:[] }
```

### Determine method argument and return types
Let's assume that our generic class have following methods:
```java
public class Cake<T> {
    ...
    public List<String> getIngredients() {
        ...
    }

    public void setFilling(T filling) {
        ...
    }
}
```

To determine their argument or return types we can use 
[ExecutableTypeMetaResolver](src/main/java/com/github/vladislavsevruk/resolver/resolver/executable/ExecutableTypeMetaResolver.java):
```kotlin
ExecutableTypeResolver<TypeMeta<?>> executableTypeResolver = new ExecutableTypeMetaResolver();
// get method to determine its return and argument types
Method methodToResolve = Cake.class.getDeclaredMethod("getIngredients");
TypeMeta<?> methodReturnTypeMeta = executableTypeResolver.getReturnType(Cake.class, methodToResolve);
List<TypeMeta<?>> methodArgumentsTypeMetaList = executableTypeResolver
        .getParameterTypes(Cake.class, methodToResolve);
```

Resulted __TypeMeta__ items will have following structures:
  - Return type (_methodReturnTypeMeta_):
  ```javascript
  { type: List, wildcard: false, genericTypes: [
      { type: String, wildcard: false, genericTypes:[] }
  ] }
  ```
  - Argument types (_methodArgumentsTypeMetaList_):
  ```javascript
  []
  ```

If we need to determine types of method that uses generic parameters we may use subclass of 
[TypeProvider](src/main/java/com/github/vladislavsevruk/resolver/type/TypeProvider.java):
```kotlin
ExecutableTypeResolver<TypeMeta<?>> executableTypeResolver = new ExecutableTypeMetaResolver();
// get method to determine its return and argument types
Method methodToResolve = Cake.class.getDeclaredMethod("setFilling", Object.class);
// create type provider with generic class where field declared
// we use String as Cake's type parameter for this example
TypeProvider<?> typeProvider = new TypeProvider<Cake<String>>() {};
TypeMeta<?> methodReturnTypeMeta = executableTypeResolver.getReturnType(typeProvider, methodToResolve);
List<TypeMeta<?>> methodArgumentsTypeMetaList = executableTypeResolver
        .getParameterTypes(typeProvider, methodToResolve);
```

And as a result __TypeMeta__ items will have following structure:
  - Return type (_methodReturnTypeMeta_):
  ```javascript
  { type: void, wildcard: false, genericTypes: [] }
  ```
  - Argument types (_methodArgumentsTypeMetaList_):
  ```javascript
  [{ type: String, wildcard: false, genericTypes:[] }]
  ```

## License
This project is licensed under the MIT License, you can read the full text [here](LICENSE).
