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
import com.konik.quizlevelunlock.Model.RankModel;
import com.konik.quizlevelunlock.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RankAdapter  extends RecyclerView.Adapter<RankAdapter.RankAdapter_Holder> {
    private Context mContext;
    private List<RankModel> mData;
    private RecylerviewClickInterface recylerviewClickInterface;
    public RankAdapter (android.content.Context mContext, List<RankModel> mData, RecylerviewClickInterface recylerviewClickInterface) {
        this.mContext = mContext;
        this.mData = mData;
        this.recylerviewClickInterface = recylerviewClickInterface;
    }

    @NonNull
    @Override
    public RankAdapter.RankAdapter_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_rank_item,parent,false); //connecting to cardview
        return new RankAdapter.RankAdapter_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankAdapter.RankAdapter_Holder holder, int position) {
        //String dPhotoURL = mData.get(position).getCoPhotoUrl();
        //Picasso.get().load(dPhotoURL).into(holder.mItemUserImage);
        String dsParticipantUID = mData.get(position).getParticipantUID();
        long diResult = mData.get(position).getResult();
        long dlLiveRank = mData.get(position).getLiveRank();


        //holder.mItemTotalScore.setText(String.valueOf(dlLiveRank));
        String dsAnswerText = mData.get(position).getExtra();
        holder.mItemRankNo.setText(String.valueOf(dlLiveRank));
        holder.mItemAnswerText.setText(dsAnswerText);


        getUserData(holder,dsParticipantUID);
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference user_data_ref = db.collection("QuizMate").document("All_USER");

    private void getUserData(RankAdapter.RankAdapter_Holder holder, String dsParticipantUID) {
        if(!dsParticipantUID.equals("")){
            user_data_ref.collection("Reg_USER").document(dsParticipantUID).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                //Toast.makeText(getApplicationContext(),"User Information Found", Toast.LENGTH_SHORT).show();;

                                String dUserName = documentSnapshot.getString("name");
                                String dUserUniversity = documentSnapshot.getString("university");
                                String dUserPhotoURL = documentSnapshot.getString("photoURL");
                                String dTotal = documentSnapshot.getString("total");
                                //String dUserType = documentSnapshot.getString("userType");
                                long diPoints = documentSnapshot.getLong("points");

                                if(!dUserPhotoURL.equals("NO")){
                                    Picasso.get().load(dUserPhotoURL).into(holder.mItemUserImage);
                                }
                                holder.mItemUserName.setText(dUserName);

                            }else{
                                holder.mItemUserName.setText("Data not found");
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class RankAdapter_Holder extends RecyclerView.ViewHolder {

        ImageView mItemUserImage;
        TextView mItemUserName;
        TextView mItemRankNo;
        TextView mItemAnswerText;

        public RankAdapter_Holder(@NonNull View itemView) {
            super(itemView);

            mItemUserImage = (ImageView) itemView.findViewById(R.id.card_rank_user_im);
            mItemUserName = (TextView)itemView.findViewById(R.id.card_rank_user_name);
            mItemRankNo = (TextView)itemView.findViewById(R.id.card_rank_no_text);
            mItemAnswerText = (TextView)itemView.findViewById(R.id.card_rank_answer_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recylerviewClickInterface .onItemClick(getAdapterPosition());
                }
            });
        }
    }



}
