package com.example.medicpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import static java.lang.Integer.*;

public class RefillActivity extends AppCompatActivity {
    private EditText qty;
    private  int medIndex;
    private Medicine med;
    private int quantity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill);
        qty = findViewById(R.id.qty);
        Intent intent = getIntent();
        medIndex = intent.getIntExtra("MED_NUMBER", 0);
        med = null;
        if (medIndex != -1) {
            med = MedicineList.getMedicineList().get(medIndex);

        }
    }
    public void saveMe(View view){

        String st = qty.getText().toString();
        quantity = 0;
        if (!st.isEmpty()) {
            quantity = Integer.parseInt(st);
        }
        med.setQuantity(quantity);
        MedicineList.setMedicine(medIndex, med);
        writeArray(MedicineList.getMedicineList());
        Toast.makeText(this,"Refilled Successful",Toast.LENGTH_SHORT).show();
        Intent in =new Intent(RefillActivity.this,MyMedActivity.class);
        startActivity(in);
        finish();

    }
    public void writeArray(List<Medicine> medicineList) {
        File f = new File(getFilesDir(), "medicine_list.srl");
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream objectwrite = new ObjectOutputStream(fos);
            objectwrite.writeObject(medicineList);
            fos.close();

            if (!f.exists()) {
                f.mkdirs();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

