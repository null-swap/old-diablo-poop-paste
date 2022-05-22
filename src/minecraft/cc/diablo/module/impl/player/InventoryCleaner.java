package cc.diablo.module.impl.player;

import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.setting.impl.BooleanSetting;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.TickEvent;
import cc.diablo.helpers.MathHelper;
import cc.diablo.helpers.Stopwatch;
import cc.diablo.helpers.player.InventoryUtils;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.lwjgl.input.Keyboard;

import java.util.Collections;
import java.util.List;

public class InventoryCleaner extends Module {
    public NumberSetting delay = new NumberSetting("Delay", 125, 1, 250, 1);
    public NumberSetting delayMin = new NumberSetting("Random Delay Min", 5, 0, 50, 1);
    public NumberSetting delayMax = new NumberSetting("Random Delay Max", 20, 0, 50, 1);
    public BooleanSetting openInventory = new BooleanSetting("Open Inventory", true);
    public BooleanSetting standingStill = new BooleanSetting("Standing Still", false);

    public int ticks;
    private Stopwatch timer = new Stopwatch();
    private final int[] boots;
    private final int[] chestplate;
    private final int[] helmet;
    private final int[] leggings;

    public static boolean isChanging;

    public InventoryCleaner() {
        super("InvManager", "fuck up ur inv", Keyboard.KEY_NONE, Category.Player);
        this.timer = new Stopwatch();
        this.boots = new int[]{313, 309, 317, 305, 301};
        this.chestplate = new int[]{311, 307, 315, 303, 299};
        this.helmet = new int[]{310, 306, 314, 302, 298};
        this.leggings = new int[]{312, 308, 316, 304, 300};
        this.addSettings(delay, delayMin, delayMax, openInventory, standingStill);
    }

    @Override
    public void onEnable() {
        isChanging = false;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        isChanging = false;
        super.onDisable();
    }

    @Subscribe
    public void onPre(UpdateEvent event) {
        if (mc.currentScreen instanceof GuiInventory) {
            if (openInventory.isChecked() && mc.currentScreen instanceof GuiInventory) {
                runTheShit();
            } else {
                if (!mc.thePlayer.isMoving()) {
                    runTheShit();
                }
            }
        } else if(standingStill.isChecked() && !mc.thePlayer.isMoving()) {
            mc.gameSettings.keyBindInventory.pressed = true;
            runTheShit();
        } else {
            isChanging = false;
        }
    }

    public void runTheShit() {
        //isChanging = true;
        List<Slot> inventorySlots = Lists.newArrayList();
        inventorySlots.addAll(mc.thePlayer.inventoryContainer.inventorySlots);
        Collections.shuffle(inventorySlots);

        for (Slot slot : inventorySlots) {
            ItemStack item = slot.getStack();
            if (item != null) {
                int windowId = mc.thePlayer.inventoryContainer.windowId;
                if (timer.hasReached((long) (delay.getVal() + MathHelper.getRandInt((int) Math.round(delayMin.getVal()), (int) Math.round(delayMax.getVal()))))) {
                    if (moveOrDropItem(slot, windowId)) {
                        timer.reset();
                        return;
                    }
                }
            }
        }
    }

    private int getProtValue(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemArmor) {
            final int normalValue = ((ItemArmor) stack.getItem()).damageReduceAmount;
            final int enchantmentValue = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
            return normalValue + enchantmentValue;
        }
        return -1;
    }

    private static final int swordSlot = 0;
    private static final int pickaxeSlot = 1;
    private static final int axeSlot = 2;
    private static final int blockSlot = 3;
    private static final int gappleSlot = 4;

    private boolean moveOrDropItem(Slot slot, int windowId) {
        boolean runBlock = true;
        boolean runGapple = true;
        ItemStack stack = slot.getStack();
        if (stack == null || stack.getItem() == null) return false;
        if (stack.getItem() instanceof net.minecraft.item.ItemSword) {
            if (InventoryUtils.getBestSwordSlot() != null && InventoryUtils.getBestSwordSlot() == slot) {
                if (slot.slotNumber == swordSlot) return false;
                InventoryUtils.swap(slot.slotNumber, swordSlot, windowId);
            } else {
                InventoryUtils.drop(slot.slotNumber, windowId);
            }
            return true;
        }
        if (stack.getItem() instanceof net.minecraft.item.ItemPickaxe) {
            if (InventoryUtils.getBestPickaxeSlot() != null && InventoryUtils.getBestPickaxeSlot() == slot) {
                if (slot.slotNumber == pickaxeSlot) return false;
                InventoryUtils.swap(slot.slotNumber, pickaxeSlot, windowId);
            } else {
                InventoryUtils.drop(slot.slotNumber, windowId);
            }
            return true;
        }
        if (stack.getItem() instanceof net.minecraft.item.ItemAxe) {
            if (InventoryUtils.getBestAxeSlot() != null && InventoryUtils.getBestAxeSlot() == slot) {
                if (slot.slotNumber == axeSlot) return false;
                InventoryUtils.swap(slot.slotNumber, axeSlot, windowId);
            } else {
                InventoryUtils.drop(slot.slotNumber, windowId);
            }
            return true;
        }
        if (stack.getItem() instanceof net.minecraft.item.ItemSpade || stack.getItem() instanceof net.minecraft.item.ItemHoe) {
            InventoryUtils.drop(slot.slotNumber, windowId);
            return true;
        }

        if (stack.getItem() instanceof net.minecraft.item.ItemArmor) {
            if (slot.slotNumber >= 5 && slot.slotNumber <= 8) return false;
            for (int type = 1; type < 5; type++) {
                ItemStack currentArmor = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                if (isBestArmor(stack, type) && (currentArmor == null || !isBestArmor(currentArmor, type)))
                    return false;
            }
            InventoryUtils.drop(slot.slotNumber, windowId);
            return true;
        }

        /*
        if(stack.getItem() instanceof ItemBlock){
            boolean dofuny = true;
            for (Slot slot2 : mc.thePlayer.inventoryContainer.inventorySlots) {
                if(slot2.getHasStack() && dofuny) {
                    if (slot2.getStack().getItem() instanceof ItemBlock) {
                        InventoryUtils.swap(slot.slotNumber, blockSlot, windowId);
                        dofuny = false;
                        return true;
                    }
                }
            }

        }

         */

        if (stack.getItem() instanceof ItemBlock) {
            //need ur help vince for this and the one below just want it to check if the block slot (3 in the hotbar) has more than any or slot and if so it doesnt sort anything
            int count = 0;
            for (Slot slot2 : mc.thePlayer.inventoryContainer.inventorySlots) {
                if (slot2.getHasStack() && mc.thePlayer.inventoryContainer.getSlot(blockSlot).getStack().stackSize > slot2.getStack().stackSize) {
                    runBlock = false;
                }
                if (slot2.getStack().getItem() instanceof ItemBlock) {
                    count++;
                }
            }
            InventoryUtils.swap(slot.slotNumber, blockSlot, windowId);
            if (count > 1 && runBlock) {
                InventoryUtils.click(slot.slotNumber);
                InventoryUtils.click(blockSlot);
            }
            return true;
        }

        if (Item.getIdFromItem(stack.getItem()) == 322) {
            int count = 0;
            for (Slot slot2 : mc.thePlayer.inventoryContainer.inventorySlots) {
                if (slot2.getHasStack() && Item.getIdFromItem(slot2.getStack().getItem()) == 322 && mc.thePlayer.inventoryContainer.getSlot(gappleSlot).getStack().stackSize > slot2.getStack().stackSize) {
                    runGapple = false;
                }
                if (Item.getIdFromItem(slot2.getStack().getItem()) == 322) {
                    count++;
                }

            }
            InventoryUtils.swap(slot.slotNumber, gappleSlot, windowId);
            if (count > 1 && runGapple) {
                InventoryUtils.click(slot.slotNumber);
                InventoryUtils.click(gappleSlot);
            }
            return true;
        }

        getBestArmor();
        return false;
    }

    public void getBestArmor() {
        for (int type = 1; type < 5; type++) {
            if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                ItemStack item = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                if (!isBestArmor(item, type)) {
                    InventoryUtils.drop(4 + type, mc.thePlayer.inventoryContainer.windowId);
                    return;
                }
            } else {
                for (int i = 9; i < 45; i++) {
                    if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                        if (isBestArmor(is, type) && getProtection(is) > 0.0F) {
                            InventoryUtils.shiftClick(i, mc.thePlayer.inventoryContainer.windowId);
                            return;
                        }
                    }
                }
            }
        }
    }

    public static boolean isBestArmor(ItemStack stack, int type) {
        float prot = getProtection(stack);
        String strType = "";
        if (type == 1) {
            strType = "helmet";
        } else if (type == 2) {
            strType = "chestplate";
        } else if (type == 3) {
            strType = "leggings";
        } else if (type == 4) {
            strType = "boots";
        }
        if (!stack.getUnlocalizedName().contains(strType)) return false;
        for (int i = 5; i < 45; i++) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType)) return false;
            }
        }
        return true;
    }

    public static float getProtection(ItemStack stack) {
        float prot = 0.0F;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) stack.getItem();
            prot = (float) (prot + armor.damageReduceAmount + ((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075D);
            prot = (float) (prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0D);
            prot = (float) (prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0D);
            prot = (float) (prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0D);
            prot = (float) (prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0D);
            prot = (float) (prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) / 100.0D);
            prot = (float) (prot + EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) / 100.0D);
        }
        return prot;
    }
}
