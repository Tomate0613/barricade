package net.modgarden.barricade.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BarrierBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.modgarden.barricade.block.entity.AdvancedBarrierBlockEntity;
import org.jetbrains.annotations.Nullable;

public class AdvancedBarrierBlock extends BarrierBlock implements EntityBlock {
    public static final MapCodec<BarrierBlock> CODEC = simpleCodec(AdvancedBarrierBlock::new);

    public AdvancedBarrierBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public MapCodec<BarrierBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (level.getBlockEntity(pos) instanceof AdvancedBarrierBlockEntity blockEntity) {
            if (blockEntity.getBlockedEntities() == null || !blockEntity.getBlockedEntities().canPass(context)) {
                if (blockEntity.getBlockedDirections() != null && blockEntity.getBlockedDirections().doesNotBlock()) {
                    return Shapes.empty();
                } else if (blockEntity.getBlockedDirections() == null || blockEntity.getBlockedDirections().blocksAll()) {
                    return super.getCollisionShape(state, level, pos, context);
                } else {
                    Direction direction = blockEntity.getBlockedDirections().blockingDirection(pos, context);
                    if (direction == null) {
                        return Shapes.empty();
                    }
                    return super.getCollisionShape(state, level, pos, context);
                }
            }
        }
        return Shapes.empty();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AdvancedBarrierBlockEntity(pos, state);
    }
}
