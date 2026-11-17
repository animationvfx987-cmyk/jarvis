package dev.clownclient.gui;

import dev.clownclient.config.ConfigManager;
import dev.clownclient.module.ClientModule;
import dev.clownclient.module.ModuleCategory;
import dev.clownclient.module.ModuleRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ClickGuiScreen extends Screen {
    private static final int RED = 0xFFE12B43;
    private static final int PANEL = 0xF018181D;
    private ModuleCategory selected = ModuleCategory.MOVEMENT;
    private EditBox search;
    private String appliedSearch = "";

    public ClickGuiScreen() { super(Component.literal("CLOWN CLIENT")); }

    @Override protected void init() {
        int left = Math.max(18, (width - 560) / 2);
        int top = Math.max(18, (height - 360) / 2);
        int panelWidth = Math.min(560, width - 36);
        search = new EditBox(font, left + 190, top + 48, panelWidth - 210, 20, Component.literal("Search modules"));
        search.setHint(Component.literal("Search modules..."));
        search.setValue(appliedSearch);
        addRenderableWidget(search);
        addRenderableWidget(Button.builder(Component.literal("APPLY SEARCH"), button -> {
            appliedSearch = search.getValue().trim().toLowerCase(Locale.ROOT);
            refreshUi();
        }).bounds(left + panelWidth - 108, top + 76, 98, 20).build());

        int categoryY = top + 110;
        for (ModuleCategory category : ModuleCategory.values()) {
            ModuleCategory captured = category;
            addRenderableWidget(Button.builder(Component.literal(category.displayName()), button -> {
                selected = captured;
                refreshUi();
            }).bounds(left + 12, categoryY, 150, 22).build());
            categoryY += 28;
        }

        List<ClientModule> visible = new ArrayList<>();
        for (ClientModule module : ModuleRegistry.all()) {
            if (module.category() == selected && (appliedSearch.isBlank()
                    || module.name().toLowerCase(Locale.ROOT).contains(appliedSearch)
                    || module.description().toLowerCase(Locale.ROOT).contains(appliedSearch))) visible.add(module);
        }
        int moduleY = top + 110;
        for (ClientModule module : visible) {
            addRenderableWidget(Button.builder(Component.literal((module.enabled() ? "[ON]  " : "[OFF]  ") + module.name()), button -> {
                module.toggle(Minecraft.getInstance());
                ConfigManager.save();
                refreshUi();
            }).bounds(left + 178, moduleY, panelWidth - 198, 24).build());
            moduleY += 32;
        }
        addRenderableWidget(Button.builder(Component.literal("SAVE CONFIG"), button -> ConfigManager.save())
                .bounds(left + 12, top + 300, 150, 22).build());
        addRenderableWidget(Button.builder(Component.literal("CLOSE"), button -> onClose())
                .bounds(left + panelWidth - 108, top + 300, 98, 22).build());
    }

    private void refreshUi() {
        clearWidgets();
        init();
    }

    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics, mouseX, mouseY, delta);
        int panelWidth = Math.min(560, width - 36);
        int left = Math.max(18, (width - 560) / 2);
        int top = Math.max(18, (height - 360) / 2);
        graphics.fill(left, top, left + panelWidth, top + 340, PANEL);
        graphics.fill(left, top, left + panelWidth, top + 3, RED);
        graphics.drawString(font, "CLOWN CLIENT", left + 14, top + 14, 0xFFFFFFFF, false);
        graphics.drawString(font, "1.21.11  /  FABRIC  /  CLIENT UTILITIES", left + 14, top + 29, 0xFFAAAAB4, false);
        graphics.fill(left + 170, top + 42, left + 172, top + 286, 0xFF48404A);
        graphics.drawString(font, "CATEGORIES", left + 14, top + 92, RED, false);
        graphics.drawString(font, selected.displayName().toUpperCase(Locale.ROOT), left + 180, top + 92, RED, false);
        graphics.drawString(font, "Fair-play features only · local settings", left + 14, top + 322, 0xFFAAAAB4, false);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override public void onClose() {
        ConfigManager.save();
        Minecraft.getInstance().setScreen(null);
    }

    @Override public boolean isPauseScreen() { return false; }
}
