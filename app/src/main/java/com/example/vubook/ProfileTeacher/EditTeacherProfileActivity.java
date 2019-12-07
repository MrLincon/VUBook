package com.example.vubook.ProfileTeacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.vubook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class EditTeacherProfileActivity extends AppCompatActivity {

    private static final String TAG = EditTeacherProfileActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView toolbarTitle;
    ProgressBar progressBar;

    Spinner department_spinner;

    EditText user_name, user_email, user_designation, user_office, user_counseling_hour, user_contact;
    TextView add_image;

    CircularImageView imageView;

    private FirebaseAuth mAuth;
    private String userID;
    private String image_link,thumb_image_link;


    private FirebaseFirestore db;

    private StorageReference mStorageRef;
    private StorageReference mStorageRefThumb;
    private DocumentReference document_reference;
    private DocumentReference document_ref;
    private DocumentReference doc_ref;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;
    private Uri resultUri;
    private Uri thumbResultUri;

    public String user_department;

    Bitmap bitmap;
    Bitmap thumbBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_teacher_profile);

        toolbar = findViewById(R.id.edit_profile_toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Edit Profile");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        progressBar = findViewById(R.id.progress_loading);
        add_image = findViewById(R.id.tv_add_image);
        user_name = findViewById(R.id.name);
        user_email = findViewById(R.id.email);
        user_designation = findViewById(R.id.designation);
        user_office = findViewById(R.id.office);
        user_counseling_hour = findViewById(R.id.counseling_hour);
        user_contact = findViewById(R.id.contact);
        imageView = findViewById(R.id.add_image);

        department_spinner = findViewById(R.id.department);
        final ArrayAdapter<CharSequence> departmentAdapted = ArrayAdapter.createFromResource(this,
                R.array.department, android.R.layout.simple_spinner_item);
        departmentAdapted.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department_spinner.setAdapter(departmentAdapted);
        department_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_department = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImage();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImage();
            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference().child("TeacherProfile");
        mStorageRefThumb = FirebaseStorage.getInstance().getReference().child("TeacherProfileThumbnails");

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        userID = mAuth.getUid();

        progressBar.setVisibility(View.GONE);

//
        document_reference = db.collection("TeacherDetails").document(userID);

        document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    progressBar.setVisibility(View.VISIBLE);


                    String name = documentSnapshot.getString("name");
                    String email = documentSnapshot.getString("email");
                    String designation = documentSnapshot.getString("designation");
                    String department = documentSnapshot.getString("department");
                    String office = documentSnapshot.getString("office");
                    String counseling_hour = documentSnapshot.getString("counseling_hour");
                    String contact = documentSnapshot.getString("contact");
                    String url = documentSnapshot.getString("thumbImageUrl");

                    user_name.setText(name);
                    user_email.setText(email);
                    user_designation.setText(designation);
                    user_office.setText(office);
                    user_counseling_hour.setText(counseling_hour);
                    user_contact.setText(contact);
                    Picasso.get().load(url).error(R.drawable.add_user_pic).into(imageView);

                    if (department != null) {
                        int spinnerPosition = departmentAdapted.getPosition(department);
                        department_spinner.setSelection(spinnerPosition);
                    }
                } else {
                    Toast.makeText(EditTeacherProfileActivity.this, "Problem loading user image!", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        progressBar.setVisibility(View.GONE);


    }

    private void userImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Log.d(TAG, "onActivityResult: inside Activity Result");

            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(EditTeacherProfileActivity.this);
        }

//            CropImage.ActivityResult result = null;

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                Log.d(TAG, "onActivityResult: Crop Image");

                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {

                    resultUri = result.getUri();
                    thumbResultUri = result.getUri();

                    Picasso.get().load(resultUri).error(R.drawable.add_user_pic).into(imageView);

                    File compressed = new File(resultUri.getPath());
                    File compressedThumb = new File(thumbResultUri.getPath());

                    try {
                        bitmap = new Compressor(EditTeacherProfileActivity.this)
                                .setQuality(100)
                                .compressToBitmap(compressed);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
                    final byte[] image_byte = byteArrayOutputStream.toByteArray();

                    try {
                        thumbBitmap = new Compressor(EditTeacherProfileActivity.this)
                                .setQuality(30)
                                .compressToBitmap(compressedThumb);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream thumbByteArrayOutputStream = new ByteArrayOutputStream();
                    thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 15, thumbByteArrayOutputStream);
                    final byte[] thumb_image_byte = thumbByteArrayOutputStream.toByteArray();

//                    long time= System.currentTimeMillis();


                    final StorageReference imageName = mStorageRef.child(userID);
                    final StorageReference thumbImageName = mStorageRefThumb.child(userID);

                    final ProgressDialog progressDialog = new ProgressDialog(EditTeacherProfileActivity.this);
                    progressDialog.setTitle("Uploading Photo");
                    progressDialog.setMessage("Please wait a few seconds!");
                    progressDialog.show();

                    UploadTask uploadTask = imageName.putBytes(image_byte);
                    UploadTask uploadTaskThumb = thumbImageName.putBytes(thumb_image_byte);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(EditTeacherProfileActivity.this, "Uploaded", Toast.LENGTH_LONG).show();

                            imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    image_link = uri.toString();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    doc_ref = db.collection("TeacherDetails").document(userID);

                                    doc_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                if (documentSnapshot != null && documentSnapshot.exists()) {
                                                    doc_ref.update("imageUrl", image_link).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            progressDialog.cancel();
                                                            Toast.makeText(EditTeacherProfileActivity.this, "Profile picture uploaded!", Toast.LENGTH_SHORT).show();

                                                            Intent save_img = new Intent(EditTeacherProfileActivity.this, TeacherProfileActivity.class);
                                                            startActivity(save_img);
                                                            finish();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.cancel();
                                                            Toast.makeText(EditTeacherProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                                                } else {
                                                    Map<String, String> userMapImg = new HashMap<>();

                                                    userMapImg.put("imageUrl", image_link);

                                                    doc_ref.set(userMapImg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            progressDialog.cancel();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.cancel();
                                                            Toast.makeText(EditTeacherProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });




                    //Uploading Thumbnails

                    uploadTaskThumb.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(EditTeacherProfileActivity.this, "Uploaded", Toast.LENGTH_LONG).show();

                            thumbImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    thumb_image_link = uri.toString();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    doc_ref = db.collection("TeacherDetails").document(userID);

                                    doc_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                if (documentSnapshot != null && documentSnapshot.exists()) {
                                                    doc_ref.update("thumbImageUrl", thumb_image_link).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(EditTeacherProfileActivity.this, "Profile thumb picture uploaded!", Toast.LENGTH_SHORT).show();

                                                            Intent save_img = new Intent(EditTeacherProfileActivity.this, TeacherProfileActivity.class);
                                                            startActivity(save_img);
                                                            finish();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(EditTeacherProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                                                } else {
                                                    Map<String, String> userMapThumbImg = new HashMap<>();

                                                    userMapThumbImg.put("thumbImageUrl", thumb_image_link);

                                                    doc_ref.set(userMapThumbImg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(EditTeacherProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
//                Exception error = croppedImage.getError();
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            }

    }


    private void updateDetails() {

        final String name = user_name.getText().toString().trim();
        final String email = user_email.getText().toString().trim();
        final String designation = user_designation.getText().toString();
        final String department = user_department;
        final String office = user_office.getText().toString().trim();
        final String counseling_hour = user_counseling_hour.getText().toString().trim();
        final String contact = user_contact.getText().toString().trim();


        if (name.isEmpty()) {
            user_name.setError("Field must be filled");
            return;
        }
        if (email.isEmpty()) {
            user_email.setError("Field must be filled");
            return;
        }
        if (designation.isEmpty()) {
            user_designation.setError("Field must be filled");
            return;
        }if (office.isEmpty()) {
            user_office.setError("Field must be filled");
            return;
        }if (counseling_hour.isEmpty()) {
            user_counseling_hour.setError("Field must be filled");
            return;
        }
        if (contact.isEmpty()) {
            user_contact.setError("Field must be filled");
            return;

        } else {

            ProgressDialog progressDialog = new ProgressDialog(EditTeacherProfileActivity.this);
            progressDialog.setTitle("Updating");
            progressDialog.setMessage("Please wait a few seconds!");
            progressDialog.show();


            document_ref = db.collection("TeacherDetails").document(userID);

            document_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot != null && documentSnapshot.exists()) {

                            document_ref.update("name", name,
                                    "email", email,
                                    "designation", designation,
                                    "department", department,
                                    "office", office,
                                    "counseling_hour", counseling_hour,
                                    "contact", contact).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditTeacherProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                            Intent save = new Intent(EditTeacherProfileActivity.this, TeacherProfileActivity.class);
                            startActivity(save);
                            finish();
                        } else {
                            Map<String, String> userMap = new HashMap<>();

                            userMap.put("imageUrl", image_link);
                            userMap.put("name", name);
                            userMap.put("email", email);
                            userMap.put("designation", designation);
                            userMap.put("department", department);
                            userMap.put("office", office);
                            userMap.put("counseling_hour", counseling_hour);
                            userMap.put("contact", contact);

                            document_ref.set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditTeacherProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                            Intent save = new Intent(EditTeacherProfileActivity.this, TeacherProfileActivity.class);
                            startActivity(save);
                            finish();
                        }
                    }


                }
            });


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_save_teacher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_details:
                updateDetails();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
