package dad.app.ts.com.tablayouttest1.model;

interface ICommentHandle {
    public void comment(CommentHandle.Commentlocalback commentlocalback, String title, String content);

    public void getCommentFromServer(CommentHandle.Commentlocalback commentlocalback, String title);
}
