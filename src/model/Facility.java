package model;

public class Facility {
    public static final String[] HEADER = {
        "facility_id","facility_name","facility_type","address","postcode","phone_number","email",
        "opening_hours","manager_name","capacity","specialities_offered"
    };

    public String facilityId, facilityName, facilityType, address, postcode, phoneNumber, email,
            openingHours, managerName, capacity, specialitiesOffered;

    public Facility(String[] a){
        this(a[0],a[1],a[2],a[3],a[4],a[5],a[6],a[7],a[8],a[9],a[10]);
    }

    public Facility(String facilityId,String facilityName,String facilityType,String address,String postcode,String phoneNumber,String email,
                    String openingHours,String managerName,String capacity,String specialitiesOffered){
        this.facilityId=n(facilityId); this.facilityName=n(facilityName); this.facilityType=n(facilityType); this.address=n(address);
        this.postcode=n(postcode); this.phoneNumber=n(phoneNumber); this.email=n(email); this.openingHours=n(openingHours);
        this.managerName=n(managerName); this.capacity=n(capacity); this.specialitiesOffered=n(specialitiesOffered);
    }

    public String[] toRow(){
        return new String[]{facilityId,facilityName,facilityType,address,postcode,phoneNumber,email,openingHours,managerName,capacity,specialitiesOffered};
    }

    private static String n(String s){ return s==null ? "" : s; }
}
