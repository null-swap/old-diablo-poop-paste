package cc.diablo.event.impl;

import cc.diablo.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import java.util.List;

public class CollisionEvent extends Event
{
    private Block block;
    private BlockPos.MutableBlockPos blockPos;
    private List<AxisAlignedBB> list;
    public double x = 0;
    public double y = 0;
    public double z = 0;

    public CollisionEvent(final BlockPos.MutableBlockPos pos, Block block, final List<AxisAlignedBB> bList) {
        this.block = block;
        this.blockPos = pos;
        this.list = bList;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Block getBlock() {
        return block;
    }

    public BlockPos.MutableBlockPos getBlockPos() {
        return blockPos;
    }

    public List<AxisAlignedBB> getList() {
        return list;
    }
    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public void setBlock(final Block block) {
        this.block = block;
    }

    public void setBlockPos(final BlockPos.MutableBlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public void setList(final List<AxisAlignedBB> boundingBox) {
        this.list = boundingBox;
    }
}
