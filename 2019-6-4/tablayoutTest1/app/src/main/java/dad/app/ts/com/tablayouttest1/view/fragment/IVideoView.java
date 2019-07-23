package dad.app.ts.com.tablayouttest1.view.fragment;

import android.widget.SimpleAdapter;

public interface IVideoView {
    public void setAdapter(SimpleAdapter simpleAdapter);

    public void setValue(int value);

    public void showMessage(String message);

    public void setProgressMode(boolean flag);

    public void error();
}
