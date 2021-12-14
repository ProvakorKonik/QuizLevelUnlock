package com.konik.quizlevelunlock.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.konik.quizlevelunlock.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class LevelAdd extends AppCompatActivity {

    private ImageView mLevelImage;
    private EditText mLevelName;
    private EditText mLevelTotalQuestion, mLevelCoins;
    private EditText mLevelDuration, mLevelMinimumScore;

    private Button mLevelUpdateBtn;

    //Firebase Auth
    private String dUserUID = "NO";
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //For going to Account Activity Page

    //Photo Selecting
    private final int CODE_IMG_GALLERY = 1;
    private final String SAMPLE_CROPPED_IMG_NAME = "SampleCropIng";
    Uri imageUri_storage;
    Uri imageUriResultCrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_add);

        mLevelImage = (ImageView) findViewById(R.id.level_add_imageview);
        mLevelName = (EditText) findViewById(R.id.level_add_name_edit);
        mLevelTotalQuestion = (EditText) findViewById(R.id.level_add_total_question_edit);
        mLevelMinimumScore = (EditText)findViewById(R.id.level_add_minimum_score);
        mLevelCoins = (EditText) findViewById(R.id.level_add_coins_edit);
        mLevelDuration = (EditText) findViewById(R.id.level_add_duration_edit);


        mLevelUpdateBtn = (Button)findViewById(R.id.level_add_update_btn);

        //Error Auth Check is not declared
        mLevelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent() //Image Selecting
                        .setAction(Intent.ACTION_GET_CONTENT)
                        .setType("image/*"), CODE_IMG_GALLERY);
            }
        });

        getIntentMethod();
        mLevelUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });


    }
    private String dsLevelName = "NO", dsLevelPriority = "NO";
    private String  dsLevelTotalQuestion = "NO",dsLevelMinimumScore = "NO", dsLevelCoins = "NO";
    private String dsLevelPassword = "NO",dsLevelDuration = "NO";
    private int diPriority = 0, diLevelCoins = 0,  diLevelDuration = 0;
    private int diLevelTotalQuestion = 0;
    private void CheckData() {
        dsLevelName = mLevelName.getText().toString();


        dsLevelTotalQuestion = mLevelTotalQuestion.getText().toString();
        dsLevelMinimumScore = mLevelMinimumScore.getText().toString();
        dsLevelCoins = mLevelCoins.getText().toString();

        dsLevelDuration = mLevelDuration.getText().toString();
        //long Level date remain


        if(imageUriResultCrop == null){
            Toast.makeText(getApplicationContext(),"Click Image to add", Toast.LENGTH_SHORT).show();;
        }else if(dsLevelName.equals("NO") ||  dsLevelMinimumScore.equals("NO")
                || dsLevelDuration.equals("NO") ){
            Toast.makeText(getApplicationContext(),"Please fillup all ", Toast.LENGTH_SHORT).show();;
        }else if(dsLevelName.equals("") || dsLevelMinimumScore.equals("")
                ||  dsLevelDuration.equals("")  ){
            Toast.makeText(getApplicationContext(),"Please fillup all ", Toast.LENGTH_SHORT).show();;
        }else if(intentFoundError){
            Toast.makeText(getApplicationContext(),"Intent error", Toast.LENGTH_SHORT).show();;
        }else{


            diLevelTotalQuestion = Integer.parseInt(dsLevelTotalQuestion);
            diPriority = 0;
            diLevelCoins = Integer.parseInt(dsLevelCoins);
            diLevelDuration = Integer.parseInt(dsLevelDuration);
            UploadCropedImageFunction(imageUriResultCrop);
        }

    }
    //Firebase Storage
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();;
    StorageReference ref;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void UploadCropedImageFunction(Uri filePath) {
        if(filePath != null)
        {
            dUserUID = FirebaseAuth.getInstance().getUid();
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String dsTimeMiliSeconds = String.valueOf(System.currentTimeMillis());
            ref = storageReference.child("QuizMate/"+"SubjectCoverPic"+"/"+SubjectUID+"/"+ dsLevelName+" "+dsTimeMiliSeconds +"."+getFileExtention(filePath));
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        //Photo Uploaded now get the URL
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String dPhotoURL = uri.toString();
                                    Toast.makeText(getApplicationContext(), "Photo Uploaded", Toast.LENGTH_SHORT).show();
                                    DocumentReference subjectRef = db.collection("QuizMate").document("Information").collection("AllSubjects")
                                            .document(SubjectUID);
                                    subjectRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if(documentSnapshot.exists()){
                                                final long diTotalLevel = documentSnapshot.getLong("SubjectiTotalLevel") +1;

                                                //Uploading
                                                Map<String, Object> note = new HashMap<>();
                                                note.put("LevelName", dsLevelName);
                                                note.put("LevelPhotoUrl", dPhotoURL);
                                                note.put("LevelSyllabus", "NO");
                                                //note.put("LevelPassword", dsLevelPassword); //Error
                                                note.put("LevelExtra", dsLevelMinimumScore);
                                                note.put("LevelCreator", dUserUID);
                                                note.put("LeveliViewCount", 0);
                                                note.put("LeveliTotalQues", diLevelTotalQuestion);
                                                note.put("LeveliPriority", diTotalLevel);
                                                note.put("LeveliCoins", diLevelCoins);
                                                note.put("LeveliTotalParticipant", 0);
                                                note.put("LeveliDuration", diLevelDuration);

                                                FieldValue ddDate =  FieldValue.serverTimestamp();
                                                note.put("LeveliDate", ddDate); // Date of Level

                                                subjectRef.collection("AllLevel").document(String.valueOf(diTotalLevel)).set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getApplicationContext(),"Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                                        subjectRef.update("SubjectiTotalLevel",diTotalLevel);


                                                        progressDialog.dismiss();
                                                        mLevelUpdateBtn.setText("UPDATED");
                                                        mLevelName.setText("");
                                                        mLevelTotalQuestion.setText("");
                                                        finish();
                                                        Intent intent = new Intent(LevelAdd.this, Level.class);    //Error Intent not sent
                                                        intent.putExtra("SubjectUID", SubjectUID);
                                                        intent.putExtra("SubjectName", SubjectName);
                                                        startActivity(intent);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(),"Failed to upload data", Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                        mLevelUpdateBtn.setText("Try Again");
                                                        mLevelName.setText("Failed");

                                                        Toast.makeText(getApplicationContext(),"Failed Please Try Again", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }else{
                                                Toast.makeText(getApplicationContext(),"Serial Not Found", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(getApplicationContext(),"SubjectUID = "+SubjectUID, Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                mLevelUpdateBtn.setText("Try Again");
                                                mLevelName.setText("Failed");

                                                Toast.makeText(getApplicationContext(),"Failed Please Try Again", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            mLevelUpdateBtn.setText("Try Again");
                                            mLevelName.setText("Failed");
                                            Toast.makeText(getApplicationContext(),"Failed Please Try Again", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            mLevelUpdateBtn.setText("Failed Photo Upload");
                            Toast.makeText(getApplicationContext(), "Failed Photo", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), "Upload Failed Photo Not Found ", Toast.LENGTH_SHORT).show();
        }
    }

    String SubjectUID = "NO", SubjectName = "NO";
    private boolean intentFoundError = true;
    private void getIntentMethod() {
        //////////////GET INTENT DATA
        final Intent intentx = getIntent();
        if(intentx.getExtras() != null)
        {
            SubjectUID = intentx.getExtras().getString("SubjectUID");    //Sylhet
            SubjectName = intentx.getExtras().getString("SubjectName");    //Grocery or Food or Home Services
            intentFoundError = CheckIntentMethod(SubjectUID);
            intentFoundError = CheckIntentMethod(SubjectName);

            if(!intentFoundError){
                //callViewModel();
            }
        }else{
            SubjectUID = "NO";
            SubjectName = "NO";
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


    @Override   //Selecting Image
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_IMG_GALLERY && resultCode == RESULT_OK &&  data.getData() != null && data != null){
            //Photo Successfully Selected

            imageUri_storage = data.getData();
            String dFileSize = getSize(imageUri_storage);       //GETTING IMAGE FILE SIZE
            double  dFileSizeDouble = Double.parseDouble(dFileSize);
            int dMB = 1000;
            dFileSizeDouble =  dFileSizeDouble/dMB;
            //dFileSizeDouble =  dFileSizeDouble/dMB;

            if(dFileSizeDouble <= 5000){
                Picasso.get().load(imageUri_storage).resize(200, 200).centerCrop().into(mLevelImage);
                Toast.makeText(getApplicationContext(),"Selected",Toast.LENGTH_SHORT).show();
                imageUriResultCrop = imageUri_storage;
            }else{
                Toast.makeText(this, "Failed! (File is Larger Than 5MB)",Toast.LENGTH_SHORT).show();
            }


        }else {
            Toast.makeText(this, "Canceled",Toast.LENGTH_SHORT).show();
        }
    }
    public String getSize(Uri uri) {
        String fileSize = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {

                // get file size
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (!cursor.isNull(sizeIndex)) {
                    fileSize = cursor.getString(sizeIndex);
                }
            }
        } finally {
            cursor.close();
        }
        return fileSize;
    }
    private String getFileExtention(Uri uri){   //IMAGE
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        //Not worked in Croped File so i constant it
        return "JPEG";
    }

    @Override
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        /*if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }*/
    }
}