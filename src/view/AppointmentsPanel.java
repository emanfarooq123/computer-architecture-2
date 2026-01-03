package view;

import controller.AppointmentController;
import model.Appointment;
import persistence.Repository;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentsPanel extends JPanel {
    private final AppointmentController ctl;
    private final JTable table;
    private final AppointmentsTableModel model;
    private final CrudHeader header;

    public AppointmentsPanel(Frame owner, AppointmentController ctl) {
        super(new BorderLayout(12,12));
        this.ctl = ctl;

        header = new CrudHeader("Appointments", "Add/Edit/Delete Appointments • Data saved/fetched from CSV");
        model = new AppointmentsTableModel(ctl.all());
        table = new JTable(model);
        Ui.styleTable(table);
        FilterUtil.attach(header.search, table);

        header.add.addActionListener(e -> addAppointment(owner));
        header.edit.addActionListener(e -> editAppointment(owner));
        header.del.addActionListener(e -> deleteAppointment());
        header.refresh.addActionListener(e -> refresh());

        add(Ui.pad(header), BorderLayout.NORTH);
        add(Ui.pad(new JScrollPane(table)), BorderLayout.CENTER);
    }

    private void addAppointment(Frame owner){
        try {
            Appointment seed = ctl.blank();
            String[] labels = {"Patient ID","Clinician ID","Facility ID","Date (YYYY-MM-DD)","Time (HH:MM)","Status","Reason for visit"};
            String[] init = {"","","", Repository.today(),"","Booked",""};
            SimpleFormDialog d = new SimpleFormDialog(owner, "New Appointment", labels, init, new int[]{0,1,3});
            d.setVisible(true);
            String[] r = d.result();
            if (r == null) return;

            String today = Repository.today();
            Appointment a = new Appointment(seed.appointmentId, r[0], r[1], r[2], r[3], r[4], seed.durationMinutes, seed.appointmentType, r[5], r[6], "", today, today);
            ctl.add(a);
            refresh();
        } catch (Exception ex){ Message.error(ex); }
    }

    private void editAppointment(Frame owner){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select an appointment first."); return; }
        int row = table.convertRowIndexToModel(v);

        try {
            Appointment a0 = ctl.all().get(row);
            String[] labels = {"Appointment ID","Patient ID","Clinician ID","Facility ID","Date (YYYY-MM-DD)","Time (HH:MM)","Status","Reason for visit"};
            String[] init = {a0.appointmentId,a0.patientId,a0.clinicianId,a0.facilityId,a0.appointmentDate,a0.appointmentTime,a0.status,a0.reasonForVisit};
            SimpleFormDialog d = new SimpleFormDialog(owner, "Edit Appointment", labels, init, new int[]{1,2,4}, new int[]{0});
            d.setVisible(true);
            String[] r = d.result();
            if (r == null) return;

            Appointment a = new Appointment(a0.appointmentId, r[1], r[2], r[3], r[4], r[5], a0.durationMinutes, a0.appointmentType,
                    r[6], r[7], a0.notes, a0.createdDate, Repository.today());
            ctl.set(row, a);
            refresh();
        } catch (Exception ex){ Message.error(ex); }
    }

    private void deleteAppointment(){
        int v = table.getSelectedRow();
        if (v < 0) { Message.info("Select an appointment first."); return; }
        int row = table.convertRowIndexToModel(v);

        if (!Message.confirm("Delete selected appointment? This will auto-save to CSV.")) return;
        try { ctl.remove(row); refresh(); }
        catch (Exception ex){ Message.error(ex); }
    }

    private void refresh(){
        try { ctl.reloadAll(); model.setData(ctl.all()); }
        catch (Exception ex){ Message.error(ex); }
    }

    static class AppointmentsTableModel extends AbstractTableModel {
        private List<Appointment> data;
        private final String[] cols = {"Appointment ID","Patient ID","Clinician ID","Facility ID","Date","Time","Status","Reason"};

        AppointmentsTableModel(List<Appointment> data){ this.data = data; }
        void setData(List<Appointment> data){ this.data=data; fireTableDataChanged(); }

        public int getRowCount(){ return data.size(); }
        public int getColumnCount(){ return cols.length; }
        public String getColumnName(int c){ return cols[c]; }
        public Object getValueAt(int r,int c){
            Appointment x = data.get(r);
            return switch(c){
                case 0 -> x.appointmentId;
                case 1 -> x.patientId;
                case 2 -> x.clinicianId;
                case 3 -> x.facilityId;
                case 4 -> x.appointmentDate;
                case 5 -> x.appointmentTime;
                case 6 -> x.status;
                default -> x.reasonForVisit;
            };
        }
    }
}
