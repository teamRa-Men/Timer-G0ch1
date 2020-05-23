package teamramen.cs103.yoobeecolleges.timergotchi.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.Petitem;
import teamramen.cs103.yoobeecolleges.timergotchi.R;
import teamramen.cs103.yoobeecolleges.timergotchi.pet.PetActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.record.RecordActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.lists.ListsActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.timer.TimerActivity;

public class ShopActivity extends AppCompatActivity {
    ListView shoplist;
    TextView shoppoints;

    String namelist[] = {"Mushroom", "Backpack"};
    int affection[] = {-2,2} ;
    int health[] = {2,0} ;
    int price[] = {24, 1250};
    int shop_images[] ={R.drawable.food_mushroom, R.drawable.backpack_closed};
    String description[] = new String[]{"A common red Mushroom, tastes kinda icky. \n\n +0 Affection \n +5 Food ","Back pack, stores items "};
    int type[] = {0,1};

    int point;

    Petitem currentitem;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_shop);


        shoplist = findViewById(R.id.Shoplist);

        db = new DatabaseHelper(this);
        shoppoints = findViewById(R.id.textView3);
        point = db.getPoints();
        shoppoints.setText(db.getPoints() + " g");


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

    public void buyitem(View view) {

        if(currentitem.price <= point) {
            DatabaseHelper db = new DatabaseHelper(this);
            db.addPetitem(currentitem);
            point = point - currentitem.price;
            db.setPoints(point);
            shoppoints.setText(point + " g");
        }
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
        public View getView(final int i, View view, ViewGroup viewGroup) {

            View v = getLayoutInflater().inflate(R.layout.shop_items, null);

            ImageView Picture = v.findViewById(R.id.Picture);
            TextView Price = v.findViewById(R.id.Price);
            TextView Name = v.findViewById(R.id.Name);

            Picture.setImageResource(shop_images[i]);
            Price.setText(  price[i] + "g");
            Name.setText(namelist[i]);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (i < shop_images.length) {
                        TextView itemdescription = findViewById(R.id.itemdescription);
                        ImageView itemimage = findViewById(R.id.itemimage);

                        itemimage.setImageResource(shop_images[i]);
                        itemdescription.setText(description[i]);

                        currentitem = new Petitem(namelist[i], shop_images[i],type[i],i,health[i],affection[i],price[i]);
                        Button buybutton =  findViewById(R.id.buybutton);
                        buybutton.setVisibility(View.VISIBLE);
                    }
                }
            });

            return v;
        }
    }
}
