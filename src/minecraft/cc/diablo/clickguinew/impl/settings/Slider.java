package cc.diablo.clickguinew.impl.settings;

import cc.diablo.Main;
import cc.diablo.clickguinew.impl.Button;
import cc.diablo.font.TTFFontRenderer;
import cc.diablo.helpers.render.ColorHelper;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.setting.Setting;
import cc.diablo.setting.impl.BooleanSetting;
import cc.diablo.setting.impl.NumberSetting;

import java.awt.*;
import java.text.DecimalFormat;

public class Slider {
    public double X;
    public double Y;
    public double Width;
    public double Height;
    public NumberSetting s;
    public Button parent;
    public boolean sliding;

    TTFFontRenderer fr = Main.getInstance().getFontManager().getFont("clean 16");

    public Slider(double x, double y, double width, double height, Setting s, Button parent) {
        X = x;
        Y = y + height;
        Width = width;
        Height = height;
        this.s = (NumberSetting) s;
        this.parent = parent;
    }

    public int drawSlider(int mouseX, int mouseY, float partialTicks) {
        try {
            //Meth
            double getValue = (mouseX - (X + 2)) * 1.04166666667;
            if (sliding) {
                if (!RenderUtils.isHovered(mouseX, mouseY, X + 2, Y + 1, X + Width - 2, Y + Height - 1)) {
                    sliding = false;
                }
                s.setValue(((s.getMax() - s.getMin()) / 100) * getValue);
            }
            RenderUtils.drawRoundedRectangle(X + 2, Y + 1, X + Width - 2, Y + Height - 1, 2, 0xFF555555);
            RenderUtils.drawRoundedRectangle(X + 2, Y + 1, X + 2 + ((s.getVal()) / (s.getMax() - s.getMin()) * 96), Y + Height - 1, 2, ColorHelper.getColor((int) Y * 5));
            fr.drawStringWithShadow(s.name, X + 4, Y + (Height / 2) - (fr.getHeight("s") / 2), -1);
            String finalValue = new DecimalFormat("#.##").format(s.getValue());
            fr.drawStringWithShadow(finalValue, X + Width - fr.getWidth(finalValue) - 4, Y + (Height / 2) - (fr.getHeight("s") / 2), -1);
        } catch (Exception e){
            ((NumberSetting) s).setValue(s.getDef());
        }
        return (int)Height;
    }

    public void clickSlider(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtils.isHovered(mouseX,mouseY,X + 2, Y + 1, X + Width - 2, Y + Height - 1)){
            if(mouseButton == 0) {
                sliding = true;
            }
        }
    }

    public void releaseMouse(int mouseX, int mouseY, int mouseButton) {
        sliding = false;
    }
}
