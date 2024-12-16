public class ThirdLamp extends Thread{
    public void run(){
        new Thread(new TalkSender(3333,"localhost",8888,4444)).start();
    }
}
