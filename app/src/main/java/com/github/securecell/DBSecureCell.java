package com.github.securecell;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBSecureCell extends SQLiteOpenHelper
{
    private Context mContext;
    
    public DBSecureCell(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler)
    {
        super(context, name, factory, version, errorHandler);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS \"Packages\" (\"ID\" INTEGER PRIMARY KEY  NOT NULL , \"Name\" VARCHAR UNIQUE , \"Version\" VARCHAR, \"Permissions\" BLOB)");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS \"Packages\";");
        onCreate(db);
    }
    
    public void AddPackage(String packageName, String packageVersion, boolean packageCritical, String[] packagePermissions)
    {
        SQLiteDatabase mDatabase = getWritableDatabase();
        mDatabase.execSQL("INSERT INTO `Packages` VALUES(NULL,"+packageName+","+packageVersion+","+packagePermissions.toString()+");");
    }
}
