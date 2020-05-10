package teamramen.cs103.yoobeecolleges.timergotchi.lists;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ListPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> listList = new ArrayList<Fragment>();
    ArrayList<String> titleList = new ArrayList<String>();


    public  ListPagerAdapter(FragmentManager manager){
        super(manager);



    }



    @Override
    public Fragment getItem(int position) {
        return listList.get(position);
    }

    @Override
    public int getCount() {
        return listList.size();
    }

    public void addList (Fragment list, String title, int position){
        listList.add(list);
        titleList.add(title);
    }

    public void removeList (int position){
        listList.remove(position);
        titleList.remove(position);
    }

    public String getListTitle(int position){
        return titleList.get(position);
    }
}
