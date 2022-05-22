package cc.diablo.login;

import cc.diablo.Main;
import cc.diablo.helpers.render.ColorHelper;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.render.GuiMainMenu;
import cc.diablo.render.Shader;
import cc.diablo.render.implementations.LoginShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

public class GuiAuthentication extends GuiScreen {
    private GuiTextField uid;
    public Shader shader;

    public GuiAuthentication(){
        shader = new LoginShader(0);
    }

    public void updateScreen() {
        this.uid.updateCursorCounter();
    }

    public void onGuiClosed() {
        //Keyboard.enableRepeatEvents(false);
    }

    protected void keyTyped(final char par1, final int par2) {
        this.uid.textboxKeyTyped(par1, par2);
    }

    protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
        super.mouseClicked(par1, par2, par3);
        this.uid.mouseClicked(par1, par2, par3);
    }

    public void initGui() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        this.buttonList.add(new GuiButton(0, scaledResolution.getScaledWidth() / 2 - 100, scaledResolution.getScaledHeight() / 2 + 24, 200, 20, "Login"));
        this.uid = new GuiTextField(0, this.fontRendererObj, scaledResolution.getScaledWidth() / 2 - 100, scaledResolution.getScaledHeight() / 2, 200, 20);
    }

    public void drawScreen(final int i, final int j, final float f) {
        this.drawDefaultBackground();
        shader.render(this.width,this.height);
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        double x = scaledResolution.getScaledWidth() / 2 - 110;
        double y = scaledResolution.getScaledHeight() / 2 - 50;
        double x2 = scaledResolution.getScaledWidth() / 2 + 110;
        double y2 = scaledResolution.getScaledHeight() / 2 + 50;


        RenderUtils.drawRect(x - 1,y - 3,x2 + 1,y2 + 1, RenderUtils.transparency(new Color(37,37,37).getRGB(),0.6f)); //Blur
        RenderUtils.drawRect(x,y,x2,y2, RenderUtils.transparency(new Color(45, 45, 45, 255).getRGB(),0.8f)); //Main background
        RenderUtils.drawRect(x,y,x2,y2, RenderUtils.transparency(new Color(35, 35, 35, 255).getRGB(),0.8f));
        RenderUtils.drawRect(x ,y - 2,x2,y,RenderUtils.transparency(ColorHelper.getColor(0),0.8f)); //Color!


        uid.drawTextBox();
        super.drawScreen(i, j, f);
    }

    public static String   getHWID() throws NoSuchAlgorithmException {
        String hwid = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("NUMBER_OF_PROCESSORS") + System.getenv("PROCESSOR_REVISION") + System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_LEVEL");
        return new String(Base64.getEncoder().encode(MessageDigest.getInstance("SHA-256").digest(hwid.getBytes(StandardCharsets.UTF_8))));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            try {
                System.out.println(getHWID());

                String everything = uid.getText();
                System.out.println(everything);

                String str1 = null;
                String str2 = null;
                String str3 = null;
                String str5 = null;

                URL u = new URL("https://diablo.wtf/api/utils/gethwid.php?uid=" + everything);
                URLConnection uc = u.openConnection();
                uc.connect();
                uc = u.openConnection();
                uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                    str1 = inputLine;
                }
                in.close();


                URL u2 = new URL("https://diablo.wtf/api/utils/getusername.php?uid=" + everything);
                URLConnection uc2 = u2.openConnection();
                uc2.connect();
                uc2 = u2.openConnection();
                uc2.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                BufferedReader in2 = new BufferedReader(new InputStreamReader(uc2.getInputStream()));
                String inputLine2;
                while ((inputLine2 = in2.readLine()) != null) {
                    System.out.println(inputLine2);
                    str2 = inputLine2;
                }
                in2.close();


                URL u3 = new URL("https://diablo.wtf/api/utils/getsuspended.php?uid=" + everything);
                URLConnection uc3 = u3.openConnection();
                uc3.connect();
                uc3 = u3.openConnection();
                uc3.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                BufferedReader in3 = new BufferedReader(new InputStreamReader(uc3.getInputStream()));
                String inputLine3;
                while ((inputLine3 = in3.readLine()) != null) {
                    System.out.println(inputLine3);
                    str3 = inputLine3;
                }
                in3.close();


                URL u4 = new URL("https://diablo.wtf/api/utils/getstablejarversion.php");
                URLConnection uc4 = u4.openConnection();
                uc4.connect();
                uc4 = u4.openConnection();
                uc4.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                BufferedReader in4 = new BufferedReader(new InputStreamReader(uc4.getInputStream()));
                String inputLine4;
                while ((inputLine4 = in4.readLine()) != null) {
                    System.out.println(4);
                    Main.serverVersion = inputLine4;
                }
                in4.close();

                URL u5 = new URL("https://diablo.wtf/api/utils/getdiscordid.php?username=" + str2);
                URLConnection uc5 = u5.openConnection();
                uc5.connect();
                uc5 = u5.openConnection();
                uc5.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                BufferedReader in5 = new BufferedReader(new InputStreamReader(uc5.getInputStream()));
                String inputLine5;
                while ((inputLine5 = in5.readLine()) != null) {
                    System.out.println(inputLine5);
                    str5 = inputLine5;
                }
                in5.close();


                if (Objects.equals(str3, "false")) {
                    if (str5 != null) {
                        if (Objects.equals(str1, getHWID())) {
                            System.out.println("Authenticated");
                            Main.username = str2;
                            Main.uid = everything;
                            Minecraft.getMinecraft().currentScreen = new GuiMainMenu();
                        } else {
                            JOptionPane.showMessageDialog(null, "HWID Check failed, contact support for HWID reset.");
                            System.exit(0);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Your discord is not linked. You must link your discord inorder to launch");
                        System.exit(0);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Account Locked. Contact support");
                    System.exit(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
