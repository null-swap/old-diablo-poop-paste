package cc.diablo.helpers.player;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class InventoryUtils {
    public static void switchToSlot(int slot) {
        (Minecraft.getMinecraft()).thePlayer.inventory.currentItem = slot - 1;
        Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(slot - 1));
    }

    public static void shiftClick(int slot, int windowId) {
        (Minecraft.getMinecraft()).playerController.windowClick(windowId, slot, 0, 1, (Minecraft.getMinecraft()).thePlayer);
    }

    public static void shiftClick(int slot) {
        shiftClick(slot, (Minecraft.getMinecraft()).thePlayer.inventoryContainer.windowId);
    }

    public static void drop(int slot, int windowId) {
        (Minecraft.getMinecraft()).playerController.windowClick(windowId, slot, 1, 4, (Minecraft.getMinecraft()).thePlayer);
    }

    public static void drop(int slot) {
        shiftClick(slot, (Minecraft.getMinecraft()).thePlayer.inventoryContainer.windowId);
    }

    public static void click(int slot, int windowId) {
        (Minecraft.getMinecraft()).playerController.windowClick(windowId, slot, 0, 0, (Minecraft.getMinecraft()).thePlayer);
    }

    public static void click(int slot) {
        shiftClick(slot, (Minecraft.getMinecraft()).thePlayer.inventoryContainer.windowId);
    }

    public static void swap(int slot1, int hotbarSlot, int windowId) {
        if (hotbarSlot == slot1 - 36)
            return;
        (Minecraft.getMinecraft()).playerController.windowClick(windowId, slot1, hotbarSlot, 2, (Minecraft.getMinecraft()).thePlayer);
    }

    public static void swap(int slot1, int hotbarSlot) {
        swap(slot1, hotbarSlot, (Minecraft.getMinecraft()).thePlayer.inventoryContainer.windowId);
    }

    public static float getDamage(final ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemSword) {
            double damage = 4.0 + ((ItemSword)itemStack.getItem()).getDamageVsEntity();
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25;
            return (float)damage;
        }
        if (itemStack.getItem() instanceof ItemTool) {
            double damage = 1.0 + ((ItemTool)itemStack.getItem()).getToolMaterial().getDamageVsEntity();
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
            return (float)damage;
        }
        return 1.0f;
    }

    public static boolean isBestPickaxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof net.minecraft.item.ItemPickaxe))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if ((Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = (Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof net.minecraft.item.ItemPickaxe)
                    return false;
            }
        }
        return true;
    }

    public static boolean isBestShovel(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof net.minecraft.item.ItemSpade))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if ((Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = (Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof net.minecraft.item.ItemSpade)
                    return false;
            }
        }
        return true;
    }

    public static boolean isBestAxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof net.minecraft.item.ItemAxe))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if ((Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = (Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof net.minecraft.item.ItemAxe && !isBestWeapon(stack))
                    return false;
            }
        }
        return true;
    }

    public static float getToolEffect(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemTool))
            return 0.0F;
        String name = item.getUnlocalizedName();
        ItemTool tool = (ItemTool)item;
        float value = 1.0F;
        if (item instanceof net.minecraft.item.ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold"))
                value -= 5.0F;
        } else if (item instanceof net.minecraft.item.ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold"))
                value -= 5.0F;
        } else if (item instanceof net.minecraft.item.ItemAxe) {
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold"))
                value -= 5.0F;
        } else {
            return 1.0F;
        }
        value = (float)(value + EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D);
        value = (float)(value + EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0D);
        return value;
    }

    public static boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if (potion.getEffects(stack) == null)
                return true;
            for (Object o : potion.getEffects(stack)) {
                PotionEffect effect = (PotionEffect)o;
                if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId())
                    return true;
            }
        }
        return false;
    }

    public static boolean isBestWeapon(ItemStack stack) {
        float damage = getDamage(stack);
        for (int i = 9; i < 45; i++) {
            if ((Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = (Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getDamage(is) > damage && is.getItem() instanceof ItemSword)
                    return false;
            }
        }
        return stack.getItem() instanceof ItemSword;
    }

    public static Slot getBestSwordSlot() {
        Slot bestSword = null;
        for (int i = 9; i < 45; i++) {
            if ((Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Slot slot = (Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i);
                if (slot.getStack().getItem() instanceof ItemSword &&
                        isBestWeapon(slot.getStack()))
                    bestSword = slot;
            }
        }
        return bestSword;
    }

    public static Slot getBestPickaxeSlot() {
        Slot bestPickaxe = null;
        for (int i = 9; i < 45; i++) {
            if ((Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Slot slot = (Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i);
                if (slot.getStack().getItem() instanceof net.minecraft.item.ItemPickaxe &&
                        isBestPickaxe(slot.getStack()))
                    bestPickaxe = slot;
            }
        }
        return bestPickaxe;
    }

    public static Slot getBestAxeSlot() {
        Slot bestAxe = null;
        for (int i = 9; i < 45; i++) {
            if ((Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Slot slot = (Minecraft.getMinecraft()).thePlayer.inventoryContainer.getSlot(i);
                if (slot.getStack().getItem() instanceof net.minecraft.item.ItemAxe &&
                        isBestAxe(slot.getStack()))
                    bestAxe = slot;
            }
        }
        return bestAxe;
    }

    public static boolean isInventoryFull() {
        byte b;
        int i;
        ItemStack[] arrayOfItemStack;
        for (i = (arrayOfItemStack = (Minecraft.getMinecraft()).thePlayer.inventory.mainInventory).length, b = 0; b < i; ) {
            ItemStack stack = arrayOfItemStack[b];
            if (stack == null || stack.getItem() == null)
                return false;
            b++;
        }
        return true;
    }
}
