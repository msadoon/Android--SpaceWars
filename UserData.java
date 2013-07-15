package mkat.apps.spacewars;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserData extends SQLiteOpenHelper {
	 
    static final String dbName = "myDB";
    static final String tPrefs = "MyPrefs";
    static final String fmyID = "myNum";
    static final String fLastLevelUnLocked = "lastLevelUnLocked";
    static final String fTopScore = "topScore";
    static final String fMusic = "MusicOnOff";
    
    private static UserData INSTANCE = null;

    
    public UserData(Context context) {
//THE VALUE OF 1 ON THE NEXT LINE REPRESENTS THE VERSION NUMBER OF THE DATABASE
//IN THE FUTURE IF YOU MAKE CHANGES TO THE DATABASE, YOU NEED TO INCREMENT THIS NUMBER
//DOING SO WILL CAUSE THE METHOD onUpgrade() TO AUTOMATICALLY GET TRIGGERED
            super(context, dbName, null, 2);
    }

    public static UserData getInstance(final Context pContext){
        if(INSTANCE == null)
                INSTANCE = new UserData(pContext);
        return INSTANCE;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
//ESTABLISH NEW DATABASE TABLES IF THEY DON'T ALREADY EXIST IN THE DATABASE
            db.execSQL("CREATE TABLE IF NOT EXISTS "+tPrefs+" (" +
                                    fmyID + " INTEGER PRIMARY KEY , " +
                                    fLastLevelUnLocked + " INTEGER, " +
                                    fMusic + " INTEGER, " +
                                    fTopScore + " INTEGER" +
                                    ")");
   
//OPTIONALLY PREPOPULATE THE TABLE WITH SOME VALUES   
             ContentValues cv = new ContentValues();
                    cv.put(fmyID, 1);
                    cv.put(fLastLevelUnLocked, 1);
                    cv.put(fMusic, 1);
                    cv.put(fTopScore, 0);
                            db.insert(tPrefs, null, cv);
                                   
/*             
* MORE ADVANCED EXAMPLES OF USAGE
*
            db.execSQL("CREATE TRIGGER fk_empdept_deptid " +
                            " BEFORE INSERT "+
                            " ON "+employeeTable+                          
                            " FOR EACH ROW BEGIN"+
                            " SELECT CASE WHEN ((SELECT "+colDeptID+" FROM "+deptTable+" WHERE "+colDeptID+"=new."+colDept+" ) IS NULL)"+
                            " THEN RAISE (ABORT,'Foreign Key Violation') END;"+
                            "  END;");
           
            db.execSQL("CREATE VIEW "+viewEmps+
                            " AS SELECT "+employeeTable+"."+colID+" AS _id,"+
                            " "+employeeTable+"."+colName+","+
                            " "+employeeTable+"."+colAge+","+
                            " "+deptTable+"."+colDeptName+""+
                            " FROM "+employeeTable+" JOIN "+deptTable+
                            " ON "+employeeTable+"."+colDept+" ="+deptTable+"."+colDeptID
                            );
*/                             
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//THIS METHOD DELETES THE EXISTING TABLE AND THEN CALLS THE METHOD onCreate() AGAIN TO RECREATE A NEW TABLE
//THIS SERVES TO ESSENTIALLY RESET THE DATABASE
//INSTEAD YOU COULD MODIFY THE EXISTING TABLES BY ADDING/REMOVING COLUMNS/ROWS/VALUES THEN NO EXISTING DATA WOULD BE LOST
            db.execSQL("DROP TABLE IF EXISTS "+tPrefs);
            onCreate(db);
    }
   
     public int lastLevelUnLocked(int ID) {
//THIS METHOD IS CALLED BY YOUR MAIN ACTIVITY TO READ A VALUE FROM THE DATABASE                 
             SQLiteDatabase myDB = this.getReadableDatabase();
             String[] mySearch = new String[]{String.valueOf(ID)};
             Cursor myCursor = myDB.rawQuery("SELECT "+ fLastLevelUnLocked +" FROM "+ tPrefs +" WHERE "+ fmyID +"=?",mySearch);
             myCursor.moveToFirst();
             int index = myCursor.getColumnIndex(fLastLevelUnLocked);
             int myAnswer = myCursor.getInt(index);
             myCursor.close();
             return myAnswer;
     }
     
     public int setLastLevelUnLocked(int ID, int newLevel)
     {
//THIS METHOD IS CALLED BY YOUR MAIN ACTIVITY TO WRITE A VALUE TO THE DATABASE          
             SQLiteDatabase myDB = this.getWritableDatabase();
             ContentValues cv = new ContentValues();
             cv.put(fLastLevelUnLocked, newLevel);
             int numRowsAffected = myDB.update(tPrefs, cv, fmyID+"=?", new String []{String.valueOf(ID)});
             return numRowsAffected;
     }
     
     public int getHighScore(int ID) {
//THIS METHOD IS CALLED BY YOUR MAIN ACTIVITY TO READ A VALUE FROM THE DATABASE                 
             SQLiteDatabase myDB = this.getReadableDatabase();
             String[] mySearch = new String[]{String.valueOf(ID)};
             Cursor myCursor = myDB.rawQuery("SELECT "+ fTopScore +" FROM "+ tPrefs +" WHERE "+ fmyID +"=?",mySearch);
             myCursor.moveToFirst();
             int index = myCursor.getColumnIndex(fTopScore);
             int myAnswer = myCursor.getInt(index);
             myCursor.close();
             return myAnswer;
     }
     public int getMusic(int ID) {
//THIS METHOD IS CALLED BY YOUR MAIN ACTIVITY TO READ A VALUE FROM THE DATABASE                 
             SQLiteDatabase myDB = this.getReadableDatabase();
             String[] mySearch = new String[]{String.valueOf(ID)};
             Cursor myCursor = myDB.rawQuery("SELECT "+ fMusic +" FROM "+ tPrefs +" WHERE "+ fmyID +"=?",mySearch);
             myCursor.moveToFirst();
             int index = myCursor.getColumnIndex(fMusic);
             int myAnswer = myCursor.getInt(index);
             myCursor.close();
             return myAnswer;
     }
          
     public int setHighScore(int ID, int newTopScore)
     {
//THIS METHOD IS CALLED BY YOUR MAIN ACTIVITY TO WRITE A VALUE TO THE DATABASE          
             SQLiteDatabase myDB = this.getWritableDatabase();
             ContentValues cv = new ContentValues();
             cv.put(fTopScore, newTopScore);
             int numRowsAffected = myDB.update(tPrefs, cv, fmyID+"=?", new String []{String.valueOf(ID)});
             return numRowsAffected;
     }
           
     public int setMusic(int ID, int newSetting)
     {
//THIS METHOD IS CALLED BY YOUR MAIN ACTIVITY TO WRITE A VALUE TO THE DATABASE          
             SQLiteDatabase myDB = this.getWritableDatabase();
             ContentValues cv = new ContentValues();
             cv.put(fMusic, newSetting);
             int numRowsAffected = myDB.update(tPrefs, cv, fmyID+"=?", new String []{String.valueOf(ID)});
             return numRowsAffected;
     }
 	public void pauseGame(boolean who) {
		if (SceneManager.getInstance().mCurrentScene.getClass().getGenericSuperclass().equals(ManagedGameScene.class)){
			if (who){//who == true means the ingame pause is called
				GameLevel.getInstance().setChildScene(GameLevel.getInstance().mPauseScene, true, true, true);
			} else//who == 0 means the HOME/SWITCH_APPS pause is called
				GameLevel.getInstance().setChildScene(GameLevel.getInstance().mPauseScene, false, true, true);
			
		}
		//ResourceManager.getInstance().engine.stop();
		
	}
	
	public void unPauseGame() {
		Log.v("","Unpause Game");
		if (SceneManager.getInstance().mCurrentScene.getClass().getGenericSuperclass().equals(ManagedGameScene.class)){
			GameLevel.getInstance().clearChildScene();
			Log.v("","Clear Child Scene");
		}
		//ResourceManager.getInstance().engine.start();
		Log.v("","Engine Start");
	}
/*       
* MORE ADVANCED EXAMPLES OF USAGE

     void AddEmployee(String _name, int _age, int _dept) {
            SQLiteDatabase db= this.getWritableDatabase();
            ContentValues cv=new ContentValues();
                    cv.put(colName, _name);
                    cv.put(colAge, _age);
                    cv.put(colDept, _dept);
                    //cv.put(colDept,2);
            db.insert(employeeTable, colName, cv);
            db.close();
    }
   
     int getEmployeeCount()
     {
            SQLiteDatabase db=this.getWritableDatabase();
            Cursor cur= db.rawQuery("Select * from "+employeeTable, null);
            int x= cur.getCount();
            cur.close();
            return x;
     }
     
     Cursor getAllEmployees()
     {
             SQLiteDatabase db=this.getWritableDatabase();
             //Cursor cur= db.rawQuery("Select "+colID+" as _id , "+colName+", "+colAge+" from "+employeeTable, new String [] {});
             Cursor cur= db.rawQuery("SELECT * FROM "+viewEmps,null);
             return cur;
     }
     
     public int GetDeptID(String Dept)
     {
             SQLiteDatabase db=this.getReadableDatabase();
             Cursor c=db.query(deptTable, new String[]{colDeptID+" as _id",colDeptName},colDeptName+"=?", new String[]{Dept}, null, null, null);
             //Cursor c=db.rawQuery("SELECT "+colDeptID+" as _id FROM "+deptTable+" WHERE "+colDeptName+"=?", new String []{Dept});
             c.moveToFirst();
             return c.getInt(c.getColumnIndex("_id"));
     }
     
     public String GetDept(int ID)
     {
             SQLiteDatabase db=this.getReadableDatabase();
             String[] params=new String[]{String.valueOf(ID)};
             Cursor c=db.rawQuery("SELECT "+colDeptName+" FROM"+ deptTable+" WHERE "+colDeptID+"=?",params);
             c.moveToFirst();
             int index= c.getColumnIndex(colDeptName);
             return c.getString(index);
     }
     
     public Cursor getEmpByDept(String Dept)
     {
             SQLiteDatabase db=this.getReadableDatabase();
             String [] columns=new String[]{"_id",colName,colAge,colDeptName};
             Cursor c=db.query(viewEmps, columns, colDeptName+"=?", new String[]{Dept}, null, null, null);
             return c;
     }
     
     public int UpdateEmp(String _name, int _age, int _dept, int _eid)
     {
             SQLiteDatabase db=this.getWritableDatabase();
             ContentValues cv=new ContentValues();
             cv.put(colName, _name);
             cv.put(colAge, _age);
             cv.put(colDept, _dept);
             return db.update(employeeTable, cv, colID+"=?", new String []{String.valueOf(_eid)});
     }
     
     public void DeleteEmp(String _name, int _age, int _dept, int _eid)
     {
             SQLiteDatabase db=this.getWritableDatabase();
             db.delete(employeeTable,colID+"=?", new String [] {String.valueOf(_eid)});
             db.close();           
     }
       */

}
