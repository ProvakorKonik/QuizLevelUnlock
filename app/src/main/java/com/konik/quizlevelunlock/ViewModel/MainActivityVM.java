package com.konik.quizlevelunlock.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;
import android.util.Log;

import com.konik.quizlevelunlock.Model.SubjectModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivityVM extends AndroidViewModel {
    public MainActivityVM(@NonNull Application application) {
        super(application);
        Log.d("ViewModel", "allViewModel:4 Level_D_VM start");
    }
    public MutableLiveData mLiveData;
    public MutableLiveData<List<SubjectModel>> LoadSubjectList() {
        List<SubjectModel> listSubjectItem ; listSubjectItem =new ArrayList<>();
        CollectionReference notebookRef;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("ViewModel", "allViewModel:4 LoadLevel4List start");

        notebookRef = db.collection("QuizMate").document("Information").collection("AllSubjects");

        if(mLiveData == null) {
            mLiveData = new MutableLiveData();
            notebookRef
                    .orderBy("SubjectiPriority", Query.Direction.ASCENDING).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {   //documnet er vitore je multiple document query ache er jonno for loop
                            String data = "";
                            if(queryDocumentSnapshots.isEmpty()) {
                                //String bookName, String bookPhotoUrl, String bookBio, String bookCreator, String bookExtra, String bookPassword, int bookiPriority, int bookiViewCount, int bookiTotalLevel
                                listSubjectItem.add(new SubjectModel("UID","NULL", "PhotoUrl", "bookBio", "bookCreator", "bookExtra", "bookPassword", 0, 0, 0));
                                mLiveData.postValue(listSubjectItem);
                                Log.d("ViewModel", "allViewModel:4 queryDocumentSnapshots empty");
                            }else {

                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    SubjectModel book_model = documentSnapshot.toObject(SubjectModel.class);
                                    //messageModel.setDocumentID(documentSnapshot.getId());
                                    String dsBooK_UID = documentSnapshot.getId();
                                    String dsBooK_Name = book_model.getSubjectName();
                                    String dsBooK_PhotoUrl = book_model.getSubjectPhotoUrl();
                                    String dsBooK_Bio= book_model.getSubjectBio();

                                    String dsbookBio= book_model.getSubjectBio();
                                    String dsbookCreator= book_model.getSubjectCreator();
                                    String dsbookExtra = book_model.getSubjectExtra();
                                    String dsbookPassword = book_model.getSubjectPassword();
                                    int dibookiPriority = book_model.getSubjectiPriority();
                                    int dibookiViewCount = book_model.getSubjectiViewCount();
                                    int dibookiTotalLevel = book_model.getSubjectiTotalLevel();
                                    //String bookUID, String bookName, String bookPhotoUrl, String bookBio, String bookCreator, String bookExtra, String bookPassword, int bookiPriority, int bookiViewCount, int bookiTotalLevel
                                    listSubjectItem.add(new SubjectModel(dsBooK_UID,dsBooK_Name, dsBooK_PhotoUrl,dsbookBio, dsbookCreator,  dsbookExtra, dsbookPassword, dibookiPriority, dibookiViewCount, dibookiTotalLevel));
                                    mLiveData.postValue(listSubjectItem);
                                }
                                mLiveData.postValue(listSubjectItem);    //All Items level 4 , it is a one type category

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