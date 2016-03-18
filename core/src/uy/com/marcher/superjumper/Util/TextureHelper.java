package uy.com.marcher.superjumper.Util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by gordo on 12/03/16.
 */
public class TextureHelper {

    public static float textureToFrustumHeight(TextureAtlas.AtlasRegion atlasRegion){
        return ((float)atlasRegion.getRegionHeight())/((float)atlasRegion.getRegionWidth());
    }

    public static float textureToFrustumHeight(TextureRegion textureRegion){
        return ((float)textureRegion.getRegionHeight())/((float)textureRegion.getRegionWidth());

    }
}
