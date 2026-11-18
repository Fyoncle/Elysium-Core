package net.fyoncle.elysiumcore.mixin.cosmetic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fyoncle.elysiumcore.utility.constants.Constants;
import net.fyoncle.elysiumcore.utility.constants.Textures;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

@Mixin(TitleScreen.class)
public class CustomMainMenuMixin extends Screen {

    @Unique
    private ButtonWidget discordButton;
    @Unique
    private ButtonWidget wikiButton;

    protected CustomMainMenuMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void addButtons(CallbackInfo ci) {
        for (int i = 0; i < this.children().size(); i++) {
            if (!(this.children().get(i) instanceof ButtonWidget button)) continue;

            if (button.getMessage().getString().equals(Text.translatable("menu.quit").getString())) {
                addDiscordButton((ButtonWidget) this.children().get(i + 1));
            }
            if (button.getMessage().getString().equals(Text.translatable("menu.options").getString())) {
                addWikiButton((ButtonWidget) this.children().get(i + 1));
            }
        }
    }

    @Unique
    private void addDiscordButton(ButtonWidget startButton) {
        discordButton = this.addDrawableChild(new TexturedButtonWidget(
                startButton.getX() + startButton.getWidth() + 4,
                startButton.getY(),
                20, 20,
                new ButtonTextures(Textures.DISCORD_BUTTON_UNFOCUSED_TEXTURE, Textures.DISCORD_BUTTON_FOCUSED_TEXTURE),
                button -> Util.getOperatingSystem().open(Constants.Links.DISCORD_LINK)
        ));
    }

    @Unique
    private void addWikiButton(ButtonWidget startButton) {
        wikiButton = this.addDrawableChild(new TexturedButtonWidget(
                startButton.getX() + startButton.getWidth() - 248,
                startButton.getY(),
                20, 20,
                new ButtonTextures(Textures.WIKI_BUTTON_UNFOCUSED_TEXTURE, Textures.WIKI_BUTTON_FOCUSED_TEXTURE),
                button -> Util.getOperatingSystem().open(Constants.Links.WIKI_LINK)
        ));
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderTooltip(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (discordButton != null && discordButton.isHovered()) {
            context.drawTooltip(this.textRenderer, Text.of("Join our Discord!"), mouseX, mouseY);
        }
        if (wikiButton != null && wikiButton.isHovered()) {
            context.drawTooltip(this.textRenderer, Text.of("Visit the Wiki!"), mouseX, mouseY);
        }
    }
}