package mkat.apps.spacewars;

import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.pool.GenericPool;

public class PlusPointerPool extends GenericPool<PlusPointer> {
	/* The pool constructor assigns the necessary objects needed to
	 *  construct sprites */
	
		//public boolean twoEnemiesContact = false;
	//attach enemy bodies, not sprites to the screen.

	public PlusPointerPool(){
		super();
	}
	
	/* onAllocatePoolItem handles the allocation of new sprites in the
	 event that we're attempting to obtain a new item while all pool
	 items are currently in use */
	protected PlusPointer onAllocatePoolItem(Sprite powerup, String type) {
		//Log.v("","runs to allocate a player laser");	
		PlusPointer pl = new PlusPointer(ResourceManager.getInstance().mHealthPointer, powerup, type);
		//GameLevel.getInstance().attachChild(pl);
		return pl;
	}
		//GameLevel.getInstance().attachChild(pl);
		//Log.v("something", "enemy created with modifiers and listeners");
	
	@Override
	protected void onHandleRecycleItem(PlusPointer pItem){
		super.onHandleRecycleItem(pItem);
		pItem.clearEntityModifiers();
		pItem.clearUpdateHandlers();
		pItem.setVisible(false);
		pItem.reset();
		//GameLevel.getInstance().PlayerLaser = pItem;
		
		//GameLevel.getInstance().recycleBullet = true;
		//Log.v("","recycled a laser icon3");
	}

	public synchronized PlusPointer obtainPoolItem(Sprite powerup, String type) {
		// TODO Auto-generated method stub
		
		PlusPointer	pl = new PlusPointer(ResourceManager.getInstance().mHealthPointer, powerup, type);
		
		GameLevel.getInstance().attachChild(pl);
		return pl;
			
	}
	
	@Override
	protected PlusPointer onAllocatePoolItem() {
		// TODO Auto-generated method stub
		return null;
	}


}