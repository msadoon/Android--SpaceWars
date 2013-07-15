package mkat.apps.spacewars;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;

import com.badlogic.gdx.math.Vector2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;


public class GameActivity extends BaseGameActivity {
	
	// ====================================================
	// CONSTANTS
	// ====================================================
	// we define these constants to setup the game to use an appropriate camera resolution independent of the actual end-user's screen resolution
	
	//The resolution of the screen with which you are developing.
	static float DESIGN_SCREEN_WIDTH_PIXELS = 800f;
	static float DESIGN_SCREEN_HEIGHT_PIXELS = 480f;
	//The physical size of the screen with which you are developing.
	//static float DESIGN_SCREEN_WIDTH_INCHES = 4.4722441f;
	//static float DESIGN_SCREEN_HEIGHT_INCHES = 2.895118f;
	static float DESIGN_SCREEN_WIDTH_INCHES = 6.14488f;
	static float DESIGN_SCREEN_HEIGHT_INCHES = 3.70079f;
	//Define a minimum and maximum screen resolution (to prevent cramped or overlapping screen elements).
	static float MIN_WIDTH_PIXELS = 320f, MIN_HEIGHT_PIXELS = 240f;
	static float MAX_WIDTH_PIXELS = 1600f, MAX_HEIGHT_PIXELS = 960f;
	
	// ====================================================
	//VARIABLES
	// ====================================================
	// These variables will be set in onCreateEngineOptions().
	public float cameraWidth;
	public float cameraHeight;
	public float actualScreenWidthInches;
	public float actualScreenHeightInches;
	public BoundCamera mCamera;
	
	
	
	//=============================================================
	// CREATE ENGINE OPTIONS
	//=============================================================
	
	
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		
		//Determine the devices' physical screen size
		actualScreenHeightInches = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().xdpi;
		
		actualScreenWidthInches = getResources().getDisplayMetrics().heightPixels / getResources().getDisplayMetrics().ydpi;
		Log.v("","screen width " + actualScreenWidthInches);
		Log.v("","screen height " + actualScreenHeightInches);
		//Set the Camera;s Width & Height according to the device with which you design the game.
		//cameraWidth = 600;
		cameraWidth = Math.round(Math.max(Math.min(DESIGN_SCREEN_WIDTH_PIXELS * (actualScreenWidthInches / DESIGN_SCREEN_WIDTH_INCHES), MAX_WIDTH_PIXELS), MIN_WIDTH_PIXELS));
		Log.v("","camera width " + cameraWidth);
		cameraHeight = Math.round(Math.max(Math.min(DESIGN_SCREEN_HEIGHT_PIXELS * (actualScreenHeightInches / DESIGN_SCREEN_HEIGHT_INCHES), MAX_HEIGHT_PIXELS), MIN_HEIGHT_PIXELS));
		//cameraHeight = 400;
		if (cameraHeight>cameraWidth){
			cameraHeight = cameraHeight*cameraWidth;
			cameraWidth = cameraHeight/cameraWidth;
			cameraHeight = cameraHeight/cameraWidth;
			
		}
		//cameraWidth = 1920;
		//cameraHeight = 1200;
		Log.v("","camera height " + cameraHeight);
		//Define engine options
		mCamera = new SmoothCamera(0, 0, cameraWidth, cameraHeight, 200,200,0);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), mCamera);
		mCamera.setBoundsEnabled(true);
		
		GameLevel.getInstance().gameCamera = mCamera;
		//enable music
		engineOptions.getAudioOptions().setNeedsMusic(true);
		//enable sound
		engineOptions.getAudioOptions().setNeedsSound(true);
		//turn on dithering
		engineOptions.getRenderOptions().setDithering(true);
		//Turn on multi-sampling to smooth the alias of hard-edge elements
		engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedMultiSampling(true);
		//wake_loc, ie don't turn off screen when game is being played
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		
		// Return the engineOptions object, passing it to the engine.
		return engineOptions;
	}

	//=====================================================
	// CREATE RESOURCES
	// ====================================================
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		// TODO Auto-generated method stub
		return new FixedStepEngine(pEngineOptions, 60);
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		// TODO Auto-generated method stub
		
		//RESOURCE MANAGER WILL LOAD/UNLAOD RESOURCES APPROPRIATELY
		ResourceManager.getInstance().setup(this.getEngine(), this.getApplicationContext(), cameraWidth, cameraHeight, cameraWidth/DESIGN_SCREEN_WIDTH_PIXELS, cameraHeight/DESIGN_SCREEN_HEIGHT_PIXELS);
		//called after game resources, textures, sounds, graphics are loaded (last method called)
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	//=====================================================
	// CREATE SCENE
	//=====================================================
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		// TODO Auto-generated method stub
		//Register an FPSLogger to output the game's FPS during development.
		mEngine.registerUpdateHandler(new FPSLogger());
		// Tell the SceneManager to show the MainMenu
		//UserData.getInstance().setHighScore(9);
		final UserData database = UserData.getInstance( this.getApplicationContext());
		//new GameManager();
		SceneManager.getInstance().showMainMenu();
		pOnCreateSceneCallback.onCreateSceneFinished(MainMenu.getInstance());
	}

	//method used to populate the scene once it has been passed to the engine
	//usually attach entities here and not in onCreateScene for organization
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	
	
	@Override
	public synchronized void onResumeGame() {
			super.onResumeGame();
		//mEngine.notifyAll();
		//mEngine.start();
		//mEngine.startUpdateThread();
		/* If the music and button have been created */
		if ((ResourceManager.getInstance().mMusic != null)){
			/* If the mMuteButton is set to unmuted on resume...*/
			if(MainMenu.getInstance().menuMute.getCurrentTileIndex() == ResourceManager.getInstance().UNMUTE){
					/* Play the menu music */
				//Log.v("something", Boolean.toString(ResourceManager.getInstance().mMusic.isReleased()));
				if (ResourceManager.getInstance().mMusic.isReleased()) {
					//ResourceManager.getInstance().mMusic.play();
				} else {
					ResourceManager.getInstance().mMusic.resume();
				}
				//Log.v("something", "resume menu music");
			}
		}
		/* If the music and button have been created */
		if (ResourceManager.getInstance().mMusiclvl1 != null && (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 1)){
			/* If the mMuteButton is set to unmuted on resume...*/
			//Log.v("something", Integer.toString(GameLevel.getInstance().mMuteButton.getTileCount()));
			//Log.v("something",Boolean.toString(GameLevel.getInstance().mPlayer.hasParent()));
			if(GameLevel.getInstance().mMuteButton.getCurrentTileIndex() == ResourceManager.getInstance().UNMUTE){
					/* Play the menu music */
				//Log.v("something", Boolean.toString(ResourceManager.getInstance().mMusiclvl1.isReleased()));
				
				if (ResourceManager.getInstance().mMusiclvl1.isReleased()) {
					//ResourceManager.getInstance().mMusiclvl1.stop();
				} else {
					ResourceManager.getInstance().mMusiclvl1.resume();
				}
				
				//Log.v("something", "resume game music");
				
			}
			
		}
		/* If the music and button have been created */
		if (ResourceManager.getInstance().mMusiclvl2 != null && (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 2)){
			/* If the mMuteButton is set to unmuted on resume...*/
			//Log.v("something", Integer.toString(GameLevel.getInstance().mMuteButton.getTileCount()));
			//Log.v("something",Boolean.toString(GameLevel.getInstance().mPlayer.hasParent()));
			if(GameLevel.getInstance().mMuteButton.getCurrentTileIndex() == ResourceManager.getInstance().UNMUTE){
					/* Play the menu music */
				//Log.v("something", Boolean.toString(ResourceManager.getInstance().mMusiclvl1.isReleased()));
				
				if (ResourceManager.getInstance().mMusiclvl2.isReleased()) {
					//ResourceManager.getInstance().mMusiclvl1.stop();
				} else {
					ResourceManager.getInstance().mMusiclvl2.resume();
				}
				
				//Log.v("something", "resume game music");
				
			}
		}
		/* If the music and button have been created */
		if (ResourceManager.getInstance().mMusiclvl3 != null && (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 3)){
			/* If the mMuteButton is set to unmuted on resume...*/
			//Log.v("something", Integer.toString(GameLevel.getInstance().mMuteButton.getTileCount()));
			//Log.v("something",Boolean.toString(GameLevel.getInstance().mPlayer.hasParent()));
			if(GameLevel.getInstance().mMuteButton.getCurrentTileIndex() == ResourceManager.getInstance().UNMUTE){
					/* Play the menu music */
				//Log.v("something", Boolean.toString(ResourceManager.getInstance().mMusiclvl1.isReleased()));
				
				if (ResourceManager.getInstance().mMusiclvl3.isReleased()) {
					//ResourceManager.getInstance().mMusiclvl1.stop();
				} else {
					ResourceManager.getInstance().mMusiclvl3.resume();
				}
				
				//Log.v("something", "resume game music");
				
			}
		}
		/* If the music and button have been created */
		if (ResourceManager.getInstance().mMusiclvl4 != null && (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 4)){
			/* If the mMuteButton is set to unmuted on resume...*/
			//Log.v("something", Integer.toString(GameLevel.getInstance().mMuteButton.getTileCount()));
			//Log.v("something",Boolean.toString(GameLevel.getInstance().mPlayer.hasParent()));
			if(GameLevel.getInstance().mMuteButton.getCurrentTileIndex() == ResourceManager.getInstance().UNMUTE){
					/* Play the menu music */
				//Log.v("something", Boolean.toString(ResourceManager.getInstance().mMusiclvl1.isReleased()));
				
				if (ResourceManager.getInstance().mMusiclvl4.isReleased()) {
					//ResourceManager.getInstance().mMusiclvl1.stop();
				} else {
					ResourceManager.getInstance().mMusiclvl4.resume();
				}
				
				//Log.v("something", "resume game music");
				
			}
		}
		/* If the music and button have been created */
		if (ResourceManager.getInstance().mMusiclvl5 != null && (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 5)){
			/* If the mMuteButton is set to unmuted on resume...*/
			//Log.v("something", Integer.toString(GameLevel.getInstance().mMuteButton.getTileCount()));
			//Log.v("something",Boolean.toString(GameLevel.getInstance().mPlayer.hasParent()));
			if(GameLevel.getInstance().mMuteButton.getCurrentTileIndex() == ResourceManager.getInstance().UNMUTE){
					/* Play the menu music */
				//Log.v("something", Boolean.toString(ResourceManager.getInstance().mMusiclvl1.isReleased()));
				
				if (ResourceManager.getInstance().mMusiclvl5.isReleased()) {
					//ResourceManager.getInstance().mMusiclvl1.stop();
				} else {
					ResourceManager.getInstance().mMusiclvl5.resume();
				}
				
				//Log.v("something", "resume game music");
				
			}
		}
		//Debug.d(this.getClass().getSimpleName() + ".onResumeGame" + " @(Thread: '" + Thread.currentThread().getName() + "')");
	}
	
	@Override
	public synchronized void onPauseGame() {
		super.onPauseGame();
		//mEngine.stop();
		
		/*Always pause the music on pause */
		if((!ResourceManager.getInstance().mMusic.isReleased()) && ResourceManager.getInstance().mMusic != null && ResourceManager.getInstance().mMusic.isPlaying()){
			//Log.v("something", Boolean.toString(ResourceManager.getInstance().mMusic.isReleased()));
			ResourceManager.getInstance().mMusic.pause();
			//Log.v("something", "pause menu music");
			//Log.v("something", Integer.toString(MainMenu.getInstance().menuMute.getTileCount()));
			//ResourceManager.getInstance().mMusic.
		}
		
		if (SceneManager.getInstance().mCurrentScene.getClass().getGenericSuperclass().equals(ManagedGameScene.class)){
		/*Always pause the music on pause */
		if((!ResourceManager.getInstance().mMusiclvl1.isReleased()) && ResourceManager.getInstance().mMusiclvl1 != null && ResourceManager.getInstance().mMusiclvl1.isPlaying() && (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 1)){
			//Log.v("something", Boolean.toString(ResourceManager.getInstance().mMusiclvl1.isReleased()));
			ResourceManager.getInstance().mMusiclvl1.pause();
			//GameLevel.getInstance().pauseGame();
			//Log.v("something", "pause game music");
			//Log.v("something", Integer.toString(GameLevel.getInstance().mMuteButton.getTileCount()));
		}
		/*Always pause the music on pause */
		if((!ResourceManager.getInstance().mMusiclvl2.isReleased()) && ResourceManager.getInstance().mMusiclvl2 != null && ResourceManager.getInstance().mMusiclvl2.isPlaying() && (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 2)){
			//Log.v("something", Boolean.toString(ResourceManager.getInstance().mMusiclvl1.isReleased()));
			ResourceManager.getInstance().mMusiclvl2.pause();
			//GameLevel.getInstance().pauseGame();
			//Log.v("something", "pause game music");
			//Log.v("something", Integer.toString(GameLevel.getInstance().mMuteButton.getTileCount()));
		}
		/*Always pause the music on pause */
		if((!ResourceManager.getInstance().mMusiclvl3.isReleased()) && ResourceManager.getInstance().mMusiclvl3 != null && ResourceManager.getInstance().mMusiclvl3.isPlaying() && (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 3)){
			//Log.v("something", Boolean.toString(ResourceManager.getInstance().mMusiclvl1.isReleased()));
			ResourceManager.getInstance().mMusiclvl3.pause();
			//GameLevel.getInstance().pauseGame();
			//Log.v("something", "pause game music");
			//Log.v("something", Integer.toString(GameLevel.getInstance().mMuteButton.getTileCount()));
		}
		/*Always pause the music on pause */
		if((!ResourceManager.getInstance().mMusiclvl4.isReleased()) && ResourceManager.getInstance().mMusiclvl4 != null && ResourceManager.getInstance().mMusiclvl4.isPlaying() && (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 4)){
			//Log.v("something", Boolean.toString(ResourceManager.getInstance().mMusiclvl1.isReleased()));
			ResourceManager.getInstance().mMusiclvl4.pause();
			//GameLevel.getInstance().pauseGame();
			//Log.v("something", "pause game music");
			//Log.v("something", Integer.toString(GameLevel.getInstance().mMuteButton.getTileCount()));
		}
		/*Always pause the music on pause */
		if((!ResourceManager.getInstance().mMusiclvl5.isReleased()) && ResourceManager.getInstance().mMusiclvl5 != null && ResourceManager.getInstance().mMusiclvl5.isPlaying() && (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 5)){
			//Log.v("something", Boolean.toString(ResourceManager.getInstance().mMusiclvl1.isReleased()));
			ResourceManager.getInstance().mMusiclvl5.pause();
			
			//Log.v("something", "pause game music");
			//Log.v("something", Integer.toString(GameLevel.getInstance().mMuteButton.getTileCount()));
		}
		//ResourceManager.getInstance().engine.stop();
		}
		//mEngine.setScene(scene); 
		UserData.getInstance(ResourceManager.getInstance().context).pauseGame(false);
	}
	
	// If a Layer is open when the Back button is pressed, hide the layer.
	// If a Game scene or non-MainMenu is active, go back to the MainMenu.
	// Otherwise, exit the game.
	
	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) 
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if(ResourceManager.getInstance().engine!=null){
				if(SceneManager.getInstance().isLayerShown)
					SceneManager.getInstance().currentLayer.onHideLayer();
				else if(SceneManager.getInstance().mCurrentScene.getClass().getGenericSuperclass().equals(ManagedGameScene.class) || 
						(SceneManager.getInstance().mCurrentScene.getClass().getGenericSuperclass().equals(ManagedMenuScene.class) &!
							SceneManager.getInstance().mCurrentScene.getClass().equals(MainMenu.class))){
					
					//MainMenu.getInstance().detachSelf();
					//new MainMenu();
					Timer t = new Timer();
					t.schedule(new TimerTask() {
						
						@Override
						public void run() {

							ResourceManager.getInstance().engine.unregisterUpdateHandler(GameLevel.getInstance().bulletTimerHandler);
							//GameLevel.getInstance().hud.detachSelf();
							
							SceneManager.getInstance().showMainMenu();
							mCamera.setBounds(0,0,cameraWidth,cameraHeight);
						}
					}, 1000);
					
				}
					//Log.v("log here", "Should show Main Menu");}
				else
					System.exit(0);
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
		
	}
	
	public void restart(int delay) {
	    PendingIntent intent = PendingIntent.getActivity(this.getBaseContext(), 0, new Intent(getIntent()), getIntent().getFlags());
	    AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	    manager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, intent);
	    System.exit(2);
	}
			

}
