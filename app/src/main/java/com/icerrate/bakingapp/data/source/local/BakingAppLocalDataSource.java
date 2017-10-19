package com.icerrate.bakingapp.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.icerrate.bakingapp.data.model.Ingredient;
import com.icerrate.bakingapp.data.model.Recipe;
import com.icerrate.bakingapp.data.model.Step;
import com.icerrate.bakingapp.data.source.BakingAppDataSource;
import com.icerrate.bakingapp.provider.cloud.BaseService;
import com.icerrate.bakingapp.provider.db.BakingAppContract;
import com.icerrate.bakingapp.provider.db.BakingAppDBHelper;
import com.icerrate.bakingapp.view.common.BaseCallback;

import java.util.ArrayList;

import static com.icerrate.bakingapp.provider.db.BakingAppContract.Step.STEP_ID;

/**
 * @author Ivan Cerrate.
 */

public class BakingAppLocalDataSource extends BaseService implements BakingAppDataSource {

    private Context context;

    private SQLiteDatabase database;

    public BakingAppLocalDataSource(Context context) {
        this.context = context;
    }

    private BakingAppLocalDataSource open() throws SQLException {
        BakingAppDBHelper dbHelper = new BakingAppDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    private void close() {
        database.close();
    }

    @Override
    public void getRecipes(BaseCallback<ArrayList<Recipe>> callback) {
        throw new UnsupportedOperationException("Operation is not available!");
    }

    @Override
    public void getRecipeDetail(Integer recipeId, BaseCallback<Recipe> callback) {
        open();
        Recipe recipe = null;
        Cursor c1 = database.query(BakingAppContract.Recipe.TABLE_NAME,
                new String[]{BakingAppContract.Recipe._ID,
                        BakingAppContract.Recipe.NAME,
                        BakingAppContract.Recipe.SERVINGS,
                        BakingAppContract.Recipe.IMAGE},
                BakingAppContract.Recipe._ID + "=?",
                new String[]{String.valueOf(recipeId)}, null, null, null);
        while (c1.moveToNext()) {
            recipe = new Recipe();
            recipe.setId(c1.getInt(0));
            recipe.setName(c1.getString(1));
            recipe.setServings(c1.getInt(2));
            recipe.setImage(c1.getString(3));
        }
        c1.close();
        close();
        callback.onSuccess(recipe);
    }

    @Override
    public void getIngredients(Integer recipeId, BaseCallback<ArrayList<Ingredient>> callback) {
        open();
        ArrayList<Ingredient> ingredientsList = new ArrayList<>();
        Cursor c1 = database.query(BakingAppContract.Ingredient.TABLE_NAME,
                new String[]{BakingAppContract.Ingredient.QUANTITY,
                        BakingAppContract.Ingredient.MEASURE,
                        BakingAppContract.Ingredient.INGREDIENT},
                BakingAppContract.Ingredient.RECIPE_ID +"=?",
                new String[]{String.valueOf(recipeId)}, null, null, null);
        while (c1.moveToNext()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setQuantity(c1.getDouble(0));
            ingredient.setMeasure(c1.getString(1));
            ingredient.setIngredient(c1.getString(2));
            ingredientsList.add(ingredient);
        }
        c1.close();
        close();
        callback.onSuccess(ingredientsList);
    }

    @Override
    public void getSteps(Integer recipeId, BaseCallback<ArrayList<Step>> callback) {
        open();
        ArrayList<Step> stepsList = new ArrayList<>();
        Cursor c1 = database.query(BakingAppContract.Step.TABLE_NAME,
                new String[]{STEP_ID,
                        BakingAppContract.Step.SHORT_DESCRIPTION,
                        BakingAppContract.Step.DESCRIPTION,
                        BakingAppContract.Step.VIDEO_URL,
                        BakingAppContract.Step.THUMBNAIL_URL},
                BakingAppContract.Step.RECIPE_ID +"=?",
                new String[]{String.valueOf(recipeId)}, null, null, STEP_ID + " ASC");
        while (c1.moveToNext()) {
            Step step = new Step();
            step.setId(c1.getInt(0));
            step.setShortDescription(c1.getString(1));
            step.setDescription(c1.getString(2));
            step.setVideoURL(c1.getString(3));
            step.setThumbnailURL(c1.getString(4));
            stepsList.add(step);
        }
        c1.close();
        close();
        callback.onSuccess(stepsList);
    }

    @Override
    public void getStepDetail(Integer recipeId, Integer stepId, BaseCallback<Step> callback) {
        open();
        Step step = null;
        Cursor c1 = database.query(BakingAppContract.Step.TABLE_NAME,
                new String[]{STEP_ID,
                        BakingAppContract.Step.SHORT_DESCRIPTION,
                        BakingAppContract.Step.DESCRIPTION,
                        BakingAppContract.Step.VIDEO_URL,
                        BakingAppContract.Step.THUMBNAIL_URL},
                BakingAppContract.Step.RECIPE_ID + "=? AND " + STEP_ID + "=?",
                new String[]{String.valueOf(recipeId), String.valueOf(stepId)}, null, null, null);
        while (c1.moveToNext()) {
            step = new Step();
            step.setId(c1.getInt(0));
            step.setShortDescription(c1.getString(1));
            step.setDescription(c1.getString(2));
            step.setVideoURL(c1.getString(3));
            step.setThumbnailURL(c1.getString(4));
        }
        c1.close();
        close();
        callback.onSuccess(step);
    }

    @Override
    public void saveRecipes(ArrayList<Recipe> recipesList) {
        open();
        database.beginTransaction();
        try {
            String sql = "INSERT OR REPLACE INTO " + BakingAppContract.Recipe.TABLE_NAME
                    + " (" + BakingAppContract.Recipe._ID
                    + ", " + BakingAppContract.Recipe.NAME
                    + ", " + BakingAppContract.Recipe.SERVINGS
                    + ", " + BakingAppContract.Recipe.IMAGE+ ") "
                    + "VALUES (?,?,?,?)";
            SQLiteStatement st = database.compileStatement(sql);
            for(Recipe recipe : recipesList) {
                st.clearBindings();
                st.bindLong(1, recipe.getId());
                st.bindString(2, recipe.getName());
                st.bindLong(3, recipe.getServings());
                st.bindString(4, recipe.getImage());
                st.execute();
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        close();
    }

    @Override
    public void saveIngredients(Integer recipeId, ArrayList<Ingredient> ingredientsList) {
        open();
        database.beginTransaction();
        try {
            String sql = "INSERT OR REPLACE INTO " + BakingAppContract.Ingredient.TABLE_NAME
                    + " (" + BakingAppContract.Ingredient.RECIPE_ID
                    + ", " + BakingAppContract.Ingredient.QUANTITY
                    + ", " + BakingAppContract.Ingredient.MEASURE
                    + ", " + BakingAppContract.Ingredient.INGREDIENT+ ") "
                    + "VALUES (?,?,?,?)";
            SQLiteStatement st = database.compileStatement(sql);
            for(Ingredient ingredient : ingredientsList) {
                st.clearBindings();
                st.bindLong(1, recipeId);
                st.bindDouble(2, ingredient.getQuantity());
                st.bindString(3, ingredient.getMeasure());
                st.bindString(4, ingredient.getIngredient());
                st.execute();
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        close();
    }

    @Override
    public void saveSteps(Integer recipeId, ArrayList<Step> stepsList) {
        open();
        database.beginTransaction();
        try {
            String sql = "INSERT OR REPLACE INTO " + BakingAppContract.Step.TABLE_NAME
                    + " (" + STEP_ID
                    + ", " + BakingAppContract.Step.RECIPE_ID
                    + ", " + BakingAppContract.Step.SHORT_DESCRIPTION
                    + ", " + BakingAppContract.Step.DESCRIPTION
                    + ", " + BakingAppContract.Step.VIDEO_URL
                    + ", " + BakingAppContract.Step.THUMBNAIL_URL+ ") "
                    + "VALUES (?,?,?,?,?,?)";
            SQLiteStatement st = database.compileStatement(sql);
            for(Step step : stepsList) {
                st.clearBindings();
                st.bindLong(1, step.getId());
                st.bindLong(2, recipeId);
                st.bindString(3, step.getShortDescription());
                st.bindString(4, step.getDescription());
                st.bindString(5, step.getVideoURL());
                st.bindString(6, step.getThumbnailURL());
                st.execute();
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        close();
    }

    @Override
    public void prepareDataSource() {
        cleanRecipes();
        cleanIngredients();
        cleanSteps();
    }

    private void cleanRecipes() {
        open();
        database.execSQL("delete from "+ BakingAppContract.Recipe.TABLE_NAME);
        database.execSQL("vacuum");
        close();
    }

    private void cleanIngredients() {
        open();
        database.execSQL("delete from "+ BakingAppContract.Ingredient.TABLE_NAME);
        database.execSQL("vacuum");
        close();
    }

    private void cleanSteps() {
        open();
        database.execSQL("delete from "+ BakingAppContract.Step.TABLE_NAME);
        database.execSQL("vacuum");
        close();
    }

}
