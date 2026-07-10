# Contributing to Executor

Thank you for your interest in contributing to the Executor project! This document provides guidelines and instructions for contributing.

## Getting Started

### Prerequisites
- Java 17 or higher
- Gradle (or use the included `./gradlew` wrapper)
- Git

### Building the Project

To build the project, use the Gradle wrapper:

```bash
./gradlew build
```

This command will:
- Compile the source code
- Run all unit tests
- Generate the build artifacts

### Running Tests

To run only the tests:

```bash
./gradlew test
```

To run a specific test class:

```bash
./gradlew test --tests com.navneet.executor.utils.FileUtilsTest
```

## Branch Naming Convention

Please follow these naming conventions for branches:

- **Feature branches**: `feature/description-of-feature`
  - Example: `feature/add-async-processing`
- **Bug fix branches**: `bugfix/description-of-bug`
  - Example: `bugfix/fix-null-pointer-exception`
- **Documentation branches**: `docs/description-of-docs`
  - Example: `docs/update-readme`
- **Refactoring branches**: `refactor/description-of-refactor`
  - Example: `refactor/simplify-upload-logic`

## Pull Request Process

1. **Create a feature branch** from `main` using the naming convention above
2. **Make your changes** and ensure they follow the project's code style
3. **Write or update tests** for any new functionality or bug fixes
4. **Run the full build** to ensure everything compiles and tests pass:
   ```bash
   ./gradlew build
   ```
5. **Commit your changes** with clear, descriptive commit messages
6. **Push your branch** to the repository
7. **Open a Pull Request** with:
   - A clear title describing the change
   - A detailed description of what was changed and why
   - Reference to any related issues (e.g., "Fixes #123")
   - Evidence that tests pass (build output or screenshots)

## Code Style Guidelines

- Follow standard Java conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public methods and classes
- Keep methods focused and reasonably sized
- Use Lombok annotations where appropriate (e.g., `@Log4j2`, `@Autowired`)

## Testing Requirements

- **All new public methods** must have unit tests
- **All new endpoints** must have integration tests
- **Bug fixes** should include a test that reproduces the bug and verifies the fix
- Aim for at least 80% code coverage for new code

### Test File Location

Test files should be placed in `src/test/java/` with the same package structure as the code being tested.

Example:
- Source: `src/main/java/com/navneet/executor/utils/FileUtils.java`
- Test: `src/test/java/com/navneet/executor/utils/FileUtilsTest.java`

## Reporting Issues

When reporting bugs or suggesting features:

1. Check if the issue already exists
2. Provide a clear description of the problem or suggestion
3. Include steps to reproduce (for bugs)
4. Provide relevant environment information (Java version, OS, etc.)

## License

By contributing to this project, you agree that your contributions will be licensed under the MIT License.

## Questions?

If you have any questions about contributing, feel free to open an issue or contact the project maintainers.

Thank you for contributing!
