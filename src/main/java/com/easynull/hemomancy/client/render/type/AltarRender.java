package com.easynull.hemomancy.client.render.type;

import com.easynull.hemomancy.registers.blocks.type.AltarBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mw.nullcore.Utils;
import com.mw.nullcore.client.render.ShyModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.joml.Matrix4f;

public final class AltarRender extends PedestalRender<AltarBE> {
    public AltarRender(BlockEntityRendererProvider.Context context) {
        super(context);
        this.itemHeight = 0.7f;
    }

    @Override
    public void render(AltarBE altar, float pTick, PoseStack ps, MultiBufferSource buffer, int light, int overlay) {
        super.render(altar, pTick, ps, buffer, light, overlay);
        float level = (float) altar.getLp(altar) / altar.getMaxLp();
        Utils.Render.renderCube(getFluid(level), ps, buffer.getBuffer(Sheets.cutoutBlockSheet()), 0x00FF0000, light, overlay);
    }

    private ShyModel getFluid(float fluidLevel) {
        return new ShyModel().setTexture(Utils.Render.getSpriteOf(IClientFluidTypeExtensions.of(Fluids.LAVA.getSource()).getStillTexture())).setMinX(0.1f).setMinY(0.3f).setMinZ(0.1f).setMaxX(0.9f).setMaxY(Mth.clamp(fluidLevel, 0, 0.7f)).setMaxZ(0.9f);
    }
}
