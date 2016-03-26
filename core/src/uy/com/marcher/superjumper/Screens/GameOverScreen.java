package uy.com.marcher.superjumper.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uy.com.marcher.superjumper.Game.Assets;
import uy.com.marcher.superjumper.SuperJumper;
import uy.com.marcher.superjumper.Util.Constants;
import uy.com.marcher.superjumper.Util.facebook.FacebookHelper;


/**
 * Created by gordo on 10/03/16.
 */
public class GameOverScreen extends ScreenAdapter {
    private final static String TAG = GameOverScreen.class.getName();

    public Stage stage;

    private Image facebookButton;
    private Window window;
    private OrthographicCamera camera;
    private SuperJumper game;
    private FacebookHelper facebookHelper;


    public GameOverScreen(SuperJumper game) {
        this.game = game;
        this.camera = new OrthographicCamera(Constants.FRUSTUM_WIDTH, Constants.FRUSTUM_HEIGHT);
        this.camera.position.set(Constants.FRUSTUM_WIDTH/2, Constants.FRUSTUM_HEIGHT/2,0);
        facebookHelper = new FacebookHelper(game);
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
        window.setColor(Color.GRAY);
        window.pack();
        window.setSize(Gdx.graphics.getWidth()/1.2f,Gdx.graphics.getHeight()/3);
        window.setPosition(
                Gdx.graphics.getWidth()/2 - window.getWidth()/2,
                Gdx.graphics.getHeight()/2 - window.getHeight()/2
        );
        return window;
    }

    private Table buildFacebookIcon() {
        Table layer = new Table();
        layer.pad(1f,1f,0,1f);
        layer.add(new Label("Connect to facebook", Assets.windowSkin));
        //TODO: If not logged in show fb icon
        layer.row();
        layer.columnDefaults(0).padRight(1f);
        layer.columnDefaults(1).padRight(1f);
        if(!game.facebook.isSignedIn()) {
            facebookButton = new Image(Assets.instance.GUI.facebookLogin);
            facebookButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    facebookHelper.login();
                }
            });
            layer.add(facebookButton).width(180).height(62);
        }
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
