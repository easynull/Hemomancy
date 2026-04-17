package ru.easynull.hemomancy.render.type;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.joml.Quaternionf;
import ru.easynull.hemomancy.HemomancyClient;
import ru.easynull.hemomancy.registry.blocks.type.AlchemyTableBlockEntity;
import ru.easynull.hemomancy.api.Transform;

public final class AlchemyTableRenderer implements BlockEntityRenderer<AlchemyTableBlockEntity> {

    public AlchemyTableRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(AlchemyTableBlockEntity alchemy, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Transform.create(matrices, t -> {
            t.move(0.5f, 0.0f, 0.5f);

            boolean crafting = alchemy.isCrafting();
            float progress = crafting ? (float) alchemy.progress / alchemy.needLP : 0f;
            float speed = 0.1f + progress * 0.00360f;
            float time = (HemomancyClient.tickClient + tickDelta) * speed % 360;
            float radiusFactor = 1f - progress * progress;
            float baseRadius = crafting ? 0.9f * radiusFactor : 0.9f;

            ItemStack orbStack = alchemy.getStack(0);
            if (!orbStack.isEmpty()) {
                t.autoPose(() -> {
                    Random rand = Random.create(alchemy.getPos().asLong());
                    t.move(-0.1f, 0.975f + (progress / 4.5f), -0.1f);
                    t.rotate(0, 45, RotationAxis.POSITIVE_Y.rotationDegrees(rand.nextBetween(30, 120)));
                    t.rotate(90, 0, RotationAxis.POSITIVE_X.rotationDegrees(90));
                    renderItem(orbStack, t.ms(), vertexConsumers, light, overlay);
                });
            }

            ItemStack resultStack = alchemy.getStack(1);
            if (!resultStack.isEmpty()) {
                t.autoPose(() -> {
                    t.move(-0.5f, 0.8f, -0.5f);
                    BloodAltarRenderer.renderMagicItem(resultStack, matrices, vertexConsumers, tickDelta, light, overlay, alchemy.getWorld());
                });
            }

            int itemCount = 0;
            for (int i = 2; i < alchemy.size(); i++) {
                if (!alchemy.getStack(i).isEmpty()) itemCount++;
            }
            if (itemCount == 0) return;

            time = crafting ? time * (1f + progress) : time * 0.7f;
            int idx = 0;

            float globalTime = (HemomancyClient.tickClient + tickDelta) * 0.15f;

            for (int i = 2; i < alchemy.size(); i++) {
                ItemStack stack = alchemy.getStack(i);
                if (stack.isEmpty()) continue;

                float angle = (float) (2 * Math.PI * idx / itemCount + time);
                float x = MathHelper.cos(angle) * baseRadius;
                float z = MathHelper.sin(angle) * baseRadius;

                float phase = (float) idx / itemCount;
                float wavePosition = (globalTime + phase * 2.0f * (float) Math.PI) % (2.0f * (float) Math.PI);

                float waveHeight;
                if (crafting) {
                    waveHeight = 0.4f + MathHelper.sin(wavePosition) * (0.25f + progress * 0.15f);
                } else {
                    waveHeight = 0.45f + MathHelper.sin(wavePosition) * 0.2f;
                }

                if (!crafting) {
                    float heavyWave = (float) Math.pow(Math.sin(wavePosition), 3);
                    waveHeight = 0.4f + heavyWave * 0.2f;
                }

                int finalIdx = idx;
                float finalWaveHeight = waveHeight;
                t.autoPose(() -> {
                    t.move(x, finalWaveHeight, z);

                    float tilt = MathHelper.sin(wavePosition) * 5f;
                    t.rotate(tilt, 0, new Quaternionf());

                    renderFloatingItem(t, stack, light, overlay, 0.5f, finalIdx);
                });

                idx++;
            }
        });
    }

    private static void renderFloatingItem(Transform t, ItemStack stack, int light, int overlay, float height, int index) {
        if (stack.isEmpty()) return;
        t.autoPose(() -> {
            t.move(0, height + 0.2f, 0);

            float rotationSpeed = 1.5f + (index * 0.2f);
            float rotationOffset = index * 45f;
            t.rotate(0, MinecraftClient.getInstance().getTickDelta() * rotationSpeed + rotationOffset,
                    RotationAxis.POSITIVE_Y.rotationDegrees(360));

            t.scale(0, 0, 0, 0.8f, 0.8f, 0.8f);
            renderItem(stack, t.ms(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(), light, overlay);
        });
    }

    private static void renderItem(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                stack,
                ModelTransformationMode.GROUND,
                light,
                overlay,
                matrices,
                vertexConsumers,
                MinecraftClient.getInstance().world,
                0
        );
    }
}