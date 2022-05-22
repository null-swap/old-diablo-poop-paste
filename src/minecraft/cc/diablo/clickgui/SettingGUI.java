package cc.diablo.clickgui;

import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.module.Module;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;
import java.io.IOException;

public class SettingGUI extends ClickGUI {

    public Module module;
    double x, y, width, height;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawRoundedRectangle(x - 160, y, width + 160, height + 210, 15, new Color(44, 47, 51, 255).getRGB());
        RenderUtils.drawRoundedRectangle(x - 160, y, width + 160, height + 20, 15, new Color(35, 39, 42, 255).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void initGui() {
        x = sr.getScaledWidth_double() * 1.5;
        y = sr.getScaledHeight_double();
        width = x;
        height = y;
        super.initGui();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

}