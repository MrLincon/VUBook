package com.example.vubook;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import static com.example.vubook.Tab_Layout_Files.Fragment_CR.EXTRA_CONTACT_CR;
import static com.example.vubook.Tab_Layout_Files.Fragment_CR.EXTRA_DEPARTMENT_CR;
import static com.example.vubook.Tab_Layout_Files.Fragment_CR.EXTRA_EMAIL_CR;
import static com.example.vubook.Tab_Layout_Files.Fragment_CR.EXTRA_ID_CR;
import static com.example.vubook.Tab_Layout_Files.Fragment_CR.EXTRA_IMAGE_URL_CR;
import static com.example.vubook.Tab_Layout_Files.Fragment_CR.EXTRA_NAME_CR;
import static com.example.vubook.Tab_Layout_Files.Fragment_CR.EXTRA_SECTION_CR;
import static com.example.vubook.Tab_Layout_Files.Fragment_CR.EXTRA_SEMESTER_CR;
import static com.example.vubook.Tab_Layout_Files.Fragment_Teacher.EXTRA_CONTACT;
import static com.example.vubook.Tab_Layout_Files.Fragment_Teacher.EXTRA_COUNSELING_HOUR;
import static com.example.vubook.Tab_Layout_Files.Fragment_Teacher.EXTRA_DEPARTMENT;
import static com.example.vubook.Tab_Layout_Files.Fragment_Teacher.EXTRA_DESIGNATION;
import static com.example.vubook.Tab_Layout_Files.Fragment_Teacher.EXTRA_EMAIL;
import static com.example.vubook.Tab_Layout_Files.Fragment_Teacher.EXTRA_IMAGE_URL;
import static com.example.vubook.Tab_Layout_Files.Fragment_Teacher.EXTRA_NAME;
import static com.example.vubook.Tab_Layout_Files.Fragment_Teacher.EXTRA_OFFICE;

public class DetailsActivityCR extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;

    private TextView user_name,user_email,user_id,user_department,user_semester,user_section,user_contact;
    private CircularImageView circularImageView;

    ProgressBar progressBar;
    ImageView copy;
    CardView call;
    CardView mail;

    ClipboardManager clipboardManager;
    ClipData clipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cr_details);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        user_name = findViewById(R.id.name);
        user_email = findViewById(R.id.email);
        user_id = findViewById(R.id.cr_id);
        user_department = findViewById(R.id.department);
        user_semester = findViewById(R.id.semester);
        user_section = findViewById(R.id.section);
        user_contact = findViewById(R.id.contact);
        progressBar = findViewById(R.id.progress_loading);
        circularImageView = findViewById(R.id.user_image);

//        copy = findViewById(R.id.copy);
        call = findViewById(R.id.call);
        mail = findViewById(R.id.send_email);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Details");

        final Intent intent = getIntent();

        String imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL_CR);
        String name = intent.getStringExtra(EXTRA_NAME_CR);
        final String email = intent.getStringExtra(EXTRA_EMAIL_CR);
        String id = intent.getStringExtra(EXTRA_ID_CR);
        String department = intent.getStringExtra(EXTRA_DEPARTMENT_CR);
        String semester = intent.getStringExtra(EXTRA_SEMESTER_CR);
        String section = intent.getStringExtra(EXTRA_SECTION_CR);
        final String contact = intent.getStringExtra(EXTRA_CONTACT_CR);

        Picasso.get().load(imageUrl).error(R.drawable.user_default).into(circularImageView);
        user_name.setText(name);
        user_email.setText(email);
        user_id.setText(id);
        user_department.setText(department);
        user_semester.setText(semester);
        user_section.setText(section);
        user_contact.setText(contact);

        clipboardManager = (ClipboardManager) DetailsActivityCR.this.getSystemService(Context.CLIPBOARD_SERVICE);

//        copy.setVisibility(View.GONE);

//        copy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                clipData = ClipData.newPlainText("contact",Contact);
//                clipboardManager.setPrimaryClip(clipData);
//
//                Toast.makeText(DetailsActivityTeacher.this,"copied: "+Contact, Toast.LENGTH_SHORT).show();
//            }
//        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+contact));

                if (ActivityCompat.checkSelfPermission(DetailsActivityCR.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //request permission from user if the app hasn't got the required permission
                    ActivityCompat.requestPermissions(DetailsActivityCR.this,
                            new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                            10);
                    return;
                }else {     //have got permission
                    try{
                        startActivity(callIntent);  //call activity and make phone call
                    }
                    catch (ActivityNotFoundException activityException) {
                        Log.e("Calling a Phone Number", "Call failed", activityException);
                    }
                }

            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedback = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + email));

                try {
                    startActivity(Intent.createChooser(feedback, "Choose an e-mail client"));
                    finish();
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(DetailsActivityCR.this, "There is no e-mail clint installed!", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}
