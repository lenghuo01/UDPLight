public class FirstLamp extends Thread{
    public void run(){
        new Thread(new TalkSender(6666,"localhost",8888,9999)).start();
    }

}
