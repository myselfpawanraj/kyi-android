package com.app.knowyourism.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.knowyourism.Api.ResultApi;
import com.app.knowyourism.Model.Result;
import com.app.knowyourism.Model.Student;
import com.app.knowyourism.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    List< Student > studentList;
    Student student;
    TextView textviewname, textviewbranch, textviewplace, textviewadmnno, textViewfacebook;
    ImageView imageView;
    final String MALE = "https://image.freepik.com/free-vector/vector-avatar-smiling-man-facial-expression_102172-203.jpg";
    final String FEMALE = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTZA_wIwT-DV4G3E3jdNZScRLQnH4faqTH2a7PrNwlhqP4W1Zjh&usqp=CAU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textviewname = findViewById(R.id.textviewname);
        textviewadmnno = findViewById(R.id.textviewadmnno);
//        textviewplace = findViewById(R.id.textViewPlace);
        textviewbranch = findViewById(R.id.textviewBranch);
//        textViewfacebook = findViewById(R.id.facebook);
        imageView = findViewById(R.id.imageView4);

        getData();
    }

    private void getData() {
        student = (Student) getIntent().getSerializableExtra("student");
        if (student == null) return;
        textviewname.setText(student.getName());
        textviewname.setText(student.getName());
        textviewadmnno.setText(student.getAdmissionNo());
//        textViewfacebook.setText("@ " + student.getFacebook());
//        textviewplace.setText("Dhanbad" + ", " + "Jharkhand");

        String branch = student.getCourse() + " " + student.getDepartment();
        if (branch.equals(" ")) branch = "Not Available :(";
        textviewbranch.setText(branch);
//        textviewplace.setText((student.getCity().isEmpty() ? "NA" : student.getCity()) + ", " + (student.getState().isEmpty() ? "NA" : student.getState()));

        if (!student.getPhotoURL().isEmpty()) {
            Picasso picasso = new Picasso.Builder(ProfileActivity.this)
                    .listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        }
                    })
                    .build();
            picasso.load(student.getPhotoURL())
                    .fit()
                    .into(imageView);
        } else {
            Picasso.get().load((student.getSex().equals("M") ? MALE : FEMALE)).into(imageView);
        }
    }
}