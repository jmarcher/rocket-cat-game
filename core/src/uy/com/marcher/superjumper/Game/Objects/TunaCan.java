package uy.com.marcher.superjumper.Game.Objects;

import com.badlogic.gdx.math.Rectangle;
import uy.com.marcher.superjumper.Game.World;

/**
 * Created by gordo on 04/02/16.
 */
public class TunaCan extends DynamicGameObject {
    public static final float TUNACAN_WIDTH = 0.8f;
    public static final float TUNACAN_HEIGHT = 0.8f;
    public static final int TUNACAN_SCORE = 180;

    public static final float TUNACAN_VELOCITY = 10f;

    public float stateTime;

    public TunaCan(float x, float y) {
        super(x, y, TUNACAN_WIDTH, TUNACAN_HEIGHT);
        velocity.set(TUNACAN_VELOCITY,0f);
        stateTime = 0f;
        bounds = new Rectangle(x - TUNACAN_WIDTH / 2 - 0.1f, y - TUNACAN_HEIGHT / 2, TUNACAN_WIDTH+0.1f, TUNACAN_HEIGHT);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.x = position.x -0.3f;
        bounds.y = position.y -.5f;

        if (position.x < TUNACAN_WIDTH / 2) {
            position.x = TUNACAN_WIDTH / 2;
            velocity.x = TUNACAN_VELOCITY;
        }
        if (position.x > World.WORLD_WIDTH - TUNACAN_WIDTH / 2) {
            position.x = World.WORLD_WIDTH - TUNACAN_WIDTH / 2;
            velocity.x = -TUNACAN_VELOCITY;
        }
        stateTime += deltaTime;
    }

    public static int GetScore(){
        return TUNACAN_SCORE;
    }
}
