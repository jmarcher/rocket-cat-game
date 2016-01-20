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

package com.badlogicgames.superjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class WorldRenderer {
    static final float FRUSTUM_WIDTH = 10;
    static final float FRUSTUM_HEIGHT = 15;
    private static final float SCALE_RATE = 1.3f;
    World world;
    OrthographicCamera cam;
    SpriteBatch batch;
    ShapeRenderer sr;

    public WorldRenderer(SpriteBatch batch, World world) {
        this.world = world;
        this.cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        this.cam.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
        this.batch = batch;
        this.sr = new ShapeRenderer();
    }

    public void render() {
        if (world.bob.position.y > cam.position.y) cam.position.y = world.bob.position.y;
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        Gdx.gl.glClearColor(Color.SKY.r, Color.SKY.g, Color.SKY.b, 1f);
        renderBackground();
        renderObjects();
    }

    public void renderBackground() {
        //batch.disableBlending();
        batch.begin();
        //batch.setColor(Color.SKY);
        batch.draw(Assets.backgroundRegion, cam.position.x - FRUSTUM_WIDTH / 2, cam.position.y - .5f - FRUSTUM_HEIGHT / 2, FRUSTUM_WIDTH,
                FRUSTUM_HEIGHT / 5);
        batch.end();
    }

    public void renderObjects() {
        batch.enableBlending();
        batch.begin();
        renderPlatforms();
        renderBob();
        renderItems();
        renderSquirrels();
        renderCastle();
        batch.end();

        drawBobBounds(false);
        drawSpringBounds(false);
    }

    private void drawBobBounds(boolean render) {
        if(!render)
            return;
        sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        sr.rect(world.bob.bounds.getX(), world.bob.bounds.getY(), world.bob.bounds.getWidth(), world.bob.bounds.getHeight());
        sr.end();
    }

    private void drawSpringBounds(boolean render) {
        if(!render)
            return;
        int len = world.springs.size();
        for (int i = 0; i < len; i++) {
            sr.setProjectionMatrix(cam.combined);
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.RED);
            sr.rect(world.springs.get(i).position.x, world.springs.get(i).position.y, world.springs.get(i).bounds.getWidth(), world.springs.get(i).bounds.getHeight());
            sr.end();
        }
    }

    private void renderBob() {
        TextureRegion keyFrame;
        world.bob.render(batch);
        switch (world.bob.state) {
            case Bob.BOB_STATE_FALL:
                keyFrame = Assets.bobFall.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
                break;
            case Bob.BOB_STATE_JUMP:
                keyFrame = Assets.bobJump.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);

                break;
            case Bob.BOB_STATE_HIT:
            default:
                keyFrame = Assets.bobHit;
        }

        float side = world.bob.velocity.x < 0 ? 1 : -1;
        if (side < 0)
            batch.draw(keyFrame, world.bob.position.x + 0.5f, world.bob.position.y - 0.5f, (side * 1) * SCALE_RATE, (1.336f) * SCALE_RATE);
        else
            batch.draw(keyFrame, world.bob.position.x - 0.5f, world.bob.position.y - 0.5f, (side * 1) * SCALE_RATE, (1.336f) * SCALE_RATE);


    }

    private void renderPlatforms() {
        int len = world.platforms.size();
        for (int i = 0; i < len; i++) {
            Platform platform = world.platforms.get(i);
            TextureRegion keyFrame = Assets.platform;
            if (platform.state == Platform.PLATFORM_STATE_PULVERIZING) {
                keyFrame = Assets.brakingPlatform.getKeyFrame(platform.stateTime, Animation.ANIMATION_NONLOOPING);
            }

            batch.draw(keyFrame, platform.position.x - 1, platform.position.y - 0.25f, 2 * SCALE_RATE, 0.5f * SCALE_RATE);
        }
    }

    private void renderItems() {
        int len = world.springs.size();
        for (int i = 0; i < len; i++) {
            Spring spring = world.springs.get(i);

            if (!spring.wasUsed) {
                batch.draw(Assets.spring, spring.position.x - 0.1f, spring.position.y-0.1f, 0.8f, 0.8f);
            } else {
                Gdx.app.debug("Spring", "Animation?");
                TextureRegion keyFrame = Assets.springExplotion.getKeyFrame(spring.stateTime, Animation.ANIMATION_NONLOOPING);
                batch.draw(keyFrame, spring.position.x, spring.position.y,
                        (keyFrame.getTexture().getWidth()*0.8f)/Assets.spring.getTexture().getWidth(),
                        (keyFrame.getTexture().getHeight()*0.8f)/Assets.spring.getTexture().getHeight());
            }
        }

        len = world.coins.size();
        for (int i = 0; i < len; i++) {
            Coin coin = world.coins.get(i);
            TextureRegion keyFrame = Assets.coinAnim.getKeyFrame(coin.stateTime, Animation.ANIMATION_LOOPING);
            batch.draw(keyFrame, coin.position.x - 0.5f, coin.position.y - 0.5f, 1, 1);
        }
    }

    private void renderSquirrels() {
        int len = world.squirrels.size();
        for (int i = 0; i < len; i++) {
            Squirrel squirrel = world.squirrels.get(i);
            TextureRegion keyFrame = Assets.squirrelFly.getKeyFrame(squirrel.stateTime, Animation.ANIMATION_LOOPING);
            float side = squirrel.velocity.x < 0 ? -1 : 1;
            if (side < 0)
                batch.draw(keyFrame, squirrel.position.x + 0.5f, squirrel.position.y - 0.5f, side * 1, 1);
            else
                batch.draw(keyFrame, squirrel.position.x - 0.5f, squirrel.position.y - 0.5f, side * 1, 1);
        }
    }

    private void renderCastle() {
        Castle castle = world.castle;
        batch.draw(Assets.castle, castle.position.x - 1, castle.position.y - 1, 2, 2);
    }
}
