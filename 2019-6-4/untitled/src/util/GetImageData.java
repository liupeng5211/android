package util;


import org.apache.commons.net.util.Base64;

import java.io.*;

public class GetImageData {


    public static String getImgStr(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
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

    public static void writeImgStr(String imagedata) {
        byte[] op = Base64.decodeBase64(imagedata);
        // System.out.println(new String(op));
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File("d:\\temp\\1.jpg"));
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

    public static void main(String arges[]) {
        String imagedata = GetImageData.getImgStr("e:\\test.png");
        System.out.println(imagedata);
//        GetImageData.writeImgStr(imagedata);

    }

}
