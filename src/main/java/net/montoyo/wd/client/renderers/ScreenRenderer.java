/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package net.montoyo.wd.client.renderers;
import net.montoyo.wd.client.ClientProxy;

import net.montoyo.wd.net.compat.NetworkContextCompat;
import net.minecraftforge.network.PacketDistributor;

import com.cinemamod.mcef.MCEFBrowser;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.montoyo.wd.WebDisplays;
import net.montoyo.wd.entity.ScreenBlockEntity;
import net.montoyo.wd.entity.ScreenData;
import net.montoyo.wd.utilities.math.Vector3f;
import net.montoyo.wd.utilities.math.Vector3i;
import org.jetbrains.annotations.NotNull;

import static com.mojang.math.Axis.*;

public class ScreenRenderer implements BlockEntityRenderer<ScreenBlockEntity> {
	public ScreenRenderer() {
	}
	
	public static class ScreenRendererProvider implements BlockEntityRendererProvider<ScreenBlockEntity> {
		@Override
		public @NotNull BlockEntityRenderer<ScreenBlockEntity> create(@NotNull Context arg) {
			return new ScreenRenderer();
		}
	}
	
	private final Vector3i tmpi = new Vector3i();
	private final Vector3f tmpf = new Vector3f();
	private final Vector3f mid = new Vector3f();
	
	@Override
    public void render(ScreenBlockEntity te, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!te.isLoaded())
            return;

        RenderSystem.disableBlend();

        for (int i = 0; i < te.screenCount(); i++) {
            ScreenData scr = te.getScreen(i);

            // Intentar crear browser si no existe y está en rango
            if (scr.browser == null) {
                double dist = ((ClientProxy) WebDisplays.PROXY).distanceTo(te, Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition());
                if (dist <= WebDisplays.INSTANCE.loadDistance2 * 16)
                    scr.createBrowser(te, true);
            }

            // Calcular posición central
            tmpi.set(scr.side.right);
            tmpi.mul(scr.size.x);
            tmpi.addMul(scr.side.up, scr.size.y);
            tmpf.set(tmpi);
            mid.set(0.5, 0.5, 0.5);
            mid.addMul(tmpf, 0.5f);
            tmpf.set(scr.side.left);
            mid.addMul(tmpf, 0.5f);
            tmpf.set(scr.side.down);
            mid.addMul(tmpf, 0.5f);

            poseStack.pushPose();
            poseStack.translate(mid.x, mid.y, mid.z);

            // Rotaciones según el lado
            switch (scr.side) {
                case BOTTOM:
                    poseStack.mulPose(XP.rotation((float) Math.toRadians(90.f + 49.8f)));
                    break;
                case TOP:
                    poseStack.mulPose(XN.rotation((float) Math.toRadians(90.f + 49.8f)));
                    break;
                case NORTH:
                    poseStack.mulPose(YN.rotationDegrees(180.f));
                    break;
                case SOUTH:
                    break;
                case WEST:
                    poseStack.mulPose(YN.rotationDegrees(90.f));
                    break;
                case EAST:
                    poseStack.mulPose(YP.rotationDegrees(90.f));
                    break;
            }

            // Animación de encendido
            if (scr.doTurnOnAnim) {
                long lt = System.currentTimeMillis() - scr.turnOnTime;
                float ft = ((float) lt) / 100.0f;
                if (ft >= 1.0f) {
                    ft = 1.0f;
                    scr.doTurnOnAnim = false;
                }
                poseStack.scale(ft, ft, 1.0f);
            }

            // Rotación de pantalla
            if (!scr.rotation.isNull)
                poseStack.mulPose(ZP.rotationDegrees(scr.rotation.angle));

            // Calcular dimensiones
            float sw = ((float) scr.size.x) * 0.5f - 2.f / 16.f;
            float sh = ((float) scr.size.y) * 0.5f - 2.f / 16.f;

            if (scr.rotation.isVertical) {
                float tmp = sw;
                sw = sh;
                sh = tmp;
            }

            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder builder = tesselator.getBuilder();
            RenderSystem.enableDepthTest();

            // ✅ RENDERIZAR: Negro si no hay browser, contenido web si hay
            if (scr.browser == null) {
                // Pantalla NEGRA mientras carga
                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                builder.vertex(poseStack.last().pose(), -sw, -sh, 0.505f).color(0.0f, 0.0f, 0.0f, 1.0f).endVertex();
                builder.vertex(poseStack.last().pose(), sw, -sh, 0.505f).color(0.0f, 0.0f, 0.0f, 1.0f).endVertex();
                builder.vertex(poseStack.last().pose(), sw, sh, 0.505f).color(0.0f, 0.0f, 0.0f, 1.0f).endVertex();
                builder.vertex(poseStack.last().pose(), -sw, sh, 0.505f).color(0.0f, 0.0f, 0.0f, 1.0f).endVertex();
                tesselator.end();
            } else {
                // Verificar que el browser y renderer estén listos
                MCEFBrowser mcefBrowser = (MCEFBrowser) scr.browser;
                if (mcefBrowser.getRenderer() == null || mcefBrowser.getRenderer().getTextureID() == 0) {
                    // Renderer no listo, mostrar negro
                    RenderSystem.setShader(GameRenderer::getPositionColorShader);
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                    builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                    builder.vertex(poseStack.last().pose(), -sw, -sh, 0.505f).color(0.0f, 0.0f, 0.0f, 1.0f).endVertex();
                    builder.vertex(poseStack.last().pose(), sw, -sh, 0.505f).color(0.0f, 0.0f, 0.0f, 1.0f).endVertex();
                    builder.vertex(poseStack.last().pose(), sw, sh, 0.505f).color(0.0f, 0.0f, 0.0f, 1.0f).endVertex();
                    builder.vertex(poseStack.last().pose(), -sw, sh, 0.505f).color(0.0f, 0.0f, 0.0f, 1.0f).endVertex();
                    tesselator.end();
                } else {
                    // Renderer listo, mostrar contenido web
                    RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
                    RenderSystem._setShaderTexture(0, mcefBrowser.getRenderer().getTextureID());
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                    builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
                builder.vertex(poseStack.last().pose(), -sw, -sh, 0.505f).uv(0.f, 1.f).color(1.f, 1.f, 1.f, 1.f).endVertex();
                builder.vertex(poseStack.last().pose(), sw, -sh, 0.505f).uv(1.f, 1.f).color(1.f, 1.f, 1.f, 1.f).endVertex();
                builder.vertex(poseStack.last().pose(), sw, sh, 0.505f).uv(1.f, 0.f).color(1.f, 1.f, 1.f, 1.f).endVertex();
                builder.vertex(poseStack.last().pose(), -sw, sh, 0.505f).uv(0.f, 0.f).color(1.f, 1.f, 1.f, 1.f).endVertex();
                tesselator.end();
                }
            }

            RenderSystem.disableDepthTest();
            poseStack.popPose();
        }
    }
}
