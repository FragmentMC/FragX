package stanuwu.fragx.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import stanuwu.fragx.client.render.RenderSystem;
import stanuwu.fragx.client.render.font.TTFFontManager;

@Environment(EnvType.CLIENT)
public class FragXClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //load renderer
        RenderSystem.init();

        //adding fonts
        TTFFontManager.addFont("Roboto", 16);
        TTFFontManager.addFont("Righteous", 64);
    }
}
