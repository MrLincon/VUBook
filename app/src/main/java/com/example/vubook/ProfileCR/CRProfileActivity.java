package com.example.vubook.ProfileCR;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.vubook.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class CRProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;

    private TextView user_name,user_email,user_id,user_department,user_semester,user_section,user_contact;
    private CircularImageView circularImageView;
    private FloatingActionButton edit;

    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private String userID;

    private FirebaseFirestore db;
    private DocumentReference document_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cr_profile);

        toolbar = findViewById(R.id.profile_toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Profile");

        user_name = findViewById(R.id.name);
        user_email = findViewById(R.id.email);
        user_department = findViewById(R.id.department);
        user_semester = findViewById(R.id.semester);
        user_section = findViewById(R.id.section);
        user_id = findViewById(R.id.cr_id);
        user_contact = findViewById(R.id.contact);
        progressBar = findViewById(R.id.progress_loading);
        circularImageView = findViewById(R.id.user_image);

        edit = findViewById(R.id.edit_profile);
        progressBar = findViewById(R.id.progress_loading);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();

        db = FirebaseFirestore.getInstance();
        document_reference = db.collection("CRDetails").document(userID);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfile = new Intent(CRProfileActivity.this, EditCrProfileActivity.class);
                startActivity(editProfile);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        loadData();
    }

    public void loadData(){

        document_reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    progressBar.setVisibility(View.VISIBLE);

                    String name = documentSnapshot.getString("name");
                    String email = documentSnapshot.getString("email");
                    String id = documentSnapshot.getString("cr_id");
                    String department = documentSnapshot.getString("department");
                    String semester = documentSnapshot.getString("semester");
                    String section = documentSnapshot.getString("section");
                    String contact = documentSnapshot.getString("contact");
                    String url = documentSnapshot.getString("thumbImageUrl");

                    Picasso.get().load(url).error(R.drawable.user_default).into(circularImageView);
                    user_name.setText(name);
                    user_email.setText(email);
                    user_id.setText(id);
                    user_department.setText(department);
                    user_semester.setText(semester);
                    user_section.setText(section);
                    user_contact.setText(contact);

                } else {
                    Toast.makeText(CRProfileActivity.this, "Information does not exist!", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Picasso.get().load(R.drawable.user_default).into(circularImageView);
            }
        });

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_edit_teacher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.edit:
//                Intent editProfile = new Intent(TeacherProfileActivity.this,EditTeacherProfileActivity.class);
//                startActivity(editProfile);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                return true;
            case R.id.delete_profile:
                openDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CRProfileActivity.this);
        builder.setTitle("Are you sure?")
                .setMessage("If you delete this profile, it  will no longer be available in CR's list!")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        document_reference.delete();
                        Picasso.get().load(R.drawable.user_default).into(circularImageView);
                        user_name.setText("");
                        user_email.setText("");
                        user_id.setText("");
                        user_department.setText("");
                        user_semester.setText("");
                        user_section.setText("");
                        user_contact.setText("");

                        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
