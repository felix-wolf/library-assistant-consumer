public class Main {


    public static void main(String[] args) {
        System.out.println("start");
        //new LogFileReader();
        try {
            KafkaConsumerExample.runConsumer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
