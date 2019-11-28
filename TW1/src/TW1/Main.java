package TW1;

public class Main {

    public static void main(String[] args) {
        int n = 1000000;
        Counter counter = new Counter(0);
        MyThread[] myThreads = new MyThread[n];
	    for(int i = 0; i < n; i++){
	        myThreads[i] = new MyThread(counter);
	        myThreads[i].start();
        }
        try {
            for (int i = 0; i < n; i++) {
                myThreads[i].join();
            }
        }
        catch(InterruptedException e){
                e.printStackTrace();
        }
        System.out.println(counter.getValue());
    }
}
