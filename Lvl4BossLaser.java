package mkat.apps.spacewars;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.CardinalSplineMoveModifier;
import org.andengine.entity.modifier.CardinalSplineMoveModifier.CardinalSplineMoveModifierConfig;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;

import android.opengl.GLES20;

public class Lvl4BossLaser extends Sprite{
		Sprite sprite = null; 
		Lvl4Boss callingBossHere = null;
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
		//MoveModifier mod;
		CardinalSplineMoveModifier path;
		CardinalSplineMoveModifierConfig pathPoints;
		IEntityModifierListener laserModifierListener;


	public Lvl4BossLaser(ITextureRegion pTextureRegion, float x, float y, float rAngle, Lvl4Boss callingBoss) {
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
		sprite.setIgnoreUpdate(false);
		//sprite.setColor(1, 1, 1);
		sprite.setScale(3);
		final float pTension = MathUtils.random(0f, 1f);
		sprite.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
			sprite.setColor(pTension, 1, 1, 1);
		//sprite.setAnchorCenter(sprite.getHeight(), sprite.getWidth());
		//set the laser to invisible exactly as soon as it finishes reaching its destination
	
		laserModifierListener = new IEntityModifierListener() {
			
			 
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, final IEntity pItem){
			
			}
	
			
			@Override
			public void onModifierFinished(final IModifier<IEntity> pModifer, final IEntity pItem){
				sprite.setVisible(false);	
				callingBossHere.bulletLifeEnd = true;
			}
				
		
				
		};
		
		//sprite.setPosition(GameLevel.getInstance().touchX,GameLevel.getInstance().touchY);
		sprite.setRotation(rotAngle);
		//fromX = GameLevel.getInstance().enemyX2;
		//fromY = GameLevel.getInstance().enemyY2;
		//mod = new MoveModifier(.5f, fromX, fromY, 1000, 1000);
				
					if (((sprite.getRotation()<= -180.0) && (sprite.getRotation() > -270.0))){
						
					
						
						//equation of boundary line
						m = 0.6f;
						
						b = fromY - ((fromX+ResourceManager.getInstance().cameraHeight)*(m));
						
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
						laserVelocity = ResourceManager.getInstance().cameraWidth/2.0f;
						
						laserMoveDuration = (laserdtoTravel/laserVelocity);
						
						//setup path modifier
						
						//setup control points point1:boss x,y; point2,3,4 are player x,y
						final float pointsListX[] = {
								fromX+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)), fromX+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),fromX+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),x+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f))
						};
						final float pointsListY[] = {
								fromY-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)), fromY-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),fromY-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),y-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f))
						};
						
						final int controlPointCount = pointsListX.length;
						pathPoints = new CardinalSplineMoveModifierConfig(controlPointCount, -1f);
						
						for (int i = 0; i < controlPointCount; i++){
							final float positionX = pointsListX[i];
							final float positionY = pointsListY[i];
							pathPoints.setControlPoint(i, (int)Math.round(positionX), (int)Math.round(positionY));
						}
						//end setuppath modifier
						count++;
						if (sprite.getEntityModifierCount()>=0){ 
							//mod.removeModifierListener(laserModifierListener);
							sprite.clearEntityModifiers();
							//Log.v("", "gets here!");
							//mod = new MoveModifier(laserMoveDuration, fromX, fromY, x, y);
							path = new CardinalSplineMoveModifier(laserMoveDuration, pathPoints);
							//mod.addModifierListener(laserModifierListener);
							path.addModifierListener(laserModifierListener);
							//sprite.registerEntityModifier(mod);
							sprite.registerEntityModifier(new SequenceEntityModifier (new DelayModifier(0.25f), new ParallelEntityModifier(path, new RotationModifier(laserMoveDuration,0,-360))));

							
						}

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
						laserVelocity = ResourceManager.getInstance().cameraWidth/2.0f;
						
						laserMoveDuration = (laserdtoTravel/laserVelocity);

						//setup path modifier
						
						//setup control points point1:boss x,y; point2,3,4 are player x,y
						final float pointsListX[] = {
								fromX+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)), fromX+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),fromX+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),x+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f))
						};
						final float pointsListY[] = {
								fromY+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)), fromY+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),fromY+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),y+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f))
						};
						
						final int controlPointCount = pointsListX.length;
						pathPoints = new CardinalSplineMoveModifierConfig(controlPointCount, -1);
						
						for (int i = 0; i < controlPointCount; i++){
							final float positionX = pointsListX[i];
							final float positionY = pointsListY[i];
							pathPoints.setControlPoint(i, (int)Math.round(positionX), (int)Math.round(positionY));
						}
						//end setuppath modifier
						
						count++;
						if (sprite.getEntityModifierCount()>=0){ 
							//mod.removeModifierListener(laserModifierListener);
							sprite.clearEntityModifiers();
							//Log.v("", "gets here!");
							//mod = new MoveModifier(laserMoveDuration, fromX, fromY, x, y);
							path = new CardinalSplineMoveModifier(laserMoveDuration, pathPoints);
							//mod.addModifierListener(laserModifierListener);
							path.addModifierListener(laserModifierListener);
							//sprite.registerEntityModifier(mod);
							sprite.registerEntityModifier(new SequenceEntityModifier (new DelayModifier(0.25f), new ParallelEntityModifier(path, new RotationModifier(laserMoveDuration,0,-360))));

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
						laserVelocity = ResourceManager.getInstance().cameraWidth/2.0f;
						
						laserMoveDuration = (laserdtoTravel/laserVelocity);

						//setup path modifier
						
						//setup control points point1:boss x,y; point2,3,4 are player x,y
						final float pointsListX[] = {
								fromX-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)), fromX-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),fromX-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),x-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f))
						};
						final float pointsListY[] = {
								fromY+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)), fromY+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),fromY+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),y+(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f))
						};
						
						final int controlPointCount = pointsListX.length;
						pathPoints = new CardinalSplineMoveModifierConfig(controlPointCount, -1);
						
						for (int i = 0; i < controlPointCount; i++){
							final float positionX = pointsListX[i];
							final float positionY = pointsListY[i];
							pathPoints.setControlPoint(i, (int)Math.round(positionX), (int)Math.round(positionY));
						}
						//end setuppath modifier
						
						count++;
						if (sprite.getEntityModifierCount()>=0){ 
							//mod.removeModifierListener(laserModifierListener);
							sprite.clearEntityModifiers();
							//Log.v("", "gets here!");
							//mod = new MoveModifier(laserMoveDuration, fromX, fromY, x, y);
							path = new CardinalSplineMoveModifier(laserMoveDuration, pathPoints);
							//mod.addModifierListener(laserModifierListener);
							path.addModifierListener(laserModifierListener);
							//sprite.registerEntityModifier(mod);
							sprite.registerEntityModifier(new SequenceEntityModifier (new DelayModifier(0.25f), new ParallelEntityModifier(path, new RotationModifier(laserMoveDuration,0,-360))));


							
						}

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
						laserVelocity = ResourceManager.getInstance().cameraWidth/2.0f;
						
						laserMoveDuration = (laserdtoTravel/laserVelocity);

						//setup path modifier
						
						//setup control points point1:boss x,y; point2,3,4 are player x,y
						final float pointsListX[] = {
								fromX-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)), fromX-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),fromX-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),x-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f))
						};
						final float pointsListY[] = {
								fromY-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)), fromY-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),fromY-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f)),y-(float)(Math.random()*(ResourceManager.getInstance().cameraWidth*0.25f))
						};
						
						final int controlPointCount = pointsListX.length;
						pathPoints = new CardinalSplineMoveModifierConfig(controlPointCount, -1);
						
						for (int i = 0; i < controlPointCount; i++){
							final float positionX = pointsListX[i];
							final float positionY = pointsListY[i];
							pathPoints.setControlPoint(i, (int)Math.round(positionX), (int)Math.round(positionY));
						}
						//end setuppath modifier
						
						count++;
						if (sprite.getEntityModifierCount()>=0){ 
							//mod.removeModifierListener(laserModifierListener);
							sprite.clearEntityModifiers();
							//Log.v("", "gets here!");
							//mod = new MoveModifier(laserMoveDuration, fromX, fromY, x, y);
							path = new CardinalSplineMoveModifier(laserMoveDuration, pathPoints);
							//mod.addModifierListener(laserModifierListener);
							path.addModifierListener(laserModifierListener);
							//sprite.registerEntityModifier(mod);
							sprite.registerEntityModifier(new SequenceEntityModifier (new DelayModifier(0.25f), new ParallelEntityModifier(path, new RotationModifier(laserMoveDuration,0,-360))));

							
							
						}
					}
					
				
	}
}

