package ru.easynull.hemomancy.registry.fluids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import ru.easynull.hemomancy.registry.HmBlocks;
import ru.easynull.hemomancy.registry.HmFluids;
import ru.easynull.hemomancy.registry.HmItems;

import java.util.Optional;

public abstract class BloodFluid extends FlowingFluid {
    @Override
    public Fluid getFlowing() {
        return HmFluids.FLOWING_BLOOD;
    }

    @Override
    public Fluid getSource() {
        return HmFluids.BLOOD;
    }

    @Override
    public Item getBucket() {
        return HmItems.BLOOD_BUCKET;
    }

    @Override
    public void animateTick(Level level, BlockPos blockPos, FluidState fluidState, RandomSource randomSource) {
        if (!fluidState.isSource() && !(Boolean)fluidState.getValue(FALLING)) {
            if (randomSource.nextInt(64) == 0) {
                level.playLocalSound(
                        blockPos.getX() + 0.5,
                        blockPos.getY() + 0.5,
                        blockPos.getZ() + 0.5,
                        SoundEvents.WATER_AMBIENT,
                        SoundSource.BLOCKS,
                        randomSource.nextFloat() * 0.25F + 0.75F,
                        randomSource.nextFloat() + 0.5F,
                        false
                );
            }
        } else if (randomSource.nextInt(10) == 0) {
            level.addParticle(
                    ParticleTypes.UNDERWATER,
                    blockPos.getX() + randomSource.nextDouble(),
                    blockPos.getY() + randomSource.nextDouble(),
                    blockPos.getZ() + randomSource.nextDouble(),
                    0.0,
                    0.0,
                    0.0
            );
        }
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return level.getGameRules().getBoolean(GameRules.RULE_WATER_SOURCE_CONVERSION);
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        BlockEntity blockEntity = blockState.hasBlockEntity() ? levelAccessor.getBlockEntity(blockPos) : null;
        Block.dropResources(blockState, levelAccessor, blockPos, blockEntity);
    }

    @Override
    public int getSlopeFindDistance(LevelReader levelReader) {
        return 4;
    }

    @Override
    public BlockState createLegacyBlock(FluidState fluidState) {
        return HmBlocks.BLOOD.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(fluidState));
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == HmFluids.BLOOD || fluid == HmFluids.FLOWING_BLOOD;
    }

    @Override
    public int getDropOff(LevelReader levelReader) {
        return 1;
    }

    @Override
    public int getTickDelay(LevelReader levelReader) {
        return 5;
    }

    @Override
    public boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isSame(HmFluids.BLOOD);
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    public static class Flowing extends BloodFluid {
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return (Integer)fluidState.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return false;
        }
    }

    public static class Source extends BloodFluid {
        @Override
        public int getAmount(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return true;
        }
    }
}