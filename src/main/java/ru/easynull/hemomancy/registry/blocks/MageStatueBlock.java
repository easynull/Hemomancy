package ru.easynull.hemomancy.registry.blocks;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import ru.easynull.hemomancy.api.mage.MagePlayer;
import ru.easynull.hemomancy.api.mage.quest.MageQuestManager;
import ru.easynull.hemomancy.api.mage.quest.MageQuestManager.Quest;
import ru.easynull.hemomancy.net.OpenStatueGuiS2CPacket;
import ru.easynull.hemomancy.net.UpdateMageS2CPacket;

public final class MageStatueBlock extends Block {
    public MageStatueBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if(!level.isClientSide()) {
            MagePlayer.Data data = MagePlayer.of(player);
            Quest current = data.getCurrentQuest();
            if (current == null) {
                Quest newQuest = MageQuestManager.getRandomQuest(data.getLevel());
                data.setQuest(newQuest);
                player.playNotifySound(SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.AMBIENT, 1f, 1f);
            } else {
                if (current.tryComplete(player)) {
                    data.setLevel(data.getLevel() + 1);
                    data.setQuest(null);
                    player.playNotifySound(SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.AMBIENT, 1f, 1f);
                } else {
                    ServerPlayNetworking.send((ServerPlayer) player, new UpdateMageS2CPacket(data.data));
                    ServerPlayNetworking.send((ServerPlayer) player, new OpenStatueGuiS2CPacket());
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}