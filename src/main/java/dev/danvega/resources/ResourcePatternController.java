package dev.danvega.resources;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Example 3: Loading Multiple Resources with ResourcePatternResolver
 *
 * When to use ResourcePatternResolver instead of ResourceLoader?
 * ---------------------------------------------------------------
 * Use ResourceLoader:          Load ONE resource by exact path
 * Use ResourcePatternResolver: Load MULTIPLE resources matching a pattern
 *
 * Pattern Syntax:
 * - classpath:sql/*.sql        → All .sql files in the sql folder
 * - classpath:config/**\/*.xml  → All .xml files in config and subdirectories
 * - classpath*:META-INF/*.xml  → All .xml files from ALL JARs on the classpath
 *
 * Common use cases:
 * - Loading all database migration scripts
 * - Processing all configuration files in a directory
 * - Scanning for plugin descriptors across multiple JARs
 */
@RestController
@RequestMapping("/sql")
public class ResourcePatternController {

    private final ResourcePatternResolver resourcePatternResolver;

    public ResourcePatternController(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    /**
     * GET /sql
     * Load ALL SQL files matching the pattern "classpath:sql/*.sql"
     *
     * This demonstrates loading multiple resources at once - something
     * you can't do with ResourceLoader or @Value.
     */
    @GetMapping
    public String getAllSqlFiles() throws IOException {
        Resource[] resources = resourcePatternResolver.getResources("classpath:sql/*.sql");

        // Sort by filename to ensure consistent ordering
        Arrays.sort(resources, (a, b) -> {
            String nameA = a.getFilename() != null ? a.getFilename() : "";
            String nameB = b.getFilename() != null ? b.getFilename() : "";
            return nameA.compareTo(nameB);
        });

        StringBuilder result = new StringBuilder();
        result.append("Found ").append(resources.length).append(" SQL files:\n\n");

        for (Resource resource : resources) {
            result.append("=== ").append(resource.getFilename()).append(" ===\n");
            result.append(new String(resource.getContentAsByteArray(), StandardCharsets.UTF_8));
            result.append("\n");
        }

        return result.toString();
    }

}
