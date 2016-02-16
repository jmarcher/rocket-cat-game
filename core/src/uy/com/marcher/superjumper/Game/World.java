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

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import uy.com.marcher.superjumper.Game.Objects.*;
import uy.com.marcher.superjumper.Game.Objects.Decoration.Cloud;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    public static final float WORLD_WIDTH = 10;
    public static final float WORLD_HEIGHT = (15 * 20)*80;
    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_NEXT_LEVEL = 1;
    public static final int WORLD_STATE_GAME_OVER = 2;
    public static final Vector2 gravity = new Vector2(0, -20);

    public float slowMotionFactor = 1.0f;

    public final Jumper jumper;
    public final List<Platform> platforms;
    public final List<Spring> springs;
    public final List<Enemy> enemies;
    public final List<Star> stars;
    public final List<TunaCan> tunaCans;
    public final List<Cloud> clouds;
    public final List<Cloud> frontClouds;

    public final WorldListener listener;
    public final Random randomizer;
    public Castle castle;
    public float heightSoFar;
    public int score;
    public int state;
    public World(WorldListener listener) {
        this.jumper = new Jumper(5, 1);
        this.jumper.setWorld(this);
        this.platforms = new ArrayList<Platform>();
        this.springs = new ArrayList<Spring>();
        this.enemies = new ArrayList<Enemy>();
        this.stars = new ArrayList<Star>();
        this.clouds = new ArrayList<Cloud>();
        this.frontClouds = new ArrayList<Cloud>();
        this.tunaCans = new ArrayList<TunaCan>();
        this.listener = listener;
        randomizer = new Random();
        generateLevel();

        this.heightSoFar = 0;
        this.score = 0;
        this.state = WORLD_STATE_RUNNING;
    }

    private void generateLevel() {
        float y = Platform.PLATFORM_HEIGHT / 2;
        float maxJumpHeight = Jumper.JUMPER_JUMP_VELOCITY * Jumper.JUMPER_JUMP_VELOCITY / (2 * -gravity.y);
        while (y < WORLD_HEIGHT - WORLD_WIDTH / 2) {
            int type = randomizer.nextFloat() > 0.8f ? Platform.PLATFORM_TYPE_MOVING : Platform.PLATFORM_TYPE_STATIC;
            float x = randomizer.nextFloat() * (WORLD_WIDTH - Platform.PLATFORM_WIDTH) + Platform.PLATFORM_WIDTH / 2;


            Platform platform = new Platform(type, x, y);

            if (randomizer.nextFloat() > 0.9f && type != Platform.PLATFORM_TYPE_MOVING) {

                Spring spring = new Spring(platform.position.x, platform.position.y + Platform.PLATFORM_HEIGHT / 2
                       /*+ Spring.SPRING_HEIGHT / 2*/);
                springs.add(spring);
                platforms.add(platform);
            }else{
                if(randomizer.nextFloat() > 0.60f){
                    Cloud cloud = new Cloud();
                    cloud.rotation = MathUtils.random(-10f, 10f);
                    cloud.position.x = x;
                    cloud.position.y = y;
                    clouds.add(cloud);
                }else if(randomizer.nextFloat() > 0.85f){
                    Cloud cloud = new Cloud();
                    cloud.rotation = MathUtils.random(-10f, 10f);
                    cloud.position.x = x;
                    cloud.position.y = y;
                    frontClouds.add(cloud);
                }
            }

           // enemies.add(new Enemy(1f,3f)); //FIXME: Solo para probar
            if (y > WORLD_HEIGHT / 15 && randomizer.nextFloat() > 0.8f) {//FIXME: No hay enemigos para probar
                Enemy enemy = new Enemy(platform.position.x + randomizer.nextFloat(), platform.position.y
                        + Enemy.ENEMY_HEIGHT + randomizer.nextFloat() * 2);
                enemies.add(enemy);
            }

            if (randomizer.nextFloat() > 0.6f) {
                Star star = new Star(platform.position.x + randomizer.nextFloat(), platform.position.y + Star.STAR_HEIGHT
                        + randomizer.nextFloat() * 3);
                stars.add(star);
            }

            if (y > WORLD_HEIGHT / 15 && randomizer.nextFloat() > 0.99f) {
                TunaCan tunaCan = new TunaCan(platform.position.x + randomizer.nextFloat(), platform.position.y + Star.STAR_HEIGHT
                        + randomizer.nextFloat() * 3);
                tunaCans.add(tunaCan);
            }

            y += (maxJumpHeight - 0.5f);
            y -= randomizer.nextFloat() * (maxJumpHeight / 3);
        }

        //castle = new Castle(WORLD_WIDTH / 2, 50);//y);
    }

    public void update(float deltaTime, float accelX) {

        slowMotionFactor += 0.0001f;
        deltaTime = deltaTime * slowMotionFactor;
        updateBob(deltaTime, accelX);
        updatePlatforms(deltaTime);
        updateSquirrels(deltaTime);
        updateTunaCans(deltaTime);
        updateCoins(deltaTime);
        updateSprings(deltaTime);
        updateDusts(deltaTime);
        if (jumper.state != Jumper.JUMPER_STATE_HIT) checkCollisions();
        checkGameOver();
    }


    private void updateBob(float deltaTime, float accelX) {
        if (jumper.state != Jumper.JUMPER_STATE_HIT && jumper.position.y <= 0.3f) jumper.hitPlatform();
        if (jumper.state != Jumper.JUMPER_STATE_HIT) jumper.velocity.x = -accelX / 10 * Jumper.JUMPER_MOVE_VELOCITY;
        jumper.update(deltaTime);
        heightSoFar = Math.max(jumper.position.y, heightSoFar);
    }

    private void updatePlatforms(float deltaTime) {
        int len = platforms.size();
        for (int i = 0; i < len; i++) {
            Platform platform = platforms.get(i);
            if(!onlyVisibleRangeUpdate(platform))
                continue;
            platform.update(deltaTime);
            if (platform.state == Platform.PLATFORM_STATE_PULVERIZING && platform.stateTime > Platform.PLATFORM_PULVERIZE_TIME) {
                platforms.remove(platform);
                len = platforms.size();
            }
        }
    }

    private void updateDusts(float deltaTime) {
        for(Cloud cloud : clouds){
            if(!onlyVisibleRangeUpdate(cloud))
                continue;
            cloud.update(deltaTime);
        }
    }

    private boolean onlyVisibleRangeUpdate(GameObject object) {
        if(object.position.y>= heightSoFar-10f && object.position.y <= heightSoFar+10f)
            return true;
        return false;
    }

    private void updateSquirrels(float deltaTime) {
        for(Enemy enemy :enemies){
            if(!onlyVisibleRangeUpdate(enemy))
                continue;
            enemy.update(deltaTime);
        }
    }

    private void updateTunaCans(float deltaTime) {
        for(TunaCan tunaCan : tunaCans){
            tunaCan.update(deltaTime);
        }
    }


    private void updateCoins(float deltaTime) {
        for(Star star : stars){
            if(!onlyVisibleRangeUpdate(star))
                continue;
            star.update(deltaTime);
        }
    }

    private void updateSprings(float deltaTime) {
        int len = springs.size();
        for (int i = 0; i < len; i++) {

            Spring spring = springs.get(i);
            if(!onlyVisibleRangeUpdate(spring))
                continue;
            spring.update(deltaTime);
        }
    }

    private void checkCollisions() {
        checkPlatformCollisions();
        checkEnemyCollisions();
        checkItemCollisions();
        checkTunaCansCollisions();
        //checkCastleCollisions();
    }

    private void checkPlatformCollisions() {
        if (jumper.velocity.y > 0) return;

        for(Platform platform : platforms){
            if (jumper.position.y > platform.position.y) {
                if (jumper.bounds.overlaps(platform.bounds)) {
                    //jumper.hitPlatform();
                    //listener.jump();
                    jumper.velocity.y = Jumper.JUMPER_JUMP_VELOCITY / 4;
                    if (randomizer.nextFloat() > 0.8f && platform.position.y > 80.0f){
                        platform.pulverize();
                    }
                    break;
                }
            }
        }
    }

    private void checkEnemyCollisions() {
        for(Enemy enemy : enemies){
            if (enemy.bounds.overlaps(jumper.bounds)) {
                jumper.hitEnemy();
                listener.hit();
            }
        }
    }

    private void checkTunaCansCollisions(){
        for(TunaCan tunaCan : tunaCans){
            if(tunaCan.bounds.overlaps(jumper.bounds)){
                jumper.hitTunaCan();
                listener.tunaCan();
                score += TunaCan.GetScore();
            }
        }
    }

    private void checkItemCollisions() {
        int len = stars.size();
        for (int i = 0; i < len; i++) {
            Star star = stars.get(i);
            if (jumper.bounds.overlaps(star.bounds)) {
                stars.remove(star);
                len = stars.size();
                listener.coin();
                score += Star.STAR_SCORE;
            }

        }

        if (jumper.velocity.y > 0) return;

        len = springs.size();
        for (int i = 0; i < len; i++) {
            Spring spring = springs.get(i);
            if (jumper.position.y > spring.position.y) {
                if (jumper.bounds.overlaps(spring.bounds)) {
                    spring.wasUsed = true;
                    jumper.hitSpring();
                    listener.highJump();
                }
            }
        }
    }


    private void checkGameOver() {
        if (heightSoFar - 7.5f > jumper.position.y) {
            state = WORLD_STATE_GAME_OVER;
            Assets.stopAllSound();
        }
    }

    public interface WorldListener {

        void highJump();

        void hit();

        void coin();

        void tunaCan();
    }
}
