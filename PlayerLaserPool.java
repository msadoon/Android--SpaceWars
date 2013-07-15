package mkat.apps.spacewars;


import org.andengine.util.adt.pool.GenericPool;

import mkat.apps.spacewars.GameLevel;


import android.util.Log;

public class PlayerLaserPool extends GenericPool<PlayerLaser> {
	/* The pool constructor assigns the necessary objects needed to
	 *  construct sprites */
	
	public PlayerLaserPool(int i){
		super(i);
	}
	
	/* onAllocatePoolItem handles the allocation of new sprites in the
	 event that we're attempting to obtain a new item while all pool
	 items are currently in use */
	protected PlayerLaser onAllocatePoolItem(Player callingPlayer, float x, float y) {
		//Log.v("","runs to allocate a player laser");	
		PlayerLaser pl = new PlayerLaser(GameLevel.getInstance().pPlayerLaserTexture, callingPlayer, x, y);
		//GameLevel.getInstance().attachChild(pl);
		return pl;
	}
		//GameLevel.getInstance().attachChild(pl);
		//Log.v("something", "enemy created with modifiers and listeners");
	
	@Override
	protected void onHandleRecycleItem(PlayerLaser pItem){
		super.onHandleRecycleItem(pItem);
		pItem.clearEntityModifiers();
		pItem.clearUpdateHandlers();
		pItem.setVisible(false);
		pItem.reset();
		GameLevel.getInstance().PlayerLaser = pItem;
		
		//GameLevel.getInstance().recycleBullet = true;
		//Log.v("","recycled a laser icon3");
	}

	public synchronized PlayerLaser obtainPoolItem(Player callingPlayer, float x, float y) {
		// TODO Auto-generated method stub
		//super.obtainPoolItem();
		//Log.v("","runs to obtain a player laser");
		PlayerLaser pl = null;
		if (GameLevel.getInstance().firePower == false) {
			pl = new PlayerLaser(GameLevel.getInstance().pPlayerLaserTexture, callingPlayer, x, y);
		} else {
			pl = new PlayerLaser(GameLevel.getInstance().pPlayerLaserTexture2, callingPlayer, x, y);
		}
		
		GameLevel.getInstance().attachChild(pl);
		return pl;
			
	}
	
	@Override
	protected PlayerLaser onAllocatePoolItem() {
		// TODO Auto-generated method stub
		return null;
	}

}