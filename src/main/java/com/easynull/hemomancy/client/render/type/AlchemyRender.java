package com.easynull.hemomancy.client.render.type;

import com.easynull.hemomancy.registers.blocks.type.AlchemyBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mw.nullcore.Utils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class AlchemyRender implements BlockEntityRenderer<AlchemyBE> {
    public AlchemyRender(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(AlchemyBE alchemy, float pTick, PoseStack ps, MultiBufferSource buffer, int light, int overlay) {
        List<ItemStack> itemsToRender = new ArrayList<>();
        for (int i = 2; i < alchemy.getContainerSize(); i++) {
            ItemStack stack = alchemy.getItem(i);
            if (!stack.isEmpty()) {
                itemsToRender.add(stack);
            }
        }

        boolean crafting = alchemy.isCrafting();
        float time = Utils.Render.getAnimationTick() * 0.1f;

        ItemStack orbStack = alchemy.getItem(0);
        if (!orbStack.isEmpty()) {
            ps.pushPose();
            ps.translate(0, 0.15f, 0);
            PedestalRender.renderMoreItem(ps, buffer, orbStack, 0.2f, alchemy.getLevel(), alchemy.getBlockPos(), Direction.UP, 0.9f);
            ps.popPose();
        }

        ItemStack resultStack = alchemy.getItem(1);
        if (!resultStack.isEmpty()) {
            ps.pushPose();
            PedestalRender.renderMoreItem(ps, buffer, resultStack, 0.2f, alchemy.getLevel(), alchemy.getBlockPos(), Direction.UP, 1.5f);
            ps.popPose();
        }

        if (!itemsToRender.isEmpty()) {
            float progress = Math.min((float) alchemy.progress / alchemy.needLP, 1f);
            float baseRadius = crafting ? 1.2f * (1f - progress) : 1.2f;
            time = crafting ? 0.1f + progress * 2f * time : time;

            for (int i = 0; i < itemsToRender.size(); i++) {
                ItemStack itemStack = itemsToRender.get(i);
                float angle = (float) (2 * Math.PI * i / itemsToRender.size() + time);

                float x = (float) (Math.cos(angle) * baseRadius);
                float z = (float) (Math.sin(angle) * baseRadius);

                ps.pushPose();
                ps.translate(x, 0.6f, z);
                PedestalRender.renderMoreItem(ps, buffer, itemStack, 0.2f, alchemy.getLevel(), alchemy.getBlockPos(), Direction.UP, 0.9f);
                ps.popPose();
            }
        }
    }
}
