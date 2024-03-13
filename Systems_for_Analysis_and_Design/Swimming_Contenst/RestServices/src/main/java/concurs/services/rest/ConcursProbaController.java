package concurs.services.rest;

import concurs.domain.SwimmingRace;
import concurs.repository.ISwimmingRaceRepository;
import concurs.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@CrossOrigin
@RestController
@RequestMapping("/concurs/races")
public class ConcursProbaController {

    @Autowired
    private ISwimmingRaceRepository raceRepository;


    @RequestMapping( method= RequestMethod.GET)
    public SwimmingRace[] getAll(){
        System.out.println("Get all races ...");
        return raceRepository.getRaces();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id){
        System.out.println("Get by id "+id);
        SwimmingRace race=raceRepository.findById(id);
        if (race==null)
            return new ResponseEntity<String>("Race not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<SwimmingRace>(race, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public SwimmingRace create(@RequestBody SwimmingRace race){
        raceRepository.add(race);
        return race;

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public SwimmingRace update(@RequestBody SwimmingRace race) {
        System.out.println("Updating race ...");
        raceRepository.update(race,race.getID());
        return race;

    }
    // @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        System.out.println("Deleting race ... "+id);
        try {
            raceRepository.delete(id);
            return new ResponseEntity<SwimmingRace>(HttpStatus.OK);
        }catch (RepositoryException ex){
            System.out.println("Ctrl Delete user exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(RepositoryException e) {
        return e.getMessage();
    }
}
