package com.bakhtiar.quizmate.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bakhtiar.quizmate.Model.RankModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RankVM  extends AndroidViewModel {
    public RankVM(@NonNull Application application) {
        super(application);
        Log.d("ViewModel", "allViewModel:4 RankVM start");
    }
    public MutableLiveData mLiveData;
    public MutableLiveData<List<RankModel>> RankUserListt(String SubjectUID, String dsLevelUID) {
        List<RankModel> listRankItem ; listRankItem =new ArrayList<>();
        CollectionReference userRankRef;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("ViewModel", "allViewModel:4 RankVM start");

        userRankRef = db.collection("QuizMate").document("Information").collection("AllSubjects")
                .document(SubjectUID).collection("AllLevel").document(dsLevelUID).collection("Participant");

        if(mLiveData == null) {
            mLiveData = new MutableLiveData();
            userRankRef.orderBy("Result", Query.Direction.DESCENDING).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {   //documnet er vitore je multiple document query ache er jonno for loop
                            String data = "";
                            if(queryDocumentSnapshots.isEmpty()) {

                                //String uid,String choosed, String extra, String participantUID, long result, long liveRank
                                listRankItem.add(new RankModel("UID","NULL", "extra","participantUID",  0,0));
                                mLiveData.postValue(listRankItem);
                                Log.d("ViewModel", "allViewModel:4 queryDocumentSnapshots empty");
                            }else {
                                int diListSize = queryDocumentSnapshots.size();
                                long dlRankNo = 1;
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    RankModel rankModel = documentSnapshot.toObject(RankModel.class);
                                    //messageModel.setDocumentID(documentSnapshot.getId());
                                    String dsRankUserUID = documentSnapshot.getId();
                                    String dsRankUserChoosed = rankModel.getChoosed();
                                    String dsRankUserExtra = rankModel.getExtra();
                                    String dsRankUserParticipantUID = rankModel.getParticipantUID();

                                    long diLiveRank = rankModel.getLiveRank();
                                    long diResult = rankModel.getResult();

                                    listRankItem.add(new RankModel(dsRankUserUID,dsRankUserChoosed,dsRankUserExtra,dsRankUserParticipantUID,diResult,dlRankNo ));
                                    mLiveData.postValue(listRankItem);
                                    dlRankNo++;
                                }
                                //mLiveData.postValue(listRankItem);    //All Items level 4 , it is a one type category

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }

        return mLiveData;
    }
}
