package mkat.apps.spacewars;

import org.andengine.util.adt.pool.GenericPool;

public class BossPointerPool extends GenericPool<BossPointer> {
	/* The pool constructor assigns the necessary objects needed to
	 *  construct sprites */
	
		//public boolean twoEnemiesContact = false;
	//attach enemy bodies, not sprites to the screen.

	public BossPointerPool(){
		super();
	}
	
	/* onAllocatePoolItem handles the allocation of new sprites in the
	 event that we're attempting to obtain a new item while all pool
	 items are currently in use */
	protected BossPointer onAllocatePoolItem(Boss refBoss) {
		//Log.v("","runs to allocate a player laser");	
		BossPointer pl = new BossPointer(ResourceManager.getInstance().mBossPointer, refBoss);
		//GameLevel.getInstance().attachChild(pl);
		return pl;
	}
		//GameLevel.getInstance().attachChild(pl);
		//Log.v("something", "enemy created with modifiers and listeners");
	
	@Override
	protected void onHandleRecycleItem(BossPointer pItem){
		super.onHandleRecycleItem(pItem);
		pItem.clearEntityModifiers();
		pItem.clearUpdateHandlers();
		pItem.setVisible(false);
		pItem.reset();
		//GameLevel.getInstance().PlayerLaser = pItem;
		
		//GameLevel.getInstance().recycleBullet = true;
		//Log.v("","recycled a laser icon3");
	}

	public synchronized BossPointer obtainPoolItem(Boss refBoss) {
		// TODO Auto-generated method stub
		
		BossPointer	pl = new BossPointer(ResourceManager.getInstance().mBossPointer, refBoss);
		
		GameLevel.getInstance().attachChild(pl);
		return pl;
			
	}
	
	@Override
	protected BossPointer onAllocatePoolItem() {
		// TODO Auto-generated method stub
		return null;
	}


}