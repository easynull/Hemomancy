package ru.easynull.hemomancy.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import ru.easynull.hemomancy.HemomancyClient;
import ru.easynull.hemomancy.registry.blocks.type.AlchemyTableBlockEntity;
import ru.easynull.hemomancy.api.Transform;

public final class AlchemyTableRenderer implements BlockEntityRenderer<AlchemyTableBlockEntity> {

    public AlchemyTableRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(AlchemyTableBlockEntity alchemy, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        Transform.create(matrices, t -> {
            t.move(0.5f, 0.0f, 0.5f);

            boolean crafting = alchemy.isCrafting();
            float progress = crafting ? (float) alchemy.progress / alchemy.needLP : 0f;
            float speed = 0.1f + progress * 0.00360f;
            float time = (HemomancyClient.tickClient + tickDelta) * speed % 360;
            float radiusFactor = 1f - progress * progress;
            float baseRadius = crafting ? 0.9f * radiusFactor : 0.9f;

            ItemStack orbStack = alchemy.getItem(0);
            if (!orbStack.isEmpty()) {
                t.autoPose(() -> {
                    RandomSource rand = RandomSource.create(alchemy.getBlockPos().asLong());
                    t.move(-0.1f, 0.975f + (progress / 4.5f), -0.1f);
                    t.rotate(0, 45, Axis.YP.rotationDegrees(rand.nextInt(30, 120)));
                    t.rotate(90, 0, Axis.XP.rotationDegrees(90));
                    renderItem(orbStack, t.ms(), vertexConsumers, light, overlay);
                });
            }

            ItemStack resultStack = alchemy.getItem(1);
            if (!resultStack.isEmpty()) {
                t.autoPose(() -> {
                    t.move(-0.5f, 0.8f, -0.5f);
                    BloodAltarRenderer.renderMagicItem(resultStack, matrices, vertexConsumers, tickDelta, light, overlay, alchemy.getLevel());
                });
            }

            int itemCount = 0;
            int containerSize = alchemy.getContainerSize();
            for (int i = 2; i < containerSize; i++) {
                if (!alchemy.getItem(i).isEmpty()) itemCount++;
            }
            if (itemCount == 0) return;

            time = crafting ? time * (1f + progress) : time * 0.7f;
            int idx = 0;

            float globalTime = (HemomancyClient.tickClient + tickDelta) * 0.15f;

            for (int i = 2; i < containerSize; i++) {
                ItemStack stack = alchemy.getItem(i);
                if (stack.isEmpty()) continue;

                float angle = (float) (2 * Math.PI * idx / itemCount + time);
                float x = Mth.cos(angle) * baseRadius;
                float z = Mth.sin(angle) * baseRadius;

                float phase = (float) idx / itemCount;
                float wavePosition = (globalTime + phase * 2.0f * (float) Math.PI) % (2.0f * (float) Math.PI);

                float waveHeight;
                if (crafting) {
                    waveHeight = 0.4f + Mth.sin(wavePosition) * (0.25f + progress * 0.15f);
                } else {
                    waveHeight = 0.45f + Mth.sin(wavePosition) * 0.2f;
                }

                if (!crafting) {
                    float heavyWave = (float) Math.pow(Math.sin(wavePosition), 3);
                    waveHeight = 0.4f + heavyWave * 0.2f;
                }

                int finalIdx = idx;
                float finalWaveHeight = waveHeight;
                t.autoPose(() -> {
                    t.move(x, finalWaveHeight, z);

                    float tilt = Mth.sin(wavePosition) * 5f;
                    t.rotate(tilt, 0, new Quaternionf());

                    renderFloatingItem(t, stack, vertexConsumers, light, overlay, 0.5f, finalIdx);
                });

                idx++;
            }
        });
    }

    private static void renderFloatingItem(Transform t, ItemStack stack, MultiBufferSource vertexConsumers, int light, int overlay, float height, int index) {
        if (stack.isEmpty()) return;
        t.autoPose(() -> {
            t.move(0, height + 0.2f, 0);

            float rotationSpeed = 1.5f + (index * 0.2f);
            float rotationOffset = index * 45f;
            float deltaTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaTicks();
            t.rotate(0, deltaTicks * rotationSpeed + rotationOffset,
                    Axis.YP.rotationDegrees(360));

            t.scale(0, 0, 0, 0.8f, 0.8f, 0.8f);
            renderItem(stack, t.ms(), vertexConsumers, light, overlay);
        });
    }

    private static void renderItem(ItemStack stack, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack,
                ItemDisplayContext.GROUND,
                light,
                overlay,
                matrices,
                vertexConsumers,
                Minecraft.getInstance().level,
                0
        );
    }
}