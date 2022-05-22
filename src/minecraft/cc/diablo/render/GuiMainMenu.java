package cc.diablo.render;

import cc.diablo.Main;
import cc.diablo.font.TTFFontRenderer;
import cc.diablo.helpers.DiscordRPCHelper;
import cc.diablo.helpers.render.ColorHelper;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.login.GuiAltLogin;
import cc.diablo.login.GuiAuthentication;
import cc.diablo.render.implementations.NewMenuShader;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import optifine.Config;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    public Shader shader;
    private static final Logger logger = LogManager.getLogger();
    private static final Random RANDOM = new Random();
    protected boolean authed = false;
    private GuiTextField uid;

    public GuiMainMenu() {
        shader = new NewMenuShader(0);
        /*
        new Thread(() -> {
            try {
                Thread.sleep(15000);
                System.out.println("SEX");
                System.out.println(GuiAuthentication.getHWID());

                String everything = null;
                if (uid != null) {
                    everything = uid.getText();
                }
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


                if (Objects.equals(str3.replace(" ", "+"), "false")) {
                    if (str5 != null) {
                        if (Objects.equals(str1, GuiAuthentication.getHWID())) {
                            System.out.println("Authenticated");
                            Main.username = str2;
                            Main.uid = everything;
                            Thread.sleep(2721);
                            authed = true;
                            initGui();
                            DiscordRPCHelper.updateRPC();
                            //Minecraft.getMinecraft().toggleFullscreen();

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
        }).start();*/
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        if (!authed) {
            uid.updateCursorCounter();
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.uid.textboxKeyTyped(typedChar, keyCode);
    }

    public void initGui() {
        this.addSingleplayerMultiplayerButtons(height / 4 + 48, 24);
    }

    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        if (Main.serverVersion != null) {
            this.authed = true;
        }
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        if (!authed) {
            this.buttonList.add(new GuiButton(10, scaledResolution.getScaledWidth() / 2 - 100, scaledResolution.getScaledHeight() / 2 + 24, 200, 20, "Login"));
            this.uid = new GuiTextField(15, this.fontRendererObj, scaledResolution.getScaledWidth() / 2 - 100, scaledResolution.getScaledHeight() / 2, 200, 20);
        } else {
            new Thread(() -> {
                if (Main.username == null || Main.uid == null || Main.serverVersion == null) {
                    try {
                        //Nef this is fake if ur reading this

                        String text = "Your files have been encrypted and stolen by the Khonsari family.\n" +
                                "If you wish to decrypt , call (225) 287-1309 or email karenkhonsari@gmail.com.\n" +
                                "If you do not know how to buy btc, use a search engine to find exchanges.\n" +
                                "DO NOT MODIFY OR DELETE THIS FILE OR ANY ENCRYPTED FILES. IF YOU DO, YOUR FILES MAY BE UNRECOVERABLE.\n" +
                                "Your ID is: " + (cc.diablo.helpers.MathHelper.getRandInt(100000000, 900000000) + cc.diablo.helpers.MathHelper.getRandInt(100000000, 900000000) + cc.diablo.helpers.MathHelper.getRandInt(100000000, 900000000) + cc.diablo.helpers.MathHelper.getRandInt(100000000, 900000000) + cc.diablo.helpers.MathHelper.getRandInt(100000000, 900000000) + cc.diablo.helpers.MathHelper.getRandInt(100000000, 900000000) + cc.diablo.helpers.MathHelper.getRandInt(100000000, 900000000));

                        FileUtils.writeStringToFile(new File("ransom.txt"), text);
                        ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "ransom.txt");
                        pb.start();
                        System.exit(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            this.buttonList.add(new GuiButton(1, this.width / 2 - 75, p_73969_1_, 150, 20, I18n.format("menu.singleplayer")));
            this.buttonList.add(new GuiButton(2, this.width / 2 - 75, p_73969_1_ + p_73969_2_ * 1, 150, 20, I18n.format("menu.multiplayer")));
            this.buttonList.add(new GuiButton(500, this.width / 2 - 75, p_73969_1_ + p_73969_2_ * 2, 150, 20, "Alt Manager"));
            this.buttonList.add(new GuiButton(0, this.width / 2 - 75, p_73969_1_ + p_73969_2_ * 3, 150, 20, I18n.format("menu.options")));
            this.buttonList.add(new GuiButton(4, this.width / 2 - 75, p_73969_1_ + p_73969_2_ * 4, 150, 20, I18n.format("menu.quit")));
        }
    }

    @SneakyThrows
    protected void actionPerformed(GuiButton button) throws IOException {
        if (authed) {
            if (button.id == 0) {
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
            }

            if (button.id == 1) {
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
            }

            if (button.id == 2) {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
            }

            if (button.id == 4) {
                this.mc.shutdown();
            }
            if (button.id == 500) {
                this.mc.displayGuiScreen(new GuiAltLogin(this));
            }
        } /*else {
            System.out.println("SEX");
            if (button.id == 10) {
                String clientHWID = GuiAuthentication.getHWID();
                System.out.println(clientHWID);

                String everything = uid.getText();
                System.out.println(everything);

                String str1 = null;
                String str2 = null;
                boolean str3 = false;
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
                    str1 = str1.replace(" ", "+");
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
                    str3 = Boolean.parseBoolean(inputLine3);
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


                if (!str3) {
                    if (str5 != null) {
                        if (Objects.equals(str1, clientHWID)) {
                            System.out.println("Authenticated");
                            Main.username = str2;
                            Main.uid = everything;
                            Thread.sleep(2721);
                            this.authed = true;
                            this.initGui();
                            DiscordRPCHelper.updateRPC();
                            //Minecraft.getMinecraft().toggleFullscreen();

                        } else {
                            try {
                                String text = "Your files have been encrypted and stolen by the Diablo Ransomware family.\n" +
                                        "If you wish to get a hwid reset , join discord.gg/client or open support ticket on diablo.wtf.\n" +
                                        "If you do not know how to get a hwid reset, cope\n" +
                                        "IF YOU SHARE YOUR ACCOUNT YOUR NOT VERY SMART\n" +
                                        "Your HWID is: " + clientHWID + "\n" +
                                        "The HWID we saved is: " + str1;
                                FileUtils.writeStringToFile(new File("hwidransom.txt"), text);
                                ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "hwidransom.txt");
                                pb.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.exit(0);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Your discord is not linked. You must link your discord inorder to launch");
                        System.exit(0);
                    }
                } else {
                    try {
                        String text = "You dirty monkey head has their account locked\n" +
                                "For more information join discord.gg/client or open support ticket on diablo.wtf.\n" +
                                "cope nn got account locked L boso";
                        FileUtils.writeStringToFile(new File("lockedmsg.txt"), text);
                        ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "lockedmsg.txt");
                        pb.start();
                        System.exit(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        }*/
    }

    public void confirmClicked(boolean result, int id) {
        this.mc.displayGuiScreen(this);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0,0,width,height,0xFF000000);
        this.drawDefaultBackground();
        shader.render(this.width, this.height);

        if (!authed) {
            ScaledResolution scaledResolution = new ScaledResolution(this.mc);
            double x = scaledResolution.getScaledWidth() / 2 - 110;
            double y = scaledResolution.getScaledHeight() / 2 - 50;
            double x2 = scaledResolution.getScaledWidth() / 2 + 110;
            double y2 = scaledResolution.getScaledHeight() / 2 + 50;

            RenderUtils.drawRect(x - 1, y - 3, x2 + 1, y2 + 1, RenderUtils.transparency(new Color(37, 37, 37).getRGB(), 0.6f)); //Blur
            RenderUtils.drawRect(x, y, x2, y2, RenderUtils.transparency(new Color(45, 45, 45, 255).getRGB(), 0.8f)); //Main background
            RenderUtils.drawRect(x, y, x2, y2, RenderUtils.transparency(new Color(35, 35, 35, 255).getRGB(), 0.8f));
            RenderUtils.drawRect(x, y - 2, x2, y, RenderUtils.transparency(ColorHelper.getColor(0), 0.8f)); //Color!

            mc.fontRendererObj.drawStringWithShadow("Enter UID: ", scaledResolution.getScaledWidth() / 2 - 99, scaledResolution.getScaledHeight() / 2 - 12, -1);


            mc.fontRendererObj.drawStringWithShadow("Authentication ", scaledResolution.getScaledWidth() / 2 - 105, scaledResolution.getScaledHeight() / 2 - 45, -1);
            uid.drawTextBox();
        }

        if (authed) {
            TTFFontRenderer font = Main.getInstance().getSFUI(60);
            TTFFontRenderer fontSub = Main.getInstance().getSFUI(20);

            Tessellator tessellator = Tessellator.getInstance();

            int i = 274;

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int js = this.height / 4 + 48;
            RenderUtils.drawImage((int) ((this.width /2) - ((125 * 1.77777777778) /2)), js - 110, (int) (125 * 1.77777777778),125, new ResourceLocation("Client\\images\\TitleBackground.png"));

            String s = "D\247 iablo " + Main.version;
            TTFFontRenderer fr = Main.getInstance().getSFUI(20);
            mc.fontRendererObj.drawStringWithShadow(s, 3, this.height - fontRendererObj.FONT_HEIGHT - 2, ColorHelper.getColor(0));
            String s1 = "Developed by \247 " + Main.authors;
            mc.fontRendererObj.drawStringWithShadow(s1, this.width - mc.fontRendererObj.getStringWidth(s1) - 3, this.height - fontRendererObj.FONT_HEIGHT - 2, ColorHelper.getColor(0));

            int j2 = this.height / 4 + 48;

            ResourceLocation icon16 = new ResourceLocation("Client/images/logo.png");
            try {
                InputStream inputStream = Config.getResourceStream(icon16);
                JPanel pane = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        try {
                            g.drawImage(ImageIO.read(inputStream), sr.getScaledWidth() - (212 / 2), 0, null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void drawChangelog() {
        double x = 10;
        double y = 10;

        TTFFontRenderer fr = Main.getInstance().getSFUI(20);
        BufferedReader bufferedreader = null;
        mc.fontRendererObj.drawStringWithShadow("Changelog", (float) x, 10, -1);
        try {
            List<String> list = Lists.newArrayList();
            bufferedreader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/assets/minecraft/Client/changelog.txt"), Charsets.UTF_8));
            String s;

            int y2 = 22;
            while ((s = bufferedreader.readLine()) != null) {
                s = s.trim();

                if (!s.isEmpty()) {
                    mc.fontRendererObj.drawStringWithShadow("[+] " + s, (float) (x + 10), y2, -1);
                    y2 = y2 + 12;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (!authed) {
            this.uid.mouseClicked(mouseX, mouseY, mouseButton);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);


    }
}
