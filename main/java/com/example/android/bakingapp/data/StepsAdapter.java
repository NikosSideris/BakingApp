package com.example.android.bakingapp.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Step;

/**
 * Created by Nikos on 05/07/18.
 */
public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.CardViewHolder> {
    private static final String TAG = "STEPSADAPTER";
    private Context mContext;
    private Step[] mSteps;
    final private ItemClickListener mOnItemClickListener;
    private static SparseBooleanArray sSelectedItems;

    private Integer lastIndex;
    private CardViewHolder lastView;

    public StepsAdapter(Context mContext, Step[] mSteps, ItemClickListener mOnItemClickListener) {
        this.mContext = mContext;
        this.mSteps = mSteps;
        this.mOnItemClickListener = mOnItemClickListener;
        sSelectedItems = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_select_a_step_rv_item, parent, false);

        return new CardViewHolder(itemView);
    }

    public interface ItemClickListener {
        void onItemClickListener(int index);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        final Step step = mSteps[position];
        holder.short_description.setText(step.getShortDescription());
        String thumbUrl = step.getThumpnailUrl();

        if (!thumbUrl.isEmpty()) {
            Glide.with(mContext).load(step.getThumpnailUrl()).into(holder.thumbnail);
        } else {
            holder.thumbnail.setVisibility(View.GONE);
        }

        holder.mBackground.setSelected(sSelectedItems.get(position, false));

    }

    @Override
    public int getItemCount() {
        return mSteps.length;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView short_description;
        ImageView thumbnail;
        LinearLayout mBackground;

        public CardViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.iv_step_thumb);
            short_description = view.findViewById(R.id.tv_step_short_description);
            mBackground = view.findViewById(R.id.ll_step_row);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            if (lastIndex == null) {
                lastIndex = index;
                lastView = CardViewHolder.this;
                lastView.mBackground.setSelected(true);
            } else {
                lastView.mBackground.setSelected(false);
                lastIndex = index;
                lastView = CardViewHolder.this;
                lastView.mBackground.setSelected(true);
            }
            Log.d(TAG, "onClick: LAST INDEX");

/*            ****************for coloring ALL PRESSED ****************************
            if (sSelectedItems.get(index, false)) {
                sSelectedItems.delete(index);
                mBackground.setSelected(false);
//                mLabel.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
            } else {

//                mLabel.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                sSelectedItems.put(index, true);
                mBackground.setSelected(true);
            }
            ******************************************************
*/

            mOnItemClickListener.onItemClickListener(index);
        }
    }


}
