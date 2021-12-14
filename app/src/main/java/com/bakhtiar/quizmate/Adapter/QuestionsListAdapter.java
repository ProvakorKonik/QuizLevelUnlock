package com.bakhtiar.quizmate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bakhtiar.quizmate.R;
import com.bakhtiar.quizmate.RecylerviewQuestionClickInterface;
import com.bakhtiar.quizmate.View.QuestionModel;

import java.util.List;

public class QuestionsListAdapter  extends RecyclerView.Adapter<QuestionsListAdapter.QuestionsListAdapter_Holder> {
    private Context mContext;
    private List<QuestionModel> mData;
    private RecylerviewQuestionClickInterface recylerviewQuestionClickInterface;
    public QuestionsListAdapter (android.content.Context mContext, List<QuestionModel> mData, RecylerviewQuestionClickInterface recylerviewQuestionClickInterface) {
        this.mContext = mContext;
        this.mData = mData;
        this.recylerviewQuestionClickInterface = recylerviewQuestionClickInterface;
    }

    @NonNull
    @Override
    public QuestionsListAdapter.QuestionsListAdapter_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_question_item,parent,false); //connecting to cardview
        return new QuestionsListAdapter.QuestionsListAdapter_Holder(view);
    }
    String dsSolution = "NO";
    @Override
    public void onBindViewHolder(@NonNull QuestionsListAdapter.QuestionsListAdapter_Holder holder, int position) {
        /*String dPhotoURL = mData.get(position).getCoPhotoUrl();
        Picasso.get().load(dPhotoURL).into(holder.mItemImageView);*/
        String dsQuestionTopic = mData.get(position).getQuestion();
        String dsAnswer = mData.get(position).getAnswer();
        String dsF2 = mData.get(position).getFake2();
        String dsF3 = mData.get(position).getFake3();
        String dsF4 = mData.get(position).getFake4();
        String dsSolution = mData.get(position).getSuggestion();
        long diSerialNo = mData.get(position).getSerial();


        holder.mQuestionTopicText.setText(diSerialNo+") "+dsQuestionTopic);
        holder.mRadioButtonA.setText(dsAnswer);
        holder.mRadioButtonB.setText(dsF2);
        holder.mRadioButtonC.setText(dsF3);
        holder.mRadioButtonD.setText(dsF4);

        String dsUserAnswered = mData.get(position).getUserSelectedAnswer();
        int diAnswerIndexNo = mData.get(position).getAnswerOnIndex();
        if(!dsUserAnswered.equals("NO")){
            if(dsUserAnswered.equals(dsAnswer)){
                holder.mRadioButtonA.setChecked(true);
                if (diAnswerIndexNo != 1){
                    holder.mRadioButtonA.setTextColor(mContext.getResources().getColor(R.color.red_700));
                }
            }else if(dsUserAnswered.equals(dsF2)){
                holder.mRadioButtonB.setChecked(true);
                if (diAnswerIndexNo != 2){
                    holder.mRadioButtonB.setTextColor(mContext.getResources().getColor(R.color.red_700));
                }
            }else if(dsUserAnswered.equals(dsF3)){
                holder.mRadioButtonC.setChecked(true);
                if (diAnswerIndexNo != 3){
                    holder.mRadioButtonC.setTextColor(mContext.getResources().getColor(R.color.red_700));
                }
            }else if(dsUserAnswered.equals(dsF4)){
                holder.mRadioButtonD.setChecked(true);
                if (diAnswerIndexNo != 4){
                    holder.mRadioButtonD.setTextColor(mContext.getResources().getColor(R.color.red_700));
                }
            }


            if (diAnswerIndexNo== 1){
                holder.mRadioButtonA.setTextColor(mContext.getResources().getColor(R.color.green_700));
            }else if(diAnswerIndexNo == 2){
                holder.mRadioButtonB.setTextColor(mContext.getResources().getColor(R.color.green_700));
            }else if(diAnswerIndexNo == 3){
                holder.mRadioButtonC.setTextColor(mContext.getResources().getColor(R.color.green_700));
            }else if(diAnswerIndexNo == 4){
                holder.mRadioButtonD.setTextColor(mContext.getResources().getColor(R.color.green_700));
            }
            holder.mRadioButtonA.setClickable(false);
            holder.mRadioButtonB.setClickable(false);
            holder.mRadioButtonC.setClickable(false);
            holder.mRadioButtonD.setClickable(false);
            holder.mQuestionSoltiontext.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }


    class QuestionsListAdapter_Holder extends RecyclerView.ViewHolder {


        TextView mQuestionTopicText;
        RadioGroup mQuestionRadioGroup;
        RadioButton mRadioButtonA, mRadioButtonB, mRadioButtonC, mRadioButtonD;
        TextView mQuestionSoltiontext;

        public QuestionsListAdapter_Holder(@NonNull View itemView) {
            super(itemView);

            mQuestionTopicText = (TextView)itemView.findViewById(R.id.card_question_topic);
            mQuestionSoltiontext = (TextView)itemView.findViewById(R.id.card_question_solution);
            mQuestionRadioGroup = (RadioGroup)itemView.findViewById(R.id.card_question_radio_group);
            mRadioButtonA = (RadioButton)itemView.findViewById(R.id.card_question_radio_a);
            mRadioButtonB = (RadioButton)itemView.findViewById(R.id.card_question_radio_b);
            mRadioButtonC = (RadioButton)itemView.findViewById(R.id.card_question_radio_c);
            mRadioButtonD = (RadioButton)itemView.findViewById(R.id.card_question_radio_d);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recylerviewQuestionClickInterface .onItemClick(getAdapterPosition());
                }
            });
            mRadioButtonA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recylerviewQuestionClickInterface.onARadioBtnCLick(getAdapterPosition());
                }
            });
            mRadioButtonB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recylerviewQuestionClickInterface.onBRadioBtnCLick(getAdapterPosition());
                }
            });
            mRadioButtonC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recylerviewQuestionClickInterface.onCRadioBtnCLick(getAdapterPosition());
                }
            });
            mRadioButtonD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recylerviewQuestionClickInterface.onDRadioBtnCLick(getAdapterPosition());
                }
            });
            mQuestionSoltiontext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String dsSolution = recylerviewQuestionClickInterface.mQuestionSoltiontext(getAdapterPosition());
                    String dsSolutionText = mQuestionSoltiontext.getText().toString();
                    if(dsSolutionText.equals("Solution")){
                        mQuestionSoltiontext.setText(dsSolution);
                    }else{
                        mQuestionSoltiontext.setText("Solution");
                    }
                }
            });


            ;
            /*mCategoryBtn = (Button) itemView.findViewById(R.id.card_category_btn);

            mCategoryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int postion = getAdapterPosition();
                    if (postion != RecyclerView.NO_POSITION && listener1 != null) {
                        listener1.onItemClick(getSnapshots().getSnapshot(postion), postion);

                    }
                }
            });*/
        }
    }



}
