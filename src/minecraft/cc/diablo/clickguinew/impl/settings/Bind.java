package cc.diablo.clickguinew.impl.settings;

import cc.diablo.Main;
import cc.diablo.clickguinew.impl.Button;
import cc.diablo.font.TTFFontRenderer;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.setting.Setting;
import cc.diablo.setting.impl.BooleanSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class Bind {
    public double X;
    public double Y;
    public double Width;
    public double Height;
    public Button parent;

    TTFFontRenderer fr = Main.getInstance().getFontManager().getFont("clean 16");
    private boolean waitingForNextKey;

    public Bind(double x, double y, double width, double height, Button parent) {
        X = x;
        Y = y + height;
        Width = width;
        Height = height;
        this.parent = parent;
    }

    public int drawBind() {
        Height = 10;
        RenderUtils.drawRoundedRectangle(X + 2,Y+1,X + Width - 2, Y+Height-1, 2, 0xFF292929);
        fr.drawStringWithShadow(waitingForNextKey ? "Binding...": "Bind: " + Keyboard.getKeyName(parent.module.getKey()), X + 4, Y + (Height / 2) - (fr.getHeight("s") / 2),  -1);
        return (int)Height;
    }

    public void clickBind(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtils.isHovered(mouseX,mouseY,X + 2, Y+1, X + Width - 2, Y + Height -1)){
            if(mouseButton == 0) {
               // if(!parent.parent.parent.isBinding) {
                    waitingForNextKey = true;
                    parent.parent.parent.isBinding = true; //Please ignore parent.parent.parent LMAO
               // }
            }
        }else {
            parent.parent.parent.isBinding = false;
            waitingForNextKey = false;
        }
    }
    public void onTyped(char typedChar, int keyCode) {
        if(waitingForNextKey){
            if(keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_BACK){
                parent.module.setKey(0);
            }else{
                parent.module.setKey(keyCode);
            }
            parent.parent.parent.isBinding = false;
            waitingForNextKey = false;
        }
    }

}
