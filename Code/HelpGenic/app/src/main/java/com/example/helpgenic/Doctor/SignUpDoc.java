package com.example.helpgenic.Doctor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.ReportsHandler;
import com.example.helpgenic.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignUpDoc extends AppCompatActivity {

    //--------------------Declare All Views--------//
    String[] categories = {"Dermatologist", "Alergist", "Neurologist", "Pathologist", "Urologist", "Anesthesiologist", "Ophthalmologist"};
    Dialog dialog;
    TextView selectOption,dob;
    Button submitBtn;
    EditText name, mail, password, contact;
    RadioGroup rg;
    RadioButton male,female,yes,no;
    Button uploaddocs;
    java.sql.Date dateSelected = null;
    private byte[]  degreeImage=null;
    final Calendar myCalendar= Calendar.getInstance();
    // Initialize variable
    ActivityResultLauncher<Intent> resultLauncher;

    private FirebaseAuth mAuth;
    //---------------------------------------------//

    private static final int STORAGE_PERMISSION_CODE = 23;

    public boolean checkStoragePermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11 (R) or above
            return Environment.isExternalStorageManager();
        }else {
            //Below android 11
            int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            return read == PackageManager.PERMISSION_GRANTED ;
        }
    }

    private void requestForStoragePermissions() {
        //Android is 11 (R) or above
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }catch (Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        }else{
            //Below android 11
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    STORAGE_PERMISSION_CODE
            );
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_doc);
        //--------------------- Register all views ----------------------//
        submitBtn = findViewById(R.id.submitButtonDoc);
        selectOption = findViewById(R.id.selectCategory); // select category option
        name = findViewById(R.id.editTextTextPersonName7);
        mail = findViewById(R.id.editTextTextPersonName8);
        password = findViewById(R.id.editTextTextPassword2);
        dob = findViewById(R.id.editTextDate);
        contact = findViewById(R.id.editTextNumber);
        rg = findViewById(R.id.radioGroup);
        male=findViewById(R.id.Male);
        female=findViewById(R.id.Female);
        yes=findViewById(R.id.Yes);
        no=findViewById(R.id.No);
        uploaddocs = findViewById(R.id.uploaddocs);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        dob.setOnClickListener(view -> new DatePickerDialog(SignUpDoc.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show());
        //----------------------------------------------------------------//
        // When user presses the select category option
        selectOption.setOnClickListener(view -> {
            setUpDialogBox();
            handleDialogBoxFunctionality();
        });



        submitBtn.setOnClickListener(view -> {

            // if all credentials are filled and validated
            if (verifyCredentials()) {

                //check either male or female
                char gen = maleOrFemale();
                // is Surgeon;
                boolean Surgeon = isSurgeon();


                mAuth = FirebaseAuth.getInstance();

                Log.d("SignUpDoc", "Credentials Verified");

                mAuth.createUserWithEmailAndPassword(mail.getText().toString(), password.getText().toString()).addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        // Sign in success
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("SignUpDoc", "User Created Successfully with ID: " + user.getUid());

                        if (user != null) {
                            Log.d("SignUpDoc", "User is Signed in");
                            mAuth.signOut();
                            Log.d("SignUpDoc", "User is Logged Out");
                        }



                        // Inserting data in database
                        DbHandler db = new DbHandler();


                        db.insertDoctorDetailsInDb(user.getUid(),name.getText().toString(), dateSelected, contact.getText().toString(), selectOption.getText().toString(), gen , Surgeon, degreeImage);
                        Toast.makeText(SignUpDoc.this, "Success", Toast.LENGTH_SHORT).show();

                        finish();


                    }else{
                        Log.d("SignUpDoc", "Failure in User Creation" + task.getException());
                    }
                });


            }
        });



        // Initialize result launcher
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result)
            {
                // Initialize result data
                Intent data = result.getData();
                // check condition
                if (data != null) {

                    Uri selectedImageUri = data.getData();
                    // get bytes data from image and save it to database along with other doctor credentials

                    ReportsHandler rh = new ReportsHandler();
                    degreeImage = rh.loadImage(selectedImageUri , SignUpDoc.this);
                    Toast.makeText(SignUpDoc.this, "Upload Successful !", Toast.LENGTH_SHORT).show();


                }
            }

        } );



        uploaddocs.setOnClickListener( view -> {

            boolean t = checkStoragePermissions();
            selectImage();
//            // check condition
//            if (ActivityCompat.checkSelfPermission(SignUpDoc.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//                // When permission is not granted
//                // Result permission
//                requestForStoragePermissions();
//                ActivityCompat.requestPermissions(SignUpDoc.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
//            }
//            else {
//                // When permission is granted
//                // Create method
//                selectImage();
//            }
        });


    }

    public boolean verifyCredentials() {

        if (name.length() <= 4) {
            name.setError("Minimum length should 5!");
            return false;
        }

        if (mail.length() == 0) {
            mail.setError("Please fill this field!");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString()).matches()) {
            mail.setError("Invalid Email!");
            return false;
        }

        if(dateSelected == null){
            Toast.makeText(this, "Select your DOB", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!validateDatePattern()){
            Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(contact.length()<11){
            contact.setError("Invalid Contact!");
            return false;
        }


        if(password.length()< 9){
            password.setError("Must be at least 9 characters!");
            return false;
        }

        if(selectOption.getText().toString().equals("")){
            Toast.makeText(this, "Select Category", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!(male.isChecked() || female.isChecked())) {
            Toast.makeText(this, "Choose one 'Gender'", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!(no.isChecked() || yes.isChecked())){
            Toast.makeText(this, "Choose one 'Surgeon'", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(degreeImage == null){
            Toast.makeText(this, "Please Upload Your Degree img", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @SuppressLint("SimpleDateFormat")
    private void updateLabel(){

        Date t = myCalendar.getTime();
        dateSelected = new java.sql.Date(t.getTime());
        dob.setText(dateSelected.toString());


    }

    private void setUpDialogBox() {
        // initialize dialog
        dialog = new Dialog(this);
        // set custom design of dialog
        dialog.setContentView(R.layout.searchable_spinner_custom_design);
        // set custom height
        dialog.getWindow().setLayout(1000, 1500);
        // set transparent background
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // show
        dialog.show();
    }

    private void handleDialogBoxFunctionality() {

        EditText editText = dialog.findViewById(R.id.editText);
        ListView dialogList = dialog.findViewById(R.id.categoryList);
        ArrayAdapter<String> dialogAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        dialogList.setAdapter(dialogAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // leave it
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // filter list
                dialogAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // leave it
            }
        });
        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // set text view as the selected item
                selectOption.setText(dialogAdapter.getItem(i));
                // close the dialog box
                dialog.dismiss();
            }
        });
    }

    public boolean validateDatePattern() {

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            Calendar calendar = Calendar.getInstance();
            String date = sdf.format(calendar.getTime());
            java.util.Date currdate1 = null;
            try {
                currdate1 = new SimpleDateFormat("yyyy/MM/dd").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if(dateSelected != null){
                assert currdate1 != null;
                if(currdate1.compareTo(dateSelected) < 0){

                    return false;
                }
            }

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return true;


    }

    public char maleOrFemale()
    {
        if(male.isChecked())
            return 'M';
        else
            return 'F';
    }

    public boolean isSurgeon() {
        return yes.isChecked();
    }

    public void onRadioButtonClicked2(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.Yes:
                if (checked)
                    Toast.makeText(getApplicationContext(),"Yes",Toast.LENGTH_LONG).show();
                break;

            case R.id.No:
                if (checked)
                    Toast.makeText(getApplicationContext(),"No",Toast.LENGTH_LONG).show();
                break;

        }
    }


    private void selectImage() {
        // Initialize intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // set type
        intent.setType("image/*");
        // Launch intent
        resultLauncher.launch(intent);
    }


}