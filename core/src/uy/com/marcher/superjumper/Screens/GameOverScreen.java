package uy.com.marcher.superjumper.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uy.com.marcher.superjumper.Game.Assets;
import uy.com.marcher.superjumper.SuperJumper;
import uy.com.marcher.superjumper.Util.Constants;

/**
 * Created by gordo on 10/03/16.
 */
public class GameOverScreen extends ScreenAdapter {
    private final static String TAG = GameOverScreen.class.getName();

    public Stage stage;

    private Image facebook;
    private Window window;
    private OrthographicCamera camera;


    public GameOverScreen() {
        this.camera = new OrthographicCamera(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
        this.camera.position.set(Constants.VIRTUAL_WIDTH/2, Constants.VIRTUAL_HEIGHT/2,0);
    }


    private void rebuildStage(){
        Table window = buildWindowLayer();
        stage.clear();
        /*Stack stack = new Stack();
        stack.setSize(stage.getWidth(),stage.getHeight());
        /*stack.setPosition(Constants.VIRTUAL_WIDTH/2 - stack.getWidth()/2,
                Constants.VIRTUAL_HEIGHT/2 - stack.getHeight()/2);

        stack.addActor(window);*/
        stage.addActor(window);

    }

    private Table buildWindowLayer(){
        window = new Window("Game over", Assets.windowSkin);

        Table layer = buildFacebookIcon();
        window.add(layer).row();
        window.setVisible(true);
        window.setColor(1f,.9f,.973f,0.9f);
        window.pack();
        window.setSize(200,200);
        window.setPosition(
                Constants.VIRTUAL_WIDTH/2 + window.getWidth()/2,
                Constants.VIRTUAL_HEIGHT/2 + window.getHeight()/2
        );
        return window;
    }

    private Table buildFacebookIcon() {
        Table layer = new Table();
        layer.pad(10,10,0,10);
        layer.add(new Label("Connect to facebook", Assets.windowSkin));
        //TODO: If not logged in show fb icon
        layer.row();
        layer.columnDefaults(0).padRight(10);
        layer.columnDefaults(1).padRight(10);
        facebook = new Image(Assets.instance.GUI.facebookLogin);
        facebook.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                //TODO:hacer login con facebook
            }
        });
        layer.add(facebook).width(90).height(31);
        return layer;
    }

    /*public void draw(){
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.batcher.setProjectionMatrix(camera.combined);
        game.batcher.disableBlending();
        game.batcher.begin();

    }*/



    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height);
    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport(camera));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
    }

    @Override
    public void render(float delta) {
        /*Gdx.gl.glBlendColor(0.0f,0.0f,0.0f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/
        stage.act(delta);
        stage.draw();

        //if(Gdx.input.isTouched()) System.out.println("toqué la pantalla");
    }


}
