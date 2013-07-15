package mkat.apps.spacewars;

import org.andengine.util.adt.pool.GenericPool;

public class PointerPool extends GenericPool<Pointer> {
	/* The pool constructor assigns the necessary objects needed to
	 *  construct sprites */
	
		//public boolean twoEnemiesContact = false;
	//attach enemy bodies, not sprites to the screen.

	public PointerPool(){
		super();
	}
	
	/* onAllocatePoolItem handles the allocation of new sprites in the
	 event that we're attempting to obtain a new item while all pool
	 items are currently in use */
	protected Pointer onAllocatePoolItem(Enemy associateEnemy) {
		//Log.v("","runs to allocate a player laser");	
		Pointer pl = new Pointer(ResourceManager.getInstance().mEnemyPointer, associateEnemy);
		//GameLevel.getInstance().attachChild(pl);
		return pl;
	}
		//GameLevel.getInstance().attachChild(pl);
		//Log.v("something", "enemy created with modifiers and listeners");
	
	@Override
	protected void onHandleRecycleItem(Pointer pItem){
		super.onHandleRecycleItem(pItem);
		pItem.clearEntityModifiers();
		pItem.clearUpdateHandlers();
		pItem.setVisible(false);
		pItem.reset();
		//GameLevel.getInstance().PlayerLaser = pItem;
		
		//GameLevel.getInstance().recycleBullet = true;
		//Log.v("","recycled a laser icon3");
	}

	public synchronized Pointer obtainPoolItem(Enemy associateEnemy) {
		// TODO Auto-generated method stub
		
		Pointer	pl = new Pointer(ResourceManager.getInstance().mEnemyPointer, associateEnemy);
		
		GameLevel.getInstance().attachChild(pl);
		return pl;
			
	}
	
	@Override
	protected Pointer onAllocatePoolItem() {
		// TODO Auto-generated method stub
		return null;
	}


}
