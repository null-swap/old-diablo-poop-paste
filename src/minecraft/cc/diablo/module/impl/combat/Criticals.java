package cc.diablo.module.impl.combat;

import cc.diablo.event.impl.PacketEvent;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.module.impl.movement.Fly;
import cc.diablo.module.impl.movement.Speed;
import cc.diablo.setting.impl.ModeSetting;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S2APacketParticles;
import org.lwjgl.input.Keyboard;

public class Criticals extends Module {
    public ModeSetting critMode = new ModeSetting("Cricials mode", "Watchdog", "Watchdog", "Packet", "Packet2");
    public NumberSetting speed = new NumberSetting("Ticks", 6, 0, 20, 1);
    public int airTime, waitTicks;

    public Criticals() {
        super("Criticals", "Spoof body slamming faggots", Keyboard.KEY_NONE, Category.Combat);
        this.addSettings(critMode,speed);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        //this.setDisplayName("Criticals\2477 " + this.critMode.getMode() + " | " + speed.getVal());

        this.setDisplayName("Criticals\2477 " + this.critMode.getMode());
        if(!ModuleManager.getModule(Criticals.class).isToggled()){
            switch (critMode.getMode()) {
                case "Watchdog":
                            if (event.getPacket() instanceof S2APacketParticles || event.getPacket().toString().contains("S2APacketParticles")) {
                                break;
                            }
                            if (event.getPacket() instanceof C02PacketUseEntity && !(event.getPacket() instanceof S2APacketParticles) && !(event.getPacket() instanceof C0APacketAnimation)) {
                                C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();
                                if (packet.getAction() == C02PacketUseEntity.Action.ATTACK && mc.thePlayer.isCollidedVertically && this.hurtTime(packet.getEntityFromWorld(Minecraft.theWorld))) {
                                    if (!(ModuleManager.getModule(Speed.class).toggled || ModuleManager.getModule(Fly.class).toggled)) {
                                        critHypixel();
                                    }

                                }
                            }
                    break;
                case "Packet":
                    if (event.getPacket() instanceof S2APacketParticles || event.getPacket().toString().contains("S2APacketParticles")) {
                        break;
                    }
                    if (event.getPacket() instanceof C02PacketUseEntity && !(event.getPacket() instanceof S2APacketParticles) && !(event.getPacket() instanceof C0APacketAnimation)) {
                        C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();
                        if (packet.getAction() == C02PacketUseEntity.Action.ATTACK && mc.thePlayer.isCollidedVertically && this.hurtTime(packet.getEntityFromWorld(Minecraft.theWorld))) {
                            if (ModuleManager.getModule(Speed.class).toggled || ModuleManager.getModule(Fly.class).toggled) {
                                break;
                            }
                            crit();
                        }
                    }
                    break;
                case "Packet2":
                    if (event.getPacket() instanceof C02PacketUseEntity) {
                        C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();
                        if (packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                            airTime = 3 + 1;
                            PacketHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(0, 0.1232225, 0, false));
                            PacketHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(0, 1.0554E-9, 0, false));
                            PacketHelper.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(0, 0, 0, true));
                        }
                    }
                    break;
            }
        }
    }

    private boolean hurtTime(Entity entity) {
        return entity != null && entity.hurtResistantTime <= 14;
    }

    public static void crit() {

        double[] array3;
        double[] array2;
            final double[] array = array2 = (array3 = new double[4]);
            array[0] = 0.04000000074505806;
            array[1] = 0.0015999999595806003;
            array[2] = 0.029999999329447746;
            array[3] = 0.0015999999595806003;
        final double[] array5 = array2;
        for (int length = array3.length, i = 0; i < length; ++i) {
            final double offset = array2[i];
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + offset, Minecraft.getMinecraft().thePlayer.posZ, false));
        }
    }

    public static void critHypixel() {
        double[] array3;
        double[] array2;
            final double[] array = array2 = (array3 = new double[4]);
            array[0] = 0.0523043000074505806;
            array[1] = 0.0018999999595806003;
            array[2] = 0.0329124399999329447746;
            array[3] = 0.0013234999999595806003;
        final double[] array5 = array2;
        final double offset = array2[3];
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, false));
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + offset, Minecraft.getMinecraft().thePlayer.posZ, false));
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, false));

        /*
        for (int length = array3.length, i = 0; i < length; ++i) {
            final double offset = array2[i];
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + offset, Minecraft.getMinecraft().thePlayer.posZ, false));
        }

         */
    }
}
