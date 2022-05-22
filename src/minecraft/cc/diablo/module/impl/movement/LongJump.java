package cc.diablo.module.impl.movement;

import cc.diablo.event.impl.PacketEvent;
import cc.diablo.helpers.HypixelHelper;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.helpers.Stopwatch;
import cc.diablo.helpers.player.ItemUtil;
import cc.diablo.helpers.player.KillAuraHelper;
import cc.diablo.helpers.player.MoveUtils;
import cc.diablo.module.impl.combat.KillAura;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.player.EntityHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.BooleanSetting;
import cc.diablo.setting.impl.ModeSetting;
import net.minecraft.block.BlockDirectional;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;

public class LongJump extends Module {
    public ModeSetting mode = new ModeSetting("Longjump Mode","Verus","Vanilla", "Watchdog","WatchdogBow","HypixelSlime","Verus");
    public BooleanSetting damage = new BooleanSetting("Damage", true);
    public NumberSetting speed = new NumberSetting("Speed",0.75,0.25,8,0.1);
    public double moveSpeed;
    public boolean isChangedVelocity;
    public Stopwatch stopwatch = new Stopwatch();
    int ticks;

    public LongJump() {
        super("LongJump", "Longjump", Keyboard.KEY_NONE, Category.Movement);
        this.addSettings(mode, damage,speed);
    }

    @Override
    public void onEnable() {
        ticks = 0;
        stopwatch.reset();
        float x = (float) mc.thePlayer.posX;
        float y = (float) mc.thePlayer.posY;
        float z = (float) mc.thePlayer.posZ;
        float pitch = mc.thePlayer.rotationPitch;
        float yaw = mc.thePlayer.rotationYaw;
        moveSpeed = speed.getVal();
        isChangedVelocity = false;
        if(damage.isChecked()) {
            if(mode.getMode() != "WatchdogBow") {
                if(mode.getMode() != "Watchdog") {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 3.1, z, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0E-4, z, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0E-2, z, true));
                    mc.thePlayer.motionY = 0.425;
                    EntityHelper.setMotion(0.4);
                } else {
                    /*
                    for (short i = 0; i <= ((4) / 0.0325); i++) {
                        mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + ((0.0325 / 2) * 1), mc.thePlayer.posZ, false));
                        mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + ((0.0325 / 2) * 2), mc.thePlayer.posZ, false));
                        mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, (i == ((4) / 0.0325))));
                    }

                     */
                    //mc.thePlayer.motionY = 0.425;
                    EntityHelper.setMotion(mc.thePlayer.getActivePotionEffects().toString().contains("moveSpeed") ? 0.43f : 0.37f);
                }
            }
        }
        String mode = this.mode.getMode();
        switch (mode) {
            case "HypixelSlime":
                HypixelHelper.slimeDisable();
                break;
            case "Verus":
                mc.thePlayer.motionY = 0.6;
                EntityHelper.setMotion(moveSpeed);
                break;
            case "Vanilla":
                mc.thePlayer.motionY = 0.41;
                EntityHelper.setMotion(moveSpeed);
                break;
            case "WatchdogBow":
                mc.thePlayer.motionY = 0.255;
                // PacketHelper.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(x,y + 2,z), EnumFacing.UP.getIndex(),mc.thePlayer.getHeldItem(),x,y,z));
                break;
        }
        super.onEnable();
    }


    @Subscribe
    public void onMove(UpdateEvent e) {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;
        float pitch = mc.thePlayer.rotationPitch;
        float yaw = mc.thePlayer.rotationYaw;
        this.setDisplayName("Longjump\2477 "+ this.mode.getMode());
        String mode = this.mode.getMode();
        switch (mode){
            case "Verus":
            case "Vanilla":
                if(mc.thePlayer.isCollided && mc.thePlayer.onGround){
                    this.setToggled(false);
                }
                EntityHelper.setMotion(moveSpeed);
                break;
            case "Watchdog":
                ticks ++;
                if(ticks > 52) {
                    for (short i = 0; i <= ((4) / 0.0625); i++) {
                        mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + ((0.0625 / 2) * 2), mc.thePlayer.posZ, false));
                        mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + ((0.0625 / 2) * 2), mc.thePlayer.posZ, false));
                        mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, (i == ((4) / 0.0625))));

                        if(mc.thePlayer.fallDistance < 0) {
                            if (mc.thePlayer.isCollided && mc.thePlayer.onGround) {
                                this.setToggled(false);
                            }
                        }

                        EntityHelper.setMotion(0.4);
                    }
                }else {
                    mc.thePlayer.setSpeed(0);
                }

                break;
            case "WatchdogBow":
                Timer.timerSpeed = 0.75f;
                this.moveSpeed = 0.33f;
                EntityHelper.setMotion(moveSpeed);
                //mc.thePlayer.onGround = true;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.motionY -= mc.thePlayer.ticksExisted % 3 == 0 ? 0.08 : 0;
                if(stopwatch.hasReached(750)){
                    this.setToggled(false);
                }
                /*
                if(isChangedVelocity) {
                    if (mc.thePlayer.isCollided && mc.thePlayer.onGround) {
                        this.setToggled(false);
                    }
                    EntityHelper.setMotion(0.6);
                } else {
                    e.setX(x);
                    e.setY(y);
                    e.setZ(z);
                    mc.thePlayer.inventory.currentItem = getBestBow(mc.thePlayer);
                    //mc.playerController.updateController();
                    KillAuraHelper.setRotations(e,yaw,-90);
                }

                 */
                //EntityHelper.setMotion(moveSpeed);
                break;
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        String mode = this.mode.getMode();
        switch (mode) {
            case "HypixelSlime":
                if (event.getPacket() instanceof S12PacketEntityVelocity) {
                    if (mc.thePlayer.getEntityId() == ((S12PacketEntityVelocity) event.getPacket()).getEntityID()) {
                        isChangedVelocity = true;
                    }
                }
            case "WatchdogBow":
                /*
                if (event.getPacket() instanceof S12PacketEntityVelocity) {
                    if (mc.thePlayer.getEntityId() == ((S12PacketEntityVelocity) event.getPacket()).getEntityID()) {
                        isChangedVelocity = true;
                        event.setCancelled(true);
                        mc.thePlayer.motionY = 1;
                        mc.thePlayer.jump();

                    }
                }

                 */
                break;
        }
    }

    public static int getBestBow(final Entity target) {
        final int originalSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
        int weaponSlot = -1;
        float weaponDamage = 1.0f;
        for (byte slot = 0; slot < 9; ++slot) {
            Minecraft.getMinecraft().thePlayer.inventory.currentItem = slot;
            final ItemStack itemStack = Minecraft.getMinecraft().thePlayer.getHeldItem();
            if (itemStack != null && itemStack.getItem() instanceof ItemBow) {
                    weaponSlot = slot;
            }
        }
        if (weaponSlot != -1) {
            return weaponSlot;
        }
        return originalSlot;
    }

    public static float getItemDamage(final ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemBow) {
            double damage = 4.0 + ((ItemSword)itemStack.getItem()).getDamageVsEntity();
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemStack) * 1.25;
            return (float)damage;
        }
        return 1.0f;
    }
}
