package ru.easynull.hemomancy.registry.blocks;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.easynull.hemomancy.api.mage.MagePlayer;
import ru.easynull.hemomancy.api.mage.quest.MageQuestManager;
import ru.easynull.hemomancy.api.mage.quest.MageQuestManager.Quest;
import ru.easynull.hemomancy.net.OpenStatueGuiS2CPacket;
import ru.easynull.hemomancy.net.UpdateMageS2CPacket;

public final class MageStatueBlock extends Block {
    public MageStatueBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient()) {
            MagePlayer.Data data = MagePlayer.of(player);
            Quest current = data.getCurrentQuest();
            if (current == null) {
                Quest newQuest = MageQuestManager.getRandomQuest(data.getLevel());
                data.setQuest(newQuest);
                player.playSound(SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.AMBIENT, 1f, 1f);
            } else {
                if (current.tryComplete(player)) {
                    data.setLevel(data.getLevel() + 1);
                    data.setQuest(null);
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1f, 1f);
                } else {
                    ServerPlayNetworking.send((ServerPlayerEntity) player, new UpdateMageS2CPacket(data.data));
                    ServerPlayNetworking.send((ServerPlayerEntity) player, new OpenStatueGuiS2CPacket());
                }
            }
        }
        return ActionResult.SUCCESS;
    }
}