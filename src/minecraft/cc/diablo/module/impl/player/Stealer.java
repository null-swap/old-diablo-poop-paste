package cc.diablo.module.impl.player;

import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.setting.impl.BooleanSetting;
import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.TickEvent;
import cc.diablo.helpers.MathHelper;
import cc.diablo.helpers.Stopwatch;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;

public class Stealer extends Module {
    public NumberSetting delay = new NumberSetting("Delay", 30, 1, 250, 1);
    public NumberSetting delayMin = new NumberSetting("Random Delay Min", 1, 0, 50, 1);
    public NumberSetting delayMax = new NumberSetting("Random Delay Max", 10, 0, 50, 1);
    public BooleanSetting onlyChestGui = new BooleanSetting("Only Chest GUI", true);
    public static BooleanSetting silent = new BooleanSetting("Silent", false);
    private final Stopwatch timer = new Stopwatch();
    private long delaySave = (long) (delay.getVal() + MathHelper.getRandInt((int) Math.round(delayMin.getVal()), (int) Math.round(delayMax.getVal())));
    public static boolean isStealing;

    public Stealer() {
        super("Stealer", "become jewish", Keyboard.KEY_NONE, Category.Player);
        this.addSettings(delay, delayMax, delayMax, onlyChestGui, silent);
    }

    @Override
    public void onEnable() {
        isStealing = false;
        super.onEnable();
    }

    @Subscribe
    public void onTick(UpdateEvent event) {
        if (mc.thePlayer.openContainer instanceof ContainerChest) {
            final ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
            for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); ++i) {
                if (container.getLowerChestInventory().getStackInSlot(i) != null && this.timer.hasReached(delaySave)) {
                    if (onlyChestGui.isChecked()) {
                        if (container.getLowerChestInventory().getDisplayName().getUnformattedText().contains("Chest") || container.getLowerChestInventory().getDisplayName().getUnformattedText().contains("Storage")) {
                            isStealing = silent.isChecked();
                            mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
                        } else {
                            isStealing = false;
                        }
                    } else {
                        isStealing = silent.isChecked();
                        mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
                    }
                    delaySave = (long) (delay.getVal() + MathHelper.getRandInt((int) Math.round(delayMin.getVal()), (int) Math.round(delayMax.getVal())));
                    this.timer.reset();
                }
            }
            if (this.isContainerEmpty(container)) {
                mc.thePlayer.closeScreen();
            }
        } else {
            isStealing = false;
        }
    }

    public boolean isValid(Item item) {
        if (item instanceof ItemSword) {
            return true;
        }
        if (item instanceof ItemAxe) {
            return true;
        }
        if (item instanceof ItemFood) {
            return true;
        }
        if (item instanceof ItemArmor) {
            return true;
        } else {
            return item instanceof ItemPotion;
        }
    }

    public boolean isContainerEmpty(Container container) {
        boolean temp = true;
        for (int i = 0, slotAmount = (container.inventorySlots.size() == 90) ? 54 : 27; i < slotAmount; ++i) {
            if (container.getSlot(i).getHasStack()) {
                temp = false;
            }
        }
        return temp;
    }

    private EnumFacing getFacingDirection(final BlockPos pos) {
        EnumFacing direction = null;
        if (!Minecraft.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isFullCube()) {
            direction = EnumFacing.UP;
        } else if (!Minecraft.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isFullCube()) {
            direction = EnumFacing.DOWN;
        } else if (!Minecraft.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isFullCube()) {
            direction = EnumFacing.EAST;
        } else if (!Minecraft.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isFullCube()) {
            direction = EnumFacing.WEST;
        } else if (!Minecraft.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isFullCube()) {
            direction = EnumFacing.SOUTH;
        } else if (!Minecraft.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isFullCube()) {
            direction = EnumFacing.NORTH;
        }
        return direction;
    }
}
