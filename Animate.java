public class Animate implements Runnable{
    private Screen sc;

    public Animate(Screen sc){
        this.sc = sc;
    }
    public void run(){
        while(true){
            try{
                Thread.sleep(20);
            }catch(InterruptedException ex){
                Thread.currentThread().interrupt();
            }
            sc.repaint();
        }
    }
}