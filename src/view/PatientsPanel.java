package view;

import controller.PatientController;
import model.Patient;
import persistence.Repository;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class PatientsPanel extends JPanel {
    private final PatientController ctl;
    private final JTable table;
    private final PatientsTableModel model;
    private final CrudHeader header;

    public PatientsPanel(Frame owner, PatientController ctl) {
        super(new BorderLayout(12,12));
        this.ctl = ctl;

        header = new CrudHeader("Patients", "Add/Edit/Delete Patients • Data saved/fetched from CSV");
        model = new PatientsTableModel(ctl.all());
        table = new JTable(model);
        Ui.styleTable(table);
        FilterUtil.attach(header.search, table);

        header.add.addActionListener(e -> addPatient(owner));
        header.edit.addActionListener(e -> editPatient(owner));
        header.del.addActionListener(e -> deletePatient());
        header.refresh.addActionListener(e -> refresh());

        add(Ui.pad(header), BorderLayout.NORTH);
        add(Ui.pad(new JScrollPane(table)), BorderLayout.CENTER);
    }

    private void addPatient(Frame owner){
        try {
            Patient seed = ctl.blank();
            String[] labels = {"First name", "Last name", "DOB (YYYY-MM-DD)", "NHS number", "Phone", "Email", "Postcode", "GP surgery ID"};
            String[] init = {"","","","", "", "", "", ""};
            SimpleFormDialog d = new SimpleFormDialog(owner, "New Patient", labels, init, new int[]{0,1,2,3});
            d.setVisible(true);
            String[] r = d.result();
            if (r == null) return;

            Patient p = new Patient(seed.patientId, r[0], r[1], r[2], r[3], "", r[4], r[5], "", r[6], "", "", Repository.today(), r[7]);
            ctl.add(p);
            refresh();
        } catch (Exception ex){ Message.error(ex); }
    }

    private void editPatient(Frame owner){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select a patient first."); return; }
        int row = table.convertRowIndexToModel(v);

        try {
            Patient p0 = ctl.all().get(row);
            String[] labels = {"Patient ID", "First name", "Last name", "DOB (YYYY-MM-DD)", "NHS number", "Phone", "Email", "Postcode", "GP surgery ID"};
            String[] init = {p0.patientId,p0.firstName,p0.lastName,p0.dateOfBirth,p0.nhsNumber,p0.phoneNumber,p0.email,p0.postcode,p0.gpSurgeryId};
            SimpleFormDialog d = new SimpleFormDialog(owner, "Edit Patient", labels, init, new int[]{1,2,3,4}, new int[]{0});
            d.setVisible(true);
            String[] r = d.result();
            if (r == null) return;

            Patient p = new Patient(p0.patientId, r[1], r[2], r[3], r[4], p0.gender, r[5], r[6], p0.address, r[7],
                    p0.emergencyContactName, p0.emergencyContactPhone, p0.registrationDate, r[8]);
            ctl.set(row, p);
            refresh();
        } catch (Exception ex){ Message.error(ex); }
    }

    private void deletePatient(){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select a patient first."); return; }
        int row = table.convertRowIndexToModel(v);

        if (!Message.confirm("Delete selected patient? This will auto-save to CSV.")) return;
        try { ctl.remove(row); refresh(); }
        catch (Exception ex){ Message.error(ex); }
    }

    private void refresh(){
        try { ctl.reloadAll(); model.setData(ctl.all()); }
        catch (Exception ex){ Message.error(ex); }
    }

    static class PatientsTableModel extends AbstractTableModel {
        private List<Patient> data;
        private final String[] cols = {"Patient ID","First name","Last name","DOB","NHS","Phone","Email","Postcode","GP surgery"};

        PatientsTableModel(List<Patient> data){ this.data = data; }
        void setData(List<Patient> data){ this.data=data; fireTableDataChanged(); }

        public int getRowCount(){ return data.size(); }
        public int getColumnCount(){ return cols.length; }
        public String getColumnName(int c){ return cols[c]; }
        public Object getValueAt(int r,int c){
            Patient p = data.get(r);
            return switch(c){
                case 0 -> p.patientId;
                case 1 -> p.firstName;
                case 2 -> p.lastName;
                case 3 -> p.dateOfBirth;
                case 4 -> p.nhsNumber;
                case 5 -> p.phoneNumber;
                case 6 -> p.email;
                case 7 -> p.postcode;
                default -> p.gpSurgeryId;
            };
        }
    }
}
