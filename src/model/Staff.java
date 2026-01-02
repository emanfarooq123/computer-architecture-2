package model;

public class Staff {
    public static final String[] HEADER = {
        "staff_id","first_name","last_name","role","department","facility_id",
        "phone_number","email","employment_status","start_date","line_manager","access_level"
    };

    public String staffId, firstName, lastName, role, department, facilityId, phoneNumber, email,
            employmentStatus, startDate, lineManager, accessLevel;

    public Staff(String[] a){
        this(a[0],a[1],a[2],a[3],a[4],a[5],a[6],a[7],a[8],a[9],a[10],a[11]);
    }

    public Staff(String staffId,String firstName,String lastName,String role,String department,String facilityId,
                 String phoneNumber,String email,String employmentStatus,String startDate,String lineManager,String accessLevel){
        this.staffId=n(staffId); this.firstName=n(firstName); this.lastName=n(lastName); this.role=n(role);
        this.department=n(department); this.facilityId=n(facilityId); this.phoneNumber=n(phoneNumber); this.email=n(email);
        this.employmentStatus=n(employmentStatus); this.startDate=n(startDate); this.lineManager=n(lineManager); this.accessLevel=n(accessLevel);
    }

    public String[] toRow(){
        return new String[]{staffId,firstName,lastName,role,department,facilityId,phoneNumber,email,employmentStatus,startDate,lineManager,accessLevel};
    }

    private static String n(String s){ return s==null ? "" : s; }
}
