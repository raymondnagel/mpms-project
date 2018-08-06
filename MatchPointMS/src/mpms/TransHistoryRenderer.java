/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpms;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Ray
 */
public class TransHistoryRenderer extends DefaultTableCellRenderer {    

    private static final Color RED = new Color (255, 0, 0);
    private static final Color GREEN = new Color (0, 160, 0);
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        TableModel model = table.getModel();
        if (model instanceof MultiTypeTableModel)
        {
            MultiTypeTableModel multiModel = (MultiTypeTableModel)model;
            String colName = multiModel.getColumnName(column);
            if (colName.equalsIgnoreCase("AMOUNT"))
            {
                TransHistoryRenderer field = (TransHistoryRenderer)comp;
                if (value != null)
                {                    
                    field.setText(Global.DOLLARS.format(value));
                    if ((Double)value > 0)
                    {
                        field.setForeground(RED);
                    }
                    else if ((Double)value < 0)
                    {
                        field.setForeground(GREEN);
                    }
                    else
                    {
                        field.setForeground(Color.BLACK);
                    }
                    field.setHorizontalAlignment(SwingConstants.RIGHT);
                }                
            }
            
            if (colName.equalsIgnoreCase("DATE"))
            {
                TransHistoryRenderer field = (TransHistoryRenderer)comp;
                if (value != null)
                {                    
                    field.setText(Global.EXACT_TIME_AND_DATE.format(value));                    
                }                
            }
        }
        
        return comp;
    }

}
