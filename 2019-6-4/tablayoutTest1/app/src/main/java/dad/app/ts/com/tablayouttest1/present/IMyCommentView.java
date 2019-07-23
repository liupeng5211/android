package dad.app.ts.com.tablayouttest1.present;

import dad.app.ts.com.tablayouttest1.adatper.MyCommentAdapter;

public interface IMyCommentView {
    public void setAdapter(MyCommentAdapter commentAdapter);

    public void showMesage(String text);
}
