public class SecondLamp extends Thread{
    public void run(){
        new Thread(new TalkSender(1111,"localhost",8888,2222)).start();
    }
}