package cn.edu.lygtc.hello;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MultithreadingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView time;
    private TextView out;
    private Button SACButton;
    private Button ClearButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multithreading);
        init();
        clickRegister();
    }
    public void init(){
        time = findViewById(R.id.time);
        out = findViewById(R.id.outTextView);
        SACButton = findViewById(R.id.StartAndCountbutton);
        ClearButton = findViewById(R.id.ClearButton);
    }
    public void clickRegister(){
        SACButton.setOnClickListener(this);
        ClearButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

    }
}