package com.example.android.bakingapp.widget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;

import java.text.DecimalFormat;

import timber.log.Timber;

public class WidgetShowIngredients extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_show_ingredients);

        Timber.plant(new Timber.DebugTree());
        Timber.d("WidgetShowIngredients");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String title = extras.getString(BakingAppWidgetConfigureActivity.KEY_TITLE);
        String ingredients = extras.getString(BakingAppWidgetConfigureActivity.KEY_INGREDIENTS);


        TextView titleTv = findViewById(R.id.tv_showingredientswidget_title);
        TextView ingredientsTv = findViewById(R.id.tv_showingredientswidget_ingredients);

        titleTv.setText(title);
        ingredientsTv.setText(Html.fromHtml(ingredients));
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
