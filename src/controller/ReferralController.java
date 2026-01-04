package controller;

import model.Patient;
import model.Referral;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class ReferralController extends BaseController {
    public ReferralController(String folder){ super(folder); }
    public List<Referral> all(){ return repo.referrals; }

    public Referral blank(){
        String id = repo.nextId("R", repo.referrals.stream().map(r -> r.referralId).toList());
        String today = persistence.Repository.today();
        return new Referral(id,"","","","","", today, "Routine", "", "", "", "Created", "", "", today, today);
    }

    public void add(Referral r) throws Exception { repo.referrals.add(r); autosave(); }
    public void set(int idx, Referral r) throws Exception { repo.referrals.set(idx,r); autosave(); }
    public void remove(int idx) throws Exception { repo.referrals.remove(idx); autosave(); }

    public File exportLetter(Referral r) throws Exception {
        Patient pt = repo.patients.stream().filter(p -> p.patientId.equals(r.patientId)).findFirst().orElse(null);
        if (pt == null) throw new IllegalArgumentException("Patient not found: " + r.patientId);

        File dir = new File("output/referrals");
        dir.mkdirs();
        File f = new File(dir, "referral_" + r.referralId + ".txt");

        try (FileWriter fw = new FileWriter(f)) {
            fw.write("REFERRAL LETTER\n");
            fw.write("Referral ID: " + r.referralId + "\n");
            fw.write("Date: " + r.referralDate + " | Urgency: " + r.urgencyLevel + "\n\n");
            fw.write("Patient: " + pt.firstName + " " + pt.lastName + " (" + pt.patientId + ")\n");
            fw.write("DOB: " + pt.dateOfBirth + " | NHS: " + pt.nhsNumber + "\n");
            fw.write("Postcode: " + pt.postcode + "\n\n");
            fw.write("Reason: " + r.referralReason + "\n");
            fw.write("Clinical summary: " + r.clinicalSummary + "\n");
            fw.write("Requested investigations: " + r.requestedInvestigations + "\n");
            fw.write("Status: " + r.status + "\n");
        }
        return f;
    }
}
