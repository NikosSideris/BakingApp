package com.example.android.bakingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingapp.data.RecipesAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utilities.Constants;
import com.example.android.bakingapp.utilities.NetUtils;
import com.example.android.bakingapp.utilities.NormalizeRecipes;
import com.example.android.bakingapp.utilities.ScreenInfo;

import java.io.IOException;
import java.net.URL;

import timber.log.Timber;

public class SelectARecipeActivity extends AppCompatActivity implements RecipesAdapter.ItemClickListener{
    private static final String TAG = "MAIN";

    private Recipe[] mRecipes = null;

    public Context mContext;
    private ScreenInfo mDevice;

    private RecyclerView mRecyclerView;
    private RecipesAdapter mAdapterMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_a_recipe);

        Timber.plant(new Timber.DebugTree());

        Timber.d("Started: implements RecipesAdapter.ItemClickListener");

        mContext = getBaseContext();
//        boolean commsOk=checkInternet(this);
        if (!checkInternet(this)){
            Toast.makeText(mContext, "No internet Connection. Limited app functionality", Toast.LENGTH_LONG).show();
            Timber.d("No internet Connection");
        }else {
            if (savedInstanceState == null) {
                new FetchRecipes().execute();
                Timber.d("Fetching Recipes");
            } else {
                if (checkInternet(this)) {
                    mRecipes = (Recipe[]) savedInstanceState.getParcelableArray(Constants.RECIPES);
                    Timber.d("updateMainUI called");
                    updateMainUI();

                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onSaveInstanceState");
        if (mRecipes != null) {
            savedInstanceState.putParcelableArray(Constants.RECIPES, mRecipes);
        }
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    protected void onRestart() {
        Timber.d("onRestart");
//        Log.d(TAG, "onRestart");
        new FetchRecipes().execute();
        super.onRestart();
    }

    @Override
    public void onItemClickListener(int index) {

        Timber.d("onItemClickListener,switchToDetails ");
        switchToDetails(mRecipes[index]);
    }

    @SuppressLint("StaticFieldLeak")
    public class FetchRecipes extends AsyncTask<URL, Void, Recipe[]> {


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

            mRecipes = new NormalizeRecipes(r,mContext).getRecipes();
            updateMainUI();
        }
    }

    private void updateMainUI() {
//        Log.d(TAG, "updateMainUI");
        updateGridLayoutManager();
        Timber.d("updateMainUI");

        mAdapterMain = new RecipesAdapter(mContext, mRecipes, new RecipesAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(int index) {
                switchToDetails(mRecipes[index]);
            }
        });
        mRecyclerView.setAdapter(mAdapterMain);

    }

    private void updateGridLayoutManager(){
//        Log.d(TAG, "updateGridLayoutManager");
        int columns=1;
        Timber.d("updateGridLayoutManager");
        mDevice=new ScreenInfo(this);
        if (mDevice.inPortraitMode()) {
            if (mDevice.isTablet()) {columns = 2;}
        }else {                                     //landscape mode
            if (mDevice.isTablet()) {columns = 3;}
            else {columns = 2;}
        }

        mRecyclerView = findViewById(R.id.rv_select_recipe);
        GridLayoutManager gridLayoutManager=(GridLayoutManager) mRecyclerView.getLayoutManager();
        gridLayoutManager.setSpanCount(columns);
    }

    private boolean checkInternet(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }

    private void switchToDetails(Recipe recipe) {
        Log.d(TAG, "switchToDetails");
        Bundle bundle=new Bundle();
        bundle.putParcelable(Constants.RECIPE, recipe);
        Intent intent = new Intent(this, SelectAStepActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }
    @Override
    protected void onStart() {
        Timber.d("onStart");
        super.onStart();
    }


}