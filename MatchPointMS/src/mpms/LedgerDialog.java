/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpms;

import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ray
 */
public class LedgerDialog extends javax.swing.JDialog {

    /**
     * Creates new form LedgerDialog
     * @param parent
     */
    public LedgerDialog(java.awt.Dialog parent) {
        super(parent, true);
        initComponents();
        tblTable.setDefaultRenderer(Double.class, new LedgerTableRenderer());
        tblTable.setDefaultRenderer(Date.class, new LedgerTableRenderer());
        
        Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int y = 2015; y <= currentYear; y++)
        {
            cboYear.addItem(y);
        }
        cboYear.setSelectedItem(currentYear); 
        init(currentYear);
        cboYear.addActionListener((ActionEvent e) -> {
            int year = (Integer)cboYear.getSelectedItem();
            init(year);
        });
        setSize(parent.getSize());
        setLocationRelativeTo(parent);          
    }

    private void init(int year)
    {
        try {

            ResultSet results = MpmsMain.Database.extractLedger(year);
            results.first();
            MultiTypeTableModel model = Global.getLedgerModelFromResultSet(results);
            
            int amtCol = -1;           
            for (int c = 0; c < model.getColumnCount(); c++)
            {
                if (model.getColumnName(c).equalsIgnoreCase("AMOUNT"))
                {
                    amtCol = c;
                }
            }
            double revenue = 0.00;
            for (int r = 0; r < model.getRowCount(); r++)
            {
                double amount = (Double)model.getValueAt(r, amtCol);
                revenue += amount;
            }
            
            
            
            tblTable.setModel(model);
            txtRevenue.setText(Global.DOLLARS.format(revenue));
            
        } catch (SQLException ex) {
            Logger.getLogger(PlayerTransactHistoryDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scpTable = new javax.swing.JScrollPane();
        tblTable = new javax.swing.JTable();
        pnlCenterClose = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        lblRevenue = new javax.swing.JLabel();
        txtRevenue = new javax.swing.JTextField();
        cboYear = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MPTTC Ledger");
        setAlwaysOnTop(true);
        setUndecorated(true);

        tblTable.setAutoCreateRowSorter(true);
        tblTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        scpTable.setViewportView(tblTable);

        btnClose.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        pnlCenterClose.add(btnClose);

        lblRevenue.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblRevenue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRevenue.setText("Revenue:");

        txtRevenue.setEditable(false);
        txtRevenue.setBackground(new java.awt.Color(255, 255, 255));
        txtRevenue.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtRevenue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlCenterClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cboYear, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRevenue, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRevenue, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(scpTable, javax.swing.GroupLayout.DEFAULT_SIZE, 804, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scpTable, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRevenue, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRevenue, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlCenterClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cboYear, lblRevenue, txtRevenue});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JComboBox cboYear;
    private javax.swing.JLabel lblRevenue;
    private javax.swing.JPanel pnlCenterClose;
    private javax.swing.JScrollPane scpTable;
    private javax.swing.JTable tblTable;
    private javax.swing.JTextField txtRevenue;
    // End of variables declaration//GEN-END:variables
}
