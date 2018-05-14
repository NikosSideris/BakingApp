package com.example.android.bakingapp.utilities;

import android.content.Context;
import android.net.Uri;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;

/**
 * Created by Nikos on 05/04/18.
 */
public class NormalizeRecipes {

    /*
    Make sure input is suitable and valid
     */
    private Recipe[] mRecipes;
    private int numberOfRecipes;
    private Context mContext;

    private final static String DEFAULT_NAME = "Surprise Recipe";
    private final static String DEFAULT_STEP_SHORT_DESCR = "Surprise Step";

    public NormalizeRecipes(Recipe[] mRecipes, Context context) {
        this.mRecipes = mRecipes;
        numberOfRecipes = mRecipes.length;
        mContext=context;
        normalize();
    }
    public Recipe[] getRecipes() { //returns the recipes normalized
        return mRecipes;
    }

    private void normalize() {

        String placeHolder;
        /*name AND at least one ingredient AND one step MUST exist and be valid for a recipe to be valid*/
        for (int i = 0; i < numberOfRecipes; i++) {
            boolean nameFlag = false, ingredientFlag = false, stepFlag = false;
            Recipe recipe = mRecipes[i];
            //check name: if empty, put a default recipe name
            if (recipe.getName().isEmpty()) {
                recipe.setName(DEFAULT_NAME);
                nameFlag = true;
            }else{
                recipe.setName(RemoveSpecialCharacters.removeSpecialCharacters(recipe.getName()));
            }
            //check image: if no image, put ""
            if (!isImage(recipe.getImage())) {
                recipe.setImage("");
            }
            //check ingredients
            Ingredient[] ingredients = recipe.getIngredients();
            ArrayList<Integer> toBeRemoved = new ArrayList<>();
            for (int j = 0; j < ingredients.length; j++) {
                //check measure: if empty, put "--"
                Ingredient ingredient = ingredients[j];
                if (ingredient.getMeasure().isEmpty()) {
                    ingredient.setMeasure("--");
                }else{
                    placeHolder= RemoveSpecialCharacters.removeSpecialCharacters(ingredient.getMeasure());
                    ingredient.setMeasure(placeHolder.trim());  //trim spaces
                }
                //check ingredient: if empty
                if (ingredient.getIngredient().isEmpty()) {
                    toBeRemoved.add(j);
                }
                ingredients[j]= ingredient;
            }
            //remove empty ingredients
            int size=toBeRemoved.size();
            Ingredient[] temp=new Ingredient[ingredients.length-size];
            int count=0;
            for (int j=0;j<ingredients.length;j++){
                if (!toBeRemoved.contains(j)){
                    temp[count]=ingredients[j];
                    count++;
                }
            }
            ingredients=new Ingredient[temp.length];
            ingredients=temp;

            if (0 == ingredients.length) {
                ingredientFlag = true;
            }
                //check steps
                Step[] steps = recipe.getSteps();
                toBeRemoved = new ArrayList<>(); //reset toBeRemoved
                for (int j = 0; j < steps.length; j++) {
                    Boolean isBlank = false;
                    Step step = steps[j];
                    //check short description: if empty put default
                    String shortD = step.getShortDescription();
                    if (shortD.isEmpty()) {
                        step.setShortDescription(DEFAULT_STEP_SHORT_DESCR);
                        isBlank = true;
                    }else{
                        placeHolder=step.getShortDescription();
                        placeHolder= RemoveSpecialCharacters.removeSpecialCharacters(placeHolder);
                        step.setShortDescription(placeHolder);
                    }
                    //check description: if empty put short descr
                    String description = step.getDescription();
                    if (description.isEmpty()) {
                        step.setDescription(step.getShortDescription());
                        isBlank = isBlank && true;
                    }else{
                        placeHolder=step.getDescription();
                        placeHolder= RemoveSpecialCharacters.removeSpecialCharacters(placeHolder);
                        step.setDescription(placeHolder);
                    }
                    if (isBlank){stepFlag=true;}
                    //check video: if empty or not video put ""
                    String videoUri = step.getVideoUrl();
                    if (!isVideo(videoUri)) {
                        step.setVideoUrl("");
                    }
                    //check thumbnail: if empty or not image put ""
                    String thumbUri = step.getThumpnailUrl();
                    if (!isImage(thumbUri)) {
                        step.setThumpnailUrl("");
                    }
                    steps[j]= step;
                    if (isBlank) {
                        toBeRemoved.add(j);
                    }
                }

            size=toBeRemoved.size();
            Step[] tempStep=new Step[steps.length-size];
            count=0;
            for (int j=0;j<steps.length;j++){
                if (!toBeRemoved.contains(j)){
                    tempStep[count]=steps[j];
                    count++;
                }
            }
            steps=new Step[tempStep.length];
            steps=tempStep;

                mRecipes[i] = recipe;
                if(nameFlag || ingredientFlag || stepFlag){
                    //name, 1 ingredient and 1 step neccessary, so remove recipe
                    removeItem(mRecipes,i);
                    i--;
                    numberOfRecipes--;
                }
            }
        }


    private Recipe[] removeItem (Recipe[] r, int index){

        int numberOfItems=r.length;
        Recipe[] temp=new Recipe[numberOfItems-1];
        int j=0;
        for (int i=0;i<numberOfItems;i++){
            if (i!=index){
                temp[j]=r[i];
                j++;
            }
        }
        return temp;
    }
    private boolean isImage(String uriString){
        MimeType mimeType = new MimeType(mContext);
        Uri myUri = Uri.parse(uriString);
        if (uriString.isEmpty() || !mimeType.getMimeType(myUri).contains("image")) {
            return false;
        }
        return true;
    }

    private boolean isVideo(String uriString){
        MimeType mimeType = new MimeType(mContext);
        Uri myUri = Uri.parse(uriString);
        if (uriString.isEmpty() || !mimeType.getMimeType(myUri).contains("video")) {
            return false;
        }
        return true;
    }

}