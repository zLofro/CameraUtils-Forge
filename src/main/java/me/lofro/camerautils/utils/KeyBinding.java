package me.lofro.camerautils.utils;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_CAMERA = "key.category.camerautils.camera";
    public static final String KEY_ZOOM = "key.camerautils.zoom";

    public static final KeyMapping ZOOM_KEY = new KeyMapping(
            KEY_ZOOM,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            KEY_CATEGORY_CAMERA
    );
}
