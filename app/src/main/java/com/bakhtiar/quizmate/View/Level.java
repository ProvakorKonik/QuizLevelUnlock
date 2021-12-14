package com.bakhtiar.quizmate.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bakhtiar.RecylerviewClickInterface;
import com.bakhtiar.quizmate.Adapter.LevelAdapter;
import com.bakhtiar.quizmate.Model.LevelModel;
import com.bakhtiar.quizmate.R;
import com.bakhtiar.quizmate.ViewModel.LevelVM;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Level extends AppCompatActivity implements RecylerviewClickInterface {
    private Button mAddBtn;

    //RecyclerView
    private RecyclerView mLevel_RecyclerView;
    List<LevelModel> listSubjectItem;
    LevelAdapter mLevel_Adapter;

    //Firebase Auth
    private String dUserUID = "NO", dUserEmail = "NO";
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //For going to Account Activity Page
    private String dsAdminEmail = "cse_1812020048@lus.ac.bd";

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        mAddBtn = (Button)findViewById(R.id.level_add_btn);

        mLevel_RecyclerView = (RecyclerView)findViewById(R.id.level_recyclerview);
        mAddBtn.setVisibility(View.GONE);
        getIntentMethod();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() { ///for going to Account Activity Page
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    dUserUID = user.getUid();
                    dUserEmail = user.getEmail();

                    if(dUserEmail.equals(dsAdminEmail)){
                        mAddBtn.setVisibility(View.VISIBLE);
                    }else{
                        mAddBtn.setVisibility(View.GONE);
                    }
                }else{

                }
            }
        };

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LevelAdd.class);
                intent.putExtra("SubjectUID", SubjectUID);
                intent.putExtra("SubjectName", SubjectName);
                startActivity(intent);
            }
        });



    }
    //View Model
    private LevelVM levelVM;
    private void callViewModel() {
        Log.d("ViewModel", "allViewModel:1 levelVM start");
        levelVM = new ViewModelProvider(this).get(LevelVM.class);
        levelVM.LevelsList(SubjectUID).observe(this, new Observer<List<LevelModel>>() {
            @Override
            public void onChanged(List<LevelModel> level_models) {
                Log.d("ViewModel", "allViewModel:1 onChanged listview4 size = "+level_models.size());
                if (level_models.get(0).getLevelName().equals("NULL")){
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "No Items Found", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                }else{

                    mLevel_Adapter = new LevelAdapter(Level.this,level_models,Level.this);
                    mLevel_Adapter.notifyDataSetChanged();
                    //
                    listSubjectItem = level_models;
                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        mLevel_RecyclerView.setLayoutManager(new GridLayoutManager(Level.this,2));
                        mLevel_RecyclerView.setAdapter(mLevel_Adapter);
                    } else {
                        //It will swap from right to left
                        mLevel_RecyclerView.setLayoutManager(new GridLayoutManager(Level.this,2));
                        mLevel_RecyclerView.setAdapter(mLevel_Adapter);
                    }
                }
            }
        });
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private void CheckPartipant(String dsBtnMode) {

        if(dsBtnMode.equals("StartExam")){
            Intent intent = new Intent(getApplicationContext(), QuestionsList.class);
            intent.putExtra("SubjectUID", SubjectUID);
            intent.putExtra("SubjectName", SubjectName);
            intent.putExtra("dsLevelUID", dsLevelUID);
            intent.putExtra("dsLevelName", "ERROR");
            intent.putExtra("dsBtnMode", "StartExam");
            intent.putExtra("diLevelDuration", diContestDuration);   //Error = Time should be automatically
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"You have not Participated!", Toast.LENGTH_SHORT).show();; //ERROR
        }
    }


    String SubjectUID = "NO", SubjectName = "NO", dsLevelUID = "NO";
    private boolean intentFoundError = true;
    private void getIntentMethod() {
        //////////////GET INTENT DATA
        final Intent intent = getIntent();
        if(intent.getExtras() != null)
        {
            SubjectUID = intent.getExtras().getString("SubjectUID");
            SubjectName = intent.getExtras().getString("SubjectName");
            intentFoundError = CheckIntentMethod(SubjectUID);
            intentFoundError = CheckIntentMethod(SubjectName);

            if(!intentFoundError){
                callViewModel();
            }else{
                Toast.makeText(getApplicationContext(),"Intent error", Toast.LENGTH_SHORT).show();;
            }
        }else{
            SubjectUID = "NO";
            SubjectName = "NO";
            Toast.makeText(getApplicationContext(),"Intent error", Toast.LENGTH_SHORT).show();
        }

    }
    private boolean CheckIntentMethod(String dsTestIntent){
        if(TextUtils.isEmpty(dsTestIntent)) {
            dsTestIntent= "NO";
            Toast.makeText(getApplicationContext(), "intent NULL  " , Toast.LENGTH_SHORT).show();
        }else if (SubjectUID.equals("")){
            dsTestIntent= "NO";
            Toast.makeText(getApplicationContext(), "intent 404" , Toast.LENGTH_SHORT).show();
        }else{
            intentFoundError = false;
        }
        return intentFoundError;
    }

    DocumentReference quesRef;
    public long diContestDuration = 0;
    @Override
    public void onItemClick(int position) {
        dsLevelUID = listSubjectItem.get(position).getLevelUID();
        diContestDuration = listSubjectItem.get(position).getLeveliDuration();
        String dsLevelExtraMandatoryScore = listSubjectItem.get(position).getLevelExtra();
        quesRef = db.collection("QuizMate").document("Information").collection("AllSubjects")
                .document(SubjectUID).collection("AllLevel").document(dsLevelUID); //
        if(dsLevelUID.equals("1")){
            //GO Start The Contest
            CheckPartipant("StartExam");
        }else{
            int diLevelUID = Integer.parseInt(dsLevelUID);
            String dsPreviousLevelUID = String.valueOf(diLevelUID-1);

            db.collection("QuizMate").document("Information").collection("AllSubjects")
                    .document(SubjectUID).collection("AllLevel").document(dsPreviousLevelUID).collection("Participant")
                    .document(dUserUID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        long dlUserResult = documentSnapshot.getLong("Result");
                        long dlExtraMandatoryScore = Long.parseLong(dsLevelExtraMandatoryScore);
                        if(dlUserResult >= dlExtraMandatoryScore){
                            Toast.makeText(getApplicationContext(),"Good Result ", Toast.LENGTH_SHORT).show();;
                            CheckPartipant("StartExam");
                        }else{
                            Toast.makeText(getApplicationContext(),"Previous result is Bad. Not allowing you !", Toast.LENGTH_SHORT).show();;
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Please Participated on Previous Contest ", Toast.LENGTH_SHORT).show();;
                    }
                }
            });

        }
    }

    @Override
    public void onItemLongClick(int position) {
        if(dUserEmail.equals(dsAdminEmail)){
            Intent intent = new Intent(getApplicationContext(), QuestionAdd.class);
            dsLevelUID = listSubjectItem.get(position).getLevelUID();
            intent.putExtra("SubjectUID", SubjectUID);
            intent.putExtra("dsLevelUID", dsLevelUID);
            startActivity(intent);
        }else{
            //Toast.makeText(getApplicationContext(),"Your are !", Toast.LENGTH_SHORT).show();;
        }
    }

    @Override
    public void onRankItem(int position) {
        Intent intent = new Intent(getApplicationContext(), Rank.class);
        dsLevelUID = listSubjectItem.get(position).getLevelUID();
        intent.putExtra("SubjectUID", SubjectUID);
        intent.putExtra("dsLevelUID", dsLevelUID);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}