package mkat.apps.spacewars;

import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Boss extends Sprite{

	TimerHandler bossBulletTimerHandler;
	TimerHandler movementTimer;
	public Boss(float a, float b, ITextureRegion pTextureRegion, VertexBufferObjectManager IVertexObjectBuffer) {
		super((float)(0 + (int)(Math.random() * ((ResourceManager.getInstance().cameraWidth - 0) + 1))),(float)(0 + (int)(Math.random() * ((ResourceManager.getInstance().cameraHeight - 0) + 1))),pTextureRegion, ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		//super(200,200, pTextureRegion,ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		
		//sprite = new 
		// TODO Auto-generated constructor stub
	}
	
	public TimerHandler getTimerHandler(){
		return bossBulletTimerHandler;
	}
	public TimerHandler getMoveHandler(){
		return movementTimer;
	}
	
	public void setTimerHandler(TimerHandler bossBulletTimerHandler2){
		bossBulletTimerHandler = bossBulletTimerHandler2;
	}
	
	public void setMoveHandler(TimerHandler MovementTimerHandler2){
		movementTimer = MovementTimerHandler2;
	}
}
