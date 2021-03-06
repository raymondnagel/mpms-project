/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpms;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;
import static mpms.MpmsMain.Database;
import mpms.data.Player;

/**
 *
 * @author Ray
 */
public class AdminDialog extends javax.swing.JDialog {

    /**
     * Creates new form AdminDialog
     * @param parent
     */
    public AdminDialog(MpmsMain parent) {
        super(parent, true);
        initComponents();
        setSize(parent.getSize());
        setLocationRelativeTo(parent);
    }

    public void executeQuery()
    {
        try
        {
            String query = txaQuery.getText();
            ResultSet results = Database.executeQuery(query);
            TableModel model = Global.getTableModelFromResultSet(results);
            if (model != null)
            {
                tblResults.setModel(model);
                txaQuery.requestFocus();
                txaQuery.selectAll();            
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(MpmsMain.class.getName()).log(Level.SEVERE, null, ex);
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

        tabAdmin = new javax.swing.JTabbedPane();
        pnlTools = new javax.swing.JPanel();
        btnRefund = new javax.swing.JButton();
        btnEmailList = new javax.swing.JButton();
        btnExitMpms = new javax.swing.JButton();
        btnExitAdmin = new javax.swing.JButton();
        btnLedger = new javax.swing.JButton();
        btnAssignRating = new javax.swing.JButton();
        pnlQuery = new javax.swing.JPanel();
        scpQuery = new javax.swing.JScrollPane();
        txaQuery = new javax.swing.JTextArea();
        scpResults = new javax.swing.JScrollPane();
        tblResults = new javax.swing.JTable();
        pnlSounds = new javax.swing.JPanel();
        btnCheckInSound = new javax.swing.JButton();
        btnWelcomeSound = new javax.swing.JButton();
        btnLessonSound = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Administrator Panel");
        setUndecorated(true);

        btnRefund.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRefund.setText("Issue Refund");
        btnRefund.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefundActionPerformed(evt);
            }
        });

        btnEmailList.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnEmailList.setText("Get Email List");
        btnEmailList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmailListActionPerformed(evt);
            }
        });

        btnExitMpms.setBackground(new java.awt.Color(255, 51, 51));
        btnExitMpms.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnExitMpms.setText("Exit Program");
        btnExitMpms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitMpmsActionPerformed(evt);
            }
        });

        btnExitAdmin.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnExitAdmin.setText("Close Admin Mode");
        btnExitAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitAdminActionPerformed(evt);
            }
        });

        btnLedger.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLedger.setText("Show MPTTC Ledger");
        btnLedger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLedgerActionPerformed(evt);
            }
        });

        btnAssignRating.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnAssignRating.setText("Set Club Rating");
        btnAssignRating.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssignRatingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlToolsLayout = new javax.swing.GroupLayout(pnlTools);
        pnlTools.setLayout(pnlToolsLayout);
        pnlToolsLayout.setHorizontalGroup(
            pnlToolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlToolsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlToolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlToolsLayout.createSequentialGroup()
                        .addGap(0, 399, Short.MAX_VALUE)
                        .addComponent(btnExitMpms, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlToolsLayout.createSequentialGroup()
                        .addGroup(pnlToolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEmailList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnLedger, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnExitAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlToolsLayout.createSequentialGroup()
                                .addComponent(btnRefund, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAssignRating, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlToolsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAssignRating, btnEmailList, btnExitAdmin, btnExitMpms, btnRefund});

        pnlToolsLayout.setVerticalGroup(
            pnlToolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlToolsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlToolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAssignRating, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRefund, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnEmailList)
                .addGap(18, 18, 18)
                .addComponent(btnLedger)
                .addGap(18, 18, 18)
                .addComponent(btnExitAdmin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExitMpms)
                .addContainerGap())
        );

        pnlToolsLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnEmailList, btnExitAdmin, btnExitMpms, btnLedger, btnRefund});

        tabAdmin.addTab("Tools", pnlTools);

        scpQuery.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        txaQuery.setBackground(new java.awt.Color(0, 0, 0));
        txaQuery.setColumns(20);
        txaQuery.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        txaQuery.setForeground(new java.awt.Color(0, 255, 0));
        txaQuery.setRows(5);
        txaQuery.setCaretColor(new java.awt.Color(0, 255, 0));
        txaQuery.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txaQueryKeyReleased(evt);
            }
        });
        scpQuery.setViewportView(txaQuery);

        tblResults.setAutoCreateRowSorter(true);
        scpResults.setViewportView(tblResults);

        javax.swing.GroupLayout pnlQueryLayout = new javax.swing.GroupLayout(pnlQuery);
        pnlQuery.setLayout(pnlQueryLayout);
        pnlQueryLayout.setHorizontalGroup(
            pnlQueryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlQueryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlQueryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scpQuery)
                    .addComponent(scpResults, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlQueryLayout.setVerticalGroup(
            pnlQueryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlQueryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scpQuery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpResults, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabAdmin.addTab("Query", pnlQuery);

        btnCheckInSound.setText("Check In");
        btnCheckInSound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckInSoundActionPerformed(evt);
            }
        });

        btnWelcomeSound.setText("Welcome");
        btnWelcomeSound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWelcomeSoundActionPerformed(evt);
            }
        });

        btnLessonSound.setText("Lesson");
        btnLessonSound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLessonSoundActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSoundsLayout = new javax.swing.GroupLayout(pnlSounds);
        pnlSounds.setLayout(pnlSoundsLayout);
        pnlSoundsLayout.setHorizontalGroup(
            pnlSoundsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSoundsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSoundsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCheckInSound, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnWelcomeSound, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLessonSound, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(409, Short.MAX_VALUE))
        );
        pnlSoundsLayout.setVerticalGroup(
            pnlSoundsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSoundsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCheckInSound, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnWelcomeSound, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLessonSound, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(167, Short.MAX_VALUE))
        );

        tabAdmin.addTab("Sounds", pnlSounds);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabAdmin)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabAdmin)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txaQueryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txaQueryKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if (evt.isControlDown())
            {
                executeQuery();
            }
        }
    }//GEN-LAST:event_txaQueryKeyReleased

    private void btnExitMpmsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitMpmsActionPerformed
        MpmsMain.Database.closeConnection();
        System.exit(0);
    }//GEN-LAST:event_btnExitMpmsActionPerformed

    private void btnEmailListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmailListActionPerformed
        List<Player> players = Player.allFromDatabase(Player.PLAYER_LIST_MODE.ById);
        ArrayList<String> emails = new ArrayList<>();
        StringBuilder list = new StringBuilder();
        for (int p = 0; p < players.size(); p++)
        {
            if (players.get(p).getEmail() != null && !players.get(p).getEmail().trim().isEmpty())
            {
                if (!emails.contains(players.get(p).getEmail().trim()))
                {
                    if (p > 0)
                        list.append("; ");
                    list.append(players.get(p).getEmail().trim());
                }
                emails.add(players.get(p).getEmail().trim());
            }
        }
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        cb.setContents(new StringSelection(list.toString()), (Clipboard clpbrd, Transferable t) -> {
        });
    }//GEN-LAST:event_btnEmailListActionPerformed

    private void btnExitAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitAdminActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_btnExitAdminActionPerformed

    private void btnRefundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefundActionPerformed
        RefundDialog refundDlg = new RefundDialog(this);
        refundDlg.setVisible(true);
    }//GEN-LAST:event_btnRefundActionPerformed

    private void btnLedgerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLedgerActionPerformed
        LedgerDialog ledgerDlg = new LedgerDialog(this);
        ledgerDlg.setVisible(true);
    }//GEN-LAST:event_btnLedgerActionPerformed

    private void btnCheckInSoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckInSoundActionPerformed
        Global.playSound("check_in");
    }//GEN-LAST:event_btnCheckInSoundActionPerformed

    private void btnWelcomeSoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWelcomeSoundActionPerformed
        Global.playSound("welcome_new");
    }//GEN-LAST:event_btnWelcomeSoundActionPerformed

    private void btnLessonSoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLessonSoundActionPerformed
        Global.playSound("lesson");
    }//GEN-LAST:event_btnLessonSoundActionPerformed

    private void btnAssignRatingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssignRatingActionPerformed
        String idStr = JOptionPane.showInputDialog(this, "Please enter the player id:", null);
        try {
        int id = Integer.parseInt(idStr);
        if (id < 0)
        {
            JOptionPane.showMessageDialog(this, "The id must be positive integer.", "Invalid Id", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Player player = Player.fromDatabase(id);
        if (player != null)
        {
            String ratingStr = JOptionPane.showInputDialog(this, "Please enter the new rating for " + player.getLastFirst() + ":", null);
            try {
                int rating = Integer.parseInt(ratingStr);
                
                player.setClubRating(rating);
                player.setRatingAcc(0.3);
                player.updateToDatabase();
                
                JOptionPane.showMessageDialog(this, player.getName() + "'s rating was successfully set to " + player.getClubRating() + ".");
                
            } catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(this, "The rating must be an integer.", "Invalid Rating", JOptionPane.ERROR_MESSAGE);
            }            
        }
        
        } catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "The id must be an integer.", "Invalid Id", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "The id does not exist in the database.", "Invalid Id", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAssignRatingActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAssignRating;
    private javax.swing.JButton btnCheckInSound;
    private javax.swing.JButton btnEmailList;
    private javax.swing.JButton btnExitAdmin;
    private javax.swing.JButton btnExitMpms;
    private javax.swing.JButton btnLedger;
    private javax.swing.JButton btnLessonSound;
    private javax.swing.JButton btnRefund;
    private javax.swing.JButton btnWelcomeSound;
    private javax.swing.JPanel pnlQuery;
    private javax.swing.JPanel pnlSounds;
    private javax.swing.JPanel pnlTools;
    private javax.swing.JScrollPane scpQuery;
    private javax.swing.JScrollPane scpResults;
    private javax.swing.JTabbedPane tabAdmin;
    private javax.swing.JTable tblResults;
    private javax.swing.JTextArea txaQuery;
    // End of variables declaration//GEN-END:variables
}
