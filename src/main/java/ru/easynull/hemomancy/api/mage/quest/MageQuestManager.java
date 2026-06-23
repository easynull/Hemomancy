package ru.easynull.hemomancy.api.mage.quest;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.api.mage.MagePlayer;
import ru.easynull.hemomancy.api.mage.quest.task.CollectorTask;
import ru.easynull.hemomancy.api.mage.quest.task.KillerTask;
import ru.easynull.hemomancy.api.mage.quest.task.Task;
import ru.easynull.hemomancy.net.UpdateMageS2CPacket;

import java.util.*;

public final class MageQuestManager {
    private static final Map<ResourceLocation, Quest> QUESTS = new HashMap<>();

    public static void onInit() {
        MagePlayer.onInit();
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (source.getEntity() instanceof ServerPlayer player) {
                handleKillEntity(player, entity);
            }
        });

        add(Hemomancy.path("spider_silk_hunt"), 0,
                new KillerTask(ResourceLocation.tryBuild("minecraft", "spider"), 12),
                new CollectorTask(ResourceLocation.tryBuild("minecraft", "string"), 32));

        add(Hemomancy.path("creeper_powder"), 1,
                new KillerTask(ResourceLocation.tryBuild("minecraft", "creeper"), 10),
                new CollectorTask(ResourceLocation.tryBuild("minecraft", "gunpowder"), 48));

        add(Hemomancy.path("zombie_plague"), 2,
                new KillerTask(ResourceLocation.tryBuild("minecraft", "zombie"), 25));

        add(Hemomancy.path("blank_glyph_stockpile"), 3,
                new CollectorTask(Hemomancy.path("blank_glyph"), 64));

        add(Hemomancy.path("skeleton_bone_harvest"), 3,
                new KillerTask(ResourceLocation.tryBuild("minecraft", "skeleton"), 20),
                new CollectorTask(ResourceLocation.tryBuild("minecraft", "bone"), 40));

        add(Hemomancy.path("fortified_glyph_forge"), 4,
                new CollectorTask(Hemomancy.path("fortified_glyph"), 32));

        add(Hemomancy.path("crimson_glyph_ritual"), 5,
                new CollectorTask(Hemomancy.path("crimson_glyph"), 24));

        add(Hemomancy.path("water_lava_sigil_pair"), 6,
                new CollectorTask(Hemomancy.path("water_sigil"), 1),
                new CollectorTask(Hemomancy.path("lava_sigil"), 1));

        add(Hemomancy.path("weak_blood_orb_charge"), 6,
                new CollectorTask(Hemomancy.path("weak_blood_orb"), 1));

        add(Hemomancy.path("air_sigil_freedom"), 7,
                new CollectorTask(Hemomancy.path("air_sigil"), 1));

        add(Hemomancy.path("filled_glyph_mass"), 7,
                new CollectorTask(Hemomancy.path("filled_glyph"), 48));

        add(Hemomancy.path("demonic_glyph_corruption"), 8,
                new CollectorTask(Hemomancy.path("demonic_glyph"), 16));

        add(Hemomancy.path("capacity_rune_network"), 8,
                new CollectorTask(Hemomancy.path("capacity_rune"), 12));

        add(Hemomancy.path("infernal_glyph_apocalypse"), 9,
                new CollectorTask(Hemomancy.path("infernal_glyph"), 12));

        add(Hemomancy.path("crimson_steel_stock"), 9,
                new CollectorTask(Hemomancy.path("crimson_steel_ingot"), 32));

        add(Hemomancy.path("transcendental_orb_ascension"), 10,
                new CollectorTask(Hemomancy.path("transcendental_blood_orb"), 1));

        add(Hemomancy.path("dragon_eternal_conquest"), 10,
                new KillerTask(ResourceLocation.tryBuild("minecraft", "ender_dragon"), 1),
                new CollectorTask(ResourceLocation.tryBuild("minecraft", "dragon_egg"), 1),
                new CollectorTask(ResourceLocation.tryBuild("minecraft", "chorus_fruit"), 128),
                new KillerTask(ResourceLocation.tryBuild("minecraft", "enderman"), 100));

        add(Hemomancy.path("magnetism_sigil_collection"), 4,
                new CollectorTask(Hemomancy.path("magnetism_sigil"), 1));

        add(Hemomancy.path("growth_sigil_garden"), 5,
                new CollectorTask(Hemomancy.path("grow_sigil"), 1));

        add(Hemomancy.path("resonant_capacity_run"), 6,
                new CollectorTask(Hemomancy.path("resonant_capacity_rune"), 8));

        add(Hemomancy.path("relations_rune_link"), 7,
                new CollectorTask(Hemomancy.path("relations_rune"), 6));

        add(Hemomancy.path("desecrated_tool_set"), 8,
                new CollectorTask(Hemomancy.path("desecrated_pickaxe"), 1),
                new CollectorTask(Hemomancy.path("desecrated_axe"), 1));

        add(Hemomancy.path("transcendental_crystal_pile"), 9,
                new CollectorTask(Hemomancy.path("transcendental_crystal"), 16));

        add(Hemomancy.path("movement_sigil_hoard"), 9,
                new CollectorTask(Hemomancy.path("movement_sigil"), 1));

        add(Hemomancy.path("teleposition_sigil_voyage"), 10,
                new CollectorTask(Hemomancy.path("teleposition_sigil"), 1));

        add(Hemomancy.path("drainage_sigil_flood"), 6,
                new CollectorTask(Hemomancy.path("drainage_sigil"), 1));

        add(Hemomancy.path("resistance_sigil_armor"), 7,
                new CollectorTask(Hemomancy.path("resistance_sigil"), 1));

        add(Hemomancy.path("blank_rune_stockpile"), 3,
                new CollectorTask(Hemomancy.path("blank_rune"), 128));

        add(Hemomancy.path("speed_rune_rush"), 4,
                new CollectorTask(Hemomancy.path("speed_rune"), 16));

        add(Hemomancy.path("sacrifices_rune_blood"), 5,
                new CollectorTask(Hemomancy.path("sacrifices_rune"), 12));

        add(Hemomancy.path("master_blood_orb"), 8,
                new CollectorTask(Hemomancy.path("master_blood_orb"), 1));

        add(Hemomancy.path("archmage_blood_orb"), 9,
                new CollectorTask(Hemomancy.path("archmage_blood_orb"), 1));

        add(Hemomancy.path("crimson_ornament_decor"), 10,
                new CollectorTask(Hemomancy.path("crimson_ornament"), 32));
    }

    public static void add(ResourceLocation id, int lvlDiff, Task... tasks) {
        Quest quest = new Quest(id, lvlDiff, List.of(tasks));
        if (QUESTS.containsKey(id)) {
            throw new IllegalStateException("Quest already registered!");
        }
        QUESTS.put(quest.id(), quest);
    }

    public static Quest get(ResourceLocation id) {
        return QUESTS.get(id);
    }

    public static Collection<Quest> getAll() {
        return QUESTS.values();
    }

    public static Quest getRandomQuest(int playerLevel) {
        List<Quest> candidates = new ArrayList<>();
        Random random = new Random();

        for (Quest quest : QUESTS.values()) {
            if (quest.lvlDiff() <= playerLevel + 2 && quest.lvlDiff() >= playerLevel - 2) {
                candidates.add(quest);
            }
        }

        if (candidates.isEmpty()) {
            candidates.addAll(QUESTS.values());
        }
        return candidates.get(random.nextInt(candidates.size()));
    }

    private static void handleKillEntity(Player player, LivingEntity target) {
        MagePlayer.Data data = MagePlayer.of(player);
        Quest quest = data.getCurrentQuest();
        if (quest == null) return;

        boolean updated = false;
        for (Task task : quest.tasks()) {
            if (task.getType() == Task.Type.KILLER && task.matches(target) && !task.isCompleted()) {
                task.increment(1);
                updated = true;
            }
        }
        if (updated) {
            data.updateQuest(quest);
            ServerPlayNetworking.send((ServerPlayer) player, new UpdateMageS2CPacket(data.getCurrentQuest().toNbt()));
        }
    }

    public record Quest(ResourceLocation id, int lvlDiff, List<Task> tasks) {
        public CompoundTag toNbt() {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("Id", id.toString());
            nbt.putInt("Diff", lvlDiff);
            ListTag list = new ListTag();
            for (Task task : tasks) {
                list.add(task.toNbt());
            }
            nbt.put("Tasks", list);
            return nbt;
        }

        public static Quest fromNbt(CompoundTag nbt) {
            ResourceLocation id = ResourceLocation.tryParse(nbt.getString("Id"));
            int lvlDiff = nbt.getInt("Diff");
            ListTag list = nbt.getList("Tasks", 10);
            List<Task> tasks = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                tasks.add(Task.fromNbt(list.getCompound(i)));
            }
            return new Quest(id, lvlDiff, tasks);
        }

        public boolean isCompleted(Player player) {
            for (Task task : tasks) {
                if (task.getType() == Task.Type.KILLER && !task.isCompleted()) return false;
                if (task.getType() == Task.Type.COLLECTOR) {
                    CollectorTask collect = (CollectorTask) task;
                    if (player.getInventory().countItem(BuiltInRegistries.ITEM.get(collect.getTarget())) < collect.getRequired())
                        return false;
                }
            }
            return true;
        }

        public boolean tryComplete(Player player) {
            if (!isCompleted(player)) return false;

            for (Task task : tasks) {
                if (task.getType() == Task.Type.COLLECTOR) {
                    CollectorTask collect = (CollectorTask) task;
                    Item item = BuiltInRegistries.ITEM.get(collect.getTarget());
                    player.getInventory().removeItem(player.getInventory().findSlotMatchingItem(item.getDefaultInstance()), collect.getRequired());
                }
            }
            return true;
        }
    }
}
