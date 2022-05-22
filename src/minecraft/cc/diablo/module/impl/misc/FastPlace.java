package cc.diablo.module.impl.misc;

import cc.diablo.event.EventType;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;

public class FastPlace extends Module {
    public FastPlace() {
        super("Fast Place", "Speeds up block placement", Keyboard.KEY_NONE, Category.Misc);
    }

    private static boolean isSplashPot(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion) stack.getItem();
            return ItemPotion.isSplash(stack.getItemDamage());
        }
        return false;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == EventType.Pre && !(mc.thePlayer.getItemInUse().getItem() instanceof ItemBow) && !(mc.thePlayer.getItemInUse().getItem() instanceof ItemSword) && (mc.thePlayer.getItemInUse().getItem() instanceof ItemFood || mc.thePlayer.getItemInUse().getItem() instanceof ItemBucketMilk || (mc.thePlayer.getItemInUse().getItem() instanceof ItemPotion && !isSplashPot(mc.thePlayer.getItemInUse()))) && !mc.thePlayer.isBlocking()) {
            if (mc.thePlayer.getItemInUseDuration() == 16) {
                for (int i = 0; i < 17; ++i) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    Timer.timerSpeed = 1.1f;
                }
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                Timer.timerSpeed = 1.0f;
            }
        }
    }

    @Override
    public void onDisable() {
        Timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}
