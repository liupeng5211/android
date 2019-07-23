package dad.app.ts.com.tablayouttest1.present;

import android.content.Context;
import dad.app.ts.com.tablayouttest1.adatper.CommentAdapter;
import dad.app.ts.com.tablayouttest1.model.CommentHandle;

public class CommentPresenter implements ICommentPresenter, CommentHandle.Commentlocalback {
    private Context mContext;
    private ICommentView iCommentView;
    private CommentHandle mCommentHandle;

    public CommentPresenter(Context mContext, ICommentView iCommentView) {
        this.mContext = mContext;
        this.iCommentView = iCommentView;
        mCommentHandle = new CommentHandle(mContext);
    }

    @Override
    public void add(String title, String content) {
        mCommentHandle.comment(this, title, content);
    }

    @Override
    public void getComment(String title) {
        mCommentHandle.getCommentFromServer(this, title);

    }

    @Override
    public void success(CommentAdapter commentAdapter) {
        iCommentView.addItem(commentAdapter);

    }

    @Override
    public void setAdapter(CommentAdapter commentAdapter) {
        iCommentView.setAdapter(commentAdapter);

    }
}
//不锈钢兔子拍出天价