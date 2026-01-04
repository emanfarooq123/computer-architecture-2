package controller;

import model.Patient;
import persistence.Repository;
import java.util.List;

public class PatientController extends BaseController {
    public PatientController(String folder){ super(folder); }
    public List<Patient> all(){ return repo.patients; }

    public Patient blank(){
        String id = repo.nextId("P", repo.patients.stream().map(p -> p.patientId).toList());
        return new Patient(id,"","","","","","","","","","","", Repository.today(), "");
    }

    public void add(Patient p) throws Exception { repo.patients.add(p); autosave(); }
    public void set(int idx, Patient p) throws Exception { repo.patients.set(idx,p); autosave(); }
    public void remove(int idx) throws Exception { repo.patients.remove(idx); autosave(); }
}
