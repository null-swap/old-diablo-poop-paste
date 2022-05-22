package cc.diablo.module.impl.render;

import cc.diablo.event.impl.Render3DEvent;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityCrit2FX;
import net.minecraft.client.particle.EntityCritFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.particles.Particle;

import java.util.ArrayList;
import java.util.List;

public class DamageParticles extends Module {
    private final List<Particle> particles = new ArrayList<Particle>();

    public DamageParticles(){
        super("DamageParticles","Sets the hit particle to the health reduced from the player", Keyboard.KEY_NONE, Category.Render);
    }

    @Subscribe
    public void onRender(Render3DEvent e){
        for (Entity entity : Minecraft.theWorld.getLoadedEntityList()) {

            if(entity instanceof EntityCrit2FX) {

                EntityCritFX p = (EntityCritFX) entity;
                double x = p.getPosition().getX();
                double n = x - mc.getRenderManager().viewerPosX;
                double y = p.getPosition().getY();
                double n2 = y - mc.getRenderManager().viewerPosY;
                double z = p.getPosition().getZ();
                double n3 = z - mc.getRenderManager().viewerPosZ;
                GlStateManager.pushMatrix();
                GlStateManager.enablePolygonOffset();
                GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
                GlStateManager.translate((float) n, (float) n2, (float) n3);
                GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                float textY = mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;
                GlStateManager.rotate(mc.getRenderManager().playerViewX, textY, 0.0f, 0.0f);
                final double size = 0.03;
                GlStateManager.scale(-size, -size, size);
                RenderUtils.enableGL2D();
                RenderUtils.disableGL2D();
                GL11.glDepthMask(false);
                //mc.fontRendererObj.drawStringWithShadow(en, (float) -(this.mc.fontRendererObj.getStringWidth(p.text) / 2), (float) -(this.mc.fontRendererObj.getHeight() - 1), 0);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glDepthMask(true);
                GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
                GlStateManager.disablePolygonOffset();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }
}
