package tbsc.fps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.jline.utils.Log;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

@Mod(modid = TbscFPS.MODID, clientSideOnly = true)
public class TbscFPS {
    public static final String MODID = "tbscfps";

    public static KeyBinding keyToggle = new KeyBinding("key.tbscfps.toggle", Keyboard.KEY_NONE, "keycat.tbscfps");
    public static KeyBinding keyRotate = new KeyBinding("key.tbscfps.rotate", Keyboard.KEY_NONE, "keycat.tbscfps");

    public static final String POS_CONF_KEY = "counterPosition";
    public static CounterPosition pos = CounterPosition.TOP_LEFT;
    public static final String COLOR_CONF_KEY = "fpsCounterColor";
    public static int counterColorCode = 0xFF0000;
    public static Configuration config;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ClientRegistry.registerKeyBinding(keyToggle);
        ClientRegistry.registerKeyBinding(keyRotate);

        MinecraftForge.EVENT_BUS.register(this);

        config = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig();
    }

    public static void syncConfig() {
        try {
            processConfig();
        } catch (Exception e) {
            Log.error("TbscFPS has a problem loading its configuration!");
            e.printStackTrace();
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MODID)) {
            processConfig();
        }
    }

    private static void processConfig() {
        pos = CounterPosition.valueOf(config.get(Configuration.CATEGORY_GENERAL, POS_CONF_KEY,
                CounterPosition.TOP_LEFT.name(),
                "Position of the FPS counter on screen " +
                        "(possible values: TOP_LEFT, TOP_MIDDLE, TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT, BOTTOM_MIDDLE, BOTTOM_LEFT, CENTER_LEFT)",
                CounterPosition.names()).getString());
        counterColorCode = Color.decode(config.get(Configuration.CATEGORY_GENERAL, COLOR_CONF_KEY,
                "#FF0000", "Hex color code for the FPS counter", Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
                        Pattern.CASE_INSENSITIVE)).getString()).getRGB();
    }

    public static boolean shouldRender = true;

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (TbscFPS.keyToggle.isPressed()) {
            shouldRender = !shouldRender;
        }

        if (TbscFPS.keyRotate.isPressed()) {
            config.get(Configuration.CATEGORY_GENERAL, POS_CONF_KEY, "")
                    .set(CounterPosition.values()[(pos.ordinal() + 1) % CounterPosition.values().length].name());
            processConfig();
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT || !TbscFPS.shouldRender || Minecraft.getMinecraft().gameSettings.showDebugInfo) {
            return;
        }

        // obfuscation of fps static field in Minecraft
        String fpsCount = String.valueOf(Minecraft.getDebugFPS());
        int color = TbscFPS.counterColorCode;

        int charCount = fpsCount.length();

        int width = event.getResolution().getScaledWidth();
        int height = event.getResolution().getScaledHeight();

        int yPosTop = 4;
        int xPosLeft = 4;
        int yPosDown = height - 11;
        int xPosRight = width - (charCount * 6) - 2;
        int xPosCenter = width / 2 - (charCount * 3);
        int yPosCenter = height / 2 - 4;

        if (fontRenderer == null) {
            fontRenderer = Minecraft.getMinecraft().fontRenderer;
        }

        switch (TbscFPS.pos) {
            case TOP_LEFT:
                this.drawString(fpsCount, xPosLeft, yPosTop, color);
                break;
            case TOP_MIDDLE:
                this.drawString(fpsCount, xPosCenter, yPosTop, color);
                break;
            case TOP_RIGHT:
                this.drawString(fpsCount, xPosRight, yPosTop, color);
                break;
            case CENTER_RIGHT:
                this.drawString(fpsCount, xPosRight, yPosCenter, color);
                break;
            case BOTTOM_RIGHT:
                this.drawString(fpsCount, xPosRight, yPosDown, color);
                break;
            case BOTTOM_MIDDLE:
                this.drawString(fpsCount, xPosCenter, yPosDown, color);
                break;
            case BOTTOM_LEFT:
                this.drawString(fpsCount, xPosLeft, yPosDown, color);
                break;
            case CENTER_LEFT:
                this.drawString(fpsCount, xPosLeft, yPosCenter, color);
                break;
        }
    }

    private FontRenderer fontRenderer;

    private void drawString(String text, int x, int y, int color) {
        if (fontRenderer != null) {
            fontRenderer.drawString(text, x, y, color);
        }
    }

    private Field field;

    public <R> R reflGetField(String name) {
        Class<Minecraft> clazz = Minecraft.class;
        try {
            if (field == null) {
                field = ObfuscationReflectionHelper.findField(clazz, name);
                field.setAccessible(true);
            }
            return (R) field.get(null);
        } catch (IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }
}
