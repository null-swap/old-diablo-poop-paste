package cc.diablo.render.notification;

import cc.diablo.Main;
import cc.diablo.event.impl.OverlayEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.Stopwatch;
import cc.diablo.helpers.TimerUtil;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.module.impl.player.Scaffold;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;

import java.awt.*;

public class Notification {
    public TimerUtil timer = new TimerUtil();
    public String text;
    public long delay;
    public double x,y,x2,y2,width,height;

    public Notification(String text, long delay){
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.text = text;
        this.delay = delay;
        this.width = 200;
        this.height = 27;
        this.x = sr.getScaledWidth() - 3 - this.width;
        this.y = sr.getScaledHeight() - 7 - this.height;
        this.x2 = sr.getScaledWidth() - 3;
        this.y2 = sr.getScaledHeight() - 7;
        Main.getInstance().getEventBus().register(this);
        this.timer.reset();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
        if(timer.reach(delay)){
            NotificationManager.notifications.remove(this);
            this.timer.reset();
        }
    }


}
