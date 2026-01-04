package controller;

import model.Clinician;
import persistence.Repository;
import java.util.List;

public class ClinicianController extends BaseController {
    public ClinicianController(String folder){ super(folder); }
    public List<Clinician> all(){ return repo.clinicians; }

    public Clinician blank(){
        String id = repo.nextId("C", repo.clinicians.stream().map(c -> c.clinicianId).toList());
        return new Clinician(id,"","","Dr","", "", "", "", "", "", "Active", Repository.today());
    }

    public void add(Clinician c) throws Exception { repo.clinicians.add(c); autosave(); }
    public void set(int idx, Clinician c) throws Exception { repo.clinicians.set(idx,c); autosave(); }
    public void remove(int idx) throws Exception { repo.clinicians.remove(idx); autosave(); }
}
