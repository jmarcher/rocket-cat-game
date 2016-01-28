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

public class Spring extends GameObject {
    public static float SPRING_WIDTH = 0.8f;
    public static float SPRING_HEIGHT = 0.8f;
    public boolean wasUsed = false;

    public float stateTime;


    public Spring(float x, float y) {
        super(x, y, SPRING_WIDTH, SPRING_HEIGHT);
        bounds.x = position.x -0.1f;
        bounds.y = position.y -0.2f;
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
    }
}
