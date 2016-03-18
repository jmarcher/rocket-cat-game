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
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import uy.com.marcher.superjumper.Util.Animation;
import uy.com.marcher.superjumper.Util.Constants;
import uy.com.marcher.superjumper.Util.Settings;

import java.util.ArrayList;

public class Assets implements Disposable, AssetErrorListener{
    private static final String TAG = Assets.class.getName();

    public static final Assets instance = new Assets();



    private AssetManager assetManager;

    public static Texture background;
    public static TextureRegion backgroundRegion;

    public static BitmapFont font;
    public static Music music;

    public static Sound highJumpSound;
    public static Sound hitSound;
    public static Sound coinSound;
    public static Sound clickSound;
    public static Sound engineSound;
    public static Sound tunaCanSound;
    public static ArrayList<Long> soundIds = new ArrayList<Long>();

    public static Skin windowSkin;


    public AssetJumper jumper;
    public AssetsGUI GUI;
    public AssetSister sister;
    public AssetsHelpers helpers;
    public AssetsDecorations decorations;
    public AssetsEnemies enemies;



    public static FreeTypeFontGenerator fontGenerator;
    public static FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    public ShaderProgram blurShader;
    public FrameBuffer fboA;
    public FrameBuffer fboB;

    private Assets(){}

    public void init(AssetManager assetManager){
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS_OBJETS, TextureAtlas.class);

        loadNonTextureAssets();
        assetManager.finishLoading();
        Gdx.app.debug(TAG, "Assets loaded: #"+assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: "+a);

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJETS);
        for(Texture texture : atlas.getTextures()){
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        //create the resources
        jumper = new AssetJumper(atlas);
        GUI = new AssetsGUI(atlas);
        sister = new AssetSister(atlas);
        helpers = new AssetsHelpers(atlas);
        decorations = new AssetsDecorations(atlas);
        enemies = new AssetsEnemies(atlas);
    }

    /*private void loadShaders() {
        blurShader = new ShaderProgram(Gdx.files.internal("data/shaders/blur.vert"),
                Gdx.files.internal("data/shaders/blur.frag"));

        fboA = new FrameBuffer(Pixmap.Format.RGBA8888, Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, false);
        fboB = new FrameBuffer(Pixmap.Format.RGBA8888, Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, false);

        if (!blurShader.isCompiled()) {
            Gdx.app.error("Shader", blurShader.getLog());
            Gdx.app.exit();
        }

        blurShader.begin();
        blurShader.setUniformf("resolution", Constants.VIRTUAL_WIDTH);
        blurShader.end();
    }*/

    public static Texture loadTexture(String file) {
        Texture texture = new Texture(Gdx.files.internal(file));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return texture;
    }

    public void loadNonTextureAssets() {
        loadFont();
        background = loadTexture("data/bg_parallax_stars_640x1600.png");
        backgroundRegion = new TextureRegion(background, 0, 0, 640, 972);
        windowSkin = new Skin(
                Gdx.files.internal("data/skins/uiskin.json"),
                new TextureAtlas("data/skins/uiskin.atlas")
        );
        loadSound();
        //loadShaders();
    }

    private static void loadSound() {
        music = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/spacial.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);

        highJumpSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/jump.mp3"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/hit.wav"));
        coinSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/pickup.mp3"));

        clickSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/click.wav"));
        engineSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/rocketEngine.mp3"));
        tunaCanSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/tunaCan.wav"));
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
        if (Settings.soundEnabled) {
            long soundId = sound.play(Constants.EFFECTS_VOLUME);
            soundIds.add(soundId);
        }
    }

    public static void stopAllSound(){
        music.stop();
        highJumpSound.stop();
        hitSound.stop();
        engineSound.stop();
        coinSound.stop();
        clickSound.stop();
        tunaCanSound.stop();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG,"Couldn't loadNonTextureAssets asset '" + asset.fileName + "'", throwable);
    }


    @Override
    public void dispose() {
        assetManager.dispose();
//        blurShader.dispose();
        fboA.dispose();
        fboB.dispose();
    }


    /**
     * Inner classes
     * Clean code suck my dick!
     */

    public class AssetJumper{
        public final TextureAtlas.AtlasRegion jumperRegion;
        public final TextureAtlas.AtlasRegion jumperDeadRegion;

        public AssetJumper(TextureAtlas atlas) {
            this.jumperRegion = atlas.findRegion("jumper");
            this.jumperDeadRegion = atlas.findRegion("jumper_dead");
        }
    }

    public class AssetSister{
        public final TextureAtlas.AtlasRegion sisterRegion;

        public AssetSister(TextureAtlas atlas) {
            this.sisterRegion = atlas.findRegion("sister");
        }
    }

    public class AssetsGUI{
        public final TextureAtlas.AtlasRegion pauseButton;
        public final TextureAtlas.AtlasRegion resumeButton;
        public final TextureAtlas.AtlasRegion soundOn;
        public final TextureAtlas.AtlasRegion soundOff;
        public final TextureAtlas.AtlasRegion facebookLogin;

        public AssetsGUI(TextureAtlas atlas){
            this.pauseButton = atlas.findRegion("pause");
            this.resumeButton = atlas.findRegion("play");
            this.soundOff = atlas.findRegion("soundOff");
            this.soundOn = atlas.findRegion("soundOn");
            this.facebookLogin = atlas.findRegion("facebook");
        }
    }

    public class AssetsDecorations{
        public final TextureAtlas.AtlasRegion cloud;

        public AssetsDecorations(TextureAtlas atlas){
            cloud = atlas.findRegion("cloud");
        }
    }

    public class AssetsEnemies{
        public final TextureAtlas.AtlasRegion deathBaloon;

        public AssetsEnemies(TextureAtlas atlas){
            deathBaloon = atlas.findRegion("deathBaloon");
        }
    }

    public class AssetsHelpers{
        public final TextureAtlas.AtlasRegion tunaCan;
        public final Animation springAnimation;
        public final TextureAtlas.AtlasRegion spring;
        public final TextureAtlas.AtlasRegion platform;
        public final Animation platformAnimation;
        public final Animation starAnimation;

        public AssetsHelpers(TextureAtlas atlas){

            tunaCan = atlas.findRegion("tunaCan");
            springAnimation = new Animation(0.3f, atlas.findRegion("spring_open"),
                    atlas.findRegion("spring_middle"),
                    atlas.findRegion("spring_contracted"),
                    atlas.findRegion("spring_middle"),
                    atlas.findRegion("spring_open"));

            spring = atlas.findRegion("spring_open");


            platform = atlas.findRegion("platform");
            platformAnimation = new Animation(0.3f, atlas.findRegions("platform"));
            starAnimation = new Animation(0.2f, atlas.findRegions("star"));

        }
    }
}
