package cc.diablo.module.impl.render;

import cc.diablo.event.impl.Render3DEvent;
import cc.diablo.helpers.render.ChatHelper;
import cc.diablo.helpers.render.ColorHelper;
import cc.diablo.helpers.render.RenderUtils;
import cc.diablo.module.Category;
import cc.diablo.module.Module;
import cc.diablo.setting.impl.BooleanSetting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class XRay extends Module {
    public static List<BlockPos> blockPosList = new CopyOnWriteArrayList<>();
    public static BooleanSetting coalOre = new BooleanSetting("Coal Ore",false);
    public static BooleanSetting ironOre = new BooleanSetting("Iron Ore",false);
    public static BooleanSetting goldOre = new BooleanSetting("Gold Ore",false);
    public static BooleanSetting redstoneOre = new BooleanSetting("Redstone Ore",false);
    public static BooleanSetting diamondOre = new BooleanSetting("Diamond Ore",true);

    public XRay(){
        super("XRay","See through walls to fine le ores", Keyboard.KEY_NONE, Category.Render);
        this.addSettings(coalOre,ironOre,goldOre,redstoneOre,diamondOre);
    }

    @Override
    public void onEnable(){
        blockPosList.clear();
        mc.renderGlobal.loadRenderers();
        super.onEnable();
    }

    @Override
    public void onDisable(){
        blockPosList.clear();
        mc.renderGlobal.loadRenderers();
        super.onEnable();
    }

    @Subscribe
    public void onRender3D(Render3DEvent e){
        for (BlockPos object : blockPosList) {
            if(coalOre.isChecked()) {
                if (Block.getStateId(Minecraft.theWorld.getBlockState(object)) == 16) {
                    RenderUtils.drawLineToPosition(object, new Color(0,0,0).getRGB());
                }
            }
            if(ironOre.isChecked()) {
                if (Block.getStateId(Minecraft.theWorld.getBlockState(object)) == 15) {
                    RenderUtils.drawLineToPosition(object, new Color(139, 131, 131).getRGB());
                }
            }
            if(goldOre.isChecked()) {
                if (Block.getStateId(Minecraft.theWorld.getBlockState(object)) == 14) {
                    RenderUtils.drawLineToPosition(object, new Color(255, 204, 36).getRGB());
                }
            }
            if(redstoneOre.isChecked()) {
                if (Block.getStateId(Minecraft.theWorld.getBlockState(object)) == 73) {
                    RenderUtils.drawLineToPosition(object, new Color(255, 54, 54).getRGB());
                }
            }
            if(diamondOre.isChecked()) {
                if (Block.getStateId(Minecraft.theWorld.getBlockState(object)) == 56) {
                    //RenderUtils.drawLineToPosition(object, new Color(0, 187, 255).getRGB());
                    RenderUtils.drawLineToPosition(object, new Color(0, 187, 255).getRGB());
                }
            }
        }
    }
}
