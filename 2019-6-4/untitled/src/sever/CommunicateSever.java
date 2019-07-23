package sever;

import daywork.DayHandlePicture;
import daywork.DayHandleVideo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CommunicateSever extends Thread {
    public static void main(String ag[]) {
        new Thread(
                () -> {
                    DayHandleVideo.dayOfVideoWork();
                    DayHandlePicture.dayOfPictureWork();
                }
        ).start();

        CommunicateSever communicateSever = new CommunicateSever();
        communicateSever.start();
        // new Db().getConnection();
    }

    public void run() {
        System.out.println("服务器已经启动");
        try {
            // 创建绑定到特定端口的服务器套接字 1-65535
            ServerSocket serversocket4 = new ServerSocket(62224);
            // System.out.println("服务器端口"+serversocket.getLocalPort());
            // serversocket.getLocalPort();
            while (true) {
                // 建立连接，获取socket对象
                try {
                    Socket socket4 = serversocket4.accept();
                    System.out.println("有客户端连接到服务器" + socket4.getInetAddress().getHostAddress());
                    TransferThread registerThread = new TransferThread(socket4);
                    registerThread.start();


                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
