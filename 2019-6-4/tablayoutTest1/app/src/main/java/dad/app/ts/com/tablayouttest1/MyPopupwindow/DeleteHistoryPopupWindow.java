package dad.app.ts.com.tablayouttest1.MyPopupwindow;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import dad.app.ts.com.tablayouttest1.R;
import dad.app.ts.com.tablayouttest1.present.ConnectSeverPresent;
import dad.app.ts.com.tablayouttest1.present.ICollectView;
import dad.app.ts.com.tablayouttest1.util.CollectDbHelper;
import dad.app.ts.com.tablayouttest1.util.DbControl;
import dad.app.ts.com.tablayouttest1.util.HistoryDbHelper;

public class DeleteHistoryPopupWindow extends PopupWindow {
    private View conentView;
    private Context mContext;
    private Bundle mBundle;
    private static final String TAG = "popwindow";
    private static final String USERDATA = "userdata";
    private SharedPreferences mSharedPreferences;
    private HistoryDbHelper mDbHelper;
    private SQLiteDatabase mDatabase;
    private ICollectView mCollectView;

    public DeleteHistoryPopupWindow(final Activity context, final Bundle bundle, ICollectView view) {

        this.mContext = context;
        this.mBundle = bundle;
        this.mCollectView = view;
        final String title = bundle.getString("title");
        final String content = bundle.getString("content");
        Log.d(TAG, "title: " + title);
        mSharedPreferences = mContext.getSharedPreferences(USERDATA, 0);
        final String username = mSharedPreferences.getString("username", "");
        if (username.equals("")) {
            view.tip("请先登录！");
            return;
        }
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.delete_popup_window_view, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xbbbbbbbb);
//        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.AnimationPreview);
        mDbHelper = new HistoryDbHelper(mContext, "history_news", null, 1);
        mDatabase = mDbHelper.getReadableDatabase();

        TextView deleteTv = conentView.findViewById(R.id.delete);
        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                int count = mDatabase.delete("history_news", "title = ? and user = ?", new String[]{title, username});
                Log.d(TAG, "count: " + count);
                mCollectView.refreshAdapter();
                dismiss();
            }
        });


    }

    /**
     * 显示popupWindow
     *
     * @param parent parent view.
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
//            this.showAtLocation(parent, 0, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
