package com.easynull.hemomancy.client.render.type;

import com.easynull.hemomancy.registers.blocks.type.AltarBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mw.nullcore.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public final class AltarRender implements BlockEntityRenderer<AltarBE> {
    final BlockEntityRendererProvider.Context context;

    public AltarRender(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(AltarBE altar, float pTick, PoseStack ps, MultiBufferSource buffer, int light, int overlay) {
        ps.pushPose();
        renderMoreItem(ps, buffer, altar.getFirst(),0.2f, altar.getLevel(), altar.getBlockPos(), Direction.UP);
        ps.popPose();
    }

    public static void renderMoreItem(PoseStack ps, MultiBufferSource bufferSource, ItemStack stack, float spreadFactor, Level level, BlockPos pos, Direction direction) {
        if (stack.isEmpty()) return;
        final RandomSource rand = RandomSource.create(pos.asLong());
        int count = stack.getCount();
        int light = LevelRenderer.getLightColor(level, level.getBlockState(pos), pos.relative(direction));

        ps.pushPose();
        for (int i = 0; i < count; i++) {
            ps.pushPose();
            float xOffset = (rand.nextFloat() - 0.5f) * spreadFactor;
            float yOffset = (rand.nextFloat() - 0.5f) * spreadFactor;
            float zOffset = (rand.nextFloat() - 0.5f) * spreadFactor;
            ps.translate(xOffset + 0.5f, yOffset + 1.1f, zOffset + 0.5f);
            ps.mulPose(Axis.YN.rotationDegrees(Utils.Render.getAnimationTick()));
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, ps, bufferSource, level, 0);
            ps.popPose();
        }
        ps.popPose();
    }
}
