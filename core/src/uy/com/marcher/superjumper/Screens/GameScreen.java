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

package uy.com.marcher.superjumper.Screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import uy.com.marcher.superjumper.Game.Assets;
import uy.com.marcher.superjumper.Game.Objects.Jumper;
import uy.com.marcher.superjumper.Game.World;
import uy.com.marcher.superjumper.Game.WorldRenderer;
import uy.com.marcher.superjumper.Game.World.WorldListener;
import uy.com.marcher.superjumper.Util.Constants;
import uy.com.marcher.superjumper.Util.Settings;
import uy.com.marcher.superjumper.SuperJumper;

public class GameScreen extends ScreenAdapter {
    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;

    SuperJumper game;

    int state;
    OrthographicCamera guiCam;
    Vector3 touchPoint;
    World world;
    WorldListener worldListener;
    WorldRenderer renderer;
    Rectangle pauseBounds;
    Rectangle resumeBounds;
    Rectangle volumeControlBounds;
    int lastScore;
    String scoreString;
    String altitudeString;

    GlyphLayout glyphLayout = new GlyphLayout();

    long engineSound = -1;

    public GameScreen(SuperJumper game) {
        this.game = game;
        this.sr = new ShapeRenderer();

        state = GAME_READY;
        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        touchPoint = new Vector3();
        worldListener = new WorldListener() {
            @Override
            public void jump() {
                Assets.playSound(Assets.jumpSound);
            }

            @Override
            public void highJump() {
                Assets.playSound(Assets.highJumpSound);
            }

            @Override
            public void hit() {
                Assets.playSound(Assets.hitSound);
            }

            @Override
            public void coin() {
                Assets.playSound(Assets.coinSound);
            }
        };
        world = new World(worldListener);
        renderer = new WorldRenderer(game.batcher, world);
        pauseBounds = new Rectangle(320 - 32, 480 - 32, 32, 32);
        volumeControlBounds = new Rectangle(320 - 70, 480 - 32,32,32);
        resumeBounds = new Rectangle(160 - 48/2, 240-48/2, 48, 48);
        lastScore = 0;
        scoreString = "SCORE: 0";
        altitudeString = "0 m";

    }

    public void update(float deltaTime) {
        if (deltaTime > 0.1f) deltaTime = 0.1f;

        if (Gdx.input.justTouched()) {
            //guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (volumeControlBounds.contains(touchPoint.x, touchPoint.y)) {
                Assets.playSound(Assets.clickSound);
                Settings.soundEnabled = !Settings.soundEnabled;
                if (Settings.soundEnabled)
                    Assets.music.play();
                else
                    Assets.music.pause();
            }
        }
        switch (state) {
            case GAME_READY:
                updateReady();
                break;
            case GAME_RUNNING:
                updateRunning(deltaTime);
                break;
            case GAME_PAUSED:
                updatePaused();
                break;
            case GAME_LEVEL_END:
                updateLevelEnd();
                break;
            case GAME_OVER:
                updateGameOver();
                break;
        }
    }

    private void updateReady() {
        if (Gdx.input.justTouched()) {
            state = GAME_RUNNING;
            if (Settings.soundEnabled){
                //Assets.menuMusic.stop();
                Assets.music.play();

            }
        }
    }

    private void updateRunning(float deltaTime) {
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (pauseBounds.contains(touchPoint.x, touchPoint.y)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_PAUSED;
                return;
            }
        }

        ApplicationType appType = Gdx.app.getType();

        if (Gdx.input.justTouched()) {
            world.jumper.makeJump();
            if(engineSound==-1) {
                engineSound = Assets.engineSound.loop();
                Assets.engineSound.setVolume(engineSound, Constants.EFFECTS_VOLUME);
            }else {
                Assets.engineSound.pause();
               Assets.engineSound.resume();
                Assets.engineSound.setVolume(engineSound, Constants.EFFECTS_VOLUME);
            }
        }
        if(world.jumper.state == Jumper.JUMPER_STATE_FALL){
            if(engineSound != -1){
                Assets.engineSound.pause();
            }
        }

        // should work also with Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)
        if (appType == ApplicationType.Android || appType == ApplicationType.iOS) {
            world.update(deltaTime, Gdx.input.getAccelerometerX());
        } else {
            float accel = 0;
            if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) accel = 5f;
            if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) accel = -5f;
            world.update(deltaTime, accel);
        }
        altitudeString = Math.round(world.heightSoFar) + " m";
        if (world.score != lastScore) {
            lastScore = world.score;
            scoreString = "SCORE: " + lastScore;
        }
        if (world.state == World.WORLD_STATE_NEXT_LEVEL) {
            game.setScreen(new WinScreen(game));
        }
        if (world.state == World.WORLD_STATE_GAME_OVER) {
            state = GAME_OVER;
            if (lastScore >= Settings.highscores[4])
                scoreString = "NEW HIGHSCORE: " + lastScore;
            else
                scoreString = "SCORE: " + lastScore;
            Settings.addScore(lastScore);
            Settings.save();
        }
    }

    private void updatePaused() {
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            Assets.engineSound.stop();
            if (resumeBounds.contains(touchPoint.x, touchPoint.y)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_RUNNING;
                return;
            }

        }
    }

    private void updateLevelEnd() {
        if (Gdx.input.justTouched()) {
            world = new World(worldListener);
            renderer = new WorldRenderer(game.batcher, world);
            world.score = lastScore;
            state = GAME_READY;
        }
    }

    private void updateGameOver() {
        if (Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game));
        }
    }


    ShapeRenderer sr;
    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        guiCam.update();
        game.batcher.setProjectionMatrix(guiCam.combined);
        game.batcher.enableBlending();
        game.batcher.begin();
        game.batcher.draw(Settings.soundEnabled ? Assets.instance.GUI.soundOn : Assets.instance.GUI.soundOff,
                320 - 70, 480 - 32,32,32);

        switch (state) {
            case GAME_READY:
                presentReady();
                break;
            case GAME_RUNNING:
                presentRunning();
                break;
            case GAME_PAUSED:
                presentPaused();
                break;
            case GAME_LEVEL_END:
                presentLevelEnd();
                break;
            case GAME_OVER:
                presentGameOver();
                break;
        }
        game.batcher.end();
/*
        sr.setProjectionMatrix(guiCam.combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        sr.rect(volumeControlBounds.x, volumeControlBounds.y,
               volumeControlBounds.getWidth(), volumeControlBounds.height);
        sr.end();*/
    }

    private void presentReady() {
        game.batcher.draw(Assets.ready, 160 - 192 / 2, 240 - 32 / 2, 192, 32);
    }

    private void presentRunning() {
        game.batcher.draw(Assets.instance.GUI.pauseButton, 320 -32, 480 - 32, 32, 32);
        Assets.font.draw(game.batcher, scoreString, 16, 480 - 20);
        Assets.font.draw(game.batcher, altitudeString,16,480-42);
        game.batcher.setColor(Color.RED);
        Assets.font.draw(game.batcher, Gdx.graphics.getFramesPerSecond()+"", 20,20);
        game.batcher.setColor(1,1,1,1);
    }

    private void presentPaused() {
        game.batcher.draw(Assets.instance.GUI.resumeButton, 160 - 48 / 2, 240 - 48 / 2, 48, 48);
        Assets.font.draw(game.batcher, scoreString, 16, 480 - 20);
    }

    private void presentLevelEnd() {
        glyphLayout.setText(Assets.font, "the princess is ...");
        Assets.font.draw(game.batcher, glyphLayout, 160 - glyphLayout.width / 2, 480 - 40);
        glyphLayout.setText(Assets.font, "in another castle!");
        Assets.font.draw(game.batcher, glyphLayout, 160 - glyphLayout.width / 2, 40);
    }

    private void presentGameOver() {
        game.batcher.draw(Assets.gameOver, 160 - 160 / 2, 240 - 96 / 2, 160, 96);
        glyphLayout.setText(Assets.font, scoreString);
        Assets.font.draw(game.batcher, scoreString, 160 - glyphLayout.width / 2, 480 - 20);
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    @Override
    public void pause() {
        if (state == GAME_RUNNING) state = GAME_PAUSED;
    }
}