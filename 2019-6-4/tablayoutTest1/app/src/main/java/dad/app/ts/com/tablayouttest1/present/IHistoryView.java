package dad.app.ts.com.tablayouttest1.present;

import dad.app.ts.com.tablayouttest1.adatper.CollectNewsAdapter;

public interface IHistoryView {
    public void setAdapter(CollectNewsAdapter collectNewsAdapter);

    public void showMessage(String message);
}
