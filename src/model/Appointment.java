package model;

public class Appointment {
    public static final String[] HEADER = {
        "appointment_id","patient_id","clinician_id","facility_id","appointment_date","appointment_time","duration_minutes",
        "appointment_type","status","reason_for_visit","notes","created_date","last_modified"
    };

    public String appointmentId, patientId, clinicianId, facilityId, appointmentDate, appointmentTime, durationMinutes,
            appointmentType, status, reasonForVisit, notes, createdDate, lastModified;

    public Appointment(String[] a){ this(a[0],a[1],a[2],a[3],a[4],a[5],a[6],a[7],a[8],a[9],a[10],a[11],a[12]); }

    public Appointment(String appointmentId,String patientId,String clinicianId,String facilityId,String appointmentDate,String appointmentTime,
                       String durationMinutes,String appointmentType,String status,String reasonForVisit,String notes,String createdDate,String lastModified){
        this.appointmentId=n(appointmentId); this.patientId=n(patientId); this.clinicianId=n(clinicianId); this.facilityId=n(facilityId);
        this.appointmentDate=n(appointmentDate); this.appointmentTime=n(appointmentTime); this.durationMinutes=n(durationMinutes);
        this.appointmentType=n(appointmentType); this.status=n(status); this.reasonForVisit=n(reasonForVisit); this.notes=n(notes);
        this.createdDate=n(createdDate); this.lastModified=n(lastModified);
    }

    public String[] toRow(){
        return new String[]{appointmentId,patientId,clinicianId,facilityId,appointmentDate,appointmentTime,durationMinutes,
                appointmentType,status,reasonForVisit,notes,createdDate,lastModified};
    }
    private static String n(String s){ return s==null ? "" : s; }
}
