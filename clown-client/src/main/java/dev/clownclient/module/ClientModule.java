package dev.clownclient.module;

import net.minecraft.client.Minecraft;

public abstract class ClientModule {
    private final String name;
    private final String description;
    private final ModuleCategory category;
    private boolean enabled;

    protected ClientModule(String name, String description, ModuleCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public final String name() { return name; }
    public final String description() { return description; }
    public final ModuleCategory category() { return category; }
    public final boolean enabled() { return enabled; }

    public final void setEnabled(boolean value, Minecraft client) {
        if (enabled == value) return;
        enabled = value;
        try {
            if (value) onEnable(client); else onDisable(client);
        } catch (RuntimeException exception) {
            enabled = false;
            try { onDisable(client); } catch (RuntimeException ignored) { }
            throw exception;
        }
    }

    public final void toggle(Minecraft client) { setEnabled(!enabled, client); }
    public void tick(Minecraft client) { }
    protected void onEnable(Minecraft client) { }
    protected void onDisable(Minecraft client) { }
}
