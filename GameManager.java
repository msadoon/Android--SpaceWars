package mkat.apps.spacewars;

import java.io.FileOutputStream;


import android.util.Log;

public class GameManager {
	
	private static GameManager INSTANCE;
	
	private static final int INITIAL_SCORE = 0;
	private static final int INITIAL_ENEMY_COUNT = 10;
	private static final int INITIAL_LIFE = 10;
	private static final int INITIAL_FIREPOWER = 0;
	private static final int INITIAL_FIRERATE = 0;
	
	private int mCurrentScore;
	private int mEnemyCount;
	private int mLife;
	private int mFirepower;
	private int mFirerate;
	private boolean mEnemyDown, mGameManager = false;
	
	
	GameManager(){
		
	}
	
	public static GameManager getInstance(){
		if (INSTANCE == null){
			INSTANCE = new GameManager();
			
		}
		
		return INSTANCE;
	}
	
	//get current Score
	public int getCurrentScore(){
		return this.mCurrentScore;
	}
	
	//get EnemyCount;
	public int getCurrentEnemyCount(){
		return this.mEnemyCount;
	}
	
	//get Life;
	public int getLife(){
		return this.mLife;
	}
	
	//get firepower
	public int getFirepower(){
		return this.mFirepower;
	}
	
	//get firerate
	public int getFirerate(){
		return this.mFirerate;
	}
	
	public void decrementLife(int lifeLoss){
		this.mLife -= lifeLoss;
	}
	
	public void incrementLife(int lifeGain){
		this.mLife += lifeGain;
	}
	
	public void incrementScore(int scoreInc){
		this.mCurrentScore += scoreInc;
		if (mCurrentScore > UserData.getInstance(ResourceManager.getInstance().context).getHighScore(1)){
			UserData.getInstance(ResourceManager.getInstance().context).setHighScore(1, mCurrentScore);
		}
		
		//int highScore = UserData.getInstance().getHighScore();
		//if (mCurrentScore >= highScore){
			//UserData.getInstance().setSoundMuted(true);
		//	UserData.getInstance().setHighScore(highScore);
		//}
	}
	
	public void decrementEnemyCount(){
		this.mEnemyCount -= 1;
		Log.i("MODIFIER", "Enemy Count Down 1 " + mEnemyCount);
		this.mEnemyDown = true;
	}
	
	public boolean isEnemyDown() {
		boolean temp = this.mEnemyDown;
		this.mEnemyDown = false;
		Log.v("", "is enemy down" + temp);
		return temp;
	}
	
	public void resetEnemyCount(int i){
		this.mEnemyCount = i;
	}
	
	public void zeroLife(){
		this.mLife = 0;
	}
	
	public void GMDone(boolean value){
		this.mGameManager = value;
	}
	
	public boolean GetGMDone(){
		return this.mGameManager;
	}
	
	public void resetGame(){
		this.mLife = GameManager.INITIAL_LIFE;
		this.mCurrentScore = GameManager.INITIAL_SCORE;
		this.mEnemyCount = GameManager.INITIAL_ENEMY_COUNT;
		this.mFirepower = GameManager.INITIAL_FIREPOWER;
		this.mFirerate = GameManager.INITIAL_FIRERATE;

	}
	
}
