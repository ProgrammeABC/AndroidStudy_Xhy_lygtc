package cn.edu.lygtc.hello;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class fruitshop extends AppCompatActivity {
    private RecyclerView fruitList;
    private HomeAdapter fruitAdapter;
    private String [] names = {"coreA7","Ryzen-S9","code8-86","cool-007","fire-696","water-789"};
    private int [] icon = {R.drawable.cpu1,R.drawable.cpu2,R.drawable.cpu3,R.drawable.cpu4,R.drawable.cpu5,R.drawable.cpu6};
    private String [] details = {"intel","AMD","NUll","NULL2","NULL3","NULL4"};
    private String [] prices = {"999","222.2","333.3","555.5","888.000","6666"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruitshop);
        fruitList =(RecyclerView) findViewById(R.id.fruitlist);
        fruitList.setLayoutManager(new LinearLayoutManager(this));
        fruitAdapter = new HomeAdapter();
        fruitList.setAdapter(fruitAdapter);
    }
    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(fruitshop.this).inflate(R.layout.fruit_item,parent,false));
            return holder;
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position){
            holder.Name.setText("型号："+names[position]);
            holder.Price.setText("价格：￥"+prices[position]);
            holder.iv.setImageResource(icon[position]);
        }
        public int getItemCount(){
            return names.length;
        }
        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView Name;
            ImageView iv;
            TextView Price;
            public MyViewHolder(View view){
                super(view);
                Name = (TextView) view.findViewById(R.id.NameTextView);
                iv = (ImageView) view.findViewById(R.id.imageView2);
                Price = (TextView) view.findViewById(R.id.PricetextView);

            }
        }
    }
}