package mkat.apps.spacewars;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.util.adt.pool.GenericPool;

public class Lvl3BossLaserPool extends GenericPool<Lvl3BossLaser> {
	/* The pool constructor assigns the necessary objects needed to
	 *  construct sprites */
	//private static final float DEMO_VELOCITY = 200.0f;
	public Lvl3BossLaserPool(int i){
		super(i);
	}
	
	/* onAllocatePoolItem handles the allocation of new sprites in the
	 event that we're attempting to obtain a new item while all pool
	 items are currently in use */
	
	protected Lvl3BossLaser onAllocatePoolItem(float x, float y, float angle, Lvl3Boss callingBoss) {
		//super.onAllocatePoolItem();
		//Log.v("","runs to allocate a enemy laser");	
		Lvl3BossLaser bl = new Lvl3BossLaser(GameLevel.getInstance().pBossLaserTexture, x, y, angle, callingBoss);
		//final PhysicsHandler physicsHandler = new PhysicsHandler(bl);
        //bl.registerUpdateHandler(physicsHandler);
        //physicsHandler.setVelocity(DEMO_VELOCITY, DEMO_VELOCITY);
		//GameLevel.getInstance().attachChild(bl);
		return bl;
	}
		//GameLevel.getInstance().attachChild(pl);
		//Log.v("something", "enemy created with modifiers and listeners");
	
	@Override
	protected void onHandleRecycleItem(Lvl3BossLaser pItem){
		super.onHandleRecycleItem(pItem);
		pItem.clearEntityModifiers();
		pItem.clearUpdateHandlers();
		pItem.setVisible(false);
		pItem.reset();
		
		//GameLevel.getInstance().recycleBullet = true;
		//Log.v("","recycled a enemy laser icon3");
	}

	public synchronized Lvl3BossLaser obtainPoolItem(float x, float y, float angle, Lvl3Boss callingBoss) {
		// TODO Auto-generated method stub
		super.obtainPoolItem();
		//Log.v("","runs to obtain a enemy laser");
		Lvl3BossLaser bl = new Lvl3BossLaser(GameLevel.getInstance().pBossLaserTexture, x, y, angle, callingBoss);
		
		//final Ball ball = new Ball(centerX, centerY, this.mFaceTextureRegion);
        //final PhysicsHandler physicsHandler = new PhysicsHandler(bl);
        //bl.registerUpdateHandler(physicsHandler);
        //physicsHandler.setVelocity(DEMO_VELOCITY, DEMO_VELOCITY);
		
		GameLevel.getInstance().attachChild(bl);
		return bl;
			
	}

	@Override
	protected Lvl3BossLaser onAllocatePoolItem() {
		// TODO Auto-generated method stub
		//Log.v("", "Shouldn't run~");
		return null;
	}
}