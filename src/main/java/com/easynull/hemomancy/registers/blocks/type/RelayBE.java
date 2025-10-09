//package com.easynull.hemomancy.registers.blocks.type;
//
//import com.easynull.hemomancy.Hemomancy;
//import com.easynull.hemomancy.core.LpElement;
//import com.easynull.hemomancy.core.Wandable;
//import com.easynull.hemomancy.registers.HcBlockEntities;
//import com.easynull.hemomancy.utils.EnergyUtils;
//import com.mw.nullcore.Utils;
//import com.mw.nullcore.core.blocks.type.Tickable;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.nbt.Tag;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public final class RelayBE extends BlockEntity implements Tickable, LpElement, Wandable {
//    private String mode = "recessive";
//    private final List<BlockPos> childRelays = new ArrayList<>(3);
//    private BlockPos connectedRelay = null;
//
//    public RelayBE(BlockPos pos, BlockState blockState) {
//        super(HcBlockEntities.arterialRelay.get(), pos, blockState);
//    }
//
//    @Override
//    public void tick() {
//        if (level.isClientSide()) return;
//        childRelays.removeIf(pos -> {
//            boolean invalid = !(level.getBlockEntity(pos) instanceof RelayBE);
//            if (invalid) Hemomancy.LOGGER.info("Relay at {} removed invalid child at {}", worldPosition, pos);
//            return invalid;
//        });
//        if (level.getGameTime() % 60 == 0) {
//            Utils.Block.forEachCube(worldPosition, 8, pos -> {
//                if (pos.equals(worldPosition)) return;
//                BlockEntity be = level.getBlockEntity(pos);
//                if (be instanceof RelayBE target && target.getMode().equals("dominant") && childRelays.size() < 3 && !childRelays.contains(pos)) {
//                    childRelays.add(pos);
//                    Hemomancy.LOGGER.info("Relay at {} added dominant child at {}", worldPosition, pos);
//                }
//            });
//
//                childRelays.forEach(pos -> {
//                    if (level.getBlockEntity(pos.above()) instanceof LpElement target) {
//                        target.reducerLp(-200, level.getBlockEntity(pos.above()));
//                        Hemomancy.LOGGER.info("Lp element connectable!");
//                        EnergyUtils.extractInFrom(source, target, 100, true);
//                    }
//                });
//            if (mode.equals("recessive")) Hemomancy.LOGGER.info("This relay have {} child", childRelays.size());
//        }
//    }
//
//    @Override
//    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
//        super.saveAdditional(tag, registries);
//        tag.putString("mode", mode);
//        ListTag children = new ListTag();
//        for (BlockPos pos : childRelays) {
//            CompoundTag childTag = new CompoundTag();
//            childTag.putInt("x", pos.getX());
//            childTag.putInt("y", pos.getY());
//            childTag.putInt("z", pos.getZ());
//            children.add(childTag);
//        }
//        tag.put("children", children);
//        if (connectedRelay != null) {
//            CompoundTag connTag = new CompoundTag();
//            connTag.putInt("x", connectedRelay.getX());
//            connTag.putInt("y", connectedRelay.getY());
//            connTag.putInt("z", connectedRelay.getZ());
//            tag.put("connected", connTag);
//        }
//    }
//
//    @Override
//    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
//        super.loadAdditional(tag, registries);
//        mode = tag.getString("mode");
//        childRelays.clear();
//        ListTag children = tag.getList("children", Tag.TAG_COMPOUND);
//        for (Tag childTag : children) {
//            CompoundTag c = (CompoundTag) childTag;
//            childRelays.add(new BlockPos(c.getInt("x"), c.getInt("y"), c.getInt("z")));
//        }
//        if (tag.contains("connected")) {
//            CompoundTag conn = tag.getCompound("connected");
//            connectedRelay = new BlockPos(conn.getInt("x"), conn.getInt("y"), conn.getInt("z"));
//        }
//    }
//
//    @Override
//    public String[] getModes() {
//        return new String[]{"recessive", "dominant"};
//    }
//
//    @Override
//    public String getMode() {
//        return mode;
//    }
//
//    @Override
//    public void setMode(String mode) {
//        this.mode = mode;
//    }
//}
