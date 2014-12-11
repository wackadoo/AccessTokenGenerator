AccessTokenGenerator
====================

Creates an access token from an identifier and scope.
In order to build your own jar you need to install maven first and then execute mvn package inside the root folder.
The jar will be placed inside the folder /target.

Use the Generator
---------------

Run the demo from the terminal with default values:

```
java -jar AccessTokenGenerator.jar
```
or
```
java -jar AccessTokenGenerator.jar identifer scope1 scope2
```

You have to run the demo with at least one identifier and one scope argument!

When you want to use the generator on your server call

  generateAccessToken(String identifier, String[] scope)

like this

```java
String identifier = "identifier";
String[] scope = {"scope1", "scope2"}
generateAccessToken(indetifier, scope)
```