package com.easynull.hemomancy.client.render.type;

import com.easynull.hemomancy.registers.blocks.type.AlchemyBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mw.nullcore.Utils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public final class AlchemyRender implements BlockEntityRenderer<AlchemyBE> {
    public AlchemyRender(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(AlchemyBE alchemy, float pTick, PoseStack ps, MultiBufferSource buffer, int light, int overlay) {
        int containerSize = alchemy.getContainerSize();
        float radius = 0.8f;
        float angleStep = (float) (6.5f * Math.PI / containerSize);
        float speed = 0.05f;

        for (int i = 0; i < containerSize; i++) {
            ItemStack stack = alchemy.getItem(i);
            if (!stack.isEmpty()) {
                float angle = i * angleStep + Utils.Render.getAnimationTick() * speed;
                float x = radius * (float) Math.sin(angle);
                float z = radius * (float) Math.cos(angle);
                ps.pushPose();
                ps.translate(x, 0, z);
                PedestalRender.renderMoreItem(ps, buffer, stack, 0.2f, alchemy.getLevel(), alchemy.getBlockPos(), Direction.UP, 1.2f);
                ps.popPose();
            }
        }
    }
}
