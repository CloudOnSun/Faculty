package rpcprotocols;

import concurs.domain.*;
//import domain.*;
import service.ContestException;
import service.IService;
import service.IServiceObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ContestServicesRpcProxy implements IService {

    private String host;
    private int port;
    private IServiceObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ContestServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }

    @Override
    public Admin getAccount(AdminLogInDTO adminLogInDTO, IServiceObserver client) throws ContestException {
        initializeConnection();
        Request request = new Request.Builder().type(RequestType.LOG_IN).data(adminLogInDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new ContestException(err);
        }
        this.client = client;
        Admin admin = (Admin) response.data();
        return admin;
    }

    @Override
    public ContWithRaceDTOList getByRace(RaceDTO race, IServiceObserver client) throws ContestException {
        Request request = new Request.Builder().type(RequestType.FIND_CONTS).data(race).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ContestException(err);
        }
        ContWithRaceDTOList contsDTO = (ContWithRaceDTOList) response.data();
        return contsDTO;
    }

    @Override
    public RaceNrContDTOList nrContestantEachRace(IServiceObserver client) throws ContestException {
        Request request = new Request.Builder().type(RequestType.ALL_RACES).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ContestException(err);
        }
        RaceNrContDTOList racesDTO = (RaceNrContDTOList) response.data();
        return racesDTO;
    }

    @Override
    public SwimmingRace findByDistanceAndStyle(RaceDTO race, IServiceObserver client) throws ContestException {
        Request request = new Request.Builder().type(RequestType.FIND_RACE).data(race).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ContestException(err);
        }
        SwimmingRace racel = (SwimmingRace) response.data();
        return racel;
    }


    @Override
    public void addContestantToRaces(ContestantDTO contestantDTO, IServiceObserver client) throws ContestException {
        Request request = new Request.Builder().type(RequestType.ADD_CONT).data(contestantDTO).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ContestException(err);
        }
    }

    @Override
    public void logOut(Admin admin, IServiceObserver client) throws ContestException {
        Request req = new Request.Builder().type(RequestType.LOG_OUT).data(admin).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if(response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ContestException(err);
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

    private void sendRequest(Request request) throws ContestException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ContestException("Error sending object " + e);
        }
    }

    private Response readResponse() throws ContestException {
        Response response=null;
        try{
            response=qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws ContestException {
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

    private void handleUpdate(Response response){
        if (response.type() == ResponseType.CONT_ADDED){
            System.out.println("Proxy added result");
            try{
                client.updateContestants();
            } catch (ContestException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response){
        return response.type()== ResponseType.CONT_ADDED;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

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
