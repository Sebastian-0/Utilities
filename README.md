[![Release](https://jitpack.io/v/Sebastian-0/Utilities.svg)](https://jitpack.io/#Sebastian-0/Utilities)

# Utilities
This repository contains some general-purpose utilites that I use in my projects. The following are some of the utilities included:
 * A logger class called Debugger which is handy to report errors
 * An object pool that can be used to reuse object instances
 * A zip-file extractor that makes it easy to extract the contents of a zip-file
 
## Adding to your build
To add a dependency on my utilities using Gradle, use the following:
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Sebastian-0:Utilities:1.0.1'
}
```

Or using Maven:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.Sebastian-0</groupId>
    <artifactId>Utilities</artifactId>
    <version>1.0.1</version>
</dependency>
```



## License
This utility library is free to use as long as you comply to the GNU LGPL v3 license or later (see LICENSE for details). For clarification, you may compile this library into a jar archive and include it as a dependency in any project of your own, commercial or non-commercial, as long as credit is given to me. Furthermore, I reserve the exclusive right to re-license this library, either for use in specific projects or for public use. 

## Documentation
All the classes in the package have full javadoc documentation, except for the QuickHull class.
