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

import android.util.Log;

public class Lvl1BossLaser extends Sprite{

	Sprite sprite = null;
	Lvl1Boss callingBossHere = null;
	int count = 0;
	float fromX = 0;
	float fromY = 0;
	float rotAngle = 0;
	float a, o = 0; //Point of intersection between both lines
	float laserdtoTravel = 0;
	float laserVelocity, laserMoveDuration = 0;
	boolean registerUnregister = false;
	Line line1, line2, line3, line4;
	MoveModifier mod;
	ScaleModifier scale;
	IEntityModifierListener laserModifierListener;
	
	
	public Lvl1BossLaser(ITextureRegion pTextureRegion, float x, float y, float rAngle, Lvl1Boss callingBoss) {
		super(x,y,pTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		fromX = x;
		fromY = y;
		rotAngle = rAngle;
		sprite = this;
		sprite.setVisible(true);
		sprite.setCullingEnabled(true);
		sprite.setIgnoreUpdate(false);
		sprite.setColor(1, 1, 1);
			sprite.setRotation(rotAngle);
			Log.v("","creates boss lasers");
			setupBossLaser(sprite);
			callingBossHere = callingBoss;
			//GameLevel.getInstance().attachChild(allLasers[g]);
		
		
	
	}

	//setup laser:
	private void setupBossLaser(Sprite laser) {
		//Log.v("","sets up enemy laser");
		//Log.v("", "value of laserFired " + GameLevel.getInstance().laserFired);
		
		//sprite.dispose();
		//.setAnchorCenter(.getHeight(), .getWidth());
		//set the laser to invisible exactly as soon as it finishes reaching its destination
		
		
		//.setPosition(GameLevel.getInstance().touchX,GameLevel.getInstance().touchY);
		final Sprite laser2;
			laser2 = laser;
			laserModifierListener = new IEntityModifierListener() {
				
				//When the modifier starts, this method is called 
				@Override
				public void onModifierStarted(IModifier<IEntity> pModifier, final IEntity pItem){
				
				}
		
				//When the modifier finishes, this method is called
				@Override
				public void onModifierFinished(final IModifier<IEntity> pModifer, final IEntity pItem){
					laser2.setVisible(false);	
					callingBossHere.bulletLifeEnd = true;
				}
					
			
					//AFTER THIS PLAYER"S LIFE = 0, so game over, dump current scene, load game over text and quit to menu.
			};
			Log.v("","which sprite");
			
		
			if (((laser2.getRotation()==-45))){
				//equation of the circle:
				a = (float)Math.cos(MathUtils.degToRad(-45f))*(ResourceManager.getInstance().cameraWidth*0.375f);
				o = (float)Math.sin(MathUtils.degToRad(-45f))*(ResourceManager.getInstance().cameraWidth*0.375f);
				//laser2.setRotation(-90);
				//add a to y, o to x
								
				//let's get the distance our laser has to travel
				laserdtoTravel = MathUtils.distance(fromX, fromY, (fromX-o), (fromY+a));
				//Log.v("",Float.toString(laserdtoTravel));
				//let's get the velocity
				laserVelocity = ResourceManager.getInstance().cameraHeight/1.0f;
				
				laserMoveDuration = (laserdtoTravel/laserVelocity);
				
				count++;
				if (laser2.getEntityModifierCount()>=0){ 
					//mod.removeModifierListener(laserModifierListener);
					laser2.clearEntityModifiers();
					//Log.v("", "gets here!");
					mod = new MoveModifier(laserMoveDuration, fromX, fromY, fromX-o, fromY+a);
					scale = new ScaleModifier(laserMoveDuration, 1, 10);
					scale.addModifierListener(laserModifierListener);
					mod.addModifierListener(laserModifierListener);
					laser2.registerEntityModifier(mod);
					laser2.registerEntityModifier(scale);
					
				}
				
				
				
			} else if (((laser2.getRotation()==-90))){
				//equation of the circle:
				
				//add a to y, o to x
								
				//let's get the distance our laser has to travel
				laserdtoTravel = MathUtils.distance(fromX, fromY, (fromX-(ResourceManager.getInstance().cameraWidth*0.375f)), (fromY));
				//Log.v("",Float.toString(laserdtoTravel));
				//let's get the velocity
				laserVelocity = ResourceManager.getInstance().cameraHeight/1.0f;
				
				laserMoveDuration = (laserdtoTravel/laserVelocity);
				
				count++;
				if (laser2.getEntityModifierCount()>=0){ 
					//mod.removeModifierListener(laserModifierListener);
					laser2.clearEntityModifiers();
					//Log.v("", "gets here!");
					mod = new MoveModifier(laserMoveDuration, fromX, fromY, fromX-(ResourceManager.getInstance().cameraWidth*0.375f), fromY);
					mod.addModifierListener(laserModifierListener);
					scale = new ScaleModifier(laserMoveDuration, 1, 10);
					scale.addModifierListener(laserModifierListener);
					laser2.registerEntityModifier(mod);
					laser2.registerEntityModifier(scale);
					
				}
				
				
				
			} else if (((laser2.getRotation()==-135))){
				//equation of the circle:
				a = (float)Math.cos(MathUtils.degToRad(-45f))*(ResourceManager.getInstance().cameraWidth*0.375f);
				o = (float)Math.sin(MathUtils.degToRad(-45f))*(ResourceManager.getInstance().cameraWidth*0.375f);
				//laser2.setRotation(0);
				//add a to y, o to x
								
				//let's get the distance our laser has to travel
				laserdtoTravel = MathUtils.distance(fromX, fromY, (fromX-o), (fromY-a));
				//Log.v("",Float.toString(laserdtoTravel));
				//let's get the velocity
				laserVelocity = (ResourceManager.getInstance().cameraHeight)/1.0f;
				
				laserMoveDuration = (laserdtoTravel/laserVelocity);
				
				count++;
				if (laser2.getEntityModifierCount()>=0){ 
					//mod.removeModifierListener(laserModifierListener);
					laser2.clearEntityModifiers();
					//Log.v("", "gets here!");
					mod = new MoveModifier(laserMoveDuration, fromX, fromY, fromX-o, fromY-a);
					mod.addModifierListener(laserModifierListener);
					scale = new ScaleModifier(laserMoveDuration, 1, 10);
					scale.addModifierListener(laserModifierListener);
					laser2.registerEntityModifier(mod);
					laser2.registerEntityModifier(scale);
					
				}
				
				
				
			} else if (((laser2.getRotation()==-180))){
				//equation of the circle:
				
				//add a to y, o to x
								
				//let's get the distance our laser has to travel
				laserdtoTravel = MathUtils.distance(fromX, fromY, (fromX), (fromY-(ResourceManager.getInstance().cameraWidth*0.375f)));
				//Log.v("",Float.toString(laserdtoTravel));
				//let's get the velocity
				laserVelocity = (ResourceManager.getInstance().cameraHeight)/1.0f;
				
				laserMoveDuration = (laserdtoTravel/laserVelocity);
				
				count++;
				if (laser2.getEntityModifierCount()>=0){ 
					//mod.removeModifierListener(laserModifierListener);
					laser2.clearEntityModifiers();
					//Log.v("", "gets here!");
					mod = new MoveModifier(laserMoveDuration, fromX, fromY, fromX, fromY-(ResourceManager.getInstance().cameraWidth*0.375f));
					mod.addModifierListener(laserModifierListener);
					scale = new ScaleModifier(laserMoveDuration, 1, 10);
					scale.addModifierListener(laserModifierListener);
					laser2.registerEntityModifier(mod);
					laser2.registerEntityModifier(scale);
					
				}
				
				
				
			} else if (((laser2.getRotation()==-225))){
				//equation of the circle:
				a = (float)Math.cos(MathUtils.degToRad(-45f))*(ResourceManager.getInstance().cameraWidth*0.375f);
				o = (float)Math.sin(MathUtils.degToRad(-45f))*(ResourceManager.getInstance().cameraWidth*0.375f);
				//laser2.setRotation(-45);
				//add a to y, o to x
								
				//let's get the distance our laser has to travel
				laserdtoTravel = MathUtils.distance(fromX, fromY, (fromX+o), (fromY-a));
				//Log.v("",Float.toString(laserdtoTravel));
				//let's get the velocity
				laserVelocity = (ResourceManager.getInstance().cameraHeight)/1.0f;
				
				laserMoveDuration = (laserdtoTravel/laserVelocity);
				
				count++;
				if (laser2.getEntityModifierCount()>=0){ 
					//mod.removeModifierListener(laserModifierListener);
					laser2.clearEntityModifiers();
					//Log.v("", "gets here!");
					mod = new MoveModifier(laserMoveDuration, fromX, fromY, fromX+o, fromY-a);
					mod.addModifierListener(laserModifierListener);
					scale = new ScaleModifier(laserMoveDuration, 1, 10);
					scale.addModifierListener(laserModifierListener);
					laser2.registerEntityModifier(mod);
					laser2.registerEntityModifier(scale);
					
				}
				
				
				
			} else if (((laser2.getRotation()==-270))){
				//equation of the circle:
				
				//add a to y, o to x
								
				//let's get the distance our laser has to travel
				laserdtoTravel = MathUtils.distance(fromX, fromY, (fromX+(ResourceManager.getInstance().cameraWidth*0.375f)), (fromY));
				//Log.v("",Float.toString(laserdtoTravel));
				//let's get the velocity
				laserVelocity = (ResourceManager.getInstance().cameraHeight)/1.0f;
				
				laserMoveDuration = (laserdtoTravel/laserVelocity);
				
				count++;
				if (laser2.getEntityModifierCount()>=0){ 
					//mod.removeModifierListener(laserModifierListener);
					laser2.clearEntityModifiers();
					//Log.v("", "gets here!");
					mod = new MoveModifier(laserMoveDuration, fromX, fromY, fromX+(ResourceManager.getInstance().cameraWidth*0.375f), fromY);
					mod.addModifierListener(laserModifierListener);
					scale = new ScaleModifier(laserMoveDuration, 1, 10);
					scale.addModifierListener(laserModifierListener);
					laser2.registerEntityModifier(mod);
					
					laser2.registerEntityModifier(scale);
				}
				
				
				
			} else if (((laser2.getRotation()==-315))){
				//equation of the circle:
				a = (float)Math.cos(MathUtils.degToRad(-45))*(ResourceManager.getInstance().cameraWidth*0.375f);
				o = (float)Math.sin(MathUtils.degToRad(-45))*(ResourceManager.getInstance().cameraWidth*0.375f);
				//laser2.setRotation(0);
				//add a to y, o to x
								
				//let's get the distance our laser has to travel
				laserdtoTravel = MathUtils.distance(fromX, fromY, (fromX+o), (fromY+a));
				//Log.v("",Float.toString(laserdtoTravel));
				//let's get the velocity
				laserVelocity = (ResourceManager.getInstance().cameraHeight)/1.0f;
				
				laserMoveDuration = (laserdtoTravel/laserVelocity);
				
				count++;
				if (laser2.getEntityModifierCount()>=0){ 
					//mod.removeModifierListener(laserModifierListener);
					laser2.clearEntityModifiers();
					//Log.v("", "gets here!");
					mod = new MoveModifier(laserMoveDuration, fromX, fromY, fromX+o, fromY+a);
					mod.addModifierListener(laserModifierListener);
					scale = new ScaleModifier(laserMoveDuration, 1, 10);
					scale.addModifierListener(laserModifierListener);
					laser2.registerEntityModifier(mod);
					laser2.registerEntityModifier(scale);
					
				}
				
				
				
			} else if (((laser2.getRotation()==-360))){
				//equation of the circle:
				
				//add a to y, o to x
								
				//let's get the distance our laser has to travel
				laserdtoTravel = MathUtils.distance(fromX, fromY, (fromX), (fromY+(ResourceManager.getInstance().cameraWidth*0.375f)));
				//Log.v("",Float.toString(laserdtoTravel));
				//let's get the velocity
				laserVelocity = (ResourceManager.getInstance().cameraHeight)/1.0f;
				
				laserMoveDuration = (laserdtoTravel/laserVelocity);
				
				count++;
				if (laser2.getEntityModifierCount()>=0){ 
					//mod.removeModifierListener(laserModifierListener);
					laser2.clearEntityModifiers();
					//Log.v("", "gets here!");
					mod = new MoveModifier(laserMoveDuration, fromX, fromY, fromX, fromY+(ResourceManager.getInstance().cameraWidth*0.375f));
					mod.addModifierListener(laserModifierListener);
					scale = new ScaleModifier(laserMoveDuration, 1, 10);
					scale.addModifierListener(laserModifierListener);
					laser2.registerEntityModifier(mod);
					laser2.registerEntityModifier(scale);
					
					
				}
				
				
				
			} 
					
				
	}
}

