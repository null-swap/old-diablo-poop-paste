package cc.diablo.module.impl.combat;

import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.PacketHelper;
import cc.diablo.helpers.player.ItemUtil;
import cc.diablo.helpers.player.KillAuraHelper;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import jdk.nashorn.internal.runtime.logging.Logger;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;

import java.sql.Wrapper;
import java.util.Iterator;

public class AutoPot extends Module {
    private static boolean doThrow;
    int slot;

    public AutoPot(){
        super("Auto Pot","Automatically use potions", Keyboard.KEY_NONE, Category.Combat);
    }

    @Override
    public void onEnable(){
        slot = -1;
        super.onEnable();
    }
    @Subscribe
    public void onUpdate(UpdateEvent e){
        if(mc.currentScreen instanceof GuiInventory && mc.thePlayer.onGround) {
            int potionSlot = this.getPotionFromInv();
            boolean shouldSplash = mc.thePlayer.getHealth() <= 10;
            if (potionSlot != -1) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(potionSlot).getStack();
                Item item = is.getItem();
                if (item instanceof ItemPotion) {
                    ItemPotion potion = (ItemPotion) item;
                    if (potion.getEffects(is) != null) {
                        Iterator var10 = potion.getEffects(is).iterator();

                        while (var10.hasNext()) {
                            Object o = var10.next();
                            PotionEffect effect = (PotionEffect) o;
                            if (effect.getPotionID() == Potion.moveSpeed.id && true && !mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                                shouldSplash = true;
                            }
                        }
                    }
                }
            }

            int prevSlot = mc.thePlayer.inventory.currentItem;
            if (potionSlot != -1 && (mc.thePlayer.ticksExisted & 20) == 0) {
                mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, potionSlot, 1, 2, mc.thePlayer);
                KillAuraHelper.setRotations(e, mc.thePlayer.rotationYaw, 85);
                PacketHelper.sendPacketNoEvent(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, 85, mc.thePlayer.onGround));
                PacketHelper.sendPacketNoEvent(new C09PacketHeldItemChange(1));
                PacketHelper.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(1)));
                PacketHelper.sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }
        }
    }

    private int getPotionFromInv() {
        int pot = -1;

        label52:
        for(int i = 0; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (item instanceof ItemPotion) {
                    ItemPotion potion = (ItemPotion)item;
                    if (potion.getEffects(is) != null) {
                        Iterator var6 = potion.getEffects(is).iterator();

                        while(true) {
                            PotionEffect effect;
                            do {
                                if (!var6.hasNext()) {
                                    continue label52;
                                }

                                Object o = var6.next();
                                effect = (PotionEffect)o;
                            } while(effect.getPotionID() != Potion.heal.id && (effect.getPotionID() != Potion.regeneration.id || false || mc.thePlayer.isPotionActive(Potion.regeneration)) && (effect.getPotionID() != Potion.moveSpeed.id || false || mc.thePlayer.isPotionActive(Potion.moveSpeed)));

                            if (ItemPotion.isSplash(is.getItemDamage())) {
                                pot = i;
                            }
                        }
                    }
                }
            }
        }

        return pot;
    }
}
