package dev.clownclient.gui;

import dev.clownclient.config.ConfigManager;
import dev.clownclient.module.ClientModule;
import dev.clownclient.module.ModuleCategory;
import dev.clownclient.module.ModuleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ClickGuiScreen extends Screen {
    private static final int RED = 0xFFE12B43;
    private static final int PANEL = 0xF018181D;
    private ModuleCategory selected = ModuleCategory.MOVEMENT;
    private EditBox search;
    private String appliedSearch = "";

    public ClickGuiScreen() {
        super(Component.literal("CLOWN CLIENT"));
    }

    private int panelWidth() {
        return Math.max(1, Math.min(560, width - 24));
    }

    private int panelLeft() {
        return Math.max(0, (width - panelWidth()) / 2);
    }

    private int panelTop() {
        return Math.max(0, (height - 360) / 2);
    }

    @Override
    protected void init() {
        int left = panelLeft();
        int top = panelTop();
        int panelWidth = panelWidth();
        int contentWidth = Math.max(1, panelWidth - 198);
        int searchWidth = Math.max(1, panelWidth - 310);

        search = new EditBox(font, left + 178, top + 48, searchWidth, 20,
                Component.literal("Search modules"));
        search.setHint(Component.literal("Search modules..."));
        search.setValue(appliedSearch);
        addRenderableWidget(search);

        int actionX = Math.max(left + 1, left + panelWidth - 104);
        addRenderableWidget(Button.builder(Component.literal("APPLY"), button -> {
            appliedSearch = search.getValue().trim().toLowerCase(Locale.ROOT);
            rebuildWidgets();
        }).bounds(actionX, top + 48, Math.max(1, Math.min(94, panelWidth - 2)), 20).build());

        int categoryY = top + 110;
        for (ModuleCategory category : ModuleCategory.values()) {
            ModuleCategory captured = category;
            addRenderableWidget(Button.builder(Component.literal(category.displayName()), button -> {
                selected = captured;
                rebuildWidgets();
            }).bounds(left + 12, categoryY, Math.max(1, Math.min(150, panelWidth - 24)), 22).build());
            categoryY += 28;
        }

        List<ClientModule> visible = new ArrayList<>();
        for (ClientModule module : ModuleRegistry.all()) {
            if (module.category() == selected && (appliedSearch.isBlank()
                    || module.name().toLowerCase(Locale.ROOT).contains(appliedSearch)
                    || module.description().toLowerCase(Locale.ROOT).contains(appliedSearch))) {
                visible.add(module);
            }
        }

        int moduleY = top + 110;
        int maxRows = Math.max(0, Math.min(5, (height - moduleY - 52) / 30));
        for (int i = 0; i < Math.min(visible.size(), maxRows); i++) {
            ClientModule module = visible.get(i);
            addRenderableWidget(Button.builder(
                    Component.literal((module.enabled() ? "[ON]  " : "[OFF]  ") + module.name()),
                    button -> {
                        module.toggle(Minecraft.getInstance());
                        ConfigManager.save();
                        rebuildWidgets();
                    }).bounds(left + 178, moduleY, contentWidth, 24).build());
            moduleY += 30;
        }

        addRenderableWidget(Button.builder(Component.literal("SAVE CONFIG"), button -> ConfigManager.save())
                .bounds(left + 12, top + 300, Math.max(1, Math.min(150, panelWidth - 24)), 22).build());
        addRenderableWidget(Button.builder(Component.literal("CLOSE"), button -> onClose())
                .bounds(Math.max(left + 1, left + panelWidth - 104), top + 300,
                        Math.max(1, Math.min(94, panelWidth - 2)), 22).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics, mouseX, mouseY, delta);
        int panelWidth = panelWidth();
        int left = panelLeft();
        int top = panelTop();
        graphics.fill(left, top, left + panelWidth, top + Math.min(340, Math.max(1, height - top)), PANEL);
        graphics.fill(left, top, left + panelWidth, top + 3, RED);
        graphics.drawString(font, "CLOWN CLIENT", left + 14, top + 14, 0xFFFFFFFF, false);
        graphics.drawString(font, "1.21.11 / FABRIC / CLIENT UTILITIES",
                left + 14, top + 29, 0xFFAAAAB4, false);
        if (panelWidth > 174) {
            graphics.fill(left + 170, top + 42, left + 172, top + 286, 0xFF48404A);
        }
        graphics.drawString(font, "CATEGORIES", left + 14, top + 92, RED, false);
        graphics.drawString(font, selected.displayName().toUpperCase(Locale.ROOT),
                Math.min(left + 180, left + panelWidth - 4), top + 92, RED, false);
        graphics.drawString(font, "Fair-play features only - local settings",
                left + 14, Math.min(top + 322, height - 10), 0xFFAAAAB4, false);
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        ConfigManager.save();
        Minecraft.getInstance().setScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
