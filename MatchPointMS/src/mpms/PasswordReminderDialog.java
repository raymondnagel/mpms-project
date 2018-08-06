/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpms;

import java.awt.CardLayout;
import java.awt.Color;
import mpms.data.Player;

/**
 *
 * @author Ray
 */
public class PasswordReminderDialog extends javax.swing.JDialog {

    private Player player = null;
    private int value = 255;
    private boolean reminded = false;
    
    /**
     * Creates new form PasswordReminderDialog
     * @param parent
     * @param player
     */
    public PasswordReminderDialog(java.awt.Frame parent, Player player) {
        super(parent, true);
        initComponents();
        setLocationRelativeTo(parent);
        this.player = player;
    }

    public boolean isReminded()
    {
        return this.reminded;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlCards = new javax.swing.JPanel();
        pnlAdmin = new javax.swing.JPanel();
        lblAdminPassword = new javax.swing.JLabel();
        pwdAdminPassword = new javax.swing.JPasswordField();
        pnlPassword = new javax.swing.JPanel();
        lblInstructions = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        pnlContinue = new javax.swing.JPanel();
        btnContinue = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Password Reminder");

        pnlCards.setLayout(new java.awt.CardLayout());

        lblAdminPassword.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblAdminPassword.setText("Enter Admin Password:");

        pwdAdminPassword.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        pwdAdminPassword.setEchoChar('\u2022');
        pwdAdminPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pwdAdminPasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAdminLayout = new javax.swing.GroupLayout(pnlAdmin);
        pnlAdmin.setLayout(pnlAdminLayout);
        pnlAdminLayout.setHorizontalGroup(
            pnlAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdminLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblAdminPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pwdAdminPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pnlAdminLayout.setVerticalGroup(
            pnlAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdminLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAdminPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pwdAdminPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        pnlCards.add(pnlAdmin, "card2");

        lblInstructions.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblInstructions.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInstructions.setText("Drag mouse over the box below to reveal password:");

        lblPassword.setBackground(new java.awt.Color(0, 255, 0));
        lblPassword.setFont(new java.awt.Font("Courier New", 0, 18)); // NOI18N
        lblPassword.setForeground(new java.awt.Color(0, 255, 0));
        lblPassword.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPassword.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        lblPassword.setOpaque(true);
        lblPassword.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                lblPasswordMouseDragged(evt);
            }
        });

        btnContinue.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnContinue.setText("Proceed to Account");
        btnContinue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContinueActionPerformed(evt);
            }
        });
        pnlContinue.add(btnContinue);

        javax.swing.GroupLayout pnlPasswordLayout = new javax.swing.GroupLayout(pnlPassword);
        pnlPassword.setLayout(pnlPasswordLayout);
        pnlPasswordLayout.setHorizontalGroup(
            pnlPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPasswordLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInstructions, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
                    .addComponent(lblPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlContinue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlPasswordLayout.setVerticalGroup(
            pnlPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPasswordLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInstructions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(pnlContinue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlCards.add(pnlPassword, "card3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pwdAdminPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwdAdminPasswordActionPerformed
        String password = new String(pwdAdminPassword.getPassword());
        pwdAdminPassword.setText("");
        if (Global.AUTHENTICATION_PASSWORD.equals(password))
        {
            lblPassword.setText(player.getPassword());
            ((CardLayout)pnlCards.getLayout()).last(pnlCards);
        }
    }//GEN-LAST:event_pwdAdminPasswordActionPerformed

    private void lblPasswordMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPasswordMouseDragged
        if (value > 0)
        {
            value--;
            lblPassword.setForeground(new Color(0, value, 0));
        }
    }//GEN-LAST:event_lblPasswordMouseDragged

    private void btnContinueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContinueActionPerformed
        reminded = true;
        this.setVisible(false);
    }//GEN-LAST:event_btnContinueActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnContinue;
    private javax.swing.JLabel lblAdminPassword;
    private javax.swing.JLabel lblInstructions;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JPanel pnlAdmin;
    private javax.swing.JPanel pnlCards;
    private javax.swing.JPanel pnlContinue;
    private javax.swing.JPanel pnlPassword;
    private javax.swing.JPasswordField pwdAdminPassword;
    // End of variables declaration//GEN-END:variables
}
