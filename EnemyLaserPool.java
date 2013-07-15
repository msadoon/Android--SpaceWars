package mkat.apps.spacewars;

import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.pool.GenericPool;

import android.util.Log;

public class EnemyLaserPool extends GenericPool<EnemyLaser> {
	/* The pool constructor assigns the necessary objects needed to
	 *  construct sprites */
	
	public EnemyLaserPool(int i){
		super(i);
	}
	
	/* onAllocatePoolItem handles the allocation of new sprites in the
	 event that we're attempting to obtain a new item while all pool
	 items are currently in use */
	
	protected EnemyLaser onAllocatePoolItem(float x, float y, float angle, Enemy callingEnemy) {
		//super.onAllocatePoolItem();
		//Log.v("","runs to allocate a enemy laser");	
		EnemyLaser el = new EnemyLaser(GameLevel.getInstance().pEnemyLaserTexture, x, y, angle, callingEnemy);
		//GameLevel.getInstance().attachChild(pl);
		return el;
	}
		//GameLevel.getInstance().attachChild(pl);
		//Log.v("something", "enemy created with modifiers and listeners");
	
	@Override
	protected void onHandleRecycleItem(EnemyLaser pItem){
		super.onHandleRecycleItem(pItem);
		pItem.clearEntityModifiers();
		pItem.clearUpdateHandlers();
		pItem.setVisible(false);
		pItem.reset();
		
		//GameLevel.getInstance().recycleBullet = true;
		//Log.v("","recycled a enemy laser icon3");
	}

	public synchronized EnemyLaser obtainPoolItem(float x, float y, float angle, Enemy callingEnemy) {
		// TODO Auto-generated method stub
		super.obtainPoolItem();
		//Log.v("","runs to obtain a enemy laser");
		EnemyLaser el = new EnemyLaser(GameLevel.getInstance().pEnemyLaserTexture, x, y, angle, callingEnemy);
		GameLevel.getInstance().attachChild(el);
		return el;
			
	}

	@Override
	protected EnemyLaser onAllocatePoolItem() {
		// TODO Auto-generated method stub
		//Log.v("", "Shouldn't run~");
		return null;
	}
}
