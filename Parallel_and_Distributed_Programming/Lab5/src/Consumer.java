public class Consumer extends Thread {
    private BlockingQueue coada;
    private FinalList rezultateFinale;

    private IdLocksMap idLocksMap;

    public Consumer(BlockingQueue coada, FinalList rezultateFinale, IdLocksMap idLocksMap) {
        this.coada = coada;
        this.rezultateFinale = rezultateFinale;
        this.idLocksMap = idLocksMap;
    }

    @Override
    public void run() {
        while (true) {
            Node n = coada.get();
            if (n != null) {
                var mutex = idLocksMap.get(n.ID);
                mutex.lock();
                rezultateFinale.adaugaNod(n);
                mutex.unlock();
            }
            else
                break;
        }
    }
}