package cc.diablo.clickgui;

import cc.diablo.clickgui.impl.Frame;
import cc.diablo.module.Category;
import net.minecraft.client.gui.GuiScreen;

public class ClickGUIDropdown extends GuiScreen {
    double x, y, x2, y2, width, height;

    @Override
    public void initGui() {
        x = sr.getScaledWidth_double() / 7;
        y = sr.getScaledHeight_double() / 7;
        width = 200;
        height = 20;
        x2 = x;
        y2 = y;
        for(Category e : Category.values()){
            new Frame(x,y,width,height,e);
            x += sr.getScaledWidth_double() / 7;
            y += y = sr.getScaledHeight_double() / 7;
        }
        super.initGui();
    }
}