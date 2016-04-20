package tbsc.tbscfps;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.lwjgl.input.Keyboard;
import tbsc.tbscfps.util.CounterPosition;
import tbsc.tbscfps.util.PositionUtil;

import java.awt.*;
import java.util.regex.Pattern;

@Mod(modid = TbscFPS.MODID, version = TbscFPS.VERSION, guiFactory = TbscFPS.GUI_FACTORY)
public class TbscFPS {

    public static final String MODID = "TbscFPS";
    public static final String VERSION = "1.1";
    public static final String GUI_FACTORY = "tbsc.tbscfps.gui.config.TFModGuiFactory";

    public static KeyBinding keyToggle;
    public static Minecraft mc;
    public static Configuration config;

    public static CounterPosition pos = CounterPosition.TOP_LEFT;
    public static int counterColorCode = 0xFF0000;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (event.getSide() == Side.SERVER) {
            FMLLog.bigWarning("CLIENT-SIDE MOD INSTALLED ON SERVER. REMOVE FROM SERVER.");
            FMLLog.bigWarning("You are lucky I am adding a safety-check, or you'll crash.");
            return;
        }
        mc = Minecraft.getMinecraft();
        keyToggle = new KeyBinding("Toggle FPS Counter", Keyboard.KEY_G, "TbscFPS");
        ClientRegistry.registerKeyBinding(keyToggle);
        config = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(new FPSRenderEventHandler());
    }

    private void loadConfig(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(this);

        syncConfig();
    }

    public static void syncConfig() {
        try {
            processConfig();
        } catch (Exception e) {
            FMLLog.warning(MODID + " has a problem loading its configuration!");
            e.printStackTrace();
        } finally {
            if(config.hasChanged()) {
                config.save();
            }
        }
    }

    private static void processConfig() {
        pos = PositionUtil.toPosition(config.get(Configuration.CATEGORY_GENERAL, "fpsCounterPosition", "topLeft",
                "Position of the FPS counter on screen", CounterPosition.positionValues()).getString());
        counterColorCode = Color.decode(config.get(Configuration.CATEGORY_GENERAL, "fpsCounterColor",
                "#FF0000", "Hex color code for the FPS counter", Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
                        Pattern.CASE_INSENSITIVE)).getString()).getRGB();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(MODID)) {
            syncConfig();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event) {
        syncConfig();
    }

    public static boolean shouldRender = true;

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (TbscFPS.keyToggle.isPressed()) {
            shouldRender = !shouldRender;
        }
    }

}
