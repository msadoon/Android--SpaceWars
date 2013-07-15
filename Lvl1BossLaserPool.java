package mkat.apps.spacewars;

import org.andengine.util.adt.pool.GenericPool;

public class Lvl1BossLaserPool extends GenericPool<Lvl1BossLaser> {
	/* The pool constructor assigns the necessary objects needed to
	 *  construct sprites */
	
	public Lvl1BossLaserPool(int i){
		super(i);
	}
	
	/* onAllocatePoolItem handles the allocation of new sprites in the
	 event that we're attempting to obtain a new item while all pool
	 items are currently in use */
	
	protected Lvl1BossLaser onAllocatePoolItem(float x, float y, float angle, Lvl1Boss callingBoss) {
		//super.onAllocatePoolItem();
		//Log.v("","runs to allocate a enemy laser");	
		Lvl1BossLaser bl = new Lvl1BossLaser(GameLevel.getInstance().pBossLaserTexture, x, y, angle, callingBoss);
		//GameLevel.getInstance().attachChild(bl);
		return bl;
	}
		//GameLevel.getInstance().attachChild(pl);
		//Log.v("something", "enemy created with modifiers and listeners");
	
	@Override
	protected void onHandleRecycleItem(Lvl1BossLaser pItem){
		super.onHandleRecycleItem(pItem);
		pItem.clearEntityModifiers();
		pItem.clearUpdateHandlers();
		pItem.setVisible(false);
		pItem.reset();
		
		//GameLevel.getInstance().recycleBullet = true;
		//Log.v("","recycled a enemy laser icon3");
	}

	public synchronized Lvl1BossLaser obtainPoolItem(float x, float y, float angle, Lvl1Boss callingBoss) {
		// TODO Auto-generated method stub
		super.obtainPoolItem();
		//Log.v("","runs to obtain a enemy laser");
		Lvl1BossLaser bl = new Lvl1BossLaser(GameLevel.getInstance().pBossLaserTexture, x, y, angle, callingBoss);
		GameLevel.getInstance().attachChild(bl);
		return bl;
			
	}

	@Override
	protected Lvl1BossLaser onAllocatePoolItem() {
		// TODO Auto-generated method stub
		//Log.v("", "Shouldn't run~");
		return null;
	}
}