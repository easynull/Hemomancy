package ru.easynull.hemomancy.render.types;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import ru.easynull.hemomancy.HemomancyClient;
import ru.easynull.hemomancy.registry.blocks.type.BloodAltarBE;
import ru.easynull.hemomancy.api.Transform;

public final class BloodAltarRenderer implements BlockEntityRenderer<BloodAltarBE> {
    private static final ItemRenderer renderer = MinecraftClient.getInstance().getItemRenderer();

    public BloodAltarRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(BloodAltarBE altar, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        renderMagicItem(altar.getFirst(), matrices, vertexConsumers, tickDelta, light, overlay, altar.getWorld());

        float level = (float) altar.getLp(altar) / altar.getMaxLp();
        if (level > 0) {
            renderFluid(matrices, vertexConsumers, level, light, overlay);
        }
    }

    public static void renderMagicItem(ItemStack stack, MatrixStack ms, VertexConsumerProvider consumers, float tickDelta, int light, int overlay, World world) {
        if (stack.isEmpty()) return;

        float time = HemomancyClient.tickClient + tickDelta;
        float hover = (float) Math.sin(time * 0.07f) *  0.07f + (stack.getItem() instanceof BlockItem ? 0.5f : 0.6f);

        float rotation = time * 0.8f;

        Transform.create(ms, trans -> {
            trans.start();
            trans.move(0.5f, hover, 0.5f);
            trans.rotate(0, 0, new Quaternionf().rotateY((float) Math.toRadians(rotation)));

            float tilt = (float) Math.sin(time * 0.05f) * 4f;
            trans.rotate(0, 0, new Quaternionf().rotateX((float) Math.toRadians(tilt)));
            trans.rotate(0, 0, new Quaternionf().rotateZ((float) Math.toRadians(tilt * 0.7f)));
            renderer.renderItem(stack, ModelTransformationMode.GROUND, light, overlay, ms, consumers, world, 0);
            trans.stop();
        });
    }

    private static void renderFluid(MatrixStack ms, VertexConsumerProvider consumers, float level, int light, int overlay) {
        ms.push();
        ms.translate(0.5f, 0.435f, 0.5f);
        ms.scale(0.52f, level * 0.42f, 0.52f);
        ms.translate(-0.5f, -0.435f, -0.5f);

        var sprite = FluidVariantRendering.getSprite(FluidVariant.of(Fluids.LAVA));
        if (sprite != null) {
            VertexConsumer consumer = consumers.getBuffer(RenderLayer.getTranslucent());

            float minU = sprite.getMinU();
            float maxU = sprite.getMaxU();
            float minV = sprite.getMinV();
            float maxV = sprite.getMaxV();

            int color = 0xF1FF0000;

            Matrix4f matrix = ms.peek().getPositionMatrix();

            consumer.vertex(matrix, 0, 1, 0).color(color).texture(minU, minV).light(light).overlay(overlay).normal(0, 1, 0).next();
            consumer.vertex(matrix, 0, 1, 1).color(color).texture(minU, maxV).light(light).overlay(overlay).normal(0, 1, 0).next();
            consumer.vertex(matrix, 1, 1, 1).color(color).texture(maxU, maxV).light(light).overlay(overlay).normal(0, 1, 0).next();
            consumer.vertex(matrix, 1, 1, 0).color(color).texture(maxU, minV).light(light).overlay(overlay).normal(0, 1, 0).next();

            consumer.vertex(matrix, 0, 0, 0).color(color).texture(minU, minV).light(light).overlay(overlay).normal(0, -1, 0).next();
            consumer.vertex(matrix, 1, 0, 0).color(color).texture(maxU, minV).light(light).overlay(overlay).normal(0, -1, 0).next();
            consumer.vertex(matrix, 1, 0, 1).color(color).texture(maxU, maxV).light(light).overlay(overlay).normal(0, -1, 0).next();
            consumer.vertex(matrix, 0, 0, 1).color(color).texture(minU, maxV).light(light).overlay(overlay).normal(0, -1, 0).next();

            consumer.vertex(matrix, 0, 0, 0).color(color).texture(minU, maxV).light(light).overlay(overlay).normal(0, 0, -1).next();
            consumer.vertex(matrix, 1, 0, 0).color(color).texture(maxU, maxV).light(light).overlay(overlay).normal(0, 0, -1).next();
            consumer.vertex(matrix, 1, 1, 0).color(color).texture(maxU, minV).light(light).overlay(overlay).normal(0, 0, -1).next();
            consumer.vertex(matrix, 0, 1, 0).color(color).texture(minU, minV).light(light).overlay(overlay).normal(0, 0, -1).next();

            consumer.vertex(matrix, 0, 0, 1).color(color).texture(minU, maxV).light(light).overlay(overlay).normal(0, 0, 1).next();
            consumer.vertex(matrix, 0, 1, 1).color(color).texture(minU, minV).light(light).overlay(overlay).normal(0, 0, 1).next();
            consumer.vertex(matrix, 1, 1, 1).color(color).texture(maxU, minV).light(light).overlay(overlay).normal(0, 0, 1).next();
            consumer.vertex(matrix, 1, 0, 1).color(color).texture(maxU, maxV).light(light).overlay(overlay).normal(0, 0, 1).next();

            consumer.vertex(matrix, 0, 0, 0).color(color).texture(minU, maxV).light(light).overlay(overlay).normal(-1, 0, 0).next();
            consumer.vertex(matrix, 0, 1, 0).color(color).texture(minU, minV).light(light).overlay(overlay).normal(-1, 0, 0).next();
            consumer.vertex(matrix, 0, 1, 1).color(color).texture(maxU, minV).light(light).overlay(overlay).normal(-1, 0, 0).next();
            consumer.vertex(matrix, 0, 0, 1).color(color).texture(maxU, maxV).light(light).overlay(overlay).normal(-1, 0, 0).next();

            consumer.vertex(matrix, 1, 0, 0).color(color).texture(minU, maxV).light(light).overlay(overlay).normal(1, 0, 0).next();
            consumer.vertex(matrix, 1, 0, 1).color(color).texture(maxU, maxV).light(light).overlay(overlay).normal(1, 0, 0).next();
            consumer.vertex(matrix, 1, 1, 1).color(color).texture(maxU, minV).light(light).overlay(overlay).normal(1, 0, 0).next();
            consumer.vertex(matrix, 1, 1, 0).color(color).texture(minU, minV).light(light).overlay(overlay).normal(1, 0, 0).next();

            ms.pop();
        }
    }
}
