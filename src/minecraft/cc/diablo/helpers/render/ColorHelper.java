package cc.diablo.helpers.render;

import cc.diablo.module.impl.render.HUD;
import net.minecraft.util.MathHelper;

import java.awt.*;

public class ColorHelper {
    public static int getAstolfo(int delay, float offset, float hueSetting) {
        float speed = (float) HUD.delay.getVal();
        float hue = (float) (System.currentTimeMillis() % delay) + (offset);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += hueSetting;
        return Color.HSBtoRGB(hue, 0.5F, 1F);
    }

    public static int getColor(int offset){
            //return getColorWave(new Color(45, 24, 158),100).getRGB();
        switch (HUD.colorMode.getMode()) {
            case "Astolfo":
                return ColorHelper.getAstolfo(10000000, offset, 0.5f);
            case "Wave":
                return ColorHelper.getColorWave(new Color((int) HUD.red.getVal(),(int)HUD.green.getVal(),(int) HUD.blue.getVal()),offset).getRGB();
            case "Solid":
                return new Color((int) HUD.red.getVal(),(int)HUD.green.getVal(),(int) HUD.blue.getVal()).getRGB();
            case "Rainbow":
                return RenderUtils.getRainbow(6000, offset, 1f);
            case "Rainbow Light":
                return RenderUtils.getRainbow(6000, offset, 0.55f);
        }
        return ColorHelper.getAstolfo(10000000, offset, 0.5f);
    }

    public static int getColor(Color color) {
        return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getColor(int brightness, int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }

    public static int getColor(int red, int green, int blue) {
        return getColor(red, green, blue, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = MathHelper.clamp_int(alpha, 0, 255) << 24;
        color |= MathHelper.clamp_int(red, 0, 255) << 16;
        color |= MathHelper.clamp_int(green, 0, 255) << 8;
        color |= MathHelper.clamp_int(blue, 0, 255);
        return color;
    }
    public static Color getAstolfoColor(int delay, float offset) {
        float speed = (float) HUD.delay.getVal();
        float hue = (float) (System.currentTimeMillis() % delay) + (offset);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.getHSBColor(hue, 0.5F, 1F);
    }

    public static Color getColorWave(Color color, float offset) {
        float speed = (float) HUD.delay.getVal();
        float hue = (float) (System.currentTimeMillis() % 10000000) + (offset);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        float[] colors = Color.RGBtoHSB(color.getRed(),
                color.getGreen(),
                color.getBlue(), null);
        return Color.getHSBColor(colors[0], 1f, hue);
    }
}
