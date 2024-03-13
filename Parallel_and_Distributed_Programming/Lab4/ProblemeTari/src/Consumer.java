public class Consumer extends Thread {
    private BlockingQueue coada;
    private FinalList rezultateFinale;

    public Consumer(BlockingQueue coada, FinalList rezultateFinale) {
        this.coada = coada;
        this.rezultateFinale = rezultateFinale;
    }

    @Override
    public void run() {
        while (true) {
            Node n = coada.get();
            if (n != null)
                rezultateFinale.add(n);
            else
                break;
        }
    }
}