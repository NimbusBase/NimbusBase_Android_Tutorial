package com.nimbusbase.nimbusbase_android_tutorial;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    public final static String
        FRAGMENT_TAG_INDEX = "index_fragment_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Singleton.CONTEXT = getApplicationContext();
        Singleton.base();

        final FragmentManager
                fragmentManager = this.getFragmentManager();

        final IndexFragment
                indexFragment = (IndexFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG_INDEX);
        if (indexFragment == null) {
            final IndexFragment
                    newIndexFragment = new IndexFragment();
            fragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, newIndexFragment, FRAGMENT_TAG_INDEX)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
