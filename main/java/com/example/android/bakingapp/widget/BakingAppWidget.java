package com.example.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;

import timber.log.Timber;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link BakingAppWidgetConfigureActivity BakingAppWidgetConfigureActivity}
 */
public class BakingAppWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        Timber.e("updateAppWidget" + appWidgetId);
//CharSequence
        String recipeTitle = BakingAppWidgetConfigureActivity.loadTitleFromPreferences(context, appWidgetId);
        int recipeId = (BakingAppWidgetConfigureActivity.loadIdFromPreferences(context, appWidgetId));
        String recipeImage = (BakingAppWidgetConfigureActivity.loadImageFromPreferences(context, appWidgetId));
        String recipeIngredients = (BakingAppWidgetConfigureActivity.loadIngredientsFromPreferences(context, appWidgetId));

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.appwidget_text, recipeTitle);
        if (!recipeImage.equals("")) {
            views.setImageViewUri(R.id.appwidget_image, Uri.parse(recipeImage));
        }

// Register an onClickListener
        Intent intent = new Intent(context, WidgetShowIngredients.class);
        Bundle bundle = new Bundle();
        bundle.putString(BakingAppWidgetConfigureActivity.KEY_TITLE, recipeTitle);
        bundle.putString(BakingAppWidgetConfigureActivity.KEY_INGREDIENTS, recipeIngredients);
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.rl_widget, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Timber.plant(new Timber.DebugTree());
        Timber.e("onUpdate");
        for (int appWidgetId : appWidgetIds) {
            Timber.e("id=" + appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.

        for (int appWidgetId : appWidgetIds) {
            Timber.e("onDeleted" + appWidgetId);
            BakingAppWidgetConfigureActivity.deletePreferences(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Timber.e("onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Timber.e("onDisabled");
    }


}

