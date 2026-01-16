package dev.danvega.resources;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Example 1: Loading Resources with @Value and Resource
 * <p>
 * Spring's Resource interface provides a powerful abstraction for accessing low-level resources.
 * When combined with @Value, Spring automatically creates the appropriate Resource implementation
 * based on the prefix you use in the resource path.
 *
 */
@RestController
public class ResourceController {

    public static final String FILENAME = "Filename: ";
    public static final String EXISTS = "Exists: ";
    public static final String READABLE = "Readable: ";
    public static final String DESCRIPTION = "Description: ";
    public static final String CLASS = "Class: ";
    // ==================== CLASSPATH RESOURCE ====================
    // Use "classpath:" to load files from src/main/resources
    // Spring creates a ClassPathResource instance
    // This is the most common way to load application resources
    @Value("classpath:myFile.txt")
    private Resource classpathResource;

    // ==================== URL RESOURCE ====================
    // Use "https://" (or "http://") to load resources from the web
    // Spring creates a UrlResource instance
    // Useful for loading remote configuration, data files, or documentation
    @Value("${resource.url}")
    private Resource urlResource;

    // ==================== FILE SYSTEM RESOURCE ====================
    // Use "file:" with "./" for paths relative to the working directory
    // Spring creates a FileUrlResource instance
    // The working directory is typically where you run the application from
    // Note: For absolute paths, use "file:/absolute/path/to/file.txt"
    @Value("file:./data/config.txt")
    private Resource fileResource;

    /**
     * GET /
     * Demonstrates loading a text file from the classpath.
     * The file myFile.txt is located in src/main/resources/
     */
    @GetMapping
    public String getClasspathResource() throws IOException {
        return new String(classpathResource.getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    /**
     * GET /classpath
     * Demonstrates using ClassPathResource directly instead of @Value injection.
     * <p>
     * You can instantiate Resource implementations directly when:
     * - You need to create resources programmatically (e.g., in a loop or based on logic)
     * - You're in a non-Spring-managed class (no @Value available)
     * - You prefer explicit control over the resource type
     * <p>
     * Available implementations: ClassPathResource, FileSystemResource, UrlResource, etc.
     */
    @GetMapping("/classpath")
    public String getClasspathResource2() throws IOException {
        Resource resource = new ClassPathResource("myFile.txt");
        return new String(resource.getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    /**
     * GET /readme
     * Demonstrates loading a resource from a remote URL.
     * Fetches the README.md from a GitHub repository.
     */
    @GetMapping("/readme")
    public String getUrlResource() throws IOException {
        return new String(urlResource.getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    /**
     * GET /file
     * Demonstrates loading a file from the filesystem.
     * The file is loaded relative to the application's working directory.
     */
    @GetMapping("/file")
    public String getFileResource() throws IOException {
        return new String(fileResource.getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    /**
     * GET /info
     * Demonstrates the metadata methods available on the Resource interface.
     * Shows filename, existence check, readability, and description.
     */
    @GetMapping("/info")
    public String getResourceInfo() {
        StringBuilder info = new StringBuilder();

        info.append("=== Classpath Resource (myFile.txt) ===\n");
        info.append(FILENAME).append(classpathResource.getFilename()).append("\n");
        info.append(EXISTS).append(classpathResource.exists()).append("\n");
        info.append(READABLE).append(classpathResource.isReadable()).append("\n");
        info.append(DESCRIPTION).append(classpathResource.getDescription()).append("\n");
        info.append(CLASS).append(classpathResource.getClass().getSimpleName()).append("\n\n");

        info.append("=== File Resource (data/config.txt) ===\n");
        info.append(FILENAME).append(fileResource.getFilename()).append("\n");
        info.append(EXISTS).append(fileResource.exists()).append("\n");
        info.append(READABLE).append(fileResource.isReadable()).append("\n");
        info.append(DESCRIPTION).append(fileResource.getDescription()).append("\n");
        info.append(CLASS).append(fileResource.getClass().getSimpleName()).append("\n\n");

        info.append("=== URL Resource (GitHub README) ===\n");
        info.append(FILENAME).append(urlResource.getFilename()).append("\n");
        info.append(EXISTS).append(urlResource.exists()).append("\n");
        info.append(READABLE).append(urlResource.isReadable()).append("\n");
        info.append(DESCRIPTION).append(urlResource.getDescription()).append("\n");
        info.append(CLASS).append(urlResource.getClass().getSimpleName()).append("\n");

        return info.toString();
    }

}
