package cc.diablo.module.impl.movement;

import cc.diablo.event.impl.CollideEvent;
import cc.diablo.event.impl.MoveRawEvent;
import cc.diablo.event.impl.PacketEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.HypixelHelper;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.helpers.player.EntityHelper;
import cc.diablo.helpers.player.MoveUtils;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.module.impl.combat.TargetStrafe;
import cc.diablo.setting.impl.BooleanSetting;
import cc.diablo.setting.impl.ModeSetting;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;
import store.intent.intentguard.annotation.Native;

import java.util.ArrayList;
import java.util.Random;

public class Fly extends Module {
    public ModeSetting mode = new ModeSetting("Flight mode", "VerusDMG", "Vanilla", "VerusDMG", "Collide", "VerusDEV","BlocksMC","TP-Fly","HypixelSlime","MuffinIsDad","Invaded","MushMC","Ghostly","GhostlyInv","Watchdog","Gwen","Viper");
    public NumberSetting timer = new NumberSetting("Timer", 1, 0.1, 8, 0.1);
    public NumberSetting speed = new NumberSetting("Speed", 4, 0.5, 8, 0.1);
    public BooleanSetting viewBobbing = new BooleanSetting("View Bobbing",true);
    public BooleanSetting noMotion = new BooleanSetting("Stop Motion",false);
    public BooleanSetting distanceDisable = new BooleanSetting("Auto Disable after Distance",false);
    public NumberSetting distance = new NumberSetting("Distance", 12, 1, 180, 1);
    int state;
    double start_y;
    public boolean isChangedVelocity;
    boolean prevViewBobbing;

    public Fly() {
        super("Fly", "Allows Flight", Keyboard.KEY_NONE, Category.Movement);
        this.addSettings(mode,timer,speed, viewBobbing, noMotion);
    }

    @Override
    public void onEnable(){

        start_y = mc.thePlayer.posY;
        if(viewBobbing.isChecked()) {
            prevViewBobbing = mc.gameSettings.viewBobbing;
            mc.gameSettings.viewBobbing = true;
        }
        isChangedVelocity = false;
        String mode = this.mode.getMode();
        switch (mode) {
            case "Invaded":
                if (!mc.thePlayer.onGround) {
                    ChatHelper.addChat("You Must Be On Ground!");
                    ModuleManager.getModule(Fly.class).setToggled(false);
                }

                state = 0;

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(75);
                            mc.gameSettings.keyBindUseItem.pressed = true;
                            Thread.sleep(270);
                            mc.gameSettings.keyBindUseItem.pressed = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
            case "Watchdog":
                if(mc.thePlayer.onGround) {
                    PacketHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.05, mc.thePlayer.posZ, false));
                    PacketHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.08, mc.thePlayer.posZ, false));
                } else {
                    ChatHelper.addChat("You Must Be On Ground!");
                    setToggled(false);
                }

                state = 0;
                start_y = mc.thePlayer.posY;
                break;

            case "HypixelSlime":
                HypixelHelper.slimeDisable();
                break;
            case "BlocksMC":
                /*
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 3.1, z, false));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0E-4, z, false));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0E-2, z, true));
                 */
                break;
            case "GhostlyInv":
            case "Ghostly":
            case "VerusDMG":
            case "VerusDEV":
                damageVerus();
                /*
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 3.1, z, false));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0E-4, z, false));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0E-2, z, true));
                 */
                break;
            case "Collide":
                double x = mc.thePlayer.posX;
                double y = mc.thePlayer.posY;
                double z = mc.thePlayer.posZ;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 3.1, z, false));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0E-4, z, false));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0E-2, z, true));
                break;
        }
        super.onEnable();
    }

    @Override
    public void onDisable(){
        mc.gameSettings.viewBobbing = prevViewBobbing;
        if(this.noMotion.isChecked()) {
            EntityHelper.setMotion(0);
        }
        Timer.timerSpeed = 1f;
        super.onDisable();
    }

    @Subscribe
    public void onCollide(CollideEvent e) {
        String mode = this.mode.getMode();
        final double x = e.getX();
        final double y = e.getY();
        final double z = e.getZ();

        switch (mode) {
            case "Watchdog":
                if(state == 1) {
                    e.setBoundingBox(AxisAlignedBB.fromBounds(15.0,1.0,15.0,-15.0,-1.0,-15.0).offset(x,y+0.05,z));
                }
            break;
            case "Collide":
                /*
                if (y < mc.thePlayer.posY && !mc.gameSettings.keyBindSneak.isKeyDown()) {
                    e.setBoundingBox(AxisAlignedBB.fromBounds(15.0, 1.0, 15.0, -15.0, -1.0, -15.0).offset(x, y, z));
                }
<<<<<<< Updated upstream
            break;
=======

                 */
                break;
            case "VerusDMG":
            case "VerusDEV":
            case "GhostlyInv":
            case "BlocksMC":
            case "Ghostly":
            case "Viper":
            case "Invaded":
                if (y < mc.thePlayer.posY && isChangedVelocity) {
                    e.setBoundingBox(AxisAlignedBB.fromBounds(15.0, 1.0, 15.0, -15.0, -1.0, -15.0).offset(x, y, z));
                }
            break;
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        String mode = this.mode.getMode();
        this.setDisplayName("Fly\2477 " + this.mode.getMode());
        if (state == 1) {
            Timer.timerSpeed = (float) this.timer.getVal();if (mc.thePlayer.isMoving()) {
                mc.thePlayer.setSpeed(0.28f - 0.13);
            }
        }else{
            Timer.timerSpeed = 8;
        }
        switch (mode) {
            case "Invaded":
                if (!isChangedVelocity) {
                    mc.thePlayer.posY = start_y;
                    e.setRotationPitch(-88.7f);
                }
                break;
            case "Watchdog":
                e.setOnGround(false);
                break;
            case "Vanilla":
                if (e.isPre()) {
                    EntityHelper.setMotion(speed.getVal());
                    if (MovementInput.jump && !ModuleManager.getModule(TargetStrafe.class).isToggled()) {
                        mc.thePlayer.motionY = 0.5;
                    } else if (MovementInput.sneak && !ModuleManager.getModule(TargetStrafe.class).isToggled()) {
                        mc.thePlayer.motionY = -0.5;
                    } else {
                        mc.thePlayer.motionY = 0;
                        mc.thePlayer.motionY -= mc.thePlayer.ticksExisted % 10 == 0 ? 0.08 : 0;
                    }
                }
                break;
            case "HypixelSlime":
                if(isChangedVelocity) {
                    if (e.isPre()) {
                        EntityHelper.setMotion(speed.getVal());
                        if (MovementInput.jump) {
                            mc.thePlayer.motionY = 0.75;
                        } else if (MovementInput.sneak) {
                            mc.thePlayer.motionY = -0.75;
                        } else {
                            mc.thePlayer.motionY = 0;
                            mc.thePlayer.motionY -= mc.thePlayer.ticksExisted % 10 == 0 ? 0.08 : 0;
                        }
                    }
                }
                break;
            case "TP-Fly":
                mc.thePlayer.motionY = 0.0D;
                mc.thePlayer.motionX = 0.0D;
                mc.thePlayer.motionZ = 0.0D;
                double value = MathHelper.getRandomDoubleInRange(new Random(), 4.420D, 5.19D);
                double x = -Math.sin(MoveUtils.getDirection()) * value;
                double z = Math.cos(MoveUtils.getDirection()) * value;
                if(mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.ticksExisted % this.speed.getVal() == 0) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY - 1.75, mc.thePlayer.posZ + z);
                    } else {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                    }
                }

                mc.thePlayer.motionY = 0;
                break;
            case "MushMC":
                Timer.timerSpeed = 0.85f;
                EntityHelper.setMotion(speed.getVal());
                if (MovementInput.jump) {
                    mc.thePlayer.motionY = 0.75;
                } else if (MovementInput.sneak) {
                    mc.thePlayer.motionY = -0.75;
                } else {
                    mc.thePlayer.motionY = 0;
                    mc.thePlayer.motionY -= mc.thePlayer.ticksExisted % 10 == 0 ? 0.08 : 0;
                }
                break;
            case "Collide":
                Timer.timerSpeed = 0.1f;
                EntityHelper.setMotion(speed.getVal());
                if (MovementInput.jump) {
                    mc.thePlayer.motionY = 0.75;
                } else if (MovementInput.sneak) {
                    mc.thePlayer.motionY = -0.75;
                } else {
                    mc.thePlayer.motionY = 0;
                    mc.thePlayer.motionY -= mc.thePlayer.ticksExisted % 10 == 0 ? 0.08 : 0;
                }
        }

    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        String mode = this.mode.getMode();
        switch (mode) {
            case "Watchdog":
                if (event.getPacket() instanceof S08PacketPlayerPosLook && state == 0) {
                    state = 1;
                }
                break;
            case "HypixelSlime":
                if (event.getPacket() instanceof S12PacketEntityVelocity) {
                    if (mc.thePlayer.getEntityId() == ((S12PacketEntityVelocity) event.getPacket()).getEntityID()) {
                        event.setCancelled(true);
                        isChangedVelocity = true;
                    }
                }

                break;
            case "VerusDMG":
                if (event.getPacket() instanceof S12PacketEntityVelocity) {
                    if (mc.thePlayer.getEntityId() == ((S12PacketEntityVelocity) event.getPacket()).getEntityID()) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ);
                        isChangedVelocity = true;
                    }
                }
                break;
            case "Invaded":
                if (event.getPacket() instanceof S12PacketEntityVelocity) {
                    if (mc.thePlayer.getEntityId() == ((S12PacketEntityVelocity) event.getPacket()).getEntityID()) {
                        isChangedVelocity = true;
                    }
                }
                break;
        }
    }
    @Subscribe
    public void onMoveRaw(MoveRawEvent e){
        switch (this.mode.getMode()) {
            case "Viper":
                if (!mc.thePlayer.isMovingOnGround()) {
                    Timer.timerSpeed = 1F;
                    return;
                }

                if (mc.thePlayer.isMoving()) {
                    Timer.timerSpeed = 0.3f;
                    for (int i = 0; i < 17; ++i) {
                        TPGROUND(e, 0.06, 0);
                    }
                }
                break;
            case "Watchdog":
                if (state == 0) {
                    e.setX(0);
                    e.setZ(0);
                }
                break;
            case "BlocksMC":
                if (!mc.thePlayer.isInLava() && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && mc.thePlayer.ridingEntity == null && mc.thePlayer.isMoving()) {
                    Timer.timerSpeed = 0.05f;
                    if (mc.thePlayer.hurtTime <= 16) {
                        EntityHelper.setMotion(speed.getVal());
                    }
                }
            case "VerusDEV":
            case "Ghostly":
                if (!mc.thePlayer.isInLava() && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && mc.thePlayer.ridingEntity == null) {
                    if (mc.thePlayer.isMoving()) {
                        mc.gameSettings.keyBindJump.pressed = false;
                        if (mc.thePlayer.onGround) {
                            if (mc.gameSettings.keyBindForward.isPressed()) {
                                mc.thePlayer.setSprinting(true);
                            }
                            mc.thePlayer.jump();
                            e.y = 0.41999998688697815;
                            //mc.thePlayer.motionY = 0.41999998688697815;
                            mc.thePlayer.motionY = 0.0;
                        }

                        if (mc.thePlayer.hurtTime <= 16) {
                            EntityHelper.setMotion(speed.getVal());
                        }
                        //setMoveSpeed(event, 0.35);
                    }
                    Timer.timerSpeed = 0.45f;
                }
                break;
            case "GhostlyInv":
                if (!mc.thePlayer.isInLava() && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && mc.thePlayer.ridingEntity == null) {
                    if (mc.thePlayer.isMoving()) {
                        mc.gameSettings.keyBindJump.pressed = false;
                        if (MovementInput.jump) {
                            mc.thePlayer.motionY = 0.3;
                        } else if (MovementInput.sneak) {
                            mc.thePlayer.motionY = -0.3;
                        } else {
                            mc.thePlayer.motionY = 0;
                            mc.thePlayer.motionY -= mc.thePlayer.ticksExisted % 10 == 0 ? 0.08 : 0;
                        }
                        EntityHelper.setMotion(speed.getVal());
                        //setMoveSpeed(event, 0.35);
                    }
                }

                if (mc.thePlayer.ticksExisted % 90 == 0) {
                    this.setToggled(false);
                }
                break;
            case "VerusDMG":
                if (!mc.thePlayer.isInLava() && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && mc.thePlayer.ridingEntity == null && isChangedVelocity) {
                    if (mc.thePlayer.isMoving()) {
                        mc.gameSettings.keyBindJump.pressed = false;
                        if (mc.thePlayer.onGround) {
                            if (mc.gameSettings.keyBindForward.isPressed()) {
                                mc.thePlayer.setSprinting(true);
                            }
                            /*
                            mc.thePlayer.jump();
                            e.y = 0.41999998688697815;
                            mc.thePlayer.motionY = 0.41999998688697815;
                            mc.thePlayer.motionY = 0.0;
                             */
                        }

                        if (mc.thePlayer.hurtTime <= 16) {
                            EntityHelper.setMotion(mc.thePlayer.hurtTime < 7 ? speed.getVal() * 1.5 : speed.getVal());
                        }
                        //setMoveSpeed(event, 0.35);
                    }

                    mc.thePlayer.onGround = true;
                    if (MovementInput.jump) {
                        mc.thePlayer.motionY = 0.75;
                    } else if (MovementInput.sneak) {
                        mc.thePlayer.motionY = -0.75;
                    }// else {
                    //mc.thePlayer.motionY = 0;
                    //mc.thePlayer.motionY -= mc.thePlayer.ticksExisted % 10 == 0 ? 0.08 : 0;
                    //}
                    //mc.timer.timerSpeed = 0.45f;
                }
                break;
            case "Invaded":
                if (isChangedVelocity) {
                    Timer.timerSpeed = 1f;

                    EntityHelper.setMotion(speed.getVal());
                    //EntityHelper.setMotion(speed.getVal());

                    if (mc.gameSettings.keyBindForward.isPressed()) {
                        mc.thePlayer.setSprinting(true);
                    }

                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        e.y = 0.41999998688697815;
                    }
                    if (MovementInput.jump) {
                        mc.thePlayer.motionY = 0.75;
                    } else if (MovementInput.sneak) {
                        mc.thePlayer.motionY = -0.75;
                    } else {
                        mc.thePlayer.motionY = 0;
                        mc.thePlayer.motionY -= mc.thePlayer.ticksExisted % 10 == 0 ? 0.08 : 0;
                    }
                } else {
                    if (state == 0) {
                        e.setX(0);
                        e.setZ(0);
                    }

                }
                break;
        }
    }


    public static void damageVerus() {
        mc.getNetHandler().getNetworkManager().sendPacket(
                new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));

        double val1 = 0;

        for (int i = 0; i <= 6; i++) {
            val1 += 0.5;
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                    mc.thePlayer.posY + val1, mc.thePlayer.posZ, true));
        }

        double val2 = mc.thePlayer.posY + val1;

        ArrayList<Float> vals = new ArrayList<>();

        vals.add(0.07840000152587834f);
        vals.add(0.07840000152587834f);
        vals.add(0.23052736891295922f);
        vals.add(0.30431682745754074f);
        vals.add(0.37663049823865435f);
        vals.add(0.44749789698342113f);
        vals.add(0.5169479491049742f);
        vals.add(0.5850090015087517f);
        vals.add(0.6517088341626192f);
        vals.add(0.1537296175885956f);

        for (float value : vals) {
            val2 -= value;
        }
        mc.thePlayer.sendQueue.addToSendQueue(
                new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, val2, mc.thePlayer.posZ, false));
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));

        mc.getNetHandler().getNetworkManager().sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
        mc.thePlayer.jump();
    }

    public static void TPGROUND(MoveRawEvent event, double speed, double y) {
        float yaw = mc.thePlayer.rotationYaw;
        final float forward = mc.thePlayer.moveForward;
        final float strafe = mc.thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        if (strafe > 0.0f) {
            yaw -= ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        float direction =  yaw * 0.017453292f;

        final double posX = mc.thePlayer.posX;
        final double posY = mc.thePlayer.posY;
        final double posZ = mc.thePlayer.posZ;
        final double raycastFirstX = -Math.sin(direction);
        final double raycastFirstZ = Math.cos(direction);
        final double raycastFinalX = raycastFirstX * speed;
        final double raycastFinalZ = raycastFirstZ * speed;
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX + raycastFinalX, posY + y, posZ + raycastFinalZ, true));
        mc.thePlayer.setPosition(posX + raycastFinalX, posY + y, posZ + raycastFinalZ);
    }
}