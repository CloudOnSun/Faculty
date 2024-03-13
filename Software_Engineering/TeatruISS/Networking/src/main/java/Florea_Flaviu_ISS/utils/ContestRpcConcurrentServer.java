package Florea_Flaviu_ISS.utils;

import Florea_Flaviu_ISS.rpcprotocol.ClientRpcWorker;
import Florea_Flaviu_ISS.service.IService;


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
        ClientRpcWorker worker =new ClientRpcWorker(contestServer, client);

        Thread tw= new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
