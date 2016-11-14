package com.tools.joe.notiman.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tools.joe.notiman.R;
import com.tools.joe.notiman.data.GlobalPref;
import com.tools.joe.notiman.di.ui.NotiListActivity.*;
import com.tools.joe.notiman.model.Constant;
import com.tools.joe.notiman.model.DrawableMap;
import com.tools.joe.notiman.model.NotiInfo;
import com.tools.joe.notiman.model.Utils;
import com.tools.joe.notiman.ui.guester.CustomSwipeDetector;
import com.tools.joe.notiman.ui.guester.GuestureChecker;
import com.tools.joe.notiman.ui.list.ListAdapter;

import java.util.List;

import javax.inject.Inject;

public class NotiListActivity extends BaseActivity implements NotiListView {
    private final int NO_PERMISSION = 1;
    private boolean mReturnFromPerm = false;
    NotiListActivityComponent mComponent;

    @Inject
    LocalBroadcastManager mLbm;

    @Inject
    NotiListPresenter mPresenter;

    @Inject
    CustomSwipeDetector mCustomSwipeDetector;

    @Inject
    DrawableMap mDrawableMap;

    @Inject
    GlobalPref mGlobalPref;

    private NotiInfoReceiver mNotiReceiver;
    private ListView mListView;
    private NotiListAdapter<NotiInfo> mNotiListAdapter;
    private RelativeLayout mLlSwipeTip;
    private CheckBox mCbKonwn;
    private LinearLayout mLlNoNotiTip;

    @Override
    protected int getLayout() {
        return R.layout.activity_noti_list;
    }

    @Override
    public void inject() {
        mComponent = DaggerNotiListActivityComponent.builder()
                        .appComponent(getAppComponent())
                        .notiListActivityModule(new NotiListActivityModule(this))
                        .build();
        mComponent.inject(this);
    }

    @Override
    protected void actionBarConfig(ActionBar ab) {
        ab.setTitle(R.string.title_intercepted_noti);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_app);
    }

    @Override
    protected void menuDeleteAll() {
        super.menuDeleteAll();

        mPresenter.removeAll();
    }

    @Override
    public void initActivity() {
        mPresenter.setContext(this);
        mPresenter.setView(this);
        mPresenter.setGlobalPref(mGlobalPref);

        mLlSwipeTip = (RelativeLayout) findViewById(R.id.ll_swipe_tip);
        mCbKonwn = (CheckBox) findViewById(R.id.cb_konw);
        mCbKonwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.markSwipeTipKnown();
            }
        });

        mLlNoNotiTip = (LinearLayout) findViewById(R.id.ll_no_noti_tip);

        mNotiListAdapter = new NotiListAdapter<>(this, mPresenter.getNotiList(true));
        mListView = (ListView) findViewById(R.id.lv_noti);
        mListView.setAdapter(mNotiListAdapter);

        GuestureChecker checker = new GuestureChecker() {
            @Override
            public boolean isCaught(float initialX, float initialY, float lastX, float lastY) {
                final float deltaX = lastX - initialX;
                final float screenWidth = NotiListActivity.this.getWindowManager().getDefaultDisplay().getWidth();
                if(deltaX < 0 && Math.abs(deltaX)>(float)(screenWidth/12)) {
                    return true;
                }
                return false;
            }
        };
        mCustomSwipeDetector.setChecker(checker);

        mListView.setOnTouchListener(mCustomSwipeDetector);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<NotiInfo> list = mPresenter.getNotiList(false);
                final NotiInfo notiInfo = list.get(position);

                if(mCustomSwipeDetector.caughtSwipe()) {
                    view.startAnimation(getRemoveItemAnimation(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mPresenter.remove(notiInfo);
                        }

                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    }));
                } else {
                    mPresenter.clickItem(notiInfo);
                }
            }
        });

        mNotiReceiver = new NotiInfoReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_NOTI_INTERCEPT);
        mLbm.registerReceiver(mNotiReceiver, intentFilter);

        if(mPresenter.hasNotiPerm()) {
            mPresenter.start();
            mPresenter.waitPackageListInitDone();
        } else {
            mReturnFromPerm = true;
            showDialog(NO_PERMISSION);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mPresenter.hasNotiPerm() && mReturnFromPerm) {
            mReturnFromPerm = false;
            mPresenter.start();
            mPresenter.waitPackageListInitDone();
        } else if(!mPresenter.hasNotiPerm()) {
            mReturnFromPerm = true;
            showDialog(NO_PERMISSION);
        }
    }

    private AnimationSet getRemoveItemAnimation(Animation.AnimationListener al) {
        TranslateAnimation ta = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_PARENT,0.0f,
                TranslateAnimation.RELATIVE_TO_PARENT,-1.0f,
                TranslateAnimation.RELATIVE_TO_PARENT,0.0f,
                TranslateAnimation.RELATIVE_TO_PARENT,0.0f);
        ta.setInterpolator(new AccelerateInterpolator());
        ta.setRepeatCount(0);
        ta.setDuration(Constant.ITEM_DEL_ANIM_DURATION);
        ta.setAnimationListener(al);

        AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
        aa.setDuration(Constant.ITEM_DEL_ANIM_DURATION);
        aa.setRepeatCount(0);

        AnimationSet as = new AnimationSet(false);
        as.addAnimation(ta);
        as.addAnimation(aa);

        return as;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLbm.unregisterReceiver(mNotiReceiver);
        mNotiReceiver = null;

        mPresenter.setContext(null);
        mPresenter.setView(null);
        if(isFinishing()) {
            mPresenter.setGlobalPref(null);
            mComponent = null;
        }
    }

    @Override
    public void updateListView() {
        mNotiListAdapter.notifyDataSetChanged();
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case NO_PERMISSION:
                return new AlertDialog.Builder(this)
                        .setTitle(R.string.perm_dialog_title)
                        .setMessage(R.string.perm_dialog_content)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        mPresenter.go2OpenNotiPerm();
                                    }
                                })
                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
									    finish();
                                    }
                                }).create();
        }
        return null;
    }

    class NotiInfoReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(TextUtils.isEmpty(action)
                    || !action.equals(Constant.ACTION_NOTI_INTERCEPT)) {
                return;
            }

            mPresenter.onNewNotiReceived();
        }
    }

    class ViewHolder {
        ImageView iv;
        TextView  tvTitle;
        TextView  tvContent;
        TextView  tvTime;
    }

    class NotiListAdapter<T> extends ListAdapter {
        public NotiListAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder vh;

            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_noti, null);

                ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
                TextView  title = (TextView) convertView.findViewById(R.id.title);
                TextView  content = (TextView) convertView.findViewById(R.id.content);
                TextView  time = (TextView) convertView.findViewById(R.id.time);

                vh = new ViewHolder();
                vh.iv = iv;
                vh.tvTitle = title;
                vh.tvContent = content;
                vh.tvTime = time;

                convertView.setTag(vh);
            } else {
                vh = (ViewHolder)convertView.getTag();
            }



            NotiInfo notiInfo = (NotiInfo) mData.get(position);
            if(notiInfo != null) {
                if(!TextUtils.isEmpty(notiInfo.title)) {
                    vh.tvTitle.setText(notiInfo.title);
                }
                if(!TextUtils.isEmpty(notiInfo.content)) {
                    vh.tvContent.setText(notiInfo.content);
                }
                vh.tvTime.setText(Utils.ts2String(notiInfo.time));

                Drawable icon = mDrawableMap.get(notiInfo.packageName);
                if(icon != null) {
                    vh.iv.setImageDrawable(icon);
                }
            }


            return convertView;
        }
    }

    @Override
    public void showWaiting() {
        showProgressDialog(getResources().getString(R.string.waiting));
    }

    @Override
    public void dismissWaiting() {
        dismissProgressDialog();
    }

    @Override
    public void showSwipeTip(boolean show) {
        int v = show ? View.VISIBLE : View.GONE;
        mLlSwipeTip.setVisibility(v);
    }

    @Override
    public void showNoNotiTip(boolean show) {
        int v = show ? View.VISIBLE : View.GONE;
        mLlNoNotiTip.setVisibility(v);
    }

    @Override
    public void go2Setting() {
        menuStartConfigActivity();
    }
}
