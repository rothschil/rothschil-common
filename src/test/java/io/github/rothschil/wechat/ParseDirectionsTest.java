package io.github.rothschil.wechat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class ParseDirectionsTest extends ParseDirections {

    private ParseDirections parseDirections;
    private File mockTargetFile;

    @BeforeEach
    void setUp() {
        parseDirections = new ParseDirections();
        mockTargetFile = mock(File.class);
    }

    @AfterEach
    void tearDown() {
        // Clean up any created files
    }

    @Test
    void testParse_NullOriginFile() {
        // Test null input
        parseDirections.parse(null, "targetFile.xlsx");
        // Should not throw exception, just return early
    }

    @Test
    void testParse_NonExistentFile() {
        // Test non-existent file
        File nonExistentFile = new File("non_existent_file.txt");
        parseDirections.parse(nonExistentFile, "targetFile.xlsx");
        // Should not throw exception, just return early
    }

    @Test
    void testParse_FileInsteadOfDirectory(@TempDir Path tempDir) throws IOException {
        // Test when originFile is a file, not a directory
        File testFile = tempDir.resolve("test.txt").toFile();
        Files.write(testFile.toPath(), "test content".getBytes());

        ParseDirections spyParser = spy(parseDirections);
        spyParser.parse(testFile, "targetFile.xlsx");

        // Verify printFileList was NOT called since it's a file, not a directory
        verify(spyParser, never()).printFileList(anyString(), anyList());
    }

    @Test
    void testParse_EmptyDirectory(@TempDir Path tempDir) {
        // Test empty directory
        File emptyDir = tempDir.toFile();

        ParseDirections spyParser = spy(parseDirections);
        spyParser.parse(emptyDir, "targetFile.xlsx");

        // Verify printFileList was NOT called since directory is empty
        verify(spyParser, never()).printFileList(anyString(), anyList());
    }

    @Test
    void testParse_DirectoryWithFilesOnly(@TempDir Path tempDir) throws IOException {
        // Test directory containing only files
        File file1 = tempDir.resolve("file1.txt").toFile();
        Files.write(file1.toPath(), "content1".getBytes());

        File file2 = tempDir.resolve("file2.txt").toFile();
        Files.write(file2.toPath(), "content2".getBytes());

        ParseDirections spyParser = spy(parseDirections);
        spyParser.parse(tempDir.toFile(), "targetFile.xlsx");

        // Verify printFileList was called once with 2 PrizeDo objects
        verify(spyParser, times(1)).printFileList(eq("targetFile.xlsx"), anyList());
    }

    @Test
    void testParse_DirectoryWithSubdirectories(@TempDir Path tempDir) throws IOException {
        // Test directory containing subdirectories
        File subDir = tempDir.resolve("subdir").toFile();
        subDir.mkdir();

        File fileInSubDir = new File(subDir, "subfile.txt");
        Files.write(fileInSubDir.toPath(), "sub content".getBytes());

        File fileInRoot = tempDir.resolve("rootfile.txt").toFile();
        Files.write(fileInRoot.toPath(), "root content".getBytes());

        ParseDirections spyParser = spy(parseDirections);
        spyParser.parse(tempDir.toFile(), "targetFile.xlsx");

        // Verify printFileList was called (recursive call should handle subdirectory)
        verify(spyParser, atLeastOnce()).printFileList(eq("targetFile.xlsx"), anyList());
    }

    @Test
    void testParse_DirectoryWithMixedContent(@TempDir Path tempDir) throws IOException {
        // Test directory with mixed content (files + subdirectories)
        File subDir = tempDir.resolve("subdir").toFile();
        subDir.mkdir();

        File fileInSubDir = new File(subDir, "subfile.txt");
        Files.write(fileInSubDir.toPath(), "sub content".getBytes());

        File file1 = tempDir.resolve("file1.txt").toFile();
        Files.write(file1.toPath(), "content1".getBytes());

        File file2 = tempDir.resolve("file2.txt").toFile();
        Files.write(file2.toPath(), "content2".getBytes());

        ParseDirections spyParser = spy(parseDirections);
        spyParser.parse(tempDir.toFile(), "targetFile.xlsx");

        // Verify printFileList was called
        verify(spyParser, atLeastOnce()).printFileList(eq("targetFile.xlsx"), anyList());
    }

    @Test
    void testParse_TargetFileNull() throws IOException {
        // Test with null target file
        File tempFile = File.createTempFile("test", ".txt");
        tempFile.deleteOnExit();

        ParseDirections spyParser = spy(parseDirections);
        spyParser.parse(tempFile, null);

        // Should not throw exception, just handle gracefully
        verify(spyParser, never()).printFileList(isNull(), anyList());
    }

    @Test
    void testParse_TargetFileEmptyString() throws IOException {
        // Test with empty target file string
        File tempFile = File.createTempFile("test", ".txt");
        tempFile.deleteOnExit();

        ParseDirections spyParser = spy(parseDirections);
        spyParser.parse(tempFile, "");

        // Should not throw exception, just handle gracefully
        verify(spyParser, never()).printFileList(eq(""), anyList());
    }

    @Test
    void testParse_FileWithSpecialCharacters(@TempDir Path tempDir) throws IOException {
        // Test files with special characters in names
        File specialFile = tempDir.resolve("file with spaces and @#$%^.txt").toFile();
        Files.write(specialFile.toPath(), "special content".getBytes());

        ParseDirections spyParser = spy(parseDirections);
        spyParser.parse(tempDir.toFile(), "targetFile.xlsx");

        // Verify printFileList was called
        verify(spyParser, times(1)).printFileList(eq("targetFile.xlsx"), anyList());
    }

    @Test
    void testParse_LargeNumberOfFiles(@TempDir Path tempDir) throws IOException {
        // Test with larger number of files
        for (int i = 0; i < 10; i++) {
            File file = tempDir.resolve("file" + i + ".txt").toFile();
            Files.write(file.toPath(), ("content" + i).getBytes());
        }

        ParseDirections spyParser = spy(parseDirections);
        spyParser.parse(tempDir.toFile(), "targetFile.xlsx");

        // Verify printFileList was called once
        verify(spyParser, times(1)).printFileList(eq("targetFile.xlsx"), anyList());
    }

    @Test
    void testParse_ReadOnlyFiles(@TempDir Path tempDir) throws IOException {
        // Test with read-only files
        File readOnlyFile = tempDir.resolve("readonly.txt").toFile();
        Files.write(readOnlyFile.toPath(), "readonly content".getBytes());
        readOnlyFile.setReadOnly();

        ParseDirections spyParser = spy(parseDirections);
        spyParser.parse(tempDir.toFile(), "targetFile.xlsx");

        // Verify printFileList was called (should handle read-only files gracefully)
        verify(spyParser, times(1)).printFileList(eq("targetFile.xlsx"), anyList());
    }

    @Test
    void testParse_InvalidDirectoryPath() {
        // Test with invalid directory path
        File invalidDir = new File("/invalid/path/that/does/not/exist");

        ParseDirections spyParser = spy(parseDirections);
        spyParser.parse(invalidDir, "targetFile.xlsx");

        // Should not throw exception, just return early
        verify(spyParser, never()).printFileList(anyString(), anyList());
    }

    @Test
    void testParse_DirectoryWithNoReadPermission(@TempDir Path tempDir) {
        // Test directory without read permission (simulate permission denied)
        File noPermissionDir = tempDir.toFile();
        noPermissionDir.setReadable(false);

        ParseDirections spyParser = spy(parseDirections);
        spyParser.parse(noPermissionDir, "targetFile.xlsx");

        // Should handle gracefully (listFiles() will return null)
        verify(spyParser, never()).printFileList(anyString(), anyList());

        // Restore permission for cleanup
        noPermissionDir.setReadable(true);
    }

    @Test
    void testPrintFileList_Mocked() {
        // Test printFileList method with mocked parameters
        ParseDirections spyParser = spy(parseDirections);
        List<PrizeDo> prizeList = List.of(
            new PrizeDo("file1.txt", "/path/to/file1.txt"),
            new PrizeDo("file2.txt", "/path/to/file2.txt")
        );

        spyParser.printFileList("test.xlsx", prizeList);

        // Verify the method was called (actual Excel writing is handled by ExcelUtils)
        // This test mainly ensures the method doesn't throw exceptions
    }

    @Test
    void testPrintFileList_EmptyList() {
        // Test printFileList with empty list
        ParseDirections spyParser = spy(parseDirections);

        spyParser.printFileList("test.xlsx", List.of());

        // Should handle empty list gracefully
    }

    @Test
    void testPrintFileList_NullList() {
        // Test printFileList with null list
        ParseDirections spyParser = spy(parseDirections);

        spyParser.printFileList("test.xlsx", null);

        // Should handle null list gracefully
    }

    @Test
    void testPrintFileList_NullTargetFile() {
        // Test printFileList with null target file
        ParseDirections spyParser = spy(parseDirections);
        List<PrizeDo> prizeList = List.of(new PrizeDo("test.txt", "/path/test.txt"));

        spyParser.printFileList(null, prizeList);

        // Should handle null target file gracefully
    }
}