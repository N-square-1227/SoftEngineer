package com.example.homework1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        String recvMessage = intent.getStringExtra(MainActivity.MESSAGE_TYPE);
        TextView t=(TextView) findViewById(R.id.textView3);
        if(recvMessage.contains("false")){          //没有选择check
            int index=recvMessage.indexOf(":");
            recvMessage=recvMessage.substring(0,index);
            t.setText(recvMessage);
        }else{                                      //选择了check
            int index=recvMessage.indexOf(":");
            recvMessage=recvMessage.substring(0,index);
            t.setText(recvMessage+":checked");
        }

    }
}
