/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpms;

import mpms.data.Charge;
import mpms.data.Player;

/**
 *
 * @author Ray
 */
public class PurchaseConfirmDialog extends javax.swing.JDialog {

    private Player player = null;
    
    /**
     * Creates new form PurchaseConfirmDialog
     * @param parent
     * @param player
     * @param charge
     */
    public PurchaseConfirmDialog(java.awt.Frame parent, Player player, Charge charge) {
        super(parent, true);
        initComponents();
        init(player, charge);
        setLocationRelativeTo(parent);
    }
    public PurchaseConfirmDialog(java.awt.Dialog parent, Player player, Charge charge) {
        super(parent, true);
        initComponents();
        init(player, charge);
        setLocationRelativeTo(parent);
    }
    
    private void init(Player player, Charge charge)
    {
        this.player = player;
        lblTitle.setText(player.getFirstName() + ", thank you for your purchase!");
        lblDescription.setText(Global.DOLLARS.format(charge.getAmount()) + ": " + charge.getDescription());        
        
        Player sponsor = player.getSponsor();
        if (sponsor == null)
        {
            lblInstructions.setText("You may see a Club Officer at any time to make payment.");
            btnPayNow.setVisible(true);
            btnPayLater.setVisible(true);
            btnOk.setVisible(false);
        }
        else
        {
            lblInstructions.setText("All charges will be paid by your sponsor, " + sponsor.toString() + ".");
            btnPayNow.setVisible(false);
            btnPayLater.setVisible(false);
            btnOk.setVisible(true);
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

        lblTitle = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        lblInstructions = new javax.swing.JLabel();
        pnlPayment = new javax.swing.JPanel();
        btnPayNow = new javax.swing.JButton();
        btnPayLater = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Purchase Confirmation");

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("[0], thank you for your purchase!");

        lblDescription.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDescription.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDescription.setText("<Purchase Description>");

        lblInstructions.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblInstructions.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInstructions.setText("You may see a Club Officer at any time to make payment.");

        btnPayNow.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnPayNow.setText("Pay Now");
        btnPayNow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayNowActionPerformed(evt);
            }
        });
        pnlPayment.add(btnPayNow);

        btnPayLater.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPayLater.setText("Pay Later");
        btnPayLater.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayLaterActionPerformed(evt);
            }
        });
        pnlPayment.add(btnPayLater);

        btnOk.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        pnlPayment.add(btnOk);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
                    .addComponent(lblDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlPayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblInstructions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(lblInstructions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPayNowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayNowActionPerformed
        PaymentDialog paymentDlg = new PaymentDialog(this, player);
        paymentDlg.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnPayNowActionPerformed

    private void btnPayLaterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayLaterActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_btnPayLaterActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_btnOkActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnPayLater;
    private javax.swing.JButton btnPayNow;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblInstructions;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlPayment;
    // End of variables declaration//GEN-END:variables
}
