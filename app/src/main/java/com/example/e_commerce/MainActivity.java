package com.example.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.adapter.CategoryAdapter;
import com.example.e_commerce.adapter.CourseAdapter;
import com.example.e_commerce.model.Category;
import com.example.e_commerce.model.Course;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Инициализируем все нужные нам списки и переменные
    RecyclerView categoryRecycler, courseRecycler;
    CategoryAdapter categoryAdapter;
    static CourseAdapter courseAdapter;
    static List<Course> courseList = new ArrayList<>();
    static List<Course> fullCourseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Генерируем список наших категорий
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1,"Творчество"));
        categoryList.add(new Category(2,"Сайты"));
        categoryList.add(new Category(3,"Языки"));
        categoryList.add(new Category(4,"Прочее"));

        setCategoryRecycler(categoryList);

        // Генерируем список наших курсов
        courseList.add(new Course(1,"java", "Профессия Java\nразработчик", "1000","Начальный","#424345", "Программа обучения Джава – рассчитана на новичков в данной сфере.За программу вы сможите изучить основы Java,изучите разработку веб сайтов на основе Java , изучите построение полноценных Андроид приложений.",3));
        courseList.add(new Course(2,"python_3", "Профессия Python\nразработчик","900","Сложный","#9FA52D", "Получите одну из самых востребованных IT-профессий. Вы освоите Python, научитесь писать программы и веб-приложения. Реализуете 7 проектов для портфолио, а мы дадим гарантию трудоустройства.",3));
        courseList.add(new Course(3,"_05197", "Курсы по Nails\nдизайну","800","Начальный","#7B68EE", "Данные курсы позволят вам расширить свои представления о возможностях в сфере Красоты.Вы познакомитесь с правилами ухода за кожей рук и ног, санитарно-гигиеническими нормами работы специалиста по маникюру.",4));
        courseList.add(new Course(4,"_47490", "Курсы Baristo\nдля начинающих","700","Начальный","#FF0000", "В данном курсе Вы получите полное представление о профессии и обучитесь базовым навыкам, которые должен уметь любой бариста. В этот перечень входят знания о кофе, как о продукте, сортов, обработки, обжарки.Управление эспрессо-машиной.",4));
        courseList.add(new Course(5,"penguin", "Курсы Рисования\nдля всех возрастов","600","Начальный","#FFDAB9", "На наших курсах вы сразу начнете рисовать и осваивать техники пастели, акварели,маркеров,узнаете все секреты рисования и построения,нарисуете законченные картины,сможете самостоятельно развиваться и рисовать любые сложные работы.",1));
        courseList.add(new Course(6,"dolphin", "Введение в \n веб-разработку","7 300","Начальный","#0066FF", "Введение. Обучение HTML, CSS, Хостинг, Backend-разработка, Frontend-разработка. Данный курс поможет вам полностью разобраться, что же из себя представляет веб-разработка, а также даст полные, базовые знания в этом направлении.",2));
        courseList.add(new Course(7,"teclado", "Основы в \n CSS, HTML, JS","6 500","Начальный","#000033", "Данный курс позволит вам разобраться, что из себя представляет HTML и CSS. Мы разберем Базовые CSS-свойства и научимся создавать интерактивные сайты при помощи JavaScript.",2));


        fullCourseList.addAll(courseList);

        setCourseRecycler(courseList);
    }
    // Переход в корзину, где будут лежать все добавленные курсы.
    public void openShoppingCart(View view){
        Intent intent = new Intent(this, OrderPage.class);
        startActivity(intent);
    }

    // Создается лента со всеми курсами, которые есть в нвшем приложении
    private void setCourseRecycler(List<Course> courseList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        courseRecycler = findViewById(R.id.courseRecycler);
        courseRecycler.setLayoutManager(layoutManager);

        courseAdapter = new CourseAdapter(this,courseList);
        courseRecycler.setAdapter(courseAdapter);
    }
    // Создается лента со всеми категориями, которые есть в нвшем приложении
    private void setCategoryRecycler(List<Category> categoryList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        categoryRecycler = findViewById(R.id.categoryRecycler);
        categoryRecycler.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(this,categoryList);
        categoryRecycler.setAdapter(categoryAdapter);
    }
    // Метод, который показывает, какие есть крусы в определенной категории
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
//   В данных  методах мы осуществляем переходы на страницы, которые расположены на боковой панеле
    public void openAbout_us(View view){
        Intent intent = new Intent(this, About_usPage.class);
        startActivity(intent);
    }
    public void openContact(View view){
        Intent intent = new Intent(this, ContactPage.class);
        startActivity(intent);
    }

}