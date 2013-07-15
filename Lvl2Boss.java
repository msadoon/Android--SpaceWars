package mkat.apps.spacewars;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


import android.util.Log;



public class Lvl2Boss extends Boss{
	
	MoveModifier moveBoss1;
	IEntityModifierListener entityModifierListener;

	/*Define Move Modifiers*/
	int counter = 0;
	float duration = GameLevel.getInstance().duration;
	float distance;
	float fromX;
	float toX, x;
	float fromY;
	float toY, y;
	float rotateX;
	float rotateY;
	float directionX;
	float directionY;
	float rotationAngle;
	AnimatedSprite animatedSprite;
	Lvl2Boss sprite = null;
	Sprite bullet = null;
	boolean recycleBullet = true;
	boolean explodeAnimation = false;
	boolean bulletLifeEnd = false;
	//boolean beenHit = false;
	Lvl2BossLaserPool bulletPool;
	TimerHandler bossBulletTimerHandler, movementTimer;
	//Body enemyPhysics;
	//final FixtureDef enemyFixtureDef = PhysicsFactory.createFixtureDef(1f, 0.5f, 0.9f);
	//public FixedStepPhysicsWorld mPhysicsWorld;
	// These variables are needed for the objects allocated by the pool
	ITextureRegion mTextureRegion;
	VertexBufferObjectManager mVertexBufferObjectManager;
	

	public Lvl2Boss(ITextureRegion pTextureRegion) {
		super((float)(0 + (int)(Math.random() * ((1024 - 0) + 1))),(float)(0 + (int)(Math.random() * ((640 - 0) + 1))),pTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		//super(200,200, pTextureRegion,ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		sprite = this;
		//enemyPhysics = PhysicsFactory.createBoxBody(GameLevel.getInstance().mPhysicsWorld, sprite, BodyType.StaticBody, enemyFixtureDef);;
		fromX = sprite.getX();
		fromY = sprite.getY();
		//Handle rotation of every sprite
		
	    //sprite.setRotation(MathUtils.radToDeg(rotationAngle));
		
		
		//GameLevel.getInstance().bulletTimerHandler = bossBulletTimerHandler;
		
		setUpBoss();
		GameLevel.getInstance().attachChild(sprite);

		//sprite = new 
		// TODO Auto-generated constructor stub
	}
	
	private void setUpBoss() {
		//beenHit = false;
		sprite.setVisible(true);
		sprite.setIgnoreUpdate(false);
		sprite.setColor(1, 1, 1);
		sprite.setCullingEnabled(true);
		//GameLevel.getInstance().attachChild(sprite);
		bulletPool = new Lvl2BossLaserPool(1);
		//PLAYER LASER SETUP
		//Create new laser
		Log.v("","calls for a Boss laser");
		
		Log.v("Boss1", sprite.getX() + " " + sprite.getY() );
		
		moveBoss1 = new MoveModifier(duration, fromX, fromY, toX, toY);
		
		sprite.registerEntityModifier(moveBoss1);
		
		
		//SETUP ENEMY LASER
		ResourceManager.getInstance().engine.registerUpdateHandler(bossBulletTimerHandler = new TimerHandler(3f, new ITimerCallback()
		{

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO Auto-generated method stub
				bossBulletTimerHandler.setTimerSeconds(1f);
				bossBulletTimerHandler.reset();
				//Log.v("", "time runs every second");
				if (GameLevel.getInstance().BossReady) {
					//Log.v(""," Enemy recycledOrNot? " + recycleBullet);
					if (recycleBullet == true){
					
						if (bulletPool.getAvailableItemCount()>0){
							bullet = bulletPool.obtainPoolItem(sprite.getX(), sprite.getY(),sprite.getRotation(), sprite);
							//Log.v(""," After get a new bullet on scene " + recycleBullet);
							//GameLevel.getInstance().EnemyLaser = bullet;
							bulletLifeEnd = false;
							explodeAnimation = false;
							recycleBullet = false;
						}
						
					} else {
						bulletPool.recyclePoolItem((Lvl2BossLaser)bullet);
						
						ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
			                   
								if (bullet!= null){
									bullet.detachSelf();
								
									bulletLifeEnd = true;
									//Log.v("","Removes bullet from scene");
									recycleBullet = true;
									//Log.v(""," After detach bullet from Scene? " + recycleBullet);
								}
							}
						});
					}
				}
				
				
			}
		}));
		//END SETUP ENEMY LASER
		//setup timer handler for boss bullets so they can be cleared by the GameLevel.
				setTimerHandler(bossBulletTimerHandler);
				bossBulletTimerHandler = getTimerHandler();
		//SETUP ENEMY MOVEMENT
		ResourceManager.getInstance().engine.registerUpdateHandler(movementTimer = new TimerHandler(2f, new ITimerCallback()
		{

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				
				movementTimer.setTimerSeconds(4f);
				movementTimer.reset();
				
				if (GameLevel.getInstance().BossReady) {
					//Log.v(""," Enemy recycledOrNot? " + recycleBullet);
					x = (float)(0 + (int)(Math.random() * ((ResourceManager.getInstance().cameraHeight - 0) + 1)));
					y = (float)(0 + (int)(Math.random() * ((ResourceManager.getInstance().cameraWidth - 0) + 1)));
						ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								sprite.setX(x);
								sprite.setY(y);
								}
							
						});
					}
				}
				
				
			
		}));
		//END SETUP ENEMY MOVEMENT
		
		GameLevel.getInstance().registerUpdateHandler(new IUpdateHandler() {
		
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				//sprite.getRotation();
				if (GameLevel.getInstance().PlayerReady && bullet != null){
						
					
					if ((bullet.collidesWith(GameLevel.getInstance().mPlayer)) && (explodeAnimation == false) && (bulletLifeEnd == false)){
						explodeAnimation = true;
						
						bulletPool.recyclePoolItem((Lvl2BossLaser)bullet);
						
						ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
			                   
								if (bullet!= null){
									bullet.detachSelf();
								
									GameLevel.getInstance().playerHitBoss = true;
									bulletLifeEnd = true;
									//Log.v("","Removes bullet from scene");
									recycleBullet = true;
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
					animatedSprite = new AnimatedSprite(GameLevel.getInstance().mPlayer.getX(), GameLevel.getInstance().mPlayer.getY(), ResourceManager.getInstance().mTiledTextureRegionExplosion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			
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
									GameLevel.getInstance().detachChild(animatedSprite);
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
					GameLevel.getInstance().attachChild(animatedSprite);
				} 
				
				}
			}
			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}

		
		});
	}
}