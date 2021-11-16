package tbsc.fps;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.lang.reflect.Field;

@Mod("tbscfps")
public class TbscFPS {
    public static KeyBinding keyToggle = new KeyBinding("key.tbscfps.toggle", GLFW.GLFW_KEY_UNKNOWN, "keycat.tbscfps");
    public static KeyBinding keyRotate = new KeyBinding("key.tbscfps.rotate", GLFW.GLFW_KEY_UNKNOWN, "keycat.tbscfps");

    public static CounterPosition pos = CounterPosition.TOP_LEFT;
    public static int counterColorCode = 0xFF0000;

    public TbscFPS() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientPreInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onConfigReloaded);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void clientPreInit(final FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(keyToggle);
        ClientRegistry.registerKeyBinding(keyRotate);

        Config.loadConfig(Config.CONFIG_SPEC, FMLPaths.CONFIGDIR.get().resolve("tbscfps.toml"));
        processConfig();
    }

    public void onConfigReloaded(ModConfig.ModConfigEvent event) {
        if (event instanceof ModConfig.Reloading) {
            processConfig();
        }
    }

    private static void processConfig() {
        pos = Config.POSITION.get();
        counterColorCode = Color.decode(Config.COUNTER_COLOR.get()).getRGB();
    }

    public static boolean shouldRender = true;

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (TbscFPS.keyToggle.isDown()) {
            shouldRender = !shouldRender;
        }

        if (TbscFPS.keyRotate.isDown()) {
            Config.POSITION.set(CounterPosition.values()[(pos.ordinal() + 1) % CounterPosition.values().length]);
            processConfig();
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT || !TbscFPS.shouldRender || Minecraft.getInstance().options.renderDebug) {
            return;
        }

        // obfuscation of fps static field in Minecraft
        String fpsCount = String.valueOf(this.<Integer>reflGetField("field_71470_ab"));
        int color = TbscFPS.counterColorCode;

        int charCount = fpsCount.length();

        int width = event.getWindow().getGuiScaledWidth();
        int height = event.getWindow().getGuiScaledHeight();

        int yPosTop = 4;
        int xPosLeft = 4;
        int yPosDown = height - 11;
        int xPosRight = width - (charCount * 6) - 2;
        int xPosCenter = width / 2 - (charCount * 3);
        int yPosCenter = height / 2 - 4;

        if (fontRenderer == null) {
            fontRenderer = Minecraft.getInstance().font;
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
    private final MatrixStack stack = new MatrixStack();

    private void drawString(String text, float x, float y, int color) {
        if (fontRenderer != null) {
            fontRenderer.draw(stack, text, x, y, color);
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
