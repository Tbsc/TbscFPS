package tbsc.tbscfps;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.lwjgl.input.Keyboard;
import tbsc.tbscfps.util.CounterPosition;
import tbsc.tbscfps.util.PositionUtil;

@Mod(modid = TbscFPS.MODID, version = TbscFPS.VERSION, guiFactory = TbscFPS.GUI_FACTORY)
public class TbscFPS {

    public static final String MODID = "TbscFPS";
    public static final String VERSION = "1.1";
    public static final String GUI_FACTORY = "tbsc.tbscfps.gui.config.TFModGuiFactory";

    public static KeyBinding keyToggle;
    public static Minecraft mc;
    public static Configuration config;

    public static CounterPosition pos = CounterPosition.TOP_LEFT;

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

    public static void syncConfig() {
        try {
            // Load config
            config.load();

            // Read props from config
            Property counterPos = config.get(Configuration.CATEGORY_GENERAL, "Pos - e.g. topLeft", "topLeft", "Values accepted: " +
                    "topLeft, topMiddle, topRight, centerRight, downRight, downMiddle, downLeft, centerLeft.");
            String posString = counterPos.getString();
            pos = PositionUtil.toPosition(posString);
        } catch (Exception e) {
            // Exception
        } finally {
            // Save props to config
            if (config.hasChanged()) config.save();
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
            if (shouldRender == true) shouldRender = false; else shouldRender = true;
        }
    }

}
