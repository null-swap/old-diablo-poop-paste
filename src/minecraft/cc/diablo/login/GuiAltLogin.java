package cc.diablo.login;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.AuthenticationService;
import com.github.steveice10.mc.auth.service.MojangAuthenticationService;
import com.github.steveice10.mc.auth.service.MsaAuthenticationService;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;


public final class GuiAltLogin
        extends GuiScreen {
    private PasswordField password;
    private final GuiScreen previousScreen;
    private AltLoginThread thread;
    private GuiTextField username;
    public boolean mojangauth = true;

    public GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.previousScreen);
                break;
            }
            case 0: {
                if(!mojangauth)
                {


                    try {
                        MsaAuthenticationService msaAuthenticationService = new MsaAuthenticationService("");
                        msaAuthenticationService.setUsername(this.username.getText());
                        msaAuthenticationService.setPassword(this.password.getText());
                        msaAuthenticationService.login();
                        msaAuthenticationService.setProxy(Proxy.NO_PROXY);
                        GameProfile gameProfile = msaAuthenticationService.getAvailableProfiles().get(0);
                        mc.session = new Session(gameProfile.getName(), gameProfile.getIdAsString(), msaAuthenticationService.getAccessToken(), "mojang");
                    } catch (RequestException e) {
                        e.printStackTrace();
                    }

                    /*
                    MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                    MicrosoftAuthResult result = null;
                    try {
                        result = authenticator.loginWithCredentials(this.username.getText(), this.password.getText());
                        System.out.printf("Logged in with '%s'%n", result.getProfile().getName());
                        Minecraft.getMinecraft().session = new Session(result.getProfile().getName(),result.getProfile().getId(), result.getAccessToken(),"legacy");
                    } catch (MicrosoftAuthenticationException e) {
                        e.printStackTrace();
                    }

                     */
                }
                else
                {
                    this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
                    this.thread.start();
                }
                break;
            }
            case 2: {
                String data = null;
                try {
                    data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                } catch (Exception ignored) {
                    break;
                }
                if (!data.contains(":")) break;
                String[] credentials = data.split(":");
                this.username.setText(credentials[0]);
                this.password.setText(credentials[1]);
                break;
            }
            case 3: {
                mojangauth = !mojangauth;
                break;
            }
            case 4: {
                this.username.setText("Diablo_" + RandomStringUtils.random(9, "abcdefghijklmnopqrstuvwxyz1234567890"));
                this.password.setText("");
                this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
                this.thread.start();
                break;
            }
            case 5: {

                try {
                    String json = readUrl(
                            "http://drilledalts.xyz/api/gen?key="+password.getText()).replace("{", "").replace("}", "").replace("\"", "");

                    String[] accountCreds = json.split(",");

                    String username = accountCreds[0].replace("email","").replace(":","");
                    String password = accountCreds[1].replace("password","").replace(":","");

                    System.out.println(username);
                    System.out.println(password);

                    this.thread = new AltLoginThread(username, password);
                    this.thread.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            case 6:

                break;
            case 7:

                break;
        }
    }

    @Override
    public void drawScreen(int x2, int y2, float z2) {
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.drawCenteredString(this.mc.fontRendererObj, "Alt Login", width / 2, 20, -1);
        this.drawCenteredString(this.mc.fontRendererObj, this.thread == null ? EnumChatFormatting.GRAY + "Idle..." : this.thread.getStatus(), width / 2, 29, -1);
        this.drawCenteredString(this.mc.fontRendererObj, mojangauth ? ChatFormatting.AQUA + "Minecraft" : ChatFormatting.LIGHT_PURPLE + "Mojang", width / 2 - 1, 40, -1);
        if (this.username.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "Username / E-Mail", width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            this.drawString(this.mc.fontRendererObj, "Password/API", width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 24 + 24, "Back"));
        this.buttonList.add(new GuiButton(2, width / 2 - 100, var3 + 72 + 12 + 24, "Import Alt"));
        this.buttonList.add(new GuiButton(3, width / 2 - 100, var3 + 72 + 12 + 24 + 24 + 24, "Switch Auth"));
        this.buttonList.add(new GuiButton(4, width / 2 - 100, var3 + 72 + 12 + 24 + 24 + 24 + 24, "Generate Cracked"));
        //this.buttonList.add(new GuiButton(5, width / 2 - 100, var3 + 72 + 12 + 24 + 24 + 24 + 24 + 24, "Generate Drilled"));
        this.username = new GuiTextField(var3, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x2, int y2, int button) {
        try {
            super.mouseClicked(x2, y2, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x2, y2, button);
        this.password.mouseClicked(x2, y2, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }

    }

    private AuthenticationService login(String clientToken, String with, boolean token) {
        AuthenticationService auth = new MojangAuthenticationService(clientToken);
        auth.setProxy(Proxy.NO_PROXY);
        auth.setUsername(this.username.getText());
        if(token) {
            auth.setAccessToken(with);
        } else {
            auth.setPassword(with);
        }

        try {
            auth.login();
        } catch(RequestException e) {
            System.err.println("Failed to log in with " + (token ? "token" : "password") + "!");
            e.printStackTrace();
        }

        return auth;
    }
}

