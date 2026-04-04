package xyz.carmine.raven.world.instance.template;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class InstanceTemplateRegistry {
    private static final Map<String, InstanceTemplate> registryMap = new HashMap<>();

    public static void register(@NotNull InstanceTemplate template) {
        registryMap.put(template.templateId(), template);
    }

    public static void register(@NotNull InstanceTemplate... templates) {
        for (InstanceTemplate template : templates) {
            register(template);
        }
    }

    public static @NotNull InstanceTemplate get(@NotNull String id) {
        InstanceTemplate template = registryMap.get(id);
        if (template == null) {
            throw new NoSuchElementException(id);
        }

        return template;
    }

    public static @NotNull Collection<InstanceTemplate> getTemplates() {
        return registryMap.values();
    }
}
