package mkat.apps.spacewars;

import org.andengine.entity.IEntity;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;

public class EnemyLaser extends Sprite{

	Sprite sprite = null; 
	Enemy callingEnemyHere = null;
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
	IEntityModifierListener laserModifierListener;
	
	
	public EnemyLaser(ITextureRegion pTextureRegion, float x, float y, float rAngle, Enemy callingEnemy) {
		super(x,y,pTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		fromX = x;
		fromY = y;
		sprite = this;
		rotAngle = rAngle;
		//Log.v("","creates enemy laser");
		setupEnemyLaser();
		callingEnemyHere = callingEnemy;
	}

	//setup laser:
	private void setupEnemyLaser() {
		//Log.v("","sets up enemy laser");
		//Log.v("", "value of laserFired " + GameLevel.getInstance().laserFired);
		
		sprite.setVisible(true);
		sprite.setIgnoreUpdate(false);
		sprite.setColor(1, 1, 1);
		sprite.setCullingEnabled(true);
		//sprite.setAnchorCenter(sprite.getHeight(), sprite.getWidth());
		//set the laser to invisible exactly as soon as it finishes reaching its destination
		laserModifierListener = new IEntityModifierListener() {
			
			//When the modifier starts, this method is called 
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, final IEntity pItem){
			
			}
	
			//When the modifier finishes, this method is called
			@Override
			public void onModifierFinished(final IModifier<IEntity> pModifer, final IEntity pItem){
				sprite.setVisible(false);	
				callingEnemyHere.bulletLifeEnd = true;
			}
				
		
				//AFTER THIS PLAYER"S LIFE = 0, so game over, dump current scene, load game over text and quit to menu.
		};
		
		//sprite.setPosition(GameLevel.getInstance().touchX,GameLevel.getInstance().touchY);
		sprite.setRotation(rotAngle);
		//fromX = GameLevel.getInstance().enemyX2;
		//fromY = GameLevel.getInstance().enemyY2;
		//mod = new MoveModifier(.5f, fromX, fromY, 1000, 1000);
				
					if (((sprite.getRotation()<= -180.0) && (sprite.getRotation() > -270.0))){
						
					
						
						//equation of boundary line
						m = 0.6f;
						
						b = fromY - ((fromX+ResourceManager.getInstance().cameraWidth)*(m));
						
						//find equation of line for intersecting line
						dx =  fromX - GameLevel.getInstance().mPlayer.getX();
						dy =  fromY - GameLevel.getInstance().mPlayer.getY();
						m2 = dy/dx;
						b2 = GameLevel.getInstance().mPlayer.getY() - ((GameLevel.getInstance().mPlayer.getX())*m2);
						
						//Now solve for the point of intersection between our boundary line and our intersecting line.
						x = (b2 - b)/(m - m2);
						
						//Now with our x, solve for y in either equation:
						y = (m*x) + b;
						
						//let's get the distance our laser has to travel
						laserdtoTravel = MathUtils.distance(fromX, fromY, x, y);
						//Log.v("",Float.toString(laserdtoTravel));
						//let's get the velocity
						laserVelocity = ResourceManager.getInstance().cameraHeight/1.0f;
						
						laserMoveDuration = (laserdtoTravel/laserVelocity);
						if (GameLevel.getInstance().fireRateEnemy == 1f){
							//do nothing;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.9f){
							laserMoveDuration = laserMoveDuration/1.10f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.8f){
							laserMoveDuration = laserMoveDuration/1.15f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.75f){
							laserMoveDuration = laserMoveDuration/1.20f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.7f){
							laserMoveDuration = laserMoveDuration/1.25f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						}
						//Now with our two points, we can create a move modifier for our laser.
						//line1 = new Line(fromX, fromY, fromX+800f, fromY, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						//line2 = new Line(fromX+800f, fromY, fromX, fromY-480f, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						//line3 = new Line(fromX, fromY-480f, fromX, fromY, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						//line4 = new Line(fromX, fromY, x, y, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						
						//if (count > 0) {
							//GameLevel.getInstance().detachChild(line1);
							//GameLevel.getInstance().detachChild(line2);
							//GameLevel.getInstance().detachChild(line3);
							//GameLevel.getInstance().detachChild(line4);
						//}
						//GameLevel.getInstance().attachChild(line1);
						//GameLevel.getInstance().attachChild(line2);
						//GameLevel.getInstance().attachChild(line3);
						//GameLevel.getInstance().attachChild(line4);
						count++;
						if (sprite.getEntityModifierCount()>=0){ 
							//mod.removeModifierListener(laserModifierListener);
							sprite.clearEntityModifiers();
							//Log.v("", "gets here!");
							mod = new MoveModifier(laserMoveDuration, fromX, fromY, x, y);
							mod.addModifierListener(laserModifierListener);
							sprite.registerEntityModifier(mod);
							
							//GameLevel.getInstance().attachChild(sprite);
							//registerUnregister = true;
							//Log.v("","ENEMY x start " + Float.toString(fromX) + "  y start" + fromY);
							//Log.v("","ENEMY x end " + Float.toString(x) + "  y end" + y);
							//Log.v("","ENEMY distance to travel " + Float.toString(laserdtoTravel));
							//Log.v("","ENEMY laser velocity " + Float.toString(laserVelocity));
							//Log.v("","ENEMY move duration" + Float.toString(laserMoveDuration));
							//Log.v("", "ENEMY Unregister old modifier, register new one");
							//Log.v("","ENEMY Laser icon should fire here!");
							
						}
						
						//GameLevel.getInstance().resetEntityModifiers();
						
						//GameLevel.getInstance().laserFired = true;
						
					} else if ((sprite.getRotation()<= -270.0) && (sprite.getRotation() >= -360.0)){
						
						
						//equation of boundary line
						m = -0.6f;
						
						b = fromY - ((fromX+ResourceManager.getInstance().cameraHeight)*(m));
						
						//find equation of line for intersecting line
						//find equation of line for intersecting line
						dx =  fromX - GameLevel.getInstance().mPlayer.getX();
						dy =  fromY - GameLevel.getInstance().mPlayer.getY();
						m2 = dy/dx;
						b2 = GameLevel.getInstance().mPlayer.getY() - ((GameLevel.getInstance().mPlayer.getX())*m2);
						
						//Now solve for the point of intersection between our boundary line and our intersecting line.
						x = (b2 - b)/(m - m2);
						
						//Now with our x, solve for y in either equation:
						y = (m*x) + b;
						
						//let's get the distance our laser has to travel
						laserdtoTravel = MathUtils.distance(fromX, fromY, x, y);
						//Log.v("",Float.toString(laserdtoTravel));
						//let's get the velocity
						laserVelocity = ResourceManager.getInstance().cameraHeight/1.0f;
						
						laserMoveDuration = (laserdtoTravel/laserVelocity);
						if (GameLevel.getInstance().fireRateEnemy == 1f){
							//do nothing;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.9f){
							laserMoveDuration = laserMoveDuration/1.10f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.8f){
							laserMoveDuration = laserMoveDuration/1.15f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.75f){
							laserMoveDuration = laserMoveDuration/1.20f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.7f){
							laserMoveDuration = laserMoveDuration/1.25f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						}
						//Now with our two points, we can create a move modifier for our laser.
						//line1 = new Line(fromX, fromY, fromX+800f, fromY, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						//line2 = new Line(fromX+800f, fromY, fromX, fromY+480f, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						//line3 = new Line(fromX, fromY+480f, fromX, fromY, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						//line4 = new Line(fromX, fromY, x, y, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						
						//if (count > 0) {
							//GameLevel.getInstance().detachChild(line1);
							//GameLevel.getInstance().detachChild(line2);
							//GameLevel.getInstance().detachChild(line3);
							//GameLevel.getInstance().detachChild(line4);
						//}
						//GameLevel.getInstance().attachChild(line1);
						//GameLevel.getInstance().attachChild(line2);
						//GameLevel.getInstance().attachChild(line3);
						//GameLevel.getInstance().attachChild(line4);
						count++;
						if (sprite.getEntityModifierCount()>=0){ 
							//mod.removeModifierListener(laserModifierListener);
							sprite.clearEntityModifiers();
							//Log.v("", "gets here!");
							mod = new MoveModifier(laserMoveDuration, fromX, fromY, x, y);
							mod.addModifierListener(laserModifierListener);
							sprite.registerEntityModifier(mod);
							
							//GameLevel.getInstance().attachChild(sprite);
							//registerUnregister = true;
							//Log.v("","ENEMY x start " + Float.toString(fromX) + "  y start" + fromY);
							//Log.v("","ENEMY x end " + Float.toString(x) + "  y end" + y);
							//Log.v("","ENEMY distance to travel " + Float.toString(laserdtoTravel));
							//Log.v("","ENEMY laser velocity " + Float.toString(laserVelocity));
							//Log.v("","ENEMY move duration" + Float.toString(laserMoveDuration));
							//Log.v("", "ENEMY Unregister old modifier, register new one");
							//Log.v("","ENEMY Laser icon should fire here!");
							
						}
					} else if ((sprite.getRotation()<= 0.0) && (sprite.getRotation() > -90.0)){

						//equation of boundary line
						m = 0.6f;
						
						b = fromY - ((fromX-ResourceManager.getInstance().cameraHeight)*(m));
						
						//find equation of line for intersecting line
						//find equation of line for intersecting line
						dx =  fromX - GameLevel.getInstance().mPlayer.getX();
						dy =  fromY - GameLevel.getInstance().mPlayer.getY();
						m2 = dy/dx;
						b2 = GameLevel.getInstance().mPlayer.getY() - ((GameLevel.getInstance().mPlayer.getX())*m2);
						
						//Now solve for the point of intersection between our boundary line and our intersecting line.
						x = (b2 - b)/(m - m2);
						
						//Now with our x, solve for y in either equation:
						y = (m*x) + b;
						
						//let's get the distance our laser has to travel
						laserdtoTravel = MathUtils.distance(fromX, fromY, x, y);
						//Log.v("",Float.toString(laserdtoTravel));
						//let's get the velocity
						laserVelocity = ResourceManager.getInstance().cameraHeight/1.0f;
						
						laserMoveDuration = (laserdtoTravel/laserVelocity);
						if (GameLevel.getInstance().fireRateEnemy == 1f){
							//do nothing;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.9f){
							laserMoveDuration = laserMoveDuration/1.10f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.8f){
							laserMoveDuration = laserMoveDuration/1.15f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.75f){
							laserMoveDuration = laserMoveDuration/1.20f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.70f){
							laserMoveDuration = laserMoveDuration/1.25f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						}
						//if (count >0){
							//line1.detachSelf();
							//line2.detachSelf();
							//line3.detachSelf();
							//line4.detachSelf();
						//}
						//Now with our two points, we can create a move modifier for our laser.
						//line1 = new Line(fromX, fromY, fromX-800f, fromY, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						//line2 = new Line(fromX-800f, fromY, fromX, fromY+480f, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						//line3 = new Line(fromX, fromY+480f, fromX, fromY, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						//line4 = new Line(fromX, fromY, x, y, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						
						
							
						
						//GameLevel.getInstance().attachChild(line1);
						//GameLevel.getInstance().attachChild(line2);
						//GameLevel.getInstance().attachChild(line3);
						//GameLevel.getInstance().attachChild(line4);
						count++;
						if (sprite.getEntityModifierCount()>=0){ 
							//mod.removeModifierListener(laserModifierListener);
							sprite.clearEntityModifiers();
							//Log.v("", "gets here!");
							mod = new MoveModifier(laserMoveDuration, fromX, fromY, x, y);
							mod.addModifierListener(laserModifierListener);
							sprite.registerEntityModifier(mod);
							
							//GameLevel.getInstance().attachChild(sprite);
							//registerUnregister = true;
							//Log.v("","ENEMY x start " + Float.toString(fromX) + "  y start" + fromY);
							//Log.v("","ENEMY x end " + Float.toString(x) + "  y end" + y);
							//Log.v("","ENEMY distance to travel " + Float.toString(laserdtoTravel));
							//Log.v("","ENEMY laser velocity " + Float.toString(laserVelocity));
							//Log.v("","ENEMY move duration" + Float.toString(laserMoveDuration));
							//Log.v("", "ENEMY Unregister old modifier, register new one");
							//Log.v("","ENEMY Laser icon should fire here!");
							
						}
						
						//GameLevel.getInstance().resetEntityModifiers();
						
						//GameLevel.getInstance().laserFired = true;
					} else {
						//GameLevel.getInstance().laserFired = false;
						//equation of boundary line
						m = -0.6f;
						
						b = (fromY) - (fromX-ResourceManager.getInstance().cameraHeight)*(m);
						
						//find equation of line for intersecting line
						dx =  fromX - GameLevel.getInstance().mPlayer.getX();
						dy =  fromY - GameLevel.getInstance().mPlayer.getY();
						m2 = dy/dx;
						b2 = GameLevel.getInstance().mPlayer.getY() - ((GameLevel.getInstance().mPlayer.getX())*m2);
						
						//Now solve for the point of intersection between our boundary line and our intersecting line.
						x = (b2 - b)/(m - m2);
						
						//Now with our x, solve for y in either equation:
						y = (m*x) + b;
						
						//let's get the distance our laser has to travel
						laserdtoTravel = MathUtils.distance(fromX, fromY, x, y);
						//Log.v("",Float.toString(laserdtoTravel));
						//let's get the velocity
						laserVelocity = ResourceManager.getInstance().cameraHeight/1.0f;
						
						laserMoveDuration = (laserdtoTravel/laserVelocity);
						if (GameLevel.getInstance().fireRateEnemy == 1f){
							//do nothing;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.9f){
							laserMoveDuration = laserMoveDuration/1.10f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.80f){
							laserMoveDuration = laserMoveDuration/1.15f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.75f){
							laserMoveDuration = laserMoveDuration/1.20f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						} else if (GameLevel.getInstance().fireRateEnemy == 0.70f){
							laserMoveDuration = laserMoveDuration/1.25f;
							//GameLevel.getInstance().fireRateDuration = laserMoveDuration;
						}
						//Now with our two points, we can create a move modifier for our laser.
						//line1 = new Line(fromX, fromY, fromX-800f, fromY, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						//line2 = new Line(fromX-800f, fromY, fromX, fromY-480f, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						//line3 = new Line(fromX, fromY-480f, fromX, fromY, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						//line4 = new Line(fromX, fromY, x, y, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
						
						//if (count > 0) {
							//GameLevel.getInstance().detachChild(line1);
							//GameLevel.getInstance().detachChild(line2);
							//GameLevel.getInstance().detachChild(line3);
							//GameLevel.getInstance().detachChild(line4);
						//}
						//GameLevel.getInstance().attachChild(line1);
						//GameLevel.getInstance().attachChild(line2);
						//GameLevel.getInstance().attachChild(line3);
						//GameLevel.getInstance().attachChild(line4);
						count++;
						if (sprite.getEntityModifierCount()>=0){ 
							//mod.removeModifierListener(laserModifierListener);
							sprite.clearEntityModifiers();
							//Log.v("", "gets here!");
							mod = new MoveModifier(laserMoveDuration, fromX, fromY, x, y);
							mod.addModifierListener(laserModifierListener);
							sprite.registerEntityModifier(mod);
							
							//GameLevel.getInstance().attachChild(sprite);
							//registerUnregister = true;
							//Log.v("","ENEMY x start " + Float.toString(fromX) + "  y start" + fromY);
							//Log.v("","ENEMY x end " + Float.toString(x) + "  y end" + y);
							//Log.v("","ENEMY distance to travel " + Float.toString(laserdtoTravel));
							//Log.v("","ENEMY laser velocity " + Float.toString(laserVelocity));
							//Log.v("","ENEMY move duration" + Float.toString(laserMoveDuration));
							//Log.v("", "ENEMY Unregister old modifier, register new one");
							//Log.v("","ENEMY Laser icon should fire here!");
							
						}
					}
					
				
	}
}
