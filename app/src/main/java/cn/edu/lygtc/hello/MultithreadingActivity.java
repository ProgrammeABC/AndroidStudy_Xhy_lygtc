package cn.edu.lygtc.hello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MultithreadingActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText inputEditText;
    private Button countButton;
    private Handler handler;
    private int maxValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multithreading);
        init();
        clickRegister();
    }
    public void init(){
        inputEditText = findViewById(R.id.mtInputEditText);
        countButton = findViewById(R.id.mtCountButton);
//        handler = new Handler(Looper.getMainLooper()){
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
//                maxValue = msg.getData();
//                inputEditText.setText(maxValue);
//            }
//        };
    }
    public void clickRegister(){
        countButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
            CounterTask task = new CounterTask();
            task.execute(0);
//              Counter counter = new Counter();
//              counter.start();
    }
    class Counter extends Thread{
        private int time;

        void Counter(){
            nameXx();
        }
        public void nameXx(){
            time++;
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("time",String.valueOf(time));
            msg.setData(data);
        }
    }
    class CounterTask extends AsyncTask<Integer,Integer,Integer> {
        private int duration;
        private boolean state;

        public CounterTask(){
            this.duration = 0;
            state = true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int count = values[0];
//            int hour;
//            int minute;
//            int second;
//            int tenSecond;
//            String inf = "ok"+count;

            inputEditText.setText(count);
        }



        @Override
        protected Integer doInBackground(Integer... integers) {
            String sourceData;
            sourceData = inputEditText.getText().toString();
            if(sourceData.contains("-")){
               String[] parts= sourceData.split(",");
                publishProgress(Integer.valueOf(duration));
            } else throw new IllegalArgumentException("当前字符串中未发现“-”，无法分割内容");
            return duration;
//            while(state){
//                long start = System.currentTimeMillis();
//                duration ++;
//                publishProgress(Integer.valueOf(duration));
//                long end = System.currentTimeMillis();
//                try {
//                    Thread.sleep(10-(end - start));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
        }
    }

}
