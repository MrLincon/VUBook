package com.example.vubook.Tab_Layout_Files;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vubook.Adapters.Teacher;
import com.example.vubook.Adapters.TeacherAdapter;
import com.example.vubook.DetailsActivityTeacher;
import com.example.vubook.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class Fragment_Teacher extends Fragment {

    View view;
    RecyclerView recyclerView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference teacher = db.collection("TeacherDetails");

    private TeacherAdapter adapter;

    public static final String EXTRA_IMAGE_URL = "com.example.firebaseprofile.EXTRA_IMAGE_URL";
    public static final String EXTRA_NAME = "com.example.firebaseprofile.EXTRA_NAME";
    public static final String EXTRA_EMAIL = "com.example.firebaseprofile.EXTRA_EMAIL";
    public static final String EXTRA_DESIGNATION = "com.example.firebaseprofile.EXTRA_DESIGNATION";
    public static final String EXTRA_DEPARTMENT = "com.example.firebaseprofile.EXTRA_DEPARTMENT";
    public static final String EXTRA_OFFICE = "com.example.firebaseprofile.EXTRA_OFFICE";
    public static final String EXTRA_COUNSELING_HOUR = "com.example.firebaseprofile.EXTRA_COUNSELING_HOUR";
    public static final String EXTRA_CONTACT = "com.example.firebaseprofile.EXTRA_CONTACT";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teacher,container,false);

        recyclerView = view.findViewById(R.id.recyclerview);

        Query query = teacher.orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Teacher> options = new FirestoreRecyclerOptions.Builder<Teacher>()
                .setQuery(query, Teacher.class)
                .build();

        adapter = new TeacherAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        adapter.setOnItemClickListener(new TeacherAdapter.OnItemClickListener()  {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot) {
                Teacher teacher = documentSnapshot.toObject(Teacher.class);
                String id = documentSnapshot.getId();

                Intent intent = new Intent(getContext(), DetailsActivityTeacher.class);

                String imageUrl = teacher.getThumbImageUrl();
                String name = teacher.getName();
                String email = teacher.getEmail();
                String designation = teacher.getDesignation();
                String department = teacher.getDepartment();
                String office = teacher.getOffice();
                String counseling_hour = teacher.getCounseling_hour();
                String contact = teacher.getContact();

                intent.putExtra(EXTRA_IMAGE_URL,imageUrl);
                intent.putExtra(EXTRA_NAME,name);
                intent.putExtra(EXTRA_EMAIL,email);
                intent.putExtra(EXTRA_DESIGNATION,designation);
                intent.putExtra(EXTRA_DEPARTMENT,department);
                intent.putExtra(EXTRA_OFFICE,office);
                intent.putExtra(EXTRA_COUNSELING_HOUR,counseling_hour);
                intent.putExtra(EXTRA_CONTACT,contact);

                startActivity(intent);
            }
        });

        return view;
    }
}
