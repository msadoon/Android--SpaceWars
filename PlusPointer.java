package mkat.apps.spacewars;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.math.MathUtils;

import android.util.Log;



public class PlusPointer extends Sprite{
	Sprite pointer, powerup1;
	Line line;
	String type1;
	//mm = new MoveModifier(1, GameLevel.getInstance().mPlayer.getX(), GameLevel.getInstance().mPlayer.getY(), sprite.getX(),sprite.getY());
	//pointer.registerEntityModifier(mm);
	public PlusPointer(ITextureRegion pTextureRegion, Sprite powerup, String type) {
		super(GameLevel.getInstance().mPlayer.getX(),GameLevel.getInstance().mPlayer.getY(), pTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		
		pointer = this;
		powerup1 = powerup;
		type1 = type;
		
		if (type1 == "Health"){
			line = GameLevel.getInstance().plusPointerHealthLine;
			Log.v("","LINE IS HEALTH");
		} else if (type1 == "FireRate"){
			line = GameLevel.getInstance().plusPointerRateLine;
			Log.v("","LINE IS RATE");
		} else if (type1 == "FirePower"){
			line = GameLevel.getInstance().plusPointerPowerLine;
			Log.v("","LINE IS POWER");
		} else {
			line = null;
		}
		setupPointer();
		
	}
	
	//setup laser:
	private void setupPointer() {
		
		pointer.setAlpha(1f);
		//thisGameLevel.attachChild(pointer);
		//END COMPASS
		//COMPASS
		//Log.v("","number of enemies in array " + enemyCount);
			
					
					GameLevel.getInstance().registerUpdateHandler(new IUpdateHandler() {
						
						
						@Override
						public void onUpdate(float pSecondsElapsed) {
						
						//Pointer for Compass
						if (GameLevel.getInstance().PlayerReady){
							
							
							if (type1 == "Health" && (GameLevel.getInstance().plusPointerHealthLine != null)){
								GameLevel.getInstance().plusPointerHealthLine.setPosition(powerup1.getX(), powerup1.getY(), GameLevel.getInstance().mPlayer.getX(), GameLevel.getInstance().mPlayer.getY());
							} 
							if (type1 == "FireRate" && (GameLevel.getInstance().plusPointerRateLine != null)){
								GameLevel.getInstance().plusPointerRateLine.setPosition(powerup1.getX(), powerup1.getY(), GameLevel.getInstance().mPlayer.getX(), GameLevel.getInstance().mPlayer.getY());
							} 
							if (type1 == "FirePower" && (GameLevel.getInstance().plusPointerPowerLine != null)){
								GameLevel.getInstance().plusPointerPowerLine.setPosition(powerup1.getX(), powerup1.getY(), GameLevel.getInstance().mPlayer.getX(), GameLevel.getInstance().mPlayer.getY());
							}
							
							//COMPASS
							final float dX = GameLevel.getInstance().mPlayer.getX() - powerup1.getX();
							final float dY = GameLevel.getInstance().mPlayer.getY() - powerup1.getY();
							/* Calculate the angle of rotation in radians*/
							final float angle = (float) Math.atan2(-dY, dX);
						
							/* Convert the angle from radians to degrees, adding the
							default image rotation */
							final float rotation = MathUtils.radToDeg(angle-90);
							/* Set the arrow's new rotation */
							pointer.setRotation(rotation);
							
							if ( (line != null) && line.collidesWith(GameLevel.getInstance().mPlayer.compass1)){
								//equation of compass 1
								float x = GameLevel.getInstance().mPlayer.compass1.getX1();
						
								//find the equation of direction line between enemy and player
								float slope = (line.getY1() - line.getY2())/(line.getX1()-line.getX2());
								float b = line.getY1() - (slope * line.getX1());
						
								//substitute x into y and we get our point of intersection
								float y = slope*x + b;
						
								//x and y are where we put our pointer (subtract 5 pixels for viewing purposes)
								pointer.setPosition(x+5, y);
						
							} else if ( (line != null) && line.collidesWith(GameLevel.getInstance().mPlayer.compass2)) {
								//equation of compass 2
								float y = GameLevel.getInstance().mPlayer.compass2.getY1();
						
								//find the equation of direction line between enemy and player
								float slope = (line.getY1() - line.getY2())/(line.getX1()-line.getX2());
								float b = line.getY1() - (slope * line.getX1());
						
								//substitute x into y and we get our point of intersection
								float x = (y - b)/slope;
						
								//x and y are where we put our pointer (subtract 5 pixels for viewing purposes)
								pointer.setPosition(x, y-5);
						
							} else if ( (line != null) && line.collidesWith(GameLevel.getInstance().mPlayer.compass3)) {
								//equation of compass 3
								float x = GameLevel.getInstance().mPlayer.compass3.getX1();
						
								//find the equation of direction line between enemy and player
								float slope = (line.getY1() - line.getY2())/(line.getX1()-line.getX2());
								float b = line.getY1() - (slope * line.getX1());
						
								//substitute x into y and we get our point of intersection
								float y = slope*x + b;
						
								//x and y are where we put our pointer (subtract 5 pixels for viewing purposes)
								pointer.setPosition(x-5, y);
						
							} else if ( (line != null) && line.collidesWith(GameLevel.getInstance().mPlayer.compass4)) {
								//equation of compass 2
								float y = GameLevel.getInstance().mPlayer.compass4.getY1();
						
								//find the equation of direction line between enemy and player
								float slope = (line.getY1() - line.getY2())/(line.getX1()-line.getX2());
								float b = line.getY1() - (slope * line.getX1());
						
								//substitute x into y and we get our point of intersection
								float x = (y - b)/slope;
						
								//x and y are where we put our pointer (subtract 5 pixels for viewing purposes)
								pointer.setPosition(x, y+5);
						
							}
							
						}
					}

						@Override
						public void reset() {
							// TODO Auto-generated method stub
							
						}
					});
					//END COMPASS
			}
	
		

			
	}
	