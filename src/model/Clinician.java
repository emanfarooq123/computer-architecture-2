package model;

public class Clinician {
    public static final String[] HEADER = {
        "clinician_id","first_name","last_name","title","speciality","gmc_number",
        "phone_number","email","workplace_id","workplace_type","employment_status","start_date"
    };

    public String clinicianId, firstName, lastName, title, speciality, gmcNumber, phoneNumber, email,
            workplaceId, workplaceType, employmentStatus, startDate;

    public Clinician(String[] a){
        this(a[0],a[1],a[2],a[3],a[4],a[5],a[6],a[7],a[8],a[9],a[10],a[11]);
    }

    public Clinician(String clinicianId,String firstName,String lastName,String title,String speciality,String gmcNumber,
                     String phoneNumber,String email,String workplaceId,String workplaceType,String employmentStatus,String startDate){
        this.clinicianId=n(clinicianId); this.firstName=n(firstName); this.lastName=n(lastName); this.title=n(title);
        this.speciality=n(speciality); this.gmcNumber=n(gmcNumber); this.phoneNumber=n(phoneNumber); this.email=n(email);
        this.workplaceId=n(workplaceId); this.workplaceType=n(workplaceType); this.employmentStatus=n(employmentStatus); this.startDate=n(startDate);
    }

    public String[] toRow(){
        return new String[]{clinicianId,firstName,lastName,title,speciality,gmcNumber,phoneNumber,email,workplaceId,workplaceType,employmentStatus,startDate};
    }

    private static String n(String s){ return s==null ? "" : s; }
}
