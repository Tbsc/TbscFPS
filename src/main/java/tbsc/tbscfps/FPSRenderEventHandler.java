package tbsc.tbscfps;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;
import tbsc.tbscfps.util.CounterPosition;

import java.lang.reflect.Field;

public class FPSRenderEventHandler extends Gui {

    private Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.modID.equals(TbscFPS.MODID)) {
            TbscFPS.syncConfig();
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (event.isCancelable() || event.type != RenderGameOverlayEvent.ElementType.TEXT || !TbscFPS.shouldRender) {
            return;
        }

        // All values needed for render position
        int width = event.resolution.getScaledWidth();
        int height = event.resolution.getScaledHeight();

        int yPosTop = 4;
        int xPosLeft = 6;
        int yPosDown = height - 4;
        int xPosRight = width - 6;
        int xPosCenter = width / 2 - 4;
        int yPosCenter = height / 2 - 2;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);

        Field field = null;

        try {
            field = ReflectionHelper.findField(mc.getClass(), "debugFPS");
        } catch (Exception e) {
            field = ReflectionHelper.findField(mc.getClass(), "field_71470_ab");
        }
        try {
            field.setAccessible(true);
            int fpsCount = field.getInt(null);
            field.setAccessible(false);
            if (TbscFPS.pos == CounterPosition.TOP_LEFT) {
                this.drawString(mc.fontRenderer, String.valueOf(fpsCount), xPosLeft, yPosTop, TbscFPS.counterColorCode);
                return;
            }
            if (TbscFPS.pos == CounterPosition.TOP_MIDDLE) {
                this.drawString(mc.fontRenderer, String.valueOf(fpsCount), xPosCenter, yPosTop, TbscFPS.counterColorCode);
                return;
            }
            if (TbscFPS.pos == CounterPosition.TOP_RIGHT) {
                this.drawString(mc.fontRenderer, String.valueOf(fpsCount), xPosRight, yPosTop, TbscFPS.counterColorCode);
                return;
            }
            if (TbscFPS.pos == CounterPosition.CENTER_RIGHT) {
                this.drawString(mc.fontRenderer, String.valueOf(fpsCount), xPosRight, yPosCenter, TbscFPS.counterColorCode);
                return;
            }
            if (TbscFPS.pos == CounterPosition.BOTTOM_RIGHT) {
                this.drawString(mc.fontRenderer, String.valueOf(fpsCount), xPosRight, yPosDown, TbscFPS.counterColorCode);
                return;
            }
            if (TbscFPS.pos == CounterPosition.BOTTOM_MIDDLE) {
                this.drawString(mc.fontRenderer, String.valueOf(fpsCount), xPosCenter, yPosDown, TbscFPS.counterColorCode);
                return;
            }
            if (TbscFPS.pos == CounterPosition.BOTTOM_LEFT) {
                this.drawString(mc.fontRenderer, String.valueOf(fpsCount), xPosLeft, yPosDown, TbscFPS.counterColorCode);
                return;
            }
            if (TbscFPS.pos == CounterPosition.CENTER_LEFT) {
                this.drawString(mc.fontRenderer, String.valueOf(fpsCount), xPosLeft, yPosCenter, TbscFPS.counterColorCode);
                return;
            }
        } catch (IllegalAccessException e) {
            this.drawString(mc.fontRenderer, "?", xPosLeft, yPosTop, TbscFPS.counterColorCode);
            return;
        }
        this.drawString(mc.fontRenderer, String.valueOf("?"), xPosLeft, yPosTop, TbscFPS.counterColorCode);
    }

}
