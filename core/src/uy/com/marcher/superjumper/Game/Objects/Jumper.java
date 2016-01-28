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

package uy.com.marcher.superjumper.Game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import uy.com.marcher.superjumper.Game.World;

public class Jumper extends DynamicGameObject {


    public static final int JUMPER_STATE_JUMP = 0;
    public static final int JUMPER_STATE_FALL = 1;
    public static final int JUMPER_STATE_HIT = 2;
    public static final float JUMPER_JUMP_VELOCITY = 14;
    public static final float JUMPER_MOVE_VELOCITY = 20;
    public static final float JUMPER_WIDTH = 1f;
    public static final float JUMPER_HEIGHT = 1.2f;
    public ParticleEffect fireParticle = new ParticleEffect();

    public int state;
    public float stateTime;
    public float lookingAtSide;

    public Jumper(float x, float y) {
        super(x, y, JUMPER_WIDTH, JUMPER_HEIGHT);
        state = JUMPER_STATE_FALL;
        stateTime = 0;
        fireParticle.load(Gdx.files.internal("data/particles/fireNsmoke.pfx"), Gdx.files.internal("data/particles"));
        fireParticle.flipY();
        fireParticle.scaleEffect(.01f);
        lookingAtSide = 1;
        //fireEmitter = fireParticle.getEmitters().first();
        //fireParticle.start();
    }

    public void update(float deltaTime) {
        velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.x = position.x - bounds.width / 2;
        bounds.y = (position.y - bounds.height / 3);
        //Gdx.gl.glClear(GL20.GL_ACTIVE_TEXTURE);
        if(velocity.y > 0 && state == JUMPER_STATE_JUMP){
            //fireParticle.getEmitters().first().setMaxParticleCount(this.maxParticleCount);
            fireParticle.getEmitters().first().setContinuous(true);
            fireParticle.setPosition(this.position.x -0.3f, this.position.y-0.3f);
            fireParticle.allowCompletion();
            fireParticle.start();
        }else{
            fireParticle.getEmitters().first().setContinuous(false);
            fireParticle.setPosition(this.position.x -0.3f, this.position.y-0.3f);
            fireParticle.getEmitters().first().duration = 0.1f;
        }
        fireParticle.update(deltaTime*2f);
        if (velocity.y > 0 && state != JUMPER_STATE_HIT) {
            if (state != JUMPER_STATE_JUMP) {
                state = JUMPER_STATE_JUMP;
                //this.fireParticle.allowCompletion();
                stateTime = 0;
            }
        }

        if (velocity.y < 0 && state != JUMPER_STATE_HIT) {
            if (state != JUMPER_STATE_FALL) {
                state = JUMPER_STATE_FALL;
                stateTime = 0;
            }
        }

        if (position.x < 0) position.x = World.WORLD_WIDTH;
        if (position.x > World.WORLD_WIDTH) position.x = 0;

        stateTime += deltaTime;
    }

    /**
     * Pre: Preinitialized batch
     *
     * @param batch
     */
    public void render(SpriteBatch batch) {
        fireParticle.draw(batch);
    }

    public void hitSquirrel() {
        velocity.set(0, 0);
        state = JUMPER_STATE_HIT;
        stateTime = 0;
    }

    @Deprecated
    public void hitPlatform() { //TODO: Borrable, solo dirsponible para compatibilidad
        makeJump();
    }

    public void makeJump(){
        velocity.y = JUMPER_JUMP_VELOCITY;
        state= JUMPER_STATE_JUMP;
        stateTime = 0;
    }

    public void hitSpring() {
        velocity.y = JUMPER_JUMP_VELOCITY * 2.5f;
        state = JUMPER_STATE_JUMP;
        stateTime = 0;
    }
}
