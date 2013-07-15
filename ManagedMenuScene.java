package mkat.apps.spacewars;

import android.util.Log;

public abstract class ManagedMenuScene extends ManagedScene{
	
	public ManagedMenuScene thisManagedMenuScene = this;
	
	public ManagedMenuScene(float pLoadingScreenMinimumSecondsShown) {
		super(pLoadingScreenMinimumSecondsShown);

	}
	
	public ManagedMenuScene() {
	};
	

	@Override
	public void onLoadScene() {
		
		
			if (ResourceManager.getInstance().counter == 0){
				Log.v("MODIFIER", "LOAD MENU RESOURCES " + ResourceManager.getInstance().counter);
				ResourceManager.getInstance().loadMenuTextures(ResourceManager.getInstance().engine, ResourceManager.getInstance().context); //load menu textures
				ResourceManager.getInstance().loadMenuMusic(ResourceManager.getInstance().engine, ResourceManager.getInstance().context); //load menu music
				ResourceManager.getInstance().loadMenuSounds(ResourceManager.getInstance().engine,ResourceManager.getInstance().context);
				ResourceManager.getInstance().loadMenuFont(ResourceManager.getInstance().engine,ResourceManager.getInstance().context);
				ResourceManager.getInstance().loadMuteButton(ResourceManager.getInstance().engine,ResourceManager.getInstance().context);
			}
				
			//Log.i("MODIFIER", "Load Menu Resources End " + ResourceManager.getInstance().counter);
		
	}
	
	@Override
	public void onUnloadScene() {
		ResourceManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
		// TODO Auto-generated method stub
		Log.v("MODIFIER", "UNLOAD MENU CHILDREN, ENTITYMODIFIERS,TOUCHAREAS,UPDATEHANDLERS, AND SELF AND MUTEBUTTON ");
		MainMenu.getInstance().newGameSprite.detachSelf();
		MainMenu.getInstance().creditsSprite.detachSelf();
		MainMenu.getInstance().resumeSprite.detachSelf();
		MainMenu.getInstance().menuBGD.detachSelf();
		MainMenu.getInstance().menuMute.detachSelf();
		MainMenu.getInstance().highScore.detachSelf();
		
		thisManagedMenuScene.detachChildren();
		//Log.v("","this is how many children GameLevel has after detach children is run: " + thisManagedGameScene.getChildCount());
		thisManagedMenuScene.clearEntityModifiers();
		thisManagedMenuScene.clearTouchAreas();
		thisManagedMenuScene.clearUpdateHandlers();
		thisManagedMenuScene.detachSelf();
		
		//ResourceManager.getInstance().unloadMenuTextures(); //load menu textures
		//ResourceManager.getInstance().unloadMenuMusic(); //load menu music
		//ResourceManager.getInstance().unloadMenuSounds();
		//ResourceManager.getInstance().unloadMenuFont();
		//ResourceManager.getInstance().unloadMuteButton();
			}
	});
	}

	@Override
	public void onShowScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHideScene() {
		// TODO Auto-generated method stub
		
	}

}
