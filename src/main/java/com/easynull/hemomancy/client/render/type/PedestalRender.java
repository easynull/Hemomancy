package com.easynull.hemomancy.client.render.type;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mw.nullcore.Utils;
import com.mw.nullcore.core.blocks.type.ContainerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PedestalRender<T extends ContainerBlockEntity> implements BlockEntityRenderer<T> {
    float itemHeight = 1.1f;

    public PedestalRender(BlockEntityRendererProvider.Context context){}

    @Override
    public void render(T altar, float pTick, PoseStack ps, MultiBufferSource buffer, int light, int overlay) {
        ps.pushPose();
        renderMoreItem(ps, buffer, altar.getFirst(), 0.2f, altar.getLevel(), altar.getBlockPos(), Direction.UP, itemHeight);
        ps.popPose();
    }

    public static void renderMoreItem(PoseStack ps, MultiBufferSource bufferSource, ItemStack stack, float spreadFactor, Level level, BlockPos pos, Direction direction, float height) {
        if (stack.isEmpty()) return;
        final RandomSource rand = RandomSource.create(pos.asLong());
        int count = stack.getCount();
        int light = LevelRenderer.getLightColor(level, level.getBlockState(pos), pos.relative(direction));

        ps.pushPose();
        for (int i = 0; i < count; i++) {
            ps.pushPose();
            float xOffset = count > 1 ? (rand.nextFloat() - 0.5f) * spreadFactor : 0f;
            float yOffset = (rand.nextFloat() - 0.5f) * spreadFactor;
            float zOffset = count > 1 ? (rand.nextFloat() - 0.5f) * spreadFactor : 0f;
            ps.translate(xOffset + 0.5f, yOffset + height + 0.06f * Mth.sin(Utils.Render.getAnimationTick() * 0.0360f), zOffset + 0.5f);
            ps.mulPose(Axis.YN.rotationDegrees(Utils.Render.getAnimationTick()));
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, ps, bufferSource, level, 0);
            ps.popPose();
        }
        ps.popPose();
    }
}
