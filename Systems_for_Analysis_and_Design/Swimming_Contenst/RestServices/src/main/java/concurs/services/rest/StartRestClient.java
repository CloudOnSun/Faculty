package concurs.services.rest;

import concurs.domain.SwimmingRace;
import org.springframework.web.client.RestClientException;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by grigo on 5/11/17.
 */
public class StartRestClient {
    private final static RaceClient raceClient = new RaceClient();

    public static void main(String[] args) {
        //  RestTemplate restTemplate=new RestTemplate();
        SwimmingRace raceT = new SwimmingRace("distanceTest", "styleTest");
        raceT.setID(4);
        AtomicReference<SwimmingRace> race2 = new AtomicReference<>();
        try {
            show(() -> {
                race2.set(raceClient.create(raceT));
                System.out.println(race2);
            });
            show(() -> {
                SwimmingRace[] res = raceClient.getAll();
                for (SwimmingRace r : res) {
                    System.out.println(r.getID() + ": " + r.getDistance() + ": " + r.getStyle());
                }
            });

            show(() -> System.out.println(raceClient.getById(race2.get().getID())));
            show(() -> raceClient.delete(race2.get().getID()));
            show(() -> {
                SwimmingRace[] res = raceClient.getAll();
                for (SwimmingRace r : res) {
                    System.out.println(r.getID() + ": " + r.getDistance() + ": " + r.getStyle());
                }
            });
        } catch (RestClientException ex) {
            System.out.println("Exception ... " + ex.getMessage());
        }
    }


    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            //  LOG.error("Service exception", e);
            System.out.println("Service exception" + e);
        }
    }
}
