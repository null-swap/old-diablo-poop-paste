package cc.diablo.clickguinew.impl;

import cc.diablo.Main;
import cc.diablo.clickguinew.ClickGUI;
import cc.diablo.font.TTFFontRenderer;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.helpers.render.ColorHelper;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.Category;
import cc.diablo.module.Module;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Frame {
    public double x, y, x2, y2, offsetX, offsetY, topBarY, transparencyVal, buttonOffset, booleanHeight, keybindHeight, sliderHeight, modeHeight;
    public Category cat;
    public int topBar, topBarBoarder, buttonFrameColor, settingsFrameColor;
    private boolean dragging;
    public ArrayList<Module> modules = new ArrayList<Module>();
    public ArrayList<Button> buttons = new ArrayList<Button>();
    public ClickGUI parent;

    TTFFontRenderer moduleTitle = Main.getInstance().getFontManager().getFont("clean 20");
    private boolean moving;

    public float getFontWidth(String text) {
        TTFFontRenderer fr = Main.getInstance().getFontManager().getFont("clean 18");
        return fr.getWidth(text);
    }

    public Frame(double xInp, double yInp, double width, double height, double topBarHeight, Category c, ClickGUI parent) {
        booleanHeight = 14;
        keybindHeight = 14;
        sliderHeight = 14;
        modeHeight = 14;
        buttonOffset = 0;
        this.parent = parent;
        topBarY = yInp;
        y = yInp + topBarHeight;
        x = xInp;
        transparencyVal = 0.9;
        cat = c;
        for (Module m : ModuleManager.getModules()) {
            if (m.getCategory() == c) {
                modules.add(m);
            }
        }
        modules.sort(Comparator.comparing(module -> moduleTitle.getWidth(((Module) module).getName())).reversed());
        for (Module m : modules) {
            buttons.add(new Button(x, y + buttonOffset, width, 14, m, c, this));
            buttonOffset += 14;
        }

        x2 = x + width;
        y2 = y + buttonOffset;

        topBar = RenderUtils.transparency(ColorHelper.getColor(0), transparencyVal);
        topBarBoarder = RenderUtils.transparency(new Color(27, 27, 27).getRGB(), transparencyVal);
        buttonFrameColor = RenderUtils.transparency(new Color(17, 17, 17).getRGB(), 0.75);
        settingsFrameColor = RenderUtils.transparency(new Color(55, 55, 55).getRGB(), 0.1);
    }

    public void drawFrame(int mouseX, int mouseY, float partialTicks) {
        if (moving) {
            // x = mouseX;
            //y = mouseY;
        }
        double buttonY = this.y;
        for (Button b : buttons) {
            b.Y = buttonY;
            buttonY += b.Height;
            buttonY += b.drawButton(mouseX, mouseY, partialTicks);
        }
        RenderUtils.drawRect(x - 1.5, topBarY, x2 + 1.5, y - 1, topBarBoarder);
        RenderUtils.drawRect(x - 1.5, y - 1, x2 + 1.5, y, topBar);
        RenderUtils.drawRect(x, y, x2, buttonY, buttonFrameColor);
        moduleTitle.drawStringWithShadow(cat.name(), x + 3, topBarY + 4, -1);

        for (Button b : buttons) {
            b.drawButton(mouseX, mouseY, partialTicks);
        }

    }

    public void clickFrame(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtils.isHovered((int) mouseX, (int) mouseY, x, topBarY, x2, y)) {
            ChatHelper.addChat("Clicked " + cat.name());
            moving = true;
        }
        for (Button button : buttons) {
            button.clickButton(mouseX, mouseY, mouseButton);
        }

    }

    public void releaseMouse(int mouseX, int mouseY, int mouseButton) {
        moving = false;
        for(Button b: buttons){
            b.releaseMouse(mouseX, mouseY, mouseButton);
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        for (Button button : buttons) {
            button.keyTyped(typedChar, keyCode);
        }
    }
}
