package cc.diablo.render.notification;

import cc.diablo.Main;
import cc.diablo.event.impl.OverlayEvent;
import cc.diablo.font.TTFFontRenderer;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.helpers.render.ColorHelper;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class NotificationManager {
    public static ArrayList<Notification> notifications = new ArrayList<Notification>();

    public static void SendNotification(String text, long time){
        ChatHelper.addChat("sent");
        NotificationManager.notifications.add(new Notification(text,time));
    }

    public static void drawNotifications(){
        for (Notification n : notifications) {
            float width2 = (float) (n.width);
            float var37 = (float) (n.width);
            float var42 = (float) (double) Math.round((double) (n.timer.time() * width2) / width2);
            float var46 = (float) (width2 / n.delay);
            float var48 = var42 * var46;
            float var51 = (var37) / width2;

            GL11.glPushMatrix();
            //GL11.glEnable(3089);
            GL11.glScissor((int) n.x,(int) n.y,(int) n.x2,(int) n.y2 + 105);
            RenderUtils.drawRect(n.x,n.y,n.x2,n.y2,RenderUtils.transparency(new Color(27, 27, 27).getRGB(), 0.6f));
            RenderUtils.drawRect(n.x,n.y2 - 1,n.x2 - var48 * var51,n.y2, ColorHelper.getColor( 150));
            TTFFontRenderer fr = Main.getInstance().getFontManager().getFont("clean 18");
            fr.drawStringWithShadow(n.text,(float) n.x2 - fr.getWidth(n.text) - 2,(float) n.y2 - 14,-1);
            //GL11.glDisable(3089);
            GL11.glPopMatrix();
        }
    }
}
