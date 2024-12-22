package me.lofro.camerautils.mixins.client;

import me.lofro.camerautils.utils.KeyBinding;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Redirect(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 0))
    private Object sensitivity(OptionInstance<Double> instance) {
        if (!KeyBinding.ZOOM_KEY.isDown()) {
            return instance.get();
        }
        return instance.get() * Math.min(1D, 0.1);
    }

}
