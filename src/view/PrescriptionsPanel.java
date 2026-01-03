package view;

import controller.PrescriptionController;
import model.Prescription;
import persistence.Repository;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class PrescriptionsPanel extends JPanel {
    private final PrescriptionController ctl;
    private final JTable table;
    private final PrescriptionsTableModel model;
    private final CrudHeader header;
    private final JButton export = new JButton("Export TXT");

    public PrescriptionsPanel(Frame owner, PrescriptionController ctl) {
        super(new BorderLayout(12,12));
        this.ctl = ctl;

        header = new CrudHeader("Prescriptions", "Add/Edit/Delete/Export Prescriptions • Data saved/fetched from CSV");
        JPanel headerWrap = new JPanel(new BorderLayout());
        headerWrap.setOpaque(false);
        headerWrap.add(header, BorderLayout.CENTER);

        JPanel extra = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,5));
        extra.setOpaque(false);
        extra.add(export);
        headerWrap.add(extra, BorderLayout.SOUTH);

        model = new PrescriptionsTableModel(ctl.all());
        table = new JTable(model);
        Ui.styleTable(table);
        FilterUtil.attach(header.search, table);

        header.add.addActionListener(e -> addPrescription(owner));
        header.edit.addActionListener(e -> editPrescription(owner));
        header.del.addActionListener(e -> deletePrescription());
        header.refresh.addActionListener(e -> refresh());
        export.addActionListener(e -> exportSelected());

        add(Ui.pad(headerWrap), BorderLayout.NORTH);
        add(Ui.pad(new JScrollPane(table)), BorderLayout.CENTER);
    }

    private void addPrescription(Frame owner){
        try {
            Prescription seed = ctl.blank();
            String[] labels = {"Patient ID","Clinician ID","Appointment ID","Medication","Dosage","Frequency","Status"};
            String[] init = {"","","","", "", "", "Issued"};
            SimpleFormDialog d = new SimpleFormDialog(owner, "New Prescription", labels, init, new int[]{0,1,3});
            d.setVisible(true);
            String[] r = d.result();
            if (r == null) return;

            String today = Repository.today();
            Prescription p = new Prescription(seed.prescriptionId, r[0], r[1], r[2], today, r[3], r[4], r[5], "", "", "", "", r[6], today, "");
            ctl.add(p);
            refresh();
        } catch (Exception ex){ Message.error(ex); }
    }

    private void editPrescription(Frame owner){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select a prescription first."); return; }
        int row = table.convertRowIndexToModel(v);

        try {
            Prescription p0 = ctl.all().get(row);
            String[] labels = {"Prescription ID","Patient ID","Clinician ID","Appointment ID","Medication","Dosage","Frequency","Status"};
            String[] init = {p0.prescriptionId,p0.patientId,p0.clinicianId,p0.appointmentId,p0.medicationName,p0.dosage,p0.frequency,p0.status};
            SimpleFormDialog d = new SimpleFormDialog(owner, "Edit Prescription", labels, init, new int[]{1,2,4}, new int[]{0});
            d.setVisible(true);
            String[] r = d.result();
            if (r == null) return;

            Prescription p = new Prescription(p0.prescriptionId, r[1], r[2], r[3], p0.prescriptionDate, r[4], r[5], r[6],
                    p0.durationDays, p0.quantity, p0.instructions, p0.pharmacyName, r[7], p0.issueDate, p0.collectionDate);
            ctl.set(row, p);
            refresh();
        } catch (Exception ex){ Message.error(ex); }
    }

    private void deletePrescription(){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select a prescription first."); return; }
        int row = table.convertRowIndexToModel(v);

        if (!Message.confirm("Delete selected prescription? This will auto-save to CSV.")) return;
        try { ctl.remove(row); refresh(); }
        catch (Exception ex){ Message.error(ex); }
    }

    private void exportSelected(){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select a prescription first."); return; }
        int row = table.convertRowIndexToModel(v);
        try {
            File f = ctl.exportTxt(ctl.all().get(row));
            Message.info("Exported: " + f.getPath());
        } catch (Exception ex){ Message.error(ex); }
    }

    private void refresh(){
        try { ctl.reloadAll(); model.setData(ctl.all()); }
        catch (Exception ex){ Message.error(ex); }
    }

    static class PrescriptionsTableModel extends AbstractTableModel {
        private List<Prescription> data;
        private final String[] cols = {"Prescription ID","Patient ID","Clinician ID","Appointment ID","Medication","Dosage","Frequency","Status"};

        PrescriptionsTableModel(List<Prescription> data){ this.data = data; }
        void setData(List<Prescription> data){ this.data=data; fireTableDataChanged(); }

        public int getRowCount(){ return data.size(); }
        public int getColumnCount(){ return cols.length; }
        public String getColumnName(int c){ return cols[c]; }
        public Object getValueAt(int r,int c){
            Prescription x = data.get(r);
            return switch(c){
                case 0 -> x.prescriptionId;
                case 1 -> x.patientId;
                case 2 -> x.clinicianId;
                case 3 -> x.appointmentId;
                case 4 -> x.medicationName;
                case 5 -> x.dosage;
                case 6 -> x.frequency;
                default -> x.status;
            };
        }
    }
}
