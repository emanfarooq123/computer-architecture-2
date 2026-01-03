package view;

import controller.ClinicianController;
import model.Clinician;
import persistence.Repository;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class CliniciansPanel extends JPanel {
    private final ClinicianController ctl;
    private final JTable table;
    private final CliniciansTableModel model;
    private final CrudHeader header;

    public CliniciansPanel(Frame owner, ClinicianController ctl) {
        super(new BorderLayout(12,12));
        this.ctl = ctl;

        header = new CrudHeader("Clinicians", "Add/Edit/Delete Clinicians • Data saved/fetched from CSV");
        model = new CliniciansTableModel(ctl.all());
        table = new JTable(model);
        Ui.styleTable(table);
        FilterUtil.attach(header.search, table);

        header.add.addActionListener(e -> addClinician(owner));
        header.edit.addActionListener(e -> editClinician(owner));
        header.del.addActionListener(e -> deleteClinician());
        header.refresh.addActionListener(e -> refresh());

        add(Ui.pad(header), BorderLayout.NORTH);
        add(Ui.pad(new JScrollPane(table)), BorderLayout.CENTER);
    }

    private void addClinician(Frame owner){
        try {
            Clinician seed = ctl.blank();
            String[] labels = {"First name","Last name","Title","Speciality","Phone","Email","Workplace ID","Workplace type","Employment status"};
            String[] init = {"","","Dr","","","","","Clinic","Active"};
            SimpleFormDialog d = new SimpleFormDialog(owner, "New Clinician", labels, init, new int[]{0,1,2});
            d.setVisible(true);
            String[] r = d.result();
            if (r == null) return;

            Clinician c = new Clinician(seed.clinicianId, r[0], r[1], r[2], r[3], "", r[4], r[5], r[6], r[7], r[8], Repository.today());
            ctl.add(c);
            refresh();
        } catch (Exception ex){ Message.error(ex); }
    }

    private void editClinician(Frame owner){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select a clinician first."); return; }
        int row = table.convertRowIndexToModel(v);

        try {
            Clinician c0 = ctl.all().get(row);
            String[] labels = {"Clinician ID","First name","Last name","Title","Speciality","Phone","Email","Workplace ID","Workplace type","Employment status"};
            String[] init = {c0.clinicianId,c0.firstName,c0.lastName,c0.title,c0.speciality,c0.phoneNumber,c0.email,c0.workplaceId,c0.workplaceType,c0.employmentStatus};
            SimpleFormDialog d = new SimpleFormDialog(owner, "Edit Clinician", labels, init, new int[]{1,2,3}, new int[]{0});
            d.setVisible(true);
            String[] r = d.result();
            if (r == null) return;

            Clinician c = new Clinician(c0.clinicianId, r[1], r[2], r[3], r[4], c0.gmcNumber, r[5], r[6], r[7], r[8], r[9], c0.startDate);
            ctl.set(row, c);
            refresh();
        } catch (Exception ex){ Message.error(ex); }
    }

    private void deleteClinician(){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select a clinician first."); return; }
        int row = table.convertRowIndexToModel(v);

        if (!Message.confirm("Delete selected clinician? This will auto-save to CSV.")) return;
        try { ctl.remove(row); refresh(); }
        catch (Exception ex){ Message.error(ex); }
    }

    private void refresh(){
        try { ctl.reloadAll(); model.setData(ctl.all()); }
        catch (Exception ex){ Message.error(ex); }
    }

    static class CliniciansTableModel extends AbstractTableModel {
        private List<Clinician> data;
        private final String[] cols = {"Clinician ID","First name","Last name","Title","Speciality","Phone","Email","Workplace ID","Status"};

        CliniciansTableModel(List<Clinician> data){ this.data = data; }
        void setData(List<Clinician> data){ this.data=data; fireTableDataChanged(); }

        public int getRowCount(){ return data.size(); }
        public int getColumnCount(){ return cols.length; }
        public String getColumnName(int c){ return cols[c]; }
        public Object getValueAt(int r,int c){
            Clinician x = data.get(r);
            return switch(c){
                case 0 -> x.clinicianId;
                case 1 -> x.firstName;
                case 2 -> x.lastName;
                case 3 -> x.title;
                case 4 -> x.speciality;
                case 5 -> x.phoneNumber;
                case 6 -> x.email;
                case 7 -> x.workplaceId;
                default -> x.employmentStatus;
            };
        }
    }
}
