package dad.app.ts.com.tablayouttest1.model;

import android.graphics.Bitmap;

public class User {
    private String mUserName;
    private String mUesrAge;
    private String mUserSex;
    private Bitmap mImage;

    public Bitmap getmImage() {
        return mImage;
    }

    public void setmImage(Bitmap mImage) {
        this.mImage = mImage;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmUesrAge() {
        return mUesrAge;
    }

    public void setmUesrAge(String mUesrAge) {
        this.mUesrAge = mUesrAge;
    }

    public String getmUserSex() {
        return mUserSex;
    }

    public void setmUserSex(String mUserSex) {
        this.mUserSex = mUserSex;
    }
}
