package mkat.apps.spacewars;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.math.MathUtils;



public class BossPointer extends Sprite{
	Sprite pointer;
	Boss associatedBoss;
	//mm = new MoveModifier(1, GameLevel.getInstance().mPlayer.getX(), GameLevel.getInstance().mPlayer.getY(), sprite.getX(),sprite.getY());
	//pointer.registerEntityModifier(mm);
	public BossPointer(ITextureRegion pTextureRegion, Boss toBoss) {
		super(GameLevel.getInstance().mPlayer.getX(),GameLevel.getInstance().mPlayer.getY(), pTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		
		pointer = this;
		associatedBoss = toBoss;
		setupPointer();
		
	}
	
	//setup laser:
	private void setupPointer() {
		pointer.setCullingEnabled(true);
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
							
							
							//COMPASS
							final float dX = GameLevel.getInstance().mPlayer.getX() - associatedBoss.getX();
							final float dY = GameLevel.getInstance().mPlayer.getY() - associatedBoss.getY();
							/* Calculate the angle of rotation in radians*/
							final float angle = (float) Math.atan2(-dY, dX);
						
							/* Convert the angle from radians to degrees, adding the
							default image rotation */
							final float rotation = MathUtils.radToDeg(angle-90);
							/* Set the arrow's new rotation */
							pointer.setRotation(rotation);
							
							if ((GameLevel.getInstance().bossPointerLine != null) && GameLevel.getInstance().bossPointerLine.collidesWith(GameLevel.getInstance().mPlayer.compass1)){
								//equation of compass 1
								float x = GameLevel.getInstance().mPlayer.compass1.getX1();
						
								//find the equation of direction line between enemy and player
								float slope = (GameLevel.getInstance().bossPointerLine.getY1() - GameLevel.getInstance().bossPointerLine.getY2())/(GameLevel.getInstance().bossPointerLine.getX1()-GameLevel.getInstance().bossPointerLine.getX2());
								float b = GameLevel.getInstance().bossPointerLine.getY1() - (slope * GameLevel.getInstance().bossPointerLine.getX1());
						
								//substitute x into y and we get our point of intersection
								float y = slope*x + b;
						
								//x and y are where we put our pointer (subtract 5 pixels for viewing purposes)
								pointer.setPosition(x+5, y);
						
							} else if ((GameLevel.getInstance().bossPointerLine != null) && GameLevel.getInstance().bossPointerLine.collidesWith(GameLevel.getInstance().mPlayer.compass2)) {
								//equation of compass 2
								float y = GameLevel.getInstance().mPlayer.compass2.getY1();
						
								//find the equation of direction line between enemy and player
								float slope = (GameLevel.getInstance().bossPointerLine.getY1() - GameLevel.getInstance().bossPointerLine.getY2())/(GameLevel.getInstance().bossPointerLine.getX1()-GameLevel.getInstance().bossPointerLine.getX2());
								float b = GameLevel.getInstance().bossPointerLine.getY1() - (slope * GameLevel.getInstance().bossPointerLine.getX1());
						
								//substitute x into y and we get our point of intersection
								float x = (y - b)/slope;
						
								//x and y are where we put our pointer (subtract 5 pixels for viewing purposes)
								pointer.setPosition(x, y-5);
						
							} else if ((GameLevel.getInstance().bossPointerLine != null) && GameLevel.getInstance().bossPointerLine.collidesWith(GameLevel.getInstance().mPlayer.compass3)) {
								//equation of compass 3
								float x = GameLevel.getInstance().mPlayer.compass3.getX1();
						
								//find the equation of direction line between enemy and player
								float slope = (GameLevel.getInstance().bossPointerLine.getY1() - GameLevel.getInstance().bossPointerLine.getY2())/(GameLevel.getInstance().bossPointerLine.getX1()-GameLevel.getInstance().bossPointerLine.getX2());
								float b = GameLevel.getInstance().bossPointerLine.getY1() - (slope * GameLevel.getInstance().bossPointerLine.getX1());
						
								//substitute x into y and we get our point of intersection
								float y = slope*x + b;
						
								//x and y are where we put our pointer (subtract 5 pixels for viewing purposes)
								pointer.setPosition(x-5, y);
						
							} else if ((GameLevel.getInstance().bossPointerLine != null) && GameLevel.getInstance().bossPointerLine.collidesWith(GameLevel.getInstance().mPlayer.compass4)) {
								//equation of compass 2
								float y = GameLevel.getInstance().mPlayer.compass4.getY1();
						
								//find the equation of direction line between enemy and player
								float slope = (GameLevel.getInstance().bossPointerLine.getY1() - GameLevel.getInstance().bossPointerLine.getY2())/(GameLevel.getInstance().bossPointerLine.getX1()-GameLevel.getInstance().bossPointerLine.getX2());
								float b = GameLevel.getInstance().bossPointerLine.getY1() - (slope * GameLevel.getInstance().bossPointerLine.getX1());
						
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
	
