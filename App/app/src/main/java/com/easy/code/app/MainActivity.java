package com.easy.code.app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easy.code.app.sqllite.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private ViewPager viewPager;
    List<String> names = new ArrayList<>();
    List<Bitmap> bitmaps= new ArrayList<>();
    int size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        actionFab();
        read();
        SetView();
    }

    private void initComponent(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    public void SetView(){
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }


            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                View view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.list_person, null, false);

                    final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(
                                    getBaseContext(), LinearLayoutManager.VERTICAL, false
                            )
                    );
                    recyclerView.setAdapter(new RecycleAdapter());
                    container.addView(view);

                return view;
            }
        });
    }

    private void actionFab(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),AddPersonActivity.class);
                startActivity(intent);
            }
        });

    }
     private void read(){

         AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                 "InformaciónPersona", null, 1);
         SQLiteDatabase bd = admin.getWritableDatabase();
         Cursor c= bd.rawQuery("SELECT * FROM Persona" ,null);
         byte[] image = null;
         Bitmap bitmap = null;

         while (c.moveToNext()){
             System.out.println(c.getString(1));
             names.add(c.getString(1));

             image = c.getBlob(4);

             bitmap = BitmapFactory.decodeByteArray(image , 0, image .length);


             if (bitmap != null) {
                 bitmaps.add(bitmap);
                 System.out.println("bitmap is not null");
             }

         }

         size = c.getCount();
         bd.close();
     }



    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder>{


        @Override
        public RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_person, parent, false);
            return new RecycleAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecycleAdapter.ViewHolder holder, int position) {
            try{
                holder.txtNamePerson.setText(names.get(position));
                holder.image.setImageBitmap(bitmaps.get(position));

            }catch (IndexOutOfBoundsException ex){
                Log.e("Recycler",ex.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return size;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtNamePerson;
            ImageView image;


            public ViewHolder(View itemView) {
                super(itemView);
                txtNamePerson = (TextView) itemView.findViewById(R.id.txtNamePerson);
                image = (ImageView) itemView.findViewById(R.id.photo);
            }
        }
    }


}
