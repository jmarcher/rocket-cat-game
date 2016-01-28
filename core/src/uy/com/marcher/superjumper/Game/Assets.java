/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package uy.com.marcher.superjumper.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.utils.Disposable;
import uy.com.marcher.superjumper.Util.Animation;
import uy.com.marcher.superjumper.Util.Settings;

public class Assets implements Disposable, AssetErrorListener{
    private static final String TAG = Assets.class.getName();

    public static final Assets instance = new Assets();

    private AssetManager assetManager;

    public static Texture background;
    public static TextureRegion backgroundRegion;

    public static Texture dust;
    public static TextureRegion dustRegion;

    public static Texture items;
    public static TextureRegion mainMenu;
    public static TextureRegion pauseMenu;
    public static TextureRegion ready;
    public static TextureRegion gameOver;
    public static TextureRegion highScoresRegion;
    public static TextureRegion logo;
    public static TextureRegion soundOn;
    public static TextureRegion soundOff;
    public static TextureRegion arrow;
    public static TextureRegion pause;
    public static TextureRegion spring;
    public static TextureRegion castle;
    public static Animation coinAnim;
    public static Animation bobJump;
    public static Animation bobFall;
    public static Animation springExplotion;
    public static TextureRegion bobHit;
    public static Animation squirrelFly;
    public static TextureRegion platform;
    public static Animation brakingPlatform;
    public static BitmapFont font;
    public static Music music;
    public static Sound jumpSound;
    public static Sound highJumpSound;
    public static Sound hitSound;
    public static Sound coinSound;
    public static Sound clickSound;
    private static Texture catItemsKlein;



    public static FreeTypeFontGenerator fontGenerator;
    public static FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;


    public void init(AssetManager assetManager){
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        load();
        assetManager.finishLoading();
        Gdx.app.debug(TAG, "Assets loaded: #"+assetManager.getAssetNames().size);
    }

    public static Texture loadTexture(String file) {
        Texture texture = new Texture(Gdx.files.internal(file));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Linear);
        return texture;
    }

    public static void load() {
        loadFont();

        items = loadTexture("data/items.png");
        catItemsKlein = loadTexture("data/rocket_cat_klein.png");

        background = loadTexture("data/bg_parallax_stars_640x1600.png");
        backgroundRegion = new TextureRegion(background, 0, 0, 640, 972);

        dustRegion = new TextureRegion(catItemsKlein, 475, 524, 187, 100);

        mainMenu = new TextureRegion(items, 0, 224, 300, 110);
        pauseMenu = new TextureRegion(items, 224, 128, 192, 96);
        ready = new TextureRegion(items, 320, 224, 192, 32);
        gameOver = new TextureRegion(items, 352, 256, 160, 96);
        highScoresRegion = new TextureRegion(Assets.items, 0, 257, 300, 110 / 3);
        logo = new TextureRegion(items, 0, 352, 274, 142);
        soundOff = new TextureRegion(items, 0, 0, 64, 64);
        soundOn = new TextureRegion(items, 64, 0, 64, 64);
        arrow = new TextureRegion(items, 0, 64, 64, 64);
        pause = new TextureRegion(items, 64, 64, 64, 64);

        spring = new TextureRegion(catItemsKlein, 1089, 99, 73, 56);
        castle = new TextureRegion(catItemsKlein, 869, 149, 62, 71);
        coinAnim = new Animation(0.1f, new TextureRegion(catItemsKlein, 783, 424, 66, 64), new TextureRegion(catItemsKlein, 865, 424, 65, 66),
                new TextureRegion(catItemsKlein, 947, 424, 66, 64), new TextureRegion(catItemsKlein, 1025, 424, 66, 66));

        bobJump = new Animation(0.2f, new TextureRegion(catItemsKlein, 1266, 933, 134, 179), new TextureRegion(catItemsKlein, 1266, 665, 134, 179));
        bobFall = new Animation(0.2f, new TextureRegion(catItemsKlein, 1266, 933, 134, 179), new TextureRegion(catItemsKlein, 1266, 665, 134, 179));
        platform = new TextureRegion(catItemsKlein, 578, 742, 237, 109);
        brakingPlatform = new Animation(0.2f, new TextureRegion(catItemsKlein, 578, 742, 237, 109),
                new TextureRegion(catItemsKlein, 35, 12, 254, 131),
                new TextureRegion(catItemsKlein, 319, 0, 263, 143),
                new TextureRegion(catItemsKlein, 625, 0, 254, 150));

        bobHit = new TextureRegion(catItemsKlein, 1260, 384, 146, 214);
        squirrelFly = new Animation(0.2f, new TextureRegion(catItemsKlein, 3, 1224, 145, 231));

        springExplotion = new Animation(0.3f, new TextureRegion(catItemsKlein, 1089, 99, 73, 56),
                new TextureRegion(catItemsKlein, 989, 66, 73, 40),
                new TextureRegion(catItemsKlein, 989, 126, 73, 30),
                new TextureRegion(catItemsKlein, 989, 66, 73, 40),
                new TextureRegion(catItemsKlein, 1089, 99, 73, 56));
        //font = new BitmapFont(Gdx.files.internal("data/newfont.fnt"), Gdx.files.internal("data/newfont.png"), false);


        music = Gdx.audio.newMusic(Gdx.files.internal("data/music.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        if (Settings.soundEnabled) music.play();
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("data/jump.wav"));
        highJumpSound = Gdx.audio.newSound(Gdx.files.internal("data/highjump.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("data/hit.wav"));
        coinSound = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("data/click.wav"));
    }

    private static void loadFont() {
        fontGenerator =new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/cubic.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 16;
        fontParameter.minFilter = Texture.TextureFilter.Linear;
        fontParameter.magFilter = Texture.TextureFilter.Linear;
        fontParameter.borderColor  = Color.BLACK;
        fontParameter.borderWidth = 1;
        font = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose(); // don't forget to dispose to avoid memory leaks!
    }

    public static void playSound(Sound sound) {
        if (Settings.soundEnabled) sound.play(1);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG,"Couldn't load asset '" + asset.fileName + "'", throwable);
    }


    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
