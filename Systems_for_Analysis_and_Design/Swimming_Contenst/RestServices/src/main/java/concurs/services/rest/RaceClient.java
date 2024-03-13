package concurs.services.rest;

import concurs.domain.SwimmingRace;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class RaceClient {
    public static final String URL = "http://localhost:8080/concurs/races";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public SwimmingRace[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, SwimmingRace[].class));
    }

    public SwimmingRace getById(Integer id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), SwimmingRace.class));
    }

    public SwimmingRace create(SwimmingRace race) {
        return execute(() -> restTemplate.postForObject(URL, race, SwimmingRace.class));
    }

    public void update(SwimmingRace race) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, race.getID()), race);
            return null;
        });
    }

    public void delete(Integer id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }
}
