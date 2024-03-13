package utils;

import rpcprotocols.ContestClientRpcWorker;
import service.IService;

import java.net.Socket;

public class ContestRpcConcurrentServer extends AbstractConcurrentServer{
    private IService contestServer;

    public ContestRpcConcurrentServer(int port, IService triatlonServer) {
        super(port);
        this.contestServer = triatlonServer;
        System.out.println("Chat- TriatlonRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ContestClientRpcWorker worker =new ContestClientRpcWorker(contestServer, client);

        Thread tw= new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
