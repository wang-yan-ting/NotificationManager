package com.tools.joe.notiman.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tools.joe.notiman.Injectable;
import com.tools.joe.notiman.NotiManApp;
import com.tools.joe.notiman.R;
import com.tools.joe.notiman.di.AppComponent;

/**
 * Created by joe_wang on 2016/8/1.
 */
public abstract class BaseActivity extends AppCompatActivity implements Injectable {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBarConfig(actionBar);
        }

        inject();
        initActivity();
    }

    abstract protected int getLayout();
    protected void initActivity() {}
    @Override public void inject() {}
    protected void actionBarConfig(ActionBar ab) {}

    protected AppComponent getAppComponent() {
        return ((NotiManApp)getApplication()).getAppComponent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater meInflater = getMenuInflater();
        meInflater.inflate(R.menu.menu, menu);
        return true;
    }

    protected void showProgressDialog(String msg){
        try {
            if(mProgressDialog == null){
                mProgressDialog = ProgressDialog.show(this, "",
                        msg, true, false);
            }else{
                mProgressDialog.show();
            }
        } catch (Exception e) {}
    }

    protected void dismissProgressDialog(){
        if(mProgressDialog!=null && mProgressDialog.isShowing()){
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {}
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(getClass().getSimpleName().equals(ConfigActivity.class.getSimpleName())) {
            menu.findItem(R.id.config).setVisible(false);
            menu.findItem(R.id.del_all).setVisible(false);
        } else {
            menu.findItem(R.id.config).setVisible(true);
            menu.findItem(R.id.del_all).setVisible(true);
        }

        menu.findItem(R.id.about).setVisible(true);

        if(getClass().getSimpleName().equals(AboutPage.class.getSimpleName())) {
            menu.findItem(R.id.config).setVisible(false);
            menu.findItem(R.id.del_all).setVisible(false);
            menu.findItem(R.id.about).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.config:
                menuStartConfigActivity();
                break;

            case R.id.del_all:
                menuDeleteAll();
                break;

            case R.id.about:
                menuAbout();
                break;

            case android.R.id.home:
                if(!getClass().getSimpleName().equals(NotiListActivity.class.getSimpleName())) {
                    finish();
                }
                break;

            default:
                break;
        }
        return true;
    }

    protected void menuDeleteAll() {}

    protected void menuStartConfigActivity() {
        Intent i = new Intent(this, ConfigActivity.class);
        startActivity(i);
    }

    private void menuAbout() {
        Intent i = new Intent(this, AboutPage.class);
        startActivity(i);
    }
}
