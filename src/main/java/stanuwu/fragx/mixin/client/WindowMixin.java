package stanuwu.fragx.mixin.client;

import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stanuwu.fragx.client.event.EventHandler;
import stanuwu.fragx.client.event.EventType;
import stanuwu.fragx.client.event.events.ResizeWindowEvent;

@Mixin(Window.class)
public class WindowMixin {
    @Inject(at = @At("TAIL"), method = "onWindowSizeChanged")
    private void resizeWindowEvent(CallbackInfo ci) {
        EventHandler.getInstance().fire(EventType.RESIZE_WINDOW, new ResizeWindowEvent((Window) (Object) this));
    }
}
