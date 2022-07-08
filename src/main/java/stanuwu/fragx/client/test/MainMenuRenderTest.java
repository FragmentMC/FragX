package stanuwu.fragx.client.test;

import lombok.experimental.UtilityClass;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import stanuwu.fragx.client.render.Render2d;
import stanuwu.fragx.client.render.font.TTFFontManager;
import stanuwu.fragx.client.render.font.TTFFontRenderer;

import java.awt.*;

@UtilityClass
public class MainMenuRenderTest {
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        //just some testing
        Render2d.rounded_rect(100, 100, 20, 20, 5, new Color(255, 125, 0, 125));
        Render2d.rounded_rect(50, 50, 50, 50, 10, Color.ORANGE);
        Render2d.rect(50, 150, 50, 50, Color.ORANGE, Color.BLACK, Color.RED, Color.MAGENTA);
        Render2d.rounded_rect(125, 150, 50, 50, 15, Color.ORANGE, Color.BLACK, Color.RED, Color.MAGENTA);
        Render2d.rect(200, 25, 200, 200, Color.GREEN);
        Render2d.rounded_rect(200, 25, 200, 200, 30, Color.RED);

        Identifier logo = new Identifier("fragx", "textures/fragment_r.png");
        Render2d.texture_rect_full(200, 25, 200, 200, logo);

        TTFFontRenderer font = TTFFontManager.getFont("Righteous", 64);
        String text = "TEST string";
        font.drawString("UWU", 10, 74, Color.ORANGE, Color.BLACK, Color.RED, Color.MAGENTA);
        font.drawCenteredString("UWU", 50, 120, Color.ORANGE, Color.BLACK, Color.RED, Color.MAGENTA);
        Render2d.rect(45, 115, 10, 10, Color.RED);
        font.drawString(text, 10, 10, Color.RED);
        Render2d.rect(10, 10, font.getWidth(text), font.getHeight(), new Color(255, 0, 0, 125));

        TTFFontRenderer font2 = TTFFontManager.getFont("Roboto", 16);
        String text2 = "TEST STRING 2 abcdefg -.as!?\\";
        font2.drawString(text2, 10, 200, Color.RED);
        Render2d.rect(10, 200, font2.getWidth(text2), font2.getHeight(), new Color(255, 0, 0, 125));
    }
}
