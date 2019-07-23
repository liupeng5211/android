package dad.app.ts.com.tablayouttest1.present;

import android.graphics.Bitmap;

import dad.app.ts.com.tablayouttest1.adatper.CommentAdapter;

public interface ICommentView {
    public void addItem(CommentAdapter commentAdapter);

    public void setAdapter(CommentAdapter commentAdapter);
}
