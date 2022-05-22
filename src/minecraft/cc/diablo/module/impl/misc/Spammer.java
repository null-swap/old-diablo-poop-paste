package cc.diablo.module.impl.misc;

import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.MathHelper;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.helpers.Stopwatch;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.lwjgl.input.Keyboard;

public class Spammer extends Module {
    public Stopwatch timer = new Stopwatch();
    private final NumberSetting delay = new NumberSetting("Delay (MS)",3000,100,30000,50);
    public Spammer(){
        super("Spammer","Spams a message in chat", Keyboard.KEY_NONE, Category.Misc);
        this.addSettings(delay);
    }


    @Override
    public void onEnable(){
        timer.reset();
        super.onEnable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
        if(timer.hasReached((long) delay.getVal())){
            //PacketHelper.sendPacketNoEvent(new C01PacketChatMessage(getMessage()));
            PacketHelper.sendPacketNoEvent(new C01PacketChatMessage("Beamed by diablo <dot> wtf " + MathHelper.getRandInt(1,1000)));
            timer.reset();
        }
    }

    public String getMessage(){
        String[] messages = {"get good get diablo","dm Vince#7777 to buy","bagelclient <dot> xyz ontop","only 15$","zamn you just got rolled by diablo","diablo ontop","diablo > all","diablo owns you and all"};
        return messages[MathHelper.getRandInt(1,messages.length)]     ;
    }
}
