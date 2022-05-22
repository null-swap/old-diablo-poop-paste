package cc.diablo.module.impl.render;

import cc.diablo.Main;
import cc.diablo.event.impl.OverlayEvent;
import cc.diablo.event.impl.Render3DEvent;
import cc.diablo.font.TTFFontRenderer;
import cc.diablo.font.TTFRenderer;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.helpers.render.ColorHelper;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.manager.friend.FriendManager;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerList extends Module {
    public ArrayList<Entity> entityArrayList = new ArrayList<Entity>();
    public ModeSetting mode = new ModeSetting("Playerlist","Nearby","Server","Nearby");

    public PlayerList(){
        super("PlayerList","List all players in your game", Keyboard.KEY_NONE, Category.Render);
        this.addSettings(mode);
    }

    @Subscribe
    public void onRender(OverlayEvent e){
        int theY = 33;
        int x = 3;
        int y = 17;
        int width = mc.fontRendererObj.getStringWidth("aaaaaaaaaaaaa") + 40;
        //TTFRenderer fr = new TTFRenderer("Comfortaa", 0, 18);
        NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
        List<NetworkPlayerInfo> list = GuiPlayerTabOverlay.field_175252_a.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
        switch (mode.getMode()) {
            case "Server":
                RenderUtils.drawRect(x - 1,y - 3,x + width + 1 ,y+(12*list.size()) + 17, RenderUtils.transparency(new Color(37,37,37).getRGB(),0.6f)); //Blur
                RenderUtils.drawRect(x,y,x + width,y+(12*list.size()) + 16, RenderUtils.transparency(new Color(45, 45, 45, 255).getRGB(),0.8f)); //Main background
                RenderUtils.drawRect(x,y,x + width,y+13, RenderUtils.transparency(new Color(35, 35, 35, 255).getRGB(),0.8f));
                RenderUtils.drawRect(x ,y - 2,x + width,y,RenderUtils.transparency(ColorHelper.getColor(0),0.8f)); //Color!
                mc.fontRendererObj.drawStringWithShadow("Players",x + 2,y + 3,-1);

                for (NetworkPlayerInfo networkplayerinfo : list) {
                    mc.fontRendererObj.drawStringWithShadow(networkplayerinfo.getGameProfile().getName(), 18, theY + 1, new Color(200, 200, 200).getRGB());
                    RenderUtils.drawHead((AbstractClientPlayer) Minecraft.theWorld.getPlayerEntityByUUID(nethandlerplayclient.getGameProfile().getId()), 5, theY, 10, 10);
                    //fr.drawString(GuiPlayerTabOverlay.getPlayerName(networkplayerinfo),10,theY,new Color(255,255,255).getRGB());
                    mc.fontRendererObj.drawStringWithShadow(String.valueOf(list.size()),x + width - 2 - mc.fontRendererObj.getStringWidth(String.valueOf(list.size())),y + 3,-1);
                    theY += 12;
                }
            case "Nearby":
                int count = 0;

                for (Entity ent : Minecraft.theWorld.loadedEntityList) {
                    if(ent instanceof EntityPlayer) {
                        if(!ent.isInvisible()) {
                            count += 1;
                        }
                    }
                }

                RenderUtils.drawRect(x - 1,y - 3,x + width + 1 ,y+(12*count) + 17, RenderUtils.transparency(new Color(37,37,37).getRGB(),0.6f)); //Blur
                RenderUtils.drawRect(x,y,x + width,y+(12*count) + 16, RenderUtils.transparency(new Color(45, 45, 45, 255).getRGB(),0.8f)); //Main background
                RenderUtils.drawRect(x,y,x + width,y+13, RenderUtils.transparency(new Color(35, 35, 35, 255).getRGB(),0.8f));
                RenderUtils.drawRect(x ,y - 2,x + width,y,RenderUtils.transparency(ColorHelper.getColor(0),0.8f)); //Color!
                mc.fontRendererObj.drawStringWithShadow("Players",x + 2,y + 3,-1);

                for (Entity ent : Minecraft.theWorld.loadedEntityList) {
                    if(ent instanceof EntityPlayer) {
                        if(!ent.isInvisible()) {
                            mc.fontRendererObj.drawStringWithShadow(ent.getName(), 18, theY + 1, new Color(200, 200, 200).getRGB());
                            RenderUtils.drawHead((AbstractClientPlayer) ent, 5, theY, 10, 10);
                            theY += 12;
                        }
                    }
                }

                mc.fontRendererObj.drawStringWithShadow(String.valueOf(count),x + width - 2 - mc.fontRendererObj.getStringWidth(String.valueOf(count)),y + 3,-1);
        }
    }
}
