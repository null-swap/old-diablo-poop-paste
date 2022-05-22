package cc.diablo.clickguinew.impl.settings;

import cc.diablo.Main;
import cc.diablo.clickguinew.impl.Button;
import cc.diablo.font.TTFFontRenderer;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.setting.Setting;
import cc.diablo.setting.impl.ModeSetting;

import java.awt.*;

public class Mode {
    public double X;
    public double Y;
    public double Width;
    public double Height;
    private Setting s;
    public Button parent;

    TTFFontRenderer fr = Main.getInstance().getFontManager().getFont("clean 16");

    public Mode(double x, double y, double width, double height, Setting s, Button parent) {
        X = x;
        Y = y + height;
        Width = width;
        Height = height;
        this.s = s;
        this.parent = parent;
        parent.offset += height;

    }

    public int drawMode(int mouseX, int mouseY) {
        Height = 25;
        try {
            RenderUtils.drawRect(X + 2, Y + 2, X + Width - 2, Y + 12, RenderUtils.transparency(new Color(29, 29, 29, 255).getRGB(), 0.3f));
            RenderUtils.drawRect(X + 2, Y + 12, X + Width - 2, Y + 25, RenderUtils.transparency(new Color(37, 37, 37).getRGB(), 0.3f));
            fr.drawStringWithShadow(s.name, X + 4, Y + 3, -1);
            fr.drawStringWithShadow(((ModeSetting) s).getMode(), X + 5.5, Y + 14, RenderUtils.isHovered(mouseX, mouseY, X, Y, X + Width, Y + Height) ? -1 : new Color(144, 144, 144).getRGB());
        } catch (Exception e) {
            ((ModeSetting) s).setMode(((ModeSetting) s).defaultMode);
        }
        return (int) Height;
    }

    public void clickMode(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtils.isHovered(mouseX, mouseY, X + 2, Y + 12, X + Width - 2, Y + 25)) {
            if (mouseButton == 0) {
                int newMode = ((ModeSetting) s).settings.indexOf(((ModeSetting) s).getMode()) + 1;

                if(newMode > ((ModeSetting) s).settings.size() - 1) {
                    ((ModeSetting) s).setMode(((ModeSetting) s).settings.get(0));
                } else {
                    ((ModeSetting) s).setMode(((ModeSetting) s).settings.get(newMode));
                }

            }else if(mouseButton == 1){
                int newMode = ((ModeSetting) s).settings.indexOf(((ModeSetting) s).getMode()) -1;

                if(newMode >= 0){
                    ((ModeSetting) s).setMode(((ModeSetting) s).settings.get(newMode));
                }else{
                    ((ModeSetting) s).setMode(((ModeSetting) s).settings.get(((ModeSetting) s).settings.size() - 1));
                }
            }
        }
    }
}
