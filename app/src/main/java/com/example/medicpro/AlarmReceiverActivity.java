package com.example.medicpro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicpro.Medicine;
import com.example.medicpro.MedicineList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class AlarmReceiverActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private Medicine med;
    private TextView medNameTextView;
    private int medIndex;
    AlertDialog.Builder alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.alarm);

        medNameTextView = findViewById(R.id.medNameTextView);
        Intent intent = getIntent();
        medIndex = intent.getIntExtra("MED_NUMBER", 0);
        med = MedicineList.getMed(medIndex);
        medNameTextView.setText("Medicine: "+ med.getName() + "\nQuantity = " + med.getQuantity());

        Button stopAlarm = (Button) findViewById(R.id.stopAlarm);
        stopAlarm.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                mMediaPlayer.stop();

                return false;
            }
        });

        playSound(this, getAlarmUri());
    }

    public void subtractMedQt(View view) {
        int quantity = med.getQuantity();
        quantity--;

        Toast.makeText(this,"Medicine Taken!",Toast.LENGTH_SHORT).show();
        if(quantity < 0)
            quantity = 0;
        med.setQuantity(quantity);
        medNameTextView.setText("Medicine: "+med.getName() + "\nQuantity = " + med.getQuantity());
        MedicineList.setMedicine(medIndex, med);
        writeArray(MedicineList.getMedicineList());
        if(quantity<5){
            alertDialog = new AlertDialog.Builder(AlarmReceiverActivity.this);
            alertDialog.setTitle("Warning!");
            alertDialog.setMessage("Do You Want to Refill?");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //.....................mail.....................

                    Intent intent = new Intent(AlarmReceiverActivity.this, MyMailActivity.class);
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

    private void writeArray(List<Medicine> medicineList) {
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

    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("Unexpected Error Occured");
        }
    }

    //Get an alarm sound. Try for an alarm. If none set, try notification,
    //Otherwise, ringtone.
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
}
