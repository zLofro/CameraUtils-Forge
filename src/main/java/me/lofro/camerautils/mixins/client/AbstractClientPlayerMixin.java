package me.lofro.camerautils.mixins.client;

import me.lofro.camerautils.CameraUtils;
import me.lofro.camerautils.utils.KeyBinding;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {

    @Inject(at = @At("RETURN"), method = "getFieldOfViewModifier", cancellable = true)
    private void getFieldOfViewModifier(CallbackInfoReturnable<Float> cir) {
        if (CameraUtils.ZOOM_TRACK != null) {
            cir.setReturnValue(CameraUtils.ZOOM_TRACK.getCurrentFOV());
        } else {
            cir.setReturnValue(cir.getReturnValue() * (KeyBinding.ZOOM_KEY.isDown() ? (float) 0.1 : 1F));
        }
    }

}
