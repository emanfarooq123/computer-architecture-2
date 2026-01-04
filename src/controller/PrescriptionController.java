package controller;

import model.Patient;
import model.Prescription;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class PrescriptionController extends BaseController {
    public PrescriptionController(String folder){ super(folder); }
    public List<Prescription> all(){ return repo.prescriptions; }

    public Prescription blank(){
        String id = repo.nextId("RX", repo.prescriptions.stream().map(p -> p.prescriptionId).toList());
        String today = persistence.Repository.today();
        return new Prescription(id,"","","", today, "", "", "", "", "", "", "", "Issued", today, "");
    }

    public void add(Prescription p) throws Exception { repo.prescriptions.add(p); autosave(); }
    public void set(int idx, Prescription p) throws Exception { repo.prescriptions.set(idx,p); autosave(); }
    public void remove(int idx) throws Exception { repo.prescriptions.remove(idx); autosave(); }

    public File exportTxt(Prescription pr) throws Exception {
        Patient pt = repo.patients.stream().filter(p -> p.patientId.equals(pr.patientId)).findFirst().orElse(null);
        if (pt == null) throw new IllegalArgumentException("Patient not found: " + pr.patientId);

        File dir = new File("output/prescriptions");
        dir.mkdirs();
        File f = new File(dir, "prescription_" + pr.prescriptionId + ".txt");

        try (FileWriter fw = new FileWriter(f)) {
            fw.write("PRESCRIPTION\n");
            fw.write("Prescription ID: " + pr.prescriptionId + "\n");
            fw.write("Patient: " + pt.firstName + " " + pt.lastName + " (" + pt.patientId + ")\n");
            fw.write("Medication: " + pr.medicationName + "\n");
            fw.write("Dosage: " + pr.dosage + "\n");
            fw.write("Frequency: " + pr.frequency + "\n");
            fw.write("Duration: " + pr.durationDays + " days\n");
            fw.write("Instructions: " + pr.instructions + "\n");
            fw.write("Pharmacy: " + pr.pharmacyName + "\n");
            fw.write("Status: " + pr.status + "\n");
        }
        return f;
    }
}
