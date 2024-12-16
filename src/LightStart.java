
public class LightStart {
    public static void main(String[] args) {
        FirstLamp firstLamp = new FirstLamp();
        SecondLamp secondLamp = new SecondLamp();
        ThirdLamp thirdLamp = new ThirdLamp();
        firstLamp.start();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        secondLamp.start();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thirdLamp.start();
    }
}
