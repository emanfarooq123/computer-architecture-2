package model;

public class Referral {
    public static final String[] HEADER = {
        "referral_id","patient_id","referring_clinician_id","referred_to_clinician_id","referring_facility_id",
        "referred_to_facility_id","referral_date","urgency_level","referral_reason","clinical_summary",
        "requested_investigations","status","appointment_id","notes","created_date","last_updated"
    };

    public String referralId, patientId, referringClinicianId, referredToClinicianId, referringFacilityId, referredToFacilityId,
            referralDate, urgencyLevel, referralReason, clinicalSummary, requestedInvestigations, status,
            appointmentId, notes, createdDate, lastUpdated;

    public Referral(String[] a){ this(a[0],a[1],a[2],a[3],a[4],a[5],a[6],a[7],a[8],a[9],a[10],a[11],a[12],a[13],a[14],a[15]); }

    public Referral(String referralId,String patientId,String referringClinicianId,String referredToClinicianId,String referringFacilityId,
                    String referredToFacilityId,String referralDate,String urgencyLevel,String referralReason,String clinicalSummary,
                    String requestedInvestigations,String status,String appointmentId,String notes,String createdDate,String lastUpdated){
        this.referralId=n(referralId); this.patientId=n(patientId); this.referringClinicianId=n(referringClinicianId); this.referredToClinicianId=n(referredToClinicianId);
        this.referringFacilityId=n(referringFacilityId); this.referredToFacilityId=n(referredToFacilityId); this.referralDate=n(referralDate);
        this.urgencyLevel=n(urgencyLevel); this.referralReason=n(referralReason); this.clinicalSummary=n(clinicalSummary);
        this.requestedInvestigations=n(requestedInvestigations); this.status=n(status); this.appointmentId=n(appointmentId); this.notes=n(notes);
        this.createdDate=n(createdDate); this.lastUpdated=n(lastUpdated);
    }

    public String[] toRow(){
        return new String[]{referralId,patientId,referringClinicianId,referredToClinicianId,referringFacilityId,referredToFacilityId,
                referralDate,urgencyLevel,referralReason,clinicalSummary,requestedInvestigations,status,appointmentId,notes,createdDate,lastUpdated};
    }
    private static String n(String s){ return s==null ? "" : s; }
}
