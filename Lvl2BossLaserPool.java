package mkat.apps.spacewars;

import org.andengine.util.adt.pool.GenericPool;

public class Lvl2BossLaserPool extends GenericPool<Lvl2BossLaser> {
	/* The pool constructor assigns the necessary objects needed to
	 *  construct sprites */
	
	public Lvl2BossLaserPool(int i){
		super(i);
	}
	
	/* onAllocatePoolItem handles the allocation of new sprites in the
	 event that we're attempting to obtain a new item while all pool
	 items are currently in use */
	
	protected Lvl2BossLaser onAllocatePoolItem(float x, float y, float angle, Lvl2Boss callingBoss) {
		//super.onAllocatePoolItem();
		//Log.v("","runs to allocate a enemy laser");	
		Lvl2BossLaser bl = new Lvl2BossLaser(GameLevel.getInstance().pBossLaserTexture, x, y, angle, callingBoss);
		//GameLevel.getInstance().attachChild(bl);
		return bl;
	}
		//GameLevel.getInstance().attachChild(pl);
		//Log.v("something", "enemy created with modifiers and listeners");
	
	@Override
	protected void onHandleRecycleItem(Lvl2BossLaser pItem){
		super.onHandleRecycleItem(pItem);
		pItem.clearEntityModifiers();
		pItem.clearUpdateHandlers();
		pItem.setVisible(false);
		pItem.reset();
		
		//GameLevel.getInstance().recycleBullet = true;
		//Log.v("","recycled a enemy laser icon3");
	}

	public synchronized Lvl2BossLaser obtainPoolItem(float x, float y, float angle, Lvl2Boss callingBoss) {
		// TODO Auto-generated method stub
		super.obtainPoolItem();
		//Log.v("","runs to obtain a enemy laser");
		Lvl2BossLaser bl = new Lvl2BossLaser(GameLevel.getInstance().pBossLaserTexture, x, y, angle, callingBoss);
		GameLevel.getInstance().attachChild(bl);
		return bl;
			
	}

	@Override
	protected Lvl2BossLaser onAllocatePoolItem() {
		// TODO Auto-generated method stub
		//Log.v("", "Shouldn't run~");
		return null;
	}
}