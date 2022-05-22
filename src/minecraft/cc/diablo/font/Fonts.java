package cc.diablo.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class Fonts {
    public static final MCFontRenderer tabGuiIconFont = new MCFontRenderer(fontFromTTF(new ResourceLocation("Client/diablo/font/Icon-Font.ttf"),22,0), true, true);
    public static final MCFontRenderer clickGuiIconFont = new MCFontRenderer(fontFromTTF(new ResourceLocation("Client/diablo/font/skeetfont.ttf"),36,0), true, true);
    public static final MCFontRenderer diablotest = new MCFontRenderer(fontFromTTF(new ResourceLocation("Client/diablo/font/Montserrat-Medium.ttf"),14,0), true, true);
    public static final MCFontRenderer diablotest2 = new MCFontRenderer(fontFromTTF(new ResourceLocation("Client/diablo/font/verdana.ttf"),20,0), true, true);
    private static Font fontFromTTF(ResourceLocation fontLocation, float fontSize, int fontType) {
        Font output = null;
        try {
            output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}