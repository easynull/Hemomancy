package ru.easynull.hemomancy.render.gui.book.element;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import ru.easynull.hemomancy.api.altar.TierManager;
import ru.easynull.hemomancy.data.HmBlockTagsProvider;
import ru.easynull.hemomancy.render.gui.book.PageGui;

import java.util.List;

import static ru.easynull.hemomancy.utils.HmClientUtils.cyclingItem;

public final class StructureElement implements PageElement {
    private final List<TierManager.Component> components;
    private final BlockState zeroState;
    private final int height;

    private final float offsetY;
    private float rotationYaw = -10;
    private float rotationPitch = 20;
    private float panX = 0;
    private float panY = 0;
    private float zoom = 1.5f;
    private TagKey<?> tagId = HmBlockTagsProvider.RUNES;

    private static final float MIN_ZOOM = 0.5f;
    private static final float MAX_ZOOM = 3.5f;

    public StructureElement(List<TierManager.Component> blocks, BlockState zeroState, int height, float offsetY) {
        this.components = blocks;
        this.zeroState = zeroState;
        this.height = height;
        this.offsetY = offsetY;
    }

    public StructureElement(List<TierManager.Component> blocks, BlockState zeroState, int height) {
        this(blocks, zeroState, height, 0);
    }

    @Override
    public int getHeight(int maxWidth) {
        return height + 6;
    }

    @Override
    public void render(GuiGraphics context, int x, int y, int mouseX, int mouseY, float delta) {
        x = x + 19;
        context.fill(RenderType.endGateway(), x - 11, y, x + 98, y + height - 1, 0xFFECE3D6);
        context.fill(x - 11, y, x + 98, y + height - 1, 0x32FF0000);
        context.renderOutline(x - 12, y, 111, height, 0xFFB8ADA1);
        context.enableScissor(x - 11, y + 1, x + 98, y + height - 1);

        PoseStack viewMatrices = context.pose();
        viewMatrices.pushPose();

        float centerX = x + PageGui.BOOK_WIDTH / 7.2f;
        float centerY = y + height / 2.4f;
        viewMatrices.translate(centerX, centerY, 100f);

        viewMatrices.translate(panX, offsetY + panY, 0);

        float scale = 10.0f * zoom;
        viewMatrices.scale(scale, -scale, scale);

        viewMatrices.mulPose(Axis.YP.rotationDegrees(rotationYaw));
        viewMatrices.mulPose(Axis.XP.rotationDegrees(rotationPitch));

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        for (TierManager.Component comp : components) {
            viewMatrices.pushPose();
            viewMatrices.translate(comp.pos.getX(), comp.pos.getY(), comp.pos.getZ());

            BlockState state = comp.state == null ? Blocks.POLISHED_DEEPSLATE.defaultBlockState() :
                    comp.isUniversal() ? Block.byItem(cyclingItem(Minecraft.getInstance().level, tagId, 80)).defaultBlockState() :
                    comp.state;
            renderBlock(context, state);
            viewMatrices.popPose();
        }

        if (zeroState != null) {
            viewMatrices.pushPose();
            viewMatrices.translate(0, 0, 0);
            renderBlock(context, zeroState);
            viewMatrices.popPose();
        }

        viewMatrices.popPose();
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        context.disableScissor();
    }

    private static void renderBlock(GuiGraphics gg, BlockState state) {
        Minecraft client = Minecraft.getInstance();
        BlockRenderDispatcher blockRenderer = client.getBlockRenderer();
        BakedModel model = blockRenderer.getBlockModel(state);

        VertexConsumer consumer = gg.bufferSource().getBuffer(ItemBlockRenderTypes.getChunkRenderType(state));

        PoseStack ps = gg.pose();
        PoseStack.Pose entry = ps.last();
        RandomSource random = RandomSource.create();

        for (Direction direction : Direction.values()) {
            renderQuads(entry, consumer, model.getQuads(state, direction, random));
        }
        renderQuads(entry, consumer, model.getQuads(state, null, random));

        gg.flush();
    }

    private static void renderQuads(PoseStack.Pose entry, VertexConsumer consumer, List<BakedQuad> quads) {
        for (BakedQuad quad : quads) {
            consumer.putBulkData(entry, quad, 1f, 1f, 1f, 1f, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        }
    }

    public StructureElement setUniversalTag(TagKey<?> tagId) {
        this.tagId = tagId;
        return this;
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

    // В 1.21.1 сигнатура метода mouseScrolled изменилась: теперь она принимает horizontalAmount и verticalAmount
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        float factor = verticalAmount > 0 ? 1.1f : 0.9f;
        zoom = Mth.clamp(zoom * factor, MIN_ZOOM, MAX_ZOOM);
        return true;
    }
}