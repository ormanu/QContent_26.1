package ormanu.qcontent.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class TeddyBearBlock extends HorizontalDirectionalBlock {

    private static final VoxelShape SHAPE_NORTH = Shapes.box(
            3.65 / 16.0, 0.0 / 16.0, 4.5 / 16.0,
            12.5 / 16.0, 14.0 / 16.0, 11.5 / 16.0
    );

    private static final VoxelShape SHAPE_EAST  = rotateY(SHAPE_NORTH, 1);
    private static final VoxelShape SHAPE_SOUTH = rotateY(SHAPE_NORTH, 2);
    private static final VoxelShape SHAPE_WEST  = rotateY(SHAPE_NORTH, 3);

    public TeddyBearBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
    }

    @Override
    protected @Nullable MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        // faces the player (same as furnace)
        return this.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(HorizontalDirectionalBlock.FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return this.rotate(state, mirror.getRotation(state.getValue(HorizontalDirectionalBlock.FACING)));
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, net.minecraft.core.BlockPos pos, CollisionContext context) {
        return getFacingShape(state);
    }

    // COLLISION shape (physical hitbox)
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, net.minecraft.core.BlockPos pos, CollisionContext context) {
        return getFacingShape(state);
    }

    private static VoxelShape getFacingShape(BlockState state) {
        return switch (state.getValue(FACING)) {
            case EAST  -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST  -> SHAPE_WEST;
            default    -> SHAPE_NORTH;
        };
    }

    /**
     * Rotates a VoxelShape around the Y axis (90° steps).
     * This works because VoxelShape AABBs are in 0..1 coordinates.
     */
    private static VoxelShape rotateY(VoxelShape shape, int times) {
        VoxelShape rotated = shape;
        for (int i = 0; i < times; i++) {
            VoxelShape next = Shapes.empty();
            for (var box : rotated.toAabbs()) {
                // (x,z) -> (1 - z, x) 90° rotation
                next = Shapes.or(next, Shapes.box(
                        1.0 - box.maxZ, box.minY, box.minX,
                        1.0 - box.minZ, box.maxY, box.maxX
                ));
            }
            rotated = next;
        }
        return rotated;
    }
}