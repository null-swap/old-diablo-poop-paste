package cc.diablo.module.impl.player;

import cc.diablo.event.impl.CollideEvent;
import cc.diablo.event.impl.OverlayEvent;
import cc.diablo.helpers.MathHelper;
import cc.diablo.helpers.Stopwatch;
import cc.diablo.helpers.module.ModuleData;
import cc.diablo.helpers.render.ColorHelper;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.setting.impl.BooleanSetting;
import cc.diablo.setting.impl.ModeSetting;
import cc.diablo.setting.impl.NumberSetting;
import com.google.common.eventbus.Subscribe;
import cc.diablo.event.impl.SafeWalkEvent;
import cc.diablo.event.impl.UpdateEvent;
import cc.diablo.helpers.player.EntityHelper;
import cc.diablo.helpers.player.KillAuraHelper;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.block.*;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@ModuleData(name = "Scaffold", category = Category.Player, description = "become bob the builder")

public class Scaffold extends Module {
    public ModeSetting mode = new ModeSetting("Rotations", "Watchdog", "Smooth", "Watchdog", "Legit", "Snap", "None");
    public ModeSetting placeMode = new ModeSetting("Place Mode", "Packet", "Packet", "Right Click");
    public ModeSetting towerMode = new ModeSetting("Tower", "None", "Hypixel", "None");
    public BooleanSetting safeWalk = new BooleanSetting("Safe Walk", true);
    public NumberSetting timer = new NumberSetting("Timer", 1, 1, 5, 0.05);
    public BooleanSetting sprint = new BooleanSetting("Sprint", false);
    public NumberSetting delayMin = new NumberSetting("Delay Min", 75, 0, 500, 1);
    public NumberSetting delayMax = new NumberSetting("Delay Max", 100, 0, 500, 1);
    public NumberSetting minRot = new NumberSetting("Minimum Rotation", 5, 0, 15, 0.2);
    public NumberSetting maxRot = new NumberSetting("Maximum Rotation", 5, 0, 15, 0.2);
    public BooleanSetting noAura = new BooleanSetting("No Aura", false);
    public BooleanSetting autoJump = new BooleanSetting("Auto Jump", false);
    public BooleanSetting collide = new BooleanSetting("Collide", false);
    public static BlockData data = null;
    private final List<Block> validBlocks;
    private final List<Block> invalidBlocks;
    private final BlockPos[] blockPositions;
    private final EnumFacing[] facings;
    private int slot;
    public float yaw, pitch;
    private Stopwatch stopwatch = new Stopwatch();
    double keepY;

    public Scaffold() {
        this.invalidBlocks = Arrays.asList(Blocks.redstone_wire, Blocks.tallgrass, Blocks.redstone_torch, Blocks.enchanting_table, Blocks.furnace, Blocks.carpet, Blocks.crafting_table, Blocks.trapped_chest, Blocks.chest, Blocks.dispenser, Blocks.air, Blocks.water, Blocks.lava, Blocks.flowing_water, Blocks.flowing_lava, Blocks.sand, Blocks.snow_layer, Blocks.torch, Blocks.anvil, Blocks.jukebox, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.noteblock, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.wooden_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.yellow_flower, Blocks.red_flower, Blocks.anvil, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.cactus, Blocks.ladder, Blocks.web, Blocks.gravel, Blocks.tnt);
        this.validBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava);
        this.blockPositions = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1)};
        this.facings = new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.NORTH};
        this.slot = -1;
        this.addSettings(mode, placeMode, towerMode, safeWalk, timer, sprint, delayMin, delayMax, minRot, maxRot, autoJump, collide);
    }

    @Override
    public void onEnable() {
        keepY = mc.thePlayer.posY - 1;
        stopwatch.reset();
        this.slot = mc.thePlayer.inventory.currentItem;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        /*
        PacketHelper.sendPacketNoEvent(new C0CPacketInput(mc.thePlayer.moveStrafing, mc.thePlayer.moveForward, mc.thePlayer.movementInput.jump, false));
        PacketHelper.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));

         */
        mc.gameSettings.keyBindUseItem.pressed = false;
        if (Timer.timerSpeed != 1f) {
            Timer.timerSpeed = 1f;
        }
        mc.thePlayer.inventory.currentItem = this.slot;
        super.onDisable();
    }

    @Subscribe
    public void onSafewalk(SafeWalkEvent e) {
        if (mc.thePlayer.onGround && safeWalk.isChecked()) {
            e.setWalkSafely(true);
        }
    }

    @Subscribe
    public void onOverlay(OverlayEvent event) {
        boolean render = false;
        if (mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
            ScaledResolution sr = new ScaledResolution(mc);
            int width = 60;
            int height = 15;
            int x = (int) ((sr.getScaledWidth() / 8) * 5 - (width * 1.4));
            int y = (sr.getScaledHeight() / 6) * 4 - height;
            int blocks = 0;
            for (int index = 36; index < 45; ++index) {
                final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                if (itemStack != null && itemStack.getItem() instanceof ItemBlock && !(((ItemBlock) itemStack.getItem()).getBlock() instanceof BlockFalling)) {
                    blocks += itemStack.stackSize;
                    render = true;
                }
            }

            if (render) {
                //GuiIngame.renderHotbarItem();
                RenderUtils.drawRect(x - 1, y - 3, x + 51 + width, y + height + 1, RenderUtils.transparency(new Color(37, 37, 37).getRGB(), 0.6f)); //Blur
                RenderUtils.drawRect(x, y, x + 50 + width, y + height, RenderUtils.transparency(new Color(45, 45, 45, 255).getRGB(), 0.8f)); //Main background
                RenderUtils.drawRect(x, y - 2, x + 50 + width, y, RenderUtils.transparency(ColorHelper.getColor(0), 0.8f)); //Color!

                mc.fontRendererObj.drawStringWithShadow(mc.thePlayer.getHeldItem().getDisplayName(), x + 3, y + 3, -1);

                /*
                if (timer.getVal() != 1 && timer.getVal() > 0) {
                    mc.fontRendererObj.drawStringWithShadow("Timer: " + timer.getVal(), x + 3, y + 13, -1);
                }

                 */
                mc.fontRendererObj.drawStringWithShadow(String.valueOf(blocks), x + 47 + width - mc.fontRendererObj.getStringWidth(String.valueOf(blocks)), y + 3, -1);
            }
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        if (mc.gameSettings.keyBindJump.pressed && !mc.thePlayer.isMoving()) {
            keepY = mc.thePlayer.posY - 1;
        }
        if(mc.thePlayer.fallDistance < -1){
            keepY = mc.thePlayer.posY - 1;
        }

        if (mc.thePlayer.onGround && autoJump.isChecked() && mc.thePlayer.isMoving()) {
            mc.thePlayer.jump();
            mc.thePlayer.motionY = 0.4199997f;
        }

        data = this.getBlockData(new BlockPos(mc.thePlayer.posX, keepY, mc.thePlayer.posZ));
        this.setDisplayName(this.getName() + "\2477 " + mode.getMode());
        final WorldClient world = Minecraft.theWorld;
        final EntityPlayerSP player = mc.thePlayer;
        final Vec3 hitVec = this.getVec3(data);
        mc.thePlayer.setSprinting(sprint.isChecked());
        double yDif = 1.0;

        if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && mc.thePlayer.onGround) {
            EntityHelper.setMotion(0.08f);
        }
        if(!mc.gameSettings.keyBindJump.pressed) {
            Timer.timerSpeed = (float) timer.getVal();
        }

        int slot = -1;
        int blockCount = 0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack itemStack = player.inventory.getStackInSlot(i);
            if (itemStack != null) {
                final int stackSize = itemStack.stackSize;
                if (this.isValidItem(itemStack.getItem()) && stackSize > blockCount) {
                    blockCount = stackSize;
                    slot = i;
                }
            }
        }

        int randomInt = MathHelper.getRandInt((int) delayMin.getVal(), (int) delayMax.getVal());
            float[] rotations = getBlockRotations(data.pos, data.face);
            switch (mode.getMode()) {
                case "Watchdog":
                    KillAuraHelper.setRotations(e, (float) MathHelper.round(rotations[0], 39), rotations[1] - 0.7f - MathHelper.getRandomInRange((int) minRot.getVal(), (int) maxRot.getVal()));
                    break;
                case "Smooth":
                    KillAuraHelper.setRotations(e, rotations[0], rotations[1]);
                    break;
                case "Legit":
                    KillAuraHelper.setRotations(e, rotations[0], rotations[1] - 0.7f - MathHelper.getRandomInRange((int) minRot.getVal(), (int) maxRot.getVal()));
                    break;
                case "Snap":
                    KillAuraHelper.setRotations(e, yaw, pitch);
            }

        if (e.isPost()) {
            if (data != null && slot != -1 && e.isPost()) {
                if (stopwatch.hasReached(randomInt)) {
                    randomInt = MathHelper.getRandInt((int) delayMin.getVal(), (int) delayMax.getVal());
                    player.inventory.currentItem = slot;
                    if (this.placeMode.isMode("Right Click")) {
                        if (mc.playerController.onPlayerRightClick(player, world, player.getCurrentEquippedItem(), data.pos, data.face, hitVec)) {
                            yaw = (float) MathHelper.round((double) rotations[0], 37);
                            pitch = rotations[1] - 0.7f;
                            if(mc.gameSettings.keyBindJump.pressed) {
                                switch (towerMode.getMode()) {
                                    case "Hypixel":
                                        mc.thePlayer.motionX = 0;
                                        mc.thePlayer.motionZ = 0;
                                        if (mc.thePlayer.ticksExisted % 2 == 0) {
                                            mc.thePlayer.motionY = 0.276;
                                        }
                                        Timer.timerSpeed = 4f;
                                    break;
                                }
                            }

                            player.swingItem();
                        }
                    } else if (this.placeMode.isMode("Packet")) {
//                        yaw = (float) MathHelper.round((double) rotations[0], 37);
//                        pitch = rotations[1] - 0.7f;
//                        PacketHelper.sendPacket(new C08PacketPlayerBlockPlacement(data.pos, ));
//                        player.swingItem();
//                        VINCE CAN U FINISH THIS FOR ME PLS :3
                        //no useless bruvn
                    }
                    stopwatch.reset();
                }
            }
        }
    }

    @Subscribe
    public void onCollide(CollideEvent e) {
        final double x = e.getX();
        final double y = e.getY();
        final double z = e.getZ();
        if(collide.isChecked() || (mc.gameSettings.keyBindJump.pressed && Objects.equals(towerMode.getMode(), "Hypixel"))) {
            if (y < mc.thePlayer.posY) {
                e.setBoundingBox(AxisAlignedBB.fromBounds(15.0, 1.0, 15.0, -15.0, -1.0, -15.0).offset(x, y, z));
            }
        }
    }

    public static double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;
        if (mc.thePlayer.moveForward < 0F) forward = -0.5F;
        else if (mc.thePlayer.moveForward > 0F) forward = 0.5F;

        if (mc.thePlayer.moveStrafing > 0F) rotationYaw -= 90F * forward;

        if (mc.thePlayer.moveStrafing < 0F) rotationYaw += 90F * forward;

        return rotationYaw;
    }

    private float[] getBlockRotations(BlockPos blockPos, EnumFacing enumFacing) {
        if (blockPos == null && enumFacing == null) {
            return null;
        } else {
            Vec3 positionEyes = mc.thePlayer.getPositionEyes(2.0F);
            Vec3 add = (new Vec3((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D));
            double n = add.xCoord - positionEyes.xCoord;
            double n2 = add.yCoord - positionEyes.yCoord;
            double n3 = add.zCoord - positionEyes.zCoord;
            return new float[]{(float) (Math.atan2(n3, n) * 180.0D / 3.141592653589793D - 90.0D), -((float) (Math.atan2(n2, (float) Math.hypot(n, n3)) * 180.0D / 3.141592653589793D))};
        }
    }

    private Vec3 getVec3(final BlockData data) {
        final BlockPos pos = data.pos;
        final EnumFacing face = data.face;
        double x = pos.getX() + 0.5f;
        double y = pos.getY() + 0.5f;
        double z = pos.getZ() + 0.5f;
        /*
        if(!mc.getCurrentServerData().serverName.contains("hypixel.net")){

        }

         */
        x += face.getFrontOffsetX() / 2.0;
        z += face.getFrontOffsetZ() / 2.0;
        y += face.getFrontOffsetY() / 2.0;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += this.randomNumber(0.3, -0.3);
            z += this.randomNumber(0.3, -0.3);
        } else {
            y += this.randomNumber(0.49, 0.5);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += this.randomNumber(0.3, -0.3);
        }

        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += this.randomNumber(0.3, -0.3);
        }
        return new Vec3(x, y, z);
    }

    private double randomNumber(final double max, final double min) {
        return Math.random() * (max - min) + min;
    }

    private BlockData getBlockData(final BlockPos pos) {
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos1 = pos.add(-1, 0, 0);
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos1.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos1.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos1.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos1.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos1.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos2 = pos.add(1, 0, 0);
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos2.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos2.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos2.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos2.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos2.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos3 = pos.add(0, 0, 1);
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos3.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos3.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos3.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos3.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos3.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos4 = pos.add(0, 0, -1);
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos4.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos4.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos4.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos4.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos4.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos19 = pos.add(-2, 0, 0);
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos1.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos1.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos1.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos1.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos1.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos2.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos2.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos2.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos2.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos2.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos3.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos3.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos3.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos3.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos3.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos4.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos4.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos4.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos4.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos4.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos5 = pos.add(0, -1, 0);
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos5.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos5.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos5.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos5.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos5.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos6 = pos5.add(1, 0, 0);
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos6.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos6.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos6.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos6.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos6.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos7 = pos5.add(-1, 0, 0);
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState((pos7.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState(pos7.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState(pos7.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState(pos7.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState(pos7.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos8 = pos5.add(0, 0, 1);
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState(pos8.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState(pos8.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState(pos8.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState(pos8.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState(pos8.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos9 = pos5.add(0, 0, -1);
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState(pos9.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState(pos9.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState(pos9.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState(pos9.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalidBlocks.contains(Minecraft.theWorld.getBlockState(pos9.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }

    private static class BlockData {
        public final BlockPos pos;
        public final EnumFacing face;

        private BlockData(final BlockPos pos, final EnumFacing face) {
            this.pos = pos;
            this.face = face;
        }
    }

    private boolean isValidItem(final Item item) {
        if (item instanceof ItemBlock) {
            final ItemBlock iBlock = (ItemBlock) item;
            final Block block = iBlock.getBlock();
            return !this.invalidBlocks.contains(block);
        }
        return false;
    }
}
