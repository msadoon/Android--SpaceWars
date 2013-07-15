package mkat.apps.spacewars;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import android.util.Log;

public class ManagedGameScene extends ManagedScene{
	// Create an easy to manage HUD that we can attach/detach when the game scene is shown or hidden.
	//public HUD GameHud = new HUD();
	public ManagedGameScene thisManagedGameScene = this;
	
	//HUD Score
	private static final String SCORE_STRING_PREFIX = "Score: ";
	private static final String SCORE_FORMAT = "0000";
	private static final int MAX_CHARACTER_COUNT = SCORE_STRING_PREFIX.length() + SCORE_FORMAT.length();
	//Text mScoreText = null;
	Sprite[] lifeBars = null;
	
	public ManagedGameScene() {
		//Let the Scene Manager know that we want to show a Loading Scene for at least 2 seconds.
		this(2f);
	};
	
	public ManagedGameScene(float pLoadingScreenMinimumSecondsShown) {
		super(pLoadingScreenMinimumSecondsShown);

		// Setup the touch attributes for the Game Scenes.
		//this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		//this.setTouchAreaBindingOnActionDownEnabled(true);
		//this.setTouchAreaBindingOnActionMoveEnabled(true);
		// Scale the Game Scenes according to the Camera's scale factor
		//this.setScale(ResourceManager.getInstance().cameraScaleFactorX, ResourceManager.getInstance().cameraScaleFactorY);
		//this.setPosition(0, ResourceManager.getInstance().cameraHeight/2f);
		//GameHud.setScaleCenter(0f, 0f);
		//GameHud.setScale(ResourceManager.getInstance().cameraScaleFactorX, ResourceManager.getInstance().cameraScaleFactorY);
	}

	//These objects will make up our loading scene.
	private Text LoadingText;
	private Scene LoadingScene;
	@Override
	public Scene onLoadingScreenLoadandShown() {
		// TODO Auto-generated method stub
		//Load the game font
		ResourceManager.getInstance().loadGameFont(ResourceManager.getInstance().engine, ResourceManager.getInstance().context);
		LoadingScene = new Scene();
		LoadingScene.setBackgroundEnabled(true);
		LoadingText = new Text(0,0,ResourceManager.getInstance().mFont, "Loading Game Resources", ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		LoadingText.setPosition(LoadingText.getWidth()/2f, ResourceManager.getInstance().cameraHeight-LoadingText.getHeight()/2f);
		LoadingScene.attachChild(LoadingText);
		return LoadingScene;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		// TODO Auto-generated method stub
		//detach the loading screen resources.
		ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				LoadingText.detachSelf();
				LoadingText = null;
				LoadingScene = null;
			}
		});
		
	}

	@Override
	public void onLoadScene() {
		// TODO Auto-generated method stub
		Log.v("Is layer shown", "Load game resources" + ResourceManager.getInstance().counter2);
		//Load level 1 resources
		ResourceManager.getInstance().loadLevel1Textures(ResourceManager.getInstance().engine, ResourceManager.getInstance().context);
		ResourceManager.getInstance().loadGameFont(ResourceManager.getInstance().engine, ResourceManager.getInstance().context);
		//Load the level sounds
		ResourceManager.getInstance().loadGameSounds(ResourceManager.getInstance().engine, ResourceManager.getInstance().context);
		ResourceManager.getInstance().loadlvl1Music(ResourceManager.getInstance().engine, ResourceManager.getInstance().context);
		ResourceManager.getInstance().loadlvl2Music(ResourceManager.getInstance().engine, ResourceManager.getInstance().context);
		ResourceManager.getInstance().loadlvl3Music(ResourceManager.getInstance().engine, ResourceManager.getInstance().context);
		ResourceManager.getInstance().loadlvl4Music(ResourceManager.getInstance().engine, ResourceManager.getInstance().context);
		ResourceManager.getInstance().loadlvl5Music(ResourceManager.getInstance().engine, ResourceManager.getInstance().context);
		ResourceManager.getInstance().loadHUDTextures(ResourceManager.getInstance().engine, ResourceManager.getInstance().context);
		ResourceManager.getInstance().loadMuteButton(ResourceManager.getInstance().engine,ResourceManager.getInstance().context);
			//what happens if the music is not loaded, can it still be accessed through the Resource Manager singleton? See if the Music On/Off button works, if yes, then what is the point of loading the music in ManagedGameScene
		Log.v("Is layer shown", "Load game resources done" + ResourceManager.getInstance().counter2);
		
		//Setup the HUD Score
		ResourceManager.getInstance().mFont.prepareLetters("SCORE: 1234567890".toCharArray());
		
		GameManager.getInstance().resetGame();
		GameManager.getInstance().GMDone(true);
		
		
		//mScoreText.setColor(0, 175, 255);
		//GameHud.attachChild(mScoreText);
		//GameHud.setColor(Color.RED);
		//GameHud.setX(250);
		//GameHud.setY(250);
				
	}

	@Override
	public void onShowScene() {
		// TODO Auto-generated method stub
		//ResourceManager.getInstance().engine.getCamera().setHUD(GameHud);
		//this makes sure the laser doesn't get fired before the scene is visible to the player, also helps recycle the laser one for one.
		
		
	}

	@Override
	public void onHideScene() {
		// TODO Auto-generated method stub
		//runnables.get(i).run();
		//runnables.remove(i).run();
		
				
				GameLevel.getInstance().hud.detachChild(GameLevel.getInstance().mScoreText);
				Log.v("","Removing score!");
				for (int i = 0;i<GameManager.getInstance().getLife();i++){
					GameLevel.getInstance().hud.detachChild(GameLevel.getInstance().lifeBars[i]);
					Log.v("","Removing Life!");
				}
				GameLevel.getInstance().hud.detachChild(GameLevel.getInstance().pause);
				Log.v("","Removing pause!");
				GameLevel.getInstance().hud.detachChild(GameLevel.getInstance().mMuteButton);
				Log.v("","removing mute button!");
				GameLevel.getInstance().hud.unregisterTouchArea(GameLevel.getInstance().mMuteButton);
				Log.v("","removing mute button register area!");
				//MUTE UNMUTE BUTTON SETUP
				GameLevel.getInstance().hud.detachChildren();
				//GameLevel.getInstance().hud.detachSelf();
			
		ResourceManager.getInstance().engine.clearDrawHandlers();
		//ResourceManager.getInstance().engine.getCamera().setHUD(null);
		ResourceManager.getInstance().engine.clearUpdateHandlers();
		Log.v("","Game Scene hidden!");
		//this makes sure the laser doesn't get fired before the scene is visible to the player, also helps recycle the laser one for one.
		GameLevel.getInstance().readyToFireLaser = false;
		GameLevel.getInstance().readyToFireEnemyLaser = false;
		
		
	}

	@Override
	public void onUnloadScene() {
		// TODO Auto-generated method stub
		ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				GameLevel.getInstance().mPlayer.detachSelf();
				//GameLevel.getInstance().mMuteButton.detachSelf();
				//if (GameLevel.getInstance().PlayerLaser != null) {
					//GameLevel.getInstance().PlayerLaser.dispose();
				//}
				//for (int i = 0; i<GameLevel.getInstance().totalEnemiesInPool; i++){
					//if (GameLevel.getInstance().mEnemyWave[i].){ 
						//GameLevel.getInstance().mEnemyWave[i].dispose();
					//}
				//}
				GameLevel.getInstance().pCurrentEnemyTexture = null;
				GameLevel.getInstance().pCurrentPlayerTexture = null; 
				GameLevel.getInstance().pPlayerLaserTexture = null; 
				GameLevel.getInstance().pEnemyLaserTexture = null;
				
				GameLevel.getInstance().enemyWave = null;
				//GameLevel.getInstance().pointer = null;
				GameLevel.getInstance().moveEnemy1 = null;
				GameLevel.getInstance().entityModifierListener = null;

				
				Log.v("","this is how many children GameLevel has before detach children is run: " + thisManagedGameScene.getChildCount());
				thisManagedGameScene.detachChildren();
				Log.v("","this is how many children GameLevel has after detach children is run: " + thisManagedGameScene.getChildCount());
				thisManagedGameScene.clearEntityModifiers();
				thisManagedGameScene.clearTouchAreas();
				thisManagedGameScene.clearUpdateHandlers();
				//thisManagedGameScene.detachChild(GameHud);
				thisManagedGameScene.detachSelf();
				for (int i = 0; i<GameLevel.getInstance().totalEnemiesInPool; i++){
					GameLevel.getInstance().mEnemyWave[i] = null;
					GameLevel.getInstance().pointer[i] = null;
				}
				
				//GameLevel.getInstance().dispose();
				Log.v("Is layer shown", "Unload game resources start" + ResourceManager.getInstance().counter2);
				ResourceManager.getInstance().unloadLevel1Textures();
				ResourceManager.getInstance().unloadGameSounds();
				ResourceManager.getInstance().unloadGameFont();
				ResourceManager.getInstance().unloadlvl1Music();
				ResourceManager.getInstance().unloadlvl2Music();
				ResourceManager.getInstance().unloadlvl3Music();
				ResourceManager.getInstance().unloadlvl4Music();
				ResourceManager.getInstance().unloadlvl5Music();
				ResourceManager.getInstance().unloadHUDTextures();
				ResourceManager.getInstance().unloadMuteButton();
				Log.v("Is layer shown", "Unload game resources end" + ResourceManager.getInstance().counter2);
				//Remember to unload level music as well..first figure out why it needs to be loaded
				Log.v("","Game Scene unloaded!");
			}
		});
	}
	
}
