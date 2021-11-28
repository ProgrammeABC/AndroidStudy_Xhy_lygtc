package cn.edu.lygtc.hello;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class BookActivity extends AppCompatActivity implements View.OnClickListener{
    MyAppDBHelper DataBaseHelper;
    private EditText timeText;
    private EditText amountText;
    private EditText remarkText;
    private Button addButton;
    private Button queryButton;
    private Button deleteButton;
    private Button updateButton;
    private String category;
    private TextView outText;
    private Button pageAddButton;
    private Button pageReduceButton;
    private Button saveChangeButton;
    private Button ChangeCancelButton;
    private TextView pageNumber;
    private String temp_up;

    private String[] categoryType = {"食品","衣物","娱乐","交通","通讯","其他","请选择"};
    private Spinner categoryList;
    private String version = "v0.0.09beta";
    private String Welcome = "欢迎使用小二记账本"+version+"!";
    private Boolean Activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sp = getSharedPreferences("ActivityMode",MODE_PRIVATE);
        Activity = sp.getBoolean("Activity",false);
        if(Activity) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_book);
            DataBaseHelper = new MyAppDBHelper(this);
            init();
            outText.setText(Welcome);
        } else {
            Toast.makeText(this, "请先激活软件！", Toast.LENGTH_SHORT).show();
            Intent GotoActivity = new Intent(BookActivity.this,MainActivity.class);
            startActivity(GotoActivity);
        }
    }
    private void init(){
        timeText =  findViewById(R.id.timeEditText);
        amountText = findViewById(R.id.accountEditText);
        remarkText = findViewById(R.id.categoryRemarkText);
        outText = findViewById(R.id.outTextView);
        addButton = findViewById(R.id.addDataButton);
        queryButton = findViewById(R.id.queryDataButton);
        deleteButton = findViewById(R.id.deleteDataButton);
        updateButton = findViewById(R.id.updateDataButton);
        pageAddButton = findViewById(R.id.pageAddButton);
        pageReduceButton = findViewById(R.id.pageReduceButton);
        saveChangeButton = findViewById(R.id.saveChangeButton);
        ChangeCancelButton = findViewById(R.id.saveCancelButton);
        pageNumber = findViewById(R.id.pageNumberTextView);

        addButton.setOnClickListener(this);
        queryButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        updateButton.setOnClickListener(this);
        pageAddButton.setOnClickListener(this);
        pageReduceButton.setOnClickListener(this);
        saveChangeButton.setOnClickListener(this);
        ChangeCancelButton.setOnClickListener(this);

        categoryList = findViewById(R.id.choiceTypeSpinner);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,categoryType);
        categoryList.setAdapter(typeAdapter);
        categoryList.setSelection(6);
        categoryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] categoryList = categoryType;
//                Toast.makeText(BookActivity.this, "你选择的是:"+categoryList[pos], Toast.LENGTH_LONG).show();
                category = categoryList[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback

            }
        });
    }
    int page = 1,count = 0;
    Cursor allCursor;
    public void query(int page_no) {
        SQLiteDatabase db;
        db = DataBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT time,total,description,_id FROM book WHERE category=?  ORDER BY time", new String[]{category});
        if (category.equals("请选择")) {
            outText.setText("请先选择交易类目！");
        } else if(cursor.getCount() == 0){
            outText.setText("未查询到可信数据");
        } else{
            cursor.moveToPosition(page_no);
            if (cursor.getCount() % 3 != 0)
                count = cursor.getCount() / 3 + 1;
            else {
                count = cursor.getCount() / 3;
            }
            int no = 0;
            pageNumber.setText(page + "/" + count);
            outText.setText("查询到 " + cursor.getCount() + " 条 " + category + " 类数据,结果如下：\n");
            outText.append(((page-1)*3+no+1)+".日期：" + cursor.getString(0) + " 添加序列:" + cursor.getString(3) + "\n   --金额：" + cursor.getString(1) + "\n   --备忘：" + cursor.getString(2));
            while (cursor.moveToNext()) {
                no++;
                outText.append("\n" +((page-1)*3+no+1)+ ".日期：" + cursor.getString(0) + " 添加序列:" + cursor.getString(3) + "\n   --金额：" + cursor.getString(1) + "\n   --备忘：" + cursor.getString(2));
                if (no == 2) break;
            }
            allCursor = cursor;

            cursor.close();
            db.close();
        }
    }
    public String del(String del_no){
        SQLiteDatabase db = DataBaseHelper.getWritableDatabase();
        int del_count =  db.rawQuery("SELECT * FROM book WHERE _id = ?",new String[]{del_no}).getCount();
        if(del_count!=0){
            db.delete("book","_id=?",new String[]{del_no});
            db.close();
            pageAddButton.setVisibility(View.VISIBLE);
            pageReduceButton.setVisibility(View.VISIBLE);
            pageNumber.setVisibility(View.VISIBLE);
            page = 1;
            query(0);
            return "添加序列为"+del_no+"的数据已删除！";
        }
        else {
            db.close();
            return "指定数据不存在！";
        }
    }
    public void update(String id){
        SQLiteDatabase db;
        db= DataBaseHelper.getWritableDatabase();
        db.execSQL("UPDATE book SET time = ?,total = ?,category = ?,description = ? WHERE _id = ?",new String[]{timeText.getText().toString()
        ,amountText.getText().toString(),category,remarkText.getText().toString(),id});
//        ContentValues values = new ContentValues();
//        values.put("time",timeText.getText().toString());
//        values.put("total",amountText.getText().toString());
//        values.put("description",remarkText.getText().toString());
//        values.put("category",category);
//        db.update("book",values,"_id=?",new String[]{id});
        EditClear();
        Toast.makeText(this, "数据更新成功！", Toast.LENGTH_SHORT).show();
        Cursor cursor = db.rawQuery("SELECT time,total,description,category FROM book WHERE _id=?  ORDER BY time", new String[]{id});
            cursor.moveToFirst();
            pageNumber.setText("1/1");
            outText.setText("查询到 " + cursor.getCount() + " 条更改后的 " + cursor.getString(3) + " 类数据,结果如下：\n");
            outText.append("1.日期：" + cursor.getString(0) + " 添加序列:" + id + "\n  " +
                    " --金额：" + cursor.getString(1) + "\n   --备忘：" + cursor.getString(2));
            cursor.close();
        db.close();
    }
    public void button_show(){
        addButton.setVisibility(View.VISIBLE);
        queryButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.VISIBLE);
        saveChangeButton.setVisibility(View.INVISIBLE);
        ChangeCancelButton.setVisibility(View.INVISIBLE);
    }
    public void button_hidden(){
        addButton.setVisibility(View.INVISIBLE);
        queryButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
        updateButton.setVisibility(View.INVISIBLE);
        saveChangeButton.setVisibility(View.VISIBLE);
        ChangeCancelButton.setVisibility(View.VISIBLE);
    }
    public void EditClear(){
        timeText.setText("");
        amountText.setText("");
        remarkText.setText("");
        categoryList.setSelection(6);
    }
    @Override
    public void onClick(View v) {
        String time;
        String remark;
        Double amount;
        SQLiteDatabase db;
        ContentValues values;
        switch (v.getId()){
            case R.id.pageAddButton:
                if(page<count) {
                    page++;
                    query((page-1)*3);
                    }
                pageNumber.setText(page+"/"+count);
                break;
            case R.id.pageReduceButton:
                if(page>1) {
                    page--;
                    query((page-1)*3);
                }
                pageNumber.setText(page+"/"+count);
                break;
            case R.id.addDataButton:
                remark = remarkText.getText().toString();
                time = timeText.getText().toString();
                if(time.equals("")){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    time = simpleDateFormat.format(date);
                }
                if(amountText.getText().toString().equals("")){
                    Toast.makeText(this, "金额不得为空！", Toast.LENGTH_SHORT).show();
                } else if(category.equals("请选择")){
                    Toast.makeText(this, "请选择交易类目！", Toast.LENGTH_SHORT).show();
                } else{
                    amount =  Double.valueOf(amountText.getText().toString());
                    db = DataBaseHelper.getWritableDatabase();
                    values = new ContentValues();
                    values.put("time",time);
                    values.put("total",amount);
                    values.put("description",remark);
                    values.put("category",category);
                    db.insert("book",null,values);
                    timeText.setText("");
                    amountText.setText("");
                    remarkText.setText("");
                    Toast.makeText(this, "数据添加成功！", Toast.LENGTH_LONG).show();
                    db.close();
                }
                break;
            case R.id.queryDataButton:
                pageAddButton.setVisibility(View.VISIBLE);
                pageReduceButton.setVisibility(View.VISIBLE);
                pageNumber.setVisibility(View.VISIBLE);
                page = 1;
                query(0);
                break;
            case R.id.deleteDataButton:
                final EditText et = new EditText(this);
                new AlertDialog.Builder(this).setTitle("请输入要删除数据的添加序列：")
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //按下确定键后的事件
                                String Msg= del(et.getText().toString());
                                Toast.makeText(getApplicationContext(), Msg,Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("取消",null).show();
            break;
            case R.id.updateDataButton:
                final EditText ud_et = new EditText(this);
                new AlertDialog.Builder(this).setTitle("请输入要修改数据的添加序列：")
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setView(ud_et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //按下确定键后的事件
                                temp_up = ud_et.getText().toString();
                                SQLiteDatabase db = DataBaseHelper.getReadableDatabase();
                                Cursor preData = db.rawQuery("SELECT time,total,category,description" +
                                        " FROM book WHERE _id=?",new String[]{temp_up});
                                if(preData.getCount()!=0){
                                    preData.moveToFirst();
                                    timeText.setText(preData.getString(0));
                                    amountText.setText(preData.getString(1));
                                    remarkText.setText(preData.getString(3));
                                    int cg_code = 6;
                                    switch (preData.getString(2)){
                                        case "食品": cg_code = 0; break;
                                        case "衣物": cg_code = 1; break;
                                        case "娱乐": cg_code = 2; break;
                                        case "交通": cg_code = 3; break;
                                        case "通讯": cg_code = 4; break;
                                        case "其他": cg_code = 5; break;
                                    }
                                    categoryList.setSelection(cg_code);
                                    button_hidden();
                                }else{
                                    Toast.makeText(BookActivity.this, "未查询到相应数据！", Toast.LENGTH_SHORT).show();
                                }
//                                Toast.makeText(getApplicationContext(), Msg,Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("取消",null).show();
            break;
            case R.id.saveChangeButton:
                update(temp_up);
                button_show();                temp_up = "";
//                outText.setText(Welcome);
            break;
            case R.id.saveCancelButton:
                button_show();
                temp_up = "";
                EditClear();
                outText.setText(Welcome);
            break;
        }
    }
}