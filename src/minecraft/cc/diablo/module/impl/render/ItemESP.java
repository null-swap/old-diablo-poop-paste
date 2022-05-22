package cc.diablo.module.impl.render;

import cc.diablo.event.impl.Render3DEvent;
import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
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

public class ItemESP extends Module {
    private static final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
    private static final Frustum frustrum = new Frustum();

    public ItemESP(){
        super("ItemESP","Renders a box on items that are dropped", Keyboard.KEY_NONE, Category.Render);
    }
    @Subscribe
    public void onRender(Render3DEvent e) {
        for (Object object : Minecraft.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            if (entity instanceof EntityItem) {
                double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) mc.timer.renderPartialTicks;
                double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) mc.timer.renderPartialTicks;
                double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) mc.timer.renderPartialTicks;
                double finalWidth = (double) entity.width / 1.75;
                double finalHeight = (double) entity.height + 0.5;
                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(posX - 0.15, ModuleManager.getModule(ItemPhysics.class).toggled ? posY + 0.35 : posY + 0.65, posZ- 0.225, posX +0.15, ModuleManager.getModule(ItemPhysics.class).toggled ? posY - 0.15 : posY + 0.15, posZ +0.225);
                List<Vector3d> vectorList = Arrays.asList(new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));
                mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);
                Vector4d posVec = null;
                for (Vector3d vector : vectorList) {
                    FloatBuffer otherVec = GLAllocation.createDirectFloatBuffer(4);
                    GL11.glGetFloat(2982, modelview);
                    GL11.glGetFloat(2983, projection);
                    GL11.glGetInteger(2978, viewport);
                    if (GLU.gluProject((float) (vector.x - mc.getRenderManager().viewerPosX), (float) ((double) ((float) vector.y) - mc.getRenderManager().viewerPosY), (float) ((double) ((float) vector.z) - mc.getRenderManager().viewerPosZ), modelview, projection, viewport, otherVec)) {
                        vector = new Vector3d(otherVec.get(0) / (float) new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor(), ((float) Display.getHeight() - otherVec.get(1)) / (float) new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor(), otherVec.get(2));
                    }
                    if (!(vector.z >= 0.0) || !(vector.z < 1.0)) continue;
                    if (posVec == null) {
                        posVec = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                    }
                    posVec.x = Math.min(vector.x, posVec.x);
                    posVec.y = Math.min(vector.y, posVec.y);
                    posVec.z = Math.max(vector.x, posVec.z);
                    posVec.w = Math.max(vector.y, posVec.w);
                }
                mc.entityRenderer.setupOverlayRendering();
                if (posVec == null) continue;
                int color = -1;
                GL11.glPushMatrix();
                float x = (float) posVec.x;
                float w = (float) posVec.z - x;
                float y = (float) posVec.y;
                float h = (float) posVec.w - y;
                        drawBorderedRect(x, y, w, h, 1, new Color(0, 0, 0).getRGB(), 0);
                        drawBorderedRect(x, y, w, h, 0.5, new Color(-1, true).getRGB(), 0);
                GL11.glPopMatrix();
            }
        }
    }

    public boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public void drawBorderedRect(double x, double y, double width, double height, double lineSize, int borderColor, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
        Gui.drawRect(x, y, x + width, y + lineSize, borderColor);
        Gui.drawRect(x, y, x + lineSize, y + height, borderColor);
        Gui.drawRect(x + width, y, x + width - lineSize, y + height, borderColor);
        Gui.drawRect(x, y + height, x + width, y + height - lineSize, borderColor);
    }

    public void drawBar(float x, float y, float width, float height, float max, float value, int color, int color2) {
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        float inc = height / max;
        GL11.glColor4f(f1, f2, f3, f);
        float incY = y + height - inc;
        int i = 0;
        while ((float)i < value) {
            drawBorderedRect(x + 0.25f, incY, width - 0.5f, inc, 0.25, new Color(color2, true).getRGB(), color);
            incY -= inc;
            ++i;
        }
    }

    private int getHealthColor(EntityLivingBase player) {
        return Color.HSBtoRGB(Math.max(0.0f, Math.min(player.getHealth(), player.getMaxHealth()) / player.getMaxHealth()) / 3.0f, 0.56f, 1.0f) | 0xFF000000;
    }

}
