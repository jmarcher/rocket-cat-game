package uy.com.marcher.superjumper.Screens;

/**
 * Created by gordo on 26/01/16.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {
    private SpriteBatch batch;
    private Texture ttrSplash;

    public SplashScreen() {
        super();
        batch = new SpriteBatch();
        ttrSplash = new Texture(Gdx.files.internal("data/nerdsLogo.png"));
        ttrSplash.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, .98f, .973f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(ttrSplash,
                Gdx.graphics.getWidth()/2 - ttrSplash.getWidth()/2,
                Gdx.graphics.getHeight()/2 - ttrSplash.getHeight()/2,0,0,310,95);
        batch.end();
    }

    @Override
    public void hide() { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void show() { }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void dispose() {
        ttrSplash.dispose();
        batch.dispose();
    }
}