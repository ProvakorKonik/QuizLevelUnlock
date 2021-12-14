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
import com.konik.quizlevelunlock.Model.SubjectModel;
import com.konik.quizlevelunlock.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectAdapter_Holder> {
    private Context mContext;
    private List<SubjectModel> mData;
    private RecylerviewClickInterface recylerviewClickInterface;
    public SubjectAdapter (android.content.Context mContext, List<SubjectModel> mData, RecylerviewClickInterface recylerviewClickInterface) {
        this.mContext = mContext;
        this.mData = mData;
        this.recylerviewClickInterface = recylerviewClickInterface;
    }

    @NonNull
    @Override
    public SubjectAdapter_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_subject_item,parent,false); //connecting to cardview
        return new SubjectAdapter_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter_Holder holder, int position) {
        String dPhotoURL = mData.get(position).getSubjectPhotoUrl();
        Picasso.get().load(dPhotoURL).fit().centerCrop().into(holder.mSubjectItemImageView);
        String dsTitle = mData.get(position).getSubjectName();
        String dsBio = mData.get(position).getSubjectBio();
        holder.mSubjectItemTitleText.setText(dsTitle);
        holder.mSubjectItemBioText.setText(dsBio);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class SubjectAdapter_Holder extends RecyclerView.ViewHolder {

        ImageView mSubjectItemImageView;
        TextView mSubjectItemTitleText;
        TextView mSubjectItemBioText;

        public SubjectAdapter_Holder(@NonNull View itemView) {
            super(itemView);

            mSubjectItemImageView = (ImageView) itemView.findViewById(R.id.level_d_item_img);
            mSubjectItemTitleText = (TextView)itemView.findViewById(R.id.level_d_title_id);
            mSubjectItemBioText = (TextView)itemView.findViewById(R.id.level_d_bio_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recylerviewClickInterface .onItemClick(getAdapterPosition());
                }
            });

        }
    }



}
