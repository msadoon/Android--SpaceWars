package mkat.apps.spacewars;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.util.Log;

public class Lvl3BossLaser extends Sprite{
		Sprite sprite = null; 
		Lvl3Boss callingBossHere = null;
		int count = 0;
		float fromX = 0;
		float fromY = 0;
		float rotAngle = 0;
		float m, m2 = 0;//slope of boundary line and intersecting line
		float b, b2 = 0;//y-int of boundary line and intersecting line
		float dy, dx = 0;//for intersecting line
		float x, y = 0; //Point of intersection between both lines
		float laserdtoTravel = 0;
		float laserVelocity, laserMoveDuration = 0;
		boolean registerUnregister = false;
		Line line1, line2, line3, line4;
		MoveModifier mod;
		ScaleModifier scale;
		IEntityModifierListener laserModifierListener;
		private final PhysicsHandler mPhysicsHandler;
        private static final float CAMERA_WIDTH = 1024;
        private static final float CAMERA_HEIGHT = 640;

        private static final float DEMO_VELOCITY = (ResourceManager.getInstance().cameraWidth*0.375f);

	public Lvl3BossLaser(ITextureRegion pTextureRegion, float x, float y, float rAngle, Lvl3Boss callingBoss) {
		super(x,y,pTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		fromX = x;
		fromY = y;
		sprite = this;
		rotAngle = rAngle;
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		this.mPhysicsHandler.setVelocity(DEMO_VELOCITY, DEMO_VELOCITY);
		//Log.v("","creates enemy laser");
		setupBossLaser();
		callingBossHere = callingBoss;
		
	}

	//setup laser:
	private void setupBossLaser() {
		//Log.v("","sets up enemy laser");
		//Log.v("", "value of laserFired " + GameLevel.getInstance().laserFired);
		sprite.setCullingEnabled(true);
		sprite.setVisible(true);
		sprite.setIgnoreUpdate(false);
		sprite.setColor(1, 1, 1);
		sprite.setScale(5);
		//sprite.setAnchorCenter(sprite.getHeight(), sprite.getWidth());
		//set the laser to invisible exactly as soon as it finishes reaching its destination
		
		
		//sprite.setPosition(GameLevel.getInstance().touchX,GameLevel.getInstance().touchY);
		//sprite.setRotation(rotAngle);
		//fromX = GameLevel.getInstance().enemyX2;
		//fromY = GameLevel.getInstance().enemyY2;
		//mod = new MoveModifier(.5f, fromX, fromY, 1000, 1000);
				
	}
	
	@Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
            if(this.mX <= 0) {
                    this.mPhysicsHandler.setVelocityX(DEMO_VELOCITY);
            } else if(this.mX + this.getWidth() >= CAMERA_WIDTH) {
                    this.mPhysicsHandler.setVelocityX(-DEMO_VELOCITY);
            }

            if(this.mY <= 0) {
                    this.mPhysicsHandler.setVelocityY(DEMO_VELOCITY);
            } else if(this.mY + this.getHeight() >= CAMERA_HEIGHT) {
                    this.mPhysicsHandler.setVelocityY(-DEMO_VELOCITY);
            }

            super.onManagedUpdate(pSecondsElapsed);
            
            
            if (GameLevel.getInstance().PlayerReady && sprite != null){
            	if (!GameLevel.getInstance().BossReady) {
            		ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							
							sprite.detachSelf();
						}
					});
    				
    			}
				//Log.v("","Player Ready and Bullet not null");
				//Log.v("","explode Animation " + explodeAnimation);
				//Log.v("","bulletLifeEnd " + bulletLifeEnd);
				if ((sprite.collidesWith(GameLevel.getInstance().mPlayer))){
					callingBossHere.explodeAnimation = true;
					Log.v("","COLLISION!!!!!!!!");
					callingBossHere.bulletPool.recyclePoolItem((Lvl3BossLaser)sprite);
					
					ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
		                   
							if (sprite!= null){
								sprite.detachSelf();
							
								GameLevel.getInstance().playerHitBoss = true;
								callingBossHere.bulletLifeEnd = true;
								//Log.v("","Removes bullet from scene");
								callingBossHere.recycleBullet = true;
								//Log.v(""," After detach bullet from Scene? " + recycleBullet);
							}
						}
					});
				//ANIMATED SPRITE BEGINS
				 //if (bulletPool.getUnrecycledItemCount()>0){
				// bulletPool.recyclePoolItem((EnemyLaser)bullet);
				//	ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
				//		@Override
				//		public void run() {
		                   
				//			if (bullet!= null){
				//				bullet.detachSelf();
				//				//Log.v("","Removes bullet from scene");
				//				recycleBullet = true;
				//				//Log.v(""," After detach bullet from Scene? " + recycleBullet);
				//			}
				//		}
				//	});
				 //}
				//Create a new animated sprite in the center of the scene
				callingBossHere.animatedSprite = new AnimatedSprite(GameLevel.getInstance().mPlayer.getX(), GameLevel.getInstance().mPlayer.getY(), ResourceManager.getInstance().mTiledTextureRegionExplosion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		
				//Length to play each frame before moving on to the next 
				long frameDuration[] = {75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75,75};
		
				// we can define the indices of the animation to play between 
				int firstTileIndex = 0;
				int lastTileIndex = ResourceManager.getInstance().mTiledTextureRegionExplosion.getTileCount()-1;
				
				//Log.i("MODIFIER", Integer.toString(lastTileIndex));
		
				boolean loopAnimation = false;
				//GameLevel.getInstance().mPlayer.clearEntityModifiers();
				//GameLevel.getInstance().mPlayer.clearUpdateHandlers();
				//GameLevel.getInstance().mPlayer.detachSelf();
				//GameLevel.getInstance().mPlayer = null;
				 //Animate the sprite with the data as set defined above 
				callingBossHere.animatedSprite.animate(frameDuration, firstTileIndex, lastTileIndex, loopAnimation, new IAnimationListener() {
			
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
								GameLevel.getInstance().detachChild(callingBossHere.animatedSprite);
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
				GameLevel.getInstance().attachChild(callingBossHere.animatedSprite);
			} 
			
			}
    }
}

