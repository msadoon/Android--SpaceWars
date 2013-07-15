package mkat.apps.spacewars;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

public class ResourceManager {

	//====================================================
	// CONSTANTS
	//====================================================
	private static ResourceManager INSTANCE;
	//Music On/Off
	public int MUTE = 1;
	public int UNMUTE = 0; //should be one
	//====================================================
	// VARIABLES
	//====================================================
	public Engine engine;
	public Context context;
	public float cameraWidth;
	public float cameraHeight;
	public float cameraScaleFactorX;
	public float cameraScaleFactorY;
	
	// ======================== Game Resources ================= //
	// ======================== Menu Resources ================= //
	// =================== Shared Game and Menu Resources ====== //
	public ITexture mFontTexture, mFontTextureM;
	public ITextureRegion mGameBackgroundTextureRegion, mGameBackgroundTextureRegion2, mGameBackgroundTextureRegion3, mGameBackgroundTextureRegion4, mGameBackgroundTextureRegion5, mMenuBackgroundTextureRegion;
	public ITiledTextureRegion mMenuBackgroundNewGame, mMenuBackgroundResume, mMenuBackgroundCredits, mMenuBackgroundSound, mMenuBackgroundSoundM;
	public ITextureRegion mCreditsLayer, mTitle, mPlayerTextureRegion, mHealthPointer, mBossPointer, mEnemyPointer, mPause, mPlay, mPlayerLifeTextureRegion, mGCTextureRegion, mGameOverTextureRegion, mEnemy1TextureRegion5, mEnemy2TextureRegion5, mEnemy3TextureRegion5, mEnemy4TextureRegion5, mEnemy5TextureRegion5, mEnemy1TextureRegion4, mEnemy2TextureRegion4, mEnemy3TextureRegion4, mEnemy4TextureRegion4, mEnemy5TextureRegion4, mEnemy1TextureRegion, mEnemy1TextureRegion3, mEnemy2TextureRegion3, mEnemy3TextureRegion3, mEnemy4TextureRegion3, mEnemy5TextureRegion3, mEnemy1TextureRegion2, mEnemy2TextureRegion2, mEnemy3TextureRegion2, mEnemy4TextureRegion2, mEnemy5TextureRegion2, mEnemy2TextureRegion, mEnemy3TextureRegion, mEnemy4TextureRegion, mEnemy5TextureRegion, mEnemyLaserTextureRegion, mBoss1TextureRegion, mBoss1TextureRegion2, mBoss1TextureRegion3, mBoss1TextureRegion4, mBoss1TextureRegion5, mBossLaserTextureRegion,mBossLaser2TextureRegion, mFPPTextureRegion, mFRPTextureRegion, mHealthTextureRegion, mPlayerLaserTextureRegion, mPlayerLaserTextureRegion2,mWave1TextureRegion,mWave2TextureRegion, mWave3TextureRegion, mWave4TextureRegion, mWave5TextureRegion, mLevel1TextureRegion, mLevel2TextureRegion, mLevel3TextureRegion, mLevel4TextureRegion,mLevel5TextureRegion, mBossMsgTextureRegion, mEnvFlames;
	public TiledTextureRegion mTiledTextureRegionExplosion;
	public Sound mSound1, mSound2, mSound3, mSound4, mSoundMenu;
	public Music mMusic, mMusiclvl1, mMusiclvl2, mMusiclvl3, mMusiclvl4, mMusiclvl5;
	public Font mFont, mFontM;
	public int counter, counter2 = 0;//help to check if the menu resources have been loaded/not
	// This variable will be used to revert the TextureFactory's default path when we change it.
	private String mPreviousAssetBasePath = "";
		
	//====================================================
	// CONSTRUCTOR
	//====================================================
	ResourceManager(){
		//empty constructor b/c singleton
	}
	
	//====================================================
	// GETTERS & SETTERS
	//====================================================
	// Retrieves a global instance of the ResourceManager
	public synchronized static ResourceManager getInstance(){
			if (INSTANCE == null){
				INSTANCE = new ResourceManager();
			}
			return INSTANCE;
	}
	
	//====================================================
	// PUBLIC METHODS
	//====================================================
	// Setup the ResourceManager
	public void setup(final Engine pEngine, final Context pContext, final float pCameraWidth, final float pCameraHeight, final float pCameraScaleX, final float pCameraScaleY){
			engine = pEngine;
			context = pContext;
			cameraWidth = pCameraWidth;
			cameraHeight = pCameraHeight;
			cameraScaleFactorX = pCameraScaleX;
			cameraScaleFactorY = pCameraScaleY;
	}
	
	//TEXTURES!
	
	//Load Level 1 Textures
	public synchronized void loadLevel1Textures(Engine pEngine, Context pContext) {
		// Set our game assets folder in "assets/gfx/game/"
		//LEVEL1
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/level1/");
		//Background BitmapTextureAtlas
		BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(pEngine.getTextureManager(), 2048, 2048, BitmapTextureFormat.RGB_565);
		
		mGameBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, pContext, "backgrounda.png", 10, 10);
				
		//Player, enemies, boss, lasers, powerups, health
		BuildableBitmapTextureAtlas mBuildableBitmapTextureAtlas = new BuildableBitmapTextureAtlas(pEngine.getTextureManager(), 1024, 1024, BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
	    mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "player.png");
		mEnemy1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "enemy1a.png");
		mEnemy2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "enemy2a.png");
		mEnemy3TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "enemy3a.png");
		mEnemy4TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "enemy4a.png");
		mEnemy5TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "enemy5a.png");
		mEnemyLaserTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "enemy_laser.png");
		mBoss1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "boss1a.png");
		mBossLaserTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "boss_laser.png");
		mBossLaser2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "boss_laser2.png");
		mFPPTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "fire_power_powerup.png");
		mFRPTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "fire_rate_powerup.png");
		mHealthTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "health_pickup.png");
		mPlayerLaserTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "player_laser.png");
		mPlayerLaserTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "player_laser_2.png");
		mHealthPointer = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "health_arrow.png");
		mEnemyPointer = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "enemy_arrow.png");
		mBossPointer = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "bossarrow.png");
		try {
			mBuildableBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			
		}catch (TextureAtlasBuilderException e){
			e.printStackTrace();
		}
		//END LEVEL1
		BuildableBitmapTextureAtlas mBitmapTextureAtlasExplosion = new BuildableBitmapTextureAtlas(pEngine.getTextureManager(), 512, 192, TextureOptions.BILINEAR);
		mTiledTextureRegionExplosion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlasExplosion, pContext, "destroyed_explosion.png", 8, 3);
		
		
		try{
			mBitmapTextureAtlasExplosion.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
		}catch (TextureAtlasBuilderException e){
			e.printStackTrace();
		}
		//LEVEL2
				BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/level2/");
				//Background BitmapTextureAtlas
				BitmapTextureAtlas mBitmapTextureAtlas2 = new BitmapTextureAtlas(pEngine.getTextureManager(), 2048, 2048, BitmapTextureFormat.RGB_565);
				
				mGameBackgroundTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas2, pContext, "backgroundb.png", 10, 10);
						
				//Player, enemies, boss, lasers, powerups, health
				BuildableBitmapTextureAtlas mBuildableBitmapTextureAtlas2 = new BuildableBitmapTextureAtlas(pEngine.getTextureManager(), 1024, 1024, BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
				mEnemy1TextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas2, pContext, "enemy1b.png");
				mEnemy2TextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas2, pContext, "enemy2b.png");
				mEnemy3TextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas2, pContext, "enemy3b.png");
				mEnemy4TextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas2, pContext, "enemy4b.png");
				mEnemy5TextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas2, pContext, "enemy5b.png");
				mBoss1TextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas2, pContext, "boss1b.png");
				
				try {
					mBuildableBitmapTextureAtlas2.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
					
				}catch (TextureAtlasBuilderException e){
					e.printStackTrace();
				}
		//END LEVEL2
		
		//LEVEL3
				BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/level3/");
				//Background BitmapTextureAtlas
				BitmapTextureAtlas mBitmapTextureAtlas3 = new BitmapTextureAtlas(pEngine.getTextureManager(), 2048, 2048, BitmapTextureFormat.RGB_565);
				
				mGameBackgroundTextureRegion3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas3, pContext, "backgroundc.png", 10, 10);
						
				//Player, enemies, boss, lasers, powerups, health
				BuildableBitmapTextureAtlas mBuildableBitmapTextureAtlas3 = new BuildableBitmapTextureAtlas(pEngine.getTextureManager(), 1024, 1024, BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
				mEnemy1TextureRegion3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas3, pContext, "enemy1c.png");
				mEnemy2TextureRegion3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas3, pContext, "enemy2c.png");
				mEnemy3TextureRegion3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas3, pContext, "enemy3c.png");
				mEnemy4TextureRegion3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas3, pContext, "enemy4c.png");
				mEnemy5TextureRegion3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas3, pContext, "enemy5c.png");
				mBoss1TextureRegion3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas3, pContext, "boss1c.png");
				
				try {
					mBuildableBitmapTextureAtlas3.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
					
				}catch (TextureAtlasBuilderException e){
					e.printStackTrace();
				}
		//END LEVEL3
		
				//LEVEL4
				BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/level4/");
				//Background BitmapTextureAtlas
				BitmapTextureAtlas mBitmapTextureAtlas4 = new BitmapTextureAtlas(pEngine.getTextureManager(), 2048, 2048, BitmapTextureFormat.RGB_565);
				
				mGameBackgroundTextureRegion4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas4, pContext, "backgroundd.png", 10, 10);
						
				//Player, enemies, boss, lasers, powerups, health
				BuildableBitmapTextureAtlas mBuildableBitmapTextureAtlas4 = new BuildableBitmapTextureAtlas(pEngine.getTextureManager(), 1024, 1024, BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
				mEnemy1TextureRegion4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas4, pContext, "enemy1d.png");
				mEnemy2TextureRegion4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas4, pContext, "enemy2d.png");
				mEnemy3TextureRegion4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas4, pContext, "enemy3d.png");
				mEnemy4TextureRegion4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas4, pContext, "enemy4d.png");
				mEnemy5TextureRegion4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas4, pContext, "enemy5d.png");
				mBoss1TextureRegion4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas4, pContext, "boss1d.png");
			
				try {
					mBuildableBitmapTextureAtlas4.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
					
				}catch (TextureAtlasBuilderException e){
					e.printStackTrace();
				}
		//END LEVEL4		
		
				//LEVEL5
				BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/level5/");
				//Background BitmapTextureAtlas
				BitmapTextureAtlas mBitmapTextureAtlas5 = new BitmapTextureAtlas(pEngine.getTextureManager(), 2048, 2048, BitmapTextureFormat.RGB_565);
				
				mGameBackgroundTextureRegion5 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas5, pContext, "backgrounde.png", 10, 10);
						
				//Player, enemies, boss, lasers, powerups, health
				BuildableBitmapTextureAtlas mBuildableBitmapTextureAtlas5 = new BuildableBitmapTextureAtlas(pEngine.getTextureManager(), 1024, 1024, BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
				mEnemy1TextureRegion5 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas5, pContext, "enemy1e.png");
				mEnemy2TextureRegion5 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas5, pContext, "enemy2e.png");
				mEnemy3TextureRegion5 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas5, pContext, "enemy3e.png");
				mEnemy4TextureRegion5 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas5, pContext, "enemy4e.png");
				mEnemy5TextureRegion5 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas5, pContext, "enemy5e.png");
				mBoss1TextureRegion5 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas5, pContext, "boss1e.png");
				mEnvFlames = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas5, pContext, "env_flames.png");
				try {
					mBuildableBitmapTextureAtlas5.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
					
				}catch (TextureAtlasBuilderException e){
					e.printStackTrace();
				}
		//END LEVEL5			
				
		//512 192
		//Destroy Explosion sprite sheet
		
		
		mBitmapTextureAtlas.load(); //loads background
		mBitmapTextureAtlas2.load();
		mBitmapTextureAtlas3.load();
		mBitmapTextureAtlas4.load();
		mBitmapTextureAtlas5.load();
		mBuildableBitmapTextureAtlas.load(); //loads gametextures except explosion
		mBuildableBitmapTextureAtlas2.load();
		mBuildableBitmapTextureAtlas3.load();
		mBuildableBitmapTextureAtlas4.load();
		mBuildableBitmapTextureAtlas5.load();
		mBitmapTextureAtlasExplosion.load(); //load explosion sprite sheet
		// Revert the Asset Path.
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(mPreviousAssetBasePath);
		counter2++;
	}

	
	//Load HUD Textures
	public synchronized void loadHUDTextures(Engine pEngine, Context pContext) {
		// Set our game assets folder in "assets/gfx/game/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/HUD/");
			
		//Player, enemies, boss, lasers, powerups, health
		BuildableBitmapTextureAtlas mBuildableBitmapTextureAtlas = new BuildableBitmapTextureAtlas(pEngine.getTextureManager(), 1024, 1024, BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
	    mPlayerLifeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "life.png");
	    mGameOverTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "gameover.png");
	    mWave1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "wave1.png");
	    mWave2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "wave2.png");
	    mWave3TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "wave3.png");
	    mWave4TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "wave4.png");
	    mWave5TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "wave5.png");
	    mLevel1TextureRegion  = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "level1.png");
	    mLevel2TextureRegion  = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "level2.png");
	    mLevel3TextureRegion  = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "level3.png");
	    mLevel4TextureRegion  = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "level4.png");
	    mLevel5TextureRegion  = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "level5.png");
	    mBossMsgTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "boss.png");
		mGCTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "game_complete.png");
		mPause = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "pause.png");
		mPlay = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBuildableBitmapTextureAtlas, pContext, "play.png");
		
	    try {
			mBuildableBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			
		}catch (TextureAtlasBuilderException e){
			e.printStackTrace();
		}
		
		mBuildableBitmapTextureAtlas.load(); //loads gametextures except explosion
		
		// Revert the Asset Path.
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(mPreviousAssetBasePath);
		counter2++;
	}
	
	//Unload Level 1 Textures
		public synchronized void unloadHUDTextures(){
			
			if(mPause!=null) {
				if(mPause.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mPause.getTexture().unload();
					mPause = null;
				}
			}
			if(mPlay!=null) {
				if(mPlay.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mPlay.getTexture().unload();
					mPlay = null;
				}
			}
			if(mLevel1TextureRegion!=null) {
				if(mLevel1TextureRegion.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mLevel1TextureRegion.getTexture().unload();
					mLevel1TextureRegion = null;
				}
			}
			
			if(mGCTextureRegion!=null) {
				if(mGCTextureRegion.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mGCTextureRegion.getTexture().unload();
					mGCTextureRegion = null;
				}
			}
			
			if(mLevel2TextureRegion!=null) {
				if(mLevel2TextureRegion.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mLevel2TextureRegion.getTexture().unload();
					mLevel2TextureRegion = null;
				}
			}
			if(mLevel3TextureRegion!=null) {
				if(mLevel3TextureRegion.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mLevel3TextureRegion.getTexture().unload();
					mLevel3TextureRegion = null;
				}
			}
			if(mLevel4TextureRegion!=null) {
				if(mLevel4TextureRegion.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mLevel4TextureRegion.getTexture().unload();
					mLevel4TextureRegion = null;
				}
			}
			if(mLevel5TextureRegion!=null) {
				if(mLevel5TextureRegion.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mLevel5TextureRegion.getTexture().unload();
					mLevel5TextureRegion = null;
				}
			}
			if(mPlayerLifeTextureRegion!=null) {
				if(mPlayerLifeTextureRegion.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mPlayerLifeTextureRegion.getTexture().unload();
					mPlayerLifeTextureRegion = null;
				}
			}
			if(mBossMsgTextureRegion!=null) {
				if(mBossMsgTextureRegion.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mBossMsgTextureRegion.getTexture().unload();
					mBossMsgTextureRegion = null;
				}
			}
			if(mGameOverTextureRegion!=null) {
				if(mGameOverTextureRegion.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mGameOverTextureRegion.getTexture().unload();
					mGameOverTextureRegion = null;
				}
			}
			if(mWave1TextureRegion!=null) {
				if(mWave1TextureRegion.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mWave1TextureRegion.getTexture().unload();
					mWave1TextureRegion = null;
				}
			}
			if(mWave2TextureRegion!=null) {
				if(mWave2TextureRegion.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mWave2TextureRegion.getTexture().unload();
					mWave2TextureRegion = null;
				}
			}
			if(mWave3TextureRegion!=null) {
				if(mWave3TextureRegion.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mWave3TextureRegion.getTexture().unload();
					mWave3TextureRegion = null;
				}
			}
			if(mWave4TextureRegion!=null) {
				if(mWave4TextureRegion.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mWave4TextureRegion.getTexture().unload();
					mWave4TextureRegion = null;
				}
			}
			if(mWave5TextureRegion!=null) {
				if(mWave5TextureRegion.getTexture().isLoadedToHardware()) {
					//Log.v("","game background unloaded from hardware");
					mWave5TextureRegion.getTexture().unload();
					mWave5TextureRegion = null;
				}
			}
			
			counter2--;
		}
	
	//Unload Level 1 Textures
	public synchronized void unloadLevel1Textures(){
		
		if(mGameBackgroundTextureRegion!=null) {
			if(mGameBackgroundTextureRegion.getTexture().isLoadedToHardware()) {
				//Log.v("","game background unloaded from hardware");
				mGameBackgroundTextureRegion.getTexture().unload();
				mGameBackgroundTextureRegion = null;
			}
		}
		
		if(mHealthPointer!=null) {
			if(mHealthPointer.getTexture().isLoadedToHardware()) {
				//Log.v("","game background unloaded from hardware");
				mHealthPointer.getTexture().unload();
				mHealthPointer = null;
			}
		}
		
		if(mEnemyPointer!=null) {
			if(mEnemyPointer.getTexture().isLoadedToHardware()) {
				//Log.v("","game background unloaded from hardware");
				mEnemyPointer.getTexture().unload();
				mEnemyPointer = null;
			}
		}
		
		if(mBossPointer!=null) {
			if(mBossPointer.getTexture().isLoadedToHardware()) {
				//Log.v("","game background unloaded from hardware");
				mBossPointer.getTexture().unload();
				mBossPointer = null;
			}
		}
		
		if(mEnvFlames!=null) {
			if(mEnvFlames.getTexture().isLoadedToHardware()) {
				//Log.v("","game background unloaded from hardware");
				mEnvFlames.getTexture().unload();
				mEnvFlames = null;
			}
		}
		
		if(mGameBackgroundTextureRegion2!=null) {
			if(mGameBackgroundTextureRegion2.getTexture().isLoadedToHardware()) {
				//Log.v("","game background unloaded from hardware");
				mGameBackgroundTextureRegion2.getTexture().unload();
				mGameBackgroundTextureRegion2 = null;
			}
		}
		
		if(mGameBackgroundTextureRegion3!=null) {
			if(mGameBackgroundTextureRegion3.getTexture().isLoadedToHardware()) {
				//Log.v("","game background unloaded from hardware");
				mGameBackgroundTextureRegion3.getTexture().unload();
				mGameBackgroundTextureRegion3 = null;
			}
		}
		
		if(mGameBackgroundTextureRegion4!=null) {
			if(mGameBackgroundTextureRegion4.getTexture().isLoadedToHardware()) {
				//Log.v("","game background unloaded from hardware");
				mGameBackgroundTextureRegion4.getTexture().unload();
				mGameBackgroundTextureRegion4 = null;
			}
		}
		
		if(mGameBackgroundTextureRegion5!=null) {
			if(mGameBackgroundTextureRegion5.getTexture().isLoadedToHardware()) {
				//Log.v("","game background unloaded from hardware");
				mGameBackgroundTextureRegion5.getTexture().unload();
				mGameBackgroundTextureRegion5 = null;
			}
		}
		
		if(mPlayerTextureRegion!=null) {
			if(mPlayerTextureRegion.getTexture().isLoadedToHardware()) {
				//Log.v("","player texture unloaded from hardware");
				mPlayerTextureRegion.getTexture().unload();
				mPlayerTextureRegion = null;
			}
		}
		
		if(mEnemy1TextureRegion!=null) {
			if(mEnemy1TextureRegion.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy texture unloaded from hardware");
				mEnemy1TextureRegion.getTexture().unload();
				mEnemy1TextureRegion = null;
			}
		}
		
		if(mEnemy2TextureRegion!=null) {
			if(mEnemy2TextureRegion.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy2 texture unloaded from hardware");
				mEnemy2TextureRegion.getTexture().unload();
				mEnemy2TextureRegion = null;
			}
		}
		
		if(mEnemy3TextureRegion!=null) {
			if(mEnemy3TextureRegion.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy 3 texture unloaded from hardware");
				mEnemy3TextureRegion.getTexture().unload();
				mEnemy3TextureRegion = null;
			}
		}
		
		if(mEnemy4TextureRegion!=null) {
			if(mEnemy4TextureRegion.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy 4 texture unloaded from hardware");
				mEnemy4TextureRegion.getTexture().unload();
				mEnemy4TextureRegion = null;
			}
		}
		
		if(mEnemy5TextureRegion!=null) {
			if(mEnemy5TextureRegion.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy 5 texture unloaded from hardware");
				mEnemy5TextureRegion.getTexture().unload();
				mEnemy5TextureRegion = null;
			}
		}
		
		if(mEnemy1TextureRegion4!=null) {
			if(mEnemy1TextureRegion4.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy texture unloaded from hardware");
				mEnemy1TextureRegion4.getTexture().unload();
				mEnemy1TextureRegion4 = null;
			}
		}
		
		if(mEnemy2TextureRegion4!=null) {
			if(mEnemy2TextureRegion4.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy2 texture unloaded from hardware");
				mEnemy2TextureRegion4.getTexture().unload();
				mEnemy2TextureRegion4 = null;
			}
		}
		
		if(mEnemy3TextureRegion4!=null) {
			if(mEnemy3TextureRegion4.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy 3 texture unloaded from hardware");
				mEnemy3TextureRegion4.getTexture().unload();
				mEnemy3TextureRegion4 = null;
			}
		}
		
		if(mEnemy4TextureRegion4!=null) {
			if(mEnemy4TextureRegion4.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy 4 texture unloaded from hardware");
				mEnemy4TextureRegion4.getTexture().unload();
				mEnemy4TextureRegion4 = null;
			}
		}
		
		if(mEnemy5TextureRegion4!=null) {
			if(mEnemy5TextureRegion4.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy 5 texture unloaded from hardware");
				mEnemy5TextureRegion4.getTexture().unload();
				mEnemy5TextureRegion4 = null;
			}
		}
		
		if(mEnemy1TextureRegion5!=null) {
			if(mEnemy1TextureRegion5.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy texture unloaded from hardware");
				mEnemy1TextureRegion5.getTexture().unload();
				mEnemy1TextureRegion5 = null;
			}
		}
		
		if(mEnemy2TextureRegion5!=null) {
			if(mEnemy2TextureRegion5.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy2 texture unloaded from hardware");
				mEnemy2TextureRegion5.getTexture().unload();
				mEnemy2TextureRegion5 = null;
			}
		}
		
		if(mEnemy3TextureRegion5!=null) {
			if(mEnemy3TextureRegion5.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy 3 texture unloaded from hardware");
				mEnemy3TextureRegion5.getTexture().unload();
				mEnemy3TextureRegion5 = null;
			}
		}
		
		if(mEnemy4TextureRegion5!=null) {
			if(mEnemy4TextureRegion5.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy 4 texture unloaded from hardware");
				mEnemy4TextureRegion5.getTexture().unload();
				mEnemy4TextureRegion5 = null;
			}
		}
		
		if(mEnemy5TextureRegion5!=null) {
			if(mEnemy5TextureRegion5.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy 5 texture unloaded from hardware");
				mEnemy5TextureRegion5.getTexture().unload();
				mEnemy5TextureRegion5 = null;
			}
		}
		
		if(mEnemy1TextureRegion3!=null) {
			if(mEnemy1TextureRegion3.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy texture unloaded from hardware");
				mEnemy1TextureRegion3.getTexture().unload();
				mEnemy1TextureRegion3 = null;
			}
		}
		
		if(mEnemy2TextureRegion3!=null) {
			if(mEnemy2TextureRegion3.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy texture unloaded from hardware");
				mEnemy2TextureRegion3.getTexture().unload();
				mEnemy2TextureRegion3 = null;
			}
		}
		
		if(mEnemy3TextureRegion3!=null) {
			if(mEnemy3TextureRegion3.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy texture unloaded from hardware");
				mEnemy3TextureRegion3.getTexture().unload();
				mEnemy3TextureRegion3 = null;
			}
		}
		
		if(mEnemy4TextureRegion3!=null) {
			if(mEnemy4TextureRegion3.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy texture unloaded from hardware");
				mEnemy4TextureRegion3.getTexture().unload();
				mEnemy4TextureRegion3 = null;
			}
		}
		
		if(mEnemy5TextureRegion3!=null) {
			if(mEnemy5TextureRegion3.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy texture unloaded from hardware");
				mEnemy5TextureRegion3.getTexture().unload();
				mEnemy5TextureRegion3 = null;
			}
		}
		
		if(mEnemy1TextureRegion2!=null) {
			if(mEnemy1TextureRegion2.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy2 texture unloaded from hardware");
				mEnemy1TextureRegion2.getTexture().unload();
				mEnemy1TextureRegion2 = null;
			}
		}
		
		if(mEnemy2TextureRegion2!=null) {
			if(mEnemy2TextureRegion2.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy2 texture unloaded from hardware");
				mEnemy2TextureRegion2.getTexture().unload();
				mEnemy2TextureRegion2 = null;
			}
		}
		
		if(mEnemy3TextureRegion2!=null) {
			if(mEnemy3TextureRegion2.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy 3 texture unloaded from hardware");
				mEnemy3TextureRegion2.getTexture().unload();
				mEnemy3TextureRegion2 = null;
			}
		}
		
		if(mEnemy4TextureRegion2!=null) {
			if(mEnemy4TextureRegion2.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy 4 texture unloaded from hardware");
				mEnemy4TextureRegion2.getTexture().unload();
				mEnemy4TextureRegion2 = null;
			}
		}
		
		if(mEnemy5TextureRegion2!=null) {
			if(mEnemy5TextureRegion2.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy 5 texture unloaded from hardware");
				mEnemy5TextureRegion2.getTexture().unload();
				mEnemy5TextureRegion2 = null;
			}
		}
		
		if(mEnemyLaserTextureRegion!=null) {
			if(mEnemyLaserTextureRegion.getTexture().isLoadedToHardware()) {
				//Log.v("","enemy laser texture unloaded from hardware");
				mEnemyLaserTextureRegion.getTexture().unload();
				mEnemyLaserTextureRegion = null;
			}
		}
		
		if(mBoss1TextureRegion!=null) {
			if(mBoss1TextureRegion.getTexture().isLoadedToHardware()) {
				//Log.v("","boss1 texture unloaded from hardware");
				mBoss1TextureRegion.getTexture().unload();
				mBoss1TextureRegion = null;
			}
		}
		
		if(mBoss1TextureRegion2!=null) {
			if(mBoss1TextureRegion2.getTexture().isLoadedToHardware()) {
				//Log.v("","boss1 texture unloaded from hardware");
				mBoss1TextureRegion2.getTexture().unload();
				mBoss1TextureRegion2 = null;
			}
		}
		
		if(mBossLaserTextureRegion!=null) {
			if(mBossLaserTextureRegion.getTexture().isLoadedToHardware()) {
				//Log.v("","bosslaser  texture unloaded from hardware");
				mBossLaserTextureRegion.getTexture().unload();
				mBossLaserTextureRegion = null;
			}
		}
		if(mBossLaser2TextureRegion!=null) {
			if(mBossLaser2TextureRegion.getTexture().isLoadedToHardware()) {
				//Log.v("","bosslaser  texture unloaded from hardware");
				mBossLaser2TextureRegion.getTexture().unload();
				mBossLaser2TextureRegion = null;
			}
		}
		if(mFRPTextureRegion!=null) {
			if(mFRPTextureRegion.getTexture().isLoadedToHardware()) {
				//Log.v("","FRP texture unloaded from hardware");
				mFRPTextureRegion.getTexture().unload();
				mFRPTextureRegion = null;
			}
		}
		
		if(mFPPTextureRegion!=null) {
			if(mFPPTextureRegion.getTexture().isLoadedToHardware()) {
				//Log.v("","FPP texture unloaded from hardware");
				mFPPTextureRegion.getTexture().unload();
				mFPPTextureRegion = null;
			}
		}
		
		if(mHealthTextureRegion!=null) {
			if(mHealthTextureRegion.getTexture().isLoadedToHardware()) {
				//Log.v("","Health texture unloaded from hardware");
				mHealthTextureRegion.getTexture().unload();
				mHealthTextureRegion = null;
			}
		}
		
		if(mPlayerLaserTextureRegion!=null) {
			if(mPlayerLaserTextureRegion.getTexture().isLoadedToHardware()) {
				//Log.v("","Player Laser texture unloaded from hardware");
				mPlayerLaserTextureRegion.getTexture().unload();
				mPlayerLaserTextureRegion = null;
			}
		}
		
		if(mPlayerLaserTextureRegion2!=null) {
			if(mPlayerLaserTextureRegion2.getTexture().isLoadedToHardware()) {
				//Log.v("","Player Laser texture unloaded from hardware");
				mPlayerLaserTextureRegion2.getTexture().unload();
				mPlayerLaserTextureRegion2 = null;
			}
		}
		
		if(mTiledTextureRegionExplosion!=null) {
			if(mTiledTextureRegionExplosion.getTexture().isLoadedToHardware()) {
				//Log.v("","Explosion texture unloaded from hardware");
				mTiledTextureRegionExplosion.getTexture().unload();
				mTiledTextureRegionExplosion = null;
			}
		}
		counter2--;
	}
	
	//Load Menu Textures
		public synchronized void loadMenuTextures(Engine pEngine, Context pContext){
			//menu background
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
			BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(pEngine.getTextureManager(), 1024, 1024, BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
			mMenuBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, pContext, "menu_bgmg.png", 10, 10);
			
			//BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
			BitmapTextureAtlas mBitmapTextureAtlas2 = new BitmapTextureAtlas(pEngine.getTextureManager(), 1024, 1024, BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
			mCreditsLayer = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas2, pContext, "credits_layer.png", 10, 10);
			mTitle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas2, pContext, "title.png", 500, 500);
			
			//menu icons
			BuildableBitmapTextureAtlas mBuildableBitmapTextureAtlas2 = new BuildableBitmapTextureAtlas(pEngine.getTextureManager(), 1024, 1024, BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
			mMenuBackgroundNewGame = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBuildableBitmapTextureAtlas2, pContext, "newgame_spritesheet.png", 2, 1);
			
			mMenuBackgroundResume = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBuildableBitmapTextureAtlas2, pContext, "resume_spritesheet.png", 2, 1);
			mMenuBackgroundCredits = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBuildableBitmapTextureAtlas2, pContext, "credits_spritesheet.png", 2, 1);
			mMenuBackgroundSoundM = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBuildableBitmapTextureAtlas2, pContext, "soundMode_sprite_sheet.png", 2, 1);
			
			try{
				mBuildableBitmapTextureAtlas2.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			}catch (TextureAtlasBuilderException e){
				e.printStackTrace();
			}
			
			//load Menu Resources
			mBitmapTextureAtlas.load();
			mBitmapTextureAtlas2.load();
			mBuildableBitmapTextureAtlas2.load();
			
			// Revert the Asset Path.
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(mPreviousAssetBasePath);
			counter++;
		}
		
		public synchronized void loadMuteButton(Engine pEngine, Context pContext){
			//mute icon
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
			BuildableBitmapTextureAtlas mBuildableBitmapTextureAtlas3 = new BuildableBitmapTextureAtlas(pEngine.getTextureManager(), 1024, 1024, BitmapTextureFormat.RGBA_4444, TextureOptions.BILINEAR);
			mMenuBackgroundSound = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBuildableBitmapTextureAtlas3, pContext, "soundMode_sprite_sheet.png", 2, 1);
			try{
				mBuildableBitmapTextureAtlas3.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			}catch (TextureAtlasBuilderException e){
				e.printStackTrace();
			}
			
			//load muteButton
			mBuildableBitmapTextureAtlas3.load();
			
			// Revert the Asset Path.
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(mPreviousAssetBasePath);
			counter++;
			counter2++;
		}
		
		public synchronized void unloadMuteButton(){
			//mute icon
			if(mMenuBackgroundSound!=null) {
				if(mMenuBackgroundSound.getTexture().isLoadedToHardware()) {
					//Log.v("","MenuMute texture unloaded from hardware");
					mMenuBackgroundSound.getTexture().unload();
					mMenuBackgroundSound = null;
				}
			}
			counter--;
			counter2--;
		}
		
		
		//Unload Menu Textures
		public synchronized void unloadMenuTextures(){
			
			if(mMenuBackgroundTextureRegion!=null) {
				if(mMenuBackgroundTextureRegion.getTexture().isLoadedToHardware()) {
					//Log.v("","Menu Background texture unloaded from hardware");
					mMenuBackgroundTextureRegion.getTexture().unload();
					mMenuBackgroundTextureRegion = null;
				}
			}
			
			if(mTitle!=null) {
				if(mTitle.getTexture().isLoadedToHardware()) {
					//Log.v("","Menu Background texture unloaded from hardware");
					mTitle.getTexture().unload();
					mTitle = null;
				}
			}
			
			if(mCreditsLayer!=null) {
				if(mCreditsLayer.getTexture().isLoadedToHardware()) {
					//Log.v("","Menu Background texture unloaded from hardware");
					mCreditsLayer.getTexture().unload();
					mCreditsLayer = null;
				}
			}
			
			if(mMenuBackgroundResume!=null) {
				if(mMenuBackgroundResume.getTexture().isLoadedToHardware()) {
					//Log.v("","Menu Resume texture unloaded from hardware");
					mMenuBackgroundResume.getTexture().unload();
					mMenuBackgroundResume = null;
				}
			}
			
			if(mMenuBackgroundCredits!=null) {
				if(mMenuBackgroundCredits.getTexture().isLoadedToHardware()) {
					//Log.v("","Menu Credits texture unloaded from hardware");
					mMenuBackgroundCredits.getTexture().unload();
					mMenuBackgroundCredits = null;
				}
			}
			
			if(mMenuBackgroundNewGame!=null) {
				if(mMenuBackgroundNewGame.getTexture().isLoadedToHardware()) {
					//Log.v("","Menu New Game texture unloaded from hardware");
					mMenuBackgroundNewGame.getTexture().unload();
					mMenuBackgroundNewGame = null;
				}
			}
			
			if(mMenuBackgroundSound!=null) {
				if(mMenuBackgroundSound.getTexture().isLoadedToHardware()) {
					//Log.v("","Menu Background Sound texture unloaded from hardware");
					mMenuBackgroundSound.getTexture().unload();
					mMenuBackgroundSound = null;
				}
			}
			counter--;
					
		}
	
	//SOUNDS!
		
	//Load Menu Sounds
	public synchronized void loadMenuSounds(Engine pEngine, Context pContext){
		SoundFactory.setAssetBasePath("sfx/menu/");
		
		if(mSoundMenu==null) {
			try {
				
				mSoundMenu	= SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "menu_confirm.mp3");
			} catch (final IOException e) {
				//Log.v("Sounds Load","Exception:" + e.getMessage());
			}
		}
		
		// Revert the Asset Path.
		SoundFactory.setAssetBasePath(mPreviousAssetBasePath);
		counter++;
	}
	
	//Unload Menu Sounds
	public void unloadMenuSounds(){
		if(mSoundMenu!=null)
			if(mSoundMenu.isLoaded()) {
				//Log.v("","Remove Menu Sounds");
				mSoundMenu.stop();
				engine.getSoundManager().remove(mSoundMenu);
				mSoundMenu = null;
			}
		counter--;
	}
	
	//Load Game Sounds	
	public synchronized void loadGameSounds(Engine pEngine, Context pContext) {
		SoundFactory.setAssetBasePath("sfx/game/");
		
		
			if(mSound1==null) {
				try {
					
					mSound1	= SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "laser1.mp3");
				} catch (final IOException e) {
					//Log.v("Sounds Load","Exception:" + e.getMessage());
				}
			}
			
			if(mSound2==null) {
				try {
					
					mSound2	= SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "enemy_gone.mp3");
				} catch (final IOException e) {
					//Log.v("Sounds Load","Exception:" + e.getMessage());
				}
			}
			
			if(mSound3==null) {
				try {
					
					mSound3	= SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "boss_gone.mp3");
				} catch (final IOException e) {
					//Log.v("Sounds Load","Exception:" + e.getMessage());
				}
			}
			
			if(mSound4==null) {
				try {
					
					mSound4	= SoundFactory.createSoundFromAsset(engine.getSoundManager(), context, "item_pickup.mp3");
				} catch (final IOException e) {
					//Log.v("Sounds Load","Exception:" + e.getMessage());
				}
			}
			
			// Revert the Asset Path.
			SoundFactory.setAssetBasePath(mPreviousAssetBasePath);
			counter2++;
		}
	
	public void unloadGameSounds(){
		if(mSound1!=null)
			if(mSound1.isLoaded()) {
				//Log.v("","Remove Game Sound1");
				mSound1.stop();
				engine.getSoundManager().remove(mSound1);
				mSound1 = null;
			}
		if(mSound2!=null)
			if(mSound2.isLoaded()) {
				//Log.v("","Remove Game Sound2");
				mSound2.stop();
				engine.getSoundManager().remove(mSound2);
				mSound2 = null;
			}
		if(mSound3!=null)
			if(mSound3.isLoaded()) {
				//Log.v("","Remove Game Sound3");
				mSound3.stop();
				engine.getSoundManager().remove(mSound3);
				mSound3 = null;
			}
		if(mSound4!=null)
			if(mSound4.isLoaded()) {
				//Log.v("","Remove Game Sound4");
				mSound4.stop();
				engine.getSoundManager().remove(mSound4);
				mSound4 = null;
			}
		counter2--;
	}
	
	//Load Main Music - NOTE: Change main music to something more upbeat!	
	public synchronized void loadMenuMusic(Engine pEngine, Context pContext) {
		MusicFactory.setAssetBasePath("sfx/game/");
		
		if(mMusic==null) {
			try {
				mMusic	= MusicFactory.createMusicFromAsset(engine.getMusicManager(), context, "start_song.mp3");
			} catch (final IOException e) {
				//Log.v("Music Load","Exception:" + e.getMessage());
			}
		}
		
		// Revert the Asset Path.
		MusicFactory.setAssetBasePath(mPreviousAssetBasePath);
		counter++;
	}
	
	public void unloadMenuMusic(){
		if(mMusic!=null)
			if(mMusic.isPlaying()) {
				//Log.v("","Remove Menu Music");
				mMusic.stop();
				mMusic.release();
				engine.getMusicManager().remove(mMusic);
				mMusic = null;
			} else if (mMusic.isReleased()){
				mMusic.stop();
				engine.getMusicManager().remove(mMusic);
				mMusic = null;
			}
		counter--;
	}
	
	//Load Main Music - NOTE: Change main music to something more upbeat!	
	public synchronized void loadlvl1Music(Engine pEngine, Context pContext) {
		MusicFactory.setAssetBasePath("sfx/game/lvl1/");
		
		if(mMusiclvl1==null) {
			
			try {
				mMusiclvl1	= MusicFactory.createMusicFromAsset(engine.getMusicManager(), context, "lvl1.mp3");
			} catch (final IOException e) {
				//L/og.v("Music Load","Exception:" + e.getMessage());
			}
		}
		
		// Revert the Asset Path.
		MusicFactory.setAssetBasePath(mPreviousAssetBasePath);
		counter2++;
	}
	
	public synchronized void loadlvl2Music(Engine pEngine, Context pContext) {
		MusicFactory.setAssetBasePath("sfx/game/lvl2/");
		
		if(mMusiclvl2==null) {
			
			try {
				mMusiclvl2	= MusicFactory.createMusicFromAsset(engine.getMusicManager(), context, "lvl2.mp3");
			} catch (final IOException e) {
				//L/og.v("Music Load","Exception:" + e.getMessage());
			}
		}
		
		// Revert the Asset Path.
		MusicFactory.setAssetBasePath(mPreviousAssetBasePath);
		counter2++;
	}
	
	public synchronized void loadlvl3Music(Engine pEngine, Context pContext) {
		MusicFactory.setAssetBasePath("sfx/game/lvl3/");
		
		if(mMusiclvl3==null) {
			
			try {
				mMusiclvl3	= MusicFactory.createMusicFromAsset(engine.getMusicManager(), context, "lvl3.mp3");
			} catch (final IOException e) {
				//L/og.v("Music Load","Exception:" + e.getMessage());
			}
		}
		
		// Revert the Asset Path.
		MusicFactory.setAssetBasePath(mPreviousAssetBasePath);
		counter2++;
	}
	
	public synchronized void loadlvl4Music(Engine pEngine, Context pContext) {
		MusicFactory.setAssetBasePath("sfx/game/lvl4/");
		
		if(mMusiclvl4==null) {
			
			try {
				mMusiclvl4	= MusicFactory.createMusicFromAsset(engine.getMusicManager(), context, "lvl4.mp3");
			} catch (final IOException e) {
				//L/og.v("Music Load","Exception:" + e.getMessage());
			}
		}
		
		// Revert the Asset Path.
		MusicFactory.setAssetBasePath(mPreviousAssetBasePath);
		counter2++;
	}
	
	public synchronized void loadlvl5Music(Engine pEngine, Context pContext) {
		MusicFactory.setAssetBasePath("sfx/game/lvl5/");
		
		if(mMusiclvl5==null) {
			
			try {
				mMusiclvl5	= MusicFactory.createMusicFromAsset(engine.getMusicManager(), context, "lvl5.mp3");
			} catch (final IOException e) {
				//L/og.v("Music Load","Exception:" + e.getMessage());
			}
		}
		
		// Revert the Asset Path.
		MusicFactory.setAssetBasePath(mPreviousAssetBasePath);
		counter2++;
	}
	
	public void unloadlvl1Music(){
		if(mMusiclvl1!=null){
			if(!mMusiclvl1.isReleased()) {
				//Log.v("","Remove lvl1 Music");
				mMusiclvl1.stop();
				mMusiclvl1.release();
				engine.getMusicManager().remove(mMusiclvl1);
				mMusiclvl1 = null;
			} else if (mMusiclvl1.isReleased()){
				//mMusiclvl1.stop();
				engine.getMusicManager().remove(mMusiclvl1);
				mMusiclvl1 = null;
			}
		counter2--;
		}
	}
	
	public void unloadlvl2Music(){
		if(mMusiclvl2!=null){
			if(mMusiclvl2.isPlaying()) {
				//Log.v("","Remove lvl1 Music");
				mMusiclvl2.stop();
				mMusiclvl2.release();
				engine.getMusicManager().remove(mMusiclvl2);
				mMusiclvl2 = null;
			} else if (mMusiclvl2.isReleased()){
				mMusiclvl2.stop();
				engine.getMusicManager().remove(mMusiclvl2);
				mMusiclvl2 = null;
			}
		counter2--;
		}
	}
	
	public void unloadlvl3Music(){
		if(mMusiclvl3!=null){
			if(mMusiclvl3.isPlaying()) {
				//Log.v("","Remove lvl1 Music");
				mMusiclvl3.stop();
				mMusiclvl3.release();
				engine.getMusicManager().remove(mMusiclvl3);
				mMusiclvl3 = null;
			} else if (mMusiclvl3.isReleased()){
				mMusiclvl3.stop();
				engine.getMusicManager().remove(mMusiclvl3);
				mMusiclvl3 = null;
			}
		counter2--;
		}
	}
	
	public void unloadlvl4Music(){
		if(mMusiclvl4!=null){
			if(mMusiclvl4.isPlaying()) {
				//Log.v("","Remove lvl1 Music");
				mMusiclvl4.stop();
				mMusiclvl4.release();
				engine.getMusicManager().remove(mMusiclvl4);
				mMusiclvl4 = null;
			} else if (mMusiclvl4.isReleased()){
				mMusiclvl4.stop();
				engine.getMusicManager().remove(mMusiclvl4);
				mMusiclvl4 = null;
			}
		counter2--;
		}
	}
	
	public void unloadlvl5Music(){
		if(mMusiclvl5!=null){
			if(mMusiclvl5.isPlaying()) {
				//Log.v("","Remove lvl1 Music");
				mMusiclvl5.stop();
				mMusiclvl5.release();
				engine.getMusicManager().remove(mMusiclvl5);
				mMusiclvl5 = null;
			} else if (mMusiclvl5.isReleased()){
				mMusiclvl5.stop();
				engine.getMusicManager().remove(mMusiclvl5);
				mMusiclvl5 = null;
			}
		counter2--;
		}
	}
	
	//Load Game Font
	public synchronized void loadGameFont(Engine pEngine, Context pContext){
				//Font creation using preset fonts
		FontFactory.setAssetBasePath("gfx/HUD/");
		if(mFontTexture==null) {
			mFontTexture = new BitmapTextureAtlas(pEngine.getTextureManager(), 256, 256);
			mFont = FontFactory.createFromAsset(pEngine.getFontManager(), mFontTexture, pContext.getAssets(), "GenBkBasBI.ttf", 32f, true, org.andengine.util.adt.color.Color.WHITE_ABGR_PACKED_INT);
			mFont.load();
		}
		
		// Revert the Asset Path.
		FontFactory.setAssetBasePath(mPreviousAssetBasePath);		
		counter2++;
	}
	
	//Load Menu Font
		public synchronized void loadMenuFont(Engine pEngine, Context pContext){
					//Font creation using preset fonts
			FontFactory.setAssetBasePath("gfx/HUD/");
			if(mFontTextureM==null) {
				mFontTextureM = new BitmapTextureAtlas(pEngine.getTextureManager(), 256, 256);
				mFontM = FontFactory.createFromAsset(pEngine.getFontManager(), mFontTextureM, pContext.getAssets(), "GenBkBasBI.ttf", 32f, true, org.andengine.util.adt.color.Color.WHITE_ABGR_PACKED_INT);
				mFontM.load();
			}
			
			// Revert the Asset Path.
			FontFactory.setAssetBasePath(mPreviousAssetBasePath);		
			counter++;
		}
	
	//unload Game font
	public synchronized void unloadMenuFont(){
			if(mFontTextureM!=null) {
				//Log.v("","Menu Font Texture unload");
				mFontTextureM.unload();
				mFontTextureM = null;
				mFontM.unload();
			}
			counter--;
	}
	
	//unload Game font
	public synchronized void unloadGameFont(){
				if(mFontTexture!=null) {
					//Log.v("","Game Font Texture unload");
					mFontTexture.unload();
					mFontTexture = null;
					mFont.unload();
				}
				counter2--;
		}
}
