package tbsc.tbscfps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import tbsc.tbscfps.util.CounterPosition;
import tbsc.tbscfps.util.PositionUtil;

@Mod(modid = TbscFPS.MODID, version = TbscFPS.VERSION, guiFactory = TbscFPS.GUI_FACTORY)
public class TbscFPS {

    public static final String MODID = "TbscFPS";
    public static final String VERSION = "2.1";
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
        MinecraftForge.EVENT_BUS.register(new FPSRenderEventHandler());
    }

    public static void syncConfig() {
        try {
            // Load config
            config.load();

            // Read props from config
            Property counterPos = config.get(Configuration.CATEGORY_GENERAL, "FPS Counter Position", "topLeft", "Values accepted: " +
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
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
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
