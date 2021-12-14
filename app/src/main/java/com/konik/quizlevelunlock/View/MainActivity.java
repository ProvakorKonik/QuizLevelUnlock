package com.konik.quizlevelunlock.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.konik.RecylerviewClickInterface;
import com.konik.quizlevelunlock.Adapter.SubjectAdapter;
import com.konik.quizlevelunlock.LoginRegistration;
import com.konik.quizlevelunlock.LoginStart;
import com.konik.quizlevelunlock.Model.SubjectModel;
import com.konik.quizlevelunlock.R;
import com.konik.quizlevelunlock.ViewModel.MainActivityVM;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecylerviewClickInterface {

private Button mLoginBtn;
    private Button mAddSubject;

    private ImageView mUserImageView;
    private TextView mUserName, mUserEmailText;

    //RecyclerView
    private RecyclerView mSubject_RecyclerView;
    List<SubjectModel> listSubjectItem;
    SubjectAdapter mSubject_adapter;

    //Firebase Auth
    private String dUserUID = "NO", dUserEmail = "NO";
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //For going to Account Activity Page

    private String dsAdminEmail = "cse_1812020048@lus.ac.bd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserImageView = (ImageView) findViewById(R.id.home_profile_image) ;
        mUserName = (TextView)findViewById(R.id.home_profile_txt_user_name) ;
        mUserEmailText = (TextView)findViewById(R.id.home_profile_user_email) ;
        mAddSubject = (Button)findViewById(R.id.home_linear_profile_add_subject_btn);
        mLoginBtn = (Button)findViewById(R.id.home_linear_profile_login_btn);
        mSubject_RecyclerView = (RecyclerView)findViewById(R.id.home_book_recycler_view);
        mAddSubject.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() { ///for going to Account Activity Page
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    dUserUID = user.getUid();
                    dUserEmail = user.getEmail();
                    mLoginBtn.setText("Logout");
                    mUserEmailText.setText(dUserEmail);
                    if(dUserEmail.equals(dsAdminEmail)){
                        mAddSubject.setVisibility(View.VISIBLE);
                    }else{
                        mAddSubject.setVisibility(View.GONE);
                    }

                    checkUserData();
                    callViewModel();
                }else{

                }
            }
        };

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLoginBtn.getText().toString().equals("Logout")){
                    mAuth.signOut();
                    mLoginBtn.setText("Login");
                    Intent intent = new Intent(getApplicationContext(), LoginStart.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(), LoginStart.class);
                    startActivity(intent);
                }

            }
        });
        mAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SubjectAdd.class);
                startActivity(intent);
            }
        });
    }
    private static final String TAGO = "MainActivity";
    private void checkUserData() {
        Log.d(TAGO, "onActivityResult: checkUserData()");
        dUserUID = FirebaseAuth.getInstance().getUid();
        //Toast.makeText(getApplicationContext(),"checkUserData()", Toast.LENGTH_SHORT).show();;
        if(dUserUID.equals("")  || dUserUID == null ){
            Toast.makeText(getApplicationContext(),"Logged in but UID 404", Toast.LENGTH_SHORT).show();;
        }else{
            Log.d(TAGO, "onActivityResult: checkUserData() dUserUID found");
            //Please Modify Database Auth READ WRITE Condition if its not connect to database
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference user_data_ref = db.collection("QuizMate").document("All_USER");
            user_data_ref.collection("Reg_USER").document(dUserUID).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                String msName = documentSnapshot.getString("name");
                                String msuniversity = documentSnapshot.getString("university");
                                String msUserType = documentSnapshot.getString("userType");
                                String msUserPhotoURL = documentSnapshot.getString("photoURL");
                                long mlPoints = documentSnapshot.getLong("points");

                                Picasso.get().load(msUserPhotoURL).fit().centerCrop().into(mUserImageView);
                                mUserName.setText(msName);

                            }else{
                                Toast.makeText(getApplicationContext(),"User Information 404", Toast.LENGTH_SHORT).show();;
                                Intent intent = new Intent(getApplicationContext(), LoginRegistration.class);
                                startActivity(intent);

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull  Exception e) {
                    Log.d(TAGO, "onActivityResult: checkUserData() onFailure "+e);

                }
            });
        }
    }

    //View Model
    private MainActivityVM mainActivityVM;
    private void callViewModel() {
        Log.d("ViewModel", "allViewModel:1 mainActivityVM start");
        mainActivityVM = new ViewModelProvider(this).get(MainActivityVM.class);
        mainActivityVM.LoadSubjectList().observe(this, new Observer<List<SubjectModel>>() {
            @Override
            public void onChanged(List<SubjectModel> level_d_models) {
                Log.d("ViewModel", "allViewModel:1 onChanged listview4 size = "+level_d_models.size());
                if (level_d_models.get(0).getSubjectName().equals("NULL")){
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "No Subject Found", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                }else{

                    mSubject_adapter = new SubjectAdapter(MainActivity.this,level_d_models,MainActivity.this);
                    mSubject_adapter.notifyDataSetChanged();
                    listSubjectItem = level_d_models;
                    //It will swap from right to left
                    mSubject_RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));
                    mSubject_RecyclerView.setAdapter(mSubject_adapter);

                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        String SubjectUID = listSubjectItem.get(position).getSubjectUID();
        String SubjectName = listSubjectItem.get(position).getSubjectName();

        Intent intent = new Intent(getApplicationContext(), Level.class);
        intent.putExtra("SubjectUID", SubjectUID);
        intent.putExtra("SubjectName", SubjectName);
        startActivity(intent);

    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onRankItem(int position) {

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
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}