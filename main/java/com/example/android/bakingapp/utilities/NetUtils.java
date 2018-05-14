package com.example.android.bakingapp.utilities;


import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import timber.log.Timber;

public class NetUtils {

    private static final String J_ID = "id";
    private static final String J_NAME = "name";

    private static final String J_INGREDIENTS = "ingredients";
    private static final String J_QUANTITY = "quantity";
    private static final String J_MEASURE = "measure";
    private static final String J_INGREDIENT = "ingredient";

    private static final String J_STEPS = "steps";
    private static final String J_STEP_ID = "id";
    private static final String J_SHORT_DESCRIPTION = "shortDescription";
    private static final String J_DESCRPTION = "description";
    private static final String J_VIDEO_URL = "videoURL";
    private static final String J_THUMBNAIL_URL = "thumbnailURL";

    private static final String J_SERVINGS = "servings";
    private static final String J_IMAGE = "image";

    public static final String RESULTS_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";


    public static Recipe[] getAllRecipes(String json) {
        try {

            JSONArray results=new JSONArray(json);

            Recipe[] recipes = new Recipe[results.length()];

            for (int i = 0; i < results.length(); i++) {
                recipes[i] = getRecipeFromJSON(results, i);

            }
            return recipes;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    private static Recipe getRecipeFromJSON(JSONArray json, int index) {
        try {

            JSONObject recipeJsonObject = json.getJSONObject(index);

            int id = recipeJsonObject.optInt(J_ID);
            String name = recipeJsonObject.optString(J_NAME);
            int servings = recipeJsonObject.optInt(J_SERVINGS);
            String image = recipeJsonObject.optString(J_IMAGE);

            Recipe m = new Recipe();
            m.setId(id);
            m.setName(name);
            m.setServings(servings);
            m.setImage(image);

            JSONArray jsonIngredients=recipeJsonObject.getJSONArray(J_INGREDIENTS);
            Ingredient[] ingr = getIngredients(jsonIngredients);
            m.setIngredients(ingr);

            JSONArray jsonSteps=recipeJsonObject.getJSONArray(J_STEPS);
            Step[] steps=getSteps(jsonSteps);
            m.setSteps(steps);

            return m;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Ingredient[] getIngredients(JSONArray jsonIngredients) {
        int length = jsonIngredients.length();
        Ingredient[] ingredients = new Ingredient[length];

        double q=0;
        String m="",g="";
        for (int i=0;i<length;i++){
            Ingredient ingr=new Ingredient();
            try {
                JSONObject jsonObject=jsonIngredients.getJSONObject(i);
                q=(jsonObject.optDouble(J_QUANTITY));
                m=(jsonObject.optString(J_MEASURE));
                g=(jsonObject.optString(J_INGREDIENT));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ingr.setQuantity(q);
            ingr.setMeasure(m);
            ingr.setIngredient(g);
            ingredients[i]=ingr;

        }
        return ingredients;

    }


    private static Step[] getSteps(JSONArray jsonSteps) {

        int length = jsonSteps.length();
        Step[] steps=new Step[length];
        for (int i=0;i<length;i++){
            Step s=new Step();
            try {
                JSONObject jsonObject=jsonSteps.getJSONObject(i);
                s.setId(jsonObject.optInt(J_STEP_ID));
                s.setShortDescription(jsonObject.optString(J_SHORT_DESCRIPTION));
                s.setDescription(jsonObject.optString(J_DESCRPTION));
                s.setVideoUrl(jsonObject.optString(J_VIDEO_URL));
                s.setThumpnailUrl(jsonObject.optString(J_THUMBNAIL_URL));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            steps[i]=s;
        }
        return steps;
    }


    public static String getRecipeData(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }catch (Exception e){
            Timber.d("EXCEPTION: %s", e);
        }
        finally {
            urlConnection.disconnect();
        }
        return null;
    }
}


