/**

	Copyright:
	==========
	
	Splinter - The RAT (Remote Administrator Tool)
	Developed By Solomon Sonya, Nick Kulesza, and Dan Gunter
	Copyright 2013 Solomon Sonya
	
	This copyright applies to the entire Splinter Project and all relating source code

	This program is free software: you are free to  redistribute 
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.       

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
	
	By executing this program, you assume full responsibility 
	and will hold zero responsibility, liability, damages, etc to the
	development team over this program or any variations of this program.
	This program is not meant to be harmful or used in a malicious manner.
	
	Notes:
	===========
	This program is 100% open source and still a very BETA version. 
	I don't know of any significant bugs.... but I'm sure they may exist ;-)
	If you find one, congratulations, please forward the data back to us 
	and we'll do our best to put a fix/workaround if applicable (and time permitting...)
	Finally, feature imprevements/updates, etc, please let us know what you would
	like to see, and we'll do my best to have it incorporated into the newer 
	versions of Splinter or new projects to come.  We're here to help.
	
	Thanks again, 
	
	Solomon
	
	Contact: 
	========
	Twitter	--> @splinter_therat, @carpenter1010
	Email	--> splinterbotnet@gmail.com
	GitHub	--> https://github.com/splinterbotnet
**/




package Controller.GUI;

import Controller.Drivers.Drivers;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class JTable_Solomon extends JPanel
  implements ItemListener, ActionListener, ListSelectionListener
{
  String strMyClassName = "JTable_Solomon";
  public DefaultTableModel dfltTblMdl;
  JPanel jpnlJTable = new JPanel();

  JTable jtblMyJTbl = null;
  JScrollPane jscrlpneJTable;
  JPanel jpnl_jcbSortTableBy = new JPanel(new BorderLayout());
  JPanel jpnlSortOptions = new JPanel();
  JLabel jlblSortBy = new JLabel("Sort By...", 2);
  public JComboBox jcbSortTableBy = null;
  JCheckBox jcbSortInAscendingOrder = new JCheckBox("Sort in Ascending Order", true);
  public boolean sortInAscendingOrder = true;
  public JCheckBox jcbEnableGPS_Resolution = new JCheckBox("Enable GPS Resolution", true);

  public JPanel jpnlNumRows = new JPanel();
  JLabel jlblNumRows_Text = new JLabel("Num Rows Populated: ", 2);
  JLabel jlblNumRows = new JLabel("0", 2);

  public JButton jbtnRefresh = new JButton("Refresh");
  public JButton jbtnDisconnectImplant = new JButton("Disconnect Selected Implant");
  public JButton jbtnGoogleMap = new JButton("Google Map");
  public JButton jbtnClear = new JButton("Clear and Refresh");

  private JPanel jpnlNorth_Heading = new JPanel(new GridLayout(2, 1));
  public JLabel jlblHeading = null;

  public volatile boolean updateJTable = false;
  public Timer tmrUpdateJTable = new Timer(50, this);

  public Color clrDefaultBackground = Color.white;
  public Color clrDefaultForeground = Color.black;
  Vector vctTblData;
  boolean acceptComponent = true;

  public boolean i_am_beacon_jtable = false;
  public boolean i_am_loggie_jtable = false;

  public JTable_Solomon(Vector vColNames, Vector vToolTips, String headingTitle, Color titleForeground, Color titleBackground)
  {
    try
    {
      try
      {
        this.jlblHeading = new JLabel(headingTitle, 0);
        this.jlblHeading.setForeground(titleForeground);

        if (titleBackground != null)
        {
          this.jlblHeading.setOpaque(true);
          this.jlblHeading.setBackground(titleBackground);
        }
        this.jlblHeading.setFont(new Font("Courier", 1, 18));
      }
      catch (Exception localException1) {
      }
      initializeJTable(vColNames, vToolTips);
      initializeGUI(vColNames);
      try
      {
        this.tmrUpdateJTable.start();
      } catch (Exception localException2) {
      }
    }
    catch (Exception e) {
      Drivers.eop("JTable_Solomon Constructor", this.strMyClassName, e, "Error on Constructor Method", false);
    }
  }

  public boolean initializeGUI(Vector vColNm)
  {
    try
    {
      setLayout(new BorderLayout());

      this.jcbSortTableBy = new JComboBox();

      for (int i = 0; i < vColNm.size(); i++)
      {
        this.jcbSortTableBy.addItem((String)vColNm.get(i));
      }

      this.jscrlpneJTable = new JScrollPane(this.jtblMyJTbl);
      add(this.jscrlpneJTable, "Center");

      this.jpnlSortOptions.add(this.jlblSortBy);
      this.jpnlSortOptions.add(this.jcbSortTableBy);
      this.jpnlSortOptions.add(this.jcbSortInAscendingOrder);

      this.jpnl_jcbSortTableBy.add("North", this.jlblHeading);
      this.jpnl_jcbSortTableBy.add("South", new JLabel("  "));
      this.jpnl_jcbSortTableBy.add(this.jpnlSortOptions, "West");
      add(this.jpnl_jcbSortTableBy, "North");

      this.jpnlNumRows.add(this.jbtnDisconnectImplant);
      this.jpnlNumRows.add(this.jbtnGoogleMap);
      this.jpnlNumRows.add(this.jbtnRefresh);
      this.jpnlNumRows.add(this.jbtnClear); this.jbtnClear.setVisible(false);

      this.jpnlNumRows.add(this.jcbEnableGPS_Resolution);
      this.jpnlNumRows.add(this.jlblNumRows_Text);
      this.jpnlNumRows.add(this.jlblNumRows);
      this.jpnl_jcbSortTableBy.add(this.jpnlNumRows, "East");

      this.jtblMyJTbl.revalidate();
      this.jtblMyJTbl.repaint();
      validate();

      this.jcbSortInAscendingOrder.addItemListener(this);
      this.jcbSortTableBy.addItemListener(this);
      this.jtblMyJTbl.getSelectionModel().addListSelectionListener(this);

      this.jlblSortBy.setForeground(new Color(0, 51, 255));
      this.jlblNumRows_Text.setForeground(new Color(0, 51, 255));

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("initializeGUI", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean initializeJTable(Vector vctColNms, final Vector vctColToolTips)
  {
    try
    {
      this.dfltTblMdl = new DefaultTableModel(null, vctColNms);

      this.jtblMyJTbl = new JTable(this.dfltTblMdl)
      {
        public boolean isCellEditable(int r, int c)
        {
          return false;
        }

        protected JTableHeader createDefaultTableHeader()
        {
          return new JTableHeader(this.columnModel)
          {
            public String getToolTipText(MouseEvent me)
            {
              try
              {
                String tip = null;
                Point mouseHoverPoint = me.getPoint();
                int colIndex_HoverPoint = this.columnModel.getColumnIndexAtX(mouseHoverPoint.x);
                int colIndex = this.columnModel.getColumn(colIndex_HoverPoint).getModelIndex();

                if (colIndex < vctColToolTips.size()) {
                  return (String)vctColToolTips.get(colIndex);
                }

                return "";
              }
              catch (Exception e)
              {
              }
              return "";
            }
          };
        }

        public Component prepareRenderer(TableCellRenderer renderer, int row, int col)
        {
          Component c = null;
          JTable_Solomon.this.acceptComponent = false;
          try
          {
            c = super.prepareRenderer(renderer, row, col);

            if (row < JTable_Solomon.this.dfltTblMdl.getRowCount()) {
              JTable_Solomon.this.acceptComponent = true;
            }
          }
          catch (Exception localException)
          {
          }

          if (JTable_Solomon.this.acceptComponent)
          {
            TableCellRenderer cellRenderer = renderer;

            if ((JTable_Solomon.this.dfltTblMdl.getValueAt(row, 0) == null) || (!JTable_Solomon.this.dfltTblMdl.getValueAt(row, 0).equals("*")) || (isCellSelected(row, col)))
            {
              if (isCellSelected(row, col))
              {
                c.setForeground(Color.black);
              }
              else
              {
                c.setBackground(JTable_Solomon.this.clrDefaultBackground);
                c.setForeground(JTable_Solomon.this.clrDefaultForeground);
              }
            }

            if (((c instanceof JComponent)) && (col == 0))
            {
              JComponent jc = (JComponent)c;
              jc.setToolTipText("Testing tool tip");
            }
            else if (((c instanceof JComponent)) && (col > 0))
            {
              try
              {
                JComponent jc = (JComponent)c;
                jc.setToolTipText(""+JTable_Solomon.this.dfltTblMdl.getValueAt(row, col));
              }
              catch (Exception localException1)
              {
              }

            }

          }

          return c;
        }
      };
      this.jtblMyJTbl.setSelectionMode(0);
      this.jtblMyJTbl.setAutoResizeMode(0);
      this.jtblMyJTbl.setAutoCreateColumnsFromModel(false);
      this.jtblMyJTbl.setAutoCreateRowSorter(false);
      this.jtblMyJTbl.getTableHeader().setReorderingAllowed(false);

      this.jtblMyJTbl.setOpaque(true);
      this.jtblMyJTbl.getTableHeader().setBackground(SystemColor.blue);

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("initializeJTable", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public void valueChanged(ListSelectionEvent lse)
  {
    try
    {
      if (lse.getSource() == this.jtblMyJTbl.getSelectionModel())
      {
        this.jtblMyJTbl.getSelectedRow();
      }

    }
    catch (Exception e)
    {
      Drivers.eop("valueChanged", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }
  }

  public void actionPerformed(ActionEvent ae)
  {
    try
    {
      if (ae.getSource() == this.tmrUpdateJTable)
      {
        if (this.updateJTable)
        {
          this.updateJTable = false;

          if (this.i_am_beacon_jtable)
          {
            Drivers.updateBeaconImplants();
          }
          else if (this.i_am_loggie_jtable)
          {
            Drivers.updateLoggingAgents();
          }
          else
          {
            Drivers.updateConnectedImplants();
          }

        }

      }

    }
    catch (Exception e)
    {
      Drivers.eop("actionPerformed", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }
  }

  public void itemStateChanged(ItemEvent ie)
  {
    try
    {
      if (ie.getSource() == this.jcbSortTableBy)
      {
        if (this.dfltTblMdl.getRowCount() > 1)
        {
          sortJTable_ByRows(this.dfltTblMdl, this.jcbSortTableBy.getSelectedIndex(), this.sortInAscendingOrder);
        }

      }
      else if (ie.getSource() == this.jcbSortInAscendingOrder)
      {
        this.sortInAscendingOrder = (!this.sortInAscendingOrder);

        if (this.dfltTblMdl.getRowCount() > 1)
        {
          sortJTable_ByRows(this.dfltTblMdl, this.jcbSortTableBy.getSelectedIndex(), this.sortInAscendingOrder);
        }
      }

    }
    catch (Exception e)
    {
      Drivers.eop("itemStateChanged", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }
  }

  public boolean addRow(Vector<String> vctInfoToPopulate)
  {
    try
    {
      if (vctInfoToPopulate == null) {
        return false;
      }

      this.dfltTblMdl.addRow(vctInfoToPopulate.toArray());

      this.dfltTblMdl.fireTableDataChanged();

      this.jlblNumRows.setText(""+this.dfltTblMdl.getRowCount());

      validate();
      this.jtblMyJTbl.validate();

      return true;
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
    }
    catch (Exception e)
    {
      Drivers.eop("addRow", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean removeAllRows()
  {
    try
    {
      this.dfltTblMdl.getDataVector().removeAllElements();

      this.dfltTblMdl.fireTableDataChanged();
      this.jtblMyJTbl.validate();

      this.jlblNumRows.setText("0");

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("removeAllRows", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean removeRow(int rowToRemove)
  {
    try
    {
      this.dfltTblMdl.removeRow(rowToRemove);

      this.jtblMyJTbl.revalidate();
      this.dfltTblMdl.fireTableRowsDeleted(rowToRemove, rowToRemove);
      this.dfltTblMdl.fireTableRowsUpdated(0, this.dfltTblMdl.getRowCount() - 1);

      this.dfltTblMdl.fireTableDataChanged();

      this.jlblNumRows.setText(""+this.dfltTblMdl.getRowCount());

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("removeRow", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public String getSelectedCellContents()
  {
    try
    {
      return (String)this.jtblMyJTbl.getValueAt(getSelectedRowIndex(), getSelectedColIndex());
    }
    catch (Exception e)
    {
      Drivers.eop("getSelectedCellContents", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return "UNKNOWN";
  }

  public String getCellContents(int row, int col)
  {
    try
    {
      return (String)this.jtblMyJTbl.getValueAt(row, col);
    }
    catch (Exception e)
    {
      Drivers.eop("getCellContents", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return "UNKNOWN";
  }

  public int getSelectedRowIndex()
  {
    try
    {
      return this.jtblMyJTbl.getSelectedRow();
    }
    catch (Exception e)
    {
      Drivers.eop("getSelectedRowIndex", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return 0;
  }

  public int getSelectedColIndex()
  {
    try
    {
      return this.jtblMyJTbl.getSelectedColumn();
    }
    catch (Exception e)
    {
      Drivers.eop("getSelectedColIndex", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return 0;
  }

  public boolean sortJTable()
  {
    try
    {
      sortJTable_ByRows(this.dfltTblMdl, this.jcbSortTableBy.getSelectedIndex(), this.sortInAscendingOrder);

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("sortJTable", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public boolean sortJTable_ByRows(DefaultTableModel dfltTModel, int colToSort, boolean ascending)
  {
    try
    {
      this.vctTblData = dfltTModel.getDataVector();

      if ((this.vctTblData == null) || (this.vctTblData.size() < 2))
      {
        return false;
      }

      Collections.sort(this.vctTblData, new ColumnSorter(colToSort, ascending));
      this.dfltTblMdl.fireTableStructureChanged();

      return true;
    }
    catch (Exception e)
    {
      Drivers.eop("sortJTable_ByRows", this.strMyClassName, e, e.getLocalizedMessage(), false);
    }

    return false;
  }

  public void sop(String out)
  {
    System.out.println(out);
  }

  public class ColumnSorter
    implements Comparator
  {
    int colIndex = 0;
    boolean ascending = true;

    ColumnSorter(int colIndex, boolean ascending)
    {
      if (colIndex < 0) {
        colIndex = 0;
      }
      this.colIndex = colIndex;
      this.ascending = ascending;
    }

    public int compare(Object a, Object b)
    {
      Vector v1 = (Vector)a;
      Vector v2 = (Vector)b;
      Object o1 = v1.get(this.colIndex);
      Object o2 = v2.get(this.colIndex);

      if (((o1 instanceof String)) && (((String)o1).length() == 0))
      {
        o1 = null;
      }

      if (((o2 instanceof String)) && (((String)o2).length() == 0))
      {
        o2 = null;
      }

      if ((o1 == null) && (o2 == null))
      {
        return 0;
      }

      if (o1 == null)
      {
        return 1;
      }

      if (o2 == null)
      {
        return -1;
      }

      if ((o1 instanceof Comparable))
      {
        if (this.ascending)
        {
          return ((Comparable)o1).compareTo(o2);
        }

        return ((Comparable)o2).compareTo(o1);
      }

      if (this.ascending)
      {
        return o1.toString().compareTo(o2.toString());
      }

      return o2.toString().compareTo(o1.toString());
    }
  }
}