package me.lofro.camerautils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import me.lofro.camerautils.data.types.ConfigData;
import me.lofro.camerautils.utils.KeyBinding;
import me.lofro.camerautils.utils.ZoomTrack;
import me.lofro.camerautils.utils.data.JsonConfig;
import me.lofro.camerautils.utils.data.Restorable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.time.LocalDate;

@Mod(CameraUtils.MOD_ID)
public class CameraUtils implements Restorable {
    public static final String MOD_ID = "camerautils";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static ZoomTrack ZOOM_TRACK;

    public static ConfigData configData;

    private JsonConfig config;

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public CameraUtils() {
        MinecraftForge.EVENT_BUS.register(this);

        try {
            this.config = JsonConfig.modConfig("config.json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.restore(config);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.save(config);
        }));
    }

    @Override
    public void restore(JsonConfig jsonConfig) {
        if (jsonConfig.jsonObject.entrySet().isEmpty()) {
            CameraUtils.configData = new ConfigData();
        } else {
            CameraUtils.configData = gson.fromJson(jsonConfig.jsonObject, ConfigData.class);
        }
    }

    @Override
    public void save(JsonConfig jsonConfig) {
        jsonConfig.jsonObject(gson.toJsonTree(configData).getAsJsonObject());
        try {
            jsonConfig.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
                    CameraUtils.ZOOM_TRACK = new ZoomTrack((float) CameraUtils.configData.from, (float) CameraUtils.configData.zoom, CameraUtils.configData.time);
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
            CameraUtils.configData.zoomSensibility = zoomSensibility;
            context.getSource().sendSuccess(() -> Component.literal("Has cambiado la sensibilidad de zoom a " + zoomSensibility), false);
            return Command.SINGLE_SUCCESS;
        }

        private static int executeSetTime(CommandContext<CommandSourceStack> context) {
            int time = IntegerArgumentType.getInteger(context, "time");
            CameraUtils.configData.time = time;
            context.getSource().sendSuccess(() -> Component.literal("Has cambiado el tiempo de zoom a " + time), false);
            return Command.SINGLE_SUCCESS;
        }

        private static int executeSetFrom(CommandContext<CommandSourceStack> context) {
            double from = DoubleArgumentType.getDouble(context, "from");
            CameraUtils.configData.from = from;
            context.getSource().sendSuccess(() -> Component.literal("Has cambiado el porcentaje de donde viene el zoom a " + from), false);
            return Command.SINGLE_SUCCESS;
        }
    }

    public static boolean onScroll(double amount) {
        if (KeyBinding.ZOOM_KEY.isDown()) {
            double zoom = CameraUtils.configData.zoom;
            double zoomSensitivity = CameraUtils.configData.zoomSensibility;
            CameraUtils.configData.zoom = Math.max(0D, Math.min(2D, zoom + (-amount * zoomSensitivity)));
            return true;
        }
        return false;
    }

}
