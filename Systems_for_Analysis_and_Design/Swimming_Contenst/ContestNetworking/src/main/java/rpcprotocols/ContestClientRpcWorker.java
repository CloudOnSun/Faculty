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

public class ContestClientRpcWorker implements Runnable, IServiceObserver {

    private IService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private volatile boolean connected;

    public ContestClientRpcWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error ---- " + e);
        }

    }

    @Override
    public void updateContestants() throws ContestException {
        Response resp = new Response.Builder().type(ResponseType.CONT_ADDED).build();
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private void sendResponse(Response response) throws IOException {
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    private Response handleRequest(Request request) {

        if (request.type() == RequestType.LOG_IN) {
            AdminLogInDTO adminDto = (AdminLogInDTO) request.data();
            try {
                Admin admin = server.getAccount(adminDto, this);
                return new Response.Builder().type(ResponseType.GOT_ACCOUNT).data(admin).build();
            } catch (ContestException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.LOG_OUT) {
            Admin admin = (Admin) request.data();
            try {
                server.logOut(admin, this);
                connected = false;
                return okResponse;
            } catch (ContestException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.ALL_RACES) {
            try {
                RaceNrContDTOList racesDTO = server.nrContestantEachRace(this);
                return new Response.Builder().type(ResponseType.GOT_ALL_RACES).data(racesDTO).build();
            } catch (ContestException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.ADD_CONT) {
            ContestantDTO contDTO = (ContestantDTO) request.data();
            try{
                server.addContestantToRaces(contDTO, this);
                return okResponse;
            } catch (ContestException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.FIND_RACE) {
            RaceDTO raceDTO = (RaceDTO) request.data();
            try {
                SwimmingRace race = server.findByDistanceAndStyle(raceDTO, this);
                return new Response.Builder().type(ResponseType.GOT_RACE).data(race).build();
            }
            catch (ContestException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.FIND_CONTS) {
            RaceDTO raceDTO = (RaceDTO) request.data();
            try{
                ContWithRaceDTOList contsDTOlist = server.getByRace(raceDTO, this);
                return new Response.Builder().type(ResponseType.GOT_CONTS).data(contsDTOlist).build();
            } catch (ContestException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return null;
    }

}
