package ru.easynull.hemomancy.render.gui.book.element;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import ru.easynull.hemomancy.Hemomancy;
import ru.easynull.hemomancy.api.altar.Tier;
import ru.easynull.hemomancy.render.gui.book.PageGui;

import java.util.List;

import static ru.easynull.hemomancy.utils.HmClientUtils.getCyclingItem;

public final class StructureElement implements PageElement {
    private final List<Tier.Component> components;
    private final BlockState zeroState;
    private final int height;

    private float rotationYaw = -10;
    private float rotationPitch = 20;
    private float panX = 0;
    private float panY = 0;
    private float zoom = 1.5f;

    private static final float MIN_ZOOM = 0.5f;
    private static final float MAX_ZOOM = 3.5f;

    public StructureElement(List<Tier.Component> blocks, BlockState zeroState, int height) {
        this.components = blocks;
        this.zeroState = zeroState;
        this.height = height;
    }

    @Override
    public int getHeight(int maxWidth) {
        return height + 6;
    }

    @Override
    public void render(DrawContext context, int x, int y, int mouseX, int mouseY, float delta) {
        x = x + 19;
        context.fill(RenderLayer.getEndGateway(), x - 11, y, x + 98, y + height - 1, 0xFFECE3D6);
        context.fill(x - 11, y, x + 98, y + height - 1, 0x32FF0000);
        context.drawBorder(x - 12, y, 111, height, 0xFFB8ADA1);
        context.enableScissor(x - 11, y + 1, x + 98, y + height - 1);
        MatrixStack viewMatrices = RenderSystem.getModelViewStack();
        viewMatrices.push();
        viewMatrices.loadIdentity();

        float centerX = x + PageGui.BOOK_WIDTH / 7.2f;
        float centerY = y + height / 2.4f;
        viewMatrices.translate(centerX, centerY, 100f);

        viewMatrices.translate(panX, panY, 0);

        float scale = 10.0f * zoom;
        viewMatrices.scale(scale, -scale, scale);

        viewMatrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotationYaw));
        viewMatrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotationPitch));

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        for (Tier.Component comp : components) {
            viewMatrices.push();
            viewMatrices.translate(comp.pos.getX(), comp.pos.getY(), comp.pos.getZ());
            BlockState state = comp.state == null ? Blocks.POLISHED_DEEPSLATE.getDefaultState() :
                    comp.isUpgrade() ?
                            Block.getBlockFromItem(getCyclingItem(MinecraftClient.getInstance().world,
                                    TagKey.of(RegistryKeys.ITEM, Hemomancy.path("runes")), 80)).getDefaultState() :
                            comp.state;
            renderBlock(viewMatrices, state);
            viewMatrices.pop();
        }

        if (zeroState != null) {
            viewMatrices.push();
            viewMatrices.translate(0, 0, 0);
            renderBlock(viewMatrices, zeroState);
            viewMatrices.pop();
        }
        viewMatrices.pop();
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        context.disableScissor();
    }

    private static void renderBlock(MatrixStack matrices, BlockState state) {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockRenderManager blockRenderer = client.getBlockRenderManager();
        BakedModel model = blockRenderer.getModel(state);

        RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapProgram);
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);

        MatrixStack.Entry entry = matrices.peek();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);

        Random random = Random.create();
        for (Direction direction : Direction.values()) {
            renderQuads(entry, buffer, model.getQuads(state, direction, random));
        }
        renderQuads(entry, buffer, model.getQuads(state, null, random));

        Tessellator.getInstance().draw();
    }

    private static void renderQuads(MatrixStack.Entry entry, VertexConsumer consumer, List<BakedQuad> quads) {
        for (BakedQuad quad : quads) {
            consumer.quad(entry, quad, 1f, 1f, 1f, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button == 0) {
            rotationYaw += (float) (deltaX * 0.5f);
            rotationPitch += (float) (deltaY * 0.5f);
            return true;
        } else if (button == 1) {
            panX += (float) (deltaX);
            panY += (float) (deltaY);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        float factor = amount > 0 ? 1.1f : 0.9f;
        zoom = MathHelper.clamp(zoom * factor, MIN_ZOOM, MAX_ZOOM);
        return true;
    }
}
