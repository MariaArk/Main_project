package com.example.e_commerce;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.e_commerce.model.Order;

public class CoursePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);

        ConstraintLayout courseBg = findViewById(R.id.CoursePageBg);
        ImageView  courseImage = findViewById(R.id.CoursePageImage);
        TextView courseTitle = findViewById(R.id.CoursePageTitle);
        TextView courseLevel = findViewById(R.id.coursePageLevel);
        TextView coursePrice = findViewById(R.id.coursePagePrice);
        TextView courseText = findViewById(R.id.CoursePageText);

        courseBg.setBackgroundColor(getIntent().getIntExtra("courseBg",0));
        courseImage.setImageResource(getIntent().getIntExtra("courseImage",0));
        courseTitle.setText(getIntent().getStringExtra("courseTitle"));
        courseLevel.setText(getIntent().getStringExtra("courseLevel"));
        coursePrice.setText(getIntent().getStringExtra("coursePrice"));
        courseText.setText(getIntent().getStringExtra("courseText"));
    }

    public void addToCart(View view){
        int item_id = getIntent().getIntExtra("courseId",0);
        Order.itemsId.add(item_id);
        Toast.makeText(this,"Добавлено", Toast.LENGTH_LONG).show();

    }
}