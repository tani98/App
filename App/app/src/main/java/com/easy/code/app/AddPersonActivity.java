package com.easy.code.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class AddPersonActivity extends AppCompatActivity {

    ActionBar actionBar;
    private Button btnPhoto;
    private ImageView imgPerson;
    private Spinner spProfession;
    private AutoCompleteTextView txtName;
    private AutoCompleteTextView txtAge;
    private ViewPager viewPager;
    private String name;
    private int age;
    private String profession;
    public List<String> phones;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initComponent();
        setRecycle();
        fillSpinner();
        actionTakePicture();
        actionSpinner();
    }

    private void initComponent(){
        btnPhoto = (Button) findViewById(R.id.btnPhoto);
        imgPerson = (ImageView) findViewById(R.id.imgPerson);
        spProfession = (Spinner) findViewById(R.id.spProfession);
        txtName = (AutoCompleteTextView) findViewById(R.id.txtName);
        txtAge = (AutoCompleteTextView) findViewById(R.id.txtAge);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

    }

    private void fillSpinner(){
        try{
            ArrayAdapter<CharSequence> adapterProfession =
                    ArrayAdapter.createFromResource(this,
                            R.array.profession,
                            android.R.layout.simple_selectable_list_item);

            spProfession.setAdapter(adapterProfession);

        }catch (NullPointerException ex){
            Log.e("Fill",ex.getMessage());
        }
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            case R.id.action_OK:
                savePerson();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void actionTakePicture(){
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, 3434);

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 3434){
            super.onActivityResult(requestCode, resultCode, data);

            Bitmap image = (Bitmap) data.getExtras().get("data");

            imgPerson.setImageBitmap(image);

        }
    }

    private void actionSpinner(){
        spProfession.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        profession = (String) parent.getItemAtPosition(position);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
    }

    private void savePerson(){
        if(txtName.getText().toString().isEmpty())
            txtName.setError("Ingrese el nombre");
        if(profession.equals("----------------"))
            Toast.makeText(getApplicationContext(), "Seleccione una profesión", Toast.LENGTH_SHORT).show();
        if(txtAge.getText().toString().isEmpty())
            txtAge.setError("Ingrese la edad");
        else if(Integer.parseInt(txtAge.getText().toString())<18 || Integer.parseInt(txtAge.getText().toString())>100 )
            txtAge.setError("Ingrese una edad valida");
    }

    private void setRecycle(){
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view.equals(object);
            }
            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                View view = LayoutInflater.from(
                        getBaseContext()).inflate(R.layout.list_phone, null, false);


                final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvPhone);
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

    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder>{

        public int size =1;
        public String phone;

        @Override
        public RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_phone, parent, false);
            return new RecycleAdapter.ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(RecycleAdapter.ViewHolder holder, int position) {
            try{
                if(position==0){
                   holder.btnDelete.setVisibility(View.INVISIBLE);
                }else{
                }
                phone = holder.txtPhone.getText().toString();


            }catch (Exception ex){
                Log.e("Recycler",ex.getMessage());
            }

        }

        @Override
        public int getItemCount() {
            return size;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public Button btnAdd;
            public Button btnDelete;
            public AutoCompleteTextView txtPhone;


            public ViewHolder(final View itemView) {
                super(itemView);
                txtPhone = (AutoCompleteTextView) itemView.findViewById(R.id.txtPhone);
                btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
                btnDelete = (Button) itemView.findViewById(R.id.btnDelete);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            if(txtPhone.getText().toString().isEmpty())
                                txtPhone.setError("Ingrese el número de teléfono");
                            else{
                                size++;
                            }
                        }catch (Exception ex){
                            Log.e("Agregar",ex.getMessage());
                        }


                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        size--;
                    }
                });
            }


        }
    }




}
