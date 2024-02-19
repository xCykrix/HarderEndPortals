package xcykrix.harderendportal.predicate;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.util.math.Direction;
import xcykrix.harderendportal.block.EndPortal;
import xcykrix.harderendportal.properties.EyeState;

public class EndPortalPredicate implements Predicate<BlockState> {
    private final ImmutableList<Block> validBlocks = ImmutableList.of(
            Blocks.END_PORTAL_FRAME,
            EndPortal.block);

    private final Direction direction;
    private EyeState excludedEye;
    private boolean requireEndPortal = false;

    private EndPortalPredicate(Direction directionIn) {
        this.direction = directionIn;
    }

    public boolean test(@Nullable BlockState blockstate) {
        if (blockstate != null && this.validBlocks.contains(blockstate.getBlock())) {
            if ((!blockstate.getBlock().equals(EndPortal.block)) && this.requireEndPortal) {
                return false;
            }

            if (blockstate.getBlock().equals(EndPortal.block) && this.excludedEye != null) {
                if (blockstate.get(EndPortal.EYE_STATE).equals(excludedEye)) {
                    return false;
                }
            }

            return blockstate.get(HorizontalFacingBlock.FACING) == this.direction;
        } else {
            return false;
        }
    }

    public static EndPortalPredicate facing(Direction direction) {
        return new EndPortalPredicate(direction);
    }

    public EndPortalPredicate withoutEye(EyeState eye) {
        this.excludedEye = eye;
        return this;
    }

    public EndPortalPredicate requireAncientFrame(boolean v) {
        this.requireEndPortal = v;
        return this;
    }
}
