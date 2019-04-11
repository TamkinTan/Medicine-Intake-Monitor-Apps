package com.example.medicpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MyMailActivity extends AppCompatActivity {
    private EditText mMedName;
    private EditText mMedQuantity;
    private EditText mMail;
    private EditText mCname;
    private EditText mCAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mail);

        mMedName =findViewById(R.id.medNameTextView);
        mMedQuantity=findViewById(R.id.quantityEditText);
        mMail = findViewById(R.id.mail);
        mCname = findViewById(R.id.cName);
        mCAddress = findViewById(R.id.pAddress);

        Button btnSend = findViewById(R.id.button2);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }
    private void sendMail(){
        String recipientList =mMail.getText().toString();
        String[] recipient =recipientList.split(",");
        String message = "Name: "+ mCname.getText().toString()+",\n Address: "+mCAddress.getText().toString()+",\n Medicine Name: "+mMedName.getText().toString()+", \n Quantity: "+mMedQuantity.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipient);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Medicine Purchase");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "choose an email client"));
    }
}
