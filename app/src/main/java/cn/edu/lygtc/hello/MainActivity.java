package cn.edu.lygtc.hello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BlendMode;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText qqID,qqPW;
    private Button login;
    private CheckBox savePW,autoLogin;
    private RadioButton saveToIS,saveToES,saveToSP;
    private FloatingActionButton toBookFloatButton;
    private int saveMode = 0;
    private String textMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qqID = findViewById(R.id.qqID);
        qqPW = findViewById(R.id.qqpw);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        savePW  =findViewById(R.id.savepw);
        autoLogin = findViewById(R.id.qqautologin);
        saveToIS = findViewById(R.id.savetoISradio);
        saveToES = findViewById(R.id.savetoESradio);
        saveToSP = findViewById(R.id.savatoSPradio);
        toBookFloatButton = findViewById(R.id.toBookFloatButton);
        SharedPreferences sp = getSharedPreferences("loginInf", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        saveToIS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    saveMode = 1;
                textMode = "saveMode="+saveMode;
                Toast.makeText(MainActivity.this,textMode,Toast.LENGTH_SHORT).show();
            }
        });
        saveToES.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    saveMode = 2;
                textMode = "saveMode="+saveMode;
                Toast.makeText(MainActivity.this,textMode,Toast.LENGTH_SHORT).show();
            }
        });
        saveToSP.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    saveMode = 0;
                textMode = "saveMode="+saveMode;
                Toast.makeText(MainActivity.this,textMode,Toast.LENGTH_SHORT).show();
            }
        });
        if (sp.contains("saveMode")) {
            saveMode = sp.getInt("saveMode", 0);
            Toast.makeText(this,"从SP存储中获取了存储方式"+saveMode,Toast.LENGTH_SHORT).show();
            savePW.setChecked(true);
        }
        switch(saveMode){
            case 0:{
                if(sp.contains("qqID"))
                {
                    Toast.makeText(this,"reading",Toast.LENGTH_LONG).show();
                    qqID.setText(sp.getString("qqID","null"));
                    qqPW.setText(sp.getString("qqPW","null"));
                    saveToSP.setChecked(true);
                }
                break;
            }
            case 1:{
                saveToIS.setChecked(true);
                break;
            }
            case 2:{

                Map<String,String> LoginInf = getLoginInf();
                Toast.makeText(this,"方式2"+LoginInf.get("qqID"),Toast.LENGTH_SHORT).show();
                if (LoginInf != null){
                    Toast.makeText(this,"duqucg",Toast.LENGTH_SHORT).show();
                    qqID.setText(LoginInf.get("qqID"));
                    qqPW.setText(LoginInf.get("qqPW"));
                    saveToES.setChecked(true);
                }
                break;
            }
        }
    }
    public void toBook(View v){
        Intent BookIntent = new Intent(this,BookActivity.class);
        startActivity(BookIntent);
    }
    @Override
    public void onClick(View v) {
        String T_qqID = "1138251341";
        String T_qqPW = "996007";
        String tips;
        String qqID_TS = qqID.getText().toString();
        String qqPW_TS = qqPW.getText().toString();
        Boolean SkipMark = false;
        SharedPreferences sp = getSharedPreferences("loginInf", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(TextUtils.isEmpty(qqID_TS) ||TextUtils.isEmpty(qqPW_TS)){
            tips="用户名和密码不得为空";
        }
        else if(!qqID_TS.equals(T_qqID)||!qqPW_TS.equals(T_qqPW)){
            tips="用户名或密码错误";
        }
        else{
            switch(saveMode){
                case 0:{
                    if(savePW.isChecked()) {
                        Toast.makeText(this,"saved",Toast.LENGTH_SHORT).show();
                        editor.putString("qqID",qqID_TS);
                        editor.putString("qqPW",qqPW_TS);
                    }
                    if(!savePW.isChecked())
                    {
                        editor.remove("qqID");
                        editor.remove("qqPW");
                    }
                    editor.commit();
                }
                break;
                case 1:{

                }
                break;
                case 2:{
                    if(savePW.isChecked()) {
                        saveMode = 2;
                        boolean isSaved = saveLoginInf(qqID_TS,qqPW_TS);
                        if(isSaved){
                            Toast.makeText(this, "saved"+isSaved, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(this, "UnSaved", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            }
            Toast.makeText(this,"UnSaved",Toast.LENGTH_SHORT).show();
            tips="用户名和密码校验成功！登陆成功！"+qqID_TS+"欢迎！"+saveMode;
            SkipMark = true;
            editor.putInt("saveMode",saveMode);
            editor.commit();
        }
            AlertDialog dialog;
        Boolean finalSkipMark = SkipMark;
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("登陆提示")
                .setMessage(tips)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(finalSkipMark){
                            Intent FruitIntent = new Intent(MainActivity.this,fruitshop.class);
                            startActivity(FruitIntent);
                        }
                    }
                });
            dialog = builder.create();
            dialog.show();

    }
    public boolean saveLoginInf(String qqID, String qqPW) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File SDPath = getExternalFilesDir(null);
            File file = new File(SDPath, "LoginInf.txt");
            FileOutputStream fos = null;
            try {
                ActivityCompat.requestPermissions(this,new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},1);
                fos = new FileOutputStream(file);
                fos.write((qqID + ":" + qqPW).getBytes());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return false;
        }
    }

    public Map<String, String> getLoginInf() {
        String state = Environment.getExternalStorageState();
        String content = "";
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File SDPath = getExternalFilesDir(null);
            File file = new File(SDPath, "LoginInf.txt");
            FileInputStream fis = null;
            BufferedReader br;
            try {
                ActivityCompat.requestPermissions(this,new String[]{"android.permission.READ_EXTERNAL_STORAGE"},2);
                fis = new FileInputStream(file);
                br = new BufferedReader(new InputStreamReader(fis));
                content = br.readLine();
                Map<String,String> userMap = new HashMap<String,String>();
                String[] infos = content.split(":");
                Toast.makeText(MainActivity.this,"尝试读取成功"+infos[0],Toast.LENGTH_LONG).show();
                userMap.put("qqID",infos[0]);
                userMap.put("qqPW",infos[1]);
                return userMap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1||requestCode == 2){
            for(int i = 0;i<permissions.length;i++){
                if((permissions[i]=="android.permission.WRITE_EXTERNAL_STORAGE")&&grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"权限"+permissions[i]+"通过",Toast.LENGTH_LONG).show();
                }else if((permissions[i]=="android.permission.READ_EXTERNAL_STORAGE")&&grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"权限"+permissions[i]+"通过",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this,"权限"+permissions[i]+"未通过",Toast.LENGTH_LONG).show();
                }
            }
        }

}
}