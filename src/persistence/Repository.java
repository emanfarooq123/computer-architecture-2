package persistence;

import model.*;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

public final class Repository {
    private static Repository instance;
    public static synchronized Repository get() {
        if (instance == null) instance = new Repository();
        return instance;
    }
    private Repository() {}

    public final List<Patient> patients = new ArrayList<>();
    public final List<Clinician> clinicians = new ArrayList<>();
    public final List<Facility> facilities = new ArrayList<>();
    public final List<Staff> staff = new ArrayList<>();
    public final List<Appointment> appointments = new ArrayList<>();
    public final List<Prescription> prescriptions = new ArrayList<>();
    public final List<Referral> referrals = new ArrayList<>();

    public void clearAll(){
        patients.clear(); clinicians.clear(); facilities.clear(); staff.clear();
        appointments.clear(); prescriptions.clear(); referrals.clear();
    }

    public void loadAll(String folder) throws Exception {
        clearAll();
        for (String[] a : CsvUtil.read(new File(folder,"patients.csv"), true)) if (a.length>=14) patients.add(new Patient(a));
        for (String[] a : CsvUtil.read(new File(folder,"clinicians.csv"), true)) if (a.length>=12) clinicians.add(new Clinician(a));
        for (String[] a : CsvUtil.read(new File(folder,"facilities.csv"), true)) if (a.length>=11) facilities.add(new Facility(a));
        for (String[] a : CsvUtil.read(new File(folder,"staff.csv"), true)) if (a.length>=12) staff.add(new Staff(a));
        for (String[] a : CsvUtil.read(new File(folder,"appointments.csv"), true)) if (a.length>=13) appointments.add(new Appointment(a));
        for (String[] a : CsvUtil.read(new File(folder,"prescriptions.csv"), true)) if (a.length>=15) prescriptions.add(new Prescription(a));
        for (String[] a : CsvUtil.read(new File(folder,"referrals.csv"), true)) if (a.length>=16) referrals.add(new Referral(a));
    }

    public void saveAll(String folder) throws Exception {
        CsvUtil.write(new File(folder,"patients.csv"), Patient.HEADER, patients.stream().map(Patient::toRow).toList());
        CsvUtil.write(new File(folder,"clinicians.csv"), Clinician.HEADER, clinicians.stream().map(Clinician::toRow).toList());
        CsvUtil.write(new File(folder,"facilities.csv"), Facility.HEADER, facilities.stream().map(Facility::toRow).toList());
        CsvUtil.write(new File(folder,"staff.csv"), Staff.HEADER, staff.stream().map(Staff::toRow).toList());
        CsvUtil.write(new File(folder,"appointments.csv"), Appointment.HEADER, appointments.stream().map(Appointment::toRow).toList());
        CsvUtil.write(new File(folder,"prescriptions.csv"), Prescription.HEADER, prescriptions.stream().map(Prescription::toRow).toList());
        CsvUtil.write(new File(folder,"referrals.csv"), Referral.HEADER, referrals.stream().map(Referral::toRow).toList());
    }

    public static String today(){ return LocalDate.now().toString(); }

    public String nextId(String prefix, List<String> existing) {
        int max=0;
        for (String s: existing) {
            if (s==null) continue;
            String digits = s.replaceAll("\\D+","");
            if (!digits.isEmpty()) {
                try { max = Math.max(max, Integer.parseInt(digits)); } catch (Exception ignored) {}
            }
        }
        return String.format("%s-%04d", prefix, max+1);
    }
}
