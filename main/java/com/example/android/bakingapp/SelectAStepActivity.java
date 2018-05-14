package com.example.android.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.ui.Fragment_Select_A_Step;
import com.example.android.bakingapp.ui.Fragment_View_Ingredients;
import com.example.android.bakingapp.ui.Fragment_View_Step;
import com.example.android.bakingapp.utilities.Constants;


import java.util.List;
import java.util.Objects;

import timber.log.Timber;

/**
 * Created by Nikos on 05/10/18.
 */
public class SelectAStepActivity extends AppCompatActivity implements Fragment_Select_A_Step.FragmentStepListener, Fragment_View_Step.OnFragmentInteractionListener {
    private static Recipe sRecipe;
    private static Step[] sSteps;
    private Context mContext;

    public static int statusBarHeight;

    private static final String KEY = "currentStep";

    private static boolean dualPanel;
    private int currentStep;

    private Fragment_Select_A_Step mFragmentSelectAstep;
    private Fragment_View_Ingredients mFragmentViewIngredients;
    private Fragment_View_Step mFragment_view_step;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        Timber.d("onCreate");
        mContext = this;
        dualPanel = false;
        statusBarHeight = getStatusBarHeight();
        setContentView(R.layout.select_a_step);

        if (findViewById(R.id.fragment_view_a_step_container) != null) {
            dualPanel = true;
        }

        //Toolbar & ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar_sas);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle(null);
        }
        //First time initialization
        if (savedInstanceState == null) {
            Timber.d("savedInstance");
            sRecipe = Objects.requireNonNull(getIntent().getExtras()).getParcelable(Constants.RECIPE);
            if (sRecipe != null) {
                sSteps = sRecipe.getSteps();
            }

            //initialize the 3 fragments
            mFragment_view_step = new Fragment_View_Step();
            mFragmentViewIngredients = new Fragment_View_Ingredients();

            mFragmentSelectAstep = new Fragment_Select_A_Step();
            Fragment_Select_A_Step.setsRecipe(sRecipe);

            if (dualPanel) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_select_a_step_container, mFragmentSelectAstep).commit();
                Fragment_View_Ingredients.setIngredients(sRecipe.getIngredients());
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_view_a_step_container, mFragmentViewIngredients).commit();
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_select_a_step_container, mFragmentSelectAstep).commit();
            }
            currentStep = -1;

        } else {      //reCreated
            sRecipe = savedInstanceState.getParcelable(Constants.RECIPE);
            currentStep = savedInstanceState.getInt(KEY);
            if (currentStep != -1) {
                currentStep++;
            }
            onFragmentStepListener(currentStep);
        }
        if (actionBar != null) {
            actionBar.setTitle(sRecipe.getName());
        }
    }

    @Override
    public void onFragmentStepListener(int index) {
        Timber.d("onFragmentStepListener %s", index);

        mFragmentSelectAstep = new Fragment_Select_A_Step();
        Fragment_Select_A_Step.setsRecipe(sRecipe);


        if (index < 1) {      //ingredients
            mFragmentViewIngredients = new Fragment_View_Ingredients();
            Fragment_View_Ingredients.setIngredients(sRecipe.getIngredients());
            if (currentStep == -1) {
                if (dualPanel) {
                    clearFragmentsBackStack();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_select_a_step_container, mFragmentSelectAstep).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_view_a_step_container, mFragmentViewIngredients).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_select_a_step_container, mFragmentViewIngredients).addToBackStack(null).commit();
                }
            } else {
                currentStep = index;
                if (dualPanel) {
                    clearFragmentsBackStack();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_select_a_step_container, mFragmentSelectAstep).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_view_a_step_container, mFragmentViewIngredients).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_select_a_step_container, mFragmentViewIngredients).addToBackStack(null).commit();
                }
            }
        } else {      //steps
            currentStep = index - 1;
            mFragment_view_step = new Fragment_View_Step();
            Fragment_View_Step.setStep(sSteps[currentStep]);

            if (dualPanel) {
                clearFragmentsBackStack();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_select_a_step_container, mFragmentSelectAstep).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_view_a_step_container, mFragment_view_step).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_select_a_step_container, mFragment_view_step).addToBackStack(null).commit();
            }
        }

    }


    private boolean clearFragmentsBackStack() {
        try {
            FragmentManager manager = getSupportFragmentManager();
            List<Fragment> fragsList = manager.getFragments();
            if (fragsList.size() == 0) {
                return true;
            }
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onRestart() {
        Timber.d("onRestart");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Timber.d("onStart");
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Timber.d("onSaveInstanceState");
        if (sRecipe != null) {
            savedInstanceState.putParcelable(Constants.RECIPE, sRecipe);
            savedInstanceState.putInt(KEY, currentStep);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    public void buttonPreviousClicked(View view) {
        Timber.d("buttonPreviousClicked");
        if (currentStep >= 1) {
            currentStep--;
            onFragmentStepListener(1 + currentStep);
        } else {
            Toast.makeText(mContext, "This is the first step.", Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonNextClicked(View view) {
        Timber.d("buttonNextClicked");
        if (currentStep < (sSteps.length - 1)) {
            currentStep++;
            onFragmentStepListener(1 + currentStep);
        } else {
            Toast.makeText(mContext, "This is the last step.", Toast.LENGTH_SHORT).show();
        }
    }


    public int getStatusBarHeight() {
        Timber.d("getStatusBarHeight");
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        //not needed really

    }
}
