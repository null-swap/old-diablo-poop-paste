package cc.diablo.clickguinew.impl;

import cc.diablo.Main;
import cc.diablo.clickguinew.impl.settings.Bind;
import cc.diablo.clickguinew.impl.settings.Boolean;
import cc.diablo.clickguinew.impl.settings.Mode;
import cc.diablo.clickguinew.impl.settings.Slider;
import cc.diablo.font.TTFFontRenderer;
import cc.diablo.helpers.render.ColorHelper;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.Setting;
import cc.diablo.setting.impl.BooleanSetting;
import cc.diablo.setting.impl.ModeSetting;
import cc.diablo.setting.impl.NumberSetting;

import java.awt.*;
import java.util.ArrayList;

public class Button {
    public double X;
    public double Y;
    public double Width;
    public double Height;
    public double offset;
    public Module module;
    private Category cat;
    public Frame parent;

    TTFFontRenderer moduleTitle = Main.getInstance().getFontManager().getFont("clean 18");

    public ArrayList<Slider> sliderSettings = new ArrayList<Slider>();
    public ArrayList<Boolean> booleanSettings = new ArrayList<Boolean>();
    public ArrayList<Mode> modeSettings = new ArrayList<Mode>();
    public Bind bindSetting;

    public Button(double x, double y, double width, double height, Module m, Category c, Frame parent) {
        this.parent = parent;
        X = x;
        Y = y;
        Width = width;
        Height = height;
        module = m;
        cat = c;
        offset = 0;

        bindSetting = new Bind(x, y + offset, width, 27, this);
        for (Setting s : m.getSettingList()) {
            if (s instanceof ModeSetting) {
                modeSettings.add(new Mode(x, y + offset, width, 25, s, this));
                offset += 27;
            }

            if (s instanceof BooleanSetting) {
                booleanSettings.add(new Boolean(x, y + offset, width, height, s, this));
                offset += height;
            }
            if (s instanceof NumberSetting) {
                sliderSettings.add(new Slider(x, y + offset, width, height, s, this));
                offset += height;
            }
        }
    }

    public int drawButton(int mouseX, int mouseY, float partialTicks) {
        double offset = 0;
        int offsetY = 0;
        moduleTitle.drawString(module.getName(), (float) X + 3, (float) (Y + 2 + offset), module.toggled ? -1 : new Color(170, 170, 170).getRGB());
        if (module.extended) {
            double settingY = Y + Height;

            for (Mode m : modeSettings) {
                m.Y = settingY;
                settingY += m.Height;
                offsetY += m.drawMode(mouseX, mouseY);
            }
            for (Boolean b : booleanSettings) {
                b.Y = settingY;
                settingY += b.Height;
                offsetY += b.drawBoolean();
            }
            for (Slider s : sliderSettings) {
                s.Y = settingY;
                settingY += s.Height;
                offsetY += s.drawSlider(mouseX, mouseY, partialTicks);
            }

            //bindSetting.Y = settingY;
            //settingY += bindSetting.Height;
            //offsetY += bindSetting.drawBind();
        }
        return offsetY;
    }

    public void clickButton(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtils.isHovered(mouseX, mouseY, X, Y, X + Width, Y + Height)) {
            if (mouseButton == 0) {
                module.setToggled(!module.toggled);
            } else if (mouseButton == 1) {
                if (module.extended) {
                    module.extended = false;
                } else {
                    module.extended = true;
                }
            }
        }
        //bindSetting.clickBind(mouseX, mouseY, mouseButton);
        if(module.isExtended()) {
            for (Boolean b : booleanSettings) {
                b.clickBoolean(mouseX, mouseY, mouseButton);
            }
            for (Slider s : sliderSettings)
                s.clickSlider(mouseX, mouseY, mouseButton);
            for (Mode b : modeSettings) {
                b.clickMode(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void releaseMouse(int mouseX, int mouseY, int mouseButton) {
        for(Slider s : sliderSettings)
            s.releaseMouse(mouseX, mouseY, mouseButton);
    }

    public void keyTyped(char typedChar, int keyCode) {
        //bindSetting.onTyped(typedChar, keyCode);
    }
}
