package com.example.android.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.StepsAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.Objects;

import timber.log.Timber;

/**
 * Created by Nikos on 05/07/18.
 */
public class Fragment_Select_A_Step extends Fragment implements StepsAdapter.ItemClickListener {
    private static final String TAG="FSAS";
    private static Recipe sRecipe;
    private Context mContext;
    private Step[] mSteps;
    private StepsAdapter mStepsAdapter;


    public Fragment_Select_A_Step() {
    }


    @Override
    public void onItemClickListener(int index) {

        mOnItemClickListener.onFragmentStepListener(index);
    }

    FragmentStepListener mOnItemClickListener;

    public interface FragmentStepListener{
        void onFragmentStepListener(int index);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        Timber.d("onCreate");

        if (sRecipe != null) {
            Timber.plant(new Timber.DebugTree());
            Timber.d("started: implements StepsAdapter.ItemClickListener");
            mContext = getContext();
            if (mSteps==null) {
            /* Create a summy Step to include in mSteps so that 1st item in recycler view
            to be the Ingredients
             */
                Step dummyStepForIngredients = new Step();
                dummyStepForIngredients = initializeStep(dummyStepForIngredients);
                Step[] temporaryStepArray = sRecipe.getSteps();
                mSteps = new Step[temporaryStepArray.length + 1]; //initialize mSteps
                mSteps[0] = dummyStepForIngredients;
                for (int i = 1; i < temporaryStepArray.length + 1; i++) {
                    mSteps[i] = temporaryStepArray[i - 1];
                }

                Timber.d("Create a dummy Step for ingredients");
            }
        }
    }
        private Step initializeStep(Step step){
        Timber.d("initializeStep");
            Timber.d("initializeStep");
            step.setThumpnailUrl("");
            step.setVideoUrl("");
            step.setShortDescription("Ingredients");
            step.setDescription("");
            step.setId(-1);
            return step;
        }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_select_a_step, container, false);
        Timber.d("inflate(R.layout.fragment_select_a_step %s", container);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.rv_select_step);
        mStepsAdapter = new StepsAdapter(mContext, mSteps, this);

        mRecyclerView.setAdapter(mStepsAdapter);

        if (sRecipe == null) {
            Timber.d("sRecipe == null");
            int itemsNumber = Objects.requireNonNull(sRecipe).getSteps().length;

            for (int i = 0; i < itemsNumber; i++) {

                LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert layoutInflater != null;
                View v = layoutInflater.inflate(R.layout.fragment_select_a_step_rv_item, container, false);
                v.setId(i);
                mRecyclerView.addView(layoutInflater.inflate(R.layout.fragment_select_a_step_rv_item, container, false));
            }
        }
        return rootView;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Timber.d("onAttach");
        try{
            mOnItemClickListener=(FragmentStepListener) context;

        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+ " must implement FragmentStepListener");
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
        Timber.d("onDetach");

//        mOnItemClickListener = null;
    }

    public static void setsRecipe(Recipe sRecipe) {
        Timber.d("setsRecipe");
        Fragment_Select_A_Step.sRecipe = sRecipe;
    }


}
