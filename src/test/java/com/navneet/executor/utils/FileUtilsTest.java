package com.navneet.executor.utils;

import com.navneet.executor.models.CustomerInfo;
import com.univocity.parsers.common.IterableResult;
import com.univocity.parsers.common.ParsingContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FileUtils class
 * Tests CSV reading and line counting functionality
 */
@SpringBootTest
class FileUtilsTest {

    private FileUtils fileUtils;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileUtils = new FileUtils();
    }

    /**
     * Happy path test: Read valid CSV file with customer data
     */
    @Test
    void testReadDataFromCsvWithValidFile() throws IOException {
        // Arrange
        File csvFile = createValidCsvFile();

        // Act
        IterableResult<CustomerInfo, ParsingContext> result = fileUtils.readDataFromCsv(
                csvFile.getAbsolutePath(),
                CustomerInfo.class
        );

        // Assert
        assertNotNull(result, "Result should not be null");
        int count = 0;
        for (CustomerInfo customer : result) {
            assertNotNull(customer, "Customer object should not be null");
            assertNotNull(customer.getId(), "Customer ID should not be null");
            assertNotNull(customer.getName(), "Customer name should not be null");
            count++;
        }
        assertEquals(3, count, "Should have read 3 customer records");
    }

    /**
     * Happy path test: Count lines in a valid file
     */
    @Test
    void testNumberOfLinesWithValidFile() throws IOException {
        // Arrange
        File csvFile = createValidCsvFile();

        // Act
        int lineCount = fileUtils.numberOfLines(csvFile.getAbsolutePath());

        // Assert
        assertEquals(4, lineCount, "Should count 4 lines (1 header + 3 data rows)");
    }

    /**
     * Happy path test: Count lines in a single-line file
     */
    @Test
    void testNumberOfLinesWithSingleLineFile() throws IOException {
        // Arrange
        File singleLineFile = tempDir.resolve("single_line.txt").toFile();
        try (FileWriter writer = new FileWriter(singleLineFile)) {
            writer.write("This is a single line");
        }

        // Act
        int lineCount = fileUtils.numberOfLines(singleLineFile.getAbsolutePath());

        // Assert
        assertEquals(1, lineCount, "Should count 1 line");
    }

    /**
     * Error case test: Read from non-existent file
     */
    @Test
    void testReadDataFromCsvWithNonExistentFile() {
        // Arrange
        String nonExistentPath = "/non/existent/path/file.csv";

        // Act
        IterableResult<CustomerInfo, ParsingContext> result = fileUtils.readDataFromCsv(
                nonExistentPath,
                CustomerInfo.class
        );

        // Assert
        assertNull(result, "Result should be null for non-existent file");
    }

    /**
     * Error case test: Count lines in non-existent file
     */
    @Test
    void testNumberOfLinesWithNonExistentFile() {
        // Arrange
        String nonExistentPath = "/non/existent/path/file.txt";

        // Act & Assert
        assertThrows(
                java.io.FileNotFoundException.class,
                () -> fileUtils.numberOfLines(nonExistentPath),
                "Should throw FileNotFoundException for non-existent file"
        );
    }

    /**
     * Edge case test: Empty CSV file
     */
    @Test
    void testNumberOfLinesWithEmptyFile() throws IOException {
        // Arrange
        File emptyFile = tempDir.resolve("empty.csv").toFile();
        emptyFile.createNewFile();

        // Act
        int lineCount = fileUtils.numberOfLines(emptyFile.getAbsolutePath());

        // Assert
        assertEquals(0, lineCount, "Should count 0 lines for empty file");
    }

    /**
     * Edge case test: File with multiple consecutive newlines
     */
    @Test
    void testNumberOfLinesWithMultipleNewlines() throws IOException {
        // Arrange
        File multiNewlineFile = tempDir.resolve("multi_newline.txt").toFile();
        try (FileWriter writer = new FileWriter(multiNewlineFile)) {
            writer.write("Line 1\n\n\nLine 2\n");
        }

        // Act
        int lineCount = fileUtils.numberOfLines(multiNewlineFile.getAbsolutePath());

        // Assert
        assertEquals(4, lineCount, "Should count 4 newline characters");
    }

    /**
     * Happy path test: Read CSV with special characters
     */
    @Test
    void testReadDataFromCsvWithSpecialCharacters() throws IOException {
        // Arrange
        File csvFile = createCsvFileWithSpecialCharacters();

        // Act
        IterableResult<CustomerInfo, ParsingContext> result = fileUtils.readDataFromCsv(
                csvFile.getAbsolutePath(),
                CustomerInfo.class
        );

        // Assert
        assertNotNull(result, "Result should not be null");
        int count = 0;
        for (CustomerInfo customer : result) {
            assertNotNull(customer, "Customer object should not be null");
            count++;
        }
        assertTrue(count > 0, "Should have read at least one record");
    }

    /**
     * Helper method to create a valid CSV file with test data
     */
    private File createValidCsvFile() throws IOException {
        File csvFile = tempDir.resolve("customers.csv").toFile();
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("id,name,mobile,email,address,city,state,pincode\n");
            writer.write("1,John Doe,9876543210,john@example.com,123 Main St,New York,NY,10001\n");
            writer.write("2,Jane Smith,9876543211,jane@example.com,456 Oak Ave,Los Angeles,CA,90001\n");
            writer.write("3,Bob Johnson,9876543212,bob@example.com,789 Pine Rd,Chicago,IL,60601\n");
        }
        return csvFile;
    }

    /**
     * Helper method to create a CSV file with special characters
     */
    private File createCsvFileWithSpecialCharacters() throws IOException {
        File csvFile = tempDir.resolve("customers_special.csv").toFile();
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("id,name,mobile,email,address,city,state,pincode\n");
            writer.write("1,José García,9876543210,jose@example.com,Calle Principal 123,Madrid,MA,28001\n");
            writer.write("2,François Müller,9876543211,francois@example.com,Rue de la Paix 456,Paris,PA,75001\n");
        }
        return csvFile;
    }
}
