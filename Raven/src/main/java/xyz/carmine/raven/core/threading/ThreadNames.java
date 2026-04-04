package xyz.carmine.raven.utils;

import org.jetbrains.annotations.NotNull;

public enum ThreadNames {
    DISCORD_CHAT_SUBSCRIBER("Discord-ChatSubscriber");

    private final String name;

    ThreadNames(@NotNull String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
