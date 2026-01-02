package model;

public class Prescription {
    public static final String[] HEADER = {
        "prescription_id","patient_id","clinician_id","appointment_id","prescription_date","medication_name","dosage",
        "frequency","duration_days","quantity","instructions","pharmacy_name","status","issue_date","collection_date"
    };

    public String prescriptionId, patientId, clinicianId, appointmentId, prescriptionDate, medicationName, dosage,
            frequency, durationDays, quantity, instructions, pharmacyName, status, issueDate, collectionDate;

    public Prescription(String[] a){ this(a[0],a[1],a[2],a[3],a[4],a[5],a[6],a[7],a[8],a[9],a[10],a[11],a[12],a[13],a[14]); }

    public Prescription(String prescriptionId,String patientId,String clinicianId,String appointmentId,String prescriptionDate,String medicationName,
                        String dosage,String frequency,String durationDays,String quantity,String instructions,String pharmacyName,
                        String status,String issueDate,String collectionDate){
        this.prescriptionId=n(prescriptionId); this.patientId=n(patientId); this.clinicianId=n(clinicianId); this.appointmentId=n(appointmentId);
        this.prescriptionDate=n(prescriptionDate); this.medicationName=n(medicationName); this.dosage=n(dosage); this.frequency=n(frequency);
        this.durationDays=n(durationDays); this.quantity=n(quantity); this.instructions=n(instructions); this.pharmacyName=n(pharmacyName);
        this.status=n(status); this.issueDate=n(issueDate); this.collectionDate=n(collectionDate);
    }

    public String[] toRow(){
        return new String[]{prescriptionId,patientId,clinicianId,appointmentId,prescriptionDate,medicationName,dosage,frequency,
                durationDays,quantity,instructions,pharmacyName,status,issueDate,collectionDate};
    }
    private static String n(String s){ return s==null ? "" : s; }
}
