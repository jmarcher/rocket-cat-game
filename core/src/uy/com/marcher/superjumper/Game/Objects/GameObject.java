package uy.com.marcher.superjumper.Game.Objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
    public final Vector2 position;
    public Rectangle bounds;
    public Vector2 origin;
    public Vector2 dimension;
    public Vector2 scale;


    public GameObject(float x, float y, float width, float height) {
        this.position = new Vector2(x, y);
        this.bounds = new Rectangle(x - width / 2, y - height / 2, width, height);
        this.origin = new Vector2();
        this.dimension=new Vector2(1,1);
        this.scale= new Vector2(1,1);
    }

    public void render(SpriteBatch batch){

    }
}
