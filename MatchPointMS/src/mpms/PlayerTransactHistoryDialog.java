/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpms;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpms.data.Player;

/**
 *
 * @author Ray
 */
public class PlayerTransactHistoryDialog extends javax.swing.JDialog {

    /**
     * Creates new form PlayerTransactHistoryDialog
     * @param parent
     * @param player
     */
    public PlayerTransactHistoryDialog(java.awt.Dialog parent, Player player) {
        super(parent, true);
        initComponents();
        tblTable.setDefaultRenderer(Double.class, new TransHistoryRenderer());
        tblTable.setDefaultRenderer(Date.class, new TransHistoryRenderer());
        
        init(player);
        setLocationRelativeTo(parent);        
    }

    private void init(Player player)
    {
        try {
            
            ResultSet results = MpmsMain.Database.extractPlayerTransactHistory(player.getId());
            results.first();
            MultiTypeTableModel model = Global.getTableModelFromResultSet(results);
            tblTable.setModel(model);
            lblTitle.setText("Transaction History for " + player.toString());
            txtBalance.setText(Global.DOLLARS.format(player.getBalance()));
            
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
        lblTitle = new javax.swing.JLabel();
        pnlCenterClose = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        lblBalance = new javax.swing.JLabel();
        txtBalance = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Player Transaction History");

        tblTable.setAutoCreateRowSorter(true);
        tblTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        scpTable.setViewportView(tblTable);

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Transaction History for _");

        btnClose.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        pnlCenterClose.add(btnClose);

        lblBalance.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBalance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBalance.setText("Current Balance:");

        txtBalance.setEditable(false);
        txtBalance.setBackground(new java.awt.Color(255, 255, 255));
        txtBalance.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtBalance.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scpTable, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                    .addComponent(pnlCenterClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpTable, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlCenterClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JLabel lblBalance;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlCenterClose;
    private javax.swing.JScrollPane scpTable;
    private javax.swing.JTable tblTable;
    private javax.swing.JTextField txtBalance;
    // End of variables declaration//GEN-END:variables
}
