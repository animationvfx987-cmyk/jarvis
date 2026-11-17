package dev.clownclient.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.clownclient.module.ClientModule;
import dev.clownclient.module.ModuleRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
    private static final Path CONFIG = FabricLoader.getInstance().getConfigDir().resolve("clownclient.json");
    private ConfigManager() { }

    public static void load(Minecraft client) {
        if (!Files.exists(CONFIG)) return;
        try {
            JsonObject root = JsonParser.parseString(Files.readString(CONFIG, StandardCharsets.UTF_8)).getAsJsonObject();
            JsonObject states = root.has("modules") && root.get("modules").isJsonObject()
                    ? root.getAsJsonObject("modules") : new JsonObject();
            for (ClientModule module : ModuleRegistry.all()) {
                if (states.has(module.name()) && states.get(module.name()).isJsonPrimitive()
                        && states.get(module.name()).getAsJsonPrimitive().isBoolean()) {
                    module.setEnabled(states.get(module.name()).getAsBoolean(), client);
                }
            }
        } catch (Exception exception) {
            System.err.println("[CLOWN CLIENT] Config could not be loaded; defaults retained: " + exception.getMessage());
        }
    }

    public static void save() {
        JsonObject root = new JsonObject();
        JsonObject states = new JsonObject();
        for (ClientModule module : ModuleRegistry.all()) states.addProperty(module.name(), module.enabled());
        root.add("modules", states);
        try {
            Files.createDirectories(CONFIG.getParent());
            Files.writeString(CONFIG, root.toString(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.err.println("[CLOWN CLIENT] Config could not be saved: " + exception.getMessage());
        }
    }
}
