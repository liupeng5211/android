package dad.app.ts.com.tablayouttest1.present;

import dad.app.ts.com.tablayouttest1.adatper.CollectNewsAdapter;
import dad.app.ts.com.tablayouttest1.model.CollectNews;

public interface ICollectView {
    public void setAdapter(CollectNewsAdapter adapter);

    public void tip(String message);

    public void refreshAdapter();
}
