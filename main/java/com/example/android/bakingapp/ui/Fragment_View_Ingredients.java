package com.example.android.bakingapp.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;

import java.text.DecimalFormat;

import timber.log.Timber;


public class Fragment_View_Ingredients extends Fragment {
//    private static final String TAG="FVI";
//    private Context mContext;
    private static Ingredient[] ingredients;

    public Fragment_View_Ingredients() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.plant(new Timber.DebugTree());
        Timber.d("CREATED");
//        mContext = getContext();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_ingredients, container, false);
        // Inflate the layout for this fragment
        TextView textView=rootView.findViewById(R.id.tv_view_ingredients);
        Timber.d("INFLATED");
        if (ingredients!=null) {
            Timber.d("ingredients!=null");
            textView.setText(Html.fromHtml(renderedIngredients(ingredients)));
        }
        return rootView;
    }

    public String renderedIngredients(Ingredient[] ingredientsArray){

        Timber.d("CALLED");
//        String newLine="\n";
        String space="   ";
        String renderIngredients="<h1>INGREDIENTS</h1><br>" ;


        DecimalFormat df = new DecimalFormat("0.##");
        df.setDecimalSeparatorAlwaysShown(false);

        for (int i=0;i<ingredientsArray.length;i++){
            double q=ingredientsArray[i].getQuantity();
            String s="";
            if (q>1){s="s";}
            String str=ingredientsArray[i].getIngredient();
            renderIngredients=renderIngredients
                    + "<b>"+ df.format(q) +"</b>" + space + space
                    + ingredientsArray[i].getMeasure() + s + space
                    + "of "
                    + str.substring(0, 1).toUpperCase() + str.substring(1)
                    + "<br><br>";

        }
        return renderIngredients;
    }

    public static void setIngredients(Ingredient[] ingredients) {
        Timber.d("Fragment_View_Ingredients.ingredients = ingredients;");
        Fragment_View_Ingredients.ingredients = ingredients;
    }
}
