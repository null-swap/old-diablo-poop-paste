package cc.diablo.clickguinew.impl.settings;

import cc.diablo.Main;
import cc.diablo.clickguinew.impl.Button;
import cc.diablo.font.TTFFontRenderer;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.setting.Setting;
import cc.diablo.setting.impl.BooleanSetting;

import java.awt.*;

public class Boolean {
    public double X;
    public double Y;
    public double Width;
    public double Height;
    public BooleanSetting s;
    public Button parent;

    TTFFontRenderer fr = Main.getInstance().getFontManager().getFont("clean 16");

    public Boolean(double x, double y, double width, double height, Setting s, Button parent) {
        X = x;
        Y = y + height;
        Width = width;
        Height = height;
        this.s = (BooleanSetting) s;
        this.parent = parent;
    }

    public int drawBoolean() {
        try {
            fr.drawStringWithShadow(s.name, X + 4, Y + (Height / 2) - (fr.getHeight("s") / 2), s.checked ? -1 : new Color(150, 150, 150).getRGB());
            RenderUtils.drawRoundedRectangle(X + Width - 4 - 16, Y + 4, X + Width - 4.5, Y + Height - 4, 4, s.isChecked() ? 0xff73ff8c : new Color(41, 41, 41).getRGB());
        } catch (Exception e) {
            ((BooleanSetting) s).setChecked(s.getDefault());
        }
        return (int) Height;
    }

    public void clickBoolean(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtils.isHovered(mouseX,mouseY,X + Width - 4 - 16, Y + 4, X + Width - 4.5, Y + Height - 4)){
            if(mouseButton == 0) {
                s.toggle();
            }
        }
    }
}
