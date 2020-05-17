package teamramen.cs103.yoobeecolleges.timergotchi.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import teamramen.cs103.yoobeecolleges.timergotchi.R;
import teamramen.cs103.yoobeecolleges.timergotchi.pet.PetActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.record.RecordActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.lists.ListsActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.timer.TimerActivity;

public class ShopActivity extends AppCompatActivity {
    ListView shoplist;

    String namelist[] = {"Mushroom", "Backpack"};
    int price[] = {24, 22};
    int shop_images[] ={R.drawable.food_mushroom, R.drawable.backpack_closed};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        shoplist = findViewById(R.id.Shoplist);

        adapter adapter = new adapter();
        shoplist.setAdapter(adapter);
    }

    public void toList(View view) {
        Intent i = new Intent(this, ListsActivity.class);
        startActivity(i);
    }

    public void toTimer(View view) {
        Intent i = new Intent(this, TimerActivity.class);
        startActivity(i);
    }

    public void toPet(View view) {
        Intent i = new Intent(this, PetActivity.class);
        startActivity(i);
    }
/*
    public void toShop(View view) {
        Intent i = new Intent(this, ShopActivity.class);
        startActivity(i);
    }*/

    public void toRecord(View view) {
        Intent i = new Intent(this, RecordActivity.class);
        startActivity(i);
    }

    class adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return namelist.length; }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View v = getLayoutInflater().inflate(R.layout.shop_items, null);

            ImageView Picture = v.findViewById(R.id.Picture);
            TextView Price = v.findViewById(R.id.Price);
            TextView Name = v.findViewById(R.id.Name);

            Picture.setImageResource(shop_images[i]);
            Price.setText(price[i]+"");
            Name.setText(namelist[i]);

            return v;
        }
    }
}
