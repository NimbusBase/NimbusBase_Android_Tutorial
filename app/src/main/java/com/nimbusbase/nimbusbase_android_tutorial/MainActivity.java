package com.nimbusbase.nimbusbase_android_tutorial;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Singleton.base().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Singleton.base().onResume(this);
    }
}
