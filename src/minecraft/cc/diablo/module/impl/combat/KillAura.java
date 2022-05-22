package cc.diablo.module.impl.combat;

import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.impl.movement.Speed;
import cc.diablo.setting.impl.ModeSetting;
import com.google.common.eventbus.Subscribe;
import com.mojang.authlib.GameProfile;
import cc.diablo.Main;
import cc.diablo.event.impl.OverlayEvent;
import cc.diablo.event.impl.Render3DEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.font.TTFFontRenderer;
import cc.diablo.helpers.*;
import cc.diablo.helpers.player.EntityHelper;
import cc.diablo.helpers.player.KillAuraHelper;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.helpers.render.ColorHelper;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.module.impl.render.Esp2D;
import cc.diablo.setting.impl.BooleanSetting;
import cc.diablo.setting.impl.NumberSetting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.*;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldServer;
import optifine.MathUtils;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class KillAura extends Module {
    public ModeSetting rotationMode = new ModeSetting("Rotations", "Smooth", "Smooth", "Watchdog", "Aids", "Legit", "None");
    public ModeSetting autoBlockMode = new ModeSetting("Auto Block", "Fake", "Hypixel", "Fake", "Verus", "Legit");
    public ModeSetting targetHudMode = new ModeSetting("Target Hud", "Diablo Old", "Diablo Old", "Diablo New");
    public BooleanSetting autoBlock = new BooleanSetting("Auto Block", false);
    public BooleanSetting blockAttack = new BooleanSetting("Block & Attack", false);
    public NumberSetting range = new NumberSetting("Range", 3.7, 2, 6, 0.1);
    public NumberSetting minCPS = new NumberSetting("Min CPS", 9, 1, 20, 1);
    public NumberSetting maxCPS = new NumberSetting("Max CPS", 14, 1, 20, 1);
    public NumberSetting block_range = new NumberSetting("Block Range", 5, 3, 10, 1);
    public static BooleanSetting stopSprint = new BooleanSetting("Stop Sprint", false);
    public static BooleanSetting targetInvis = new BooleanSetting("Target Invis", true);
    public static BooleanSetting targethud = new BooleanSetting("Targethud", true);
    public static NumberSetting speed = new NumberSetting("Auto Block Ticks", 6, 1, 20, 1);
    public static NumberSetting ticks = new NumberSetting("Ticks Existed", 1, 1, 100, 1);

    public static EntityLivingBase target = null;
    private final Stopwatch timer = new Stopwatch();
    public static boolean blocking = false;
    public static float setYaw, setPitch;
    public static int killCount = 0;

    public KillAura() {
        super("KillAura", "Automatically Attacks Entities.", Keyboard.KEY_NONE, Category.Combat);
        this.addSettings(rotationMode, autoBlockMode, targetHudMode, autoBlock, blockAttack, range, block_range, stopSprint, minCPS, maxCPS, targetInvis, targethud, speed, ticks);
    }

    @Override
    public void onEnable() {
        target = null;
        blocking = false;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if(!autoBlockMode.isMode("Fake"))
        PacketHelper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0,-1,0), EnumFacing.DOWN));
        target = null;
        blocking = false;
        super.onDisable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        this.setDisplayName("Kill Aura\2477 " + minCPS.getVal() + ":" + maxCPS.getVal() + " | " + range.getVal());

        if (target != null) {
            if (target.isDead) {
                DiscordRPCHelper.killCount += 1;
                DiscordRPCHelper.updateRPC();
            }
        }

        EntityLivingBase autoBlockRange;
        target = KillAuraHelper.getClosestPlayerEntity(this.range.getVal());
        autoBlockRange = KillAuraHelper.getClosestPlayerEntity(block_range.getVal());

        if (autoBlock.isChecked()) {
            if (autoBlockRange != null) {
                if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && mc.thePlayer.ticksExisted % speed.getVal() == 0) {
                    switch (autoBlockMode.getMode()) {
                        case "Fake":
                            blocking = true;
                            break;
                        case "Hypixel":
                            if(e.isPost()) {
                                PacketHelper.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                            } else if(e.isPre()){
                                PacketHelper.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,new BlockPos(-1,-1,-1),EnumFacing.DOWN));
                            }
                            blocking = true;

                            break;
                        case "Verus":
                            if (!blocking) {
                                blocking = true;
                                PacketHelper.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                            }
                            break;
                        case "Legit":
                            ChatHelper.addChat(target.hurtTime + " " + blocking);
                            if (target.hurtTime > 0 && !blocking) {
                                blocking = true;
                                mc.playerController.onPlayerRightClick(mc.thePlayer, Minecraft.theWorld, mc.thePlayer.getHeldItem(), new BlockPos(0, mc.thePlayer.posY - 1, 0), Block.getFacingDirection(target.getPosition()), target.getLookVec());
                            } else {
                                blocking = false;
                            }
                            break;

                    }
                }
            } else {
                if(blocking) {
                    PacketHelper.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                }
                blocking = false;
            }
        }

        if (target != null && KillAuraHelper.canAttack(target)) {

            if (stopSprint.checked) {
                mc.thePlayer.setSprinting(false);
            }
            if (e.isPre()) {
                float[] rotations = EntityHelper.getAngles(target);
                float rot0 = (float) (rotations[0] + MathHelper.randomNumber(1, 2));
                float rot1 = rotations[1] + MathHelper.getRandom();

                switch (rotationMode.getMode()) {
                    case "Smooth":
                        KillAuraHelper.setRotations(e, rot0, rot1);
                        break;
                    case "Watchdog":
                        if (mc.thePlayer.swingProgressInt == 1) {
                            setPitch = rot0;
                            setYaw = (float) MathHelper.round(rot1, 25);
                        }
                        KillAuraHelper.setRotations(e, setPitch, setYaw);
                        break;
                    case "Legit":
                        final float[] rots = EntityHelper.getAnglesAGC(target);
                        float yaw = rots[0] + MathHelper.getRandomInRange(-9.3F, 9.3F);
                        float pitch = rots[1] + MathHelper.getRandomInRange(-6.8F, 6.8F);
                        float sens = KillAuraHelper.getSensitivityMultiplier();
                        float yawGCD = (Math.round(yaw / sens) * sens);
                        float pitchGCD = (Math.round(pitch / sens) * sens);
                        //if (Math.abs(pitchGCD) > 90) {
                        //pitchGCD = 90;
                        //}
                        KillAuraHelper.setRotations(e, yaw, pitch);
                        break;
                    case "Aids":
                        KillAuraHelper.setRotations(e, mc.thePlayer.ticksExisted * 30, -90);
                        break;
                    case "None":
                        break;
                }
                if (timer.hasReached(1000 / MathHelper.getRandInt((int) minCPS.getVal(), (int) maxCPS.getVal()))) {
                    if (blockAttack.isChecked()) {
                        attack(target);
                        timer.reset();
                    } else if (!blocking) {
                        attack(target);
                        timer.reset();
                    }
                }
            }
        } else {
            target = null;
        }

    }

    @Subscribe
    public void targetHud(OverlayEvent e) {
        if (target != null && targethud.isChecked()) {
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            TTFFontRenderer fr = Main.getInstance().getSFUI(20);
            int width = 125;
            int height = 38;
            int height2 = 35;
            int x = (scaledResolution.getScaledWidth() / 6) * 4 - width;
            int y = (scaledResolution.getScaledHeight() / 4) * 3 - height;
            int healthBarX = x + 37;
            int healthBarWidth = healthBarX + (mc.fontRendererObj.getStringWidth(target.getName())) + 5;
            int healtBarHeight = 15;
            int healhBarY = y + height - healtBarHeight - 3;
            int healhBarY2 = y + height2 - healtBarHeight - 3;
            int healthPercent = (int) EntityHelper.getEntityHealthPercent((EntityPlayer) target);
            float var28 = target.getHealth() + target.getAbsorptionAmount();
            float var32 = target.getMaxHealth() + target.getAbsorptionAmount();
            //float var37 = (float)(35 + mc.fontRendererObj.getStringWidth(target.getName() + 40));
            float var37 = (float) (35 + mc.fontRendererObj.getStringWidth("aaaaaaaaaaaaaa" + 40));
            float var42 = (float) ((double) Math.round((double) var28 * 100.0D) / 100.0D);
            float var46 = 100.0F / var32;
            float var48 = var42 * var46;
            float var51 = (var37 - 50.0F) / 100.0F;

            float var421 = (float) ((double) Math.round((double) var28 * 100.0D) / 100.0D);
            float var461 = 100.0F / var32;
            float var481 = var42 * var46;
            float var511 = (var37 - 9.00F) / 100.0F;

            switch (targetHudMode.getMode()){
                case "Diablo New":
                    RenderUtils.drawEsp(target, ColorHelper.getColor(0));
                    RenderUtils.drawEsp(target,new Color(18,18,18).getRGB());
                    //RenderUtils.drawRect(x - 1,y - 1,x + 42 + mc.fontRendererObj.getStringWidth("aaaaaaaaaaaaaa"),y + height + 1,RenderUtils.transparency(new Color(37, 37, 37).getRGB(), 0.3f));
                    RenderUtils.drawRect(x,y,x + 41 + mc.fontRendererObj.getStringWidth("aaaaaaaaaaaaaa"),y + height,RenderUtils.transparency(new Color(27, 27, 27).getRGB(), 0.65f));

                    //RenderUtils.drawRect(x,y + height - 1,x + 41 + mc.fontRendererObj.getStringWidth("aaaaaaaaaaaaaa"),y + height, ColorHelper.getColor( 150));
                    //RenderUtils.drawRect(x,y,x + 41 + mc.fontRendererObj.getStringWidth("aaaaaaaaaaaaaa"),y + height,RenderUtils.transparency(new Color(27, 27, 27).getRGB(), 0.65f));

                    RenderUtils.drawHead((AbstractClientPlayer) target, x + 2, y + 2, 34, 32);//Head
                    mc.fontRendererObj.drawStringWithShadow(target.getName(), x + 39, y + 4, -1);//Name
                    try {
                        RenderUtils.drawRect(x, y + height - 1.5, x + var481 * var511 + 3, y + height, RenderUtils.transparency(ColorHelper.getColor(0), 0.8f)); //Healhbar
                        mc.fontRendererObj.drawStringWithShadow(String.format("%.1f", var48) + "%", x + 39, healhBarY + 3, -1);//Name
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case "Diablo Old":
                    RenderUtils.drawEsp(target, ColorHelper.getColor(0));
                    RenderUtils.drawEsp(target,new Color(18,18,18).getRGB());
                    RenderUtils.drawRect(x - 1, y - 3, x + 41 + mc.fontRendererObj.getStringWidth("aaaaaaaaaaaaaa"), y + height2 + 1, RenderUtils.transparency(new Color(37, 37, 37).getRGB(), 0.6f)); //Blur
                    RenderUtils.drawRect(x, y, x + 40 + mc.fontRendererObj.getStringWidth("aaaaaaaaaaaaaa"), y + height2, RenderUtils.transparency(new Color(45, 45, 45, 255).getRGB(), 0.8f)); //Main background
                    RenderUtils.drawRect(x, y - 2, x + 40 + mc.fontRendererObj.getStringWidth("aaaaaaaaaaaaaa"), y, RenderUtils.transparency(ColorHelper.getColor(0), 0.8f)); //Color!
                    RenderUtils.drawHead((AbstractClientPlayer) target, x + 2, y + 2, 32, 32);//Head
                    mc.fontRendererObj.drawStringWithShadow(target.getName(), x + 37, y + 4, -1);//Name
                    try {
                        RenderUtils.drawRect(healthBarX, healhBarY2, healthBarX + var48 * var51 + 3, healhBarY2 + healtBarHeight, RenderUtils.transparency(ColorHelper.getColor(0), 0.8f)); //Healhbar
                        mc.fontRendererObj.drawStringWithShadow(String.format("%.1f", var48) + "%", x + 39, healhBarY + 3, -1);//Name
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    break;
            }

        }
    }

    public void attack(Entity entity) {
        mc.thePlayer.swingItem();
        mc.playerController.attackEntity(mc.thePlayer, entity);
    }
}
