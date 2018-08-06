/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mpms;

import java.awt.Dialog;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import mpms.data.Player;
import mpms.data.Player.SEX;

/**
 *
 * @author rnagel
 */
public class EditPlayerDialog extends javax.swing.JDialog {
    private Player player = null;
    private Player existingPlayer = null;
    
    /**
     * Creates new form EditPlayerDialog
     * @param parent
     */
    public EditPlayerDialog(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        setLocationRelativeTo(parent);
        init();
    }
    public EditPlayerDialog(Dialog parent) {
        super(parent, true);
        initComponents();
        setLocationRelativeTo(parent);
        init();
    }
    
    private void init()
    {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int endYear = thisYear - 4;
        int startYear = endYear - 100;
        for (int y = startYear; y <= endYear; y++)
        {
            model.addElement(y);
        }
        cboYear.setModel(model);
        cboYear.setSelectedItem(endYear-18);
        txpPassword.setText("");        
    }

    public void newPlayer()
    {
        txtClubRating.setText(null);
        txtId.setText(null);               
        txtBalance.setText(Global.DOLLARS.format(0));
        txtSponsorId.setText(null);
        txtVisits.setText("0");
    }
    
    public void initialize(Player player)
    {
        existingPlayer = player;
        
        // Personal:
        txtFirstName.setText(player.getFirstName());
        txtLastName.setText(player.getLastName());
        cboSex.setSelectedIndex(player.getSex().ordinal());
        txpPassword.setText(player.getPassword());
        txpVerify.setText(player.getPassword());
        
        // Date of Birth:
        Date dob = player.getBirthDate();
        Calendar dobCalendar = new GregorianCalendar();
        dobCalendar.setTime(dob);
        cboYear.setSelectedItem(dobCalendar.get(Calendar.YEAR));
        cboMonth.setSelectedIndex(dobCalendar.get(Calendar.MONTH));
        cboDate.setSelectedItem(dobCalendar.get(Calendar.DAY_OF_MONTH));
        
        // Contact:
        txtAddress.setText(player.getAddress());
        txtCity.setText(player.getCity());
        cboState.setSelectedItem(player.getState());
        txtZip.setText(player.getZip());
        txtPhone.setText(player.getPhone());
        txtEmail.setText(player.getEmail());
                
        // Account:
        txtBalance.setText(Global.DOLLARS.format(player.getBalance()));
        if (player.getSponsorId() == null)
            txtSponsorId.setText(null);
        else
            txtSponsorId.setText(player.getSponsorId()+"");
        
        txtVisits.setText(player.getVisits()+"");        
        
        // Additional Info:
        if (player.getClubRating() == null)
            txtClubRating.setText(null);
        else
            txtClubRating.setText(player.getClubRating()+"");
        
        txtId.setText(player.getId()+"");
        txtPosition.setText(player.getPosition());
    }
    
    private void refreshAvailableBirthdates()
    {
        int year = (Integer)cboYear.getSelectedItem();
        int month = Integer.parseInt(((String)cboMonth.getSelectedItem()).split("-")[0].trim());
        Calendar defaultCalendar = new GregorianCalendar(year, month-1, 1);
        int days = defaultCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        DefaultComboBoxModel model = new DefaultComboBoxModel();       
        for (int d = 1; d <= days; d++)
        {
            model.addElement(d);
        }
        cboDate.setModel(model);
        cboDate.setSelectedIndex(0);
    }
    
    public Player getPlayer()
    {
        return this.player;
    }
    
    private boolean checkPassword()
    {
        if (!(new String(txpPassword.getPassword()).equals(new String(txpVerify.getPassword()))))
        {
            JOptionPane.showMessageDialog(this, "The passwords you entered do not match. Please enter the same password twice.", "Password Mismatch", JOptionPane.ERROR_MESSAGE);
            txpVerify.setText("");
            txpPassword.selectAll();
            txpPassword.requestFocus();
            return false;
        }
        else            
            return true;
    }
    
    private String validateProfile()
    {
        String exReason = null;
        try
        {                                    
            exReason = "first name";
            if (isMissing(txtFirstName)) return exReason + " missing";
            player.setFirstName(StringHelper.toTitleCase(txtFirstName.getText()));
            
            exReason = "last name";
            if (isMissing(txtLastName)) return exReason + " missing";
            player.setLastName(StringHelper.toTitleCase(txtLastName.getText()));
            
            exReason = "sex";
            player.setSex(SEX.valueOf((String)cboSex.getSelectedItem()));
            
            exReason = "year";
            String year = cboYear.getSelectedItem().toString();
            
            exReason = "month";
            String month = ((String)cboMonth.getSelectedItem()).split("-")[0].trim();
            
            exReason = "date";
            String date = cboDate.getSelectedItem().toString();
            
            exReason = "dob";
            player.setBirthDate(month, date, year);
            
            exReason = "password";            
            player.setPassword(new String(txpPassword.getPassword()));
            
            exReason = "address";
            player.setAddress(txtAddress.getText());
            
            exReason = "city";
            player.setCity(txtCity.getText());
            
            exReason = "state";
            player.setState((String)cboState.getSelectedItem());
            
            exReason = "zip";
            player.setZip(txtZip.getText());
            
            exReason = "phone";
            if (isMissing(txtPhone)) return exReason + " missing";
            player.setPhone(txtPhone.getText().replace("(", "").replace(")", "").replace("-", ""));
            if (player.getPhone().length() > 10)
                return exReason;
            
            exReason = "email";
            player.setEmail(txtEmail.getText());
            
            exReason = "balance";
            player.setBalance(Double.parseDouble(txtBalance.getText().replace("$", "")));
            
            exReason = "sponsor";
            player.setSponsorId(Global.parseNullableInt(txtSponsorId.getText()));
            
            exReason = "special";
            player.setPosition(txtPosition.getText());
            
            exReason = "rating";
            player.setClubRating(Global.parseNullableInt(txtClubRating.getText()));
            return null;
        }
        catch (Exception ex)
        {
            return exReason;
        }        
    }
    
    private boolean isMissing(JTextField field)
    {
        return field.getText() == null || field.getText().isEmpty();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlPlayer = new javax.swing.JPanel();
        lblFirstName = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        lblLastName = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        lblSex = new javax.swing.JLabel();
        cboSex = new javax.swing.JComboBox();
        lblDob = new javax.swing.JLabel();
        cboMonth = new javax.swing.JComboBox();
        cboDate = new javax.swing.JComboBox();
        cboYear = new javax.swing.JComboBox();
        lblPassword = new javax.swing.JLabel();
        txpPassword = new javax.swing.JPasswordField();
        lblVerify = new javax.swing.JLabel();
        txpVerify = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pnlContact = new javax.swing.JPanel();
        lblAddress = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        lblCity = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        cboState = new javax.swing.JComboBox();
        lblState = new javax.swing.JLabel();
        lblZip = new javax.swing.JLabel();
        txtZip = new javax.swing.JTextField();
        lblPhone = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        pnlInfo = new javax.swing.JPanel();
        lblClubRating = new javax.swing.JLabel();
        txtClubRating = new javax.swing.JTextField();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblSpecial = new javax.swing.JLabel();
        txtPosition = new javax.swing.JTextField();
        pnlMembership = new javax.swing.JPanel();
        lblBalance = new javax.swing.JLabel();
        txtBalance = new javax.swing.JTextField();
        lblSponsor = new javax.swing.JLabel();
        txtSponsorId = new javax.swing.JTextField();
        lblVisits = new javax.swing.JLabel();
        txtVisits = new javax.swing.JTextField();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Player");

        pnlPlayer.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Player", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        lblFirstName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblFirstName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFirstName.setText("First Name:");

        txtFirstName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        lblLastName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblLastName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLastName.setText("Last Name:");

        txtLastName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        lblSex.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblSex.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSex.setText("Sex:");

        cboSex.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cboSex.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Male", "Female" }));

        lblDob.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblDob.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDob.setText("Birth Date:");

        cboMonth.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cboMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1 - January", "2 - February", "3 - March", "4 - April", "5 - May", "6 - June", "7 - July", "8 - August", "9 - September", "10 - October", "11 - November", "12 - December" }));
        cboMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMonthActionPerformed(evt);
            }
        });

        cboDate.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        cboYear.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cboYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboYearActionPerformed(evt);
            }
        });

        lblPassword.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblPassword.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPassword.setText("Password:");

        txpPassword.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        lblVerify.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblVerify.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblVerify.setText("Verify Pwd:");

        txpVerify.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txpVerify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txpVerifyActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("*");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("*");

        javax.swing.GroupLayout pnlPlayerLayout = new javax.swing.GroupLayout(pnlPlayer);
        pnlPlayer.setLayout(pnlPlayerLayout);
        pnlPlayerLayout.setHorizontalGroup(
            pnlPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPlayerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVerify, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblDob, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblFirstName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblLastName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSex, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlPlayerLayout.createSequentialGroup()
                        .addGroup(pnlPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cboSex, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboYear, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cboMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtLastName, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFirstName, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txpPassword)
                    .addComponent(txpVerify))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboDate, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap())
        );
        pnlPlayerLayout.setVerticalGroup(
            pnlPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPlayerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFirstName)
                    .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLastName)
                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSex)
                    .addComponent(cboSex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDob)
                    .addComponent(cboMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword)
                    .addComponent(txpPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVerify)
                    .addComponent(txpVerify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlContact.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Contact", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        lblAddress.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblAddress.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAddress.setText("Address:");

        txtAddress.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        lblCity.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblCity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCity.setText("City:");

        txtCity.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        cboState.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cboState.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY" }));
        cboState.setSelectedIndex(31);

        lblState.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblState.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblState.setText("State:");

        lblZip.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblZip.setText("ZIP:");

        txtZip.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        lblPhone.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblPhone.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPhone.setText("Phone:");

        txtPhone.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        lblEmail.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblEmail.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEmail.setText("Email:");

        txtEmail.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("*");

        javax.swing.GroupLayout pnlContactLayout = new javax.swing.GroupLayout(pnlContact);
        pnlContact.setLayout(pnlContactLayout);
        pnlContactLayout.setHorizontalGroup(
            pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContactLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblPhone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAddress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                    .addComponent(lblCity, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblState, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAddress, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtEmail)
                    .addGroup(pnlContactLayout.createSequentialGroup()
                        .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlContactLayout.createSequentialGroup()
                                .addComponent(cboState, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblZip)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtZip, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                            .addComponent(txtCity)
                            .addComponent(txtPhone))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlContactLayout.setVerticalGroup(
            pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContactLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAddress)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCity)
                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblState)
                    .addComponent(lblZip)
                    .addComponent(txtZip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPhone)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContactLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmail)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Additional Info:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        lblClubRating.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblClubRating.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblClubRating.setText("Club Rating:");

        txtClubRating.setEditable(false);
        txtClubRating.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        lblId.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblId.setText("Player ID:");

        txtId.setEditable(false);
        txtId.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        lblSpecial.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblSpecial.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSpecial.setText("Position:");

        txtPosition.setEditable(false);
        txtPosition.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        javax.swing.GroupLayout pnlInfoLayout = new javax.swing.GroupLayout(pnlInfo);
        pnlInfo.setLayout(pnlInfoLayout);
        pnlInfoLayout.setHorizontalGroup(
            pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblSpecial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPosition)
                            .addComponent(txtId)))
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addComponent(lblClubRating, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtClubRating)))
                .addGap(42, 42, 42))
        );
        pnlInfoLayout.setVerticalGroup(
            pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblClubRating)
                    .addComponent(txtClubRating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblId)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSpecial)
                    .addComponent(txtPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pnlMembership.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Account", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        lblBalance.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblBalance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBalance.setText("Balance:");

        txtBalance.setEditable(false);
        txtBalance.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        lblSponsor.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblSponsor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSponsor.setText("Sponsor:");

        txtSponsorId.setEditable(false);
        txtSponsorId.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtSponsorId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtSponsorIdMousePressed(evt);
            }
        });

        lblVisits.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblVisits.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblVisits.setText("Visits:");

        txtVisits.setEditable(false);
        txtVisits.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        javax.swing.GroupLayout pnlMembershipLayout = new javax.swing.GroupLayout(pnlMembership);
        pnlMembership.setLayout(pnlMembershipLayout);
        pnlMembershipLayout.setHorizontalGroup(
            pnlMembershipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMembershipLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMembershipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblVisits, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSponsor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblBalance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMembershipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSponsorId, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                    .addComponent(txtVisits)
                    .addComponent(txtBalance))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlMembershipLayout.setVerticalGroup(
            pnlMembershipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMembershipLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMembershipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBalance)
                    .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMembershipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSponsor)
                    .addComponent(txtSponsorId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlMembershipLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVisits)
                    .addComponent(txtVisits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnOk.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("* Indicates required fields.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlPlayer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlContact, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlMembership, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(btnOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlMembership, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlPlayer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancel)
                            .addComponent(btnOk)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMonthActionPerformed
        refreshAvailableBirthdates();
    }//GEN-LAST:event_cboMonthActionPerformed

    private void cboYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboYearActionPerformed
        refreshAvailableBirthdates();
    }//GEN-LAST:event_cboYearActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.player = null;
        setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        
        if (!checkPassword()) return;        
                
        if (existingPlayer != null)
        {
            player = existingPlayer;
        }
        else
        {
            player = Player.createNew();
        }

        String validationResult = validateProfile();
        
        if (validationResult == null)
        {
            setVisible(false);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "There was a problem with the data [" + validationResult + "]. Please try again.", "Data Error", JOptionPane.ERROR_MESSAGE);
            player = null;
        }                
    }//GEN-LAST:event_btnOkActionPerformed

    
    
    private void txpVerifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txpVerifyActionPerformed
        checkPassword();
    }//GEN-LAST:event_txpVerifyActionPerformed

    private void txtSponsorIdMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSponsorIdMousePressed
        if (existingPlayer != null)
        {
            Integer sponsorId = txtSponsorId.getText() == null || txtSponsorId.getText().isEmpty() ? null : Integer.parseInt(txtSponsorId.getText());
            SponsorshipDialog sponsorDlg = new SponsorshipDialog(this, existingPlayer, sponsorId);
            sponsorDlg.setVisible(true);
            if (sponsorDlg.getSponsor() != null)
            {
                txtSponsorId.setText((sponsorDlg.getSponsor().getId() == existingPlayer.getId()) ? null : sponsorDlg.getSponsor().getId()+"");
            }
        }
    }//GEN-LAST:event_txtSponsorIdMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JComboBox cboDate;
    private javax.swing.JComboBox cboMonth;
    private javax.swing.JComboBox cboSex;
    private javax.swing.JComboBox cboState;
    private javax.swing.JComboBox cboYear;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblBalance;
    private javax.swing.JLabel lblCity;
    private javax.swing.JLabel lblClubRating;
    private javax.swing.JLabel lblDob;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFirstName;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblLastName;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblSex;
    private javax.swing.JLabel lblSpecial;
    private javax.swing.JLabel lblSponsor;
    private javax.swing.JLabel lblState;
    private javax.swing.JLabel lblVerify;
    private javax.swing.JLabel lblVisits;
    private javax.swing.JLabel lblZip;
    private javax.swing.JPanel pnlContact;
    private javax.swing.JPanel pnlInfo;
    private javax.swing.JPanel pnlMembership;
    private javax.swing.JPanel pnlPlayer;
    private javax.swing.JPasswordField txpPassword;
    private javax.swing.JPasswordField txpVerify;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtBalance;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtClubRating;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtPosition;
    private javax.swing.JTextField txtSponsorId;
    private javax.swing.JTextField txtVisits;
    private javax.swing.JTextField txtZip;
    // End of variables declaration//GEN-END:variables
}
