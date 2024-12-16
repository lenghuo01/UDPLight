
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TalkSender implements Runnable{
    DatagramSocket socket=null;
    BufferedReader reader=null;
    private int fromPort;
    private String toIP;
    private int getPort;
    private int toPort;
    public  int t;
    public  int h;
    public  int l;
    public   JLabel temperatureLabel;
    public  JLabel humidityLabel;
    public  JLabel lightIntensityLabel;
    public  JButton switchButton;
    public  JButton workButton;
    public  JButton stopButton;
    public TalkSender(int fromPort, String toIP, int toPort,int getPort) {
        this.fromPort = fromPort;
        this.toIP = toIP;
        this.toPort = toPort;
        this.getPort=getPort;
        try {
            socket = new DatagramSocket(fromPort);
            reader = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private  void placeComponents(JPanel panel) {
        panel.setLayout(null);

        temperatureLabel = new JLabel("温度：");
        temperatureLabel.setBounds(10, 20, 80, 25);
        panel.add(temperatureLabel);

        humidityLabel = new JLabel("湿度：");
        humidityLabel.setBounds(10, 50, 80, 25);
        panel.add(humidityLabel);

        lightIntensityLabel = new JLabel("光照强度：");
        lightIntensityLabel.setBounds(10, 80, 120, 25);
        panel.add(lightIntensityLabel);

        JLabel deviceAddressLabel = new JLabel("设备id：localhost:"+this.getPort);
        deviceAddressLabel.setBounds(10, 110, 200, 25);
        panel.add(deviceAddressLabel);

        switchButton = new JButton("灯");
        switchButton.setBounds(180, 10, 100, 100);
        panel.add(switchButton);

        workButton = new JButton("工作");
        workButton.setBounds(140, 140, 80, 25);
        panel.add(workButton);

        stopButton = new JButton("停止");
        stopButton.setBounds(230, 140, 80, 25);
        panel.add(stopButton);

        switchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在这里添加开关按钮的功能
            }
        });

        workButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在这里添加工作按钮的功能
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在这里添加停止按钮的功能
            }
        });
    }
    @Override
    public void run() {
        JFrame frame = new JFrame("传感器数据");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
        new Thread(new TalkReceive(this.getPort,"学生", workButton,stopButton,switchButton)).start();
        while (true) {
            try {
//                String data = reader.readLine();
                Random random = new Random();
                int temperature = random.nextInt(16) + 10;
                int humidity = random.nextInt(16) + 10;
                int lightIntensity = random.nextInt(20) + 10;
                t=temperature;
                h=humidity;
                l=lightIntensity;
                temperatureLabel.setText("温度：" + temperature+"℃");
                humidityLabel.setText("湿度：" + humidity+"%");
                lightIntensityLabel.setText("光照强度：" + lightIntensity+"%");
                String data = String.format("%d/%d/%d/%s", t, h, l,this.getPort);
                byte[] datas = data.getBytes();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DatagramPacket packet = new DatagramPacket(datas, 0, datas.length, new InetSocketAddress(this.toIP, this.toPort));
                socket.send(packet);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (data.equals("bye")) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        socket.close();
    }
}

