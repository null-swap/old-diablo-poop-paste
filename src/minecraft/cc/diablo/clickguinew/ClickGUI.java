package cc.diablo.clickguinew;

import cc.diablo.Main;
import cc.diablo.clickguinew.impl.Frame;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.module.Category;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ClickGUI extends GuiScreen {
    public static double startX, startY, offset;
    public static boolean hasRegistered = false;
    public ArrayList<Frame> frames = new ArrayList<>();
    public boolean isBinding = false;

    public ClickGUI() {
        startX = 10;
        startY = 10;

        double offset = 0;
        for (Category c : Category.values()) {
            double x = startX + offset;
            double y = startY;
            double width = 105;
            frames.add(new Frame(x, y, width, 100, 18, c,this));
            offset += width + 10;
        }

        hasRegistered = true;
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        try{

            MovementInput.moveForward = 1;
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) mc.thePlayer.rotationPitch += 2f;
            if (Keyboard.isKeyDown(Keyboard.KEY_UP)) mc.thePlayer.rotationPitch -= 2f;
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) mc.thePlayer.rotationYaw += 2f;
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) mc.thePlayer.rotationYaw -= 2f;
            KeyBinding[] keys = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight,mc.gameSettings.keyBindJump};
            Arrays.stream(keys).forEach(key -> KeyBinding.setKeyBindState(key.getKeyCode(), Keyboard.isKeyDown(key.getKeyCode())));
        } catch (Exception ignored){}

        ScaledResolution scaledResolution = new ScaledResolution(mc);

        //RenderUtils.drawRect(0,0,scaledResolution.getScaledWidth(),scaledResolution.getScaledHeight(), RenderUtils.transparency(new Color(255,255,255).getRGB(), 0.45));

        for (Frame c : frames) {
            c.drawFrame(mouseX, mouseY, partialTicks);
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException{
        if(!isBinding){
            super.keyTyped(typedChar,keyCode);
        }
        for (Frame c : frames) {
            c.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (Frame c : frames) {
            c.clickFrame(mouseX,mouseY,mouseButton);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Frame c : frames) {
            c.releaseMouse(mouseX,mouseY,mouseButton);
        }

        super.mouseReleased(mouseX, mouseY, mouseButton);
    }
}
