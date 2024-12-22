package me.lofro.camerautils.mixins.client;

import me.lofro.camerautils.CameraUtils;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(at = @At("HEAD"), method = "tickFov")
    private void tickFov(CallbackInfo ci) {
        if (CameraUtils.ZOOM_TRACK != null) {
            CameraUtils.ZOOM_TRACK.tick();
        }
    }

    @ModifyConstant(method = "tickFov", constant = @Constant(floatValue = 1.5F))
    private float increaaseFloatValue(float constant) {
        return 2.0F;
    }

}
