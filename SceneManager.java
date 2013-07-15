package mkat.apps.spacewars;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;

import android.util.Log;

public class SceneManager {

	
	//Holds the global SceneManager Instance
	private static final SceneManager INSTANCE = new SceneManager();
	//=====================================================
	// CONSTRUCTOR AND INSTANCE GETTOR
	//=====================================================
	private SceneManager() {
		
	}
	public static SceneManager getInstance() {
		return INSTANCE;
	}
	
	//=====================================================
	// VARIABLES
	//=====================================================
	//These variables reference the current scene and next scene when switching scenes.
	public ManagedScene mCurrentScene;
	private ManagedScene mNextScene;
	//Keep a reference to the engine
	private Engine mEngine = ResourceManager.getInstance().engine;
	//Used by the mLoadingScreenHandler, this variable ensures that the loading screen is shown for one frame prior to loading resources.
	private int mNumFramesPassed = - 1;
	// Keeps the mLoadingScreenHandler from being registered with the engine if it has already been registered.
	private boolean mLoadingScreenHandlerRegistered = false;
	// An update handler that shows the loading screen of mNextScene before calling it to load.
	private IUpdateHandler mLoadingScreenHandler = new IUpdateHandler() {
		@Override
		public void onUpdate(float pSecondsElapsed) {
			//Log.v("something", "UpdateHandler runs");
			// Increment the mNumFramesPassed
			mNumFramesPassed++;
			//And increment the amount of time that the loading screen has been visible
			mNextScene.elapsedLoadingScreenTime += pSecondsElapsed;
			// On the first frame AFTER the loading screen has been shown.
			if(mNumFramesPassed==1){
				//Log.v("something", "NumFramesPassed = 1");
				//Hide and unload the previous scene if it exists
				if(mCurrentScene!=null){
					mCurrentScene.onHideManagedScene();
					//Log.v("something", "hide's current scene");
					mCurrentScene.onUnloadManagedScene();
					//Log.v("something", "unload's current scene");
				}
				//Load the new scene.
				//Log.v("something", "This is the player's x position from within the sprite pool:" + Float.toString(GameLevel.getInstance().mPlayer.getX()));
				mNextScene.onLoadManagedScene();
				Log.v("something", "loads Managed Scene");
				
			}
			//Log.v("", "mNumFramesPassed " + mNumFramesPassed);
			//Log.v("", "mNextScene.elapsedLoadingScreenTime " + mNextScene.elapsedLoadingScreenTime);
			//Log.v("", "mNExtScene.minLoadingScreenTime " + mNextScene.minLoadingScreenTime);
			//On the first frame AFTER the scene has been completely loaded and loading screen has been shown for its minimum limit.
			if(mNumFramesPassed>1 && mNextScene.elapsedLoadingScreenTime>=mNextScene.minLoadingScreenTime){
				Log.v("something", "NUM FRAMES PASSED > 1");
				//Remove the loading screen that was set as a child in the schowScene() method.
				mNextScene.clearChildScene();
				Log.v("something", "clear child scene from managed scene!");
				//Tell the new scene to unload its loading screen.
				mNextScene.onLoadingScreenUnloadAndHidden();
				Log.v("something", "unload loading screen!");
				//Tell the new scene that it is shown
				mNextScene.onShowManagedScene();
				Log.v("something", "show managed scene!");
				//Set the new scene to the current scene
				mCurrentScene = mNextScene;
				
				//Reset the handler & loading screen variables to be ready for another use.
				mNextScene.elapsedLoadingScreenTime = 0f;
				mNumFramesPassed = -1;
				mEngine.unregisterUpdateHandler(this);
				mLoadingScreenHandlerRegistered = false;
			}
		}
		@Override public void reset() {}
	};
	
	//Set to TRUE in the showLayer() method if the camera had a HUD before the layer was shown
	private boolean mCameraHadHud = false;
	// Boolean to reflect whether there is a layer currently shown on the screen.
	public boolean isLayerShown = false;
	//An empty place-holder scene that we use to apply the modal properties of the layer to the currently shown scene.
	private Scene mPlaceholderModalScene;
	//Hold a reference to the current managed layer (if one exists)
	public ManagedLayer currentLayer;
	
	//=====================================================
	// PUBLIC METHODS
	//=====================================================
	//Initiates the process of switching the current managed scene for new managed scene.
	public void showScene(final ManagedScene pManagedScene){
		Log.v("something", "showScene runs");
		//Reset the camera. This is automatically overridden by any calls to alter the camera from within a managed scene's on onShowScene() method.
		mEngine.getCamera().set(0f, 0f, ResourceManager.getInstance().cameraWidth, ResourceManager.getInstance().cameraHeight);
		//If the new managed scene has a loading screen.
		if (pManagedScene.hasLoadingScreen){
			//Log.v("", "game levl has a loading screen");
			//Set the loading screen as a modal child to the new managed scene.
			pManagedScene.setChildScene(pManagedScene.onLoadingScreenLoadandShown(), true, true, true);
			// This if/else block assures that the LoadingScreen Update Handler is only registered if necessary.
			if(mLoadingScreenHandlerRegistered){
				Log.v("", "loadingscreenhandlerregistered");
				mNumFramesPassed = -1;
				mNextScene.clearChildScene();
				mNextScene.onLoadingScreenUnloadAndHidden();
			} else {
				Log.v("", "no loading screen handler registered");
				mEngine.registerUpdateHandler(mLoadingScreenHandler);
				mLoadingScreenHandlerRegistered = true;
			}
			//Set pManagedScene to mNextScene which is used by the loading screen update handler.
			mNextScene = pManagedScene;
			Log.v("","sets the pManagedScene to the NextScene");
			//Set the new scene as the engine's scene
			mEngine.setScene(pManagedScene);
			//Exit the method and let the LoadingScreen Update Handler finish the switching.
			return;
		}
		Log.v("", "scene has no loading screen");
		// If the new managed scene does not have a loading screen.
		// Set the new scene as the engine's scene.
		mNextScene = pManagedScene;
		mEngine.setScene(mNextScene);
		//If a previous managed scene exists, hide and unload it.
		if (mCurrentScene!=null)
		{
			Log.v("", "hides and 'unloads' the current scene");
			mCurrentScene.onHideManagedScene();
			mCurrentScene.onUnloadManagedScene();
		}
		//Load and show the new managed scene, and set it as the current scene.
		Log.v("", "load and show new scene");
		mNextScene.onLoadManagedScene();
		mNextScene.onShowManagedScene();
		mCurrentScene = mNextScene;
	}
	
	//Convenience method to quickly show the Main Menu
	public void showMainMenu() {
		try {
			//Log.v("","Trying to get to the main men now...");
			//new MainMenu();
			showScene(MainMenu.getInstance());
			//GameActivity g = new GameActivity();
			//g.restart(0);
			Log.v("","Got to the main menu!");
		} catch(ArrayIndexOutOfBoundsException e){
			Log.v("", "Exception caught here");
			e.printStackTrace();
		}
		
		
	}
}
