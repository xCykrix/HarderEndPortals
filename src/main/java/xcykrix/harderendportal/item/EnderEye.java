package xcykrix.harderendportal.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xcykrix.harderendportal.block.EndPortal;
import xcykrix.harderendportal.properties.EyeState;
import xcykrix.harderendportal.util.QReg;

public class EnderEye extends Item {

    // Create Item Instance
    private static String path;
    public static EnderEye item;

    public static void register(String stub) {
        path = stub;
        item = new EnderEye(
                new FabricItemSettings()
                        .fireproof()
                        .rarity(Rarity.EPIC)
                        .maxCount(16));
        QReg.item(path, item)
                .creative(ItemGroups.FUNCTIONAL, Items.ENDER_EYE, false)
                .build();
    }

    private EnderEye(Settings settings) {
        super(settings);
    }

    // Custom Code
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getBlockPos();
        BlockState blockstate = world.getBlockState(blockpos);
        Boolean frameHasEye = false;

        if (blockstate.isOf(EndPortal.block)) {
            frameHasEye = EndPortal.hasEyeInPortalFrame(blockstate);
        } else if (blockstate.isOf(Blocks.END_PORTAL_FRAME)) {
            frameHasEye = blockstate.get(EndPortalFrameBlock.EYE);
        } else {
            return ActionResult.PASS;
        }

        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        if (!frameHasEye) {
            EyeState eyeState = EyeState.getProperty(context.getStack().getItem());
            BlockState newBlockState = EndPortal.block.getDefaultState();
            newBlockState = newBlockState.with(HorizontalFacingBlock.FACING, blockstate.get(HorizontalFacingBlock.FACING));
            newBlockState = newBlockState.with(EndPortal.EYE_STATE, eyeState);

            Boolean abs = EndPortal.EndPortalAbsent(world, newBlockState, blockpos);
            if (abs) {
                Block.pushEntitiesUpBeforeBlockChange(blockstate, newBlockState, world, blockpos);
                world.setBlockState(blockpos, newBlockState, 2);
                context.getStack().decrement(1);
                world.syncWorldEvent(1503, blockpos, 0); // https://wiki.vg/Protocol - 1503 End Eye Placed

                BlockPattern.Result pattern = EndPortal.getPortalShape(EyeState.EMPTY, true).searchAround(world, blockpos);
                if (pattern != null) {
                    BlockPos blockpos1 = pattern.getFrontTopLeft().add(-3, 0, -3);

                    for (int i = 0; i < 3; ++i) {
                        for (int j = 0; j < 3; ++j) {
                            world.setBlockState(blockpos1.add(i, 0, j), Blocks.END_PORTAL.getDefaultState(), 2);
                        }
                    }

                    world.syncGlobalEvent(1038, blockpos1.add(1, 0, 1), 0); // https://wiki.vg/Protocol - 1038 End Portal Open
                }
                return ActionResult.CONSUME;
            }
            return ActionResult.PASS;
        } else if (blockstate.isOf(Blocks.END_PORTAL_FRAME)) {
            BlockState newBlockState = blockstate.with(EndPortalFrameBlock.EYE, false);
            world.setBlockState(blockpos, newBlockState, 2);
            world.spawnEntity(new ItemEntity(world, blockpos.getX(), blockpos.getY() + 1, blockpos.getZ(),
                    new ItemStack(Items.ENDER_EYE)));
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }
}
