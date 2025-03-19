package me.lofro.camerautils.mixins.client;

import me.lofro.camerautils.CameraUtils;
import me.lofro.camerautils.utils.KeyBinding;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {

    private final double MAX_ZOOM = 0.01D;
    private final double MIN_ZOOM = 1.5D;

    @Inject(at = @At("RETURN"), method = "getFieldOfViewModifier", cancellable = true)
    private void getFieldOfViewModifier(CallbackInfoReturnable<Float> cir) {
        if (CameraUtils.ZOOM_TRACK != null) {
            cir.setReturnValue(CameraUtils.ZOOM_TRACK.getCurrentFOV());
        } else {
            cir.setReturnValue(cir.getReturnValue() * (KeyBinding.ZOOM_KEY.isDown() ? (float) convert(CameraUtils.configData.zoom) : 1F));
        }
    }

    @Unique
    private double convert(double value) {
        if (value >= 2D) {
            return MIN_ZOOM;
        } else if (value <= 0D) {
            return MAX_ZOOM;
        } else if (value > 1D) {
            double factor = value - 1D;
            return 1D + factor * (MIN_ZOOM - 1D);
        } else if (value < 1D) {
            return MAX_ZOOM + value * (1D - MAX_ZOOM);
        }
        return value;
    }

}
