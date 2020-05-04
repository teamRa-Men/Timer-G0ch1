package teamramen.cs103.yoobeecolleges.timergochi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Record extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
    }

    public void toTasks(View view) {
        Intent i = new Intent(this,Tasks.class);

        startActivity(i);
    }

    public void toTimer(View view) {
        Intent i = new Intent(this,Timer.class);

        startActivity(i);
    }

    public void toPet(View view) {
        Intent i = new Intent(this,Pet.class);

        startActivity(i);
    }

    public void toShop(View view) {
        Intent i = new Intent(this,Shop.class);

        startActivity(i);
    }

    public void toRecord(View view) {
        Intent i = new Intent(this,Record.class);

        startActivity(i);
    }
}
