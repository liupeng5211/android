package dad.app.ts.com.tablayouttest1.present;

import android.content.Context;

import dad.app.ts.com.tablayouttest1.adatper.MyCommentAdapter;
import dad.app.ts.com.tablayouttest1.model.MyComment;

public class MyCommentPresenter implements IMyCommentPresenter, MyComment.MyCommentLocalback {
    private Context mContext;
    private IMyCommentView mImyCommentView;
    private MyComment mComment;

    public MyCommentPresenter(Context mContext, IMyCommentView mImyCommentView) {
        this.mContext = mContext;
        this.mImyCommentView = mImyCommentView;
        mComment = new MyComment();
    }

    @Override
    public void success(MyCommentAdapter commentAdapter) {
        mImyCommentView.setAdapter(commentAdapter);
    }

    @Override
    public void error(String message) {
        mImyCommentView.showMesage(message);

    }

    @Override
    public void getData() {
        mComment.getAllComment(mContext, this);

    }
}
