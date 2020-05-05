package teamramen.cs103.yoobeecolleges.timergotchi.ui.record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.R;



public class RecordFragment extends Fragment {

    public static RecordFragment instance;
    DatabaseHelper db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_record, container, false);
        db = new DatabaseHelper(getContext());
        instance = this;
        return root;
    }

    public void onClearRecord(){
        db.clearTasks();
    }
}