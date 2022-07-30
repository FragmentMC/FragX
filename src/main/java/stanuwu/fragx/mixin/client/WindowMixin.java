package stanuwu.fragx.mixin.client;

import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stanuwu.fragx.client.event.Events;
import stanuwu.fragx.client.event.events.ResizeWindowEvent;

@Mixin(Window.class)
public class WindowMixin {
    @Inject(at = @At("TAIL"), method = "onWindowSizeChanged")
    private void resizeWindowEvent(CallbackInfo ci) {
        Events.RESIZE_WINDOW.fire(new ResizeWindowEvent((Window) (Object) this));
    }
}
