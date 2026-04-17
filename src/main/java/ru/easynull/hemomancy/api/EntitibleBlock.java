package ru.easynull.hemomancy.api;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class EntitibleBlock extends BlockWithEntity {
    final Supplier<BlockEntityType<?>> type;

    protected EntitibleBlock(Settings settings, Supplier<BlockEntityType<?>> type) {
        super(settings);
        this.type = type;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return type.get().instantiate(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return ((world1, pos, state1, be) -> {
            if (be instanceof Tickable tick) tick.onTick();
        });
    }
}
