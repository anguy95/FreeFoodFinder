package andrewnguy.com.freefoodfinder;

/**
 * Created by anguy95 on 10/27/15.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ListTab extends Fragment {

    ArrayList<String> animalsNameList;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_tab, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        ListView animalList = (ListView) v.findViewById(R.id.listView);



        animalsNameList = new ArrayList<String>();
        getAnimalNames();
        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.mytextview, animalsNameList);
        // Set The Adapter
        animalList.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item
        animalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                String selectedAnimal = animalsNameList.get(position);
                Toast.makeText(getActivity().getApplicationContext(), "Animal Selected : " + selectedAnimal, Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    void getAnimalNames()
    {
        animalsNameList.add("DOG");
        animalsNameList.add("CAT");
        animalsNameList.add("HORSE");
        animalsNameList.add("ELEPHANT");
        animalsNameList.add("LION");
        animalsNameList.add("COW");
        animalsNameList.add("MONKEY");
        animalsNameList.add("DEER");
        animalsNameList.add("RABBIT");
        animalsNameList.add("BEER");
        animalsNameList.add("DONKEY");
        animalsNameList.add("LAMB");
        animalsNameList.add("GOAT");
        animalsNameList.add("DOG");
        animalsNameList.add("CAT");
        animalsNameList.add("HORSE");
        animalsNameList.add("ELEPHANT");
        animalsNameList.add("LION");
        animalsNameList.add("COW");
        animalsNameList.add("MONKEY");
        animalsNameList.add("DEER");
        animalsNameList.add("RABBIT");
        animalsNameList.add("BEER");
        animalsNameList.add("DONKEY");
        animalsNameList.add("LAMB");
        animalsNameList.add("GOAT");



    }
}