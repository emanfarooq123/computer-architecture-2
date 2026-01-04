package controller;

import model.Facility;
import java.util.List;

public class FacilityController extends BaseController {
    public FacilityController(String folder){ super(folder); }
    public List<Facility> all(){ return repo.facilities; }

    public Facility blank(){
        String id = repo.nextId("F", repo.facilities.stream().map(f -> f.facilityId).toList());
        return new Facility(id,"","GP Surgery","","","","","","","", "");
    }

    public void add(Facility f) throws Exception { repo.facilities.add(f); autosave(); }
    public void set(int idx, Facility f) throws Exception { repo.facilities.set(idx,f); autosave(); }
    public void remove(int idx) throws Exception { repo.facilities.remove(idx); autosave(); }
}
