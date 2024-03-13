package Florea_Flaviu_ISS.rpcprotocol;

import Florea_Flaviu_ISS.domain.*;
import Florea_Flaviu_ISS.service.IService;
import Florea_Flaviu_ISS.service.IServiceObserver;
import Florea_Flaviu_ISS.service.TeatruException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerRpcProxy implements IService {

    private String host;
    private int port;
    private IServiceObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServerRpcProxy(String host, int port) throws TeatruException {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
        initializeConnection();
    }

    @Override
    public Manager logIn(ManagerLogInDTO managerDTO, IServiceObserver client) throws TeatruException {
        Request request = new Request.Builder().type(RequestType.LOG_IN).data(managerDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new TeatruException(err);
        }
        this.client = client;
        Manager manager = (Manager) response.data();
        return manager;
    }

    @Override
    public List<Spectacol> filtruDupaGen(Genuri gen, IServiceObserver client) throws TeatruException {
        Request request = new Request.Builder().type(RequestType.FILTRE_SPEC).data(gen).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new TeatruException(err);
        }
        List<Spectacol> spectacols = (List<Spectacol>) response.data();
        return spectacols;
    }

    @Override
    public List<Spectacol> findAllSpectacole(IServiceObserver client) throws TeatruException {
        Request request = new Request.Builder().type(RequestType.FIND_ALL_SPEC).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new TeatruException(err);
        }
        this.client = client;
        List<Spectacol> spectacols = (List<Spectacol>) response.data();
        return spectacols;
    }

    @Override
    public void addSpectacol(SpectacolDTO spectacolDTO, IServiceObserver client) throws TeatruException {
        Request request = new Request.Builder().type(RequestType.ADD_SPEC).data(spectacolDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new TeatruException(err);
        }
    }

    @Override
    public void logOut(Manager manager, IServiceObserver client) throws TeatruException {
        Request req = new Request.Builder().type(RequestType.LOG_OUT).data(manager).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if(response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new TeatruException(err);
        }
    }

    @Override
    public void deleteSpectacol(Integer id, IServiceObserver client) throws TeatruException {
        Request req = new Request.Builder().type(RequestType.DEL_SPEC).data(id).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new TeatruException(err);
        }
    }

    @Override
    public void updateSpectacol(SpectacolUpdateDTO spectacolDTO, IServiceObserver client) throws TeatruException {
        Request req = new Request.Builder().type(RequestType.UP_SPEC).data(spectacolDTO).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new TeatruException(err);
        }
    }

    @Override
    public List<Loc> getLocuriSpectacol(Spectacol spectacol, IServiceObserver client) throws TeatruException {
        Request req = new Request.Builder().type(RequestType.FIND_LOCS).data(spectacol).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new TeatruException(err);
        }
        this.client = client;
        List<Loc> locuri = (List<Loc>) response.data();
        return locuri;
    }

    @Override
    public Integer addRezervare(RezervareDTO rezervareDTO, IServiceObserver client) throws TeatruException {
        Request req = new Request.Builder().type(RequestType.ADD_REZ).data(rezervareDTO).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new TeatruException(err);
        }
        Integer code = (Integer) response.data();
        return code;
    }

    @Override
    public void deleteRezervare(Integer cod, IServiceObserver client) throws TeatruException {
        Request req = new Request.Builder().type(RequestType.DEL_REZ).data(cod).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new TeatruException(err);
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws TeatruException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new TeatruException("Error sending object " + e);
        }
    }

    private Response readResponse() throws TeatruException {
        Response response=null;
        try{
            response=qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws TeatruException {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdateSpectacole(Response response){
        System.out.println("Handle update spectacole");
        try{
            client.updateSpectacole();
        } catch (TeatruException e) {
            e.printStackTrace();
        }
    }

    private void handleUpdateLocuri(Response response){
        System.out.println("Handle update locuri");
        try{
            client.updateLocuri();
        } catch (TeatruException e) {
            e.printStackTrace();
        }
    }

    private boolean isUpdateSpectacole(Response response){

        return response.type()== ResponseType.SPEC_ADDED;
    }

    private boolean isUpdateLocuri(Response response){

        return response.type() == ResponseType.LOCS_UPDATE;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    if (isUpdateSpectacole((Response)response)){
                        handleUpdateSpectacole((Response)response);
                    }
                    else if (isUpdateLocuri((Response)response)){
                        handleUpdateLocuri((Response) response);
                    }
                    else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
