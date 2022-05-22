package net.minecraft.util;

import cc.diablo.Main;
import cc.diablo.manager.module.ModuleManager;
import cc.diablo.module.impl.movement.NoSlow;
import cc.diablo.module.impl.player.InventoryMove;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.input.Keyboard;

public class MovementInputFromOptions extends MovementInput
{
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn)
    {
        this.gameSettings = gameSettingsIn;
    }

    public void updatePlayerMoveState()
    {
        InventoryMove inventorymove = (InventoryMove) ModuleManager.getModule(InventoryMove.class);
        if (inventorymove.isToggled() && !(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
            moveStrafe = 0.0F;
            moveForward = 0.0F;
            if (Keyboard.isKeyDown(this.gameSettings.keyBindForward.getKeyCode())) {
                moveForward -= 1.0F;
            }
            if (Keyboard.isKeyDown(this.gameSettings.keyBindBack.getKeyCode())) {
                moveForward -= 1.0F;
            }
            if (Keyboard.isKeyDown(this.gameSettings.keyBindLeft.getKeyCode())) {
                moveStrafe += 1.0F;
            }
            if (Keyboard.isKeyDown(this.gameSettings.keyBindRight.getKeyCode())) {
                moveStrafe -= 1.0F;
            }
            jump = Keyboard.isKeyDown(this.gameSettings.keyBindJump.getKeyCode());
            sneak = this.gameSettings.keyBindSneak.pressed;
            if (sneak) {
                moveStrafe = ((float) (moveStrafe * 0.3D));
                moveForward = ((float) (moveForward * 0.3D));
            }
        }
        else
        moveStrafe = 0.0F;
        moveForward = 0.0F;

        if (this.gameSettings.keyBindForward.isKeyDown())
        {
            ++moveForward;
        }

        if (this.gameSettings.keyBindBack.isKeyDown())
        {
            --moveForward;
        }

        if (this.gameSettings.keyBindLeft.isKeyDown())
        {
            ++moveStrafe;
        }

        if (this.gameSettings.keyBindRight.isKeyDown())
        {
            --moveStrafe;
        }

        jump = this.gameSettings.keyBindJump.isKeyDown();
        sneak = this.gameSettings.keyBindSneak.isKeyDown();

        if (sneak)
        {
            moveStrafe = (float)((double) moveStrafe * 0.3D);
            moveForward = (float)((double) moveForward * 0.3D);
        }
    }
}
