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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import uy.com.marcher.superjumper.Game.Objects.*;
import uy.com.marcher.superjumper.Game.Objects.Decoration.Cloud;
import uy.com.marcher.superjumper.Util.Animation;

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
        if (world.jumper.position.y > cam.position.y) cam.position.y = world.jumper.position.y;
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        renderBackground();
        renderObjects();
    }

    public void renderBackground() {
        //batch.disableBlending();
        batch.begin();
        //batch.setColor(Color.SKY);
        batch.draw(Assets.backgroundRegion, cam.position.x - FRUSTUM_WIDTH / 2, cam.position.y - FRUSTUM_HEIGHT / 2, FRUSTUM_WIDTH,
                FRUSTUM_HEIGHT);

        batch.end();
    }

    public void renderObjects() {
        batch.enableBlending();
        batch.begin();
        renderClouds();
        renderPlatforms();
        renderBob();
        renderFrontClouds();
        renderItems();
        renderEnemies();
        //renderCastle();
        batch.end();

        drawEnemiesBounds(false);
        drawBobBounds(false);
        drawSpringBounds(false);
        drawPlatformBounds(false);
        drawStarBounds(false);
    }

    private void drawBobBounds(boolean render) {
        if(!render)
            return;
        sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        sr.rect(world.jumper.bounds.getX(), world.jumper.bounds.getY(),
                world.jumper.bounds.getWidth(), world.jumper.bounds.getHeight());
        sr.end();
    }

    private void drawPlatformBounds(boolean render) {
        if(!render)
            return;
        int len = world.platforms.size();
        for (int i = 0; i < len; i++) {
            sr.setProjectionMatrix(cam.combined);
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.RED);
            sr.rect(world.platforms.get(i).bounds.x, world.platforms.get(i).bounds.y,
                    world.platforms.get(i).bounds.getWidth(), world.platforms.get(i).bounds.getHeight());
            sr.end();
        }
    }

    private void drawStarBounds(boolean render) {
        if(!render)
            return;
        int len = world.stars.size();
        for (int i = 0; i < len; i++) {
            sr.setProjectionMatrix(cam.combined);
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.RED);
            sr.rect(world.stars.get(i).bounds.x, world.stars.get(i).bounds.y,
                    world.stars.get(i).bounds.getWidth(), world.stars.get(i).bounds.getHeight());
            sr.end();
        }
    }

    private void drawEnemiesBounds(boolean render) {
        if(!render)
            return;
        int len = world.enemies.size();
        for (int i = 0; i < len; i++) {
            sr.setProjectionMatrix(cam.combined);
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.RED);
            sr.rect(world.enemies.get(i).bounds.x, world.enemies.get(i).bounds.y,
                    world.enemies.get(i).bounds.getWidth(), world.enemies.get(i).bounds.getHeight());
            sr.end();
        }
    }

    private void drawSpringBounds(boolean render) {
        if(!render)
            return;
        int len = world.springs.size();
        for (int i = 0; i < len; i++) {
            sr.setProjectionMatrix(cam.combined);
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.RED);
            sr.rect(world.springs.get(i).bounds.x, world.springs.get(i).bounds.y,
                    world.springs.get(i).bounds.getWidth(), world.springs.get(i).bounds.getHeight());
            sr.end();
        }
    }

    private void renderBob() {
        TextureRegion keyFrame;
        world.jumper.render(batch);
        switch (world.jumper.state) {
            case Jumper.JUMPER_STATE_FALL:
                keyFrame = Assets.instance.jumper.jumperRegion;
                break;
            case Jumper.JUMPER_STATE_JUMP:
                keyFrame = Assets.instance.jumper.jumperRegion; //Assets.bobJump.getKeyFrame(world.jumper.stateTime, Animation.ANIMATION_LOOPING);

                break;
            case Jumper.JUMPER_STATE_HIT:
            default:
                keyFrame = Assets.instance.jumper.jumperDeadRegion;
        }

        if(!MathUtils.isEqual(world.jumper.velocity.x,0,0.1f)) {
            world.jumper.lookingAtSide = world.jumper.velocity.x < 0 ? 1 : -1;
        }
        if (world.jumper.lookingAtSide < 0)
            batch.draw(keyFrame, world.jumper.position.x + 0.5f, world.jumper.position.y - 0.5f,
                    (world.jumper.lookingAtSide * 1) * SCALE_RATE, (1.336f) * SCALE_RATE);
        else
            batch.draw(keyFrame, world.jumper.position.x - 1.1f, world.jumper.position.y - 0.5f,
                    (world.jumper.lookingAtSide * 1) * SCALE_RATE, (1.336f) * SCALE_RATE);


    }

    private void renderClouds() {
        int len = world.clouds.size();
        Color c = batch.getColor();
        for (int i = 0; i < len; i++) {
            Cloud cloud = world.clouds.get(i);

            batch.setColor(c.r,c.g,c.b,world.clouds.get(i).getAlpha());
            batch.draw(Assets.dustRegion, cloud.position.x,
                    cloud.position.y, 0, 0 , Assets.dustRegion.getRegionWidth()/40,  Assets.dustRegion.getRegionHeight()/40,1,1, cloud.rotation);
        }
        batch.setColor(c.r,c.g,c.b,1.0f);
    }

    private void renderFrontClouds() {
        int len = world.frontClouds.size();
        Color c = batch.getColor();
        for (int i = 0; i < len; i++) {
            Cloud cloud = world.frontClouds.get(i);

            batch.setColor(c.r,c.g,c.b,world.frontClouds.get(i).getAlpha());
            batch.draw(Assets.dustRegion, cloud.position.x,
                    cloud.position.y, 0, 0 , Assets.dustRegion.getRegionWidth()/40,  Assets.dustRegion.getRegionHeight()/40,1,1, cloud.rotation);
        }
        batch.setColor(c.r,c.g,c.b,1.0f);
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
        for (Spring spring : world.springs) {
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

        for (Star star : world.stars) {
            TextureRegion keyFrame = Assets.coinAnim.getKeyFrame(star.stateTime, Animation.ANIMATION_LOOPING);
            batch.draw(keyFrame, star.position.x - 0.5f, star.position.y - 0.5f, 1f, 1f);
//            batch.draw(Assets.instance.helpers.tunaCan, star.position.x  - 0.6f, star.position.y - 0.6f, 1.31f,1f);
        }
    }

    private void renderEnemies() {
        int len = world.enemies.size();
        for (int i = 0; i < len; i++) {
            Enemy enemy = world.enemies.get(i);
            TextureRegion keyFrame = Assets.squirrelFly.getKeyFrame(enemy.stateTime, Animation.ANIMATION_LOOPING);
            float side = enemy.velocity.x < 0 ? -1 : 1;
            if (side < 0)
                batch.draw(keyFrame, enemy.position.x + 0.5f, enemy.position.y - 0.5f, side * 1, 1.6f);
            else
                batch.draw(keyFrame, enemy.position.x - 0.5f, enemy.position.y - 0.5f, side * 1, 1.6f);
        }
    }

}
