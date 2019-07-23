package dad.app.ts.com.tablayouttest1.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.util.Base64;

public class GetImageData {
    private Context mContext;

    public GetImageData(Context context) {
        this.mContext = context;
    }

    public String getImgStr(String imgFile) {


        InputStream in = null;
        byte[] data = null;

        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }

    public String getImgFile(File imgFile) {


        InputStream in = null;
        byte[] data = null;

        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }

    public void writeImgStr(String imagedata, String filename) {
        byte[] op = Base64.decodeBase64(imagedata);
        // System.out.println(new String(op));
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(String.valueOf(mContext.getDir(filename + ".jpg", mContext.MODE_PRIVATE))) + "/" + filename + ".jpg");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            fos.write(op, 0, op.length);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            fos.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
