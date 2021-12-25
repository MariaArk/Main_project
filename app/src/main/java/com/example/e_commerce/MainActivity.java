package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.e_commerce.adapter.CategoryAdapter;
import com.example.e_commerce.adapter.CourseAdapter;
import com.example.e_commerce.model.Category;
import com.example.e_commerce.model.Course;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    RecyclerView categoryRecycler, courseRecycler;
    CategoryAdapter categoryAdapter;
    static CourseAdapter courseAdapter;
    static List<Course> courseList = new ArrayList<>();
    static List<Course> fullCourseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1,"Хобби"));
        categoryList.add(new Category(2,"Сайты"));
        categoryList.add(new Category(3,"Языки"));
        categoryList.add(new Category(4,"Прочее"));

        setCategoryRecycler(categoryList);


        courseList.add(new Course(1,"java", "Профессия Java\nразработчик", "10 000","Начальный","#424345", "Программа обучения Джава – рассчитана на новичков в данной сфере.За программу вы сможете изучить основы Java,изучите разработку веб сайтов на основе Java , изучите построение полноценных Андроид приложений.",3));
        courseList.add(new Course(2,"python_3", "Профессия Python\nразработчик","8 500","Сложный","#9FA52D", "Получите одну из самых востребованных IT-профессий. Вы освоите Python, научитесь писать программы и веб-приложения. Реализуете 7 проектов для портфолио, а мы дадим гарантию трудоустройства.",3));
        courseList.add(new Course(3,"_05197", "Курсы по Nails\nдизайну","15 000","Начальный","#7B68EE", "Данные курсы позволят вам расширить свои представления о возможностях в сфере Красоты.Вы познакомитесь с правилами ухода за кожей рук и ног, санитарно-гигиеническими нормами работы специалиста по маникюру.",4));
        courseList.add(new Course(4,"_47490", "Курсы Baristo\nдля начинающих","7 300","Начальный","#FF0000", "В данном курсе Вы получите полное представление о профессии и обучитесь базовым навыкам, которые должен уметь любой бариста. В этот перечень входят знания о кофе, как о продукте, сортов, обработки, обжарки.Управление эспрессо-машиной.",4));
        courseList.add(new Course(5,"penguin", "Курсы Рисования\nдля всех возрастов","6 500","Начальный","#FFDAB9", "На наших курсах вы сразу начнете рисовать и осваивать техники пастели, акварели,маркеров,узнаете все секреты рисования и построения,нарисуете законченные картины,сможете самостоятельно развиваться и рисовать любые сложные работы.",1));

        fullCourseList.addAll(courseList);

        setCourseRecycler(courseList);
    }

    public void openShoppingCart(View view){
        Intent intent = new Intent(this, OrderPage.class);
        startActivity(intent);
    }

    private void setCourseRecycler(List<Course> courseList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        courseRecycler = findViewById(R.id.courseRecycler);
        courseRecycler.setLayoutManager(layoutManager);

        courseAdapter = new CourseAdapter(this,courseList);
        courseRecycler.setAdapter(courseAdapter);
    }

    private void setCategoryRecycler(List<Category> categoryList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        categoryRecycler = findViewById(R.id.categoryRecycler);
        categoryRecycler.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(this,categoryList);
        categoryRecycler.setAdapter(categoryAdapter);
    }
    public static void showCoursesByCategory(int category){

        courseList.clear();
        courseList.addAll(fullCourseList);

        List<Course> filterCourses = new ArrayList<>();

        for (Course c:courseList){
            if(c.getCategory() == category) filterCourses.add(c);
        }

        courseList.clear();
        courseList.addAll(filterCourses);

        courseAdapter.notifyDataSetChanged();

    }
}