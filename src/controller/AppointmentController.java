package controller;

import model.Appointment;
import persistence.Repository;
import java.util.List;

public class AppointmentController extends BaseController {
    public AppointmentController(String folder){ super(folder); }
    public List<Appointment> all(){ return repo.appointments; }

    public Appointment blank(){
        String id = repo.nextId("A", repo.appointments.stream().map(a -> a.appointmentId).toList());
        String today = Repository.today();
        return new Appointment(id,"","","", today, "", "15", "Consultation", "Booked", "", "", today, today);
    }

    public void add(Appointment a) throws Exception { repo.appointments.add(a); autosave(); }
    public void set(int idx, Appointment a) throws Exception { repo.appointments.set(idx,a); autosave(); }
    public void remove(int idx) throws Exception { repo.appointments.remove(idx); autosave(); }
}
