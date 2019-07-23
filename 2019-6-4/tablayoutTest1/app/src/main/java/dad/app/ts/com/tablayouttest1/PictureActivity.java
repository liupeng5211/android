package dad.app.ts.com.tablayouttest1;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import dad.app.ts.com.tablayouttest1.base.BaseActivity;

public class PictureActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bitmap bitmap =(Bitmap) getIntent().getParcelableExtra("bitmap");
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        setContentView(imageView);
    }
}
