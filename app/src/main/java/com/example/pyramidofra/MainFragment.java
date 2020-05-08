package com.example.pyramidofra;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_main, container, false);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button start = (Button) getView().findViewById(R.id.start);
        Button rules = (Button) getView().findViewById(R.id.rules);
        Button exit = (Button) getView().findViewById(R.id.exit);
        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //RulesFragment fragment = new RulesFragment();
                fragmentTransaction.replace(R.id.fragment_main,((MainActivity)getActivity()).rulesFragment); //provide the fragment ID of your first fragment which you have given in //fragment_layout_example.xml file in place of first argument fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).toGame();
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                RulesFragment fragment = new RulesFragment();
//                fragmentTransaction.replace(R.id.fragment_main, fragment); //provide the fragment ID of your first fragment which you have given in //fragment_layout_example.xml file in place of first argument fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                RulesFragment fragment = new RulesFragment();
//                fragmentTransaction.replace(R.id.fragment_main, fragment); //provide the fragment ID of your first fragment which you have given in //fragment_layout_example.xml file in place of first argument fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
            }
        });
    }

}


