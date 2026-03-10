package ru.easynull.hemomancy.api.mage;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.api.mage.quest.MageQuestManager.Quest;

public final class MagePlayer {
    public static final AttachmentType<Data> DATA = AttachmentRegistry.<Data>builder()
            .persistent(Data.CODEC)
            .initializer(Data::new)
            .copyOnDeath()
            .buildAndRegister(Hemomancy.path("blood_mage"));

    public static Data of(PlayerEntity player) {
        return player.getAttachedOrCreate(DATA);
    }

    public static void onInit(){}

    public static final class Data {
        public static final Codec<Data> CODEC = NbtCompound.CODEC.xmap(
                Data::new,
                Data::writeToNbt
        );

        public final NbtCompound data = new NbtCompound();

        public Data() {
            setLevel(0);
        }

        public Data(NbtCompound nbt) {
            this.data.copyFrom(nbt);
        }

        private NbtCompound writeToNbt() {
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
            NbtCompound questNbt = data.getCompound("Quest");
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

        public void updateData(NbtCompound nbt){
           if(!nbt.isEmpty()){
               data.copyFrom(nbt);
           }
        }
    }
}