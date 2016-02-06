package tbsc.tbscfps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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

    @SideOnly(Side.CLIENT) // Don't want to render on server
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

        int fpsCount = Minecraft.getDebugFPS();
        if (TbscFPS.pos == CounterPosition.TOP_LEFT) {
            this.drawString(mc.fontRendererObj, String.valueOf(fpsCount), xPosLeft, yPosTop, 0xff0000);
            return;
        }
        if (TbscFPS.pos == CounterPosition.TOP_MIDDLE) {
            this.drawString(mc.fontRendererObj, String.valueOf(fpsCount), xPosCenter, yPosTop, 0xff0000);
            return;
        }
        if (TbscFPS.pos == CounterPosition.TOP_RIGHT) {
            this.drawString(mc.fontRendererObj, String.valueOf(fpsCount), xPosRight, yPosTop, 0xff0000);
            return;
        }
        if (TbscFPS.pos == CounterPosition.CENTER_RIGHT) {
            this.drawString(mc.fontRendererObj, String.valueOf(fpsCount), xPosRight, yPosCenter, 0xff0000);
            return;
        }
        if (TbscFPS.pos == CounterPosition.BOTTOM_RIGHT) {
            this.drawString(mc.fontRendererObj, String.valueOf(fpsCount), xPosRight, yPosDown, 0xff0000);
            return;
        }
        if (TbscFPS.pos == CounterPosition.BOTTOM_MIDDLE) {
            this.drawString(mc.fontRendererObj, String.valueOf(fpsCount), xPosCenter, yPosDown, 0xff0000);
            return;
        }
        if (TbscFPS.pos == CounterPosition.BOTTOM_LEFT) {
            this.drawString(mc.fontRendererObj, String.valueOf(fpsCount), xPosLeft, yPosDown, 0xff0000);
            return;
        }
        if (TbscFPS.pos == CounterPosition.CENTER_LEFT) {
            this.drawString(mc.fontRendererObj, String.valueOf(fpsCount), xPosLeft, yPosCenter, 0xff0000);
            return;
        }
        this.drawString(mc.fontRendererObj, String.valueOf("?"), xPosLeft, yPosTop, 0xff0000);
    }

}
