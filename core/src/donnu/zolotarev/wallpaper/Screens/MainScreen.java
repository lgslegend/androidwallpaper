package donnu.zolotarev.wallpaper.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import donnu.zolotarev.wallpaper.Actors.Background;
import donnu.zolotarev.wallpaper.Assets.TextureAssets;
import donnu.zolotarev.wallpaper.WallPaper;

public class MainScreen implements Screen {
    private final OrthographicCamera camera;
    private final WallPaper wallPaper;

    private final ShapeRenderer renderer;
    private final TextureAssets assets;
    private final SpriteBatch batch;
    private final Stage stage;
    private Background background;

    private boolean isScreenHided;
    private boolean isScreenResting;

    private Color bgColor;


    private final ShaderProgram shader;
    private com.badlogic.gdx.math.Vector3 touchPos;

    public MainScreen(WallPaper wallPaper) {
        this.wallPaper = wallPaper;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        touchPos = new Vector3();
        
        batch = new SpriteBatch();
        Viewport view = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),camera);
        stage = new Stage(view,batch){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {

                touchPos.set(screenX, screenY, 0);
                camera.unproject(touchPos);
                shader.begin();
                shader.setUniformf("iMouse", touchPos.x, touchPos.y   );
                shader.end();

                return true;
            }
        };

        assets = new TextureAssets();

        renderer = new ShapeRenderer(10);
        renderer.setProjectionMatrix(camera.combined);

        background = new Background();
        stage.addActor(background);
        assets.load(new TextureAssets.ITextureAssetsListener() {
            @Override
            public void loaded() {
                background.updateImage();
            }
        });

        ShaderProgram.pedantic = false; //todo ???
        shader = new ShaderProgram(Gdx.files.internal("shaders/wave.vsh"),Gdx.files.internal("shaders/wave.fsh"));
        System.out.println(shader.isCompiled() ? "shader compaled, yay" : shader.getLog());
        batch.setShader(shader);

        Gdx.input.setInputProcessor(stage);

    }


    float time = 0;
    @Override
    public void render(float delta) {

        boolean settingChanged = wallPaper.isSettingChanged();
        if(settingChanged) {
            backgroundSetting();
        }

        if (!isScreenHided && !isScreenResting && !settingChanged){


            time += delta;
            shader.begin();
            shader.setUniformf("iGlobalTime", time);
            shader.end();

            Gdx.gl20.glViewport (0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.gl20.glClearColor( bgColor.r, bgColor.g, bgColor.b, bgColor.a );

            float d = wallPaper.getScreenOffset() - 0.5f;
            if(d < 0.0f) d *= -1.0f;

//            camera.position.set(0 + 2.0f - lwp.screenOffset * 4.0f + cameraX_Actor.getX(), 0.0f + cameraY_Actor.getX(), 305.0f - d * 4 - cameraZ_Actor.getX());
//            camera.lookAt(0.0f - 5.0f + lwp.screenOffset * 10.0f + cameraX_Actor.getX(), 0.0f + cameraY_Actor.getX(), 0.0f - d * 50 - cameraZ_Actor.getX());
//            camera.lookAt(0,Gdx.graphics.getWidth()*wallPaper.getScreenOffset(),0);

//            camera.position.set(Gdx.graphics.getWidth() * wallPaper.getScreenOffset(), 0, 0);
            camera.update();
            assets.update();



            background.setScreenOffset(wallPaper.getScreenOffset());
            stage.draw();
            stage.act(delta);



            //renderer.ellipse();

        }
    }

    private void backgroundSetting() {
        bgColor = new Color(1f,1f,1f,1f);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
        shader.begin();
        shader.setUniformf("u_resolution", width, height);
        shader.end();
    }

    @Override
    public void show() {
        isScreenHided = false;
        isScreenResting = false;
    }

    @Override
    public void hide() { isScreenHided = true; }

    @Override
    public void pause() { isScreenResting = true; }

    @Override
    public void resume() { isScreenHided = false; isScreenResting = false; }

    @Override
    public void dispose() {
        stage.dispose();
        assets.dispose();
        batch.dispose();
    }
}
