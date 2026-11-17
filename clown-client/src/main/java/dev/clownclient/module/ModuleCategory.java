package dev.clownclient.module;

public enum ModuleCategory {
    MOVEMENT("Movement"), VISUALS("Visuals"), UTILITY("Utility"), CLIENT("Client");

    private final String displayName;
    ModuleCategory(String displayName) { this.displayName = displayName; }
    public String displayName() { return displayName; }
}
