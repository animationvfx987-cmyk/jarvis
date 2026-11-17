package dev.clownclient.module;

import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ModuleRegistry {
    private static final List<ClientModule> MODULES = new ArrayList<>();
    private ModuleRegistry() { }

    public static void registerDefaults() {
        if (!MODULES.isEmpty()) return;
        MODULES.add(new ClientModule("Toggle Sprint", "Keeps vanilla sprint input held while enabled.", ModuleCategory.MOVEMENT) {
            @Override public void tick(Minecraft client) {
                if (client.player != null && client.options != null) {
                    client.options.keySprint.setDown(enabled() && !client.player.isShiftKeyDown());
                }
            }
            @Override protected void onDisable(Minecraft client) {
                if (client.options != null) client.options.keySprint.setDown(false);
            }
        });
        MODULES.add(new ClientModule("Toggle Sneak", "Keeps vanilla sneak input held while enabled.", ModuleCategory.MOVEMENT) {
            @Override public void tick(Minecraft client) {
                if (client.options != null) client.options.keyShift.setDown(enabled());
            }
            @Override protected void onDisable(Minecraft client) {
                if (client.options != null) client.options.keyShift.setDown(false);
            }
        });
        MODULES.add(new ClientModule("Fullbright", "Raises local gamma for visibility; does not reveal hidden blocks or entities.", ModuleCategory.VISUALS) {
            private Double previous;
            @Override protected void onEnable(Minecraft client) {
                if (client.options != null) {
                    previous = client.options.gamma().get();
                    client.options.gamma().set(16.0);
                }
            }
            @Override public void tick(Minecraft client) {
                if (enabled() && client.options != null) client.options.gamma().set(16.0);
            }
            @Override protected void onDisable(Minecraft client) {
                if (client.options != null && previous != null) client.options.gamma().set(previous);
                previous = null;
            }
        });
        MODULES.add(new ClientModule("Zoom", "Sets a reduced local field of view while enabled.", ModuleCategory.VISUALS) {
            private Integer previous;
            @Override protected void onEnable(Minecraft client) {
                if (client.options != null) {
                    previous = client.options.fov().get();
                    client.options.fov().set(Math.min(previous, 30));
                }
            }
            @Override protected void onDisable(Minecraft client) {
                if (client.options != null && previous != null) client.options.fov().set(previous);
                previous = null;
            }
        });
    }

    public static List<ClientModule> all() { return Collections.unmodifiableList(MODULES); }
    public static ClientModule find(String name) {
        return MODULES.stream().filter(module -> module.name().equals(name)).findFirst().orElse(null);
    }
    public static void tick(Minecraft client) {
        for (ClientModule module : MODULES) {
            try { module.tick(client); }
            catch (RuntimeException exception) {
                module.setEnabled(false, client);
                System.err.println("[CLOWN CLIENT] Disabled module after error: " + module.name());
                exception.printStackTrace();
            }
        }
    }
}
