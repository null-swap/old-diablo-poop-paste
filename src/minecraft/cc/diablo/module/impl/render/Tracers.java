package cc.diablo.module.impl.render;

import cc.diablo.event.impl.Render3DEvent;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Tracers extends Module {
    public Tracers(){
        super("Tracers","Draw the funny line to players", Keyboard.KEY_NONE,Category.Render);
    }
    @Subscribe
    public void onRender3D(Render3DEvent e){
        GL11.glPushMatrix();
        for (Entity ent :Minecraft.theWorld.loadedEntityList) {
            if(ent instanceof EntityPlayer) {
                RenderUtils.drawLineToPosition(new BlockPos(ent.getPosition().getX() - 1,ent.getPosition().getY() - 0.5,ent.getPosition().getZ() - 1), new Color(255, 255, 255).getRGB());
            }
        }
        GL11.glPopMatrix();
    }
}
