package uy.com.marcher.superjumper.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import uy.com.marcher.superjumper.SuperJumper;
import uy.com.marcher.superjumper.Util.Constants;

public class DesktopLauncher {

    private static  boolean rebuildAtlas =false;
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
        config.width = Constants.VIRTUAL_WIDTH;
        config.height = Constants.VIRTUAL_HEIGHT;
        new LwjglApplication(new SuperJumper(), config);
    }
}
