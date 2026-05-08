package xyz.carmine.raven.core.config;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlWriter;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;
import xyz.carmine.raven.core.exception.ConfigParsingException;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConfigLoader {
    private static final String FILE_EXTENSION = ".toml";

    @SuppressWarnings("unchecked")
    public <T> T load(@NotNull Class<T> clazz) throws ConfigParsingException {
        RavenConfig annotation = clazz.getAnnotation(RavenConfig.class);
        if (annotation == null) {
            throw new IllegalStateException("Interface must be annotated with RavenConfig");
        }

        File file = new File(annotation.filename() + FILE_EXTENSION);

        // Create default provider, so we can read the default parameters
        T defaultProvider = (T) java.lang.reflect.Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                (proxy, method, args) -> {
                    if (method.isDefault()) {
                        return InvocationHandler.invokeDefault(proxy, method, args);
                    }
                    return null;
                }
        );

        DynamicType.Builder<Object> builder = new ByteBuddy()
                .subclass(Object.class)
                .implement(clazz);

        boolean write = false;

        try (FileConfig config = FileConfig.of(file)) {
            config.load();

            for (Method method : clazz.getDeclaredMethods()) {
                // TODO Convert the key to snake case or smth
                String key = method.getName();

                if (!method.isDefault()) {
                    throw new ConfigParsingException("Method '" + key + "' must have default value");
                }

                // Parameter is missing in file
                if (!config.contains(key)) {
                    Object defaultValue = method.invoke(defaultProvider);

                    switch (defaultValue) {
                        case Pos pos -> {
                            config.add("dd." + key + ".x", pos.x());
                            config.add("dd." + key + ".y", pos.y());
                            config.add("dd." + key + ".z", pos.z());
                        }
                        case Double d -> config.add(key, d);
                        case Integer i -> config.add(key, i);
                        case String s -> config.add(key, s);
                        case null, default -> config.add(key, defaultValue);
                    }

                    write = true;
                }

                Class<?> returnType = method.getReturnType();

                Object value;
                if (returnType == int.class) {
                    value = config.getInt(key);
                } else if (returnType == String.class) {
                    value = config.get(key);
                } else if (returnType == boolean.class) {
                    value = config.get(key);
                } else if (returnType == Pos.class) {
                    double x = config.get("dd." + key + ".x");
                    double y = config.get("dd." + key + ".y");
                    double z = config.get("dd." + key + ".z");
                    // TODO Add pitch and yaw
                    value = new Pos(x, y, z);
                } else {
                    throw new IllegalArgumentException("Unsupported type: " + returnType);
                }

                builder = builder.method(ElementMatchers.named(key))
                        .intercept(FixedValue.value(value));
            }

            if (write) {
                // Save the config
                TomlWriter writer = new TomlWriter();
                writer.write(config, file, WritingMode.REPLACE_ATOMIC);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            return (T) builder
                    .make()
                    .load(clazz.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
    }
}
