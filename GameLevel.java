package mkat.apps.spacewars;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.music.exception.MusicReleasedException;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;

import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

import android.opengl.GLES20;
import android.util.Log;

public class GameLevel extends ManagedGameScene implements IOnSceneTouchListener{
	//self reference this class (GameLevel)

	private static final GameLevel INSTANCE = new GameLevel();
	public static GameLevel getInstance(){
		return INSTANCE;
	}
	public GameLevel() {
		this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionMoveEnabled(true);
		this.setOnSceneTouchListener(this);
	}
	
	//Global Variables
	TimerHandler bulletTimerHandler;
	BoundCamera gameCamera;
	Text mScoreText;
	Music mMusic;
	boolean readyToFireLaser, fireRate, fireRateAttached, health, healthAttached, firePower, firePowerAttached, readyToFireEnemyLaser, playerHit, playerHitBoss = false;
	Player mPlayer = null;
	Boss mBoss1 = null;
	TiledSprite mMuteButton = null;
	Sprite waveNum, wav1, wav2, wav3, wav4, wav5, pause, lvl1Num, lvl2Num, lvl3Num, lvl4Num, lvl5Num, play, GameOver, levelNum, fireRatePickup, firePowerPickup, healthPickup, background, back1, back2, back3, back4, back5 = null;
	ITextureRegion[] eTex;
	PlayerLaser PlayerLaser  = null;
	Enemy[] mEnemyWave;
	Pointer[] pointer;
	BossPointer[] bossPointer;
	PlusPointer[] pointerPlus;
	Line plusPointerHealthLine, plusPointerPowerLine, plusPointerRateLine, bossPointerLine;
	ITextureRegion bLaser1, boss1tex, boss2tex, boss3tex, boss4tex, boss5tex, bLaser2, pCurrentEnemyTexture, pGCTexture, pEnvFlames, pCurrentPlayerTexture, pPlayerLaserTexture, pPlayerLaserTexture2, pEnemyLaserTexture, pBossLaserTexture, pLvl1BossTexture, pFireRateTexture, pFirePowerTexture, pHealthTexture = null;// in case we need to allocate a new enemy from the pool after our pool is exhausted, we want the texture to be of the current enemy type (of the 5 possible).
	EnemyPool enemyWave;
	PointerPool pointerWave;
	PlusPointerPool plusPointerWave;
	BossPointerPool bossPointerWave;
	Rectangle boundBox; //level 5 boss bounding box
	float touchX;
	float touchY;
	float dToPlayer = 1000;
	float dToPX;
	float dToPY;
	float fireRateDuration = 0;
	float laserRotationAngle = 0;
	float enemyX, enemyY, enemyX2, enemyY2 = 0; //used for laser fire
	boolean PlayerReady, BossReady, LevelLoaded = false;
	/*Move modifier variables
		/*Define Move Modifiers*/
	int counter, waveCounter, gameLevel, totalEnemiesInPool, enemyCount, Level = 0;
	float duration = 0;
	float distance;
	float fromX;
	float toX;
	float fromY;
	float toY;
	float rotateX;
	float rotateY;
	float directionX;
	float directionY;
	float rotationAngle;
	float fireRateEnemy, fireRateEnemyStart;
	float widthX, heightY;
	Timer t;
	HUD hud;
	AnimatedSprite animatedSprite; 
	int playerLife, bossLife = 0;
	public CameraScene mPauseScene;
	MoveModifier moveEnemy1, moveBoss1;
	IEntityModifierListener entityModifierListener;
	Sprite[] lifeBars;
	//level5 boss
	BatchedSpriteParticleSystem particleSystem;
	CircleParticleEmitter particleEmitter;
	
	//HUD Score
		private static final String SCORE_STRING_PREFIX = "Score: ";
		private static final String SCORE_FORMAT = "0000";
		private static final int MAX_CHARACTER_COUNT = SCORE_STRING_PREFIX.length() + SCORE_FORMAT.length();
	//Text mScoreText = null;
	
	//Physics Collision Detection
			//public FixedStepPhysicsWorld mPhysicsWorld;
			//public Body[] dynamicEnemyBody;
			//final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(20f, 0f, 0.5f);
			//Body[] enemyBody;
	//public boolean setHalfAlphaForStaticBody = false;
	//enemy-on-enemy collisions
	MoveModifier avoidCollideOtherEnemies;
	//float duration = 0;
	IEntityModifierListener entityListener;
	
	public ManagedGameScene thisGameLevel = this;
			
	
	public void loadLevel(int i) {
		if (i == 1){
		//THIS IS THE STUFF THAT CHANGES PER LEVEL
			bossLife = 20; //per level
			fireRateEnemy = 1f;
			fireRateDuration = .5f;
			fireRateEnemyStart = 5f;
			waveCounter = 0; //per level
			
			enemyCount = 0;
			healthAttached = false; //should start as false every level
			firePowerAttached = false; //should start as false every level
			fireRateAttached = false; //should start as false every level
			readyToFireLaser = false;
			readyToFireEnemyLaser = false;
			
				//BOSS TEXTURE 
				pLvl1BossTexture = boss1tex;
				//pLvl1BossTexture = ResourceManager.getInstance().mBoss1TextureRegion;
				//END BOSS TEXTURE
				if (background != null && background.hasParent()){
					background.detachSelf();
				}
				
				background = back1;
				background.setCullingEnabled(true);
				//SETUP BACKGROUND
				thisGameLevel.attachChild(background);
				//END SETUP BACKGROUND
				//ResourceManager.getInstance().engine.getEngineOptions().getCamera().setBounds(1,1,1,1);
				//BOSS LASER TEXTURE
				pBossLaserTexture = bLaser1;
				//END BOSS LASER TEXTURE
				
				
				
				/*ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						for (int i3 = 0;i3<GameManager.getInstance().getLife();i3++){
							//lifeBarWidth += ResourceManager.getInstance().mPlayerLifeTextureRegion.getWidth();
							//lifeBars[i] = new Sprite((lifeBarWidth), ResourceManager.getInstance().cameraHeight - ResourceManager.getInstance().mPlayerLifeTextureRegion.getHeight(), ResourceManager.getInstance().mPlayerLifeTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
							if (GameLevel.getInstance().lifeBars[i3]!= null ){
								if (GameLevel.getInstance().lifeBars[i3].hasParent()){
									hud.detachChild(lifeBars[i3]);
								}
							}
						}
						GameManager.getInstance().zeroLife();
						while (GameManager.getInstance().getLife()<10){
							hud.attachChild(lifeBars[GameManager.getInstance().getLife()]);
							GameManager.getInstance().incrementLife(1);
						}
					}
				});*/
				
				
				//ATTACH TO SCENE 
				
				//END ATTACH TO SCENE
				//END THIS IS THE STUFF THAT CHANGES PER LEVEL
		} else if (i == 2){
				//THIS IS THE STUFF THAT CHANGES PER LEVEL
			bossLife = 12; //per level
			fireRateEnemy = 1f;
			fireRateEnemyStart = 3f;
			waveCounter = 0; //per level
			fireRateDuration = .5f;
			enemyCount = 0;
			healthAttached = false; //should start as false every level
			firePowerAttached = false; //should start as false every level
			fireRateAttached = false; //should start as false every level
			//give player full life
			readyToFireLaser = false;
			readyToFireEnemyLaser = false;
			
			//end give players all life
						//BOSS TEXTURE 
						pLvl1BossTexture = boss2tex;
						//pLvl1BossTexture = ResourceManager.getInstance().mBoss1TextureRegion2;
						//END BOSS TEXTURE
						
						//BOSS LASER TEXTURE
						pBossLaserTexture = bLaser2;
						//END BOSS LASER TEXTURE
						
						if (background != null && background.hasParent()){
							background.detachSelf();
						}
						//SETUP BACKGROUND
						background = back2;
						background.setCullingEnabled(true);
						//SETUP BACKGROUND
						thisGameLevel.attachChild(background);
						
						
						
							/*ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									for (int i3 = 0;i3<GameManager.getInstance().getLife();i3++){
										//lifeBarWidth += ResourceManager.getInstance().mPlayerLifeTextureRegion.getWidth();
										//lifeBars[i] = new Sprite((lifeBarWidth), ResourceManager.getInstance().cameraHeight - ResourceManager.getInstance().mPlayerLifeTextureRegion.getHeight(), ResourceManager.getInstance().mPlayerLifeTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
										if (GameLevel.getInstance().lifeBars[i3]!= null ){
											if (GameLevel.getInstance().lifeBars[i3].hasParent()){
												hud.detachChild(lifeBars[i3]);
											}
										}
									}
									GameManager.getInstance().zeroLife();
									while (GameManager.getInstance().getLife()<10){
										hud.attachChild(lifeBars[GameManager.getInstance().getLife()]);
										GameManager.getInstance().incrementLife(1);
									}
								}
							});*/
						//END THIS IS THE STUFF THAT CHANGES PER LEVEL
		}
		else if (i == 3){
			//THIS IS THE STUFF THAT CHANGES PER LEVEL
		bossLife = 20; //per level
		fireRateEnemy = 2f;
		fireRateDuration = .5f;
		fireRateEnemyStart = 2f;
		waveCounter = 0; //per level
		enemyCount = 0;
		healthAttached = false; //should start as false every level
		firePowerAttached = false; //should start as false every level
		fireRateAttached = false; //should start as false every level
		readyToFireLaser = false;
		readyToFireEnemyLaser = false;
		
					//BOSS TEXTURE 
					pLvl1BossTexture = boss3tex;
					//pLvl1BossTexture = ResourceManager.getInstance().mBoss1TextureRegion3;
					//END BOSS TEXTURE
					
					//BOSS LASER TEXTURE
					pBossLaserTexture = bLaser1;
					//END BOSS LASER TEXTURE
					
					if (background != null && background.hasParent()){
						background.detachSelf();
					}
					//SETUP BACKGROUND
					background = back3;
					background.setCullingEnabled(true);
					//SETUP BACKGROUND
					thisGameLevel.attachChild(background);
					
					
					/*ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							for (int i3 = 0;i3<GameManager.getInstance().getLife();i3++){
								//lifeBarWidth += ResourceManager.getInstance().mPlayerLifeTextureRegion.getWidth();
								//lifeBars[i] = new Sprite((lifeBarWidth), ResourceManager.getInstance().cameraHeight - ResourceManager.getInstance().mPlayerLifeTextureRegion.getHeight(), ResourceManager.getInstance().mPlayerLifeTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
								if (GameLevel.getInstance().lifeBars[i3]!= null ){
									if (GameLevel.getInstance().lifeBars[i3].hasParent()){
										hud.detachChild(lifeBars[i3]);
									}
								}
							}
							GameManager.getInstance().zeroLife();
							while (GameManager.getInstance().getLife()<10){
								hud.attachChild(lifeBars[GameManager.getInstance().getLife()]);
								GameManager.getInstance().incrementLife(1);
							}
						}
					});*/
					//END THIS IS THE STUFF THAT CHANGES PER LEVEL
	}
		else if (i == 4){
			//THIS IS THE STUFF THAT CHANGES PER LEVEL
		bossLife = 20; //per level
		fireRateEnemy = 1f;
		fireRateDuration = .5f;
		fireRateEnemyStart = 2f;
		waveCounter = 0; //per level
		enemyCount = 0;
		healthAttached = false; //should start as false every level
		firePowerAttached = false; //should start as false every level
		fireRateAttached = false; //should start as false every level
		readyToFireLaser = false;
		readyToFireEnemyLaser = false;
		
		
					//BOSS TEXTURE 
					pLvl1BossTexture = boss4tex;
					//pLvl1BossTexture = ResourceManager.getInstance().mBoss1TextureRegion4;
					//END BOSS TEXTURE
					if (background != null && background.hasParent()){
						background.detachSelf();
					}
					//BOSS LASER TEXTURE
					pBossLaserTexture = bLaser1;
					//END BOSS LASER TEXTURE
					//SETUP BACKGROUND
					background = back4;
					background.setCullingEnabled(true);
					//SETUP BACKGROUND
					thisGameLevel.attachChild(background);
					
					/*ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							for (int i3 = 0;i3<GameManager.getInstance().getLife();i3++){
								//lifeBarWidth += ResourceManager.getInstance().mPlayerLifeTextureRegion.getWidth();
								//lifeBars[i] = new Sprite((lifeBarWidth), ResourceManager.getInstance().cameraHeight - ResourceManager.getInstance().mPlayerLifeTextureRegion.getHeight(), ResourceManager.getInstance().mPlayerLifeTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
								if (GameLevel.getInstance().lifeBars[i3]!= null ){
									if (GameLevel.getInstance().lifeBars[i3].hasParent()){
										hud.detachChild(lifeBars[i3]);
									}
								}
							}
							GameManager.getInstance().zeroLife();
							while (GameManager.getInstance().getLife()<10){
								hud.attachChild(lifeBars[GameManager.getInstance().getLife()]);
								GameManager.getInstance().incrementLife(1);
							}
						}
					});*/
					//END THIS IS THE STUFF THAT CHANGES PER LEVEL
	}
		else if (i == 5){
			//THIS IS THE STUFF THAT CHANGES PER LEVEL
		bossLife = 25; //per level
		fireRateEnemy = 1f;
		fireRateDuration = .5f;
		fireRateEnemyStart = 2f;
		waveCounter = 0; //per level
		enemyCount = 0;
		healthAttached = false; //should start as false every level
		firePowerAttached = false; //should start as false every level
		fireRateAttached = false; //should start as false every level
		readyToFireLaser = false;
		readyToFireEnemyLaser = false;
		
		
					//BOSS TEXTURE 
					pLvl1BossTexture = boss5tex;
					//END BOSS TEXTURE
					//BOSS LASER TEXTURE
					pBossLaserTexture = bLaser1;
					//END BOSS LASER TEXTURE
					//BOSS LASER TEXTURE
					pEnvFlames = ResourceManager.getInstance().mEnvFlames;
					//END BOSS LASER TEXTURE
					if (background != null && background.hasParent()){
						background.detachSelf();
					}
					//SETUP BACKGROUND
					background = back5;
					background.setCullingEnabled(true);
					//SETUP BACKGROUND
					thisGameLevel.attachChild(background);
					
				
					/*ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							for (int i3 = 0;i3<GameManager.getInstance().getLife();i3++){
								//lifeBarWidth += ResourceManager.getInstance().mPlayerLifeTextureRegion.getWidth();
								//lifeBars[i] = new Sprite((lifeBarWidth), ResourceManager.getInstance().cameraHeight - ResourceManager.getInstance().mPlayerLifeTextureRegion.getHeight(), ResourceManager.getInstance().mPlayerLifeTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
								if (GameLevel.getInstance().lifeBars[i3]!= null ){
									if (GameLevel.getInstance().lifeBars[i3].hasParent()){
										hud.detachChild(lifeBars[i3]);
									}
								}
							}
							GameManager.getInstance().zeroLife();
							while (GameManager.getInstance().getLife()<10){
								hud.attachChild(lifeBars[GameManager.getInstance().getLife()]);
								GameManager.getInstance().incrementLife(1);
							}
						}
					});*/
					//END THIS IS THE STUFF THAT CHANGES PER LEVEL
					
	}
	}
	
	@Override
	public void onLoadScene() {
		super.onLoadScene();
		duration = 30; //reset on enemy waves, leave it be
		counter = 0; //handles enemy MoveModifier creation, leave it be
		widthX = (1024/ResourceManager.getInstance().cameraWidth);
		heightY = (640/ResourceManager.getInstance().cameraHeight);
		gameCamera.setBounds(0,0,(widthX*ResourceManager.getInstance().cameraWidth),(heightY*ResourceManager.getInstance().cameraHeight));
		mMusic = null;
		playerLife = GameManager.getInstance().getLife(); //leave it be between levels
		
		mMuteButton = null;
		
		//BACKGROUND
		 back1 = new Sprite((widthX*ResourceManager.getInstance().cameraWidth)/2f, (heightY*ResourceManager.getInstance().cameraHeight)/2f, ResourceManager.getInstance().mGameBackgroundTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		 back2 = new Sprite((widthX*ResourceManager.getInstance().cameraWidth)/2f, (heightY*ResourceManager.getInstance().cameraHeight)/2f, ResourceManager.getInstance().mGameBackgroundTextureRegion2, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		 back3 = new Sprite((widthX*ResourceManager.getInstance().cameraWidth)/2f, (heightY*ResourceManager.getInstance().cameraHeight)/2f, ResourceManager.getInstance().mGameBackgroundTextureRegion3, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		 back4 = new Sprite((widthX*ResourceManager.getInstance().cameraWidth)/2f, (heightY*ResourceManager.getInstance().cameraHeight)/2f, ResourceManager.getInstance().mGameBackgroundTextureRegion4, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		 back5 = new Sprite((widthX*ResourceManager.getInstance().cameraWidth)/2f, (heightY*ResourceManager.getInstance().cameraHeight)/2f, ResourceManager.getInstance().mGameBackgroundTextureRegion5, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			
		//BACKGROUND
		
		//GameManager.getInstance().resetGame();
		Log.v("","Player Life " + GameManager.getInstance().getLife());
		
		//PLAYER TEXTURE
		pCurrentPlayerTexture = ResourceManager.getInstance().mPlayerTextureRegion;
		//END PLAYER TEXTURE
		
		//PLAYER LASER TEXTURES
		pPlayerLaserTexture = ResourceManager.getInstance().mPlayerLaserTextureRegion;
		pPlayerLaserTexture2 = ResourceManager.getInstance().mPlayerLaserTextureRegion2;
		//END PLAYER LASER TEXTURES		
				
		//ENEMY LASER TEXTURE 
		pEnemyLaserTexture = ResourceManager.getInstance().mEnemyLaserTextureRegion;
		//END ENEMY LASER TEXTURE
		
		//GAME COMPLETE TEXTURE
		pGCTexture = ResourceManager.getInstance().mGCTextureRegion;
		//END GAME TEXTURE
		
		//FIRE RATE TEXTURE 
		pFireRateTexture = ResourceManager.getInstance().mFRPTextureRegion;
		fireRatePickup = new Sprite((float)(0 + (int)(Math.random() * ((1024 - 0) + 1))),(float)(0 + (int)(Math.random() * ((640 - 0) + 1))), pFireRateTexture, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		fireRatePickup.setCullingEnabled(true);
		fireRate = false;
		//END FIRE RATE TEXTURE
					
		//FIRE POWER TEXTURE 
		pFirePowerTexture = ResourceManager.getInstance().mFPPTextureRegion;
		firePowerPickup = new Sprite((float)(0 + (int)(Math.random() * ((1024 - 0) + 1))),(float)(0 + (int)(Math.random() * ((640 - 0) + 1))), pFirePowerTexture, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		firePowerPickup.setCullingEnabled(true);
		firePower = false;
		//END FIRE POWER TEXTURE
						
		//HEALTH TEXTURE
		pHealthTexture = ResourceManager.getInstance().mHealthTextureRegion;
		healthPickup = new Sprite((float)(0 + (int)(Math.random() * ((1024 - 0) + 1))),(float)(0 + (int)(Math.random() * ((640 - 0) + 1))), pHealthTexture, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		healthPickup.setCullingEnabled(true);
		health = false;
		//END HEALTH TEXTURE		
		
				//thisGameLevel.attachChild(mPlayer);
		
		enemyWave = new EnemyPool();
		pointerWave = new PointerPool();
		
		//PAUSE
		//pause button on HUD
		pause  = new Sprite((ResourceManager.getInstance().cameraWidth-(ResourceManager.getInstance().mMenuBackgroundSound.getWidth())-(ResourceManager.getInstance().mPause.getWidth()/2)), ResourceManager.getInstance().cameraHeight - (ResourceManager.getInstance().mPause.getHeight()/2), ResourceManager.getInstance().mPause, ResourceManager.getInstance().engine.getVertexBufferObjectManager()){
			
		//Override the onAreaTouched() method allowing us to define custom actions
		 
		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
			/* IN the event the mute button is pressed down on... */
			if (pSceneTouchEvent.isActionDown()){
				UserData.getInstance(ResourceManager.getInstance().context).pauseGame(true);
				return true;
			}
			return super.onAreaTouched(pSceneTouchEvent,  pTouchAreaLocalX, pTouchAreaLocalY);
		}
		};
		
		play = new Sprite(ResourceManager.getInstance().cameraWidth/3, ResourceManager.getInstance().cameraHeight - ResourceManager.getInstance().mPlay.getHeight(), ResourceManager.getInstance().mPlay, ResourceManager.getInstance().engine.getVertexBufferObjectManager()) {
			
		//Override the onAreaTouched() method allowing us to define custom actions
		 
		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
			/* IN the event the mute button is pressed down on... */
			if (pSceneTouchEvent.isActionDown()){
				UserData.getInstance(ResourceManager.getInstance().context).unPauseGame();
				Log.v("","UnPause Game!");
				return true;
			}
			return super.onAreaTouched(pSceneTouchEvent,  pTouchAreaLocalX, pTouchAreaLocalY);
		}
		};
		mPauseScene = new CameraScene(ResourceManager.getInstance().engine.getCamera());
		final int pauseX= (int) ((ResourceManager.getInstance().cameraWidth / 2) - (play.getWidth()/2));
		final int pauseY= (int) ((ResourceManager.getInstance().cameraHeight / 2) - (play.getHeight()/2));
		play.setPosition(pauseX, pauseY);
		
		mPauseScene.registerTouchArea(play);
		mPauseScene.attachChild(play);
		mPauseScene.setBackgroundEnabled(false);
		
		//Boss textures
		
		boss1tex = ResourceManager.getInstance().mBoss1TextureRegion;
		boss2tex = ResourceManager.getInstance().mBoss1TextureRegion2;
		boss3tex = ResourceManager.getInstance().mBoss1TextureRegion3;
		boss4tex = ResourceManager.getInstance().mBoss1TextureRegion4;
		boss5tex = ResourceManager.getInstance().mBoss1TextureRegion5;
		bLaser1 = ResourceManager.getInstance().mBossLaserTextureRegion;
		bLaser2 = ResourceManager.getInstance().mBossLaser2TextureRegion;
		
		//END PAUSE
				
				Level = UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1);
				LevelLoaded = false;
				
				//Set up a HUD
				hud = new HUD();
				
				gameCamera.setHUD(hud);
				//END HUD
				pointerPlus = new PlusPointer[3];
				mScoreText = new Text((ResourceManager.getInstance().cameraWidth/2f), ResourceManager.getInstance().cameraHeight - ResourceManager.getInstance().mFont.getLineHeight(), ResourceManager.getInstance().mFont, SCORE_STRING_PREFIX, MAX_CHARACTER_COUNT, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
				
				int lifeBarWidth = 0;
				lifeBars = new Sprite[GameManager.getInstance().getLife()];
				for (int i3 = 0;i3<GameManager.getInstance().getLife();i3++){
					lifeBarWidth += ResourceManager.getInstance().mPlayerLifeTextureRegion.getWidth();
					lifeBars[i3] = new Sprite((lifeBarWidth), ResourceManager.getInstance().cameraHeight - ResourceManager.getInstance().mPlayerLifeTextureRegion.getHeight(), ResourceManager.getInstance().mPlayerLifeTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
					//thisGameLevel.attachChild(lifeBars[i3]);
					hud.attachChild(lifeBars[i3]);
				}		
				
				
				
				//MUTE UNMUTE BUTTON SETUP
				if (mMuteButton != null) {
					if (mMuteButton.hasParent()){
						hud.detachChild(mMuteButton);
						hud.unregisterTouchArea(mMuteButton);
					}
				}
				
				// Create the music mute/unmute button 
				mMuteButton = new TiledSprite(ResourceManager.getInstance().cameraWidth-(ResourceManager.getInstance().mMenuBackgroundSound.getWidth()/2), ResourceManager.getInstance().cameraHeight-(ResourceManager.getInstance().mMenuBackgroundSound.getHeight()/2), ResourceManager.getInstance().mMenuBackgroundSound, ResourceManager.getInstance().engine.getVertexBufferObjectManager()) {
			
				//Override the onAreaTouched() method allowing us to define custom actions
				 
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
					/* IN the event the mute button is pressed down on... */
					if (pSceneTouchEvent.isActionDown()){
						Log.v("","Is mMusic Released" + mMusic.isReleased());
						if (mMusic.isPlaying()) {
							/*
							 * If music is playing, pause it and set tile index to MUTE
							 */UserData.getInstance(ResourceManager.getInstance().context).setMusic(1, 0);
							this.setCurrentTileIndex(ResourceManager.getInstance().MUTE);
							mMusic.pause();
							
						} else {
							/*
							 * If music is paused, play it and set tile index to
							 * UNMUTE
							 */UserData.getInstance(ResourceManager.getInstance().context).setMusic(1, 1);
							this.setCurrentTileIndex(ResourceManager.getInstance().UNMUTE);
							mMusic.play();
							ResourceManager.getInstance().engine.start();
						}
						return true;
					}
					return super.onAreaTouched(pSceneTouchEvent,  pTouchAreaLocalX, pTouchAreaLocalY);
				}
				};
		
		
				/* Set teh current tile index to unmuted on application startup*/
				if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)== 1){
					mMuteButton.setCurrentTileIndex(ResourceManager.getInstance().UNMUTE); 
				} else if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)== 0){
					mMuteButton.setCurrentTileIndex(ResourceManager.getInstance().MUTE); 
				}
				
		
				hud.registerTouchArea(mMuteButton);
				hud.attachChild(mMuteButton);
				//MUTE UNMUTE BUTTON SETUP
				
				//Pause button
				if (pause != null) {
					if (pause.hasParent()){
						hud.detachChild(pause);
						hud.unregisterTouchArea(pause);
					}
				}
				
				//end pause button
				hud.registerTouchArea(pause);
				hud.attachChild(pause);
				if (!mScoreText.hasParent()){
					hud.attachChild(mScoreText);
				}
				
		
		//Wave Nums
				wav1 = new Sprite(mScoreText.getX(), mScoreText.getY()-mScoreText.getHeight(), ResourceManager.getInstance().mWave1TextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
				wav2 = new Sprite(mScoreText.getX(), mScoreText.getY()-mScoreText.getHeight(), ResourceManager.getInstance().mWave2TextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
				wav3 = new Sprite(mScoreText.getX(), mScoreText.getY()-mScoreText.getHeight(), ResourceManager.getInstance().mWave3TextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
				wav4 = new Sprite(mScoreText.getX(), mScoreText.getY()-mScoreText.getHeight(), ResourceManager.getInstance().mWave4TextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
				wav5 = new Sprite(mScoreText.getX(), mScoreText.getY()-mScoreText.getHeight(), ResourceManager.getInstance().mWave5TextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
				
		//End
				//LEVEL NUMS
				lvl1Num = new Sprite(mScoreText.getX(), mScoreText.getY()-mScoreText.getHeight()-wav1.getHeight(), ResourceManager.getInstance().mLevel1TextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
				lvl2Num = new Sprite(mScoreText.getX(), mScoreText.getY()-mScoreText.getHeight()-wav2.getHeight(), ResourceManager.getInstance().mLevel2TextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
				lvl3Num = new Sprite(mScoreText.getX(), mScoreText.getY()-mScoreText.getHeight()-wav3.getHeight(), ResourceManager.getInstance().mLevel3TextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
				lvl4Num = new Sprite(mScoreText.getX(), mScoreText.getY()-mScoreText.getHeight()-wav4.getHeight(), ResourceManager.getInstance().mLevel4TextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
				lvl5Num = new Sprite(mScoreText.getX(), mScoreText.getY()-mScoreText.getHeight()-wav5.getHeight(), ResourceManager.getInstance().mLevel5TextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			//End		
				
		//Enemy Textures
			eTex = new ITextureRegion[25];
			eTex[0] = ResourceManager.getInstance().mEnemy1TextureRegion;
			eTex[1] = ResourceManager.getInstance().mEnemy1TextureRegion2;
			eTex[2] = ResourceManager.getInstance().mEnemy1TextureRegion3;
			eTex[3] = ResourceManager.getInstance().mEnemy1TextureRegion4;
			eTex[4] = ResourceManager.getInstance().mEnemy1TextureRegion5;
			eTex[5] = ResourceManager.getInstance().mEnemy2TextureRegion;
			eTex[6] = ResourceManager.getInstance().mEnemy2TextureRegion2;
			eTex[7] = ResourceManager.getInstance().mEnemy2TextureRegion3;
			eTex[8] = ResourceManager.getInstance().mEnemy2TextureRegion4;
			eTex[9] = ResourceManager.getInstance().mEnemy2TextureRegion5;
			eTex[10] = ResourceManager.getInstance().mEnemy3TextureRegion;
			eTex[11] = ResourceManager.getInstance().mEnemy3TextureRegion2;
			eTex[12] = ResourceManager.getInstance().mEnemy3TextureRegion3;
			eTex[13] = ResourceManager.getInstance().mEnemy3TextureRegion4;
			eTex[14] = ResourceManager.getInstance().mEnemy3TextureRegion5;
			eTex[15] = ResourceManager.getInstance().mEnemy4TextureRegion;
			eTex[16] = ResourceManager.getInstance().mEnemy4TextureRegion2;
			eTex[17] = ResourceManager.getInstance().mEnemy4TextureRegion3;
			eTex[18] = ResourceManager.getInstance().mEnemy4TextureRegion4;
			eTex[19] = ResourceManager.getInstance().mEnemy4TextureRegion5;
			eTex[20] = ResourceManager.getInstance().mEnemy5TextureRegion;
			eTex[21] = ResourceManager.getInstance().mEnemy5TextureRegion2;
			eTex[22] = ResourceManager.getInstance().mEnemy5TextureRegion3;
			eTex[23] = ResourceManager.getInstance().mEnemy5TextureRegion4;
			eTex[24] = ResourceManager.getInstance().mEnemy5TextureRegion5;
			
		//end
				
		thisGameLevel.registerUpdateHandler(new IUpdateHandler() {
			
			
			@Override
			public void onUpdate(float pSecondsElapsed) {	
				
			 if (GameManager.getInstance().GetGMDone()) {
					
				
				if (LevelLoaded == false){
					loadLevel(Level);
					//mMusic = new Music();
					if (Level == 1) {
						
							//mMusic.stop();
							//mMusic.release();
							
							Log.v("","Set music to level 1 music");
							mMusic = ResourceManager.getInstance().mMusiclvl1;
							mMusic.getMediaPlayer().seekTo(0);
							
							if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==1){
								mMusic.setLooping(true);
								mMusic.play();
								Log.v("","Level1 music should be playing");
							} else if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==0){
								mMusic.pause();
								Log.v("","Level 1 music should be paused");
							}
							Log.v("","Is mMusic Released" + mMusic.isReleased());
						
					}
					else if (Level == 2) {
						
							if (mMusic != null){
								if (mMusic.isPlaying()) mMusic.stop();
							}
							mMusic = ResourceManager.getInstance().mMusiclvl2;
							Log.v("","Set music to level 2 music");
							mMusic.getMediaPlayer().seekTo(0);
							if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==1){
								mMusic.setLooping(true);
								mMusic.play();
								Log.v("","Level2 music should be playing");
							} else if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==0){
								mMusic.pause();
								Log.v("","Level 2 music should be paused");
							}
							Log.v("","Is mMusic Released" + mMusic.isReleased());
						
					}
					else if (Level == 3) {
						
						if (mMusic != null){
							if (mMusic.isPlaying()) mMusic.stop();
						}
							mMusic = ResourceManager.getInstance().mMusiclvl3;
							Log.v("","Set music to level 3 music");
							mMusic.getMediaPlayer().seekTo(0);
							if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==1){
								mMusic.setLooping(true);
								mMusic.play();
								Log.v("","Level3 music should be playing");
							} else if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==0){
								mMusic.pause();
								Log.v("","Level 3 music should be paused");
							}
						
					}
					else if (Level == 4) {
						
						if (mMusic != null){
							if (mMusic.isPlaying()) mMusic.stop();
						}
							mMusic = ResourceManager.getInstance().mMusiclvl4;
							Log.v("","Set music to level 4 music");
							mMusic.getMediaPlayer().seekTo(0);
							if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==1){
								mMusic.setLooping(true);
								mMusic.play();
								Log.v("","Level4 music should be playing");
							} else if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==0){
								mMusic.pause();
								Log.v("","Level 4 music should be paused");
							}
						
					}
					else if (Level == 5) {
							
						if (mMusic != null){
							if (mMusic.isPlaying()) mMusic.stop();
						}
							//mMusic.release();
							mMusic = ResourceManager.getInstance().mMusiclvl5;
							Log.v("","Set music to level 5 music");
							mMusic.getMediaPlayer().seekTo(0);
							if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==1){
								mMusic.setLooping(true);
								mMusic.play();
								Log.v("","Level5 music should be playing");
							} else if (UserData.getInstance(ResourceManager.getInstance().context).getMusic(1)==0){
								mMusic.pause();
								Log.v("","Level5 music should be paused");
							}
						
					}
					
					
					LevelLoaded = true;
					
						mPlayer = new Player(pCurrentPlayerTexture);
						GameLevel.getInstance().attachChild(mPlayer);
						PlayerReady = true;
						//mPlayer.setPosition(100, 100);
						ResourceManager.getInstance().engine.getCamera().setChaseEntity(mPlayer); //set chase camera
						
					
				}
				
				
				
				//SETUP NEW WAVE OF ENEMIES
				if (enemyCount == 0){
					
									
								
					if (waveCounter == 0) {
						//player.getX()-(ResourceManager.getInstance().cameraWidth/2), player.getY()-(ResourceManager.getInstance().cameraHeight/2),player.getX()-(ResourceManager.getInstance().cameraWidth/2),player.getY()+(ResourceManager.getInstance().cameraHeight/2)
						waveNum = wav1;
						hud.attachChild(waveNum);
						if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 1){
							levelNum = lvl1Num;
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 2){
							levelNum = lvl2Num;
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 3){
							levelNum = lvl3Num;
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 4){
							levelNum = lvl4Num;
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 5){
							levelNum = lvl5Num;
						}
						
						hud.attachChild(levelNum);
						waveCounter++;
						//ENEMY TEXTURE
						//set the currentEnemyTexture - set this every time you change the enemy type (5 per level), 25 total.
						if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 1){
							pCurrentEnemyTexture = eTex[0];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 2){
							pCurrentEnemyTexture = eTex[1];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 3){
							pCurrentEnemyTexture = eTex[2];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 4){
							pCurrentEnemyTexture = eTex[3];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 5){
							pCurrentEnemyTexture = eTex[4];
						}
						
						//END ENEMY TEXTURE
						//wait(2000);
						duration = 30;
						//enemyWave = new EnemyPool();
						//pointerWave = new PointerPool();
						totalEnemiesInPool = 5;
						enemyCount = totalEnemiesInPool;
						GameManager.getInstance().resetEnemyCount(enemyCount);
						mEnemyWave = null;
						mEnemyWave = new Enemy[totalEnemiesInPool];
						pointer = new Pointer[totalEnemiesInPool];
						//enemyBody = new Body[10];  
						//firstWave.batchAllocatePoolItems(10);
						
						for (int i = 0; i<totalEnemiesInPool; i++){
							mEnemyWave[i] = enemyWave.obtainPoolItem();
							pointer[i] = pointerWave.obtainPoolItem(mEnemyWave[i]);
							
						}
						GameLevel.getInstance().mBoss1 = null;
								//ResourceManager.getInstance().engine.unregisterUpdateHandler(GameLevel.getInstance().bulletTimerHandler);
								//mPlayer.detachSelf();
								t = new Timer();
								t.schedule(new TimerTask() {
									
									@Override
									public void run() {
										
										ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
											@Override
											public void run() {
												Log.v("","TIMER TASK EXPIRES");
												hud.detachChild(waveNum);
												hud.detachChild(levelNum);
												readyToFireLaser = true;
												readyToFireEnemyLaser = true;
											}
										}); 
										
									}
								}, 5000);
							
						
						
						
					
					}
					else if (waveCounter == 1) {
						waveNum = wav2;
						hud.attachChild(waveNum);
						waveCounter++;
						//ENEMY TEXTURE
						//set the currentEnemyTexture - set this every time you change the enemy type (5 per level), 25 total.
						if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 1){
							pCurrentEnemyTexture = eTex[5];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 2){
							pCurrentEnemyTexture = eTex[6];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 3){
							pCurrentEnemyTexture = eTex[7];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 4){
							pCurrentEnemyTexture = eTex[8];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 5){
							pCurrentEnemyTexture = eTex[9];
						}
						//END ENEMY TEXTURE
						//wait(2000);
						duration = 25;
						//enemyWave = new EnemyPool();
						//pointerWave = new PointerPool();
						plusPointerWave = new PlusPointerPool();
						totalEnemiesInPool = 5;
						enemyCount = totalEnemiesInPool;
						GameManager.getInstance().resetEnemyCount(enemyCount);
						mEnemyWave = new Enemy[totalEnemiesInPool];
						pointer = new Pointer[totalEnemiesInPool];
						
						//enemyBody = new Body[10];  
						//firstWave.batchAllocatePoolItems(10);
						
						for (int i = 0; i<totalEnemiesInPool; i++){
							mEnemyWave[i] = enemyWave.obtainPoolItem();
							pointer[i] = pointerWave.obtainPoolItem(mEnemyWave[i]);
						}
						
								//ResourceManager.getInstance().engine.unregisterUpdateHandler(GameLevel.getInstance().bulletTimerHandler);
								//mPlayer.detachSelf();
								t = new Timer();
								t.schedule(new TimerTask() {
									
									@Override
									public void run() {
										ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
											@Override
											public void run() {
												Log.v("","TIMER TASK EXPIRES");
												hud.detachChild(waveNum);
												if (!firePowerPickup.hasParent()) {
													thisGameLevel.attachChild(firePowerPickup);
													firePowerAttached = true;
													
													plusPointerPowerLine = new Line(firePowerPickup.getX(), firePowerPickup.getY(), mPlayer.getX(), mPlayer.getY(), ResourceManager.getInstance().engine.getVertexBufferObjectManager());
													plusPointerPowerLine.setCullingEnabled(true);
													pointerPlus[1] = plusPointerWave.obtainPoolItem(firePowerPickup, "FirePower");
													//thisGameLevel.attachChild(plusPointerPowerLine);
													//thisGameLevel.attachChild(plusPointerP);
												}
												if (!healthPickup.hasParent()) {
													thisGameLevel.attachChild(healthPickup);
													healthAttached = true;
													
													//plusPointerH = new Sprite(GameLevel.getInstance().mPlayer.getX(),GameLevel.getInstance().mPlayer.getY(), ResourceManager.getInstance().mHealthPointer, ResourceManager.getInstance().engine.getVertexBufferObjectManager());	
													plusPointerHealthLine = new Line(healthPickup.getX(), healthPickup.getY(), mPlayer.getX(), mPlayer.getY(), ResourceManager.getInstance().engine.getVertexBufferObjectManager());
													plusPointerHealthLine.setCullingEnabled(true);
													pointerPlus[0] = plusPointerWave.obtainPoolItem(healthPickup, "Health");
													//thisGameLevel.attachChild(plusPointerHealthLine);
													Log.v("", "loaded healthPickup");
													//thisGameLevel.attachChild(plusPointerH);
												}
												readyToFireLaser = true;
												readyToFireEnemyLaser = true;
											}
										});
									}
								}, 2000);
							
					}
					else if (waveCounter == 2) {
						waveNum = wav3;
						hud.attachChild(waveNum);
						waveCounter++;
						//ENEMY TEXTURE
						//set the currentEnemyTexture - set this every time you change the enemy type (5 per level), 25 total.
						if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 1){
							pCurrentEnemyTexture = eTex[10];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 2){
							pCurrentEnemyTexture = eTex[11];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 3){
							pCurrentEnemyTexture = eTex[12];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 4){
							pCurrentEnemyTexture = eTex[13];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 5){
							pCurrentEnemyTexture = eTex[14];
						}
						//END ENEMY TEXTURE
						//wait(2000);
						duration = 25;
						//enemyWave = new EnemyPool();
						//pointerWave = new PointerPool();
						plusPointerWave = new PlusPointerPool();
						totalEnemiesInPool = 5;
						enemyCount = totalEnemiesInPool;
						GameManager.getInstance().resetEnemyCount(enemyCount);
						mEnemyWave = null;
						mEnemyWave = new Enemy[totalEnemiesInPool];
						pointer = new Pointer[totalEnemiesInPool];
						
						//enemyBody = new Body[10];  
						//firstWave.batchAllocatePoolItems(10);
						for (int i = 0; i<totalEnemiesInPool; i++){
							mEnemyWave[i] = enemyWave.obtainPoolItem();
							pointer[i] = pointerWave.obtainPoolItem(mEnemyWave[i]); 
							
							//END COMPASS
							
						}
								//ResourceManager.getInstance().engine.unregisterUpdateHandler(GameLevel.getInstance().bulletTimerHandler);
								//mPlayer.detachSelf();
								t = new Timer();
								t.schedule(new TimerTask() {
									
									@Override
									public void run() {
										ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
											@Override
											public void run() {
												Log.v("","TIMER TASK EXPIRES");
												hud.detachChild(waveNum);
												if (!healthPickup.hasParent()) {
													thisGameLevel.attachChild(healthPickup);
													healthAttached = true;
													
													//plusPointerH = new Sprite(GameLevel.getInstance().mPlayer.getX(),GameLevel.getInstance().mPlayer.getY(), ResourceManager.getInstance().mHealthPointer, ResourceManager.getInstance().engine.getVertexBufferObjectManager());	
													plusPointerHealthLine = new Line(healthPickup.getX(), healthPickup.getY(), mPlayer.getX(), mPlayer.getY(), ResourceManager.getInstance().engine.getVertexBufferObjectManager());
													plusPointerHealthLine.setCullingEnabled(true);
													pointerPlus[0] = plusPointerWave.obtainPoolItem(healthPickup, "Health");
													//thisGameLevel.attachChild(plusPointerHealthLine);
													//thisGameLevel.attachChild(plusPointerH);
												}
												readyToFireLaser = true;
												readyToFireEnemyLaser = true;
											}
										});
									}
								}, 2000);
							
					}
					else if (waveCounter == 3) {
						waveNum = wav4;
						hud.attachChild(waveNum);
						waveCounter++;
						//ENEMY TEXTURE
						//set the currentEnemyTexture - set this every time you change the enemy type (5 per level), 25 total.
						if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 1){
							pCurrentEnemyTexture = eTex[15];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 2){
							pCurrentEnemyTexture = eTex[16];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 3){
							pCurrentEnemyTexture = eTex[17];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 4){
							pCurrentEnemyTexture = eTex[18];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 5){
							pCurrentEnemyTexture = eTex[19];
						}
						//END ENEMY TEXTURE
						//wait(2000);
						duration = 20;
						//enemyWave = new EnemyPool();
						//pointerWave = new PointerPool();
						plusPointerWave = new PlusPointerPool();
						totalEnemiesInPool = 5;
						enemyCount = totalEnemiesInPool;
						GameManager.getInstance().resetEnemyCount(enemyCount);
						mEnemyWave = null;
						mEnemyWave = new Enemy[totalEnemiesInPool];
						pointer = new Pointer[totalEnemiesInPool];
						
						//enemyBody = new Body[10];  
						//firstWave.batchAllocatePoolItems(10);
						for (int i = 0; i<totalEnemiesInPool; i++){
							mEnemyWave[i] = enemyWave.obtainPoolItem();
							pointer[i] = pointerWave.obtainPoolItem(mEnemyWave[i]);
							//END COMPASS
							
						}
								//ResourceManager.getInstance().engine.unregisterUpdateHandler(GameLevel.getInstance().bulletTimerHandler);
								//mPlayer.detachSelf();
								t = new Timer();
								t.schedule(new TimerTask() {
									
									@Override
									public void run() {
										ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
											@Override
											public void run() {
												Log.v("","TIMER TASK EXPIRES");
												hud.detachChild(waveNum);
												if (!fireRatePickup.hasParent()) {
													thisGameLevel.attachChild(fireRatePickup);
													fireRateAttached = true;
																									
													//plusPointerR = new Sprite(GameLevel.getInstance().mPlayer.getX(),GameLevel.getInstance().mPlayer.getY(), ResourceManager.getInstance().mHealthPointer, ResourceManager.getInstance().engine.getVertexBufferObjectManager());	
													plusPointerRateLine = new Line(fireRatePickup.getX(), fireRatePickup.getY(), mPlayer.getX(), mPlayer.getY(), ResourceManager.getInstance().engine.getVertexBufferObjectManager());
													plusPointerRateLine.setCullingEnabled(true);
													pointerPlus[2] = plusPointerWave.obtainPoolItem(fireRatePickup, "FireRate");	
													//thisGameLevel.attachChild(plusPointerRateLine);
													//thisGameLevel.attachChild(plusPointerR);
												}
												if (!healthPickup.hasParent()) {
													thisGameLevel.attachChild(healthPickup);
													healthAttached = true;
													//plusPointer = new Sprite[1];
													
													plusPointerHealthLine = new Line(healthPickup.getX(), healthPickup.getY(), mPlayer.getX(), mPlayer.getY(), ResourceManager.getInstance().engine.getVertexBufferObjectManager());
													plusPointerHealthLine.setCullingEnabled(true);
													pointerPlus[0] = plusPointerWave.obtainPoolItem(healthPickup, "Health");
													//thisGameLevel.attachChild(plusPointerHealthLine);
													//thisGameLevel.attachChild(plusPointerH);
												}
												if (GameLevel.getInstance().PlayerLaser != null){
													mPlayer.bulletPool.recyclePoolItem(PlayerLaser);	
													GameLevel.getInstance().PlayerLaser.detachSelf();
													mPlayer.recycleBullet = true;
													mPlayer.bulletLifeEnd = true;
												}
													//mEnemyWave[i2] = null;
													//PlayerLaser = null;
													readyToFireLaser = true;
													readyToFireEnemyLaser = true;
												}
										});
									}
								}, 2000);
							
					}
					else if (waveCounter == 4) {
						waveNum = wav5;
						hud.attachChild(waveNum);
						waveCounter++;
						//ENEMY TEXTURE
						//set the currentEnemyTexture - set this every time you change the enemy type (5 per level), 25 total.
						if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 1){
							pCurrentEnemyTexture = eTex[20];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 2){
							pCurrentEnemyTexture = eTex[21];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 3){
							pCurrentEnemyTexture = eTex[22];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 4){
							pCurrentEnemyTexture = eTex[23];
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1) == 5){
							pCurrentEnemyTexture = eTex[24];
						}
						//END ENEMY TEXTURE
						//wait(2000);
						duration = 20;
						//enemyWave = new EnemyPool();
						//pointerWave = new PointerPool();
						plusPointerWave = new PlusPointerPool();
						totalEnemiesInPool = 5;
						enemyCount = totalEnemiesInPool;
						GameManager.getInstance().resetEnemyCount(enemyCount);
						mEnemyWave = null;
						mEnemyWave = new Enemy[totalEnemiesInPool];
						pointer = new Pointer[totalEnemiesInPool];
						
						//enemyBody = new Body[10];  
						//firstWave.batchAllocatePoolItems(10);
						for (int i = 0; i<totalEnemiesInPool; i++){
							mEnemyWave[i] = enemyWave.obtainPoolItem();
							pointer[i] = pointerWave.obtainPoolItem(mEnemyWave[i]);
							
						}
						
								//ResourceManager.getInstance().engine.unregisterUpdateHandler(GameLevel.getInstance().bulletTimerHandler);
								//mPlayer.detachSelf();
								t = new Timer();
								t.schedule(new TimerTask() {
									
									@Override
									public void run() {
										ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
											@Override
											public void run() {
												Log.v("","TIMER TASK EXPIRES");
												hud.detachChild(waveNum);
												if (!healthPickup.hasParent()) {
													//healthPickup.setCullingEnabled(true);
													thisGameLevel.attachChild(healthPickup);
													healthAttached = true;
													
													//plusPointerH = new Sprite(GameLevel.getInstance().mPlayer.getX(),GameLevel.getInstance().mPlayer.getY(), ResourceManager.getInstance().mHealthPointer, ResourceManager.getInstance().engine.getVertexBufferObjectManager());	
													plusPointerHealthLine = new Line(healthPickup.getX(), healthPickup.getY(), mPlayer.getX(), mPlayer.getY(), ResourceManager.getInstance().engine.getVertexBufferObjectManager());
													plusPointerHealthLine.setCullingEnabled(true);
													pointerPlus[0] = plusPointerWave.obtainPoolItem(healthPickup, "Health");
													//thisGameLevel.attachChild(plusPointerHealthLine);
													//thisGameLevel.attachChild(plusPointerH);
												}
												readyToFireLaser = true;
												readyToFireEnemyLaser = true;
											}
										});
									}
								}, 2000);
							
					}
					else if (waveCounter == 5){
						waveNum = new Sprite(mScoreText.getX(), mScoreText.getY()-mScoreText.getHeight(), ResourceManager.getInstance().mBossMsgTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						hud.attachChild(waveNum);
						waveCounter++;
						totalEnemiesInPool = 1;
						bossPointerWave = new BossPointerPool();
						//mEnemyWave = null;
						bossPointer = new BossPointer[totalEnemiesInPool];
						//wait(2000);
						duration = 25;
						if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1)==1){
							mBoss1 = new Lvl1Boss(pLvl1BossTexture);
							
							//BossReady = true;
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1)==2){
							mBoss1 = new Lvl2Boss(pLvl1BossTexture);
							//BossReady = true;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1)==3){
							mBoss1 = new Lvl3Boss(pLvl1BossTexture);
							//BossReady = true;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1)==4){
							mBoss1 = new Lvl4Boss(pLvl1BossTexture);
							///BossReady = true;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1)==5){
							mBoss1 = new Lvl5Boss(pLvl1BossTexture);
							//BossReady = true;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						}
						mBoss1.setPosition((float)(0 + (int)(Math.random() * ((1024 - 0) + 1))),(float)(0 + (int)(Math.random() * ((640 - 0) + 1))));
						mBoss1.setCullingEnabled(true);
						//BOSS1 SETUP
						//mBoss1 = new Lvl1Boss(pLvl1BossTexture);
						//BOSS2 SETUP
						
						//totalEnemiesInPool = 18;
						//enemyCount = totalEnemiesInPool;
						//GameManager.getInstance().resetEnemyCount(enemyCount);
						//mEnemyWave = new Enemy[totalEnemiesInPool];
						//enemyBody = new Body[10];  
						//firstWave.batchAllocatePoolItems(10);
						//for (int i = 0; i<totalEnemiesInPool; i++){
							//mEnemyWave[i] = enemyWave.obtainPoolItem();
						//}
								//ResourceManager.getInstance().engine.unregisterUpdateHandler(GameLevel.getInstance().bulletTimerHandler);
								//mPlayer.detachSelf();
								t = new Timer();
								t.schedule(new TimerTask() {
									
									@Override
									public void run() {
										ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
											@Override
											public void run() {
												Log.v("","BOSS CREATED");
												hud.detachChild(waveNum);
												readyToFireLaser = true;
												readyToFireEnemyLaser = true;
												BossReady = true;
												
												bossPointerLine = new Line(mBoss1.getX(), mBoss1.getY(), mPlayer.getX(), mPlayer.getY(), ResourceManager.getInstance().engine.getVertexBufferObjectManager());
												bossPointerLine.setCullingEnabled(true);
												bossPointer[0] = bossPointerWave.obtainPoolItem(mBoss1);
												//thisGameLevel.attachChild(plusPointerHealthLine);
												//thisGameLevel.attachChild(bossPointer);
											}
											
										});
									}
								}, 2000);
							
					}
					
							
					
				}
				
				//END SETUP NEW WAVE
				
				duration -= 0.01;
				
				//Update Lines for healthpickup, fireratepickup, firepowerpickup and boss
				
				if (BossReady){
					bossPointerLine.setPosition(mBoss1.getX(), mBoss1.getY(), mPlayer.getX(), mPlayer.getY());
				}
				//end update
									
					
				//FIRE RATE TEXTURE PICKUP
				if ((fireRateAttached == true) && (mPlayer.collidesWith(fireRatePickup))){
					ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							//Log.v("","BOSS CREATED");
							thisGameLevel.detachChild(fireRatePickup);
							if (ResourceManager.getInstance().mSound4 != null) {
								ResourceManager.getInstance().mSound4.play();
							}
							fireRate = true;
							fireRateAttached = false;
							//plusPointerRateLine.detachSelf();
							plusPointerRateLine = null;
							
								plusPointerWave.recyclePoolItem(pointerPlus[2]);
								pointerPlus[2].detachSelf();
								pointerPlus[2] = null;
							 
							
							Log.v("","Fire Rate Set To TRUE");
						}
					});
				}
				//END FIRE RaTE TEXTURE PICKUP
				
				//FIRE POEWR TEXTURE PICKUP
				if ((firePowerAttached == true) && (mPlayer.collidesWith(firePowerPickup))){
					ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							//Log.v("","BOSS CREATED");
							thisGameLevel.detachChild(firePowerPickup);
							if (ResourceManager.getInstance().mSound4 != null) {
								ResourceManager.getInstance().mSound4.play();
							}
							//pPlayerLaserTexture = ResourceManager.getInstance().mPlayerLaserTextureRegion2;
							firePower = true;
							firePowerAttached = false;
							//plusPointerPowerLine.detachSelf();
							plusPointerPowerLine = null;
							
								plusPointerWave.recyclePoolItem(pointerPlus[1]);
								pointerPlus[1].detachSelf();
								pointerPlus[1] = null;
							 
							Log.v("","Fire Power Set To TRUE");
						}
					});
				}
				//END FIRE POWER TEXTURE PICKUP
				
				//HEALTH TEXTURE PICKUP
				if ((healthAttached == true) && (mPlayer.collidesWith(healthPickup))){
					ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							//Log.v("","BOSS CREATED");
							thisGameLevel.detachChild(healthPickup);
							if (ResourceManager.getInstance().mSound4 != null) {
								ResourceManager.getInstance().mSound4.play();
							}
							health = true;
							healthAttached = false;
							//plusPointerHealthLine.detachSelf();
							plusPointerHealthLine= null;
						
								plusPointerWave.recyclePoolItem(pointerPlus[0]);
								pointerPlus[0].detachSelf();
								pointerPlus[0] = null;
							
							Log.v("","Health Set To TRUE");
							if (GameManager.getInstance().getLife()<=8){
								//lifeBars[playerLife+1].reset();	
								hud.attachChild(lifeBars[GameManager.getInstance().getLife()]);
								GameManager.getInstance().incrementLife(1);
								//lifeBars[playerLife+1].reset();
								hud.attachChild(lifeBars[GameManager.getInstance().getLife()]);
								GameManager.getInstance().incrementLife(1);
							}
						}
					});
				}
				//HEALTH TEXTURE PICKUP
				
				//PLAYER LIFE IF HIT
				//for (int y = 0;i<5;i++){
					if (playerHit && GameManager.getInstance().getLife() >0){
						
						ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								if (GameManager.getInstance().getLife() > 0){
									hud.detachChild(lifeBars[GameManager.getInstance().getLife()-1]);
								}
								if (ResourceManager.getInstance().mSound2 != null) {
									ResourceManager.getInstance().mSound2.play();
								}
								GameManager.getInstance().decrementLife(1);
								playerHit = false;
							}
						});
						
						
					}
					//PLAYER LIFE IF HIT BY BOSS
					//for (int y = 0;i<5;i++){
						if (playerHitBoss && GameManager.getInstance().getLife() >1){
							
							ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									hud.detachChild(lifeBars[GameManager.getInstance().getLife()-1]);
									GameManager.getInstance().decrementLife(1);
									hud.detachChild(lifeBars[GameManager.getInstance().getLife()-1]);
									GameManager.getInstance().decrementLife(1);
									if (ResourceManager.getInstance().mSound2 != null) {
										ResourceManager.getInstance().mSound2.play();
									}
									playerHitBoss = false;
								}
							});
							
						}
						else if (playerHitBoss && GameManager.getInstance().getLife() >0){
							
							ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									hud.detachChild(lifeBars[GameManager.getInstance().getLife()-1]);
									GameManager.getInstance().decrementLife(1);
									if (ResourceManager.getInstance().mSound2 != null) {
										ResourceManager.getInstance().mSound2.play();
									}
									playerHitBoss = false;
								}
							});
							
						}
						
						
				//}
				
					if (GameManager.getInstance().getLife() == 0){
						//ResourceManager.getInstance().mSound3.play();
						GameOver = new Sprite(mScoreText.getX(), mScoreText.getY()-mScoreText.getHeight(), ResourceManager.getInstance().mGameOverTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						hud.attachChild(GameOver);
						
						ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								ResourceManager.getInstance().engine.unregisterUpdateHandler(GameLevel.getInstance().bulletTimerHandler);
								mPlayer.detachSelf();
								BossReady = false;
								//mPlayer = null;
								
								//mPlayer = null;
								Timer t = new Timer();
								t.schedule(new TimerTask() {
									
									@Override
									public void run() {
										if(SceneManager.getInstance().mCurrentScene.getClass().getGenericSuperclass().equals(ManagedGameScene.class) || 
												(SceneManager.getInstance().mCurrentScene.getClass().getGenericSuperclass().equals(ManagedMenuScene.class) &!
													SceneManager.getInstance().mCurrentScene.getClass().equals(MainMenu.class))){
											ResourceManager.getInstance().engine.unregisterUpdateHandler(GameLevel.getInstance().bulletTimerHandler);
											//hud.detachSelf();
											SceneManager.getInstance().showMainMenu();
											
											//gameCamera.setBounds(0,0,ResourceManager.getInstance().cameraWidth,ResourceManager.getInstance().cameraHeight);
										}
									}
								}, 2000);
							}
						});
						
					}
					
				//END PLAYER LIFE
					
				
					
				//PLAYER LASER TO ENEMY COLLISIONS
				
				for (int i = 0; i<totalEnemiesInPool; i++){
					//ATTACH ENEMIES ONLY WHEN THEY ARE IN SIGHT
					 //thisGameLevel.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
				
					
					//END
					
					  if (((PlayerLaser!=null && mEnemyWave[i]!=null))){
						  
						//ENEMIES SHOULD FIRE WHEN THEY ARE ON THE SCREEN
						if (mEnemyWave[i].collidesWith(mPlayer.enemyFireGrid)){
							mEnemyWave[i].readyToFireEnemyLaser = true;	
						} else {
							mEnemyWave[i].readyToFireEnemyLaser = false;
						}
							
						//END ENEMIES SHOULD FIRE WHEN ON SCREEN
						if ((PlayerLaser.collidesWith(mEnemyWave[i]))){
							if (PlayerLaser.isCollisionActive){
								mEnemyWave[i].eLife -= 1;
								PlayerLaser.isCollisionActive = false;
								Log.v("","Reduce life by 1");
								ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
									@Override
									public void run() {
										//Log.i("MODIFIER", "Enemy Killed");
										GameManager.getInstance().incrementScore(5);
										hud.detachChild(mScoreText);
										mScoreText = new Text((ResourceManager.getInstance().cameraWidth/2f), ResourceManager.getInstance().cameraHeight - ResourceManager.getInstance().mFont.getLineHeight(), ResourceManager.getInstance().mFont, SCORE_STRING_PREFIX + GameManager.getInstance().getCurrentScore(), MAX_CHARACTER_COUNT, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
										hud.attachChild(mScoreText);
									}
								});
							
								ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
									@Override
									public void run() {
										//mEnemyWave[i2].bulletPool = null;
										//pointerWave.recyclePoolItem(pointer[i2]);
										//pointer[i2].detachSelf();//Recycle Pointer here
										//mEnemyWave[i2].direction.detachSelf();
										//mEnemyWave[i2].pointer.unregisterEntityModifier(mEnemyWave[i2].mm);
										//enemyWave.recyclePoolItem(mEnemyWave[i2]);
										//mEnemyWave[i2].detachSelf();
										if (ResourceManager.getInstance().mSound2 != null) {
											ResourceManager.getInstance().mSound2.play();
										}
												
											if (GameLevel.getInstance().PlayerLaser != null){
												mPlayer.bulletPool.recyclePoolItem(PlayerLaser);	
												GameLevel.getInstance().PlayerLaser.detachSelf();
												mPlayer.recycleBullet = true;
												mPlayer.bulletLifeEnd = true;
											}
												//mEnemyWave[i2] = null;
												PlayerLaser = null;
												//pointer[i2] = null;
									
									}
								});
								animatedSprite = new AnimatedSprite(mEnemyWave[i].getX(), mEnemyWave[i].getY(), ResourceManager.getInstance().mTiledTextureRegionExplosion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
								
								//Length to play each frame before moving on to the next 
								long frameDuration[] = {75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75};
					
								// we can define the indices of the animation to play between 
								int firstTileIndex = 0;
								int lastTileIndex = ResourceManager.getInstance().mTiledTextureRegionExplosion.getTileCount()-1;
							
							
					
								boolean loopAnimation = false;
								//mEnemyWave[i2] = null;
								//Animate the sprite with the data as set defined above 
								animatedSprite.animate(frameDuration, firstTileIndex, lastTileIndex, loopAnimation, new IAnimationListener() {
						
									@Override
									public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount){
										//Fired when the animation first begins to run 
										//counter++;
									}
						
									@Override
									public void onAnimationFrameChanged(AnimatedSprite pAniamtedSprite, int pOldFrameIndex, int pNewIndex) {
										// Fired every time a new frame is selected to display 
									}
						
									@Override 
									public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount){
										// Fired when an animation loop ends (from first to last frame
										ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
											@Override
											public void run() {
												thisGameLevel.detachChild(animatedSprite);
											}
										});
										Log.v("","Gets past sprite animation");
									
									}
						
									@Override
									public void onAnimationFinished(AnimatedSprite pAnimatedSprite)
									{
									
											//SceneManager.getInstance().showMainMenu();
									}
									//ANIMATED SPRITE ENDS
								});
								thisGameLevel.attachChild(animatedSprite);
							
							}
							if (mEnemyWave[i].eLife == 0){
								Log.v("","Dead");
								ResourceManager.getInstance().engine.unregisterUpdateHandler(mEnemyWave[i].getTimerHandler());
								enemyCount--;
								//PLAYER SCORING
								GameManager.getInstance().decrementEnemyCount();
								if (GameManager.getInstance().isEnemyDown()) {
								//Score increment used to be here.
									
								}
								//END PLAYER SCORING
							
							
							
								//ANIMATED SPRITE BEGINS
								//Create a new animated sprite in the center of the scene
								
								final int i2 = i;
								//mEnemyWave[i2].clearEntityModifiers();
							
								//mEnemyWave[i2].clearUpdateHandlers();
								ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
									@Override
									public void run() {
										//mEnemyWave[i2].bulletPool = null;
										pointerWave.recyclePoolItem(pointer[i2]);
										pointer[i2].detachSelf();//Recycle Pointer here
										mEnemyWave[i2].direction.detachSelf();
										//mEnemyWave[i2].pointer.unregisterEntityModifier(mEnemyWave[i2].mm);
										enemyWave.recyclePoolItem(mEnemyWave[i2]);
										mEnemyWave[i2].detachSelf();
										if (ResourceManager.getInstance().mSound2 != null) {
											ResourceManager.getInstance().mSound2.play();
										}
												
											//if (GameLevel.getInstance().PlayerLaser != null){
												//mPlayer.bulletPool.recyclePoolItem(PlayerLaser);	
												//GameLevel.getInstance().PlayerLaser.detachSelf();
												//mPlayer.recycleBullet = true;
												//mPlayer.bulletLifeEnd = true;
											//}
												mEnemyWave[i2] = null;
												//PlayerLaser = null;
												pointer[i2] = null;
									
									}
								});
								//final int i2 = i;
								
								}
							}
					  }
					 
					 
					 
					
				}
				
				//END PLAYER LASER TO ENEMY COLLISIONS
				
				
				
				//ENEMY MOVEMENT *TOWARD PLAYER*
				  for (int i = 0; i<totalEnemiesInPool; i++){
					
					  if (mEnemyWave[i]!=null){
						//ENEMY ON ENEMY CONTACT
						for (int y = 0; y<totalEnemiesInPool; y++){
							if (mEnemyWave[i]!=mEnemyWave[y] && mEnemyWave[y]!=null){
								if (mEnemyWave[i].collidesWith(mEnemyWave[y])){
										if(mEnemyWave[i].getY()>mEnemyWave[y].getY()){
											 mEnemyWave[y].setY(mEnemyWave[i].getY()-20);
										}
										else if(mEnemyWave[i].getY()<mEnemyWave[y].getY()){
											mEnemyWave[y].setY(mEnemyWave[i].getY()+20);
										}
										
									
								}
								}
							
						}
					//END ENEMY ON ENEMY CONTACT
				  						
					
					//Handle rotation of every sprite
					rotateX = mEnemyWave[i].getX() - mPlayer.getX();
					rotateY = mEnemyWave[i].getY() - mPlayer.getY();
		
				    rotationAngle = (float) Math.atan2(rotateX, rotateY);
					mEnemyWave[i].setRotation(MathUtils.radToDeg(rotationAngle)-180);
					//End
						// TODO Auto-generated method stub
						fromX = mEnemyWave[i].getX();
						toX = mPlayer.getX();
						fromY = mEnemyWave[i].getY();
						toY = mPlayer.getY();
						
						distance = MathUtils.distance(fromX, fromY, toX, toY);
						
						//update enemyX2 and enemyY2 outside the if statement for tighter laser firing from Enemy. (IE. doesn't fire the laser from the last target that was 250 distance from the player). Updates that distance. 
						//for firing laser at closest enemy
						enemyX2 = fromX;
						enemyY2 = fromY;
						//update closest enemy coordinates
						if ((distance < 250)){//autotargetting!
							//Log.v("","closest coordinates " + );
							dToPlayer = distance;
							dToPX = fromX;
							dToPY = fromY;
							//for firing laser at closest enemy
							enemyX = fromX;
							enemyY = fromY;
						}
						
						//This is if the enemy collides with the player
						entityModifierListener = new IEntityModifierListener() {
					
							//When the modifier starts, this method is called 
							@Override
							public void onModifierStarted(IModifier<IEntity> pModifier, final IEntity pItem){
							}
					
							//When the modifier finishes, this method is called
							@Override
							public void onModifierFinished(final IModifier<IEntity> pModifer, final IEntity pItem){
								
								//GameLevel.getInstance().mPlayer.dispose();
								ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
									@Override
									public void run() {
										mPlayer.detachSelf();
									}
								});
								
								ResourceManager.getInstance().engine.unregisterUpdateHandler(bulletTimerHandler);
								
								//ANIMATED SPRITE BEGINS
								//Create a new animated sprite in the center of the scene
								animatedSprite = new AnimatedSprite(mPlayer.getX(), mPlayer.getY(), ResourceManager.getInstance().mTiledTextureRegionExplosion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						
								//Length to play each frame before moving on to the next 
								long frameDuration[] = {75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75};
						
								// we can define the indices of the animation to play between 
								int firstTileIndex = 0;
								int lastTileIndex = ResourceManager.getInstance().mTiledTextureRegionExplosion.getTileCount()-1;
								
								Log.i("MODIFIER", Integer.toString(lastTileIndex));
						
								boolean loopAnimation = false;
						
								 //Animate the sprite with the data as set defined above 
								animatedSprite.animate(frameDuration, firstTileIndex, lastTileIndex, loopAnimation, new IAnimationListener() {
							
									@Override
									public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount){
										//Fired when the animation first begins to run 
										counter++;
									}
							
									@Override
									public void onAnimationFrameChanged(AnimatedSprite pAniamtedSprite, int pOldFrameIndex, int pNewIndex) {
										// Fired every time a new frame is selected to display 
									}
							
									@Override 
									public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount){
										// Fired when an animation loop ends (from first to last frame
										ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
											@Override
											public void run() {
												thisGameLevel.detachChild(animatedSprite);
											}
										});
										
										Log.v("","Gets past sprite animation");
										
									}
							
									@Override
									public void onAnimationFinished(AnimatedSprite pAnimatedSprite)
									{
										
										if (GameManager.getInstance().getLife() > 0){
											GameManager.getInstance().decrementLife(1);
										}
									}
									//ANIMATED SPRITE ENDS
								});
								thisGameLevel.attachChild(animatedSprite);
						
								//AFTER THIS PLAYER"S LIFE = 0, so game over, dump current scene, load game over text and quit to menu.
							}
						};
					
						
						
						
						//moveEnemy1.
						mEnemyWave[i].clearEntityModifiers();
						//mEnemyWave[i].unregisterEntityModifier(avoidCollideOtherEnemies);
						if (counter == 0){	
							//Log.v("","duration" + duration + "fromX" + fromX + "fromY" + fromY + "toX" + toX + "toY" + toY);
							moveEnemy1 = new MoveModifier(duration, fromX, fromY, toX, toY);
							mEnemyWave[i].registerEntityModifier(moveEnemy1);
							moveEnemy1.addModifierListener(entityModifierListener);
						}
						
					  }
				  }
				//END ENEMY MOVEMENT *TOWARD PLAYER*
				
				//BOSS MOVEMENT *RANDOM*
				
					
				  if (mBoss1!=null){					
					
					//Handle rotation of boss
					rotateX = mBoss1.getX() - mPlayer.getX();
					rotateY = mBoss1.getY() - mPlayer.getY();
		
				    rotationAngle = (float) Math.atan2(rotateX, rotateY);
					mBoss1.setRotation(MathUtils.radToDeg(rotationAngle)-180);
					//End
						// TODO Auto-generated method stub
						fromX = mBoss1.getX();
						toX = mPlayer.getX();
						fromY = mBoss1.getY();
						toY = mPlayer.getY();
						
						distance = MathUtils.distance(fromX, fromY, toX, toY);
						//Still Applies to Boss (this code is to help player fire at Boss
						//update enemyX2 and enemyY2 outside the if statement for tighter laser firing from Enemy. (IE. doesn't fire the laser from the last target that was 250 distance from the player). Updates that distance. 
						//for firing laser at closest enemy
						enemyX2 = fromX;
						enemyY2 = fromY;
						//update closest enemy coordinates
						if ((distance < 250)){//autotargetting!
							//Log.v("","closest coordinates " + );
							dToPlayer = distance;
							dToPX = fromX;
							dToPY = fromY;
							//for firing laser at closest enemy
							enemyX = fromX;
							enemyY = fromY;
						}
						//End Help Player fire at Boss by passing its coordinates
						//This is if the enemy collides with the player
						entityModifierListener = new IEntityModifierListener() {
					
							//When the modifier starts, this method is called 
							@Override
							public void onModifierStarted(IModifier<IEntity> pModifier, final IEntity pItem){
							}
					
							//When the modifier finishes, this method is called
							@Override
							public void onModifierFinished(final IModifier<IEntity> pModifer, final IEntity pItem){
								
								//GameLevel.getInstance().mPlayer.dispose();
								ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
									@Override
									public void run() {
										mPlayer.detachSelf();
									}
								});
								
								ResourceManager.getInstance().engine.unregisterUpdateHandler(bulletTimerHandler);
								
								//ANIMATED SPRITE BEGINS
								//Create a new animated sprite in the center of the scene
								animatedSprite = new AnimatedSprite(mPlayer.getX(), mPlayer.getY(), ResourceManager.getInstance().mTiledTextureRegionExplosion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						
								//Length to play each frame before moving on to the next 
								long frameDuration[] = {75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75};
						
								// we can define the indices of the animation to play between 
								int firstTileIndex = 0;
								int lastTileIndex = ResourceManager.getInstance().mTiledTextureRegionExplosion.getTileCount()-1;
								
								Log.i("MODIFIER", Integer.toString(lastTileIndex));
						
								boolean loopAnimation = false;
						
								 //Animate the sprite with the data as set defined above 
								animatedSprite.animate(frameDuration, firstTileIndex, lastTileIndex, loopAnimation, new IAnimationListener() {
							
									@Override
									public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount){
										//Fired when the animation first begins to run 
										counter++;
									}
							
									@Override
									public void onAnimationFrameChanged(AnimatedSprite pAniamtedSprite, int pOldFrameIndex, int pNewIndex) {
										// Fired every time a new frame is selected to display 
									}
							
									@Override 
									public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount){
										// Fired when an animation loop ends (from first to last frame
										ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
											@Override
											public void run() {
												thisGameLevel.detachChild(animatedSprite);
											}
										});
										
										Log.v("","Gets past sprite animation");
										
									}
							
									@Override
									public void onAnimationFinished(AnimatedSprite pAnimatedSprite)
									{
										
										SceneManager.getInstance().showMainMenu();
									}
									//ANIMATED SPRITE ENDS
								});
								thisGameLevel.attachChild(animatedSprite);
						
								//AFTER THIS PLAYER"S LIFE = 0, so game over, dump current scene, load game over text and quit to menu.
							}
						};
					
						
						
						
						//moveEnemy1.
						mBoss1.clearEntityModifiers();
						//mEnemyWave[i].unregisterEntityModifier(avoidCollideOtherEnemies);
						if (counter == 0){	
							//Log.v("","duration" + duration + "fromX" + fromX + "fromY" + fromY + "toX" + toX + "toY" + toY);
							moveBoss1 = new MoveModifier(duration, fromX, fromY, toX, toY);
							mBoss1.registerEntityModifier(moveBoss1);
							moveBoss1.addModifierListener(entityModifierListener);
						}
						
					  }
				  
				//END BOSS MOVEMENT *RANDOM*  
				  
				//PLAYER LASER TO BOSS LVL1 COLLISIONS
					if ((PlayerLaser!=null) && mBoss1!=null && BossReady){
						if ((PlayerLaser.collidesWith(mBoss1))){
					    	  //Log.v();
							if (ResourceManager.getInstance().mSound2 != null) {
								ResourceManager.getInstance().mSound2.play();
							}
							if (bossLife == 1){
								ResourceManager.getInstance().engine.unregisterUpdateHandler(mBoss1.getTimerHandler());
								if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1)==2){
									ResourceManager.getInstance().engine.unregisterUpdateHandler(mBoss1.getMoveHandler());
								}
								BossReady = false;
								mBoss1.clearEntityModifiers();
								mBoss1.clearUpdateHandlers();
								bossPointerLine = null;
								
									bossPointerWave.recyclePoolItem(bossPointer[0]);
									bossPointer[0].detachSelf();
									bossPointer[0] = null;
							
								ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
									@Override
									public void run() {
									//mEnemyWave[i2].bulletPool = null;
										if (ResourceManager.getInstance().mSound3 != null) {
											ResourceManager.getInstance().mSound3.play();
										}
										mBoss1.detachSelf();
										ResourceManager.getInstance().engine.unregisterUpdateHandler(bulletTimerHandler);
										//mPlayer.bulletPool.recyclePoolItem(PlayerLaser);								
										//if (GameLevel.getInstance().PlayerLaser != null){
											//GameLevel.getInstance().PlayerLaser.detachSelf();
											//mPlayer.recycleBullet = true;
											//mPlayer.bulletLifeEnd = true;
										//}
											mBoss1 = null;
											if (UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1)==5){
												UserData.getInstance(ResourceManager.getInstance().context).setLastLevelUnLocked(1,1);
												hud.attachChild(new Sprite(mScoreText.getX(), mScoreText.getY()-mScoreText.getHeight(), pGCTexture, ResourceManager.getInstance().engine.getVertexBufferObjectManager()));
												//particleSystem.
												mPlayer.detachSelf();
												particleSystem.detachSelf();
												boundBox.detachSelf();
												Timer t = new Timer();
												t.schedule(new TimerTask() {
													
													@Override
													public void run() {
														if(SceneManager.getInstance().mCurrentScene.getClass().getGenericSuperclass().equals(ManagedGameScene.class) || 
																(SceneManager.getInstance().mCurrentScene.getClass().getGenericSuperclass().equals(ManagedMenuScene.class) &!
																	SceneManager.getInstance().mCurrentScene.getClass().equals(MainMenu.class))){
															ResourceManager.getInstance().engine.unregisterUpdateHandler(GameLevel.getInstance().bulletTimerHandler);
															
															//ResourceManager.getInstance().engine.unregisterUpdateHandler(GameLevel.getInstance().);
															//MainMenu.getInstance().detachSelf();
															//new MainMenu();
															//thisGameLevel.detachChildren();
															SceneManager.getInstance().showMainMenu();
															gameCamera.setBounds(0,0,ResourceManager.getInstance().cameraWidth,ResourceManager.getInstance().cameraHeight);
														}
													}
												}, 5000);
												
											} else {
												UserData.getInstance(ResourceManager.getInstance().context).setLastLevelUnLocked(1,Level+1);
												LevelLoaded = false;
												Level = UserData.getInstance(ResourceManager.getInstance().context).lastLevelUnLocked(1);
												//thisGameLevel.clearUpdateHandlers();
												
												//SceneManager.getInstance().showMainMenu();
												//SceneManager.getInstance().showMainMenu();
												//SceneManager.getInstance().showScene(GameLevel.getInstance());
											}
											//Add a timer here with a message telling the player he's going to level 2.
											
											
											//SceneManager.getInstance().showMainMenu();
											//PlayerLaser = null;
									}
								});
								}
							//PLAYER SCORING
							//GameManager.getInstance().decrementEnemyCount();
							//if (GameManager.getInstance().isEnemyDown()) {
							ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									//mEnemyWave[i2].bulletPool = null;
									//mEnemyWave[i2].detachSelf();
										
										
										if (GameLevel.getInstance().PlayerLaser != null){
											GameLevel.getInstance().PlayerLaser.detachSelf();
											mPlayer.bulletPool.recyclePoolItem(PlayerLaser);
											mPlayer.recycleBullet = true;
											mPlayer.bulletLifeEnd = true;
										}
											//mEnemyWave[i2] = null;
											PlayerLaser = null;
										
									
								}
							});	
							
								ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
									@Override
									public void run() {
										//Log.i("MODIFIER", "Enemy Killed");
										bossLife -= 1;
										GameManager.getInstance().incrementScore(5);
										hud.detachChild(mScoreText);
										mScoreText = new Text((ResourceManager.getInstance().cameraWidth/2f), ResourceManager.getInstance().cameraHeight - ResourceManager.getInstance().mFont.getLineHeight(), ResourceManager.getInstance().mFont, SCORE_STRING_PREFIX + GameManager.getInstance().getCurrentScore(), MAX_CHARACTER_COUNT, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
										hud.attachChild(mScoreText);
									}
								}); 
							//}
							//END PLAYER SCORING
							
							
							
							//ANIMATED SPRITE BEGINS
							//Create a new animated sprite in the center of the scene
							animatedSprite = new AnimatedSprite(mBoss1.getX(), mBoss1.getY(), ResourceManager.getInstance().mTiledTextureRegionExplosion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
					
							//Length to play each frame before moving on to the next 
							long frameDuration[] = {75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75};
					
							// we can define the indices of the animation to play between 
							int firstTileIndex = 0;
							int lastTileIndex = ResourceManager.getInstance().mTiledTextureRegionExplosion.getTileCount()-1;

							boolean loopAnimation = false;
						
							 //Animate the sprite with the data as set defined above 
							animatedSprite.animate(frameDuration, firstTileIndex, lastTileIndex, loopAnimation, new IAnimationListener() {
						
								@Override
								public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount){
									//Fired when the animation first begins to run 
									//counter++;
								}
						
								@Override
								public void onAnimationFrameChanged(AnimatedSprite pAniamtedSprite, int pOldFrameIndex, int pNewIndex) {
									// Fired every time a new frame is selected to display 
								}
						
								@Override 
								public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount){
									// Fired when an animation loop ends (from first to last frame
									ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
										@Override
										public void run() {
											thisGameLevel.detachChild(animatedSprite);
											
										}
									});
									Log.v("","Gets past sprite animation");
									
								}
						
								@Override
								public void onAnimationFinished(AnimatedSprite pAnimatedSprite)
								{
									
									//SceneManager.getInstance().showMainMenu();
								}
								//ANIMATED SPRITE ENDS
							});
							thisGameLevel.attachChild(animatedSprite);
							
						}
					}
				
				
				//END PLAYER LASER TO BOSS LVL 1 COLLISIONS
			 }
				
			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
	
		});
		
				
	
		
	}
	
	
	@Override
	public void onHideScene() {
		// TODO Auto-generated method stub
		super.onHideScene();
	}
	@Override
	public void onShowScene() {
		// TODO Auto-generated method stub
		super.onShowScene();
		
		
	}


	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		if (PlayerReady){
			touchX = pSceneTouchEvent.getX(); 
			touchY = pSceneTouchEvent.getY();
		} else {
			touchX = 100;
			touchY = 100;
		}
		return false;
	}


}
