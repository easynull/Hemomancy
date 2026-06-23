package ru.easynull.hemomancy.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import ru.easynull.hemomancy.registry.blocks.type.BloodAltarBlockEntity;

public final class BloodAltarRenderer implements BlockEntityRenderer<BloodAltarBlockEntity> {
    private static final ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

    public BloodAltarRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(BloodAltarBlockEntity altar, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        Level level = altar.getLevel();
        if (level == null) return;
        
        ItemStack stack = altar.getItem(0);
        if (!stack.isEmpty()) {
            renderMagicItem(stack, matrices, vertexConsumers, tickDelta, light, overlay, level);
        }

        FluidVariant fluidVariant = altar.fluidStorage.variant;
        if (fluidVariant != null && !fluidVariant.isBlank() && altar.getLp(altar) > 0) {
            TextureAtlasSprite sprite = FluidVariantRendering.getSprite(fluidVariant);
            int color = FluidVariantRendering.getColor(fluidVariant, level, altar.getBlockPos());

            if (sprite != null) {
                VertexConsumer consumer = vertexConsumers.getBuffer(RenderType.translucent());
                Matrix4f matrix = matrices.last().pose();

                float minU = sprite.getU0();
                float maxU = sprite.getU1();
                float minV = sprite.getV0();
                float maxV = sprite.getV1();

                float fluidHeight = 0.44f + 0.225f * ((float) altar.getLp(altar) / altar.getMaxLp());

                consumer.addVertex(matrix, 0.1f, fluidHeight, 0.1f).setColor(color).setUv(minU, minV).setLight(light).setOverlay(overlay).setNormal(0, 1, 0);
                consumer.addVertex(matrix, 0.1f, fluidHeight, 0.9f).setColor(color).setUv(minU, maxV).setLight(light).setOverlay(overlay).setNormal(0, 1, 0);
                consumer.addVertex(matrix, 0.9f, fluidHeight, 0.9f).setColor(color).setUv(maxU, maxV).setLight(light).setOverlay(overlay).setNormal(0, 1, 0);
                consumer.addVertex(matrix, 0.9f, fluidHeight, 0.1f).setColor(color).setUv(maxU, minV).setLight(light).setOverlay(overlay).setNormal(0, 1, 0);
            }
        }
    }

    public static void renderMagicItem(ItemStack stack, PoseStack matrices, MultiBufferSource vertexConsumers, float tickDelta, int light, int overlay, Level level) {
        if (stack.isEmpty()) return;
        matrices.pushPose();

        long gameTime = level != null ? level.getGameTime() : 0;
        float globalTime = (gameTime + tickDelta) * 0.05f;

        matrices.translate(0.5, 1.1 + Math.sin(globalTime) * 0.05, 0.5);
        matrices.mulPose(Axis.YP.rotationDegrees(globalTime * 30.0f));
        matrices.scale(0.5f, 0.5f, 0.5f);

        renderer.renderStatic(
                stack,
                ItemDisplayContext.GROUND,
                light,
                overlay,
                matrices,
                vertexConsumers,
                level,
                0
        );
        matrices.popPose();
    }
}