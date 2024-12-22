package me.lofro.camerautils;

import com.mojang.logging.LogUtils;
import me.lofro.camerautils.utils.KeyBinding;
import me.lofro.camerautils.utils.ZoomTrack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CameraUtils.MOD_ID)
public class CameraUtils {
    public static final String MOD_ID = "camerautils";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static ZoomTrack ZOOM_TRACK;

    public static double zoom = 0.1;

    public CameraUtils() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.ZOOM_KEY);
        }

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (KeyBinding.ZOOM_KEY.isDown()) {
                if (CameraUtils.ZOOM_TRACK == null) {
                    CameraUtils.ZOOM_TRACK = new ZoomTrack(1, (float) CameraUtils.zoom, 5);
                }
            } else {
                CameraUtils.ZOOM_TRACK = null;
            }
        }
    }

    public static boolean onScroll(double amount) {
        if (KeyBinding.ZOOM_KEY.isDown()) {
            double zoom = CameraUtils.zoom;
            double zoomSensitivity = 0.01;
            CameraUtils.zoom = Math.max(0D, Math.min(2D, zoom + (-amount * zoomSensitivity)));
            return true;
        }
        return false;
    }

}
