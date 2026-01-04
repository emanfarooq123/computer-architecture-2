package controller;

import model.Staff;
import persistence.Repository;
import java.util.List;

public class StaffController extends BaseController {
    public StaffController(String folder){ super(folder); }
    public List<Staff> all(){ return repo.staff; }

    public Staff blank(){
        String id = repo.nextId("ST", repo.staff.stream().map(s -> s.staffId).toList());
        return new Staff(id,"","","Receptionist","", "", "", "", "Active", Repository.today(), "", "");
    }

    public void add(Staff s) throws Exception { repo.staff.add(s); autosave(); }
    public void set(int idx, Staff s) throws Exception { repo.staff.set(idx,s); autosave(); }
    public void remove(int idx) throws Exception { repo.staff.remove(idx); autosave(); }
}
