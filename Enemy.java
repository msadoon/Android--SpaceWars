package mkat.apps.spacewars;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.LoopModifier;

import android.util.Log;



public class Enemy extends Sprite{
	
	MoveModifier moveEnemy1;
	IEntityModifierListener entityModifierListener;

	/*Define Move Modifiers*/
	int counter, eLife = 0;
	float duration = GameLevel.getInstance().duration;
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
	AnimatedSprite animatedSprite;
	Enemy sprite = null;
	Line direction = null;
	Sprite bullet = null;
	boolean recycleBullet = true;
	boolean explodeAnimation = false;
	boolean bulletLifeEnd, readyToFireEnemyLaser = false;
	
	//boolean beenHit = false;
	EnemyLaserPool bulletPool;
	TimerHandler enemyBulletTimerHandler;
	//Body enemyPhysics;
	//final FixtureDef enemyFixtureDef = PhysicsFactory.createFixtureDef(1f, 0.5f, 0.9f);
	//public FixedStepPhysicsWorld mPhysicsWorld;
	// These variables are needed for the objects allocated by the pool
	ITextureRegion mTextureRegion;
	VertexBufferObjectManager mVertexBufferObjectManager;
	

	public Enemy(ITextureRegion pTextureRegion) {
		super((float)(0 + (int)(Math.random() * ((1024 - 0) + 1))),(float)(0 + (int)(Math.random() * ((640 - 0) + 1))),pTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		//super(200,200, pTextureRegion,ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		sprite = this;
		//enemyPhysics = PhysicsFactory.createBoxBody(GameLevel.getInstance().mPhysicsWorld, sprite, BodyType.StaticBody, enemyFixtureDef);;
		fromX = sprite.getX();
		fromY = sprite.getY();
		//Handle rotation of every sprite
	

	    //sprite.setRotation(MathUtils.radToDeg(rotationAngle));
		
		
		setUpEnemy();
		
		//sprite = new 
		// TODO Auto-generated constructor stub
	}

	public TimerHandler getTimerHandler(){
		return enemyBulletTimerHandler;
	}
	
	private void setUpEnemy() {
		//beenHit = false;
		sprite.setVisible(true);
		sprite.setIgnoreUpdate(false);
		sprite.setColor(1, 1, 1);
		//GameLevel.getInstance().attachChild(sprite);
		bulletPool = new EnemyLaserPool(1);
		//PLAYER LASER SETUP
		//Create new laser
		Log.v("","calls for a enemy laser");
		
		if (GameLevel.getInstance().waveCounter-1 == 0) {
			eLife = 1;
		} else if (GameLevel.getInstance().waveCounter-1 == 1){
			eLife = 2;
		} else if (GameLevel.getInstance().waveCounter-1 == 2) {
			eLife = 3;
		} else if (GameLevel.getInstance().waveCounter-1 == 3) {
			eLife = 4;
		} else if (GameLevel.getInstance().waveCounter-1 == 4) {
			eLife = 5;
		}
		
		Log.v("Sprite1", sprite.getX() + " " + sprite.getY() );
		
		moveEnemy1 = new MoveModifier(duration, fromX, fromY, toX, toY);
		sprite.setCullingEnabled(true);
		sprite.registerEntityModifier(moveEnemy1);
		
		//set up compass pointer
		//add the pointer to the scene where the direction line intersects the compass, and update it at that position.
		
		
		
		direction = new Line(sprite.getX(), sprite.getY(), sprite.getX()+1, sprite.getY()+1, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		direction.setAlpha(1f);
		//GameLevel.getInstance().attachChild(direction);	
		//GameLevel.getInstance().attachChild(pointer);
		
		
		//SETUP ENEMY LASER
		ResourceManager.getInstance().engine.registerUpdateHandler(enemyBulletTimerHandler = new TimerHandler(GameLevel.getInstance().fireRateEnemyStart, new ITimerCallback()
		{

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO Auto-generated method stub
				enemyBulletTimerHandler.setTimerSeconds(GameLevel.getInstance().fireRateEnemy);
				enemyBulletTimerHandler.reset();
				//Log.v("", "time runs every second");
				
				if (readyToFireEnemyLaser == true && GameLevel.getInstance().readyToFireEnemyLaser == true) {
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
						bulletPool.recyclePoolItem((EnemyLaser)bullet);
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
		
		GameLevel.getInstance().registerUpdateHandler(new IUpdateHandler() {
		
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				
				//Pointer for Compass
				if (GameLevel.getInstance().PlayerReady){
					//direction.
					//direction.setPosition(GameLevel.getInstance().mPlayer);
					
					
					
				
					//line 1 is direction
					
					//line 2 is is the player compass
					
					direction.setPosition(sprite.getX(), sprite.getY(), GameLevel.getInstance().mPlayer.getX(), GameLevel.getInstance().mPlayer.getY());
					//direction.setPosition(sprite.getX(), sprite.getY(), GameLevel.getInstance().mPlayer.compass.getX(), GameLevel.getInstance().mPlayer.compass.getY());
					//if (direction.collidesWith(GameLevel.getInstance().mPlayer.compass)){
						//pointer.unregisterEntityModifier(mm);
						//pointer.detachSelf();
						//mm = new MoveModifier(1, GameLevel.getInstance().mPlayer.getX(), GameLevel.getInstance().mPlayer.getY(), sprite.getX(),sprite.getY());
						//pointer.registerEntityModifier(mm);
					//} 
				}
				//End Pointer for Compass
				if (GameLevel.getInstance().PlayerReady && bullet != null ){
						
					
					
					if (bullet.collidesWith(GameLevel.getInstance().mPlayer)&& explodeAnimation == false && bulletLifeEnd == false){
						explodeAnimation = true;
						
						bulletPool.recyclePoolItem((EnemyLaser)bullet);
						ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
			                   
								if (bullet!= null){
									bullet.detachSelf();
									GameLevel.getInstance().playerHit = true;
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
