package com.example.pyramidofra;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;


public class MainActivity extends FragmentActivity {
    FragmentTransaction fTrans;
   public MainFragment mainFragment;
   public RulesFragment rulesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mainFragment = new MainFragment();
        rulesFragment=new RulesFragment();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_main, mainFragment)
                .commit();

    }

    public void toGame() {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(intent);
        finish();
    }
}



