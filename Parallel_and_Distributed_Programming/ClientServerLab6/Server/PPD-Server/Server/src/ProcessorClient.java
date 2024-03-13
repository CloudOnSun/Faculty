import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ProcessorClient implements Runnable{
    Socket clientSocket;
    BlockingQueue coadaCitire;
    FinalList listaRezultate;
    IdLocksMap idLocksMap;
    ExecutorService pool;

    public ProcessorClient(Socket clientSocket, BlockingQueue coadaCitire, FinalList listaRezultate, IdLocksMap idLocksMap, ExecutorService pool) {
        this.clientSocket = clientSocket;
        this.coadaCitire = coadaCitire;
        this.listaRezultate = listaRezultate;
        this.idLocksMap = idLocksMap;
        this.pool = pool;
    }

    @Override
    public void run() {

    }
}
