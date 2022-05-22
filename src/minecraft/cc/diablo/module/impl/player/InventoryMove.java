package cc.diablo.module.impl.player;

import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.PacketEvent;
import cc.diablo.event.impl.TickEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.player.KillAuraHelper;
import cc.diablo.helpers.MathHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MovementInput;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

public class InventoryMove extends Module {
    public InventoryMove(){
        super("Inventory Move", "Move while in GUI's", Keyboard.KEY_NONE, Category.Player);
    }

    @Subscribe
    public void onUpdate(UpdateEvent e){
        if (mc.currentScreen != null && e.isPre() && !(mc.currentScreen instanceof GuiChat)) {
            MovementInput.moveForward = 1;
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) mc.thePlayer.rotationPitch += 2f;
            if (Keyboard.isKeyDown(Keyboard.KEY_UP)) mc.thePlayer.rotationPitch -= 2f;
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) mc.thePlayer.rotationYaw += 2f;
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) mc.thePlayer.rotationYaw -= 2f;
            KeyBinding[] keys = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight,mc.gameSettings.keyBindJump};
            Arrays.stream(keys).forEach(key -> KeyBinding.setKeyBindState(key.getKeyCode(), Keyboard.isKeyDown(key.getKeyCode())));
        }
    }
}