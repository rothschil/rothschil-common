![rothschil-common](https://socialify.git.ci/AlatarWong/rothschil-common/image?description=1&descriptionEditable=The%20darkness%20will%20not%20last%20forever%2C%20but%20you%20will%20forever%20in%20my%20heart&forks=1&issues=1&language=1&name=1&owner=1&pattern=Signal&pulls=1&stargazers=1&theme=Light)


## 1. Project Summary

`rothschil-common` is a general-purpose utility library designed to provide a wide range of commonly used utility classes and functional modules for Java developers. The project is created and maintained by AlatarWong, aiming to simplify the development process, improve code quality, and enhance development efficiency.

## 2. Main Features

### 2.1. Utility Classes

- **String Handling**: Provides a rich set of string manipulation methods, such as concatenation, substring extraction, formatting, etc.
- **Date and Time Handling**: Offers date and time conversion, formatting, and calculation functionalities.
- **File Operations**: Supports file read/write, copy, delete, and other operations.
- **Collection Operations**: Provides common collection manipulation methods, such as filtering, sorting, merging, etc.
- **Encryption and Decryption**: Supports various encryption algorithms, including MD5, SHA, AES, etc.
- **Network Requests**: Provides HTTP request encapsulation to simplify network request coding.

### 2.2. Configuration Management

- **Property File Reading**: Supports reading configuration information from property files and provides convenient access methods.
- **Environment Variable Management**: Supports reading configuration information from environment variables, making it easy to switch configurations in different environments.

### 2.3. Logging

- **Logging Framework Integration**: Integrates common logging frameworks like Log4j, SLF4J, and provides a unified logging interface.
- **Log Level Management**: Supports dynamic adjustment of log levels for easier debugging and production use.

### 2.4. Exception Handling

- **Exception Capture**: Provides a global exception capture mechanism to handle uncaught exceptions uniformly.
- **Exception Information Logging**: Logs exception information for easier problem diagnosis and analysis.

### 2.5. Other Features

- **Cache Management**: Supports various caching strategies, such as in-memory caching, file caching, etc.
- **Database Operations**: Provides simple database operation methods, such as CRUD (Create, Read, Update, Delete).
- **Thread Pool Management**: Provides methods for creating and managing thread pools to optimize multi-threaded task execution.

## 3. Usage

### 3.1. Add Dependency

Add the following dependency in your `pom.xml` file:

~~~xml

<dependency>
    <groupId>com.rothschil</groupId>
    <artifactId>rothschil-common</artifactId>
    <version>1.0.0</version>
</dependency>

~~~

### 3.2. Import Utility Classes

Import the necessary utility classes in your code:

~~~java

import com.rothschil.common.util.StringUtil;
import com.rothschil.common.util.DateUtil;
import com.rothschil.common.util.FileUtil;

~~~

### 3.3. Usage Example

~~~java

public class Example {
    public static void main(String[] args) {
        // String Handling
        String result = StringUtil.join("-", "Hello", "World");
        System.out.println(result); // Output: Hello-World

        // Date and Time Handling
        String formattedDate = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(formattedDate); // Output: Current date and time

        // File Operations
        FileUtil.copyFile("source.txt", "destination.txt");
    }
}

~~~

## 4. Project Directory Structure

The `rothschil-common` project is organized into a well-structured directory hierarchy to ensure clarity and maintainability. Below is a detailed description of the project’s directory structure:

~~~

rothschil-common/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.rothschil.common/
│   │   │       ├── util/
│   │   │       │   ├── StringUtil.java
│   │   │       │   ├── DateUtil.java
│   │   │       │   ├── FileUtil.java
│   │   │       │   ├── CollectionUtil.java
│   │   │       │   ├── EncryptionUtil.java
│   │   │       │   ├── NetworkUtil.java
│   │   │       │   └── ...
│   │   │       ├── config/
│   │   │       │   ├── PropertyReader.java
│   │   │       │   ├── EnvironmentVariableManager.java
│   │   │       │   └── ...
│   │   │       ├── logging/
│   │   │       │   ├── LoggerFactory.java
│   │   │       │   ├── LogLevelManager.java
│   │   │       │   └── ...
│   │   │       ├── exception/
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   ├── ExceptionLogger.java
│   │   │       │   └── ...
│   │   │       ├── cache/
│   │   │       │   ├── CacheManager.java
│   │   │       │   ├── InMemoryCache.java
│   │   │       │   ├── FileCache.java
│   │   │       │   └── ...
│   │   │       ├── db/
│   │   │       │   ├── DatabaseOperations.java
│   │   │       │   └── ...
│   │   │       ├── thread/
│   │   │       │   ├── ThreadPoolManager.java
│   │   │       │   └── ...
│   │   │       └── ...
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── log4j.properties
│   │       └── ...
│   └── test/
│       ├── java/
│       │   └── com.rothschil.common/
│       │       ├── util/
│       │       │   ├── StringUtilTest.java
│       │       │   ├── DateUtilTest.java
│       │       │   ├── FileUtilTest.java
│       │       │   ├── CollectionUtilTest.java
│       │       │   ├── EncryptionUtilTest.java
│       │       │   ├── NetworkUtilTest.java
│       │       │   └── ...
│       │       ├── config/
│       │       │   ├── PropertyReaderTest.java
│       │       │   ├── EnvironmentVariableManagerTest.java
│       │       │   └── ...
│       │       ├── logging/
│       │       │   ├── LoggerFactoryTest.java
│       │       │   ├── LogLevelManagerTest.java
│       │       │   └── ...
│       │       ├── exception/
│       │       │   ├── GlobalExceptionHandlerTest.java
│       │       │   ├── ExceptionLoggerTest.java
│       │       │   └── ...
│       │       ├── cache/
│       │       │   ├── CacheManagerTest.java
│       │       │   ├── InMemoryCacheTest.java
│       │       │   ├── FileCacheTest.java
│       │       │   └── ...
│       │       ├── db/
│       │       │   ├── DatabaseOperationsTest.java
│       │       │   └── ...
│       │       ├── thread/
│       │       │   ├── ThreadPoolManagerTest.java
│       │       │   └── ...
│       │       └── ...
│       └── resources/
│           ├── test-application.properties
│           ├── test-log4j.properties
│           └── ...
└── pom.xml


~~~

## 5. Directory Description

- src/main/java/com.rothschil.common/: Contains the main source code for the project.

  - util/: Utility classes for common operations like string manipulation, date handling, file operations, etc.
  - config/: Classes for configuration management, including property file reading and environment variable management.
  - logging/: Classes for logging, including integration with logging frameworks and log level management.
  - exception/: Classes for exception handling, including global exception capture and logging.
  - cache/: Classes for cache management, including in-memory and file caching.
  - db/: Classes for database operations, including CRUD methods.
  - thread/: Classes for thread pool management.

- src/main/resources/: Contains configuration files and other resources.

  - application.properties: Main configuration file for the project.
  - log4j.properties: Configuration file for the Log4j logging framework.

- src/test/java/com.rothschil.common/: Contains unit tests for the project
  - util/: Unit tests for utility classes.
  - config/: Unit tests for configuration management classes.
  - logging/: Unit tests for logging classes.
  - exception/: Unit tests for exception handling classes.
  - cache/: Unit tests for cache management classes.
  - db/: Unit tests for database operation classes.
  - thread/: Unit tests for thread pool management classes.
- src/test/resources/: Contains test-specific configuration files and resources.
  - test-application.properties: Test configuration file.
  - test-log4j.properties: Test configuration file for the Log4j logging framework.
- pom.xml: Maven project object model (POM) file, which contains project configuration and dependency management information.

## 6. Contribution Guidelines

We welcome contributions and suggestions for improvement. Please follow these steps:

- Fork this repository.
- Create a new branch: git checkout -b feature/new-feature.
- Commit your changes: git commit -m 'Add new feature'.
- Push your branch: git push origin feature/new-feature.
- Open a pull request

## 7. Caffeine、Redis实现双重缓存方式

### 7.1. 解决问题

单纯使用 `Redis` 缓存，会有大量业务的请求到 `Redis` ，但每次请求来回都同样的数据，假如将此类数据存在应用服务器本身，那么理论上，将节省请求 `Redis` 的网络开销，响应速度就会提升.
而如果只使用应用服务器那优先的内存实现 `Caffeine` 来做本地缓存，在运营后期单独为缓存去扩展应用服务器，成本太高。

![20250102143725](https://abram.oss-cn-shanghai.aliyuncs.com/abram/vscode/20250102143725.png)

- 先从一级缓存（ `Caffeine`-本地应用内）中查找数据；
- 数据不存在，则从二级缓存（ `Redis` -内存）中查找数据；
- 数据仍然不存在，再从数据库（数据库-磁盘）中查找数据。