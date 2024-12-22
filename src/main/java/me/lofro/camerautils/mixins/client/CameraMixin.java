package me.lofro.camerautils.mixins.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow protected abstract double getMaxZoom(double p_90567_);

    @Shadow protected abstract void move(double p_90569_, double p_90570_, double p_90571_);

    @Redirect(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;move(DDD)V", ordinal = 0))
    private void move(Camera instance, double p_90569_, double p_90570_, double p_90571_) {
        if (!Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            move(-getMaxZoom(4F), 0F, 0F);
        } else {
            move(-getMaxZoom(4F), 0F, 0F);
        }
    }

}
