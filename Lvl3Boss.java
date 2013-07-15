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



public class Lvl3Boss extends Boss{
	
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
	Lvl3Boss sprite = null;
	Sprite bullet = null;
	boolean recycleBullet = true;
	boolean explodeAnimation = false;
	boolean bulletLifeEnd = false;
	//boolean beenHit = false;
	Lvl3BossLaserPool bulletPool;
	TimerHandler bossBulletTimerHandler, movementTimer;
	//Body enemyPhysics;
	//final FixtureDef enemyFixtureDef = PhysicsFactory.createFixtureDef(1f, 0.5f, 0.9f);
	//public FixedStepPhysicsWorld mPhysicsWorld;
	// These variables are needed for the objects allocated by the pool
	ITextureRegion mTextureRegion;
	VertexBufferObjectManager mVertexBufferObjectManager;
	

	public Lvl3Boss(ITextureRegion pTextureRegion) {
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
		bulletPool = new Lvl3BossLaserPool(8);
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
							Log.v("","BULLET CREATED FOR BOSS 3 !");
						}
						
					} else {
						//bulletPool.recyclePoolItem((Lvl3BossLaser)bullet);
						
						ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
			                   
								if (bullet!= null){
									//bullet.detachSelf();
									Log.v("","BULLET RECYCLED FOR BOSS 3 !");
									//bulletLifeEnd = true;
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

	}
}