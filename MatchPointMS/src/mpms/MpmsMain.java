/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpms;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import mpms.data.Player;
import mpms.data.TTMatch;

/**
 *
 * @author Ray
 */
public final class MpmsMain extends javax.swing.JFrame {
    
    public static MpDb Database = new MpDb();

    /**
     * Creates new form MpmsMain
     */
    public MpmsMain() {
        initComponents();
        try {
            BufferedImage img = ImageIO.read(new File("res/MP_icon.png"));
            this.setIconImage(img);
        } catch (IOException ex) {
            Logger.getLogger(MpmsMain.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
        tblRosterPlayers.setDefaultRenderer(Double.class, new RosterTableRenderer());
        
        startDatabase();
                
        refreshTabData();
        pwdPassword.addActionListener((ActionEvent e) ->
        {
            Player player = pdLoginDirectory.getSelectedPlayer();
            if (player != null)
            {
                checkIn(player, new String(pwdPassword.getPassword()));
            }
            else
            {
                pwdPassword.setText("");
                pwdPassword.setEnabled(false);
            }
        });
        pdLoginDirectory.addActionListener((ActionEvent e) ->
        {
            checkCurrentPlayerPassword();
        });
        
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        checkCurrentPlayerPassword();
    }
    
    private static void startDatabase()
    {
        Database.openConnection();
        Database.createDatabase(false);
        Database.prepareStatements();
    }
    
    private void checkCurrentPlayerPassword()
    {
        Player player = pdLoginDirectory.getSelectedPlayer();
        if (player != null)
        {
            if (player.getPassword() == null || player.getPassword().isEmpty())
            {
                pwdPassword.setEnabled(false);                    
                btnCheckIn.requestFocus();
            }
            else            
            {
                pwdPassword.setEnabled(true);
                pwdPassword.requestFocus();
            }
        }
    }
    
    public void checkIn(Player player, String password)
    {
        pwdPassword.setText("");        
        boolean noPassword = (player.getPassword() == null || player.getPassword().isEmpty());
        
        if (noPassword || player.getPassword().equals(password))
        {
            checkInSuccess(player);
        }
        else
        {
            checkInFailure(player);
        }
    }
    
    private void checkInSuccess(Player player)
    {
        PlayerAccountDialog accountDlg = new PlayerAccountDialog(this, player);
        accountDlg.setVisible(true);
        refreshTabData();
        checkCurrentPlayerPassword();
    }
    
    private void checkInFailure(Player player)
    {
        String[] options = {"Cancel", "Remind"};
        int choice = JOptionPane.showOptionDialog(this, "The password you entered was incorrect.", "Login Failed", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, null);
        if (choice == 1)
        {
            PasswordReminderDialog prDlg = new PasswordReminderDialog(this, pdLoginDirectory.getSelectedPlayer());
            prDlg.setVisible(true);
            if (prDlg.isReminded())
            {
                checkInSuccess(player);
            }
        }
    }
    
    public void refreshTabData()
    {
        try {
            // CheckIn tab
            pdLoginDirectory.loadPlayersByLetter('*');
            pwdPassword.setText("");
            pwdPassword.setEnabled(false);
            
            // Today tab
            ResultSet todayResults = MpmsMain.Database.extractVisitorsForDate(Global.getTodayDate());
            todayResults.first();
            TableModel todayModel = Global.getTableModelFromResultSet(todayResults);
            if (todayModel != null)
            {
                tblTodayPlayers.setModel(todayModel);
            }
            
            // Roster tab
            ResultSet rosterResults = MpmsMain.Database.extractRoster();
            rosterResults.first();
            TableModel rosterModel = Global.getTableModelFromResultSet(rosterResults);
            if (rosterModel != null)
            {
                tblRosterPlayers.setModel(rosterModel);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MpmsMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean checkPasswordsReady()
    {
        Player winner = (Player)cboWinner.getSelectedItem();
        Player loser = (Player)cboLoser.getSelectedItem();
        boolean playersSet = (winner != null) && (loser != null) && (winner != loser);
        pwdWinnerPassword.setEnabled(playersSet);
        pwdLoserPassword.setEnabled(playersSet);        
        btnLogMatchResult.setEnabled(playersSet);
        return playersSet;
    }
    
    public boolean checkMatchPasswords()
    {
        Player winner = (Player)cboWinner.getSelectedItem();
        Player loser = (Player)cboLoser.getSelectedItem();
        boolean playersSet = (winner != null) && (loser != null) && (winner != loser);
                        
        if (playersSet)
        {
            String winnerPwd = new String(pwdWinnerPassword.getPassword());
            String loserPwd = new String(pwdLoserPassword.getPassword());
            String adminPwd = new String(pwdAdminMatchPassword.getPassword());
            
            boolean adminPwdOk = (adminPwd != null) && Global.AUTHENTICATION_PASSWORD.equals(adminPwd);
            boolean winnerPwdOk = adminPwdOk || (winner.getPassword() != null && winner.getPassword().equals(winnerPwd));
            boolean loserPwdOk = adminPwdOk || (loser.getPassword() != null && loser.getPassword().equals(loserPwd));
            
            if (!winnerPwdOk)
            {
                JOptionPane.showMessageDialog(this, "The winner's password is incorrect. Please try again, or enter the administrator password.");
                return false;
            }
            if (!loserPwdOk)
            {
                JOptionPane.showMessageDialog(this, "The loser's password is incorrect. Please try again, or enter the administrator password.");
                return false;
            }
            return true;
        }
        return false;
    }
    
    public void logMatch(Player winner, Player loser)
    {
        int oldWinnerRating = winner.getClubRating() == null ? 0 : winner.getClubRating();
        int oldLoserRating = loser.getClubRating() == null ? 0 : loser.getClubRating();
        int winnerRating = 0;
        int loserRating = 0;
        
        if (oldWinnerRating == 0 && oldLoserRating == 0)
        {
            JOptionPane.showMessageDialog(this, "This result cannot be proccessed since neither player has a rating yet.\nOne player must play a match vs. a rated player to acquire a rating before logging this match.");            
        }
        else if (oldWinnerRating == 0 || oldLoserRating == 0)
        {
            // The un rated player will get an initial rating, and the other player's rating will not be changed:
            GameScoresDialog dialog = new GameScoresDialog(this, true);
            dialog.passPlayers(winner, loser);
            dialog.setVisible(true);
        }
        else
        {
            // Both ratings may be updated:
            int ratingSpread = oldWinnerRating - oldLoserRating;
            int pointsExchanged = 0;            
            
            if (ratingSpread >= 0)
            {
                // Expected result:
                if (Math.abs(ratingSpread) <= 12)
                    pointsExchanged = 8;
                else if (Math.abs(ratingSpread) <= 37)
                    pointsExchanged = 7;
                else if (Math.abs(ratingSpread) <= 62)
                    pointsExchanged = 6;
                else if (Math.abs(ratingSpread) <= 87)
                    pointsExchanged = 5;
                else if (Math.abs(ratingSpread) <= 112)
                    pointsExchanged = 4;
                else if (Math.abs(ratingSpread) <= 137)
                    pointsExchanged = 3;
                else if (Math.abs(ratingSpread) <= 162)
                    pointsExchanged = 2;
                else if (Math.abs(ratingSpread) <= 187)
                    pointsExchanged = 2;
                else if (Math.abs(ratingSpread) <= 212)
                    pointsExchanged = 1;
                else if (Math.abs(ratingSpread) <= 237)
                    pointsExchanged = 1;
                else if (Math.abs(ratingSpread) >= 238)
                    pointsExchanged = 0;
            }
            else
            {
                // Upset result:
                if (Math.abs(ratingSpread) <= 12)
                    pointsExchanged = 8;
                else if (Math.abs(ratingSpread) <= 37)
                    pointsExchanged = 10;
                else if (Math.abs(ratingSpread) <= 62)
                    pointsExchanged = 13;
                else if (Math.abs(ratingSpread) <= 87)
                    pointsExchanged = 16;
                else if (Math.abs(ratingSpread) <= 112)
                    pointsExchanged = 20;
                else if (Math.abs(ratingSpread) <= 137)
                    pointsExchanged = 25;
                else if (Math.abs(ratingSpread) <= 162)
                    pointsExchanged = 30;
                else if (Math.abs(ratingSpread) <= 187)
                    pointsExchanged = 35;
                else if (Math.abs(ratingSpread) <= 212)
                    pointsExchanged = 40;
                else if (Math.abs(ratingSpread) <= 237)
                    pointsExchanged = 45;
                else if (Math.abs(ratingSpread) >= 238)
                    pointsExchanged = 50;
            }
            
            winnerRating = oldWinnerRating + pointsExchanged;
            loserRating = oldLoserRating - pointsExchanged;
            
            double winnerAcc = winner.getRatingAcc();                       
            double loserAcc = loser.getRatingAcc();
            if (pointsExchanged > 0)
            {
                winnerAcc = ((winnerAcc + 1.0) *.5);
                loserAcc = ((loserAcc + 1.0) *.5);
                
                double wDiffPct = (double)pointsExchanged / (double)winnerRating;
                double lDiffPct = (double)pointsExchanged / (double)loserRating;
                wDiffPct *= 3.0;
                lDiffPct *= 3.0;
                winnerAcc -= wDiffPct;
                loserAcc -= lDiffPct;
            }
            
            int winPct = (int)(winnerAcc*100.0);
            int losPct = (int)(loserAcc*100.0);
            
            winner.setClubRating(winnerRating);
            winner.setRatingAcc(winnerAcc);
            loser.setClubRating(loserRating);
            loser.setRatingAcc(loserAcc);
            
            winner.updateToDatabase();
            loser.updateToDatabase();
            MpmsMain.Database.insertTTMatch(TTMatch.makeMatch(winner, loser, Math.abs(pointsExchanged)));
            
            JOptionPane.showMessageDialog(this, "Winner: " + winner.getName() + " + " + pointsExchanged + " = " + winnerRating + " ("+winPct+"%)\nLoser: " + loser.getName() + " - " + pointsExchanged + " = " + loserRating + " ("+losPct+"%)", "Match Result", JOptionPane.INFORMATION_MESSAGE);
        }         
        
        refreshTabData();
        resetLogMatch();
    }

    public void resetLogMatch()
    {
        // Load today's visitors into each combobox:        
        DefaultComboBoxModel winnerModel = new DefaultComboBoxModel();
        DefaultComboBoxModel loserModel = new DefaultComboBoxModel();
        List<Player> players = Player.allTodayFromDatabase();
        if (players != null && !players.isEmpty())
        {
            for (int p = 0; p < players.size(); p++)
            {
                winnerModel.addElement(players.get(p));
                loserModel.addElement(players.get(p));
            }
        }
        cboWinner.setModel(winnerModel);
        cboLoser.setModel(loserModel);
        
        // Clear password fields:
        pwdWinnerPassword.setText("");
        pwdLoserPassword.setText("");
        pwdAdminMatchPassword.setText("");
        
        // Disable log match button:
        btnLogMatchResult.setEnabled(false);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabTabs = new javax.swing.JTabbedPane();
        pnlCheckIn = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        lblMatchPointLogo = new javax.swing.JLabel();
        pnlCenterDirectory = new javax.swing.JPanel();
        pnlLogin = new javax.swing.JPanel();
        pnlLoginControls = new javax.swing.JPanel();
        pdLoginDirectory = new mpms.PlayerDirectory();
        pnlPassword = new javax.swing.JPanel();
        pwdPassword = new javax.swing.JPasswordField();
        lblPassword = new javax.swing.JLabel();
        btnCheckIn = new javax.swing.JButton();
        lblRegisterNew = new javax.swing.JLabel();
        pnlLogMatch = new javax.swing.JPanel();
        lblLogMatch = new javax.swing.JLabel();
        lblWinner = new javax.swing.JLabel();
        cboWinner = new javax.swing.JComboBox<>();
        lblLoser = new javax.swing.JLabel();
        cboLoser = new javax.swing.JComboBox<>();
        pwdWinnerPassword = new javax.swing.JPasswordField();
        pwdLoserPassword = new javax.swing.JPasswordField();
        lblWinnerPassword = new javax.swing.JLabel();
        lblLoserPassword = new javax.swing.JLabel();
        lblOrAdmin = new javax.swing.JLabel();
        pwdAdminMatchPassword = new javax.swing.JPasswordField();
        lblAdminMatchPassword = new javax.swing.JLabel();
        btnLogMatchResult = new javax.swing.JButton();
        pnlToday = new javax.swing.JPanel();
        scpTodayPlayers = new javax.swing.JScrollPane();
        tblTodayPlayers = new javax.swing.JTable();
        pnlPlayers = new javax.swing.JPanel();
        scpRosterPlayers = new javax.swing.JScrollPane();
        tblRosterPlayers = new javax.swing.JTable();
        pnlAdmin = new javax.swing.JPanel();
        lblAdminPassword = new javax.swing.JLabel();
        pwdAdminPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("MatchPoint Management System");
        setUndecorated(true);

        pnlCheckIn.setBackground(new java.awt.Color(51, 102, 255));

        lblWelcome.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWelcome.setText("WELCOME TO");

        lblMatchPointLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMatchPointLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpms/resources/MatchPointTTC_Logo_med.png"))); // NOI18N

        pnlCenterDirectory.setOpaque(false);

        pnlLogin.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        pnlLoginControls.setPreferredSize(new java.awt.Dimension(670, 280));
        pnlLoginControls.add(pdLoginDirectory);

        pwdPassword.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        pwdPassword.setEchoChar('\u2022');

        lblPassword.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblPassword.setText("Password:");

        javax.swing.GroupLayout pnlPasswordLayout = new javax.swing.GroupLayout(pnlPassword);
        pnlPassword.setLayout(pnlPasswordLayout);
        pnlPasswordLayout.setHorizontalGroup(
            pnlPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPasswordLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pwdPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlPasswordLayout.setVerticalGroup(
            pnlPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPasswordLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword)
                    .addComponent(pwdPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlLoginControls.add(pnlPassword);

        btnCheckIn.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        btnCheckIn.setText("Check In");
        btnCheckIn.setPreferredSize(new java.awt.Dimension(400, 80));
        btnCheckIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckInActionPerformed(evt);
            }
        });
        pnlLoginControls.add(btnCheckIn);

        lblRegisterNew.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRegisterNew.setForeground(new java.awt.Color(0, 51, 255));
        lblRegisterNew.setText("                                        Register New Player                                        ");
        lblRegisterNew.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblRegisterNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblRegisterNewMousePressed(evt);
            }
        });
        pnlLoginControls.add(lblRegisterNew);

        javax.swing.GroupLayout pnlLoginLayout = new javax.swing.GroupLayout(pnlLogin);
        pnlLogin.setLayout(pnlLoginLayout);
        pnlLoginLayout.setHorizontalGroup(
            pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlLoginControls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlLoginLayout.setVerticalGroup(
            pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlLoginControls, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlCenterDirectory.add(pnlLogin);

        javax.swing.GroupLayout pnlCheckInLayout = new javax.swing.GroupLayout(pnlCheckIn);
        pnlCheckIn.setLayout(pnlCheckInLayout);
        pnlCheckInLayout.setHorizontalGroup(
            pnlCheckInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCheckInLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCheckInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblWelcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblMatchPointLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 941, Short.MAX_VALUE)
                    .addComponent(pnlCenterDirectory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCheckInLayout.setVerticalGroup(
            pnlCheckInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCheckInLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWelcome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMatchPointLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlCenterDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(168, Short.MAX_VALUE))
        );

        tabTabs.addTab("Check In", pnlCheckIn);

        pnlLogMatch.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                pnlLogMatchComponentShown(evt);
            }
        });

        lblLogMatch.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblLogMatch.setText("Log a Match");

        lblWinner.setText("Select Winner:");

        cboWinner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboWinnerActionPerformed(evt);
            }
        });

        lblLoser.setText("Select Loser:");

        cboLoser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoserActionPerformed(evt);
            }
        });

        lblWinnerPassword.setText("Winner Password:");

        lblLoserPassword.setText("Loser Password:");

        lblOrAdmin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOrAdmin.setText("OR");

        lblAdminMatchPassword.setText("Administrator Password:");

        btnLogMatchResult.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnLogMatchResult.setText("Log Result & Update Ratings");
        btnLogMatchResult.setEnabled(false);
        btnLogMatchResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogMatchResultActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlLogMatchLayout = new javax.swing.GroupLayout(pnlLogMatch);
        pnlLogMatch.setLayout(pnlLogMatchLayout);
        pnlLogMatchLayout.setHorizontalGroup(
            pnlLogMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLogMatchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLogMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLogMatchLayout.createSequentialGroup()
                        .addGroup(pnlLogMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblLogMatch)
                            .addGroup(pnlLogMatchLayout.createSequentialGroup()
                                .addGroup(pnlLogMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboWinner, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblWinner))
                                .addGap(18, 18, 18)
                                .addGroup(pnlLogMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblWinnerPassword)
                                    .addComponent(pwdWinnerPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlLogMatchLayout.createSequentialGroup()
                                .addGroup(pnlLogMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboLoser, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblLoser))
                                .addGap(18, 18, 18)
                                .addGroup(pnlLogMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblLoserPassword)
                                    .addComponent(pwdLoserPassword))))
                        .addGap(18, 18, 18)
                        .addComponent(lblOrAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(pnlLogMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pwdAdminMatchPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAdminMatchPassword)))
                    .addComponent(btnLogMatchResult))
                .addContainerGap(257, Short.MAX_VALUE))
        );
        pnlLogMatchLayout.setVerticalGroup(
            pnlLogMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLogMatchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLogMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlLogMatchLayout.createSequentialGroup()
                        .addComponent(lblAdminMatchPassword)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlLogMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblOrAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pwdAdminMatchPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlLogMatchLayout.createSequentialGroup()
                        .addComponent(lblLogMatch)
                        .addGap(18, 18, 18)
                        .addGroup(pnlLogMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblWinner)
                            .addComponent(lblWinnerPassword))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlLogMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboWinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pwdWinnerPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlLogMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblLoser)
                            .addComponent(lblLoserPassword))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlLogMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboLoser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pwdLoserPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(btnLogMatchResult)
                .addContainerGap(513, Short.MAX_VALUE))
        );

        tabTabs.addTab("Log Match", pnlLogMatch);

        tblTodayPlayers.setAutoCreateRowSorter(true);
        tblTodayPlayers.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        scpTodayPlayers.setViewportView(tblTodayPlayers);

        javax.swing.GroupLayout pnlTodayLayout = new javax.swing.GroupLayout(pnlToday);
        pnlToday.setLayout(pnlTodayLayout);
        pnlTodayLayout.setHorizontalGroup(
            pnlTodayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTodayLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scpTodayPlayers, javax.swing.GroupLayout.DEFAULT_SIZE, 941, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlTodayLayout.setVerticalGroup(
            pnlTodayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTodayLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scpTodayPlayers, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabTabs.addTab("Today", pnlToday);

        tblRosterPlayers.setAutoCreateRowSorter(true);
        tblRosterPlayers.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        scpRosterPlayers.setViewportView(tblRosterPlayers);

        javax.swing.GroupLayout pnlPlayersLayout = new javax.swing.GroupLayout(pnlPlayers);
        pnlPlayers.setLayout(pnlPlayersLayout);
        pnlPlayersLayout.setHorizontalGroup(
            pnlPlayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPlayersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scpRosterPlayers, javax.swing.GroupLayout.DEFAULT_SIZE, 941, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlPlayersLayout.setVerticalGroup(
            pnlPlayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPlayersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scpRosterPlayers, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabTabs.addTab("Roster", pnlPlayers);

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
                    .addComponent(lblAdminPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(pwdAdminPassword))
                .addContainerGap(751, Short.MAX_VALUE))
        );
        pnlAdminLayout.setVerticalGroup(
            pnlAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdminLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAdminPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pwdAdminPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(643, Short.MAX_VALUE))
        );

        tabTabs.addTab("Admin", pnlAdmin);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabTabs)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabTabs)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCheckInActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCheckInActionPerformed
    {//GEN-HEADEREND:event_btnCheckInActionPerformed
        Player player = pdLoginDirectory.getSelectedPlayer();
        if (player != null)
        {
            checkIn(player, new String(pwdPassword.getPassword()));
        }
    }//GEN-LAST:event_btnCheckInActionPerformed

    private void pwdAdminPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwdAdminPasswordActionPerformed
        String password = new String(pwdAdminPassword.getPassword());
        pwdAdminPassword.setText("");
        if (Global.AUTHENTICATION_PASSWORD.equals(password))
        {
            AdminDialog adminDlg = new AdminDialog(this);
            adminDlg.setVisible(true);
            refreshTabData();
        }
    }//GEN-LAST:event_pwdAdminPasswordActionPerformed

    private void lblRegisterNewMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegisterNewMousePressed
                
        EditPlayerDialog editDlg = new EditPlayerDialog(this);
        editDlg.newPlayer();        
        editDlg.setVisible(true);
        Player player = editDlg.getPlayer();
        if (player != null)
        {
            MpmsMain.Database.insertPlayer(player);
            refreshTabData();
            // Play sound:
            Global.playSound("welcome_new");
        }
        
    }//GEN-LAST:event_lblRegisterNewMousePressed

    private void pnlLogMatchComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_pnlLogMatchComponentShown
        resetLogMatch();
    }//GEN-LAST:event_pnlLogMatchComponentShown

    private void cboWinnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboWinnerActionPerformed
        checkPasswordsReady();
    }//GEN-LAST:event_cboWinnerActionPerformed

    private void cboLoserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoserActionPerformed
        checkPasswordsReady();
    }//GEN-LAST:event_cboLoserActionPerformed

    private void btnLogMatchResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogMatchResultActionPerformed
  
        if (checkMatchPasswords())
        {
            Player winner = (Player)cboWinner.getSelectedItem();
            Player loser = (Player)cboLoser.getSelectedItem();
            logMatch(winner, loser);
        }
    }//GEN-LAST:event_btnLogMatchResultActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MpmsMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MpmsMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MpmsMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MpmsMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MpmsMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCheckIn;
    private javax.swing.JButton btnLogMatchResult;
    private javax.swing.JComboBox<String> cboLoser;
    private javax.swing.JComboBox<String> cboWinner;
    private javax.swing.JLabel lblAdminMatchPassword;
    private javax.swing.JLabel lblAdminPassword;
    private javax.swing.JLabel lblLogMatch;
    private javax.swing.JLabel lblLoser;
    private javax.swing.JLabel lblLoserPassword;
    private javax.swing.JLabel lblMatchPointLogo;
    private javax.swing.JLabel lblOrAdmin;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblRegisterNew;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JLabel lblWinner;
    private javax.swing.JLabel lblWinnerPassword;
    private mpms.PlayerDirectory pdLoginDirectory;
    private javax.swing.JPanel pnlAdmin;
    private javax.swing.JPanel pnlCenterDirectory;
    private javax.swing.JPanel pnlCheckIn;
    private javax.swing.JPanel pnlLogMatch;
    private javax.swing.JPanel pnlLogin;
    private javax.swing.JPanel pnlLoginControls;
    private javax.swing.JPanel pnlPassword;
    private javax.swing.JPanel pnlPlayers;
    private javax.swing.JPanel pnlToday;
    private javax.swing.JPasswordField pwdAdminMatchPassword;
    private javax.swing.JPasswordField pwdAdminPassword;
    private javax.swing.JPasswordField pwdLoserPassword;
    private javax.swing.JPasswordField pwdPassword;
    private javax.swing.JPasswordField pwdWinnerPassword;
    private javax.swing.JScrollPane scpRosterPlayers;
    private javax.swing.JScrollPane scpTodayPlayers;
    private javax.swing.JTabbedPane tabTabs;
    private javax.swing.JTable tblRosterPlayers;
    private javax.swing.JTable tblTodayPlayers;
    // End of variables declaration//GEN-END:variables
}
