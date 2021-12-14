package com.konik.quizlevelunlock.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.konik.quizlevelunlock.View.QuestionModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuestionsListVM extends AndroidViewModel {
    public QuestionsListVM(@NonNull Application application) {
        super(application);
        Log.d("ViewModel", "allViewModel:4 QuestionsListVM start");
    }
    public MutableLiveData mLiveData;
    public MutableLiveData<List<QuestionModel>> ContestsList(String SubjectUID, String dsLevelUID) {

        List<QuestionModel> listQuestiontItem ; listQuestiontItem = new ArrayList<>();
        CollectionReference notebookRef;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("ViewModel", "allViewModel:4 LoadLevel4List start");

        notebookRef = db.collection("QuizMate").document("Information").collection("AllSubjects")
                .document(SubjectUID).collection("AllLevel").document(dsLevelUID).collection("AllQuestion");//.document(dsLevelUID);

                //db.collection("Quiz").document("Data")
                //.collection("AllBooks").document(dsBookUID).collection("AllContest").document(dsContestUID).collection("AllQuestion");


        if(mLiveData == null) {
            mLiveData = new MutableLiveData();
            notebookRef.orderBy("Serial", Query.Direction.ASCENDING).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {   //documnet er vitore je multiple document query ache er jonno for loop
                            String data = "";
                            if(queryDocumentSnapshots.isEmpty()) {
                                //String quesUID, String userSelectedAnswer, String ques, String ans, String f2, String f3, String f4, String solution, String photoUrl, String extra, String serial) {
                                listQuestiontItem.add(new QuestionModel("NULL","userSelectedAnswer",1, "ques","ans", "f2", "f3", "f4" , "solution", "photoUrl", "extra",0));
                                mLiveData.postValue(listQuestiontItem);
                                Log.d("ViewModel", "allViewModel:4 queryDocumentSnapshots empty");
                            }else {
                                int diSerial = 1;
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    QuestionModel questionModel = documentSnapshot.toObject(QuestionModel.class);
                                    String dsQuesUID = questionModel.getQuesUID();
                                    String dsUserSelectedAnswer = questionModel.getUserSelectedAnswer();
                                    String dsQues = questionModel.getQuestion();
                                    String dsAns = questionModel.getAnswer();
                                    String dsF2 = questionModel.getFake2();
                                    String dsF3 = questionModel.getFake3();
                                    String dsF4 = questionModel.getFake4();
                                    String dsSolution = questionModel.getSuggestion();
                                    String dsPhotoUrl = questionModel.getPhotoUrl();
                                    String dsExtra = questionModel.getExtra();
                                    long dlSerial = diSerial;   diSerial++;
                                    //String bookUID, String bookName, String bookPhotoUrl, String bookBio, String bookCreator, String bookExtra, String bookPassword, int bookiPriority, int bookiViewCount, int bookiTotalLevel
                                    listQuestiontItem.add(new QuestionModel(dsQuesUID,dsUserSelectedAnswer,1, dsQues,dsAns,dsF2, dsF3,  dsF4,dsSolution,dsPhotoUrl,dsExtra,dlSerial));
                                    mLiveData.postValue(listQuestiontItem);
                                }
                                mLiveData.postValue(listQuestiontItem);
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

