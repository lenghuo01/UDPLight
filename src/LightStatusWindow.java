
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
public class LightStatusWindow {

    public static void main(String[] args) {

        JFrame frame = new JFrame("路灯状态");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }
    public static JLabel title;
    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        title = new JLabel("路灯id: xx");
        title.setBounds(100, 20, 200, 25);
        panel.add(title);

        JLabel temperatureLabel = new JLabel("温度： xx");
        temperatureLabel.setBounds(10, 50, 80, 25);
        panel.add(temperatureLabel);
//
        JLabel humidityLabel = new JLabel("湿度： xx");
        humidityLabel.setBounds(10, 80, 80, 25);
        panel.add(humidityLabel);

        JLabel brightnessLabel = new JLabel("亮度： xx");
        brightnessLabel.setBounds(10, 110, 80, 25);
        panel.add(brightnessLabel);

        JButton startServerButton = new JButton("开启服务器");
        startServerButton.setBounds(10, 140, 120, 25);
        panel.add(startServerButton);

        JButton turnOnLightButton = new JButton("开灯");
        turnOnLightButton.setBounds(140, 140, 120, 25);
        panel.add(turnOnLightButton);
        turnOnLightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("开灯");
            }
        });

        JButton turnOffLightButton = new JButton("关灯");
        turnOffLightButton.setBounds(140, 170, 120, 25);
        panel.add(turnOffLightButton);

        JLabel shangchuan = new JLabel("上传数据包列表");
        shangchuan.setBounds(10, 200, 120, 25);
        panel.add(shangchuan);

        JLabel xiafa = new JLabel("下发数据包列表");
        xiafa.setBounds(200, 200, 120, 25);
        panel.add(xiafa);
        // 添加带滚动条的文本框
        JTextArea textArea1 = new JTextArea();
        textArea1.setLineWrap(true); // 设置自动换行
        textArea1.setWrapStyleWord(true); // 设置单词换行
        textArea1.setFont(new Font("宋体", Font.PLAIN, 16)); // 设置字体大小为16
        JScrollPane scrollPane1 = new JScrollPane(textArea1);
        scrollPane1.setBounds(10, 226, 180, 80);
        panel.add(scrollPane1);

        JTextArea textArea2 = new JTextArea();
        textArea2.setLineWrap(true); // 设置自动换行
        textArea2.setWrapStyleWord(true); // 设置单词换行
        textArea2.setFont(new Font("宋体", Font.PLAIN, 16)); // 设置字体大小为16
        JScrollPane scrollPane2 = new JScrollPane(textArea2);
        scrollPane2.setBounds(191, 226, 180, 80);
        panel.add(scrollPane2);

        turnOffLightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("关灯");
            }
        });

        startServerButton.addActionListener(new ActionListener() {
            int tem = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("开启服务");
                DatagramSocket socket=null;
                try {
                    socket = new DatagramSocket(8888);
                } catch (SocketException socketException) {
                    socketException.printStackTrace();
                }
                while (true) {
                    tem++;
                    System.out.println(tem);
                    try {

                        byte[] container = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(container, 0, container.length);
                        socket.receive(packet);//阻塞式接收包裹

                        byte[] data = packet.getData();
                        String datas = new String(data, 0, data.length);
                        // this.data1 = datas;
                        String[] result = datas.split("/");
                        double num = Double.parseDouble(result[2]); // 转换为浮点数

                        int toport = Integer.parseInt(result[3].trim());
                        title.setText("路灯id: localhost:" + result[3]);

                        temperatureLabel.setText("温度：" + result[0] + "℃");
                        humidityLabel.setText("湿度：" + result[1] + "%");
                        brightnessLabel.setText("亮度：" + result[2] + "%");
                        textArea1.append((datas).trim()+"    ");


                        DatagramSocket socket1 = new DatagramSocket(7777);
                        String toData2 = "close";
                        byte[] data2 = toData2.getBytes();
                        String toData3 = "open";
                        byte[] data3 = toData3.getBytes();
                        DatagramPacket packet2 = new DatagramPacket(data2, 0, data2.length, new InetSocketAddress("localhost", toport));
                        DatagramPacket packet3 = new DatagramPacket(data3, 0, data3.length, new InetSocketAddress("localhost", toport));

                        if (20 < num) {
                            socket.send(packet2);
                            turnOnLightButton.setBackground(Color.WHITE);

                            turnOffLightButton.setBackground(Color.GREEN);
                            textArea2.append(toData2+"                ");
                        } else {
                            socket.send(packet3);
                            turnOnLightButton.setBackground(Color.GREEN);

                            turnOffLightButton.setBackground(Color.WHITE);
                            textArea2.append(toData3+"                ");
                        }


                        socket1.close();

                        // if(num==100)
                        break;

                    } catch (IOException ev) {
                        ev.printStackTrace();
                        System.out.println("error");
                    }

                }

                socket.close();
            }
        });


    }
}
