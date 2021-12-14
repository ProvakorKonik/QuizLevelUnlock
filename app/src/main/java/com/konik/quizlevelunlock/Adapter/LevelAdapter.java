package com.konik.quizlevelunlock.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.konik.RecylerviewClickInterface;
import com.konik.quizlevelunlock.Model.LevelModel;
import com.konik.quizlevelunlock.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelAdapter_Holder> {
    private Context mContext;
    private List<LevelModel> mData;
    private RecylerviewClickInterface recylerviewClickInterface;
    public LevelAdapter(Context mContext, List<LevelModel> mData, RecylerviewClickInterface recylerviewClickInterface) {
        this.mContext = mContext;
        this.mData = mData;
        this.recylerviewClickInterface = recylerviewClickInterface;
    }

    @NonNull
    @Override
    public LevelAdapter_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_level_item,parent,false); //connecting to cardview
        return new LevelAdapter_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelAdapter_Holder holder, int position) {
        String dPhotoURL = mData.get(position).getLevelPhotoUrl();
        Picasso.get().load(dPhotoURL).fit().centerCrop().into(holder.mItemImageView);

        String dsTitle = mData.get(position).getLevelName();
        long diViews = mData.get(position).getLeveliViewCount();
        String Syllabus = mData.get(position).getLevelSyllabus();
        String dsExtraMandatoryScore = mData.get(position).getLevelExtra();

        holder.mItemTittleText.setText(dsTitle);
        holder.mItemBioText.setText(Syllabus);

        if(position == 0){
            holder.mItemMandatoryScore.setText("Minimum Score = 0 ");
        }else{
            holder.mItemMandatoryScore.setText("Minimum Score = "+dsExtraMandatoryScore);
        }

        //holder.mItemViewText.setText(String.valueOf(diViews));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class LevelAdapter_Holder extends RecyclerView.ViewHolder {

        ImageView mItemImageView;
        TextView mItemTittleText;
        TextView mItemRankListText;
        TextView mItemBioText,mItemMandatoryScore;

        public LevelAdapter_Holder(@NonNull View itemView) {
            super(itemView);

            mItemImageView = (ImageView) itemView.findViewById(R.id.level_d_item_img);
            mItemTittleText = (TextView)itemView.findViewById(R.id.level_d_title_id);
            mItemRankListText = (TextView)itemView.findViewById(R.id.level_d_rank_text);
            mItemBioText = (TextView)itemView.findViewById(R.id.level_d_bio_text);
            mItemMandatoryScore = (TextView)itemView.findViewById(R.id.level_d_mandatory_socre_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recylerviewClickInterface .onItemClick(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    recylerviewClickInterface.onItemLongClick(getAdapterPosition());
                    return false;
                }
            });
            mItemRankListText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recylerviewClickInterface.onRankItem(getAdapterPosition());
                }
            });
        }
    }



}
