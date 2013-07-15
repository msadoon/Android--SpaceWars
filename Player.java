package mkat.apps.spacewars;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;


public class Player extends Sprite{


	/*Define Move Modifiers*/
	float atX = (ResourceManager.getInstance().cameraWidth*0.5f);
	float moveSpeed = 0;
	float atY = (ResourceManager.getInstance().cameraHeight*0.5f);
	float turnPointX = 0;
	float turnPointY = 0;
	float rotationAngle = 0;
	float rotateX = 0;
	float rotateY = 0;
	Line compass1, compass2, compass3,compass4 = null;
	Sprite bullet = null;
	Player player = null;
	Rectangle enemyFireGrid;
	//Sprite bullet;
	boolean recycleBullet = true;
	boolean bulletLifeEnd = false;
	boolean firstRun = false;
	PlayerLaserPool bulletPool;
	AnimatedSprite animatedSprite;
	// These variables are needed for the objects allocated by the pool
	ITextureRegion mTextureRegion;
	VertexBufferObjectManager mVertexBufferObjectManager;
	

	public Player(ITextureRegion pTextureRegion) {
		super((ResourceManager.getInstance().cameraWidth),(ResourceManager.getInstance().cameraHeight),pTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		//super(200,200, pTextureRegion,ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		player = this;
	
		setUpPlayer();
		
		//sprite = new 
		// TODO Auto-generated constructor stub
	}

	private void setUpPlayer() {
		
		player.setVisible(true);
		player.setIgnoreUpdate(false);
		player.setColor(1, 1, 1);
		//player.setX((ResourceManager.getInstance().cameraWidth*0.5f));
		//player.setY((ResourceManager.getInstance().cameraHeight*0.5f));
		//attachPlayer
		
		//for laser regen:
		
		//Compass - we create a triangle mesh around the player:
		
		//ENEMY FIRING SOLUTION
		enemyFireGrid = new Rectangle(player.getX(), player.getY(),ResourceManager.getInstance().cameraWidth,ResourceManager.getInstance().cameraHeight, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		enemyFireGrid.setColor(org.andengine.util.adt.color.Color.RED);
		//enemyFireGrid.setAlpha(0.25f);
		//GameLevel.getInstance().attachChild(enemyFireGrid);
		//END ENEMY FIRING SOLUTION
		
		
		compass1 = new Line(player.getX()-(ResourceManager.getInstance().cameraWidth/2), player.getY()-(ResourceManager.getInstance().cameraHeight/2),player.getX()-(ResourceManager.getInstance().cameraWidth/2),player.getY()+(ResourceManager.getInstance().cameraHeight/2), ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		//compass1.setScale(0.5f);
		compass1.setAlpha(1f);
		compass2 = new Line(player.getX()-(ResourceManager.getInstance().cameraWidth/2), player.getY()+(ResourceManager.getInstance().cameraHeight/2), player.getX()+(ResourceManager.getInstance().cameraWidth/2), player.getY()+(ResourceManager.getInstance().cameraHeight/2), ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		compass2.setAlpha(1f);
		//compass2.setScale(0.5f);
		compass3 = new Line(player.getX()+(ResourceManager.getInstance().cameraWidth/2), player.getY()+(ResourceManager.getInstance().cameraHeight/2), player.getX()+(ResourceManager.getInstance().cameraWidth/2), player.getY()-(ResourceManager.getInstance().cameraHeight/2), ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		compass3.setAlpha(1f);
		//compass3.setScale(0.5f);
		compass4 = new Line(player.getX()+(ResourceManager.getInstance().cameraWidth/2), player.getY()-(ResourceManager.getInstance().cameraHeight/2), player.getX()-(ResourceManager.getInstance().cameraWidth/2), player.getY()-(ResourceManager.getInstance().cameraHeight/2), ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		compass4.setAlpha(1f);
		//compass4.setScale(0.5f);
		compass1.setColor(org.andengine.util.adt.color.Color.RED);
		compass2.setColor(org.andengine.util.adt.color.Color.RED);
		compass3.setColor(org.andengine.util.adt.color.Color.RED);
		compass4.setColor(org.andengine.util.adt.color.Color.RED);
		//GameLevel.getInstance().attachChild(compass1);
		//GameLevel.getInstance().attachChild(compass2);
		//GameLevel.getInstance().attachChild(compass3);
		//GameLevel.getInstance().attachChild(compass4);
		//end compass //update the position of this mesh as the player moves in IUpdateHandler
		
			bulletPool = new PlayerLaserPool(1);
		//PLAYER LASER SETUP
		//Create new laser
		//Log.v("","calls for a player laser");
		
		//bulletPool.batchAllocatePoolItems(2000);
		//bullet = bulletPool.obtainPoolItem();
		//End laser
		
		//END PLAYER LASER SETUP
			if (GameLevel.getInstance().fireRate == true){
				moveSpeed = GameLevel.getInstance().fireRateDuration;
			} else {
				moveSpeed = 1f;
			}
		/**
		 * Creates a Time Handler used to Spawn bullets
		 */
		
		
	
		ResourceManager.getInstance().engine.registerUpdateHandler(GameLevel.getInstance().bulletTimerHandler = new TimerHandler(1f, new ITimerCallback()
			{

				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					// TODO Auto-generated method stub
					GameLevel.getInstance().bulletTimerHandler.setTimerSeconds(moveSpeed);
					GameLevel.getInstance().bulletTimerHandler.reset();
					
					
					if (GameLevel.getInstance().readyToFireLaser == true) {
						//Log.v(""," recycledOrNot? " + recycleBullet);
						if (recycleBullet == true){
						
							if (bulletPool.getAvailableItemCount()>0){
								bullet = bulletPool.obtainPoolItem(player, player.getX(), player.getY());
								bulletLifeEnd = false;
								//Log.v(""," After get a new bullet on scene " + recycleBullet);
								GameLevel.getInstance().PlayerLaser = (PlayerLaser)bullet;
								recycleBullet = false;
							}
							
						}
						else{
							
							ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
				                   
									if (GameLevel.getInstance().PlayerLaser != null){
										bulletPool.recyclePoolItem((PlayerLaser)bullet);
										GameLevel.getInstance().PlayerLaser.detachSelf();
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
		
		
		player.registerUpdateHandler(new IUpdateHandler() {
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (GameLevel.getInstance().PlayerReady){
				player.setPosition(atX, atY);
				atX = GameLevel.getInstance().touchX;
				atY = GameLevel.getInstance().touchY;
				
				turnPointX = GameLevel.getInstance().dToPX;
				turnPointY = GameLevel.getInstance().dToPY;
				//Log.v("",""+pSecondsElapsed);
				//Log.v("","distance to closest enemy Y " + Float.toString(turnPointY));
				//Handle rotation of every sprite
				rotateX = turnPointX - GameLevel.getInstance().mPlayer.getX();
				rotateY = turnPointY - GameLevel.getInstance().mPlayer.getY();

			    rotationAngle = (float) Math.atan2(rotateX, rotateY);
				player.setRotation(MathUtils.radToDeg(rotationAngle)); 
				GameLevel.getInstance().laserRotationAngle = MathUtils.radToDeg(rotationAngle); // sets up laser rotation angle to be used by the PlayerLaser Class, to rotate the laser with the ship.
				//Log.v("", "rotation angle " + MathUtils.radToDeg(rotationAngle));			
				//recycle bullets if they are offscreen
				//Log.v("","Bullet x " + bullet.getX() + " Bullet y " + bullet.getY());
				
				//Update compass based on player position
				compass1.setPosition(player.getX()-(ResourceManager.getInstance().cameraWidth/5), player.getY()-(ResourceManager.getInstance().cameraHeight/5),player.getX()-(ResourceManager.getInstance().cameraWidth/5),player.getY()+(ResourceManager.getInstance().cameraHeight/5));
				//compass1.setAlpha(1f);
				compass2.setPosition(player.getX()-(ResourceManager.getInstance().cameraWidth/5), player.getY()+(ResourceManager.getInstance().cameraHeight/5), player.getX()+(ResourceManager.getInstance().cameraWidth/5), player.getY()+(ResourceManager.getInstance().cameraHeight/5));
				//compass2.setAlpha(1f);
				compass3.setPosition(player.getX()+(ResourceManager.getInstance().cameraWidth/5), player.getY()+(ResourceManager.getInstance().cameraHeight/5), player.getX()+(ResourceManager.getInstance().cameraWidth/5), player.getY()-(ResourceManager.getInstance().cameraHeight/5));
				//compass3.setAlpha(1f);
				compass4.setPosition(player.getX()+(ResourceManager.getInstance().cameraWidth/5), player.getY()-(ResourceManager.getInstance().cameraHeight/5), player.getX()-(ResourceManager.getInstance().cameraWidth/5), player.getY()-(ResourceManager.getInstance().cameraHeight/5));
				//ENEMEY FIRING SOLUTION
				
				enemyFireGrid.setPosition(player.getX(), player.getY());
				//compass4.setAlpha(1f);
				//end update to compass
				}
			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
		
		});
		
		
	}
}
