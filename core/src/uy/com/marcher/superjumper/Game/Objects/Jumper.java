package uy.com.marcher.superjumper.Game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import uy.com.marcher.superjumper.Game.Assets;
import uy.com.marcher.superjumper.Game.World;
import uy.com.marcher.superjumper.Util.TextureHelper;

public class Jumper extends DynamicGameObject {

    public static final int JUMPER_STATE_JUMP = 0;
    public static final int JUMPER_STATE_FALL = 1;
    public static final int JUMPER_STATE_HIT = 2;
    public static final float JUMPER_JUMP_VELOCITY = 14;
    public static final float JUMPER_MOVE_VELOCITY = 20;
    private static final float JUMPER_WIDTH = 1f;
    private static final float JUMPER_HEIGHT = 1.7594f;
    private static final float TUNACAN_TIME = 10f;

    private ParticleEffect fireParticle = new ParticleEffect();
    private int state;
    private float stateTime;
    private float lookingAtSide;
    private float tunaCanLeftTime;
    private World world;

    public Jumper(float x, float y) {
        super(x, y, JUMPER_WIDTH, JUMPER_HEIGHT);
        setState(JUMPER_STATE_FALL);
        stateTime = 0;
        fireParticle.load(Gdx.files.internal("data/particles/fireNsmoke.pfx"), Gdx.files.internal("data/particles"));
        fireParticle.flipY();
        fireParticle.scaleEffect(.01f);
        lookingAtSide = 1;
        tunaCanLeftTime = 0f;
        //fireEmitter = fireParticle.getEmitters().first();
        //fireParticle.start();
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void update(float deltaTime) {
        updateTunaCanEffect(deltaTime);
        velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        updateBoundsPosition();
        //Gdx.gl.glClear(GL20.GL_ACTIVE_TEXTURE);
        updateFireParticleEffect(deltaTime);
        if (velocity.y > 0 && getState() != JUMPER_STATE_HIT) {
            if (getState() != JUMPER_STATE_JUMP) {
                setState(JUMPER_STATE_JUMP);
                //this.fireParticle.allowCompletion();
                stateTime = 0;
            }
        }

        if (velocity.y < 0 && getState() != JUMPER_STATE_HIT) {
            if (getState() != JUMPER_STATE_FALL) {
                setState(JUMPER_STATE_FALL);
                stateTime = 0;
            }
        }

        if (position.x < 0) position.x = World.WORLD_WIDTH;
        if (position.x > World.WORLD_WIDTH) position.x = 0;

        stateTime += deltaTime;
    }

    private void updateBoundsPosition() {
        float xOffset = -0.1f;
        if (this.lookingAtSide >= 0)
            xOffset = -0.4f;
        bounds.x = xOffset + position.x - bounds.width / 2 ;
        bounds.y = (position.y - bounds.height / 3);
    }

    private void updateFireParticleEffect(float deltaTime) {
        if(velocity.y > 0 && getState() == JUMPER_STATE_JUMP){
            //fireParticle.getEmitters().first().setMaxParticleCount(this.maxParticleCount);
            fireParticle.getEmitters().first().setContinuous(true);
            fireParticle.setPosition(this.position.x - 0.3f, this.position.y - 0.3f);
            fireParticle.allowCompletion();
            fireParticle.start();
        }else{
            fireParticle.getEmitters().first().setContinuous(false);
            fireParticle.setPosition(this.position.x -0.3f, this.position.y-0.3f);
            fireParticle.getEmitters().first().duration = 0.1f;
        }
        fireParticle.update(deltaTime*2f);
    }

    private void updateTunaCanEffect(float deltaTime) {
        if(tunaCanLeftTime > 0){
            tunaCanLeftTime -= deltaTime;
            if(tunaCanLeftTime <= 0){
                tunaCanLeftTime = 0;
                this.velocity.y = getJumperVelocity();
               // System.out.println("termina tuna can");
            }
        }
    }

    /**
     * Pre: Preinitialized batch
     *
     * @param batch
     */
    @Override
    public void render(SpriteBatch batch) {
        TextureRegion keyFrame;
        renderParticles(batch);
        renderTunaCanEffect(batch);

        switch (this.getState()) {
            case Jumper.JUMPER_STATE_FALL:
                keyFrame = Assets.instance.jumper.jumperRegion;
                break;
            case Jumper.JUMPER_STATE_JUMP:
                keyFrame = Assets.instance.jumper.jumperRegion; //Assets.bobJump.getKeyFrame(this.stateTime, Animation.ANIMATION_LOOPING);

                break;
            case Jumper.JUMPER_STATE_HIT:
            default:
                keyFrame = Assets.instance.jumper.jumperDeadRegion;
        }

        if(!MathUtils.isEqual(this.velocity.x,0,0.1f)) {
            this.lookingAtSide = this.velocity.x < 0 ? 1 : -1;
        }

        float xOffset = -1.1f;
        if (this.lookingAtSide < 0)
            xOffset = 0.5f;
        batch.draw(keyFrame, this.position.x + xOffset, this.position.y - 0.5f,
                (this.lookingAtSide * 1f) * 1.3f, TextureHelper.textureToFrustumHeight(keyFrame) * 1.3f);

        batch.setColor(1f,1f,1f,1f);
    }

    private void renderTunaCanEffect(SpriteBatch batch) {
        if(tunaCanLeftTime > 2f)
            batch.setColor(1.0f,1.0f,0.25f,1.0f);
        if(tunaCanLeftTime> 0f && tunaCanLeftTime <= 2f && MathUtils.round(tunaCanLeftTime * 10f)%2 == 0 ){
            batch.setColor(1.0f,1.0f,0.25f,1.0f);
        }else if(tunaCanLeftTime> 0f && tunaCanLeftTime <= 2f){
            batch.setColor(1.0f,1.0f,0.25f,.5f);
        }
    }

    private void renderParticles(SpriteBatch batch) {
        fireParticle.draw(batch);
    }

    public void hitEnemy() {
        velocity.set(0, 0);
        setState(JUMPER_STATE_HIT);
        stateTime = 0;
    }

    @Deprecated
    public void hitPlatform() { //TODO: Borrable, solo dirsponible para compatibilidad
        makeJump();
    }

    public void makeJump(){
        velocity.y = getJumperVelocity();
        setState(JUMPER_STATE_JUMP);
        stateTime = 0;
    }

    private float getJumperVelocity() {
        if(tunaCanLeftTime > 0f)
            return JUMPER_JUMP_VELOCITY *2.5f;
        return JUMPER_JUMP_VELOCITY;
    }

    public void hitSpring() {
        velocity.y = JUMPER_JUMP_VELOCITY * 2.5f;
        setState(JUMPER_STATE_JUMP);
        stateTime = 0;
    }

    public void hitTunaCan() {
      tunaCanLeftTime = TUNACAN_TIME;
    }

    public boolean isDead() {
        return getState() == JUMPER_STATE_HIT;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
