package stanuwu.fragx.mixin.client;

import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stanuwu.fragx.client.test.MainMenuRenderTest;

@Mixin(TitleScreen.class)
public class MainMenuRenderMixin {

    @Inject(method = "render", at = @At("TAIL"))
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MainMenuRenderTest.render(matrices, mouseX, mouseY, delta);
    }
}