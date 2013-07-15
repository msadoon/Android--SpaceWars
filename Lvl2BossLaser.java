package mkat.apps.spacewars;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;

public class Lvl2BossLaser extends Sprite{
		Sprite sprite = null; 
		Lvl2Boss callingBossHere = null;
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


	public Lvl2BossLaser(ITextureRegion pTextureRegion, float x, float y, float rAngle, Lvl2Boss callingBoss) {
		super(x,y,pTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		fromX = x;
		fromY = y;
		sprite = this;
		rotAngle = rAngle;
		//Log.v("","creates enemy laser");
		setupBossLaser();
		callingBossHere = callingBoss;
	}

	//setup laser:
	private void setupBossLaser() {
		//Log.v("","sets up enemy laser");
		//Log.v("", "value of laserFired " + GameLevel.getInstance().laserFired);
		
		sprite.setVisible(true);
		sprite.setCullingEnabled(true);
		sprite.setIgnoreUpdate(false);
		sprite.setColor(1, 1, 1);
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
				callingBossHere.bulletLifeEnd = true;
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
						
						count++;
						if (sprite.getEntityModifierCount()>=0){ 
							//mod.removeModifierListener(laserModifierListener);
							sprite.clearEntityModifiers();
							//Log.v("", "gets here!");
							mod = new MoveModifier(laserMoveDuration, fromX, fromY, x, y);
							scale = new ScaleModifier(laserMoveDuration, 1, 10);
							mod.addModifierListener(laserModifierListener);
							//scale.addModifierListener(laserModifierListener);
							sprite.registerEntityModifier(mod);
							sprite.registerEntityModifier(scale);

							
						}

					} else if ((sprite.getRotation()<= -270.0) && (sprite.getRotation() >= -360.0)){
						
						
						//equation of boundary line
						m = -0.6f;
						
						b = fromY - ((fromX+ResourceManager.getInstance().cameraWidth)*(m));
						
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

						count++;
						if (sprite.getEntityModifierCount()>=0){ 
							//mod.removeModifierListener(laserModifierListener);
							sprite.clearEntityModifiers();
							//Log.v("", "gets here!");
							mod = new MoveModifier(laserMoveDuration, fromX, fromY, x, y);
							scale = new ScaleModifier(laserMoveDuration, 1, 10);
							mod.addModifierListener(laserModifierListener);
							scale.addModifierListener(laserModifierListener);
							sprite.registerEntityModifier(mod);
							sprite.registerEntityModifier(scale);
							
						}
					} else if ((sprite.getRotation()<= 0.0) && (sprite.getRotation() > -90.0)){

						//equation of boundary line
						m = 0.6f;
						
						b = fromY - ((fromX-ResourceManager.getInstance().cameraWidth)*(m));
						
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

						count++;
						if (sprite.getEntityModifierCount()>=0){ 
							//mod.removeModifierListener(laserModifierListener);
							sprite.clearEntityModifiers();
							//Log.v("", "gets here!");
							mod = new MoveModifier(laserMoveDuration, fromX, fromY, x, y);
							scale = new ScaleModifier(laserMoveDuration, 1, 10);
							mod.addModifierListener(laserModifierListener);
							scale.addModifierListener(laserModifierListener);
							sprite.registerEntityModifier(mod);
							sprite.registerEntityModifier(scale);

							
						}

					} else {
						//GameLevel.getInstance().laserFired = false;
						//equation of boundary line
						m = -0.6f;
						
						b = (fromY) - (fromX-ResourceManager.getInstance().cameraWidth)*(m);
						
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

						count++;
						if (sprite.getEntityModifierCount()>=0){ 
							//mod.removeModifierListener(laserModifierListener);
							sprite.clearEntityModifiers();
							//Log.v("", "gets here!");
							mod = new MoveModifier(laserMoveDuration, fromX, fromY, x, y);
							scale = new ScaleModifier(laserMoveDuration, 1, 10);
							mod.addModifierListener(laserModifierListener);
							scale.addModifierListener(laserModifierListener);
							sprite.registerEntityModifier(mod);
							sprite.registerEntityModifier(scale);
							
							
						}
					}
					
				
	}
}

