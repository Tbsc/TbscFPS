package tbsc.tbscfps.gui.config;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import tbsc.tbscfps.TbscFPS;

public class GuiTFConfig extends GuiConfig {

    public GuiTFConfig(GuiScreen parent) {
        super(parent, new ConfigElement(TbscFPS.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                TbscFPS.MODID, TbscFPS.MODID, false, false, GuiConfig.getAbridgedConfigPath(TbscFPS.config.toString()));
    }

}
