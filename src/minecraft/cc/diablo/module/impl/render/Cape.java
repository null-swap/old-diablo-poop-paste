package cc.diablo.module.impl.render;

import cc.diablo.setting.impl.ModeSetting;
import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import jdk.nashorn.internal.runtime.logging.Logger;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class Cape extends Module {
    public ModeSetting mode = new ModeSetting("Cape","Diablo","Diablo","Diablo2","Hot");
    public ResourceLocation oldLocation;
    int countCape;

    public Cape(){
        super("Cape", "If you dont know what a cape is your fucking retarded", Keyboard.KEY_NONE, Category.Render);
        this.addSettings(mode);
    }

    @Override
    public void onEnable(){
        countCape = 0;
        oldLocation = mc.thePlayer.getLocationCape();
        super.onEnable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
        this.setDisplayName("Cape\2477 " + this.mode.getMode());
        switch (mode.getMode()) {
            case "Diablo":
                mc.thePlayer.setLocationOfCape(new ResourceLocation("Client/images/DiabloCapeNew.png"));
                break;
            case "Diablo2":
                StringBuilder countStr = new StringBuilder();
                //if(countCape )
                //countStr.append()
                mc.thePlayer.setLocationOfCape(new ResourceLocation("Client/images/rainbow/diablo"+countStr+".png"));
                break;
            case "Hot":
                mc.thePlayer.setLocationOfCape(new ResourceLocation("Client/images/hotcape.png"));

        }
    }

    @Override
    public void onDisable(){
        mc.thePlayer.setLocationOfCape(oldLocation);
        super.onDisable();
    }
}
