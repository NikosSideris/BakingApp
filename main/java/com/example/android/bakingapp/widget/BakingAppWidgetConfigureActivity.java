package com.example.android.bakingapp.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utilities.NetUtils;
import com.example.android.bakingapp.utilities.NormalizeRecipes;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import timber.log.Timber;


/**
 * The configuration screen for the {@link BakingAppWidget BakingAppWidget} AppWidget.
 */
public class BakingAppWidgetConfigureActivity extends Activity {

    public static final String KEY_TITLE = "title";
    public static final String KEY_ID = "id";
    public static final String KEY_RECIPE_SELECTED_IMAGE = "image";
    public static final String KEY_INGREDIENTS = "ingredientsArray";
    private static final String PREFS_NAME = "com.example.android.bakingapp.widget.BakingAppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    public TextView mWidgetText;
    public ListView mWidgetList;
    public Button mAddButton;
    public Recipe[] mRecipes;
    public ArrayList<String> recipesList;
    public Recipe mRecipeSelected;
    public int mRecipeSelectedIndex;
    public int mRecipeId;
    public String mImageUrl;
    public String mTitle;
    public Context mContext;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Timber.d("onClick");
            //final
            Context context = BakingAppWidgetConfigureActivity.this;

            // When the button is clicked, store the preferences;
            savePreferences(context, mAppWidgetId);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            BakingAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            Timber.d("RESULT_OK%s", mAppWidgetId);
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public BakingAppWidgetConfigureActivity() {
        super();
        Timber.d("Constructor");
    }
    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitleFromPreferences(Context context, int appWidgetId) {
        Timber.d("loadTitleFromPreferences%s", appWidgetId);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId + KEY_TITLE, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.configure);
        }
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static int loadIdFromPreferences(Context context, int appWidgetId) {
        Timber.d("loadIdFromPreferences%s", appWidgetId);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int recipeValue = prefs.getInt(PREF_PREFIX_KEY + appWidgetId + KEY_ID, -1);
        if (recipeValue != -1) {
            return recipeValue;
        } else {
            return 0;
        }
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadImageFromPreferences(Context context, int appWidgetId) {
        Timber.d("loadImagrFromPreferences%s", appWidgetId);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String imageValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId + KEY_RECIPE_SELECTED_IMAGE, null);
        if (imageValue != null) {
            return imageValue;
        } else {
            return "";
        }
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadIngredientsFromPreferences(Context context, int appWidgetId) {
        Timber.d("loadIngredientsFromPreferences%s", appWidgetId);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId + KEY_INGREDIENTS, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deletePreferences(Context context, int appWidgetId) {
        Timber.d("deletePreferences%s", appWidgetId);
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    // Write the prefix to the SharedPreferences object for this widget
    void savePreferences(Context context, int appWidgetId) {
        Timber.d("savePreferences%s", appWidgetId);

        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + KEY_TITLE, mTitle);
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId + KEY_ID, mRecipeId);
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + KEY_RECIPE_SELECTED_IMAGE, mImageUrl);
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + KEY_INGREDIENTS, renderedIngredients(mRecipeSelected.getIngredients()));

        prefs.apply();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Timber.plant(new Timber.DebugTree());
        Timber.d("onCreate");

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.baking_app_widget_configure);

        mContext = BakingAppWidgetConfigureActivity.this;

//        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Timber.d("Fetching... (Async");
        new AsyncTask<URL, Void, Recipe[]>() {


            @Override
            protected Recipe[] doInBackground(URL... urls) {
                String results;
                Recipe[] recipes = null;
                try {
                    results = NetUtils.getRecipeData(new URL(NetUtils.RESULTS_URL));
                    recipes = NetUtils.getAllRecipes(results);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return recipes;
            }

            @Override
            protected void onPostExecute(Recipe[] r) {

                mRecipes = new NormalizeRecipes(r, mContext).getRecipes();
                updateUI();
            }
        }.execute();
    }

    private void updateUI() {

        Timber.d("updateUI");

        recipesList = new ArrayList<>();
        for (int i = 0; i < mRecipes.length; i++) {
            recipesList.add(i, mRecipes[i].getName());
        }
        mWidgetText = findViewById(R.id.widget_text);
        mWidgetList = findViewById(R.id.widget_list);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recipesList);

        mWidgetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Timber.d("onItemClick");
                mRecipeSelectedIndex = position;
                mRecipeSelected = mRecipes[mRecipeSelectedIndex];
                mTitle = mRecipeSelected.getName();
                mRecipeId = mRecipeSelected.getId();
                String placeHolder = "Recipe selected:" + mTitle;
                mWidgetText.setText(placeHolder);
            }
        });
        mWidgetList.setAdapter(listAdapter);

        mAddButton = findViewById(R.id.add_button);
        mAddButton.setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mWidgetText.setText(loadTitleFromPreferences(BakingAppWidgetConfigureActivity.this, mAppWidgetId));


    }

    public String renderedIngredients(Ingredient[] ingredientsArray) {

        Timber.d("CALLED");
//        String newLine="\n";
        String space = "   ";
        String renderIngredients = "<h1>INGREDIENTS</h1><br>";


        DecimalFormat df = new DecimalFormat("0.##");
        df.setDecimalSeparatorAlwaysShown(false);

        for (int i = 0; i < ingredientsArray.length; i++) {
            double q = ingredientsArray[i].getQuantity();
            String s = "";
            if (q > 1) {
                s = "s";
            }
            String str = ingredientsArray[i].getIngredient();
            renderIngredients = renderIngredients + "<b>" + df.format(q) + "</b>" + space + space + ingredientsArray[i].getMeasure() + s + space + "of " + str.substring(0, 1).toUpperCase() + str.substring(1) + "<br><br>";

        }
        return renderIngredients;
    }
}

