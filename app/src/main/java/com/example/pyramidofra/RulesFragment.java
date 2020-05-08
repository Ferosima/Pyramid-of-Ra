package com.example.pyramidofra;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RulesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_rules, container, false);
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)   {
                if (keyCode == KeyEvent.KEYCODE_BACK) {


                    MainFragment fragment = new MainFragment();

                    FragmentTransaction  transaction=getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_main,fragment);

                    transaction.commit();

                    return true;
                }
                return false;
            }
        });

        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button exit_rules = (Button) getView().findViewById(R.id.exit_rules);
        exit_rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //MainFragment fragment = new MainFragment();
                fragmentTransaction.replace(R.id.fragment_main,  ((MainActivity) getActivity()).mainFragment); //provide the fragment ID of your first fragment which you have given in //fragment_layout_example.xml file in place of first argument fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

}