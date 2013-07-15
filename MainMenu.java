package mkat.apps.spacewars;

import java.util.Timer;
import java.util.TimerTask;

import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import android.database.Cursor;
import android.util.Log;

public class MainMenu extends ManagedMenuScene{
	
	private static final MainMenu INSTANCE = new MainMenu();
	public static MainMenu getInstance(){
		return INSTANCE;
	}
	
	public MainMenu() {
		this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionMoveEnabled(true);
	}
	
	
	Sprite menuBGD, title, creditsLayer;
	ButtonSprite creditsSprite;
	ButtonSprite newGameSprite;
	ButtonSprite resumeSprite;
	TiledSprite menuMute;
	Text highScore;
	CameraScene mCreditsScene;
	//self reference this class (GameLevel)
	public ManagedMenuScene thisMenu = this;
	
	public void onLoadScene(){
		super.onLoadScene();
	//SETUP MENU BACKGROUND
		//alreadyLoaded = true;
		menuBGD = new Sprite(ResourceManager.getInstance().cameraWidth /2f, ResourceManager.getInstance().cameraHeight /2f, ResourceManager.getInstance().mMenuBackgroundTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		
		//menuBGD.setScaleX(ResourceManager.getInstance().cameraWidth);
		//menuBGD.setScaleY(ResourceManager.getInstance().cameraHeight/480f);
		//menuBGD.setZIndex(-5000);
	//END SETUP MENU BACKGROUND
		
	//SETUP MENU BUTTONS
			/* Create the buttonSprite object in the center of the Scene */
			newGameSprite = new ButtonSprite((ResourceManager.getInstance().cameraWidth * 0.5f)-(ResourceManager.getInstance().cameraWidth*0.2875f), (ResourceManager.getInstance().cameraHeight * 0.5f)+(ResourceManager.getInstance().cameraWidth*0.075f), ResourceManager.getInstance().mMenuBackgroundNewGame, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			newGameSprite.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY){
					//GameLevel.getInstance().clearUpdateHandlers();
					//Log.v("","cleared Update Handlers of previous GameLevel instance");
					//GameLevel.getInstance().detachSelf();
					//Log.v("","detached self of GameLevel instance");
					//GameLevel.getInstance().dispose();
					//Log.v("","dispose old GameLevel instance");
					new GameLevel();
					//Create a new GameLevel and show it using the SceneManager. And play a click.
					UserData.getInstance(ResourceManager.getInstance().context).setLastLevelUnLocked(1,1);
					SceneManager.getInstance().showScene(GameLevel.getInstance()); 
						//GameLevel.getInstance().duration = 30;
					ResourceManager.getInstance().mSoundMenu.play();
					if (ResourceManager.getInstance().mMusic.isPlaying()) {
						/*
						 * If music is playing, pause it and set tile index to MUTE
						 */
						menuMute.setCurrentTileIndex(ResourceManager.getInstance().MUTE);
						ResourceManager.getInstance().mMusic.pause();
					} 
				}});
			Log.v("","new button created no errors");
			resumeSprite = new ButtonSprite((ResourceManager.getInstance().cameraWidth * 0.5f)-(ResourceManager.getInstance().cameraWidth*0.275f), (ResourceManager.getInstance().cameraHeight * 0.5f)+(ResourceManager.getInstance().cameraWidth*0.03125f)-newGameSprite.getHeight(), ResourceManager.getInstance().mMenuBackgroundResume, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			resumeSprite.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY){
				//Resume a game level that the user was in last, check UserData.
					//UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1);
					//SceneManager.getInstance().showScene(GameLevel.getInstance()); 
					UserData.getInstance(ResourceManager.getInstance().context).setLastLevelUnLocked(1, UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1));
					SceneManager.getInstance().showScene(GameLevel.getInstance()); 
					
					ResourceManager.getInstance().mSoundMenu.play();
					if (ResourceManager.getInstance().mMusic.isPlaying()) {
						/*
						 * If music is playing, pause it and set tile index to MUTE
						 */
						menuMute.setCurrentTileIndex(ResourceManager.getInstance().MUTE);
						ResourceManager.getInstance().mMusic.pause();
					} 
			}});
			Log.v("","resume button created no errors");
			creditsSprite = new ButtonSprite((ResourceManager.getInstance().cameraWidth * 0.5f)-(ResourceManager.getInstance().cameraWidth*0.26f), (ResourceManager.getInstance().cameraHeight * 0.5f)-(ResourceManager.getInstance().cameraWidth*0.01375f)-resumeSprite.getHeight(), ResourceManager.getInstance().mMenuBackgroundCredits, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			creditsSprite.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY){
				//This actions should act as the credits layer
					//SceneManager.getInstance().showCreditsLayer(false);
					creditsLayer = new Sprite(ResourceManager.getInstance().cameraWidth/3, ResourceManager.getInstance().cameraHeight - ResourceManager.getInstance().mCreditsLayer.getHeight(), ResourceManager.getInstance().mCreditsLayer, ResourceManager.getInstance().engine.getVertexBufferObjectManager()) {
						
						//Override the onAreaTouched() method allowing us to define custom actions
						 
						@Override
						public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
							/* IN the event the mute button is pressed down on... */
							if (pSceneTouchEvent.isActionDown()){
								unShowCredits();
								return true;
							}
							return super.onAreaTouched(pSceneTouchEvent,  pTouchAreaLocalX, pTouchAreaLocalY);
						}
						};
						mCreditsScene = new CameraScene(ResourceManager.getInstance().engine.getCamera());

						creditsLayer.setPosition(ResourceManager.getInstance().cameraWidth/2, ResourceManager.getInstance().cameraHeight/2);
						
						mCreditsScene.registerTouchArea(creditsLayer);
						mCreditsScene.attachChild(creditsLayer);
						mCreditsScene.setBackgroundEnabled(false);
						showCredits();
						ResourceManager.getInstance().mSoundMenu.play();
						if (ResourceManager.getInstance().mMusic.isPlaying()) {
							/*
							 * If music is playing, pause it and set tile index to MUTE
							 */
							menuMute.setCurrentTileIndex(ResourceManager.getInstance().MUTE);
							ResourceManager.getInstance().mMusic.pause();
						} 
					//ResourceManager.getInstance().mSoundMenu.play();
			}});	
			//END SETUP MENU BUTTONS
			Log.v("","credits button created no errors");
			//MUTE UNMUTE BUTTON SETUP
			title = new Sprite((float)(newGameSprite.getWidth()-(ResourceManager.getInstance().cameraWidth*0.046875)), newGameSprite.getY()+ResourceManager.getInstance().mTitle.getHeight(), ResourceManager.getInstance().mTitle, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			Log.v("","title created no errors");
			/* Create the music mute/unmute button */
			menuMute = new TiledSprite(ResourceManager.getInstance().cameraWidth-(ResourceManager.getInstance().mMenuBackgroundSoundM.getWidth()/2), ResourceManager.getInstance().cameraHeight-(ResourceManager.getInstance().mMenuBackgroundSoundM.getHeight()/2), ResourceManager.getInstance().mMenuBackgroundSoundM, ResourceManager.getInstance().engine.getVertexBufferObjectManager()) {
				
					/*
					 * Override the onAreaTouched() method allowing us to define custom actions
					 */
					@Override
					public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
						/* IN the event the mute button is pressed down on... */
						if (pSceneTouchEvent.isActionDown()){
							if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==1) {
								/*
								 * If music is playing, pause it and set tile index to MUTE
								 */
								this.setCurrentTileIndex(ResourceManager.getInstance().MUTE);
								ResourceManager.getInstance().mMusic.pause();
								UserData.getInstance(ResourceManager.getInstance().context).setMusic(1, 0);
								Log.v("","turned off music permanently: " + UserData.getInstance(ResourceManager.getInstance().context).getMusic(1));
							} else if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==0){
								/*
								 * If music is paused, play it and set tile index to
								 * UNMUTE
								 */
								
								this.setCurrentTileIndex(ResourceManager.getInstance().UNMUTE);
								UserData.getInstance(ResourceManager.getInstance().context).setMusic(1, 1);
								Log.v("","turned on music permanently: " + UserData.getInstance(ResourceManager.getInstance().context).getMusic(1));
								ResourceManager.getInstance().mMusic.play();
							}
							return true;
						}
						return super.onAreaTouched(pSceneTouchEvent,  pTouchAreaLocalX, pTouchAreaLocalY);
					}
			};
			Log.v("","mute button created no errors");
			/* Set teh current tile index to unmuted on application startup*/
			if ((UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==1)){
				menuMute.setCurrentTileIndex(ResourceManager.getInstance().UNMUTE); 
			
				/* Set the mMenuMusic object to loop once it reaches the track's end */
				if (!ResourceManager.getInstance().mMusic.isReleased()){
					ResourceManager.getInstance().mMusic.setLooping(true);
					ResourceManager.getInstance().mMusic.play();
				}
			} else if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==0) {
				menuMute.setCurrentTileIndex(ResourceManager.getInstance().MUTE); 
			}
			//END MUTE UNMUTE BUTTON SETUP
			// Create the CreditsLayerTitle text for the Layer.
			
			/*
			 *getting data from info...
			*/
			//info.close();
			highScore = new Text(ResourceManager.getInstance().cameraWidth/2,(ResourceManager.getInstance().mMenuBackgroundSoundM.getHeight()),ResourceManager.getInstance().mFontM,"High Score: " +  UserData.getInstance(ResourceManager.getInstance().context).getHighScore(1),ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			//highScore.setPosition(170f,50f);
			highScore.setColor(0f, 175, 255);
	//END SHOW HIGH SCORE
			Log.v("","high score created no errors");
			
			/* Register the buttonSprite as a 'touchable' entry */
			
			thisMenu.registerTouchArea(menuMute);
			
			if (!menuBGD.hasParent()){ thisMenu.attachChild(menuBGD);}
			if (!title.hasParent()){ thisMenu.attachChild(title);}
			if (!menuMute.hasParent()){thisMenu.attachChild(menuMute);}
			if (!highScore.hasParent()){thisMenu.attachChild(highScore);}
			
			Timer t = new Timer();
			t.schedule(new TimerTask() {
				
				@Override
				public void run() {
					
					ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							Log.v("","TIMER TASK EXPIRES");
							thisMenu.registerTouchArea(newGameSprite);
							thisMenu.registerTouchArea(resumeSprite);
							thisMenu.registerTouchArea(creditsSprite);
							if (!newGameSprite.hasParent()){thisMenu.attachChild(newGameSprite);}
							if (!resumeSprite.hasParent()){thisMenu.attachChild(resumeSprite);}
							if (!creditsSprite.hasParent()){thisMenu.attachChild(creditsSprite);}
							
							
						}
					}); 
					
				}
			}, 5000);
		
			
			//Background
			//Sprite mSprite = new Sprite(positionX, positionY, ResourceManager.getInstance().mMenuBackgroundTextureRegion, mEngine.getVertexBufferObjectManager())
			// TODO Auto-generated method stub
			GameLevel.getInstance().gameCamera.setChaseEntity(menuBGD);
			Log.v("LOAD MENU SCENE", "LOAD MENU SCENE");
	}


	@Override
	public void onShowScene() {
		//SHOW HIGH SCORE
		super.onShowScene();
		Log.v("SHOW MENU SCENE", "SHOW MENU SCENE");
		/* Set teh current tile index to unmuted on application startup*/
		if ((UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==1)){
			menuMute.setCurrentTileIndex(ResourceManager.getInstance().UNMUTE); 
		
			/* Set the mMenuMusic object to loop once it reaches the track's end */
			if (!ResourceManager.getInstance().mMusic.isReleased()){
				ResourceManager.getInstance().mMusic.setLooping(true);
				ResourceManager.getInstance().mMusic.play();
			}
		} else if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==0) {
			menuMute.setCurrentTileIndex(ResourceManager.getInstance().MUTE); 
		}
	}

	public void showCredits() {
		thisMenu.setChildScene(mCreditsScene, false, true, true);
		//ResourceManager.getInstance().engine.stop();
	}
	
	public void unShowCredits() {
		thisMenu.clearChildScene();
		//ResourceManager.getInstance().engine.start();
	}
	
	@Override
	public void onHideScene() {
		super.onHideScene();
		// TODO Auto-generated method stub
		Log.v("HIDE MENU SCENE", "HIDE MENU SCENE");
		//thisManagedMenuScene.detachChildren();
		//Log.v("","this is how many children GameLevel has after detach children is run: " + thisManagedGameScene.getChildCount());
		//thisManagedMenuScene.clearEntityModifiers();
		//thisManagedMenuScene.clearTouchAreas();
		//thisManagedMenuScene.clearUpdateHandlers();
		//thisManagedMenuScene.detachSelf();
	}

	@Override
	public Scene onLoadingScreenLoadandShown() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		// TODO Auto-generated method stub
		
	}
}
