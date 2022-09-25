package dev.isxander.handsway.mixins;

import dev.isxander.handsway.config.HandSwayConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @Unique
    private float handSway$timer = 0f;

    @Inject(
            method = "renderFirstPersonItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;push()V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void swayHand(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, boolean mainHand, Arm arm) {
        float swayIntensity = HandSwayConfig.getInstance().swayIntensity;
        float swaySpeed = HandSwayConfig.getInstance().swaySpeed;

        float armDeviation = HandSwayConfig.getInstance().armDeviation;
        boolean invertOtherArm = HandSwayConfig.getInstance().invertOtherArm;

        float xSpeedModifier = HandSwayConfig.getInstance().xSpeedModifier;
        float xIntensityDampener = HandSwayConfig.getInstance().xIntensityDampener;
        float ySpeedModifier = HandSwayConfig.getInstance().ySpeedModifier;
        float yIntensityDampener = HandSwayConfig.getInstance().yIntensityDampener;

        if (swayIntensity > 0) {
            float time = handSway$timer * swaySpeed;
            if (arm == Arm.LEFT) time += armDeviation;

            double swayX = swayIntensity * MathHelper.sin(time * xSpeedModifier) / xIntensityDampener;
            double swayY = swayIntensity * MathHelper.cos(time * ySpeedModifier) / yIntensityDampener;

            if (arm == Arm.RIGHT || !invertOtherArm) {
                matrices.translate(swayX, swayY, 0);
            } else {
                matrices.translate(-swayX, -swayY, 0);
            }

            handSway$timer += client.getLastFrameDuration() / 1000 * 50;
            if (handSway$timer >= 3600f) handSway$timer = 0f;
        }
    }

}
