package com.tools.joe.notiman.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tools.joe.notiman.R;
import com.tools.joe.notiman.di.ui.ConfigActivity.*;
import com.tools.joe.notiman.model.AppInfo;
import com.tools.joe.notiman.model.DrawableMap;
import com.tools.joe.notiman.ui.list.ListAdapter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by joe_wang on 2016/8/1.
 */
public class ConfigActivity extends BaseActivity implements ConfigView, ActionBar.OnNavigationListener {

    ConfigActivityComponent mComponent;
    @Inject
    DrawableMap mDrawableMap;

    private ListView mListView;
    private PackageListAdapter<AppInfo> plAdapter;
    private CheckBox mCbAll, mCbNone, mCbReverse;


    @Inject
    public static ConfigPresenter mConfigPresenter;

    @Override
    protected int getLayout() {
        return R.layout.activity_conf;
    }

    @Override
    public void inject() {
        mComponent = DaggerConfigActivityComponent.builder()
                .appComponent(getAppComponent())
                .configActivityModule(new ConfigActivityModule(this))
                .build();
        mComponent.inject(this);
    }

    @Override
    protected void actionBarConfig(ActionBar ab) {
        ab.setTitle(R.string.title_select_app);

        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(this,
                R.array.app_category, R.layout.spinner_item);

        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ab.setListNavigationCallbacks(list, this);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initActivity() {
        plAdapter = new PackageListAdapter(this,
                mConfigPresenter.getAppInfoList());

        mCbAll = (CheckBox) findViewById(R.id.all);
        mCbNone = (CheckBox) findViewById(R.id.none);
        mCbReverse = (CheckBox) findViewById(R.id.reverse);

        mListView = (ListView) findViewById(R.id.lv_package);
        mListView.setAdapter(plAdapter);

        mConfigPresenter.setContext(this);
        mConfigPresenter.setView(this);
//        mConfigPresenter.getInstalledPackage(0);
        mConfigPresenter.waitPackageListInitDone();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        plAdapter = null;
        mConfigPresenter.setContext(null);
        mConfigPresenter.setView(null);
        if(isFinishing()) {
            mConfigPresenter = null;
            mComponent = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConfigPresenter.saveChanges();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all:
                mConfigPresenter.selectAll();
                break;
            case R.id.none:
                mConfigPresenter.selectNone();
                break;
            case R.id.reverse:
                mConfigPresenter.selectReverse();
                break;
            default:
                break;
        }
    }

    @Override
    public void refresh() {
        plAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        showProgressDialog(getResources().getString(R.string.waiting));
    }

    @Override
    public void hideProgress() {
        dismissProgressDialog();
    }

    @Override
    public void selectAll() {
        mCbAll.setChecked(true);
        mCbNone.setChecked(false);
        mCbReverse.setChecked(false);

        refresh();
    }

    @Override
    public void selectNone() {
        mCbAll.setChecked(false);
        mCbNone.setChecked(true);
        mCbReverse.setChecked(false);

        refresh();
    }

    @Override
    public void selectReverse() {
        mCbAll.setChecked(false);
        mCbNone.setChecked(false);
        mCbReverse.setChecked(true);

        refresh();
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        mConfigPresenter.getInstalledPackage(itemPosition);
        return true;
    }

    private void clearOptionBar() {
        mCbAll.setChecked(false);
        mCbNone.setChecked(false);
        mCbReverse.setChecked(false);
    }

    class ViewHolder {
        ImageView mIv;
        TextView  mTv;
        CheckBox  mCk;
    }

    class PackageListAdapter<T> extends ListAdapter {
        public PackageListAdapter(Context context, List<T> list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            final AppInfo ai = (AppInfo) mData.get(position);
            if(ai == null) {
                return null;
            }

            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_app, null);

                final ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
                final TextView tv = (TextView) convertView.findViewById(R.id.appname);
                final CheckBox ck = (CheckBox) convertView.findViewById(R.id.check);

                viewHolder = new ViewHolder();
                viewHolder.mIv = iv;
                viewHolder.mTv = tv;
                viewHolder.mCk = ck;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }

            Drawable d = mDrawableMap.get(ai.packageName);
            if(d != null) {
                viewHolder.mIv.setImageDrawable(d);
            }
            if(!TextUtils.isEmpty(ai.simpleName)) {
                viewHolder.mTv.setText(ai.simpleName);
            }
            viewHolder.mCk.setChecked(ai.select);

            final int pos = position;
            viewHolder.mCk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearOptionBar();

                    CheckBox c = (CheckBox)v;
                    if(c.isChecked()) {
                        mConfigPresenter.addMonitorApp(ai.packageName, pos);
                    } else {
                        mConfigPresenter.deleteMonitorApp(ai.packageName, pos);
                    }
                }
            });

            final ViewHolder fvh = viewHolder;

            RelativeLayout layout = (RelativeLayout)convertView.findViewById(R.id.rl_container);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearOptionBar();

                    boolean checked = !fvh.mCk.isChecked();
                    fvh.mCk.setChecked(checked);

                    if(checked) {
                        mConfigPresenter.addMonitorApp(ai.packageName, pos);
                    } else {
                        mConfigPresenter.deleteMonitorApp(ai.packageName, pos);
                    }
                }
            });

            return convertView;
        }
    }
}
