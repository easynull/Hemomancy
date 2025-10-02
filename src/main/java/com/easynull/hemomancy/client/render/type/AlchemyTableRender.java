package com.easynull.hemomancy.client.render.type;

import com.easynull.hemomancy.registers.blocks.type.AlchemyTableBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mw.nullcore.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class AlchemyTableRender implements BlockEntityRenderer<AlchemyTableBE> {
    public AlchemyTableRender(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(AlchemyTableBE alchemy, float pTick, PoseStack ps, MultiBufferSource buffer, int light, int overlay) {
        int size = alchemy.getContainerSize();
        boolean crafting = alchemy.isCrafting();
        float progress = crafting ? (float) alchemy.progress / alchemy.needLP : 0f;
        float speed = 0.1f + progress * 0.2f;
        float time = Utils.Render.getAnimationTick() * speed;
        float radiusFactor = 1f - progress * progress;
        float baseRadius = crafting ? 0.9f * radiusFactor : 0.9f;

        ItemStack orbStack = alchemy.getItem(0);
        if (!orbStack.isEmpty()) {
            ps.pushPose();
            ps.translate(0.58, 0.975 + (progress / 4.5f), 0.58);
            ps.mulPose(Axis.YP.rotationDegrees(45));
            ps.mulPose(Axis.XN.rotationDegrees(90));
            Minecraft.getInstance().getItemRenderer().renderStatic(orbStack, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, ps, buffer, alchemy.getLevel(), 0);
            ps.popPose();
        }

        ItemStack resultStack = alchemy.getItem(1);
        if (!resultStack.isEmpty()) {
            ps.pushPose();
            PedestalRender.renderMoreItem(ps, buffer, resultStack, 0.2f, alchemy.getLevel(), alchemy.getBlockPos(), Direction.UP, 1.15f);
            ps.popPose();
        }

        int itemCount = 0;
        for (int i = 2; i < size; i++) {
            if (!alchemy.getItem(i).isEmpty()) itemCount++;
        }
        if (itemCount == 0) return;
        time = crafting ? time * (1f + progress) : time;
        int idx = 0;
        for (int i = 2; i < size; i++) {
            ItemStack stack = alchemy.getItem(i);
            if (stack.isEmpty()) continue;
            float angle = (float) (2 * Math.PI * idx++ / itemCount + time);
            float x = (float) Math.cos(angle) * baseRadius;
            float z = (float) Math.sin(angle) * baseRadius;
            ps.pushPose();
            ps.translate(x, 0.6, z);
            PedestalRender.renderMoreItem(ps, buffer, stack, 0.2f, alchemy.getLevel(), alchemy.getBlockPos(), Direction.UP, 0.45f);
            ps.popPose();
        }
    }
}
