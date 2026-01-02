package model;

public class Patient {
    public static final String[] HEADER = {
        "patient_id","first_name","last_name","date_of_birth","nhs_number","gender","phone_number","email",
        "address","postcode","emergency_contact_name","emergency_contact_phone","registration_date","gp_surgery_id"
    };

    public String patientId, firstName, lastName, dateOfBirth, nhsNumber, gender, phoneNumber, email,
            address, postcode, emergencyContactName, emergencyContactPhone, registrationDate, gpSurgeryId;

    public Patient(String[] a) { this(a[0],a[1],a[2],a[3],a[4],a[5],a[6],a[7],a[8],a[9],a[10],a[11],a[12],a[13]); }

    public Patient(String patientId,String firstName,String lastName,String dateOfBirth,String nhsNumber,String gender,
                   String phoneNumber,String email,String address,String postcode,String emergencyContactName,
                   String emergencyContactPhone,String registrationDate,String gpSurgeryId) {
        this.patientId=n(patientId); this.firstName=n(firstName); this.lastName=n(lastName); this.dateOfBirth=n(dateOfBirth);
        this.nhsNumber=n(nhsNumber); this.gender=n(gender); this.phoneNumber=n(phoneNumber); this.email=n(email);
        this.address=n(address); this.postcode=n(postcode); this.emergencyContactName=n(emergencyContactName);
        this.emergencyContactPhone=n(emergencyContactPhone); this.registrationDate=n(registrationDate); this.gpSurgeryId=n(gpSurgeryId);
    }

    public String[] toRow() {
        return new String[]{patientId,firstName,lastName,dateOfBirth,nhsNumber,gender,phoneNumber,email,address,postcode,
                emergencyContactName,emergencyContactPhone,registrationDate,gpSurgeryId};
    }

    private static String n(String s){ return s==null ? "" : s; }
}
