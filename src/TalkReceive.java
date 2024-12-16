
import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.swing.JButton;

public class TalkReceive implements Runnable{
    DatagramSocket socket=null;
    public int port;
    private String msgFrom;
    public JButton workButton;
    public JButton stopButton;
    public JButton switchButton;
    public TalkReceive(int port,String msgFrom,JButton workButton,JButton stopButton,JButton switchButton) {
        this.port = port;
        this.msgFrom = msgFrom;
        this.workButton=workButton;
        this.stopButton=stopButton;
        this.switchButton=switchButton;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        //准备接收包裹

        while (true){
            try {
                byte[] container = new byte[1024];
                DatagramPacket packet = new DatagramPacket(container,0,container.length);
                socket.receive(packet);//阻塞式接收包裹
                //断开连接判断 bye
                // byte[] data = packet.getData();
                String datas = new String(packet.getData(), 0,packet.getLength());
                System.out.println(datas);
                if(datas.contains("open")){
                    switchButton.setBackground(Color.YELLOW);
                    stopButton.setBackground(Color.WHITE);
                    workButton.setBackground(Color.GREEN);
                }else {
                    switchButton.setBackground(Color.WHITE);
                    stopButton.setBackground(Color.RED);
                    workButton.setBackground(Color.WHITE);
                }

                //发送端多发送了一个空行换位符，所以if里是false，要加个清除换位符，就可以实现关闭连接！
                if (datas.trim().equals("bye")){
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        socket.close();
    }
}


