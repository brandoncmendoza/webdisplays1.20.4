/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package net.montoyo.wd.client.renderers;
import net.montoyo.wd.net.compat.NetworkContextCompat;
import net.minecraftforge.network.PacketDistributor;

import com.cinemamod.mcef.MCEFBrowser;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.montoyo.wd.WebDisplays;
import net.montoyo.wd.client.ClientProxy;
import net.montoyo.wd.config.ClientConfig;
import net.montoyo.wd.item.ItemMinePad2;

import static com.mojang.math.Axis.*;

@OnlyIn(Dist.CLIENT)
public final class MinePadRenderer implements IItemRenderer {
        private static final float PI = (float) Math.PI;
        private final Minecraft mc = Minecraft.getInstance();
        private final ResourceLocation tex = ResourceLocation.fromNamespaceAndPath("webdisplays", "textures/item/model/minepad.png");
        private final ModelMinePad model = new ModelMinePad();
        private final ClientProxy clientProxy = (ClientProxy) WebDisplays.PROXY;

        private float sinSqrtSwingProg1;
        private float sinSqrtSwingProg2;
        private float sinSwingProg1;
        private float sinSwingProg2;

        public static boolean renderAtSide(float handSideSign) {
                float relSide = handSideSign;
                if (Minecraft.getInstance().player.getMainArm() == HumanoidArm.LEFT) relSide *= -1;

                boolean sideHold = Minecraft.getInstance().player.isShiftKeyDown() != ClientConfig.sidePad;
                if (
                                (relSide < 0 && Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ItemMinePad2) ||
                                                (relSide > 0 && Minecraft.getInstance().player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ItemMinePad2)
                ) sideHold = true;

                return sideHold;
        }

        @Override
        public final boolean render(PoseStack stack, ItemStack is, float handSideSign, float swingProgress, float equipProgress, MultiBufferSource multiBufferSource, int packedLight) {
                float sqrtSwingProg = (float) Math.sqrt(swingProgress);
                sinSqrtSwingProg1 = (float) Math.sin(sqrtSwingProg * PI);
                sinSqrtSwingProg2 = (float) Math.sin(sqrtSwingProg * PI * 2.0f);
                sinSwingProg1 = (float) Math.sin(swingProgress * PI);
                sinSwingProg2 = (float) Math.sin(swingProgress * swingProgress * PI);

                boolean sideHold = renderAtSide(handSideSign);

                stack.pushPose();
                renderArmFirstPerson(stack, multiBufferSource, packedLight, equipProgress, handSideSign);
                stack.popPose();

                stack.pushPose();
                stack.translate(handSideSign * -0.4f * sinSqrtSwingProg1, 0.2f * sinSqrtSwingProg2, -0.2f * sinSwingProg1);
                stack.translate(handSideSign * 0.56f, -0.52f - equipProgress * 0.6f, -0.72f);
                stack.mulPose(YP.rotationDegrees(handSideSign * (45.0f - sinSwingProg2 * 20.0f)));
                stack.mulPose(ZP.rotationDegrees(handSideSign * sinSqrtSwingProg1 * -20.0f));
                stack.mulPose(XP.rotationDegrees(sinSqrtSwingProg1 * -80.0f));
                stack.mulPose(YP.rotationDegrees(handSideSign * -45.0f));

                if (sideHold) {
                        stack.translate(0.0f, 0.0f, -0.2f);
                        stack.mulPose(YP.rotationDegrees(20.0f * -handSideSign));
                        float total = 0.475f;
                        float off = -0.025f;
                        stack.translate(-(total - off) + (off * handSideSign), -0.1f, 0.0f);
                        stack.mulPose(ZP.rotationDegrees(1.0f));
                } else if (handSideSign >= 0)
                        stack.translate(-1.065f, 0.0f, 0.0f);
                else
                        stack.translate(0.065f, 0.0f, 0.0f);

                stack.translate(0.063f, 0.28f, 0.001f);
                model.render(multiBufferSource, stack);
                stack.translate(-0.063f, -0.28f, -0.001f);

                if (is.getTag() != null && is.getTag().contains("PadID")) {
                        ClientProxy.PadData pd = clientProxy.getPadByID(is.getTag().getUUID("PadID"));
                        if (pd != null) {
                                float x1 = 0.0f;
                                float y1 = 0.0f;
                                float x2 = (float)(27.65 / 32.0 + 0.01);
                                float y2 = (float)(14.0 / 32.0 + 0.002);

                                stack.translate(0.063f, 0.28f, 0.001f);
                                RenderSystem.disableDepthTest();
                                
                                // Verificar si el browser está inicializado y listo
                                boolean browserReady = false;
                                if (pd.view != null && pd.view instanceof com.cinemamod.mcef.MCEFBrowser) {
                                    com.cinemamod.mcef.MCEFBrowser mcefBrowser = (com.cinemamod.mcef.MCEFBrowser) pd.view;
                                    if (mcefBrowser.getRenderer() != null && mcefBrowser.getRenderer().getTextureID() != 0) {
                                        browserReady = true;
                                        RenderSystem.setShader(GameRenderer::getPositionTexLightmapColorShader);
                                        RenderSystem.setShaderTexture(0, mcefBrowser.getRenderer().getTextureID());
                                    }
                                }
                                
                                if (!browserReady) {
                                    // Browser no listo, usar shader de color
                                    RenderSystem.setShader(GameRenderer::getPositionColorLightmapShader);
                                }
                                
                                Tesselator t = Tesselator.getInstance();
                                BufferBuilder buffer = t.getBuilder();
                                
                                if (browserReady) {
                                    // Renderizar contenido web con texturas
                                    buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR);
                                    buffer.vertex(stack.last().pose(), x1, y1, 0.0f).uv(0.0F, 1.0F).uv2(packedLight).color(255, 255, 255, 255).endVertex();
                                    buffer.vertex(stack.last().pose(), x2, y1, 0.0f).uv(1.0F, 1.0F).uv2(packedLight).color(255, 255, 255, 255).endVertex();
                                    buffer.vertex(stack.last().pose(), x2, y2, 0.0f).uv(1.0F, 0.0F).uv2(packedLight).color(255, 255, 255, 255).endVertex();
                                    buffer.vertex(stack.last().pose(), x1, y2, 0.0f).uv(0.0F, 0.0F).uv2(packedLight).color(255, 255, 255, 255).endVertex();
                                } else {
                                    // Pantalla negra de carga
                                    buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_LIGHTMAP);
                                    buffer.vertex(stack.last().pose(), x1, y1, 0.0f).color(0, 0, 0, 255).uv2(packedLight).endVertex();
                                    buffer.vertex(stack.last().pose(), x2, y1, 0.0f).color(0, 0, 0, 255).uv2(packedLight).endVertex();
                                    buffer.vertex(stack.last().pose(), x2, y2, 0.0f).color(0, 0, 0, 255).uv2(packedLight).endVertex();
                                    buffer.vertex(stack.last().pose(), x1, y2, 0.0f).color(0, 0, 0, 255).uv2(packedLight).endVertex();
                                }
                                t.end();
                                RenderSystem.enableDepthTest();
                        }
                }

                return true;
        }

        private void renderArmFirstPerson(PoseStack stack, MultiBufferSource buffer, int combinedLight, float equipProgress, float handSideSign) {
                float tx = -0.3f * sinSqrtSwingProg1;
                float ty = 0.4f * sinSqrtSwingProg2;
                float tz = -0.4f * sinSwingProg1;

                stack.translate(handSideSign * (tx + 0.64000005f), ty - 0.6f - equipProgress * 0.6f, tz - 0.71999997f);
                stack.mulPose(YP.rotationDegrees(handSideSign * 45.0f));
                stack.mulPose(YP.rotationDegrees(handSideSign * sinSqrtSwingProg1 * 70.0f));
                stack.mulPose(ZP.rotationDegrees(handSideSign * sinSwingProg2 * -20.0f));
                stack.translate(-handSideSign, 3.6f, 3.5f);
                stack.mulPose(ZP.rotationDegrees(handSideSign * 120.0f));
                stack.mulPose(XP.rotationDegrees(200.0f));
                stack.mulPose(YP.rotationDegrees(handSideSign * -135.0f));
                stack.translate(handSideSign * 5.6f, 0.0f, 0.0f);

                PlayerRenderer playerRenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().getRenderer(mc.player);

                if (handSideSign >= 0.0f)
                        playerRenderer.renderRightHand(stack, buffer, combinedLight, mc.player);
                else
                        playerRenderer.renderLeftHand(stack, buffer, combinedLight, mc.player);
        }
}
