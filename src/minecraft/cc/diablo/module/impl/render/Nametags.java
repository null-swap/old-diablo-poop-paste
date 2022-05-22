package cc.diablo.module.impl.render;

import cc.diablo.Main;
import cc.diablo.event.impl.Render3DEvent;
import cc.diablo.font.TTFFontRenderer;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

public class Nametags extends Module {
    private static final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
    private static final Frustum frustrum = new Frustum();

    public static NumberSetting red = new NumberSetting("Red",45,1,255,1);
    public static NumberSetting green = new NumberSetting("Green",24,1,255,1);
    public static NumberSetting blue = new NumberSetting("Blue",158,1,255,1);

    public Nametags(){
        super("Nametags","Shows the chinese username of a player", Keyboard.KEY_NONE,Category.Render);
        this.addSettings(red,green,blue);
    }

    @Subscribe
    public void onRender(Render3DEvent e){
        Color color = new Color((int) red.getVal(),(int) green.getVal(),(int) blue.getVal());
        for (Object o : Minecraft.theWorld.playerEntities) {
            EntityPlayer p = (EntityPlayer) o;
            if (p.isEntityAlive() && !p.isInvisible()) {
                Minecraft.getRenderManager();
                double n = p.lastTickPosX + (p.posX - p.lastTickPosX) * mc.timer.renderPartialTicks;
                double pX = n - RenderManager.renderPosX;
                Minecraft.getRenderManager();
                double n2 = p.lastTickPosY + (p.posY - p.lastTickPosY) * mc.timer.renderPartialTicks;
                double pY = n2 - RenderManager.renderPosY;
                Minecraft.getRenderManager();
                double n3 = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * mc.timer.renderPartialTicks;
                double pZ = n3 - RenderManager.renderPosZ;
                this.renderNameTag(p,p.getName(), pX, pY, pZ);
            }
        }
    }

    private void renderNameTag(EntityPlayer entity, String tag, double x, double y, double z) {
        y += (entity.isSneaking() ? 0.5 : 0.7);
        float var2 = this.mc.thePlayer.getDistanceToEntity(entity) / 4.0f;
        if (var2 < 1.6f) {
            var2 = 1.6f;
        }
        int colour = 16777215;

        double health = Math.ceil(entity.getHealth() + entity.getAbsorptionAmount()) / 1.0;
        float scale = var2 * 2.0f;
        scale /= 200.0f;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y + 1.4f, (float) z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        Minecraft.getRenderManager();
        GL11.glRotatef(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-Minecraft.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-scale, -scale, scale);
        int width = mc.fontRendererObj.getStringWidth(tag) / 2;
        GL11.glBlendFunc(770, 771);
        mc.fontRendererObj.drawStringWithShadow(tag, (float) (-width), (float) (-(mc.fontRendererObj.FONT_HEIGHT - 1)) - 3, new Color(255, 63, 63).getRGB());
        GL11.glPushMatrix();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    public boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }
}
