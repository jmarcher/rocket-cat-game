package uy.com.marcher.superjumper.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import uy.com.marcher.superjumper.Game.Objects.*;
import uy.com.marcher.superjumper.Game.Objects.Decoration.Cloud;
import uy.com.marcher.superjumper.Util.Animation;
import uy.com.marcher.superjumper.Util.Constants;

public class WorldRenderer {
    private static final float SCALE_RATE = 1.3f;
    private World world;
    private OrthographicCamera cam;
    private SpriteBatch batch;
    private ShapeRenderer sr;

    public WorldRenderer(SpriteBatch batch, World world) {
        this.world = world;
        this.cam = new OrthographicCamera(Constants.FRUSTUM_WIDTH, Constants.FRUSTUM_HEIGHT);
        this.cam.position.set(Constants.FRUSTUM_WIDTH / 2, Constants.FRUSTUM_HEIGHT / 2, 0);
        this.batch = batch;
        this.sr = new ShapeRenderer();
    }

    public void render() {
        if (world.jumper.position.y > cam.position.y) cam.position.y = world.jumper.position.y;
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        renderBackground();
        renderObjects();
    }

    public void renderBackground() {
        //batch.disableBlending();
        batch.begin();
        //batch.setColor(Color.SKY);
        batch.draw(Assets.backgroundRegion, cam.position.x - Constants.FRUSTUM_WIDTH / 2, cam.position.y - Constants.FRUSTUM_HEIGHT / 2,
                Constants.FRUSTUM_WIDTH, Constants.FRUSTUM_HEIGHT);

        batch.end();
    }

    public void renderObjects() {
        batch.enableBlending();
        batch.begin();
        renderClouds();
        renderPlatforms();
        renderBob();
        renderItems();
        renderEnemies();
        renderFrontClouds();
        //renderCastle();
        batch.end();

        drawEnemiesBounds(false);
        drawBobBounds(false);
        drawSpringBounds(false);
        drawPlatformBounds(false);
        drawStarBounds(false);
        drawTunaCanBounds(false);
    }

    private void drawTunaCanBounds(boolean render) {
        if(!render)
            return;
        for(TunaCan tunaCan : world.tunaCans){
            initializeShapeRender();
            drawObjectBound(tunaCan);
            sr.end();
        }
    }

    private void initializeShapeRender() {
        sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
    }

    private void drawBobBounds(boolean render) {
        if(!render)
            return;
        initializeShapeRender();
        drawObjectBound(world.jumper);
        sr.end();
    }

    private void drawPlatformBounds(boolean render) {
        if(!render)
            return;
        int len = world.platforms.size();
        for (int i = 0; i < len; i++) {
            initializeShapeRender();
            drawObjectBound(world.platforms.get(i));
            sr.end();
        }
    }

    private void drawStarBounds(boolean render) {
        if(!render)
            return;
        int len = world.stars.size();
        for (int i = 0; i < len; i++) {
            initializeShapeRender();
            drawObjectBound(world.stars.get(i));
            sr.end();
        }
    }

    private void drawEnemiesBounds(boolean render) {
        if(!render)
            return;
        int len = world.enemies.size();
        for (int i = 0; i < len; i++) {
            initializeShapeRender();
            drawObjectBound(world.enemies.get(i));
            sr.end();
        }
    }

    private void drawObjectBound(GameObject gameObject) {
        sr.rect(gameObject.bounds.x,gameObject.bounds.y,
                gameObject.bounds.getWidth(), gameObject.bounds.getHeight());
    }

    private void drawSpringBounds(boolean render) {
        if(!render)
            return;
        int len = world.springs.size();
        for (int i = 0; i < len; i++) {
            initializeShapeRender();
            sr.rect(world.springs.get(i).bounds.x, world.springs.get(i).bounds.y,
                    world.springs.get(i).bounds.getWidth(), world.springs.get(i).bounds.getHeight());
            sr.end();
        }
    }

    private void renderBob() {
        world.jumper.render(batch);
    }

    private boolean onlyVisibleRangeRender(GameObject object) {
        return object.position.y >= world.heightSoFar - 10f && object.position.y <= world.heightSoFar + 10f;
    }

    private void renderClouds() {
        int len = world.clouds.size();
        Color c = batch.getColor();
        for (int i = 0; i < len; i++) {
            Cloud cloud = world.clouds.get(i);
            if(!onlyVisibleRangeRender(cloud))
                continue;
            batch.setColor(c.r,c.g,c.b,world.clouds.get(i).getAlpha());
            batch.draw(Assets.instance.decorations.cloud, cloud.position.x,
                    cloud.position.y, 0, 0 , Assets.instance.decorations.cloud.getRegionWidth()/40,
                    Assets.instance.decorations.cloud.getRegionHeight()/40,1,1, cloud.rotation);
        }
        batch.setColor(c.r,c.g,c.b,1.0f);
    }

    private void renderFrontClouds() {
        int len = world.frontClouds.size();
        Color c = batch.getColor();
        for (int i = 0; i < len; i++) {
            Cloud cloud = world.frontClouds.get(i);
            if(!onlyVisibleRangeRender(cloud))
                continue;
            batch.setColor(c.r,c.g,c.b,world.frontClouds.get(i).getAlpha());
            batch.draw(Assets.instance.decorations.cloud, cloud.position.x,
                    cloud.position.y, 0, 0 , Assets.instance.decorations.cloud.getRegionWidth()/40,
                    Assets.instance.decorations.cloud.getRegionHeight()/40,1,1, cloud.rotation);
        }
        batch.setColor(c.r,c.g,c.b,1.0f);
    }

    private void renderPlatforms() {
        int len = world.platforms.size();
        for (int i = 0; i < len; i++) {
            Platform platform = world.platforms.get(i);
            if(!onlyVisibleRangeRender(platform))
                continue;
            TextureRegion keyFrame = Assets.instance.helpers.platform;
            if (platform.state == Platform.PLATFORM_STATE_PULVERIZING) {
                keyFrame = Assets.instance.helpers.platformAnimation.getKeyFrame(platform.stateTime,
                        Animation.ANIMATION_NONLOOPING);
            }

            batch.draw(keyFrame, platform.position.x - 1, platform.position.y - 0.25f, 2 * SCALE_RATE,
                    0.5f * SCALE_RATE);
        }
    }

    private void renderItems() {
        renderSprings();

        renderStars();

        renderTunaCans();
    }

    private void renderTunaCans() {
        for (TunaCan tunaCan : world.tunaCans) {
            if(!onlyVisibleRangeRender(tunaCan))
                continue;
            TextureRegion tunaCanRegion = Assets.instance.helpers.tunaCan;
            batch.draw(tunaCanRegion, tunaCan.position.x - 0.5f, tunaCan.position.y - 0.5f, 1, 1);
        }
    }

    private void renderSprings() {
        for (Spring spring : world.springs) {
            if(!onlyVisibleRangeRender(spring))
                continue;
            if (!spring.wasUsed) {
                batch.draw(Assets.instance.helpers.spring, spring.position.x - 0.1f, spring.position.y-0.1f, 0.8f,
                        0.8f);
            } else {
                Gdx.app.debug("Spring", "Animation?");
                TextureRegion keyFrame = Assets.instance.helpers.springAnimation.getKeyFrame(spring.stateTime,
                        Animation.ANIMATION_NONLOOPING);
                batch.draw(keyFrame, spring.position.x, spring.position.y,
                        (keyFrame.getTexture().getWidth()*0.8f)/keyFrame.getTexture().getWidth(),
                        (keyFrame.getTexture().getHeight()*0.8f)/keyFrame.getTexture().getHeight());
            }
        }
    }

    private void renderStars() {
        for (Star star : world.stars) {
            if(!onlyVisibleRangeRender(star))
                continue;
            TextureRegion keyFrame = Assets.instance.helpers.starAnimation.getKeyFrame(star.stateTime,
                    Animation.ANIMATION_LOOPING);
            batch.draw(keyFrame, star.position.x - 0.5f, star.position.y - 0.5f, 1, 1);

            //batch.draw(Assets.instance.helpers.tunaCan, star.position.x  - 0.6f, star.position.y - 0.6f, 1.31f,1);
        }
    }

    private void renderEnemies() {
        int len = world.enemies.size();
        for (int i = 0; i < len; i++) {
            Enemy enemy = world.enemies.get(i);
            if(!onlyVisibleRangeRender(enemy))
                continue;
            TextureRegion keyFrame = Assets.instance.enemies.deathBaloon;
            float side = enemy.velocity.x < 0 ? -1 : 1;
            if (side < 0)
                batch.draw(keyFrame, enemy.position.x + 0.5f, enemy.position.y - 0.5f, side * 1, 1.6f);
            else
                batch.draw(keyFrame, enemy.position.x - 0.5f, enemy.position.y - 0.5f, side * 1, 1.6f);
        }
    }

}
