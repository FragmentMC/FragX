package stanuwu.fragx.mixin.client;

import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stanuwu.fragx.client.render.font.TTFFontManager;
import stanuwu.fragx.client.render.font.TTFFontRenderer;

import java.awt.*;

@Mixin(TitleScreen.class)
public class MainMenuRenderMixin {

    @Inject(method = "render", at = @At("TAIL"))
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        //just some testing
        TTFFontRenderer font = TTFFontManager.getFont("Righteous", 64);
        TTFFontRenderer font2 = TTFFontManager.getFont("Roboto", 16);
        String test = "FONT SIZE TEST";
        int x = font2.getWidth(test);
        int y = font2.getHeight();
        font2.drawString(matrices, test, 50, 50, 0, Color.ORANGE);
        font2.drawString(matrices, test, 50 + x, 50, 0, Color.ORANGE);
        font2.drawString(matrices, test, 50, 50 + y, 0, Color.ORANGE);
        font2.drawString(matrices, test, 50 + x, 50 + y, 0, Color.ORANGE);

        font.drawString(matrices, "NEW TEST", 25, 100, 0, new Color(255, 0, 125, 200));
        font.drawString(matrices, "LOL", 300, 100, 0, Color.CYAN);
        font.drawCenteredString(matrices, "LOL", 300, 100, 0, Color.CYAN);

        font.drawCenteredStringShadow(matrices, "TEST", 150, 100, 0, new Color(255, 255, 0, 125));
        font2.drawStringShadow(matrices, "TEST 2", 100, 50, 0, Color.WHITE);
    }
}