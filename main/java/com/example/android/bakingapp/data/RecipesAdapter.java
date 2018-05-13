package com.example.android.bakingapp.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;


/**
 * Created by Nikos on 05/02/18.
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.CardViewHolder> {

    private Context mContext;
    private Recipe[] mRecipes;
    final private ItemClickListener mOnItemClickListener;

    public RecipesAdapter(Context mContext, Recipe[] mRecipes, ItemClickListener listener) {
        this.mContext = mContext;
        this.mRecipes = mRecipes;
        mOnItemClickListener=listener;
    }

    public interface ItemClickListener{
        void onItemClickListener(int index);
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        ImageView image;

        CardViewHolder(View view) {
            super(view);
            image =  view.findViewById(R.id.iv_recipe_image);
            name = view.findViewById(R.id.tv_recipe_name);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int index=getAdapterPosition();
            mOnItemClickListener.onItemClickListener(index);
        }
    }
    @NonNull
    @Override
    public RecipesAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.select_a_recipe_rv_item, parent, false);

            return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipesAdapter.CardViewHolder holder, final int position) {

            final Recipe recipe = mRecipes[position];
            holder.name.setText(recipe.getName());

                    // loading recipe image if available, using Glide library
        if (!recipe.getImage().isEmpty() ) {
            Glide.with(mContext).load(recipe.getImage()).into(holder.image);
        }else{
            holder.image.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes.length;
    }

}
