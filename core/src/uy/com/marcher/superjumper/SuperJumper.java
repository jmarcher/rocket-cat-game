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

package uy.com.marcher.superjumper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import uy.com.marcher.superjumper.Game.Assets;
import uy.com.marcher.superjumper.Screens.GameScreen;
import uy.com.marcher.superjumper.Screens.SplashScreen;
import uy.com.marcher.superjumper.Util.ActionResolver;
import uy.com.marcher.superjumper.Util.Settings;

public class SuperJumper extends Game implements GestureDetector.GestureListener {
    private static long SPLASH_MINIMUM_MILLIS = 950L;
    public ActionResolver actionResolver;
    // used by all screens
    public SpriteBatch batcher;

    public SuperJumper(ActionResolver actionResolver) {
        this.actionResolver = actionResolver;
    }

    @Override
    public void create() {
        /*
        setScreen(new MainMenuScreen(this));*/
        setScreen(new SplashScreen());
        final long splash_start_time = System.currentTimeMillis();
        final SuperJumper instance = this;
        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
        new Thread(new Runnable() {
            @Override
            public void run() {

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        batcher = new SpriteBatch();
                        Settings.load();
                        Assets.instance.init(new AssetManager());
                        Assets.load();

                        // Se muestra el menu principal tras la SpashScreen
                        final long splash_elapsed_time = System.currentTimeMillis() - splash_start_time;
                        if (splash_elapsed_time < SuperJumper.SPLASH_MINIMUM_MILLIS) {
                            Timer.schedule(
                                    new Timer.Task() {
                                        @Override
                                        public void run() {
                                            SuperJumper.this.setScreen(new GameScreen(instance));
                                        }
                                    }, (float)(SuperJumper.SPLASH_MINIMUM_MILLIS - splash_elapsed_time) / 1000f);
                        } else {
                            SuperJumper.this.setScreen(new GameScreen(instance));
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void dispose() {
        getScreen().dispose();
        Assets.instance.dispose();
        Gdx.app.exit();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        actionResolver.showOrLoadInterstital();
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
