package xcykrix.harderendportal.block;

import com.google.common.base.Predicates;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import xcykrix.harderendportal.predicate.EndPortalPredicate;
import xcykrix.harderendportal.properties.EyeState;
import xcykrix.harderendportal.util.QReg;

public class EndPortal extends Block {
        // Create Propterties
        public static final EnumProperty<EyeState> EYE_STATE = EnumProperty.of("eye_state", EyeState.class);
        public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

        // Create Outline
        protected static final VoxelShape NO_EYE_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);
        protected static final VoxelShape EYE_SHAPE = VoxelShapes.union(NO_EYE_SHAPE,
                        Block.createCuboidShape(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D));

        // Create Block Instance
        private static String path;
        public static EndPortal block;

        public static void register() {
                path = "end_portal";
                block = new EndPortal(
                        FabricBlockSettings.create()
                                        .luminance(3)
                                        .sounds(BlockSoundGroup.GLASS)
                                        .strength(-1.0f, 3600000.0f)
                                        .dropsNothing());
                QReg.block(path, block)
                                .build();
                QReg.item(path, new BlockItem(
                                block,
                                new FabricItemSettings()
                                                .fireproof()))
                                .creative(ItemGroups.FUNCTIONAL, Items.END_PORTAL_FRAME, false)
                                .build();
        }

        private EndPortal(Settings settings) {
                super(settings);
                setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(EYE_STATE, EyeState.EMPTY));
        }

        // Custom Code
        public static boolean hasEyeInPortalFrame(BlockState state) {
                return state.get(EYE_STATE) != EyeState.EMPTY;
        }

        public static BlockPattern getPortalShape(EyeState eyeState, Boolean filled) {
                return BlockPatternBuilder.start()
                                .aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?")
                                .where('?', CachedBlockPosition
                                                .matchesBlockState(BlockStatePredicate.forBlock(Blocks.AIR)))
                                .where('^',
                                                CachedBlockPosition.matchesBlockState(
                                                                EndPortalPredicate
                                                                                .facing(Direction.SOUTH)
                                                                                .withoutEye(eyeState)
                                                                                .requireAncientFrame(filled)
                                                                                .or(BlockStatePredicate.forBlock(
                                                                                                Blocks.END_PORTAL_FRAME)
                                                                                                .with(EndPortalFrameBlock.EYE,
                                                                                                                Predicates.equalTo(
                                                                                                                                true)))))
                                .where('>',
                                                CachedBlockPosition.matchesBlockState(
                                                                EndPortalPredicate
                                                                                .facing(Direction.WEST)
                                                                                .withoutEye(eyeState)
                                                                                .requireAncientFrame(filled)
                                                                                .or(BlockStatePredicate.forBlock(
                                                                                                Blocks.END_PORTAL_FRAME)
                                                                                                .with(EndPortalFrameBlock.EYE,
                                                                                                                Predicates.equalTo(
                                                                                                                                true)))))
                                .where('v',
                                                CachedBlockPosition.matchesBlockState(
                                                                EndPortalPredicate
                                                                                .facing(Direction.NORTH)
                                                                                .withoutEye(eyeState)
                                                                                .requireAncientFrame(filled)
                                                                                .or(BlockStatePredicate.forBlock(
                                                                                                Blocks.END_PORTAL_FRAME)
                                                                                                .with(EndPortalFrameBlock.EYE,
                                                                                                                Predicates.equalTo(
                                                                                                                                true)))))
                                .where('<',
                                                CachedBlockPosition.matchesBlockState(
                                                                EndPortalPredicate
                                                                                .facing(Direction.EAST)
                                                                                .withoutEye(eyeState)
                                                                                .requireAncientFrame(filled)
                                                                                .or(BlockStatePredicate.forBlock(
                                                                                                Blocks.END_PORTAL_FRAME)
                                                                                                .with(EndPortalFrameBlock.EYE,
                                                                                                                Predicates.equalTo(
                                                                                                                                true)))))
                                .build();
        }

        public static boolean EndPortalAbsent(WorldView worldView, BlockState state, BlockPos pos) {
                BlockPattern.Result pattern = getPortalShape(state.get(EndPortal.EYE_STATE), false)
                                .searchAround(worldView, pos);
                return pattern != null;
        }

        // NM Override Code
        @Override
        protected void appendProperties(Builder<Block, BlockState> builder) {
                builder.add(EYE_STATE);
                builder.add(FACING);
        }

        @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
                return hasEyeInPortalFrame(state) ? EYE_SHAPE : NO_EYE_SHAPE;
        }

        @Override
        public BlockState getPlacementState(ItemPlacementContext ctx) {
                return this.getDefaultState()
                                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                                .with(EYE_STATE, EyeState.EMPTY);
        }

        @Override
        public BlockState rotate(BlockState state, BlockRotation rotation) {
                return state.with(FACING, rotation.rotate(state.get(FACING)));
        }

        @Override
        public BlockState mirror(BlockState state, BlockMirror mirror) {
                return state.rotate(mirror.getRotation(state.get(FACING)));
        }
}
