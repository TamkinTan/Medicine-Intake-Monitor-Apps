package com.example.medicpro;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicpro.Medicine;
import com.example.medicpro.MedicineList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyMedActivity extends AppCompatActivity {

    private ListView list ;
    private ArrayAdapter<String> adapter;
    private Medicine med;
    int index;
    AlertDialog.Builder alertDialog;
    private TextView tv;
    private int medIndex;
    private TextView det;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_med);

        list = (ListView) findViewById(R.id.myMedTimesListView);


        tv = findViewById(R.id.tvQt);
        det = findViewById(R.id.details);
        Intent intent = getIntent();
        medIndex = intent.getIntExtra("MED_NUMBER", 0);


        index = getIntent().getIntExtra("MED_NUMBER", -1);
        med = null;
        if (medIndex != -1) {
            med = MedicineList.getMedicineList().get(medIndex);
            setList(medIndex, med);


            tv.setText("Quantity = " + med.getQuantity());
            det.setText("Instructions: "+ med.getDetails());

        }

    }
    public void subtractMedQt(View view) {
        int quantity = med.getQuantity();
        quantity--;
        Toast.makeText(this,"Medicine Taken!",Toast.LENGTH_SHORT).show();

        if(quantity < 0)
            quantity = 0;
        med.setQuantity(quantity);
        tv.setText("Quantity = " + med.getQuantity());
        MedicineList.setMedicine(medIndex, med);
        writeArray(MedicineList.getMedicineList());

        if(quantity<5){
            alertDialog = new AlertDialog.Builder(MyMedActivity.this);
            alertDialog.setTitle("Warning!");
            alertDialog.setMessage("Do You Want to Refill?");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //.....................mail.....................

                    Intent intent = new Intent(MyMedActivity.this, MyMailActivity.class);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();

        }

    }

    public void saveMedInfo(View view) {
        Intent intent = new Intent(MyMedActivity.this, RefillActivity.class);
        startActivity(intent);

        finish();

    }

    private void setList(int nr, Medicine med) {
        TextView medNameTextView = findViewById(R.id.myMedNameTextView);
        medNameTextView.setText(med.getName());

        List<String> strings = new ArrayList<>();
        List<Calendar> alarmTimes = med.getAlarmTimeList();
        for (Calendar cal : alarmTimes) {
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            String time = hour + ":";
            if(minute < 10)
                time += "0";
            time += minute;
            strings.add(time);
        }

        adapter = new ArrayAdapter<String>(this, R.layout.list_layout, strings);
        list.setAdapter(adapter);
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
