package me.lofro.camerautils;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import me.lofro.camerautils.utils.KeyBinding;
import me.lofro.camerautils.utils.ZoomTrack;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
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
    public static double zoomSensibility = 0.01;

    public static double from = 1;
    public static int time = 1;

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
                    CameraUtils.ZOOM_TRACK = new ZoomTrack((float) CameraUtils.from, (float) CameraUtils.zoom, CameraUtils.time);
                }
            } else {
                CameraUtils.ZOOM_TRACK = null;
            }
        }
    }

    @Mod.EventBusSubscriber(modid = "camerautils", value = Dist.CLIENT)
    public static class ModCommands {
        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

            dispatcher.register(
                    Commands.literal("setZoomSensibility")
                            .then(Commands.argument("zoomSensibility", DoubleArgumentType.doubleArg())
                                    .executes(ModCommands::executeSetZoomSensibility))
            );

            dispatcher.register(
                    Commands.literal("setTime")
                            .then(Commands.argument("time", IntegerArgumentType.integer())
                                    .executes(ModCommands::executeSetTime))
            );

            dispatcher.register(
                    Commands.literal("setFrom")
                            .then(Commands.argument("from", DoubleArgumentType.doubleArg())
                                    .executes(ModCommands::executeSetFrom))
            );
        }

        private static int executeSetZoomSensibility(CommandContext<CommandSourceStack> context) {
            double zoomSensibility = DoubleArgumentType.getDouble(context, "zoomSensibility");
            CameraUtils.zoomSensibility = zoomSensibility;
            context.getSource().sendSuccess(() -> Component.literal("Has cambiado la sensibilidad de zoom a " + zoomSensibility), false);
            return Command.SINGLE_SUCCESS;
        }

        private static int executeSetTime(CommandContext<CommandSourceStack> context) {
            int time = IntegerArgumentType.getInteger(context, "time");
            CameraUtils.time = time;
            context.getSource().sendSuccess(() -> Component.literal("Has cambiado el tiempo de zoom a " + time), false);
            return Command.SINGLE_SUCCESS;
        }

        private static int executeSetFrom(CommandContext<CommandSourceStack> context) {
            double from = DoubleArgumentType.getDouble(context, "from");
            CameraUtils.from = from;
            context.getSource().sendSuccess(() -> Component.literal("Has cambiado el porcentaje de donde viene el zoom a " + from), false);
            return Command.SINGLE_SUCCESS;
        }
    }

    public static boolean onScroll(double amount) {
        if (KeyBinding.ZOOM_KEY.isDown()) {
            double zoom = CameraUtils.zoom;
            double zoomSensitivity = CameraUtils.zoomSensibility;
            CameraUtils.zoom = Math.max(0D, Math.min(2D, zoom + (-amount * zoomSensitivity)));
            return true;
        }
        return false;
    }

}
