package rpcprotocols;

import concurs.domain.*;
//import domain.*;
import protobuf.ContestProtobufs;
import protobuf.ProtoUtils;
import service.ContestException;
import service.IService;
import service.IServiceObserver;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ContestServicesProtoProxy implements IService {

    private String host;
    private int port;
    private IServiceObserver client;
    private InputStream input;
    private OutputStream output;
    private Socket connection;
    private BlockingQueue<ContestProtobufs.ContestResponse> qresponses;
    private volatile boolean finished;

    public ContestServicesProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }

    @Override
    public Admin getAccount(AdminLogInDTO adminLogInDTO, IServiceObserver client) throws ContestException {
        initializeConnection();
        sendRequest(ProtoUtils.createLogInRequest(adminLogInDTO));
        ContestProtobufs.ContestResponse response = readResponse();
        if (response.getType() == ContestProtobufs.ContestResponse.Type.ERROR) {
            String err = response.getError();
            closeConnection();
            throw new ContestException(err);
        }
        this.client = client;
        return ProtoUtils.fromAdminProto(response.getAdmin());
    }

    @Override
    public ContWithRaceDTOList getByRace(RaceDTO race, IServiceObserver client) throws ContestException {
        sendRequest(ProtoUtils.createGetByRaceRequest(race));
        ContestProtobufs.ContestResponse response = readResponse();
        if (response.getType() == ContestProtobufs.ContestResponse.Type.ERROR) {
            String err = response.getError();
            throw new ContestException(err);
        }
        return ProtoUtils.fromContWithRaceDtoListProto(response.getContWithRaceDTOList());
    }

    @Override
    public RaceNrContDTOList nrContestantEachRace(IServiceObserver client) throws ContestException {
        sendRequest(ProtoUtils.createNrContEachRaceRequest());
        ContestProtobufs.ContestResponse response = readResponse();
        if (response.getType() == ContestProtobufs.ContestResponse.Type.ERROR) {
            String err = response.getError();
            throw new ContestException(err);
        }
        return ProtoUtils.fromRaceNrContDTOListProto(response.getRaceNrContDTOList());
    }

    @Override
    public SwimmingRace findByDistanceAndStyle(RaceDTO race, IServiceObserver client) throws ContestException {
        sendRequest(ProtoUtils.createFindByDistAndStyleRequest(race));
        ContestProtobufs.ContestResponse response = readResponse();
        if (response.getType() == ContestProtobufs.ContestResponse.Type.ERROR) {
            String err = response.getError();
            throw new ContestException(err);
        }
        return ProtoUtils.fromRaceProto(response.getSwimmingRace());
    }


    @Override
    public void addContestantToRaces(ContestantDTO contestantDTO, IServiceObserver client) throws ContestException {
        sendRequest(ProtoUtils.createAddContRequest(contestantDTO));
        ContestProtobufs.ContestResponse response = readResponse();
        if (response.getType() == ContestProtobufs.ContestResponse.Type.ERROR) {
            String err = response.getError();
            throw new ContestException(err);
        }
    }

    @Override
    public void logOut(Admin admin, IServiceObserver client) throws ContestException {
        sendRequest(ProtoUtils.createLogOutRequest(admin));
        ContestProtobufs.ContestResponse response = readResponse();
        closeConnection();
        if (response.getType() == ContestProtobufs.ContestResponse.Type.ERROR) {
            String err = response.getError();
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

    private void sendRequest(ContestProtobufs.ContestRequest request) throws ContestException {
        try {
            request.writeDelimitedTo(output);
            output.flush();
        } catch (IOException e) {
            throw new ContestException("Error sending object " + e);
        }
    }

    private ContestProtobufs.ContestResponse readResponse() throws ContestException {
        ContestProtobufs.ContestResponse response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws ContestException {
        try {
            connection = new Socket(host, port);
            output = connection.getOutputStream();
            //output.flush();
            input = connection.getInputStream();
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ContestServicesProtoProxy.ReaderThread());
        tw.start();
    }

    private void handleUpdate(ContestProtobufs.ContestResponse response) {
        if (response.getType() == ContestProtobufs.ContestResponse.Type.CONT_ADDED) {
            System.out.println("Proxy added result");
            try {
                client.updateContestants();
            } catch (ContestException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(ContestProtobufs.ContestResponse response) {
        return response.getType() == ContestProtobufs.ContestResponse.Type.CONT_ADDED;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    ContestProtobufs.ContestResponse response = ContestProtobufs.ContestResponse.parseDelimitedFrom(input);
                    if (isUpdate(response)) {
                        handleUpdate(response);
                    } else {

                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
