package com.icerrate.bakingapp.provider.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.icerrate.bakingapp.BuildConfig;

/**
 * @author Ivan Cerrate.
 */

public class BakingAppDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = BuildConfig.APPLICATION_ID +".baking_db";

    private static final int DATABASE_VERSION = 1;

    private static final String SCRIPT_TABLE_RECIPE = "CREATE TABLE IF NOT EXISTS "+BakingAppContract.Recipe.TABLE_NAME +" ("
            + BakingAppContract.Recipe._ID +" INT PRIMARY KEY, "
            + BakingAppContract.Recipe.NAME +" TEXT NULL, "
            + BakingAppContract.Recipe.SERVINGS +" TEXT NULL, "
            + BakingAppContract.Recipe.IMAGE +" TEXT NULL)";

    private static final String SCRIPT_TABLE_INGREDIENT = "CREATE TABLE IF NOT EXISTS "+BakingAppContract.Ingredient.TABLE_NAME +" ("
            + BakingAppContract.Ingredient.RECIPE_ID +" INT NULL, "
            + BakingAppContract.Ingredient.QUANTITY +" REAL NULL, "
            + BakingAppContract.Ingredient.MEASURE +" TEXT NULL, "
            + BakingAppContract.Ingredient.INGREDIENT +" TEXT NULL)";

    private static final String SCRIPT_TABLE_STEP = "CREATE TABLE IF NOT EXISTS "+BakingAppContract.Step.TABLE_NAME +" ("
            + BakingAppContract.Step.STEP_ID +" INT NULL, "
            + BakingAppContract.Step.RECIPE_ID +" INT NULL, "
            + BakingAppContract.Step.SHORT_DESCRIPTION +" TEXT NULL, "
            + BakingAppContract.Step.DESCRIPTION +" TEXT NULL, "
            + BakingAppContract.Step.VIDEO_URL +" TEXT NULL, "
            + BakingAppContract.Step.THUMBNAIL_URL +" TEXT NULL)";

    public BakingAppDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_TABLE_RECIPE);
        db.execSQL(SCRIPT_TABLE_INGREDIENT);
        db.execSQL(SCRIPT_TABLE_STEP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(BakingAppDBHelper.class.getName(),
                "Upgrading database from version "
                        + oldVersion + " to " + newVersion
                        + ", which will destroy all old data");
        db.execSQL("ALTER TABLE IF EXISTS " + BakingAppContract.Recipe.TABLE_NAME);
        db.execSQL("ALTER TABLE IF EXISTS " + BakingAppContract.Ingredient.TABLE_NAME);
        db.execSQL("ALTER TABLE IF EXISTS " + BakingAppContract.Step.TABLE_NAME);
        onCreate(db);
    }
}
