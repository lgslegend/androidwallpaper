package donnu.zolotarev.wallpaper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import donnu.zolotarev.wallpaper.Screens.MainScreen;


public class WallPaper extends Game {


    private Screen screen;

    private boolean paused = false;
    private float screenOffset;
    private boolean settingChanged;


    @Override
    public void create() {

        screenOffset = 0.5f;
        settingChanged = true;
        paused = false;

        screen = new MainScreen(this);
        setScreen(screen);
    }

    @Override
    public void dispose() { screen.dispose(); }

    @Override
    public void render() { if(!paused) screen.render( Gdx.graphics.getDeltaTime() ); }

    @Override
    public void resize(int width, int height) { screen.resize( width, height ); }

    @Override
    public void pause() { paused = true; }

    @Override
    public void resume() { paused = false; }


    public boolean isSettingChanged() {
        boolean b = settingChanged;
        settingChanged = false;
        return b;
    }

    public void setSettingChanged() {
        this.settingChanged =true;
    }

    public float getScreenOffset() {
        return screenOffset;
    }

    public void setScreenOffset(float screenOffset) {
        this.screenOffset = screenOffset;
    }
}
