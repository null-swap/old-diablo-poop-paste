package cc.diablo.render.implementations;

import cc.diablo.render.Shader;
import cc.diablo.render.ShaderProgram;
import org.lwjgl.opengl.GL20;

public class NewMenuShader extends Shader {
    public NewMenuShader(int pass) {
        super(new ShaderProgram("fragment/space.frag"));
        this.pass = pass;
    }

    @Override
    public void setUniforms() {
        GL20.glUniform1f(shaderProgram.getUniform("time"), pass / 100f);
        GL20.glUniform2f(shaderProgram.getUniform("resolution"), mc.displayWidth, mc.displayHeight);
    }
}
