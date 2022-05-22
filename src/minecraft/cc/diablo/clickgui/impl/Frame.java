package cc.diablo.clickgui.impl;

import cc.diablo.Main;
import cc.diablo.helpers.render.ColorHelper;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class Frame extends GuiScreen {
    public double x,y,width,height;
    public Category category;
    //@Override
    public Frame(double x, double y, double width, double height,Category category){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.category=category;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawRect((float) x,(float)y,(float)(x+width),(float)(y+height),new Color(17,17,17).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}