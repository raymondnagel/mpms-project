/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpms;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ray
 */
public class MultiTypeTableModel extends DefaultTableModel {
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        
        try {
            Class cc = getValueAt(0, columnIndex).getClass();
            return cc;
        } 
        catch (NullPointerException | ArrayIndexOutOfBoundsException ex)
        {
            return Object.class;
        }
    }

}
