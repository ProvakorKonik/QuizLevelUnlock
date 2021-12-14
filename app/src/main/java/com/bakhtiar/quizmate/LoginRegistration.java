package com.bakhtiar.quizmate;

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
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bakhtiar.quizmate.View.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginRegistration extends AppCompatActivity {
    private ImageView mUserProfilePic;
    private Button mUserInfoUpdateBtn;
    //private static final String NO = "NO";

    private String dUserType = "NO";
    private String dUserUID = "NO",dUserEmail = "NO", dUserRegistrationDate = "NO", dUserLastActivity = "NO"; private long diUserLastActivity = 0;
    private String dExtra = "NO"; int diSize = 0;
    //Photo Selecting and Croping
    private final int CODE_IMG_GALLERY = 1;
    private final String SAMPLE_CROPPED_IMG_NAME = "SampleCropIng";
    Uri imageUri_storage;
    Uri imageUriResultCrop;

    //Firebase Storage
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();;
    StorageReference ref;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference category_ref;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //For going to Account Activity Page
    private static final String TAGO = "LoginRegistration";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);

        mUserProfilePic = (ImageView)findViewById(R.id.image_add_user_name);;

        Log.d(TAGO, "onActivityResult:LoginRegistration Class Start");


        mUserInfoUpdateBtn = (Button)findViewById(R.id.user_infor_update_btn);
        //Login Check
       /* mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() { ///for going to Account Activity Page
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                String dsUserName = user.getDisplayName();
                mUserInfoName.setText(dsUserName);
                if(user != null){
                    Toast.makeText(getApplicationContext(),"Update Profile of "+dsUserName, Toast.LENGTH_SHORT).show();;

                }else{
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        };*/

        mUserProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent() //Image Selecting
                        .setAction(Intent.ACTION_GET_CONTENT)
                        .setType("image/*"), CODE_IMG_GALLERY);
                //go to this method >> onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
                Log.d(TAGO, "onActivityResult: Pic Clicked");
            }
        });
        mUserInfoUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dUserType = "Student";
                if(imageUriResultCrop == null  ){
                    if(imageUri_storage == null){
                        Toast.makeText(getApplicationContext(),"Please Select Image", Toast.LENGTH_SHORT).show();;
                    }else{
                        Toast.makeText(getApplicationContext(),"Please Crop Image", Toast.LENGTH_SHORT).show();;
                    }

                }else{
                    String date = String.valueOf(System.currentTimeMillis());
                    dUserRegistrationDate = date;
                    dUserLastActivity = date;
                    long diDate = System.currentTimeMillis();
                    long diDate2 = 1623163000000L;
                    diDate = diDate - diDate2 ;
                    diUserLastActivity = diDate;

                    UploadCropedImageFunction(imageUriResultCrop);
                }
            }
        });
    }




    private int diUserType = 1;
    private String dUserName = "NO";

    //Uplaoding Photo to FireStorage
    private void UploadCropedImageFunction(Uri filePath) {
        if(filePath != null){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();;
            if(user != null){
                dUserUID = FirebaseAuth.getInstance().getUid();
                dUserEmail = user.getEmail() ;
                dUserName = user.getDisplayName();
            }else{
                dUserUID = "NO";
                dUserEmail = "NO";
                dUserName = "NO";
            }

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            //System.currentTimeMills();
            ref = storageReference.child("QuizMate/Users_Picture/"+dUserUID+"/"+ dUserUID +"."+getFileExtention(filePath));
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        //Photo Uploaded now get the URL
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            float dProPicServerSize = taskSnapshot.getTotalByteCount() /1024 ;
                            diSize = (int)dProPicServerSize;
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String dPhotoURL = uri.toString();

                                    //Setting PhotoURL for
                                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                            .setPhotoUri(uri)
                                            .build();
                                    FirebaseAuth.getInstance().getCurrentUser().updateProfile(userProfileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(),"Photo URL Attached",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),"Photo URL Attach Failed!",Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                    //Toast.makeText(getApplicationContext(), "Photo Uploaded", Toast.LENGTH_SHORT).show();
                                    String dTotal = "0";

                                    Map<String, Object> note = new HashMap<>();
                                    note.put("name", dUserName);
                                    note.put("email", dUserEmail); //map is done
                                    note.put("uid",dUserUID);
                                    note.put("university","NO");
                                    note.put("photoURL",dPhotoURL);
                                    note.put("phone_no","00");
                                    note.put("userType",dUserType);
                                    note.put("total",dTotal);       //String
                                    note.put("points",0);   //intger coins        new
                                    note.put("viewed",0);   //integer
                                    note.put("quiz_played",0);  //integer


                                    db.collection("QuizMate").document("All_USER")
                                            .collection("Reg_USER").document(dUserUID).set(note)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(),"Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                    mUserInfoUpdateBtn.setText("UPDATED");
                                                    finish();
                                                    Intent intent = new Intent(LoginRegistration.this, MainActivity.class);
                                                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                    startActivity(intent);

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),"Failed Please Try Again", Toast.LENGTH_SHORT).show();
                                            mUserInfoUpdateBtn.setText("FAILED Information Sent");
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
                            mUserInfoUpdateBtn.setText("Failed Photo Upload");
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    //Dont forget to add class code on MainfestXml
    @Override   //Selecting Image
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAGO, "onActivityResult:onActivityResult() Start");
        if(requestCode == CODE_IMG_GALLERY && resultCode == RESULT_OK &&  data.getData() != null && data != null){
            //Photo Successfully Selected
            Log.d(TAGO, "onActivityResult: requestCode true");
            imageUri_storage = data.getData();
            String dFileSize = getSize(imageUri_storage);       //GETTING IMAGE FILE SIZE
            double  dFileSizeDouble = Double.parseDouble(dFileSize);
            int dMB = 1000;
            dFileSizeDouble =  dFileSizeDouble/dMB;
            //dFileSizeDouble =  dFileSizeDouble/dMB;

            if(dFileSizeDouble <= 5000){
                Log.d(TAGO, "onActivityResult: File size Ok true");
                Picasso.get().load(imageUri_storage).resize(200, 200).centerCrop().into(mUserProfilePic);
                Toast.makeText(getApplicationContext(),"Selected",Toast.LENGTH_SHORT).show();
                //startCrop(imageUri_storage);

                imageUriResultCrop = imageUri_storage;
                Picasso.get().load(imageUriResultCrop).into(mUserProfilePic);

            }else{
                Toast.makeText(this, "Failed! (File is Larger Than 5MB)",Toast.LENGTH_SHORT).show();
            }


        }else{
            Log.d(TAGO, "onActivityResult: File size Ok true");
            Toast.makeText(this, " requestCode Error",Toast.LENGTH_SHORT).show();
        }
    }
    //Croping Function
    Random random = new Random();
    private String getFileExtention(Uri uri){   //IMAGE
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        //Not worked in Croped File so i constant it
        return "JPEG";
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
/*
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
    }*/

}