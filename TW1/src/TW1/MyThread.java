package TW1;

public class MyThread extends Thread{
    private Counter counter;
    MyThread(Counter counter){
        this.counter = counter;
    }

    public void run(){
        for(int i = 0; i < 5; i++){
            int currentCounter = this.counter.getValue();//read
            currentCounter++;//increment
            this.counter.setValue(currentCounter);//write
        }
    }
}
