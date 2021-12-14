package com.konik.quizlevelunlock.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.konik.quizlevelunlock.Model.LevelModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LevelVM extends AndroidViewModel {
    public LevelVM(@NonNull Application application) {
        super(application);
        Log.d("ViewModel", "allViewModel:4 LevelVM start");
    }
    public MutableLiveData mLiveData;
    public MutableLiveData<List<LevelModel>> LevelsList(String dsSubjectUId) {
        List<LevelModel> listLevelItem ; listLevelItem =new ArrayList<>();
        CollectionReference notebookRef;        //Firrebase database link
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("ViewModel", "allViewModel:4 LevelVM 2start");

        notebookRef =  db.collection("QuizMate").document("Information").collection("AllSubjects");


        if(mLiveData == null) {
            mLiveData = new MutableLiveData();
            notebookRef.document(dsSubjectUId).collection("AllLevel").orderBy("LeveliPriority", Query.Direction.ASCENDING).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {   //documnet er vitore je multiple document query ache er jonno for loop
                            String data = "";
                            if(queryDocumentSnapshots.isEmpty()) {
                                //String levelUID, String levelName, String levelPhotoUrl, String levelSyllabus, String levelExtra, String levelCreator, long leveliViewCount, long leveliTotalQues, long leveliPriority, long leveliCoins, long leveliTotalParticipant, long leveliDuration, long leveliDate) {
                                listLevelItem.add(new LevelModel("UID","NULL", "PhotoUrl","Syllabus", "levelExtra",  "coCreator" , 0, 0, 0,0,0,0,null));
                                mLiveData.postValue(listLevelItem);
                                Log.d("ViewModel", "allViewModel:4x queryDocumentSnapshots empty");
                            }else {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Log.d("ViewModel", "allViewModel:4  queryDocumentSnapshots size "+ queryDocumentSnapshots.size());
                                    LevelModel contestsModel = documentSnapshot.toObject(LevelModel.class);
                                    //messageModel.setDocumentID(documentSnapshot.getId());
                                    String dsLevel_UID = documentSnapshot.getId();
                                    String dsLevel_Name = contestsModel.getLevelName();
                                    String dsLevel_PhotoUrl = contestsModel.getLevelPhotoUrl();
                                    String dsLevel_Syllabus = contestsModel.getLevelSyllabus();
                                    String dsLevel_Extra = contestsModel.getLevelExtra();
                                    String dsLevel_Creator = contestsModel.getLevelCreator();

                                    long diLevel_ViewLevelunt = contestsModel.getLeveliViewCount();
                                    long diLevel_TotalQuestion = contestsModel.getLeveliTotalQues();
                                    long diLevel_Priority = contestsModel.getLeveliPriority();
                                    long diLevel_ViewCount = contestsModel.getLeveliViewCount();
                                    long diLevel_Participent = contestsModel.getLeveliTotalParticipant();
                                    long diLevel_Duration = contestsModel.getLeveliDuration();

                                    //String bookUID, String bookName, String bookPhotoUrl, String bookBio, String bookCreator, String bookExtra, String bookPassword, int bookiPriority, int bookiViewCount, int bookiTotalLevel
                                    listLevelItem.add(new LevelModel(dsLevel_UID,dsLevel_Name, dsLevel_PhotoUrl,dsLevel_Syllabus, dsLevel_Extra,  dsLevel_Creator,
                                            diLevel_ViewCount, diLevel_TotalQuestion, diLevel_Priority, diLevel_ViewCount,diLevel_Participent ,diLevel_Duration ,contestsModel.getLeveliDate() ));
                                    mLiveData.postValue(listLevelItem);
                                }
                                mLiveData.postValue(listLevelItem);    //All Items level 4 , it is a one type category

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
