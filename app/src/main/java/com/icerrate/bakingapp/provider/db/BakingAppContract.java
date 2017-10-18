package com.icerrate.bakingapp.provider.db;

/**
 * @author Ivan Cerrate.
 */

public class BakingAppContract {


    public static class Recipe {

        public static final String TABLE_NAME = "recipe";

        public static final String _ID = "id";

        public static final String NAME = "name";

        public static final String SERVINGS = "servings";

        public static final String IMAGE = "image";
    }

    public static class Ingredient {

        public static final String TABLE_NAME = "ingredient";

        public static final String RECIPE_ID = "recipe_id";

        public static final String QUANTITY = "quantity";

        public static final String MEASURE = "measure";

        public static final String INGREDIENT = "ingredient";
    }

    public static class Step {

        public static final String TABLE_NAME = "step";

        public static final String STEP_ID = "step_id";

        public static final String RECIPE_ID = "recipe_id";

        public static final String SHORT_DESCRIPTION = "short_description";

        public static final String DESCRIPTION = "description";

        public static final String VIDEO_URL = "video_url";

        public static final String THUMBNAIL_URL = "thumbnail_url";
    }
}
