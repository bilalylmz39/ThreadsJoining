import java.util.*;

public class Node extends Thread{
    String name;
    ArrayList<Node> waiting=new ArrayList<>();
    ArrayList<Node> whoIsWaitingForMe=new ArrayList<>();
    boolean isFinished=false;
    public Object lock=new Object();

    Random random = new Random();
    int noOfWait;
    public Node(String name ) {
        this.name=name;
    }


    public String writeWaitFunc(ArrayList<Node> waitFor ){
        String x="";
        for(Node n:waitFor){
            x+=n.name+" ";
        }
        return x;
    }

    public void noConditionOtherwise(){

            while (noOfWait>0 ) {
                synchronized (lock) {
                    System.out.println("Node" + this.name + " is waiting for Node " + this.writeWaitFunc(this.waiting));
                    try {
                        lock.wait(waiting.size()*2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
             perform();
    }
    public void block(/*Node node*/){ //diğer threadlerin buraya gelip locku açması lazım
        //synchronized (lock) {
            for (Node n : this.whoIsWaitingForMe) {
                n.noOfWait--;
                if (n.noOfWait == 0 && !isFinished) {
                    n.lock.notify();

                }
            }
   //    }

    }
    public void perform() {   //Node'ların outputu
        if (!this.isFinished) {
            System.out.println("Node" + this.name + " being started");
            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Node" +this.name + " is completed");
            this.isFinished=true;
            block();

        }
    }
    @Override
    public void run() {
        noConditionOtherwise();
    }

}
