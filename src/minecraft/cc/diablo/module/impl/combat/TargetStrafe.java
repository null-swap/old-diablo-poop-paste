package cc.diablo.module.impl.combat;

import cc.diablo.event.EventType;
import cc.diablo.event.impl.MotionEvent;
import cc.diablo.event.impl.MoveRawEvent;
import cc.diablo.event.impl.Render3DEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.helpers.player.EntityHelper;
import cc.diablo.helpers.player.KillAuraHelper;
import cc.diablo.helpers.player.MoveUtils;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.helpers.render.ColorHelper;
import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.module.impl.movement.Fly;
import cc.diablo.module.impl.movement.Speed;
import cc.diablo.module.impl.render.Esp2D;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TargetStrafe extends Module {
    private PathFinder pathFinder;
    public List<Float[]> points = new CopyOnWriteArrayList<Float[]>();
    private static float rotations[];
    public float yawChange;
    public float pitchChange;
    private static EntityLivingBase target;
    private static int direction;
    public double yLevel;
    boolean deincrement;

    public static NumberSetting radius = new NumberSetting("Radius", 2, 1, 5, 0.1);

    public TargetStrafe() {
        super("Target Strafe", "Strafe around entity", Keyboard.KEY_NONE, Category.Combat);
        direction = -1;
        rotations = new float[0];
        this.addSettings(radius);
    }

    public static void setStrafe(MoveRawEvent event, double moveSpeed) {
        if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(target) <= radius.getVal() - 1) {
            MoveUtils.setMoveSpeed(event, moveSpeed, rotations[0], direction,  -0.1);
        } else if((Minecraft.getMinecraft().thePlayer.getDistanceToEntity(target) <= radius.getVal()) && (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(target)<= (radius.getVal() + 2))) {
            MoveUtils.setMoveSpeed(event, moveSpeed, rotations[0], direction, 0.1);
        } else {
            MoveUtils.setMoveSpeed(event, moveSpeed, rotations[0], direction, 2);
        }
    }

    private void switchDirection() {
        if (this.direction == 1) {
            this.direction = -1;
        } else {
            this.direction = 1;
        }
    }

    @Subscribe
    public void onRawMove(MoveRawEvent e) {
        if (mc.gameSettings.keyBindJump.pressed && ModuleManager.getModule(Speed.class).isToggled()) {
            target = KillAura.target;
            double speed = 0.2;
            if (target != null && ModuleManager.getModule(KillAura.class).isToggled()) {
                if (ModuleManager.getModule(Speed.class).isToggled()) {
                    speed = Speed.moveSpeed;
                }

                if (ModuleManager.getModule(Fly.class).isToggled()) {
                    speed = 1f;
                    mc.thePlayer.motionY = 0.07;
                }
                if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(target) <= radius.getVal() - 1) {
                    EntityHelper.setMotionStrafe(speed, mc.thePlayer.rotationYaw, direction, -0.1);
                } else if ((Minecraft.getMinecraft().thePlayer.getDistanceToEntity(target) <= radius.getVal()) && (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(target) <= (radius.getVal() + 1))) {
                    EntityHelper.setMotionStrafe(speed, mc.thePlayer.rotationYaw, direction, 0.3);
                } else {
                    EntityHelper.setMotionStrafe(speed, mc.thePlayer.rotationYaw, direction, 2);
                }
            }
        }
    }

    @Subscribe
    public void onRenderESP(Render3DEvent event) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0, mc.gameSettings.thirdPersonView > 0 ? 1.0 : 0.0, 0.0);
        GlStateManager.rotate(-90.0f, 15.0f, 0.0f, 0.0f);
        drawBorderedCircle(0, 0, ((float) radius.getVal() / 1.5f), 1.0, ColorHelper.getColor(0), 0);
        GlStateManager.popMatrix();
    }

    @Subscribe
    public void onPreMotion(UpdateEvent event) {
        if (event.getType() == EventType.Pre) {
            if (mc.gameSettings.keyBindJump.pressed && ModuleManager.getModule(Speed.class).isToggled()) {
                KillAuraHelper.setRotations(event, (float) Speed.getDirection(),mc.thePlayer.rotationPitch);
            }
            if (mc.thePlayer.isCollidedHorizontally) {
                this.switchDirection();
            }
            if (mc.gameSettings.keyBindLeft.isPressed()) {
                this.direction = 1;
            }
            if (mc.gameSettings.keyBindRight.isPressed()) {
                this.direction = -1;
            }
        }
    }

    @Subscribe
    public void onMotion(UpdateEvent event) {
        if (!ModuleManager.getModule(KillAura.class).isToggled()) {
            target = null;
        }
        if (event.getType() == EventType.Pre) {
            if (target != null) {
                if (target.deathTime > 0 || target.isDead || !target.isEntityAlive()) {
                    target = null;
                }
            }

            if (target != null && pathFinder != null && mc.gameSettings.keyBindJump.pressed) {
                if (mc.thePlayer.getDistanceToEntity(target) <= 0.39) {
                    points.clear();
                }
                points.clear();
                PathEntity e = pathFinder.createEntityPathTo(mc.theWorld, mc.thePlayer, target, 50);
                if (e != null) {
                    try {
                        int offset = (e.getCurrentPathLength() % 2 == 0) ? 1 : 2;
                        for (int i = offset; i < e.getCurrentPathLength(); i++) {
                            PathPoint pp = e.getPathPointFromIndex(i);
                            float x = pp.xCoord + 0.5f;
                            float y = pp.yCoord + 0.5f;
                            float z = pp.zCoord + 0.5f;
                            points.add(new Float[]{x, y, z});
                        }
                    } catch (Exception b) {
                        b.printStackTrace();
                    }

                    float var3 = mc.thePlayer.onGround ? mc.thePlayer.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(mc.thePlayer.posX), MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(mc.thePlayer.posZ))).getBlock().slipperiness * 0.91F : 0.91f;
                    if (points.size() > 0) {
                        if (mc.thePlayer.isMoving() && !mc.gameSettings.keyBindBack.pressed) {
                            float[] yawChange = EntityHelper.getAngles(this.target);
                            rotations = yawChange;
                        }
                    }
                }

                points.clear();
            }
        }
    }


    private void drawCircle(Entity entity, float partialTicks, double rad) {
        for (double il = 0; il < 0.05; il += 0.0006) {
            GL11.glPushMatrix();
            GlStateManager.disableTexture2D();
            GlStateManager.enableDepth();
            GL11.glDepthMask(false);
            GL11.glLineWidth(1.0f);
            GL11.glBegin(1);
            yLevel += deincrement ? -0.0001 : 0.0001;
            if (yLevel > 1.8) {
                deincrement = true;
            }
            if (yLevel <= 0) {
                deincrement = false;
            }
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

            y += yLevel;

            float r = ((float) 1 / 255) * 114;
            float g = ((float) 1 / 255) * 149;
            float b = ((float) 1 / 255) * 229;

            double pix2 = Math.PI * 2D;

            float speed = 1200f;
            float baseHue = System.currentTimeMillis() % (int) speed;
            while (baseHue > speed) {
                baseHue -= speed;
            }
            baseHue /= speed;

            for (int i = 0; i <= 90; ++i) {
                float max = ((float) i) / 45F;
                float hue = max + baseHue;
                while (hue > 1) {
                    hue -= 1;
                }
                float f3 = (float) ((int) 3 >> 24 & 255) / 255.0F;
                float f = (float) ((int) 3 >> 16 & 255) / 255.0F;
                float f1 = (float) ((int) 3 >> 8 & 255) / 255.0F;
                float f2 = (float) ((int) 3 & 255) / 255.0F;
                float red = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getRed();
                float green = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getGreen();
                float blue = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getBlue();
                GL11.glColor3f(f3, f, f1);
                GL11.glVertex3d(x + rad * Math.cos(i * pix2 / 45.0), y + il, z + rad * Math.sin(i * pix2 / 45.0));
            }

            GL11.glEnd();
            GL11.glDepthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GL11.glPopMatrix();
        }
    }

    public static void drawBorderedCircle(final int circleX, final int circleY, final double radius, final double width, final int borderColor, final int innerColor) {
        enableGL2D();
        drawCircle((float) circleX, (float) circleY, (float) (radius - 0.5 + width), 72, borderColor);
        drawFullCircle(circleX, circleY, radius, innerColor);
        disableGL2D();
    }

    public static void drawCircle(float cx, float cy, float r, final int num_segments, final int c) {
        GL11.glPushMatrix();
        cx *= 2.0f;
        cy *= 2.0f;
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        final float theta = (float) (6.2831852 / num_segments);
        final float p = (float) Math.cos(theta);
        final float s = (float) Math.sin(theta);
        float x;
        r = (x = r * 2.0f);
        float y = 0.0f;
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(2);
        for (int ii = 0; ii < num_segments; ++ii) {
            GL11.glVertex2f(x + cx, y + cy);
            final float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
        GL11.glPopMatrix();
    }

    public static void drawFullCircle(int cx, int cy, double r, final int c) {
        r *= 2.0;
        cx *= 2;
        cy *= 2;
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 2160; ++i) {
            final double x = Math.sin(i * 3.141592653589793 / 360.0) * r;
            final double y = Math.cos(i * 3.141592653589793 / 360.0) * r;
            GL11.glVertex2d(cx + x, cy + y);
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
}
