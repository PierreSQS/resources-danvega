package dev.danvega.resources;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Example 2: Loading Resources with ResourceLoader
 *
 * When to use ResourceLoader instead of @Value?
 * ---------------------------------------------
 * Use @Value when:      The resource path is known at COMPILE TIME
 * Use ResourceLoader:   The resource path is determined at RUNTIME
 *
 * ResourceLoader is ideal when:
 * - The path depends on user input or request parameters
 * - You need to load one of many possible files dynamically
 * - The resource location is computed or configured at runtime
 *
 * Example: Loading different JSON files based on the request
 * With @Value, you'd need a separate field for EVERY possible file.
 * With ResourceLoader, you build the path dynamically.
 */
@RestController
@RequestMapping("/json")
public class ResourceLoaderController {

    private final ResourceLoader resourceLoader;

    public ResourceLoaderController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * GET /json/{name}
     * Load a JSON file dynamically based on the name parameter.
     *
     * This demonstrates the key advantage of ResourceLoader:
     * the path "classpath:json/{name}.json" is built at RUNTIME
     * based on the request, not hardcoded at compile time.
     *
     * Try: /json/users or /json/products
     */
    @GetMapping("/{name}")
    public String getJsonFile(@PathVariable String name) throws IOException {
        // Build the resource path dynamically - this is why we use ResourceLoader!
        String path = "classpath:json/" + name + ".json";
        Resource resource = resourceLoader.getResource(path);

        if (!resource.exists()) {
            return "Resource not found: " + name + ".json";
        }

        return new String(resource.getContentAsByteArray(), StandardCharsets.UTF_8);
    }

}
