/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpms;

import mpms.data.Player;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

/**
 *
 * @author Ray
 */
public final class PlayerDirectory extends javax.swing.JPanel{
    
    private final String alphabet = "*ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private Player selectedPlayer = null;
    private final ArrayList<ActionListener> actionListeners = new ArrayList<>();
    
    /**
     * Creates new form PlayerDirectory
     */
    public PlayerDirectory() {
        initComponents();
        
        ActionListener listener = (ActionEvent e) -> {
            JButton btn = (JButton)e.getSource();
            char letter = btn.getText().charAt(0);
            loadPlayersByLetter(letter);
        };
        
        for (int a = 0; a < 27; a++)
        {
            getBtnByLetter(alphabet.charAt(a)).addActionListener(listener);
        }
        
        cboPlayers.addActionListener((ActionEvent e) ->
        {
            doAction(e);
        });        
    }
    
    public void addActionListener(ActionListener listener)
    {
        actionListeners.add(listener);
    }    
    
    private void doAction(ActionEvent e)
    {
        this.selectedPlayer = (Player)cboPlayers.getSelectedItem();
        for (int a = 0; a < actionListeners.size(); a++)
        {
            actionListeners.get(a).actionPerformed(e);
        }        
    }
    
    public void loadPlayersByLetter(char letter)
    {
        if (letter == '*')
            lblFilter.setText("All players by last name:");
        else
            lblFilter.setText("Players by last name starting with '" + (letter+"".toUpperCase()) + "':");
        
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        //model.addElement(null); // Prevents the first name in the list from being automatically selected.
        List<Player> players = Player.allFromDatabase(Player.PLAYER_LIST_MODE.ByLastName);
        if (players != null && !players.isEmpty())
        {
            for (int p = 0; p < players.size(); p++)
            {
                if (letter == '*' || players.get(p).getLastName().toUpperCase().startsWith(letter+"".toUpperCase()))
                    model.addElement(players.get(p));
            }
        }
        cboPlayers.setModel(model);
        doAction(new ActionEvent(cboPlayers, ActionEvent.ACTION_FIRST, "load players"));
    }

    public Player getSelectedPlayer()
    {
        return this.selectedPlayer;
    }
    
    public final JButton getBtnByLetter(char letter)
    {
        letter = Character.toUpperCase(letter);
        if (letter == '*') return btnAll;
        if (letter == 'A') return btnA;
        if (letter == 'B') return btnB;
        if (letter == 'C') return btnC;
        if (letter == 'D') return btnD;
        if (letter == 'E') return btnE;
        if (letter == 'F') return btnF;
        if (letter == 'G') return btnG;
        if (letter == 'H') return btnH;
        if (letter == 'I') return btnI;
        if (letter == 'J') return btnJ;
        if (letter == 'K') return btnK;
        if (letter == 'L') return btnL;
        if (letter == 'M') return btnM;
        if (letter == 'N') return btnN;
        if (letter == 'O') return btnO;
        if (letter == 'P') return btnP;
        if (letter == 'Q') return btnQ;
        if (letter == 'R') return btnR;
        if (letter == 'S') return btnS;
        if (letter == 'T') return btnT;
        if (letter == 'U') return btnU;
        if (letter == 'V') return btnV;
        if (letter == 'W') return btnW;
        if (letter == 'X') return btnX;
        if (letter == 'Y') return btnY;
        if (letter == 'Z') return btnZ;
        return null;
    }
    
    public void setSelectedPlayer(Integer id)
    {
        loadPlayersByLetter('*');
        for (int p = 0; p < cboPlayers.getItemCount(); p++)
        {
            if (((Player)cboPlayers.getItemAt(p)).getId() == id)
            {
                cboPlayers.setSelectedIndex(p);
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        btnA = new javax.swing.JButton();
        btnB = new javax.swing.JButton();
        btnC = new javax.swing.JButton();
        btnD = new javax.swing.JButton();
        btnE = new javax.swing.JButton();
        btnF = new javax.swing.JButton();
        btnG = new javax.swing.JButton();
        btnH = new javax.swing.JButton();
        btnI = new javax.swing.JButton();
        btnJ = new javax.swing.JButton();
        btnK = new javax.swing.JButton();
        btnL = new javax.swing.JButton();
        btnM = new javax.swing.JButton();
        btnN = new javax.swing.JButton();
        btnO = new javax.swing.JButton();
        btnP = new javax.swing.JButton();
        btnQ = new javax.swing.JButton();
        btnR = new javax.swing.JButton();
        btnS = new javax.swing.JButton();
        btnT = new javax.swing.JButton();
        btnU = new javax.swing.JButton();
        btnV = new javax.swing.JButton();
        btnW = new javax.swing.JButton();
        btnX = new javax.swing.JButton();
        btnY = new javax.swing.JButton();
        btnZ = new javax.swing.JButton();
        btnAll = new javax.swing.JButton();
        lblFilter = new javax.swing.JLabel();
        cboPlayers = new javax.swing.JComboBox();

        jPanel1.setLayout(new java.awt.GridLayout(2, 13));

        btnA.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnA.setText("A");
        jPanel1.add(btnA);

        btnB.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnB.setText("B");
        jPanel1.add(btnB);

        btnC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnC.setText("C");
        jPanel1.add(btnC);

        btnD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnD.setText("D");
        jPanel1.add(btnD);

        btnE.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnE.setText("E");
        jPanel1.add(btnE);

        btnF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnF.setText("F");
        jPanel1.add(btnF);

        btnG.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnG.setText("G");
        jPanel1.add(btnG);

        btnH.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnH.setText("H");
        jPanel1.add(btnH);

        btnI.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnI.setText("I");
        jPanel1.add(btnI);

        btnJ.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnJ.setText("J");
        jPanel1.add(btnJ);

        btnK.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnK.setText("K");
        jPanel1.add(btnK);

        btnL.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnL.setText("L");
        jPanel1.add(btnL);

        btnM.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnM.setText("M");
        jPanel1.add(btnM);

        btnN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnN.setText("N");
        jPanel1.add(btnN);

        btnO.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnO.setText("O");
        jPanel1.add(btnO);

        btnP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnP.setText("P");
        jPanel1.add(btnP);

        btnQ.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnQ.setText("Q");
        jPanel1.add(btnQ);

        btnR.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnR.setText("R");
        jPanel1.add(btnR);

        btnS.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnS.setText("S");
        jPanel1.add(btnS);

        btnT.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnT.setText("T");
        jPanel1.add(btnT);

        btnU.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnU.setText("U");
        jPanel1.add(btnU);

        btnV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnV.setText("V");
        jPanel1.add(btnV);

        btnW.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnW.setText("W");
        jPanel1.add(btnW);

        btnX.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnX.setText("X");
        jPanel1.add(btnX);

        btnY.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnY.setText("Y");
        jPanel1.add(btnY);

        btnZ.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnZ.setText("Z");
        jPanel1.add(btnZ);

        btnAll.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnAll.setText("*");

        lblFilter.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblFilter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFilter.setText("All players by last name:");

        cboPlayers.setMaximumRowCount(20);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnAll)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(cboPlayers, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(btnAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFilter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnA;
    private javax.swing.JButton btnAll;
    private javax.swing.JButton btnB;
    private javax.swing.JButton btnC;
    private javax.swing.JButton btnD;
    private javax.swing.JButton btnE;
    private javax.swing.JButton btnF;
    private javax.swing.JButton btnG;
    private javax.swing.JButton btnH;
    private javax.swing.JButton btnI;
    private javax.swing.JButton btnJ;
    private javax.swing.JButton btnK;
    private javax.swing.JButton btnL;
    private javax.swing.JButton btnM;
    private javax.swing.JButton btnN;
    private javax.swing.JButton btnO;
    private javax.swing.JButton btnP;
    private javax.swing.JButton btnQ;
    private javax.swing.JButton btnR;
    private javax.swing.JButton btnS;
    private javax.swing.JButton btnT;
    private javax.swing.JButton btnU;
    private javax.swing.JButton btnV;
    private javax.swing.JButton btnW;
    private javax.swing.JButton btnX;
    private javax.swing.JButton btnY;
    private javax.swing.JButton btnZ;
    private javax.swing.JComboBox cboPlayers;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblFilter;
    // End of variables declaration//GEN-END:variables
}