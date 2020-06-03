package teamramen.cs103.yoobeecolleges.timergotchi.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;

import android.content.ClipData;
import android.content.Intent;
import android.media.MediaPlayer;
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
    ImageView shopModBack,shopModFront,shopWear;


    String namelist[] = {"Mushroom", "Wings", "Horn","Elixer","Collar","Flower"};
    int affection[] = {0,0,0,0,0,0};
    int health[] = {10,0,0,100,0,0};
    int price[] = {0, 0,0,0,0,0};
    int shop_images[] ={R.drawable.food_mushroom, R.drawable.pet_wings,R.drawable.pet_horn,R.drawable.potion, R.drawable.pet_collar,R.drawable.pet_flower};
    String description[] = new String[]{"A common red Mushroom, tastes kinda icky. \n\n +0 Happiness \n +10 Food ","Grow wings","Grow a horn","Restores full health for demo", "Wear a collar", "Wear a flower"};
    int type[] = {0,1,2,0,3,3};


    int point;

    Petitem currentitem;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_shop);

        shopModBack = findViewById(R.id.shopModBack);
        shopModFront = findViewById(R.id.shopModFront);
        shopWear = findViewById(R.id.shopWear);
        shoplist = findViewById(R.id.Shoplist);

        db = new DatabaseHelper(this);
        shoppoints = findViewById(R.id.textView3);
        point = db.getPoints();
        shoppoints.setText(db.getPoints() + " ");



        adapter adapter = new adapter();
        shoplist.setAdapter(adapter);
        showItemDetails(0);
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
            playBuySound();
            shoppoints.setText(point + " ");
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
            Price.setText(  price[i] + " ");
            Name.setText(namelist[i]);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showItemDetails(i);
                }
            });

            return v;
        }
    }

    void showItemDetails(int i){
        if (i < shop_images.length) {
            TextView itemdescription = findViewById(R.id.itemdescription);
            ImageView itemimage = findViewById(R.id.itemimage);

            if(type[i]==1){
                shopModBack.setBackgroundResource(shop_images[i]);
                shopModFront.setBackgroundResource(R.color.colorEmpty);
                shopWear.setBackgroundResource(R.color.colorEmpty);
            }
            else if(type[i]==2){
                shopModBack.setBackgroundResource(R.color.colorEmpty);
                shopModFront.setBackgroundResource(shop_images[i]);
                shopWear.setBackgroundResource(R.color.colorEmpty);
            }
            else if(type[i]==3){
                shopModBack.setBackgroundResource(R.color.colorEmpty);
                shopModFront.setBackgroundResource(R.color.colorEmpty);
                shopWear.setBackgroundResource(shop_images[i]);
            }
            else{
                shopModBack.setBackgroundResource(R.color.colorEmpty);
                shopModFront.setBackgroundResource(R.color.colorEmpty);
                shopWear.setBackgroundResource(R.color.colorEmpty);
            }

            itemimage.setImageResource(shop_images[i]);
            itemdescription.setText(description[i]);

            currentitem = new Petitem(namelist[i], shop_images[i],type[i],i,health[i],affection[i],price[i]);
            Button buybutton =  findViewById(R.id.buybutton);
            View buyButtonOff = findViewById(R.id.buyshadow);
            TextView showPrice = findViewById(R.id.priceDetail);
            TextView showName = findViewById(R.id.nameDetail);
            showPrice.setText(currentitem.price+" ");
            showName.setText(currentitem.name+"");

            if(currentitem.price <= point) {
                buyButtonOff.setVisibility(View.INVISIBLE);
            }
            else{
                buyButtonOff.setVisibility(View.VISIBLE);
            }

        }
    }

    public void playBuySound() {
        MediaPlayer buySound = MediaPlayer.create(this, R.raw.cha_ching);
        buySound.start();
    }
}
