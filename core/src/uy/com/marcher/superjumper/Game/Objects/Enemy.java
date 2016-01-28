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

import uy.com.marcher.superjumper.Game.World;

public class Enemy extends DynamicGameObject {
    public static final float ENEMY_WIDTH = 0.5f;
    public static final float ENEMY_HEIGHT = 0.5f;
    public static final float ENEMY_VELOCITY = 2f;

    public float stateTime = 0;

    public Enemy(float x, float y) {
        super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
        velocity.set(ENEMY_VELOCITY, 0);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.x = position.x -0.3f;
        bounds.y = position.y -.5f;

        if (position.x < ENEMY_WIDTH / 2) {
            position.x = ENEMY_WIDTH / 2;
            velocity.x = ENEMY_VELOCITY;
        }
        if (position.x > World.WORLD_WIDTH - ENEMY_WIDTH / 2) {
            position.x = World.WORLD_WIDTH - ENEMY_WIDTH / 2;
            velocity.x = -ENEMY_VELOCITY;
        }
        stateTime += deltaTime;
    }
}
