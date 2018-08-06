/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpms;

import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import mpms.data.Player;
import mpms.data.TTMatch;


/**
 *
 * @author Raymond
 */
public class GameScoresDialog extends javax.swing.JDialog {

    private Player matchWinner = null;
    private Player matchLoser = null;
    
    private int loserGames = 0;
    private int winnerGames = 0;
    
    /**
     * Creates new form GameScoresDialog
     */
    public GameScoresDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void passPlayers(Player winner, Player loser)
    {
        matchWinner = winner;
        matchLoser = loser;
        lblWinner.setText(winner.getLastFirst());
        lblLoser.setText(loser.getLastFirst());
    }
    
    private void adjustWinnerScore(JComboBox loserBox, JComboBox winnerBox)
    {
        if (loserBox.getSelectedItem().toString().equals("-"))
        {
            winnerBox.setSelectedIndex(0);
            winnerBox.setEnabled(true);
        }
        else
        {
            int loser = Integer.parseInt(loserBox.getSelectedItem().toString());
            int winner = Math.max(loser+2, 11);
            winnerBox.setEnabled(false);
            winnerBox.setSelectedIndex(winner+1);            
            btnOk.setEnabled(checkGameScores());
        }        
    }
    
    private boolean checkGameScores()
    {        
        loserGames = 0;
        winnerGames = 0;
        
        // Tally games won by match winner and loser:
        if (cboLoser1.getSelectedIndex() > cboWinner1.getSelectedIndex())
            loserGames++;
        if (cboWinner1.getSelectedIndex() > cboLoser1.getSelectedIndex())
            winnerGames++;
        enableGame(cboWinner1, cboLoser1, true && winnerGames < 4);
        
        if (cboLoser2.getSelectedIndex() > cboWinner2.getSelectedIndex())
            loserGames++;
        if (cboWinner2.getSelectedIndex() > cboLoser2.getSelectedIndex())
            winnerGames++;
        enableGame(cboWinner2, cboLoser2, cboLoser1.getSelectedIndex() != cboWinner1.getSelectedIndex() && winnerGames < 4);
        
        if (cboLoser3.getSelectedIndex() > cboWinner3.getSelectedIndex())
            loserGames++;
        if (cboWinner3.getSelectedIndex() > cboLoser3.getSelectedIndex())
            winnerGames++;
        enableGame(cboWinner3, cboLoser3, cboLoser2.getSelectedIndex() != cboWinner2.getSelectedIndex() && winnerGames < 4);

        if (cboLoser4.getSelectedIndex() > cboWinner4.getSelectedIndex())
            loserGames++;
        if (cboWinner4.getSelectedIndex() > cboLoser4.getSelectedIndex())
            winnerGames++;
        enableGame(cboWinner4, cboLoser4, cboLoser3.getSelectedIndex() != cboWinner3.getSelectedIndex() && winnerGames < 4);
        
        if (cboLoser5.getSelectedIndex() > cboWinner5.getSelectedIndex())
            loserGames++;
        if (cboWinner5.getSelectedIndex() > cboLoser5.getSelectedIndex())
            winnerGames++;
        enableGame(cboWinner5, cboLoser5, cboLoser4.getSelectedIndex() != cboWinner4.getSelectedIndex() && winnerGames < 4);
        
        if (cboLoser6.getSelectedIndex() > cboWinner6.getSelectedIndex())
            loserGames++;
        if (cboWinner6.getSelectedIndex() > cboLoser6.getSelectedIndex())
            winnerGames++;
        enableGame(cboWinner6, cboLoser6, cboLoser5.getSelectedIndex() != cboWinner5.getSelectedIndex() && winnerGames < 4);
        
        if (cboLoser7.getSelectedIndex() > cboWinner7.getSelectedIndex())
            loserGames++;
        if (cboWinner7.getSelectedIndex() > cboLoser7.getSelectedIndex())
            winnerGames++;
        enableGame(cboWinner7, cboLoser7, cboLoser6.getSelectedIndex() != cboWinner6.getSelectedIndex() && winnerGames < 4);
        
        lblWinnerGames.setText(winnerGames+"");
        lblLoserGames.setText(loserGames+"");
        
        if (winnerGames > loserGames)
        {
            lblWinnerGames.setForeground(Color.BLACK);
            lblLoserGames.setForeground(Color.BLACK);
            btnOk.setEnabled(true);
        }
        else if (loserGames > winnerGames)
        {
            lblWinnerGames.setForeground(Color.RED);
            lblLoserGames.setForeground(Color.RED);
            btnOk.setEnabled(false);
        }
        
        return winnerGames > loserGames;
    }
    
    private void enableGame(JComboBox winnerBox, JComboBox loserBox, boolean prevGameDone)
    {
        if (!prevGameDone)
        {
            winnerBox.setEnabled(false);
            loserBox.setEnabled(false);
            return;
        }
        
        if (winnerBox.getSelectedIndex() == loserBox.getSelectedIndex())
        {
            winnerBox.setEnabled(true);
            loserBox.setEnabled(true);
        }
        else if (winnerBox.getSelectedIndex() < loserBox.getSelectedIndex())
        {
            winnerBox.setEnabled(true);
            loserBox.setEnabled(false);
        }
        else if (winnerBox.getSelectedIndex() > loserBox.getSelectedIndex())
        {
            winnerBox.setEnabled(false);
            loserBox.setEnabled(true);
        }
    }

    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblInstructions = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblInstructions1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblGame1 = new javax.swing.JLabel();
        lblGame2 = new javax.swing.JLabel();
        lblGame3 = new javax.swing.JLabel();
        lblGame4 = new javax.swing.JLabel();
        lblGame5 = new javax.swing.JLabel();
        lblGame6 = new javax.swing.JLabel();
        lblGame7 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblWinner = new javax.swing.JLabel();
        cboWinner1 = new javax.swing.JComboBox<>();
        cboWinner2 = new javax.swing.JComboBox<>();
        cboWinner3 = new javax.swing.JComboBox<>();
        cboWinner4 = new javax.swing.JComboBox<>();
        cboWinner5 = new javax.swing.JComboBox<>();
        cboWinner6 = new javax.swing.JComboBox<>();
        cboWinner7 = new javax.swing.JComboBox<>();
        lblWinnerGames = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblLoser = new javax.swing.JLabel();
        cboLoser1 = new javax.swing.JComboBox<>();
        cboLoser2 = new javax.swing.JComboBox<>();
        cboLoser3 = new javax.swing.JComboBox<>();
        cboLoser4 = new javax.swing.JComboBox<>();
        cboLoser5 = new javax.swing.JComboBox<>();
        cboLoser6 = new javax.swing.JComboBox<>();
        cboLoser7 = new javax.swing.JComboBox<>();
        lblLoserGames = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblTitle.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTitle.setText("Enter Game Scores");

        lblInstructions.setText("One of the players does not yet have a rating. Please enter the game scores for this match to calculate his/her initial rating.");

        btnOk.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnOk.setText("Ok");
        btnOk.setEnabled(false);
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lblInstructions1.setText("All games are to 11. Enter only the losing score for each game; the correct winning score will be calculated.");

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setMaximumSize(new java.awt.Dimension(200, 14));
        jLabel1.setMinimumSize(new java.awt.Dimension(200, 14));
        jLabel1.setPreferredSize(new java.awt.Dimension(200, 14));
        jPanel1.add(jLabel1);

        lblGame1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGame1.setText("Game 1");
        lblGame1.setMaximumSize(new java.awt.Dimension(50, 20));
        lblGame1.setMinimumSize(new java.awt.Dimension(50, 20));
        lblGame1.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel1.add(lblGame1);

        lblGame2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGame2.setText("Game 2");
        lblGame2.setMaximumSize(new java.awt.Dimension(50, 20));
        lblGame2.setMinimumSize(new java.awt.Dimension(50, 20));
        lblGame2.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel1.add(lblGame2);

        lblGame3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGame3.setText("Game 3");
        lblGame3.setMaximumSize(new java.awt.Dimension(50, 20));
        lblGame3.setMinimumSize(new java.awt.Dimension(50, 20));
        lblGame3.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel1.add(lblGame3);

        lblGame4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGame4.setText("Game 4");
        lblGame4.setMaximumSize(new java.awt.Dimension(50, 20));
        lblGame4.setMinimumSize(new java.awt.Dimension(50, 20));
        lblGame4.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel1.add(lblGame4);

        lblGame5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGame5.setText("Game 5");
        lblGame5.setMaximumSize(new java.awt.Dimension(50, 20));
        lblGame5.setMinimumSize(new java.awt.Dimension(50, 20));
        lblGame5.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel1.add(lblGame5);

        lblGame6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGame6.setText("Game 6");
        lblGame6.setMaximumSize(new java.awt.Dimension(50, 20));
        lblGame6.setMinimumSize(new java.awt.Dimension(50, 20));
        lblGame6.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel1.add(lblGame6);

        lblGame7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGame7.setText("Game 7");
        lblGame7.setMaximumSize(new java.awt.Dimension(50, 20));
        lblGame7.setMinimumSize(new java.awt.Dimension(50, 20));
        lblGame7.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel1.add(lblGame7);

        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotal.setText("Total");
        lblTotal.setMaximumSize(new java.awt.Dimension(50, 20));
        lblTotal.setMinimumSize(new java.awt.Dimension(50, 20));
        lblTotal.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel1.add(lblTotal);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblWinner.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblWinner.setText("Winner");
        lblWinner.setMaximumSize(new java.awt.Dimension(200, 14));
        lblWinner.setMinimumSize(new java.awt.Dimension(200, 14));
        lblWinner.setPreferredSize(new java.awt.Dimension(200, 14));
        jPanel2.add(lblWinner);

        cboWinner1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cboWinner1.setMaximumSize(new java.awt.Dimension(50, 20));
        cboWinner1.setMinimumSize(new java.awt.Dimension(50, 20));
        cboWinner1.setPreferredSize(new java.awt.Dimension(50, 20));
        cboWinner1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboWinner1ActionPerformed(evt);
            }
        });
        jPanel2.add(cboWinner1);

        cboWinner2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cboWinner2.setEnabled(false);
        cboWinner2.setMaximumSize(new java.awt.Dimension(50, 20));
        cboWinner2.setMinimumSize(new java.awt.Dimension(50, 20));
        cboWinner2.setPreferredSize(new java.awt.Dimension(50, 20));
        cboWinner2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboWinner2ActionPerformed(evt);
            }
        });
        jPanel2.add(cboWinner2);

        cboWinner3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cboWinner3.setEnabled(false);
        cboWinner3.setMaximumSize(new java.awt.Dimension(50, 20));
        cboWinner3.setMinimumSize(new java.awt.Dimension(50, 20));
        cboWinner3.setPreferredSize(new java.awt.Dimension(50, 20));
        cboWinner3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboWinner3ActionPerformed(evt);
            }
        });
        jPanel2.add(cboWinner3);

        cboWinner4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cboWinner4.setEnabled(false);
        cboWinner4.setMaximumSize(new java.awt.Dimension(50, 20));
        cboWinner4.setMinimumSize(new java.awt.Dimension(50, 20));
        cboWinner4.setPreferredSize(new java.awt.Dimension(50, 20));
        cboWinner4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboWinner4ActionPerformed(evt);
            }
        });
        jPanel2.add(cboWinner4);

        cboWinner5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cboWinner5.setEnabled(false);
        cboWinner5.setMaximumSize(new java.awt.Dimension(50, 20));
        cboWinner5.setMinimumSize(new java.awt.Dimension(50, 20));
        cboWinner5.setPreferredSize(new java.awt.Dimension(50, 20));
        cboWinner5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboWinner5ActionPerformed(evt);
            }
        });
        jPanel2.add(cboWinner5);

        cboWinner6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cboWinner6.setEnabled(false);
        cboWinner6.setMaximumSize(new java.awt.Dimension(50, 20));
        cboWinner6.setMinimumSize(new java.awt.Dimension(50, 20));
        cboWinner6.setPreferredSize(new java.awt.Dimension(50, 20));
        cboWinner6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboWinner6ActionPerformed(evt);
            }
        });
        jPanel2.add(cboWinner6);

        cboWinner7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cboWinner7.setEnabled(false);
        cboWinner7.setMaximumSize(new java.awt.Dimension(50, 20));
        cboWinner7.setMinimumSize(new java.awt.Dimension(50, 20));
        cboWinner7.setPreferredSize(new java.awt.Dimension(50, 20));
        cboWinner7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboWinner7ActionPerformed(evt);
            }
        });
        jPanel2.add(cboWinner7);

        lblWinnerGames.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblWinnerGames.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWinnerGames.setText("0");
        lblWinnerGames.setMaximumSize(new java.awt.Dimension(50, 20));
        lblWinnerGames.setMinimumSize(new java.awt.Dimension(50, 20));
        lblWinnerGames.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel2.add(lblWinnerGames);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblLoser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoser.setText("Loser");
        lblLoser.setMaximumSize(new java.awt.Dimension(200, 14));
        lblLoser.setMinimumSize(new java.awt.Dimension(200, 14));
        lblLoser.setPreferredSize(new java.awt.Dimension(200, 14));
        jPanel3.add(lblLoser);

        cboLoser1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cboLoser1.setMaximumSize(new java.awt.Dimension(50, 20));
        cboLoser1.setMinimumSize(new java.awt.Dimension(50, 20));
        cboLoser1.setPreferredSize(new java.awt.Dimension(50, 20));
        cboLoser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoser1ActionPerformed(evt);
            }
        });
        jPanel3.add(cboLoser1);

        cboLoser2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cboLoser2.setEnabled(false);
        cboLoser2.setMaximumSize(new java.awt.Dimension(50, 20));
        cboLoser2.setMinimumSize(new java.awt.Dimension(50, 20));
        cboLoser2.setPreferredSize(new java.awt.Dimension(50, 20));
        cboLoser2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoser2ActionPerformed(evt);
            }
        });
        jPanel3.add(cboLoser2);

        cboLoser3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cboLoser3.setEnabled(false);
        cboLoser3.setMaximumSize(new java.awt.Dimension(50, 20));
        cboLoser3.setMinimumSize(new java.awt.Dimension(50, 20));
        cboLoser3.setPreferredSize(new java.awt.Dimension(50, 20));
        cboLoser3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoser3ActionPerformed(evt);
            }
        });
        jPanel3.add(cboLoser3);

        cboLoser4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cboLoser4.setEnabled(false);
        cboLoser4.setMaximumSize(new java.awt.Dimension(50, 20));
        cboLoser4.setMinimumSize(new java.awt.Dimension(50, 20));
        cboLoser4.setPreferredSize(new java.awt.Dimension(50, 20));
        cboLoser4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoser4ActionPerformed(evt);
            }
        });
        jPanel3.add(cboLoser4);

        cboLoser5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cboLoser5.setEnabled(false);
        cboLoser5.setMaximumSize(new java.awt.Dimension(50, 20));
        cboLoser5.setMinimumSize(new java.awt.Dimension(50, 20));
        cboLoser5.setPreferredSize(new java.awt.Dimension(50, 20));
        cboLoser5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoser5ActionPerformed(evt);
            }
        });
        jPanel3.add(cboLoser5);

        cboLoser6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cboLoser6.setEnabled(false);
        cboLoser6.setMaximumSize(new java.awt.Dimension(50, 20));
        cboLoser6.setMinimumSize(new java.awt.Dimension(50, 20));
        cboLoser6.setPreferredSize(new java.awt.Dimension(50, 20));
        cboLoser6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoser6ActionPerformed(evt);
            }
        });
        jPanel3.add(cboLoser6);

        cboLoser7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cboLoser7.setEnabled(false);
        cboLoser7.setMaximumSize(new java.awt.Dimension(50, 20));
        cboLoser7.setMinimumSize(new java.awt.Dimension(50, 20));
        cboLoser7.setPreferredSize(new java.awt.Dimension(50, 20));
        cboLoser7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoser7ActionPerformed(evt);
            }
        });
        jPanel3.add(cboLoser7);

        lblLoserGames.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblLoserGames.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLoserGames.setText("0");
        lblLoserGames.setMaximumSize(new java.awt.Dimension(50, 20));
        lblLoserGames.setMinimumSize(new java.awt.Dimension(50, 20));
        lblLoserGames.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel3.add(lblLoserGames);

        btnReset.setText("Reset Form");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnReset)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblTitle)
                            .addComponent(lblInstructions)
                            .addComponent(lblInstructions1)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInstructions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInstructions1)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnOk)
                    .addComponent(btnReset))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboLoser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoser1ActionPerformed
        if (cboLoser1.isEnabled())
            adjustWinnerScore(cboLoser1, cboWinner1);
    }//GEN-LAST:event_cboLoser1ActionPerformed

    private void cboLoser2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoser2ActionPerformed
        if (cboLoser2.isEnabled())
            adjustWinnerScore(cboLoser2, cboWinner2);
    }//GEN-LAST:event_cboLoser2ActionPerformed

    private void cboLoser3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoser3ActionPerformed
        if (cboLoser3.isEnabled())
            adjustWinnerScore(cboLoser3, cboWinner3);
    }//GEN-LAST:event_cboLoser3ActionPerformed

    private void cboLoser4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoser4ActionPerformed
        if (cboLoser4.isEnabled())
            adjustWinnerScore(cboLoser4, cboWinner4);
    }//GEN-LAST:event_cboLoser4ActionPerformed

    private void cboLoser5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoser5ActionPerformed
        if (cboLoser5.isEnabled())
            adjustWinnerScore(cboLoser5, cboWinner5);
    }//GEN-LAST:event_cboLoser5ActionPerformed

    private void cboLoser6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoser6ActionPerformed
        if (cboLoser6.isEnabled())
            adjustWinnerScore(cboLoser6, cboWinner6);
    }//GEN-LAST:event_cboLoser6ActionPerformed

    private void cboLoser7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoser7ActionPerformed
        if (cboLoser7.isEnabled())
            adjustWinnerScore(cboLoser7, cboWinner7);
    }//GEN-LAST:event_cboLoser7ActionPerformed

    private void cboWinner1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboWinner1ActionPerformed
        if (cboWinner1.isEnabled())
            adjustWinnerScore(cboWinner1, cboLoser1);
    }//GEN-LAST:event_cboWinner1ActionPerformed

    private void cboWinner2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboWinner2ActionPerformed
        if (cboWinner2.isEnabled())
            adjustWinnerScore(cboWinner2, cboLoser2);
    }//GEN-LAST:event_cboWinner2ActionPerformed

    private void cboWinner3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboWinner3ActionPerformed
        if (cboWinner3.isEnabled())
            adjustWinnerScore(cboWinner3, cboLoser3);
    }//GEN-LAST:event_cboWinner3ActionPerformed

    private void cboWinner4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboWinner4ActionPerformed
        if (cboWinner4.isEnabled())
            adjustWinnerScore(cboWinner4, cboLoser4);
    }//GEN-LAST:event_cboWinner4ActionPerformed

    private void cboWinner5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboWinner5ActionPerformed
        if (cboWinner5.isEnabled())
            adjustWinnerScore(cboWinner5, cboLoser5);
    }//GEN-LAST:event_cboWinner5ActionPerformed

    private void cboWinner6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboWinner6ActionPerformed
        if (cboWinner6.isEnabled())
            adjustWinnerScore(cboWinner6, cboLoser6);
    }//GEN-LAST:event_cboWinner6ActionPerformed

    private void cboWinner7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboWinner7ActionPerformed
        if (cboWinner7.isEnabled())
            adjustWinnerScore(cboWinner7, cboLoser7);
    }//GEN-LAST:event_cboWinner7ActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        cboLoser1.setSelectedIndex(0);
        cboWinner1.setSelectedIndex(0);
        cboLoser2.setSelectedIndex(0);
        cboWinner2.setSelectedIndex(0);
        cboLoser3.setSelectedIndex(0);
        cboWinner3.setSelectedIndex(0);
        cboLoser4.setSelectedIndex(0);
        cboWinner4.setSelectedIndex(0);
        cboLoser5.setSelectedIndex(0);
        cboWinner5.setSelectedIndex(0);
        cboLoser6.setSelectedIndex(0);
        cboWinner6.setSelectedIndex(0);
        cboLoser7.setSelectedIndex(0);
        cboWinner7.setSelectedIndex(0);
        checkGameScores();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        double winnerTotal = 0;
        double loserTotal = 0;
        
        if (cboWinner1.getSelectedIndex() > 0)
            winnerTotal += Integer.parseInt(cboWinner1.getSelectedItem().toString());
        if (cboWinner2.getSelectedIndex() > 0)
            winnerTotal += Integer.parseInt(cboWinner2.getSelectedItem().toString());
        if (cboWinner3.getSelectedIndex() > 0)
            winnerTotal += Integer.parseInt(cboWinner3.getSelectedItem().toString());
        if (cboWinner4.getSelectedIndex() > 0)
            winnerTotal += Integer.parseInt(cboWinner4.getSelectedItem().toString());
        if (cboWinner5.getSelectedIndex() > 0)
            winnerTotal += Integer.parseInt(cboWinner5.getSelectedItem().toString());
        if (cboWinner6.getSelectedIndex() > 0)
            winnerTotal += Integer.parseInt(cboWinner6.getSelectedItem().toString());
        if (cboWinner7.getSelectedIndex() > 0)
            winnerTotal += Integer.parseInt(cboWinner7.getSelectedItem().toString());
        
        if (cboLoser1.getSelectedIndex() > 0)
            loserTotal += Integer.parseInt(cboLoser1.getSelectedItem().toString());
        if (cboLoser2.getSelectedIndex() > 0)
            loserTotal += Integer.parseInt(cboLoser2.getSelectedItem().toString());
        if (cboLoser3.getSelectedIndex() > 0)
            loserTotal += Integer.parseInt(cboLoser3.getSelectedItem().toString());
        if (cboLoser4.getSelectedIndex() > 0)
            loserTotal += Integer.parseInt(cboLoser4.getSelectedItem().toString());
        if (cboLoser5.getSelectedIndex() > 0)
            loserTotal += Integer.parseInt(cboLoser5.getSelectedItem().toString());
        if (cboLoser6.getSelectedIndex() > 0)
            loserTotal += Integer.parseInt(cboLoser6.getSelectedItem().toString());
        if (cboLoser7.getSelectedIndex() > 0)
            loserTotal += Integer.parseInt(cboLoser7.getSelectedItem().toString());
        
        int winnerRating = matchWinner.getClubRating() == null ? 0 : matchWinner.getClubRating();
        int loserRating = matchLoser.getClubRating() == null ? 0 : matchLoser.getClubRating();
        int totalGames = winnerGames + loserGames;
        double lPPG = loserTotal / totalGames;
        double wPPG = winnerTotal / totalGames;
        double avgPointDiff = wPPG - lPPG;
        avgPointDiff -= 1.0; // Subtract one point, because winning by 2 is as close as two players can get. Someone ALWAYS wins by at least 2!
        
        if (avgPointDiff < 0) // If the average point difference ended up being too low, give it a little boost.
            avgPointDiff = .1;
        
        double ratingDiff = 100.0 * avgPointDiff;
        
        if (winnerRating > 0 && loserRating == 0)
        {            
            loserRating = (int)(winnerRating - ratingDiff);
            matchLoser.setRatingAcc(.3);
            matchLoser.setClubRating(loserRating);
            matchLoser.updateToDatabase();
            MpmsMain.Database.insertTTMatch(TTMatch.makeMatch(matchWinner, matchLoser, 0));
            JOptionPane.showMessageDialog(this, "Based on performance, " + matchLoser.getName() + " was assigned an initial club rating of " + loserRating + ".\n" + matchWinner.getName() + "'s rating was not changed.", "Initial Rating", JOptionPane.INFORMATION_MESSAGE);
        }
        
        if (loserRating > 0 && winnerRating == 0)
        {            
            winnerRating = (int)(loserRating + ratingDiff);
            matchWinner.setRatingAcc(.3);
            matchWinner.setClubRating(winnerRating);
            matchWinner.updateToDatabase();
            MpmsMain.Database.insertTTMatch(TTMatch.makeMatch(matchWinner, matchLoser, 0));
            JOptionPane.showMessageDialog(this, "Based on performance, " + matchWinner.getName() + " was assigned an initial club rating of " + winnerRating + ".\n" + matchLoser.getName() + "'s rating was not changed.", "Initial Rating", JOptionPane.INFORMATION_MESSAGE);
        }
        
        setVisible(false);
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed

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
            java.util.logging.Logger.getLogger(GameScoresDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameScoresDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameScoresDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameScoresDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GameScoresDialog dialog = new GameScoresDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cboLoser1;
    private javax.swing.JComboBox<String> cboLoser2;
    private javax.swing.JComboBox<String> cboLoser3;
    private javax.swing.JComboBox<String> cboLoser4;
    private javax.swing.JComboBox<String> cboLoser5;
    private javax.swing.JComboBox<String> cboLoser6;
    private javax.swing.JComboBox<String> cboLoser7;
    private javax.swing.JComboBox<String> cboWinner1;
    private javax.swing.JComboBox<String> cboWinner2;
    private javax.swing.JComboBox<String> cboWinner3;
    private javax.swing.JComboBox<String> cboWinner4;
    private javax.swing.JComboBox<String> cboWinner5;
    private javax.swing.JComboBox<String> cboWinner6;
    private javax.swing.JComboBox<String> cboWinner7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblGame1;
    private javax.swing.JLabel lblGame2;
    private javax.swing.JLabel lblGame3;
    private javax.swing.JLabel lblGame4;
    private javax.swing.JLabel lblGame5;
    private javax.swing.JLabel lblGame6;
    private javax.swing.JLabel lblGame7;
    private javax.swing.JLabel lblInstructions;
    private javax.swing.JLabel lblInstructions1;
    private javax.swing.JLabel lblLoser;
    private javax.swing.JLabel lblLoserGames;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblWinner;
    private javax.swing.JLabel lblWinnerGames;
    // End of variables declaration//GEN-END:variables
}
