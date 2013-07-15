package mkat.apps.spacewars;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;
import org.andengine.util.modifier.IModifier;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import mkat.apps.spacewars.GameLevel;


import android.hardware.SensorManager;
import android.util.Log;

public class EnemyPool extends GenericPool<Enemy> {
	/* The pool constructor assigns the necessary objects needed to
	 *  construct sprites */
	
		//public boolean twoEnemiesContact = false;
	//attach enemy bodies, not sprites to the screen.
	
	
	public EnemyPool(){
		super();
		//CREATE THE PHYSICS WORLD
			
	}
	
	@Override
	public synchronized Enemy obtainPoolItem() {
		// TODO Auto-generated method stub
		
		Enemy e1 = new Enemy(GameLevel.getInstance().pCurrentEnemyTexture);
		GameLevel.getInstance().attachChild(e1);
		return e1;
	}

	/* onAllocatePoolItem handles the allocation of new sprites in the
	 event that we're attempting to obtain a new item while all pool
	 items are currently in use */
	@Override
	protected Enemy onAllocatePoolItem() {
		//mScene.attachChild(Box1);
		//mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(Box1, BoxBody));
		//Enemy e1 = new Enemy(GameLevel.getInstance().pCurrentEnemyTexture);
		//GameLevel.getInstance().attachChild(e1);
		
		//Log.v("something", "enemy created with modifiers and listeners");
		return null;
	}

	@Override
	protected void onHandleRecycleItem(Enemy pItem){
		super.onHandleRecycleItem(pItem);
		pItem.setIgnoreUpdate(true);
		pItem.clearEntityModifiers();
		pItem.clearUpdateHandlers();
		pItem.setVisible(false);
		pItem.reset();
		
		
		//GameLevel.getInstance().PlayerLaser = pItem;
		
		//GameLevel.getInstance().recycleBullet = true;
		Log.v("","recycled AN ENEMY!!");
	}
}
