package com.example.medicpro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyMedsActivity extends AppCompatActivity {

    private ListView list ;
    private ArrayAdapter<String> adapter ;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meds);

        list = (ListView) findViewById(R.id.myMedsListView);

        List<Medicine> medList = MedicineList.getMedicineList();

        List<String> strings = new ArrayList<>(medList.size());
        for (Object object : medList) {
            strings.add(Objects.toString(object, null));
        }

        adapter = new ArrayAdapter<String>(this, R.layout.list_layout, strings);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //ListEntry entry= (ListEntry) parent.getAdapter().getItem(position);
                Intent intent = new Intent(MyMedsActivity.this, MyMedActivity.class);
                //intent.putExtra("MED_NAME", "Med Name");
                intent.putExtra("MED_NUMBER", position);
                startActivity(intent);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Medicine mName = MedicineList.getMed(i);
                MedicineList.removeMed(MedicineList.getMedicineList().get(i));
                Toast.makeText(MyMedsActivity.this, "Medicine Deleted: "+ mName, Toast.LENGTH_SHORT).show();
                writeArray();
                adapter.notifyDataSetChanged();
                finish();
                startActivity(getIntent());
                return true;
            }
        });

    }
    private void writeArray() {
        File f = new File(getFilesDir(), "medicine_list.srl");
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream objectwrite = new ObjectOutputStream(fos);
            objectwrite.writeObject(MedicineList.getMedicineList());
            objectwrite.close();

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
