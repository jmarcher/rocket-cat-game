package uy.com.marcher.superjumper.Util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by gordo on 28/01/16.
 */
public class AudioManager {
    public final Sound engineSound;

    public AudioManager(AssetManager am){
        engineSound = am.get("data/sounds/rocketEngine.mp3", Sound.class);

    }
}
