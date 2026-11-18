package net.fyoncle.elysiumcore.utility.constants;

import net.fyoncle.elysiumcore.ElysiumCore;
import net.minecraft.util.Identifier;

public class Textures {
    // App Icon
    public static final String APP_ICON = "assets/elysiumcore/textures/icon/ed_icon.png";
    public static final String APP_ICON_MAC = "assets/elysiumcore/textures/icon/ed_icon.icns";

    // Menu Logo
    public static final Identifier MENU_LOGO = Identifier.of(ElysiumCore.MOD_ID,
            "textures/logo/menu_logo.png");

    // Discord Button Textures
    public static final Identifier DISCORD_BUTTON_UNFOCUSED_TEXTURE = Identifier.of(ElysiumCore.MOD_ID,
            "discord_button_unfocused");
    public static final Identifier DISCORD_BUTTON_FOCUSED_TEXTURE = Identifier.of(ElysiumCore.MOD_ID,
            "discord_button_focused");

    // Wiki Button Textures
    public static final Identifier WIKI_BUTTON_UNFOCUSED_TEXTURE = Identifier.of(ElysiumCore.MOD_ID,
            "wiki_button_unfocused");
    public static final Identifier WIKI_BUTTON_FOCUSED_TEXTURE = Identifier.of(ElysiumCore.MOD_ID,
            "wiki_button_focused");

    // Ram Warning Screen Textures
    public static final Identifier OPEN_GUIDE_BUTTON_UNFOCUSED = Identifier.of(ElysiumCore.MOD_ID,
            "open_guide_button_unfocused");
    public static final Identifier OPEN_GUIDE_BUTTON_FOCUSED = Identifier.of(ElysiumCore.MOD_ID,
            "open_guide_button_focused");
    public static final Identifier IGNORE_BUTTON_UNFOCUSED = Identifier.of(ElysiumCore.MOD_ID,
            "ignore_button_unfocused");
    public static final Identifier IGNORE_BUTTON_FOCUSED = Identifier.of(ElysiumCore.MOD_ID,
            "ignore_button_focused");
    public static final Identifier DONT_WARN_AGAIN_BUTTON_UNFOCUSED = Identifier.of(ElysiumCore.MOD_ID,
            "dont_warn_again_button_unfocused");
    public static final Identifier DONT_WARN_AGAIN_BUTTON_FOCUSED = Identifier.of(ElysiumCore.MOD_ID,
            "dont_warn_again_button_focused");

    // Default Button Textures
    public static final Identifier DEFAULT_BUTTON_UNFOCUSED = Identifier.of(ElysiumCore.MOD_ID,
            "default_button_unfocused");
    public static final Identifier DEFAULT_BUTTON_FOCUSED = Identifier.of(ElysiumCore.MOD_ID,
            "default_button_focused");
}