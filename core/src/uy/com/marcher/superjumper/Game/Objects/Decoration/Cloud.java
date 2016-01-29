package uy.com.marcher.superjumper.Game.Objects.Decoration;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import uy.com.marcher.superjumper.Game.Assets;
import uy.com.marcher.superjumper.Game.Objects.DynamicGameObject;
import uy.com.marcher.superjumper.Game.Objects.GameObject;
import uy.com.marcher.superjumper.Game.World;

/**
 * Created by gordo on 27/01/16.
 */
public class Cloud extends DynamicGameObject {
    private TextureRegion dustRegion;



    public float rotation;
    private int length;
    private float alpha;

    public Cloud(){
        super(0,0,World.WORLD_WIDTH,World.WORLD_HEIGHT);

        length =0;
        init();
    }

    public Cloud(float x, float y, float width, float height) {
        super(x, y, width, height);

        length =0;
        init();
    }

    private void init(){
        dustRegion = Assets.dustRegion;
        dimension.set(World.WORLD_WIDTH, World.WORLD_HEIGHT);
        origin.x = -dimension.x *2;
        length += dimension.y *2;
        velocity.x = MathUtils.random(-0.5f,0.6f);
        velocity.y = MathUtils.random(-0.6f,0.5f);
        alpha = MathUtils.random(0.5f,0.95f);
    }

    private void drawDust(SpriteBatch batch, float offsetX, float offsetY, float tinitColor){
        TextureRegion region = null;
        batch.setColor(tinitColor,tinitColor,tinitColor,1);

        float xRel = dimension.x * offsetX;
        float yRel = dimension.y * offsetY;

        int dustLength = 0;
        dustLength += MathUtils.ceil(length / (2 * dimension.y));
        dustLength += MathUtils.ceil(0.5f + offsetY);

        for(int i = 0; i<dustLength; ++i){
            region = dustRegion;
            batch.draw(region.getTexture(), position.x + origin.x + xRel, origin.y + yRel, origin.x, origin.y,
                    dimension.x,dimension.y, scale.x, scale.y, 0, region.getRegionX(), region.getRegionY(), region.getRegionWidth(),
                    region.getRegionHeight(),false, false);
            yRel += dimension.y;
        }
        batch.setColor(1,1,1,1);
    }

    public void render(SpriteBatch batch){
        drawDust(batch, 0.5f, 0.5f, 0.5f);

        drawDust(batch, 0.25f, 0.25f, 0.25f);
    }

    public void update(float deltaTime) {
            position.add(velocity.x * deltaTime, velocity.y*deltaTime);
    }

    public float getAlpha() {
        return alpha;
    }
}
