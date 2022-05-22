package cc.diablo.module.impl.movement;

import cc.diablo.event.impl.MoveRawEvent;
import cc.diablo.event.impl.PacketEvent;
import cc.diablo.event.impl.TickEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.HypixelHelper;
import cc.diablo.helpers.MathHelper;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.helpers.player.EntityHelper;
import cc.diablo.helpers.player.KillAuraHelper;
import cc.diablo.helpers.player.MoveUtils;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.module.impl.combat.KillAura;
import cc.diablo.setting.impl.BooleanSetting;
import cc.diablo.setting.impl.ModeSetting;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AtomicDouble;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Speed extends Module {
    public ModeSetting mode = new ModeSetting("Speed mode","Verus","Vanilla", "Watchdog","WatchdogLow","HypixelSlime","HypixelSlimeLow","Verus", "NCP","Verus Lowhop", "Verus YP","SlowHop","VanillaHop","VanillaLowHop","Funcraft","GhostlyOnGround","Invaded","Dev","Gwen","Viper","ViperOnGround");
    public BooleanSetting dmgboost = new BooleanSetting("Damage Boost", true);
    public NumberSetting timer = new NumberSetting("Timer",1.2,1,5,0.05);
    public BooleanSetting sneakTimer = new BooleanSetting("Sneak Timer", false);
    public NumberSetting speed = new NumberSetting("Speed",1.2,1,4,0.05);

    public boolean resetLastDist, porting, shouldPort, hasPorted;
    public static double moveSpeed;
    public double hypixelMultiplier;
    public float yawHypixel;
    public boolean isChangedVelocity;
    private final AtomicDouble hDist = new AtomicDouble();

    public ArrayList<Packet> packets  = new ArrayList<Packet>();

    public Speed() {
        super("Speed", "Speed Up Player Movement", Keyboard.KEY_NONE, Category.Movement);
        this.addSettings(mode, dmgboost,timer,sneakTimer,speed);
    }

    @Override
    public void onEnable() {
        hypixelMultiplier=1;
        shouldPort = false;
        hasPorted = false;
        porting = false;
        resetLastDist = false;
        isChangedVelocity = false;
        moveSpeed = 0;
        hDist.set(0);
        packets.clear();
        switch (mode.getMode()){
            case "Vanilla":
            case "VanillaHop":
            case "VanillaLowHop":
            case "HypixelSlime":
            case "HypixelSlimeLow":
                moveSpeed = speed.getVal();
                break;
        }
        super.onEnable();
    }

    @Override
    public void onDisable(){
        switch (mode.getMode()) {
            case "WatchdogLow":
                EntityHelper.setMotion(0);
            break;
        }
        Timer.timerSpeed = 1f;
        super.onDisable();
    }

    @Subscribe
    public void onMoveRaw(MoveRawEvent e) {
        switch (mode.getMode()) {
            case "Vanilla":
            case "VanillaHop":
            case "VanillaLowHop":
            case "Watchdog":
            case "WatchdogLow":
            case "HypixelSlime":
            case "HypixelSlimeLow":
            case "Gwen":
                if (sneakTimer.isChecked()) {
                    if (mc.gameSettings.keyBindSneak.pressed) {
                        MovementInput.sneak = false;
                        Timer.timerSpeed = (float) timer.getVal();
                    } else {
                        Timer.timerSpeed = 1.0f;
                    }
                } else {
                    Timer.timerSpeed = (float) timer.getVal();
                }
                break;
        }

        String mode = this.mode.getMode();
        double sqrtedSpeed = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
        switch (mode) {
            case "Verus YP":
                if (!mc.thePlayer.isInLava() && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && mc.thePlayer.ridingEntity == null) {
                    if (mc.thePlayer.isMoving()) {
                        mc.gameSettings.keyBindJump.pressed = false;
                        if (mc.thePlayer.onGround) {
                            if (mc.gameSettings.keyBindForward.isPressed()) {
                                mc.thePlayer.setSprinting(true);
                            }
                            mc.thePlayer.jump();
                            //MoveUtil.strafe(0.81F);
                            e.z = 0.41999998688697815;
                            //mc.thePlayer.motionY = 0.41999998688697815;
                            mc.thePlayer.motionY = 0.0;
                        }
                        if (mc.thePlayer.getActivePotionEffects().toString().contains("moveSpeed") && (!mc.gameSettings.keyBindBack.isPressed() && !mc.gameSettings.keyBindRight.isPressed() && !mc.gameSettings.keyBindLeft.isPressed())) {
                            if (mc.thePlayer.isSprinting()) {
                                EntityHelper.setMotion(0.469);
                            } else {
                                EntityHelper.setMotion(0.41);
                            }


                        } else {
                            EntityHelper.setMotion(0.36);
                        }
                        if (mc.gameSettings.keyBindBack.isPressed() && mc.gameSettings.keyBindRight.isPressed() && mc.gameSettings.keyBindLeft.isPressed()) {
                            EntityHelper.setMotion(0.3125);
                        }
                        //setMoveSpeed(event, 0.35);
                    }
                }
                break;
            case "Gwen":
                Timer.timerSpeed = 1.3f;
                if (mc.thePlayer.isMovingOnGround()) {
                    e.setY(mc.thePlayer.motionY = getMotion(0.41f));
                }
                setMotion(MoveUtils.getBaseMoveSpeed() * MathHelper.getRandomInRange(0.932412f, 1.11935f));
                break;
            case "ViperOnGround":
                if (!mc.thePlayer.isMovingOnGround()) {
                    Timer.timerSpeed = 1f;
                    mc.thePlayer.motionY = -5f;
                    return;
                }

                if (mc.thePlayer.isMoving()) {
                    Timer.timerSpeed = 0.3f;
                    for (int i = 0; i < 17; ++i) {
                        TP(e, 0.22, 0);
                    }
                }
                break;
            case "Watchdog":
                if (mc.thePlayer.isMovingOnGround()) {
                    e.setY(mc.thePlayer.motionY = getMotion(0.41f));
                }

                break;
            case "WatchdogLow":
                if ((mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) && (mc.thePlayer != null && Minecraft.theWorld != null)) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        mc.thePlayer.motionY = .045;
                        MoveUtils.setMoveSpeed(e, 0.411132324);
                        this.moveSpeed = 0.31;
                    } else {
                        mc.thePlayer.setSpeed(0.2855234335);
                        if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
                            mc.thePlayer.setSpeed(0.32264563);
                        }
                        if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
                            mc.thePlayer.setSpeed(0.37342335);
                        } else {
                            mc.thePlayer.setSpeed(0.301234335);
                            if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 0) {
                                mc.thePlayer.setSpeed(0.33264563);
                            }
                            if (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
                                mc.thePlayer.setSpeed(0.39342335);
                            }
                        }
                        if (mc.thePlayer.fallDistance < 0) {
                            Timer.timerSpeed = (float) (timer.getVal() * 1.5f);
                        }
                    }
                    break;

                }
        }
    }

    @Subscribe
    public void onTick(TickEvent event){
        if(hypixelMultiplier > 0.94){
            hypixelMultiplier -= 0.0125;
        }
    }

    @Subscribe
    public void onMove(UpdateEvent e) {
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        double x2 = -Math.sin(yaw) * speed.getVal();
        double z2 = Math.cos(yaw) * speed.getVal();

        this.setDisplayName("Speed\2477 " + this.mode.getMode());
        String mode = this.mode.getMode();
        switch (mode){
            case "Viper":
                if(mc.thePlayer.isMovingOnGround()) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionY = 0.41999998688697815;
                    moveSpeed = MoveUtils.getSpeedModifier(dmgboost.isChecked() ? mc.thePlayer.hurtTime != 0 ? 0.65 : 0.525 : 0.525);
                }
                EntityHelper.setMotion(moveSpeed);

                if (mc.thePlayer.onGround && mc.thePlayer.isMoving()) {
                    Timer.timerSpeed = 0.65f;
                    mc.gameSettings.keyBindJump.pressed = true;
                } else {
                    Timer.timerSpeed = 1f;
                    mc.gameSettings.keyBindJump.pressed = false;
                }

                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.fallDistance < 0.1) {
                        Timer.timerSpeed = 1.81f;
                    }
                    if (mc.thePlayer.fallDistance > 0.2) {
                        Timer.timerSpeed = 0.42f;
                    }
                    if (mc.thePlayer.fallDistance > 0.6) {
                        Timer.timerSpeed = 1.05f;
                    }
                }

                if (mc.thePlayer.fallDistance > 1) {
                    Timer.timerSpeed = 1;
                }
                break;
            case "Verus": {
                if(mc.thePlayer.isMovingOnGround()) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionY = 0.41999998688697815;
                    moveSpeed = MoveUtils.getSpeedModifier(dmgboost.isChecked() ? mc.thePlayer.hurtTime != 0 ? 0.65 : 0.3325 : 0.325);
                }
                EntityHelper.setMotion(moveSpeed);
                break;
            }
            case "Verus Lowhop": {
                if(mc.thePlayer.isMoving()) {
                    if(mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        mc.thePlayer.motionY = 0.41999998688697815;
                        moveSpeed = MoveUtils.getSpeedModifier(dmgboost.isChecked() ? mc.thePlayer.hurtTime != 0 ? 0.65 : 0.325 : 0.325);
                        shouldPort = true;
                    } else {
                        if(shouldPort) {
                            mc.thePlayer.motionY = 0;
                            e.setOnGround(true);
                            hasPorted = true;
                            shouldPort = false;
                        }
                    }
                }
                EntityHelper.setMotion(moveSpeed);
                break;
            }
            case "SlowHop":
                if ((mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically)) {
                    if (mc.thePlayer.isMoving()){
                        Timer.timerSpeed = 1f;
                        e.setPosZ(0.41999998688697815);
                        mc.thePlayer.motionY = 0.41999998688697815;
                        moveSpeed = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? MoveUtils.getBaseMoveSpeed() * 0.525 : MoveUtils.getBaseMoveSpeed() * 0.85;
                    }
                }

                EntityHelper.setMotion(moveSpeed);
                break;
            case "NCP":
                KillAuraHelper.setRotations(e, (float) getDirection(),mc.thePlayer.rotationPitch);

                if ((mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically)) {
                    if (mc.thePlayer.isMoving()){
                        Timer.timerSpeed = 1.05f;
                        hypixelMultiplier = 1.1;
                        e.setPosZ(0.41999998688697815);
                        mc.thePlayer.motionY = 0.41999998688697815;
                        moveSpeed = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? MoveUtils.getBaseMoveSpeed() * 0.5 : MoveUtils.getBaseMoveSpeed() * 0.8;
                    }
                    this.resetLastDist = true;
                } else {
                    if (this.resetLastDist) {
                        moveSpeed -= 0.6600000262260437 * (moveSpeed - MoveUtils.getBaseMoveSpeed());
                        this.resetLastDist = false;
                    }
                }

                EntityHelper.setMotion(moveSpeed);
                break;
            case "Funcraft":
                if ((mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically)) {
                    if (mc.thePlayer.isMoving()){
                        Timer.timerSpeed = 1.6f;
                        hypixelMultiplier = 1.1;
                        e.setPosZ(0.21999998688697815);
                        mc.thePlayer.motionY = 0.21999998688697815;
                        moveSpeed = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? MoveUtils.getBaseMoveSpeed() * 0.425 : MoveUtils.getBaseMoveSpeed() * 0.85;
                    }
                    this.resetLastDist = true;
                } else {
                    if (this.resetLastDist) {
                        moveSpeed -= 0.6600000262260437 * (moveSpeed - MoveUtils.getBaseMoveSpeed());
                        this.resetLastDist = false;
                    }
                }

                EntityHelper.setMotion(moveSpeed);
                break;
            case "VanillaHop":
                mc.thePlayer.setSprinting(true);
                if ((mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically)) {
                    if (mc.thePlayer.isMoving()){
                        Timer.timerSpeed = 1f;
                        e.setPosZ(0.41999998688697815);
                        mc.thePlayer.motionY = 0.41999998688697815;
                        moveSpeed = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? MoveUtils.getBaseMoveSpeed() * speed.getVal() : MoveUtils.getBaseMoveSpeed() * (speed.getVal()*1.5);
                    }
                }

                EntityHelper.setMotion(moveSpeed);
                break;
            case "HypixelSlime":
                if(mc.thePlayer.onGround){
                    HypixelHelper.slimeDisable();
                }
                if(isChangedVelocity) {
                    mc.thePlayer.setSprinting(true);
                    if ((mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically)) {
                        if (mc.thePlayer.isMoving()) {
                            e.setPosZ(0.41999998688697815);
                            mc.thePlayer.motionY = 0.41999998688697815;
                            moveSpeed = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? MoveUtils.getBaseMoveSpeed() * speed.getVal() : MoveUtils.getBaseMoveSpeed() * (speed.getVal() * 1.5);
                        }
                    }
                    EntityHelper.setMotion(moveSpeed);
                }
                break;
            case "HypixelSlimeLow":
                if(mc.thePlayer.onGround){
                    HypixelHelper.slimeDisable();
                }

                if(isChangedVelocity) {
                    mc.thePlayer.setSprinting(true);
                    if ((mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically)) {
                        if (mc.thePlayer.isMoving()) {
                            e.setPosZ(0.21999998688697815);
                            mc.thePlayer.motionY = 0.21999998688697815;
                            moveSpeed = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? MoveUtils.getBaseMoveSpeed() * speed.getVal() : MoveUtils.getBaseMoveSpeed() * (speed.getVal() * 1.5);
                        }
                    }
                    EntityHelper.setMotion(moveSpeed);
                }
            case "VanillaLowHop":
                mc.thePlayer.setSprinting(true);
                if ((mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically)) {
                    if (mc.thePlayer.isMoving()){
                        Timer.timerSpeed = 1f;
                        e.setPosZ(0.21999998688697815);
                        mc.thePlayer.motionY = 0.21999998688697815;
                        moveSpeed = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? MoveUtils.getBaseMoveSpeed() * speed.getVal() : MoveUtils.getBaseMoveSpeed() * (speed.getVal()*1.5);
                    }
                }

                EntityHelper.setMotion(moveSpeed);
                break;
            case "Vulcan":
                Timer.timerSpeed = 0.4f;

                if (!mc.thePlayer.isMoving()) return;

                if (mc.thePlayer.ticksExisted % 3 == 0 || mc.thePlayer.ticksExisted % 4 == 0 || mc.thePlayer.ticksExisted % 5 == 0 || mc.thePlayer.ticksExisted % 7 == 0 || mc.thePlayer.ticksExisted % 8 == 0 || mc.thePlayer.ticksExisted % 9 == 0 || mc.thePlayer.ticksExisted % 10 == 0) {
                    Timer.timerSpeed = 2.8f;
                    isChangedVelocity = true;
                } else {
                    isChangedVelocity = false;
                }

                /*

                if ((mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically)) {
                    if (mc.thePlayer.isMoving()){
                        yawHypixel = mc.thePlayer.rotationYaw;
                        hypixelMultiplier = 1.1;
                        e.setPosZ(0.41999998688697815;
                        mc.thePlayer.motionY = 0.41999998688697815;
                        moveSpeed = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? MoveUtils.getBaseMoveSpeed() * 0.4 : MoveUtils.getBaseMoveSpeed() * 0.7;
                    }
                    this.resetLastDist = true;
                } else {
                    if (this.resetLastDist) {
                        this.moveSpeed -= 0.6600000262260437 * (this.moveSpeed - MoveUtils.getBaseMoveSpeed());
                        this.resetLastDist = false;
                    }
                    EntityHelper.setMotion(this.moveSpeed);
                }
                if(mc.gameSettings.keyBindForward.pressed && mc.gameSettings.keyBindLeft.pressed){
                    KillAuraHelper.setRotations(e,(float) MathHelper.round(mc.thePlayer.rotationYaw - 45,25),0);
                } else if(mc.gameSettings.keyBindForward.pressed && mc.gameSettings.keyBindRight.pressed){
                    KillAuraHelper.setRotations(e,(float) MathHelper.round(mc.thePlayer.rotationYaw + 45,25),0);
                } else if(mc.gameSettings.keyBindBack.pressed && mc.gameSettings.keyBindLeft.pressed) {
                    KillAuraHelper.setRotations(e, (float) MathHelper.round(mc.thePlayer.rotationYaw - 135, 25),0);
                } else if(mc.gameSettings.keyBindBack.pressed && mc.gameSettings.keyBindRight.pressed) {
                    KillAuraHelper.setRotations(e, (float) MathHelper.round(mc.thePlayer.rotationYaw + 135, 25),0);
                } else if(mc.gameSettings.keyBindForward.pressed){
                    KillAuraHelper.setRotations(e,(float) MathHelper.round(mc.thePlayer.rotationYaw,25),0);
                } else if(mc.gameSettings.keyBindLeft.pressed){
                    KillAuraHelper.setRotations(e,(float) MathHelper.round(mc.thePlayer.rotationYaw - 90,25),0);
                } else if(mc.gameSettings.keyBindRight.pressed){
                    KillAuraHelper.setRotations(e,(float) MathHelper.round(mc.thePlayer.rotationYaw + 90,25),0);
                } else if(mc.gameSettings.keyBindBack.pressed){
                    KillAuraHelper.setRotations(e,(float) MathHelper.round(mc.thePlayer.rotationYaw - 180,25),0);
                }

                 */

                break;
            case "Invaded":
                if ((mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically)) {
                    if (mc.thePlayer.isMoving()){
                        yawHypixel = mc.thePlayer.rotationYaw;
                        Timer.timerSpeed = 2.2f;
                        hypixelMultiplier = 1.1;
                        e.setPosZ(0.41999998688697815);
                        mc.thePlayer.motionY = 0.41999998688697815;
                        moveSpeed = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? MoveUtils.getBaseMoveSpeed() * 0.4 : MoveUtils.getBaseMoveSpeed() * 0.7;
                    }
                    this.resetLastDist = true;
                } else {
                    if (this.resetLastDist) {
                        moveSpeed -= 0.6600000262260437 * (moveSpeed - MoveUtils.getBaseMoveSpeed());
                        this.resetLastDist = false;
                    }
                    //EntityHelper.setMotion(this.moveSpeed);
                }
                if(mc.gameSettings.keyBindForward.pressed && mc.gameSettings.keyBindLeft.pressed){
                    KillAuraHelper.setRotations(e,(float) MathHelper.round(mc.thePlayer.rotationYaw - 45,25),0);
                } else if(mc.gameSettings.keyBindForward.pressed && mc.gameSettings.keyBindRight.pressed){
                    KillAuraHelper.setRotations(e,(float) MathHelper.round(mc.thePlayer.rotationYaw + 45,25),0);
                } else if(mc.gameSettings.keyBindBack.pressed && mc.gameSettings.keyBindLeft.pressed) {
                    KillAuraHelper.setRotations(e, (float) MathHelper.round(mc.thePlayer.rotationYaw - 135, 25),0);
                } else if(mc.gameSettings.keyBindBack.pressed && mc.gameSettings.keyBindRight.pressed) {
                    KillAuraHelper.setRotations(e, (float) MathHelper.round(mc.thePlayer.rotationYaw + 135, 25),0);
                } else if(mc.gameSettings.keyBindForward.pressed){
                    KillAuraHelper.setRotations(e,(float) MathHelper.round(mc.thePlayer.rotationYaw,25),0);
                } else if(mc.gameSettings.keyBindLeft.pressed){
                    KillAuraHelper.setRotations(e,(float) MathHelper.round(mc.thePlayer.rotationYaw - 90,25),0);
                } else if(mc.gameSettings.keyBindRight.pressed){
                    KillAuraHelper.setRotations(e, (float) MathHelper.round(mc.thePlayer.rotationYaw + 90, 25), 0);
                } else if (mc.gameSettings.keyBindBack.pressed) {
                    KillAuraHelper.setRotations(e, (float) MathHelper.round(mc.thePlayer.rotationYaw - 180, 25), 0);
                }
                break;
            case "Vanilla":
                moveSpeed = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? MoveUtils.getBaseMoveSpeed() * speed.getVal() : MoveUtils.getBaseMoveSpeed() * (speed.getVal() * 1.5);
                EntityHelper.setMotion(moveSpeed);
                break;
            case "Watchdog":
                mc.thePlayer.setSprinting(true);
                if (e.isPre()) {
                    double var1 = getBaseSpeed() * 0.945278;
                    hDist.set(var1);
                    KillAuraHelper.setRotationsSilent(e, (float) getDirection(), mc.thePlayer.rotationPitch);
                }
                if (e.isPost() && mc.thePlayer.ticksExisted % 3 == 0) {
                    double speed = doFriction(hDist);
                    moveSpeed = getBaseSpeed() * 0.9624;

                    EntityHelper.setMotion(getBaseSpeed() * 1.0094456056f);
                }
                break;
            case "Gwen":
                KillAuraHelper.setRotations(e, (float) getDirection(), mc.thePlayer.rotationPitch);
                break;
            case "GhostlyOnGround":
                if (!mc.thePlayer.isMoving()) return;

                if (mc.thePlayer.ticksExisted % 5 == 0) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX + x2, mc.thePlayer.posY, mc.thePlayer.posZ + z2);
                }
                break;
        }

    }

    @Subscribe
    public void onPacket(PacketEvent e){
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        double x2 = -Math.sin(yaw) * speed.getVal();
        double z2 = Math.cos(yaw) * speed.getVal();

        switch (mode.getMode()){
            case "HypixelSlime":
            case "HypixelSlimeLow":
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    if (mc.thePlayer.getEntityId() == ((S12PacketEntityVelocity) e.getPacket()).getEntityID()) {
                        e.setCancelled(true);
                        isChangedVelocity = true;
                    }
                }
                break;
            case "Vulcan":
                if(isChangedVelocity) {
                    if (e.isIncoming()) {
                        e.setCancelled(true);
                        packets.add(e.getPacket());
                    }
                }else {
                    if(!packets.isEmpty()){
                        for(Packet p : packets){
                            PacketHelper.sendPacketNoEvent(p);
                        }
                    }
                }
                break;
        }
    }

    public double doFriction(AtomicDouble hdist) {
        double value = hdist.get();
        hdist.set(value - value / 159);
        return Math.max(hdist.get(), getVerusBaseSpeed());
    }

    private double getVerusBaseSpeed() {
        double base = 0.2865;
        if (mc.thePlayer.isPotionActive(1)) {
            base *= 1.0 + 0.0495 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return base;
    }

    private float getBaseSpeed() {
        float baseSpeed = 0.2873F;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amp = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0F + 0.2F * (amp + 1);
        }
        return baseSpeed;
    }

    private double getMotion(float baseMotionY) {
        Potion potion = Potion.jump;
        if (mc.thePlayer.isPotionActive(potion)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(potion).getAmplifier();
            baseMotionY += (amplifier + 1) * 0.1F;
        }

        return baseMotionY;
    }

    private void setMotion(double moveSpeed) {
        //EntityLivingBase entity = KillAura.currentTarget;
        EntityLivingBase entity = KillAura.target;
        MovementInput movementInput = mc.thePlayer.movementInput;

        double moveForward = MovementInput.moveForward;
        double moveStrafe = MovementInput.moveStrafe;
        double rotationYaw = mc.thePlayer.rotationYaw;

        if (moveForward == 0.0D && moveStrafe == 0.0D) {
            mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
        } else {
            if (moveStrafe > 0) {
                moveStrafe = 1;
            } else if (moveStrafe < 0) {
                moveStrafe = -1;
            }
            if (moveForward != 0.0D) {
                if (moveStrafe > 0.0D) {
                    rotationYaw += (moveForward > 0.0D ? -45 : 45);
                } else if (moveStrafe < 0.0D) {
                    rotationYaw += (moveForward > 0.0D ? 45 : -45);
                }
                moveStrafe = 0.0D;
                if (moveForward > 0.0D) {
                    moveForward = 1.0D;
                } else if (moveForward < 0.0D) {
                    moveForward = -1.0D;
                }
            }
            double cos = Math.cos(Math.toRadians(rotationYaw + 90.0F));
            double sin = Math.sin(Math.toRadians(rotationYaw + 90.0F));
            mc.thePlayer.motionX = moveForward * moveSpeed * cos
                    + moveStrafe * moveSpeed * sin;
            mc.thePlayer.motionZ = moveForward * moveSpeed * sin
                    - moveStrafe * moveSpeed * cos;
        }
    }

    public static double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;

        if(mc.thePlayer.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if(mc.thePlayer.moveForward < 0F)
            forward = -0.5F;
        else if(mc.thePlayer.moveForward > 0F)
            forward = 0.5F;

        if(mc.thePlayer.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if(mc.thePlayer.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return rotationYaw;
    }

    private void TP(MoveRawEvent event, double speed, double y) {
        float yaw = mc.thePlayer.rotationYaw;
        final float forward = mc.thePlayer.moveForward;
        final float strafe = mc.thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        int var1 = (forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45);
        if (strafe < 0.0f) {
            yaw += var1;
        }
        if (strafe > 0.0f) {
            yaw -= var1;
        }
        float direction =  yaw * 0.017453292f;

        final double posX = mc.thePlayer.posX;
        final double posY = mc.thePlayer.posY;
        final double posZ = mc.thePlayer.posZ;
        final double raycastFirstX = -Math.sin(direction);
        final double raycastFirstZ = Math.cos(direction);
        final double raycastFinalX = raycastFirstX * speed;
        final double raycastFinalZ = raycastFirstZ * speed;
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX + raycastFinalX, posY + y, posZ + raycastFinalZ, mc.thePlayer.onGround));
        mc.thePlayer.setPosition(posX + raycastFinalX, posY + y, posZ + raycastFinalZ);
    }

    public void setMotionPartialStrafe(float friction, float strafeComponent) {
        float remainder = 1F - strafeComponent;
        if (mc.thePlayer.onGround) {
            setMotion(friction);
        } else {
            mc.thePlayer.motionX *= strafeComponent;
            mc.thePlayer.motionZ *= strafeComponent;
            //setFriction(friction * remainder);
        }
    }
}
