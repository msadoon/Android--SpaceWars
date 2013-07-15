package mkat.apps.spacewars;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


import android.opengl.GLES20;
import android.util.Log;



public class Lvl5Boss extends Boss{
	
	MoveModifier moveBoss1;
	IEntityModifierListener entityModifierListener;

	/*Define Move Modifiers*/
	int counter= 0;
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
	float rand = 0;
	AnimatedSprite animatedSprite;
	Lvl5Boss sprite = null;
	Sprite bullet = null;
	boolean recycleBullet = true;
	boolean explodeAnimation = false;
	boolean bulletLifeEnd = false;
	//boolean beenHit = false;
	Lvl5BossLaserPool bulletPool;
	TimerHandler bossBulletTimerHandler;
	
	//Body enemyPhysics;
	//final FixtureDef enemyFixtureDef = PhysicsFactory.createFixtureDef(1f, 0.5f, 0.9f);
	//public FixedStepPhysicsWorld mPhysicsWorld;
	// These variables are needed for the objects allocated by the pool
	ITextureRegion mTextureRegion;
	VertexBufferObjectManager mVertexBufferObjectManager;
	AccelerationParticleInitializer<UncoloredSprite> particleAI1;

	public Lvl5Boss(ITextureRegion pTextureRegion) {
		super((float)(0 + (int)(Math.random() * ((1024 - 0) + 1))),(float)(0 + (int)(Math.random() * ((640 - 0) + 1))),pTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		//super(200,200, pTextureRegion,ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		sprite = this;
		//enemyPhysics = PhysicsFactory.createBoxBody(GameLevel.getInstance().mPhysicsWorld, sprite, BodyType.StaticBody, enemyFixtureDef);;
		fromX = sprite.getX();
		fromY = sprite.getY();
		//Handle rotation of every sprite
		
	    //sprite.setRotation(MathUtils.radToDeg(rotationAngle));
		rand = (float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f));
		
		setUpBoss();
		GameLevel.getInstance().attachChild(sprite);
		//bossBulletTimerHandler = getTimerHandler();
		//sprite = new 
		// TODO Auto-generated constructor stub
		GameLevel.getInstance().boundBox = new Rectangle(fromX, fromY, rand*1.5f, rand*1.5f, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		//boundBox.setColor(255,0,0);
		GameLevel.getInstance().boundBox.setColor(1, 0, 0);
		GameLevel.getInstance().boundBox.setAlpha(0f);
		//boundBox.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		//boundBox.setAnchorCenter(sprite.getX(), sprite.getY());
		GameLevel.getInstance().attachChild(GameLevel.getInstance().boundBox);
	}

	
	
	private void setUpBoss() {
		//beenHit = false;
		sprite.setVisible(true);
		sprite.setCullingEnabled(true);
		sprite.setIgnoreUpdate(false);
		sprite.setColor(1, 1, 1);
		//GameLevel.getInstance().attachChild(sprite);
		bulletPool = new Lvl5BossLaserPool(1);
		//PLAYER LASER SETUP
		//Create new laser
		Log.v("","calls for a Boss laser");
		
		Log.v("Boss1", sprite.getX() + " " + sprite.getY() );
		
		moveBoss1 = new MoveModifier(duration, fromX, fromY, toX, toY);
		
		sprite.registerEntityModifier(moveBoss1);
		
		
		
		//SETUP ENEMY LASER
		ResourceManager.getInstance().engine.registerUpdateHandler(bossBulletTimerHandler = new TimerHandler(2f, new ITimerCallback()
		{

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO Auto-generated method stub
				bossBulletTimerHandler.setTimerSeconds(1.2f);
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
						bulletPool.recyclePoolItem((Lvl5BossLaser)bullet);
						
						
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
		
		//configure particle system
		/* Define the center point of the particle system spawn*/
		final int particleSpawnCenterX = (int) sprite.getX();
		final int particleSpawnCenterY = (int) sprite.getY();
		
		 
		GameLevel.getInstance().particleEmitter = new CircleParticleEmitter(particleSpawnCenterX, particleSpawnCenterY, rand);
		 //Define the particle system properties 
		final float minSpawnRate = (ResourceManager.getInstance().cameraWidth*0.1875f);
		final float maxSpawnRate = (ResourceManager.getInstance().cameraWidth*0.25f);
		final int maxParticleCount = (int)(ResourceManager.getInstance().cameraWidth*0.625f);
		
		// Create the particle system 
		GameLevel.getInstance().particleSystem = new BatchedSpriteParticleSystem(GameLevel.getInstance().particleEmitter, minSpawnRate, maxSpawnRate, maxParticleCount, GameLevel.getInstance().pEnvFlames, ResourceManager.getInstance().engine.getVertexBufferObjectManager());

		//Add an acceleration initializer to ther particle system
		particleAI1 = new AccelerationParticleInitializer<UncoloredSprite>(-rand, rand, -rand, rand);
		GameLevel.getInstance().particleSystem.addParticleInitializer(particleAI1);
		// Add an expire initializer to the particle system 
		GameLevel.getInstance().particleSystem.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(2));
		// Add some blending to the particles
		GameLevel.getInstance().particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<UncoloredSprite>(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA));
		GameLevel.getInstance().particleSystem.addParticleInitializer(new AlphaParticleModifier<UncoloredSprite>(1, 2, 1, 0));
		//Add a scale particle modifier to the particle system 
		GameLevel.getInstance().particleSystem.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(0f, 2f, 1f, 2f));
		
		//end configure particle system
		
		GameLevel.getInstance().attachChild(GameLevel.getInstance().particleSystem);
		
		//setup timer handler for boss bullets so they can be cleared by the GameLevel.
		setTimerHandler(bossBulletTimerHandler);
		bossBulletTimerHandler = getTimerHandler();
		
		GameLevel.getInstance().registerUpdateHandler(new IUpdateHandler() {
		
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				//sprite.getRotation();
				GameLevel.getInstance().particleEmitter.setCenter(sprite.getX(), sprite.getY());
				GameLevel.getInstance().boundBox.setPosition(sprite.getX(), sprite.getY());
				
				//randomize size of rectangle and particle initializer
				rand = (float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f));
				particleAI1.setAcceleration(-rand, rand, -rand, rand);
				GameLevel.getInstance().boundBox.setSize(rand*1.5f, rand*1.5f);
				//end randomize
				
				
				if (GameLevel.getInstance().PlayerReady && bullet != null){
						
					if (GameLevel.getInstance().boundBox.collidesWith(GameLevel.getInstance().mPlayer)){
						GameLevel.getInstance().playerHit = true;
					}
					
					if (((bullet.collidesWith(GameLevel.getInstance().mPlayer)))&& explodeAnimation == false && bulletLifeEnd == false){
						explodeAnimation = true;
						
						bulletPool.recyclePoolItem((Lvl5BossLaser)bullet);
						
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

