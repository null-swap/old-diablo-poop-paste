package cc.diablo.render.animations;

import cc.diablo.helpers.render.RenderUtils;
import org.lwjgl.opengl.GL11;

public class AnimationScissor {

    public long startTime;
    public double speed;
    public double startX;
    public double startY;
    public double endX;
    public double endY;

    public AnimationScissor(double speed, double startX, double startY, double endX, double endY) {
        this.speed = speed;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.startTime = System.currentTimeMillis();
    }

    public void renderStart(){
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.prepareScissorBox(startX, startY, endX, endY);
    }
    public void renderEnd(){
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
