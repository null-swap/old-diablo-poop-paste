package cc.diablo.helpers.player;

import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ItemUtil {
    public static boolean isValidSpeedPot(final ItemStack stack) {
        return isValidSpeedPot((ItemPotion) stack.getItem(), stack);
    }

    public static boolean isValidSpeedPot(final ItemPotion potion, final ItemStack stack) {
        if (ItemPotion.isSplash(stack.getItemDamage())) {
            for (final Object o : potion.getEffects(stack)) {
                if (!(o instanceof PotionEffect) || (((PotionEffect) o).getPotionID() != Potion.moveSpeed.id && ((PotionEffect) o).getPotionID() != Potion.heal.id && ((PotionEffect) o).getPotionID() != Potion.regeneration.id))  {
                    continue;
                }
                return true;
            }
        }
        return false;
    }
}
