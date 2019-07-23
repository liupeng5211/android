package dad.app.ts.com.tablayouttest1.view.fragment;

import android.widget.SimpleAdapter;

public  interface FragmentView {
    public void setAdapter(SimpleAdapter simpleAdapter);
    public void setProgress(int value);
    public void hideProgress();
    public void showProgress();
    public void setProgressMode(boolean flag);
}
