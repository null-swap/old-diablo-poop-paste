package cc.diablo.module.impl.render;

import cc.diablo.Main;
import cc.diablo.font.TTFFontRenderer;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.Render3DEvent;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
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

public class Esp2D extends Module {

    private static final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
    private static final Frustum frustrum = new Frustum();

    public BooleanSetting box = new BooleanSetting("Box", true);
    public BooleanSetting healthbar = new BooleanSetting("Healthbar", true);
    public BooleanSetting armorBar = new BooleanSetting("Armor", true);
    public BooleanSetting swingTimeBar = new BooleanSetting("Swingtime", true);
    public BooleanSetting renderPlayer = new BooleanSetting("renderPlayer", true);
    public static NumberSetting red = new NumberSetting("Red",45,1,255,1);
    public static NumberSetting green = new NumberSetting("Green",24,1,255,1);
    public static NumberSetting blue = new NumberSetting("Blue",158,1,255,1);

    Color color = new Color((int) red.getVal(),(int) green.getVal(),(int) blue.getVal());

    public Esp2D(){
        super("2DEsp", "Esp", Keyboard.KEY_NONE, Category.Render);
        this.addSettings(box,healthbar,renderPlayer,armorBar,red,green,blue);
    }

    @Subscribe
    public void onRender(Render3DEvent e){
        Color color = new Color((int) red.getVal(),(int) green.getVal(),(int) blue.getVal());

        for (Object object : Minecraft.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            EntityLivingBase ent;
            if (!(entity instanceof EntityLivingBase) || entity.isInvisible() || (ent = (EntityLivingBase) entity) == mc.thePlayer || !isInViewFrustrum(ent))
                continue;
            double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) mc.timer.renderPartialTicks;
            double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) mc.timer.renderPartialTicks;
            double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) mc.timer.renderPartialTicks;
            double finalWidth = (double) entity.width / 1.4;
            double finalHeight = (double) entity.height + (entity.isSneaking() ? -0.3 : 0.2);
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(posX - finalWidth, posY, posZ - finalWidth, posX + finalWidth, posY + finalHeight + 0.05, posZ + finalWidth);
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
            GL11.glPushMatrix();
            float x = (float) posVec.x;
            float w = (float) posVec.z - x;
            float y = (float) posVec.y;
            float h = (float) posVec.w - y;
            int healthBarColor = getHealthColor(ent);
            if (ent instanceof EntityPlayer) {
                if (healthbar.isChecked()) {
                    drawBar(x - 2.0f, y, 1.5f, h, (float) ((int) ent.getMaxHealth()) / 2.0f, (float) ((int) ent.getMaxHealth()) / 2.0f, 0, new Color(-1459617792, true).getRGB());
                    drawBar(x - 3.0f, y, 1.5f, h, (float) ((int) ent.getMaxHealth()) / 2.0f, (float) ((int) ent.getHealth()) / 2.0f, healthBarColor, new Color(-1459617792, true).getRGB());
                }

                if (armorBar.isChecked()) {
                    drawBar(x + w + 1.0f, y, 1.5f, h, (float) ((int) ent.getMaxHealth()) / 2.0f, (float) ((int) ent.getMaxHealth()) / 2.0f, 0, new Color(-1459617792, true).getRGB());
                    drawBar(x + w + 1.0f, y, 1.5f, h, (float) ((int) 20) / 2.0f, (float) ((int) ent.getTotalArmorValue()) / 2.0f, new Color(31, 137, 252).getRGB(), new Color(-1459617792, true).getRGB());
                }

                if (box.isChecked()) {
                    drawBorderedRect(x, y, w, h, 1, new Color(0, 0, 0).getRGB(), 0);
                    drawBorderedRect(x, y, w, h, 0.5, color.getRGB(), 0);
                }
            }

            GL11.glPopMatrix();
            float var2 = mc.thePlayer.getDistanceToEntity(entity) / 2.0f;
            float scale = var2 * 8.0f;
            GL11.glPushMatrix();
            GL11.glScalef(-scale, -scale, scale);
            mc.fontRendererObj.drawStringWithShadow(ent.getName(), x, y - 14, new Color(255, 63, 63).getRGB());

            GL11.glPopMatrix();
        }

        if(renderPlayer.isChecked() && mc.gameSettings.thirdPersonView != 0) {
            Entity entity = mc.thePlayer;

            double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) mc.timer.renderPartialTicks;
            double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) mc.timer.renderPartialTicks;
            double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) mc.timer.renderPartialTicks;
            double finalWidth = (double) entity.width / 1.4;
            double finalHeight = (double) entity.height + (entity.isSneaking() ? -0.3 : 0.2);
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(posX - finalWidth, posY, posZ - finalWidth, posX + finalWidth, posY + finalHeight + 0.05, posZ + finalWidth);
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
            GL11.glPushMatrix();
            float x = (float) posVec.x;
            float w = (float) posVec.z - x;
            float y = (float) posVec.y;
            float h = (float) posVec.w - y;
            int healthBarColor = getHealthColor((EntityLivingBase) entity);
            EntityLivingBase ent = (EntityLivingBase) entity;
            if (healthbar.isChecked()) {
                drawBar(x - 2.0f, y,1.5f,h,(float) ((int) ent.getMaxHealth()) / 2.0f, (float) ((int) ent.getMaxHealth()) / 2.0f, 0, new Color(-1459617792, true).getRGB());
                drawBar(x - 3.0f, y,1.5f,h,(float) ((int) ent.getMaxHealth()) / 2.0f, (float) ((int) ent.getHealth()) / 2.0f, healthBarColor, new Color(-1459617792, true).getRGB());
            }
            if (armorBar.isChecked()) {
                drawBar(x + w  + 1.0f, y,1.5f,h,(float) ((int) ent.getMaxHealth()) / 2.0f, (float) ((int) ent.getMaxHealth()) / 2.0f, 0, new Color(-1459617792, true).getRGB());
                drawBar(x + w  + 1.0f, y,1.5f,h,(float) ((int) 20) / 2.0f, (float) ((int) ent.getTotalArmorValue()) / 2.0f, new Color(31, 137, 252).getRGB(), new Color(-1459617792, true).getRGB());
            }
            if (box.isChecked()) {
                drawBorderedRect(x,y,w,h,1, new Color(0,0,0).getRGB(), 0);
                drawBorderedRect(x,y,w,h,0.5, color.getRGB(), 0);
            }
            GL11.glPopMatrix();

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
