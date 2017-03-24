package com.easy.code.app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.easy.code.app.Util.Validation;
import com.easy.code.app.sqllite.AdminSQLiteOpenHelper;
import com.easy.code.app.sqllite.SQLiteReferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class AddPersonActivity extends AppCompatActivity {

    ActionBar actionBar;
    private Button btnPhoto;
    private ImageView imgPerson;
    private Spinner spProfession;
    private AutoCompleteTextView txtName;
    private AutoCompleteTextView txtAge;
    private ViewPager viewPagerPhone;
    private ViewPager viewPagerAddress;
    private String name;
    private int age;
    private String profession;
    public List<String> phones;
    byte[] byteArray;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initComponent();
        setRecyclePhone();
        setRecycleAddress();
        fillSpinner();
        actionImgTakePicture();
        actionBtnTakePicture();
        actionSpinner();
    }

    private void initComponent(){
        btnPhoto = (Button) findViewById(R.id.btnPhoto);
        imgPerson = (ImageView) findViewById(R.id.imgPerson);
        spProfession = (Spinner) findViewById(R.id.spProfession);
        txtName = (AutoCompleteTextView) findViewById(R.id.txtName);
        txtAge = (AutoCompleteTextView) findViewById(R.id.txtAge);
        viewPagerPhone = (ViewPager) findViewById(R.id.viewPagerPhone);
        viewPagerAddress = (ViewPager) findViewById(R.id.viewPagerAddress);

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

    private void actionBtnTakePicture(){
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionTakePicture();
            }
        });
    }

    private void actionImgTakePicture(){
        imgPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionTakePicture();
            }
        });
    }

    private void actionTakePicture(){
        try{
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(intent, 3434);
        }catch (Exception ex){
            Log.e("Tomar Foto",ex.getMessage());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            if(data!=null){
                super.onActivityResult(requestCode, resultCode, data);

                Bitmap image = (Bitmap) data.getExtras().get("data");

                imgPerson.setImageBitmap(image);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG,100,stream);
                byteArray = stream.toByteArray();
            }
        }catch (Exception ex){
            Log.e("Guardar foto",ex.getMessage());
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
                        Log.d("Spinner","Sin seleccion");
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
        else if(Integer.parseInt(txtAge.getText().toString())<18 || Integer.parseInt(txtAge.getText().toString())>105 )
            txtAge.setError("Ingrese una edad valida");
        else {
            writeDatabase();
        }
    }

    private void writeDatabase(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "InformaciónPersona", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        name = txtName.getText().toString();
        age = Integer.parseInt(txtAge.getText().toString());
        ContentValues values = new ContentValues();
        values.put(SQLiteReferences.TablePersonName, name);
        values.put(SQLiteReferences.TablePersonProfession, profession);
        values.put(SQLiteReferences.TablePersonAge, age);
        values.put(SQLiteReferences.TablePersonPhoto, byteArray);
        bd.insert("Persona", null, values);

        Cursor c= bd.rawQuery("SELECT * FROM "+SQLiteReferences.TablePerson+" WHERE ID = (SELECT MAX(ID) FROM "+SQLiteReferences.TablePerson+" )" ,null);
        int id;

        if(c.moveToFirst()){
            id = c.getInt(0);
            for(int i=1;i<=phones.size();i++){
                ContentValues valuesPhone = new ContentValues();
                valuesPhone.put(SQLiteReferences.TablePhoneNumber, phones.get(i));
                valuesPhone.put(SQLiteReferences.TablePhonePersonId, id);
                bd.insert(SQLiteReferences.TablePhone, null, valuesPhone);
            }
        }

        bd.close();


    }

    private void setRecyclePhone(){
        viewPagerPhone.setAdapter(new PagerAdapter() {
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

    private void setRecycleAddress(){
        viewPagerAddress.setAdapter(new PagerAdapter() {
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
                        getBaseContext()).inflate(R.layout.list_address, null, false);


                final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvAddress);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(
                                getBaseContext(), LinearLayoutManager.VERTICAL, false
                        )
                );
                recyclerView.setAdapter(new RecycleAdapterA());
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
        public void onBindViewHolder(final RecycleAdapter.ViewHolder holder, int position) {
            try{
                final Validation validation = new Validation();

                if(position==0){
                   holder.btnDelete.setVisibility(View.INVISIBLE);
                }else{
                }
                phone = holder.txtPhone.getText().toString();

                holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            if(holder.txtPhone.getText().toString().isEmpty())
                                holder.txtPhone.setError("Ingrese el número de teléfono");
                            else if(!validation.isPhone(holder.txtPhone.getText().toString()))
                                holder.txtPhone.setError("Número de teléfono invalido");
                            else{
                                size++;
                                holder.btnAdd.setEnabled(false);
                                holder.btnAdd.setBackground(getDrawable(R.drawable.ic_no_add));

                                int height = viewPagerPhone.getHeight();

                                ViewGroup.LayoutParams params = viewPagerPhone.getLayoutParams();
                                params.height = height+60;
                                viewPagerPhone.setLayoutParams(params);

                            }
                        }catch (Exception ex){
                            Log.e("Agregar",ex.getMessage());
                        }
                    }
                });

                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //size--;
                    }
                });


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
            }
        }
    }

    public class RecycleAdapterA extends RecyclerView.Adapter<RecycleAdapterA.ViewHolder> {

        public int size = 1;
        public String address;


        @Override
        public RecycleAdapterA.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_address, parent, false);
            return new RecycleAdapterA.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }


        @Override
        public int getItemCount() {
            return 0;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public Button btnAdd;
            public Button btnDelete;
            public AutoCompleteTextView txtAddress;
            public ViewHolder(View itemView) {
                super(itemView);
                txtAddress = (AutoCompleteTextView) itemView.findViewById(R.id.txtAddress);
                btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
                btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            }
        }
    }


}
