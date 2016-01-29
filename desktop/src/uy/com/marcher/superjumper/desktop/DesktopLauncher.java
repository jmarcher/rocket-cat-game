package uy.com.marcher.superjumper.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import uy.com.marcher.superjumper.SuperJumper;

public class DesktopLauncher {

    private static  boolean rebuildAtlas =true;
    private static boolean drawDebugOutline = false;

    public static void main(String[] arg) {
        if(rebuildAtlas){
            Settings settings = new Settings();
            settings.maxWidth = 1024;
            settings.maxHeight = 1024;
            settings.duplicatePadding=false;
            settings.debug = drawDebugOutline;
            TexturePacker.process(settings, "../../desktop/raw-data/images","data/sprites", "textures");
        }
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Super Jumper";
        config.width = 480;
        config.height = 800;
        new LwjglApplication(new SuperJumper(), config);
    }
}
