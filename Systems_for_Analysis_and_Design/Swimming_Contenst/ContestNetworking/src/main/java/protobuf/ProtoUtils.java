package protobuf;

import concurs.domain.*;
//import domain.*;

import java.util.ArrayList;
import java.util.List;

public final class ProtoUtils {

    public static ContestProtobufs.ContestRequest createLogInRequest(AdminLogInDTO adminDTO) {
        ContestProtobufs.AdminLogInDTO adminProtoDto = ContestProtobufs.AdminLogInDTO.newBuilder()
                .setEmail(adminDTO.getEmail())
                .setPassword(adminDTO.getPassword()).build();
        ContestProtobufs.ContestRequest request = ContestProtobufs.ContestRequest.newBuilder()
                .setType(ContestProtobufs.ContestRequest.Type.LOG_IN)
                .setAdminLogInDTO(adminProtoDto).build();
        return request;
    }

    public static ContestProtobufs.ContestRequest createGetByRaceRequest(RaceDTO race) {
        ContestProtobufs.RaceDTO raceDtoProto = ContestProtobufs.RaceDTO.newBuilder()
                .setDistance(race.getDistance())
                .setStyle(race.getStyle()).build();
        ContestProtobufs.ContestRequest request = ContestProtobufs.ContestRequest.newBuilder()
                .setType(ContestProtobufs.ContestRequest.Type.FIND_CONTS)
                .setRaceDTO(raceDtoProto).build();
        return request;
    }

    public static ContestProtobufs.ContestRequest createNrContEachRaceRequest() {
        return ContestProtobufs.ContestRequest.newBuilder()
                .setType(ContestProtobufs.ContestRequest.Type.ALL_RACES).build();
    }

    public static ContestProtobufs.ContestRequest createFindByDistAndStyleRequest(RaceDTO race) {
        ContestProtobufs.RaceDTO raceDtoProto = ContestProtobufs.RaceDTO.newBuilder()
                .setDistance(race.getDistance())
                .setStyle(race.getStyle()).build();
        ContestProtobufs.ContestRequest request = ContestProtobufs.ContestRequest.newBuilder()
                .setType(ContestProtobufs.ContestRequest.Type.FIND_RACE)
                .setRaceDTO(raceDtoProto).build();
        return request;
    }

    public static ContestProtobufs.ContestRequest createAddContRequest(ContestantDTO contestantDTO) {
        ContestProtobufs.ContestantDTO.Builder contDtoProto = ContestProtobufs.ContestantDTO.newBuilder()
                .setName(contestantDTO.getName())
                .setAge(contestantDTO.getAge());
        for (var race : contestantDTO.getRaces()) {
            ContestProtobufs.SwimmingRace swimmingRaceProto = ContestProtobufs.SwimmingRace.newBuilder()
                    .setId(race.getID())
                    .setDistance(race.getDistance())
                    .setStyle(race.getStyle()).build();
            contDtoProto.addRaces(swimmingRaceProto);
        }
        ContestProtobufs.ContestRequest request = ContestProtobufs.ContestRequest.newBuilder()
                .setType(ContestProtobufs.ContestRequest.Type.ADD_CONT)
                .setContestantDTO(contDtoProto.build()).build();
        return request;
    }

    public static ContestProtobufs.ContestRequest createLogOutRequest(Admin admin) {
        ContestProtobufs.Admin adminProto = ContestProtobufs.Admin.newBuilder()
                .setId(admin.getID())
                .setEmail(admin.getEmail())
                .setPassword(admin.getPassword()).build();
        ContestProtobufs.ContestRequest request = ContestProtobufs.ContestRequest.newBuilder()
                .setType(ContestProtobufs.ContestRequest.Type.LOG_OUT)
                .setAdmin(adminProto).build();
        return request;
    }

    public static ContestProtobufs.ContestResponse createOkResponse(){
        ContestProtobufs.ContestResponse response=ContestProtobufs.ContestResponse.newBuilder()
                .setType(ContestProtobufs.ContestResponse.Type.OK).build();
        return response;
    }

    public static ContestProtobufs.ContestResponse createErrorResponse(String text){
        ContestProtobufs.ContestResponse response=ContestProtobufs.ContestResponse.newBuilder()
                .setType(ContestProtobufs.ContestResponse.Type.ERROR)
                .setError(text).build();
        return response;
    }

    public static ContestProtobufs.ContestResponse createContAddedResponse(){
        ContestProtobufs.ContestResponse response=ContestProtobufs.ContestResponse.newBuilder()
                .setType(ContestProtobufs.ContestResponse.Type.CONT_ADDED).build();
        return response;
    }

    public static ContestProtobufs.ContestResponse createGotAccountResponse(Admin admin){
        ContestProtobufs.Admin adminProto = ContestProtobufs.Admin.newBuilder()
                .setId(admin.getID())
                .setEmail(admin.getEmail())
                .setPassword(admin.getPassword()).build();
        ContestProtobufs.ContestResponse response=ContestProtobufs.ContestResponse.newBuilder()
                .setType(ContestProtobufs.ContestResponse.Type.GOT_ACCOUNT)
                .setAdmin(adminProto).build();
        return response;
    }

    public static ContestProtobufs.ContestResponse createGotRaceResponse(SwimmingRace race){
        ContestProtobufs.SwimmingRace swimmingRaceProto = ContestProtobufs.SwimmingRace.newBuilder()
                .setId(race.getID())
                .setDistance(race.getDistance())
                .setStyle(race.getStyle()).build();
        ContestProtobufs.ContestResponse response=ContestProtobufs.ContestResponse.newBuilder()
                .setType(ContestProtobufs.ContestResponse.Type.GOT_RACE)
                .setSwimmingRace(swimmingRaceProto).build();
        return response;
    }

    public static ContestProtobufs.ContestResponse createGotContsResponse(ContWithRaceDTOList conts) {
        ContestProtobufs.ContWithRaceDTOList.Builder contsProto = ContestProtobufs.ContWithRaceDTOList.newBuilder();
        for (var cont : conts.getContestantWithRacesDTOList()) {
            ContestProtobufs.ContestantWithRacesDTO contProto = ContestProtobufs.ContestantWithRacesDTO.newBuilder()
                    .setName(cont.getName())
                    .setAge(cont.getAge())
                    .setRaces(cont.getRaces()).build();
            contsProto.addConts(contProto);
        }
        ContestProtobufs.ContestResponse response=ContestProtobufs.ContestResponse.newBuilder()
                .setType(ContestProtobufs.ContestResponse.Type.GOT_CONTS)
                .setContWithRaceDTOList(contsProto.build()).build();
        return response;
    }

    public static ContestProtobufs.ContestResponse createGotAllRacesResponse(RaceNrContDTOList races) {
        ContestProtobufs.RaceNrContDTOList.Builder racesProto = ContestProtobufs.RaceNrContDTOList.newBuilder();
        for (var race : races.getRaceNrContestantsDTOList()) {
            ContestProtobufs.RaceNrContestantsDTO raceProto = ContestProtobufs.RaceNrContestantsDTO.newBuilder()
                    .setDistance(race.getDistance())
                    .setStyle(race.getStyle())
                    .setNrContestants(race.getNrContestants()).build();
            racesProto.addRaces(raceProto);
        }
        ContestProtobufs.ContestResponse response=ContestProtobufs.ContestResponse.newBuilder()
                .setType(ContestProtobufs.ContestResponse.Type.GOT_ALL_RACES)
                .setRaceNrContDTOList(racesProto.build()).build();
        return response;
    }

    public static Admin fromAdminProto(ContestProtobufs.Admin adminProto) {
        Admin admin = new Admin(adminProto.getEmail(), adminProto.getPassword(), 0);
        admin.setID(adminProto.getId());
        return admin;
    }

    public static SwimmingRace fromRaceProto(ContestProtobufs.SwimmingRace raceProto) {
        SwimmingRace race = new SwimmingRace(raceProto.getDistance(), raceProto.getStyle());
        race.setID(raceProto.getId());
        return race;
    }

    public static RaceNrContDTOList fromRaceNrContDTOListProto(ContestProtobufs.RaceNrContDTOList racesProto){
        List<RaceNrContestantsDTO> races = new ArrayList<>();
        for (var raceProto : racesProto.getRacesList()) {
            races.add(new RaceNrContestantsDTO(raceProto.getDistance(), raceProto.getStyle(), raceProto. getNrContestants()));
        }
        return new RaceNrContDTOList(races);
    }

    public static ContWithRaceDTOList fromContWithRaceDtoListProto(ContestProtobufs.ContWithRaceDTOList contsProto) {
        List<ContestantWithRacesDTO> conts = new ArrayList<>();
        for (var contProto : contsProto.getContsList()) {
            conts.add(new ContestantWithRacesDTO(contProto.getName(), contProto.getAge(), contProto.getRaces()));
        }
        return new ContWithRaceDTOList(conts);
    }

}
