package ru.easynull.hemomancy.api.mage;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.api.mage.quest.MageQuestManager.Quest;

public final class MagePlayer {
    public static final AttachmentType<Data> DATA = AttachmentRegistry.<Data>builder()
            .persistent(Data.CODEC)
            .syncWith(Data.STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
            .initializer(Data::new)
            .copyOnDeath()
            .buildAndRegister(Hemomancy.path("blood_mage"));

    public static Data of(Player player) {
        return player.getAttachedOrCreate(DATA);
    }

    public static void onInit(){}

    public static final class Data {
        public static final Codec<Data> CODEC = CompoundTag.CODEC.xmap(Data::new, Data::writeToNbt);
        public static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = ByteBufCodecs.COMPOUND_TAG.<RegistryFriendlyByteBuf>cast().map(Data::new, Data::writeToNbt);

        public final CompoundTag data = new CompoundTag();

        public Data() {
            setLevel(0);
        }

        public Data(CompoundTag nbt) {
            this.data.merge(nbt);
        }

        private CompoundTag writeToNbt() {
            return data.copy();
        }

        public int getLevel() {
            return data.getInt("Level");
        }

        public void setLevel(int lvl) {
            data.putInt("Level", Math.min(lvl, 10));
        }

        public Quest getCurrentQuest() {
            if (!data.contains("Quest")) return null;
            CompoundTag questNbt = data.getCompound("Quest");
            return Quest.fromNbt(questNbt);
        }

        public void setQuest(Quest quest) {
            if (quest == null) {
                data.remove("Quest");
            } else {
                updateQuest(quest);
            }
        }

        public void updateQuest(Quest updated) {
            if (updated != null) {
                data.put("Quest", updated.toNbt());
            }
        }

        public void updateData(CompoundTag nbt){
           if(!nbt.isEmpty()){
               data.merge(nbt);
           }
        }
    }
}