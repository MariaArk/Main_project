package com.example.e_commerce.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.CoursePage;
import com.example.e_commerce.R;
import com.example.e_commerce.model.Course;

import java.util.List;
// создаем адаптер, все наследуем от класса RecyclerView
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.courseViewHolder> {
    // создаем новое поле класс Context и список на основе модели курсов
    Context context;
    List<Course> courses;

    @NonNull
    @Override
    // в данном методе указываем какой конкретно дизайн мы будем использовать для отображения каждого элемента
    public courseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View courseItem = LayoutInflater.from(context).inflate(R.layout.course_item, parent, false);
        return new courseViewHolder(courseItem);
    }

    public CourseAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
    }
    // указали, что конкретно подставили в сам дизайн
    @Override
    public void onBindViewHolder(@NonNull courseViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.courseBg.setCardBackgroundColor(Color.parseColor(courses.get(position).getColor()));

        int ImageId = context.getResources().getIdentifier("ic_"+courses.get(position).getImg(), "drawable",context.getPackageName());
        holder.courseImage.setImageResource(ImageId);
        holder.courseTitle.setText(courses.get(position).getTitle());
        holder.coursePrice.setText(courses.get(position).getPrice());
        holder.courseLevel.setText(courses.get(position).getLevel());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CoursePage.class);

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                        new Pair<View, String>(holder.courseImage, "courseImage"));

                intent.putExtra("courseBg", Color.parseColor(courses.get(position).getColor()));
                intent.putExtra("courseImage", ImageId);
                intent.putExtra("courseTitle", courses.get(position).getTitle());
                intent.putExtra("coursePrice", courses.get(position).getPrice());
                intent.putExtra("courseLevel", courses.get(position).getLevel());
                intent.putExtra("courseText", courses.get(position).getText());
                intent.putExtra("courseId", courses.get(position).getId());


                context.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    // возвращаем размерность нашего списка
    public int getItemCount() {
        return courses.size();
    }
    // в нашем вложенном классе указали с какими элементами в дизайне мы с вами работаем
    public static final class courseViewHolder extends RecyclerView.ViewHolder{

        CardView courseBg;
        ImageView courseImage;
        TextView courseTitle, coursePrice, courseLevel;
        // создали конструктор
        public courseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseBg = itemView.findViewById(R.id.courseBg);
            courseImage = itemView.findViewById(R.id.courseImage);
            courseTitle = itemView.findViewById(R.id.courseTitle);
            coursePrice = itemView.findViewById(R.id.coursePrice);
            courseLevel = itemView.findViewById(R.id.courseLevel);
        }
    }




}
