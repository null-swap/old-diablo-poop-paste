package cc.diablo.module.impl.misc;

import cc.diablo.Main;
import cc.diablo.event.impl.PacketEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.module.impl.combat.KillAura;
import cc.diablo.setting.impl.ModeSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Keyboard;

import javax.sound.sampled.*;
import java.io.IOException;

public class HitSound extends Module {
    public ModeSetting mode = new ModeSetting("Mode","Skeet","Skeet","Call of Duty");
    public HitSound(){
        super("HitSound","Plays a aids sounds on hit", Keyboard.KEY_NONE, Category.Misc);
        this.addSettings(mode);
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
        if (KillAura.target != null && KillAura.target.hurtTime > 9 ){
            doSound(KillAura.target);
        }
    }

    public void doSound(EntityLivingBase target) {
        double x = target.posX; double y = target.posY; double z = target.posZ;

        switch (mode.getMode()) {
            case "Call Of Duty":
                ChatHelper.addChat("TECHNO DOESNT KNOW WHAT A PENIS IS");
                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("Client/audio/hitsound/callofduty.ogg"), (float) x, (float) y, (float) z));
                break;
            case "Skeet":
                if(mc.thePlayer.ticksExisted % 5 == 0) {
                   // String message = "Materweal gowrl!!!! diablo <dot> wtf ";
                    String message = "vince > you on jah Vince # 7777 diablo <dot> wtf ";
                            PacketHelper.sendPacketNoEvent(new C01PacketChatMessage(message + RandomStringUtils.random(9, "abcdefghijklmnopqrstuvwxyz1234567890")));

                }
                try {
                    AudioInputStream audioInputStream = null;
                    try {
                        audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("Client/audio/hitsound/skeet.ogg"));
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }

                ChatHelper.addChat("TECHNO DOESNT KNOW WHAT A PENIS IS");
                mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("Client/audio/hitsound/skeet.ogg"), (float) x, (float) y, (float) z));
                break;
        }
    }
}
