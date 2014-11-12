/**
 * This class will contain all code to establish the JTable to include the panel containing the JTable 
 * 
 * You are free to use this code.  Only stipulation: leave author information
 * 
 * @author Solomon Sonya 
 */

package Controller.GUI;

import Controller.Drivers.*;
import Implant.Driver;

import java.awt.Color;
import java.awt.Font;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.*;


public class JTable_Solomon extends JPanel implements ItemListener, ActionListener, ListSelectionListener
{
	String strMyClassName = "JTable_Solomon";
	
	public DefaultTableModel dfltTblMdl;
	
	//JPanel jpnlMain = new JPanel(new BorderLayout());
	
	JPanel jpnlJTable = new JPanel();//to hold the ScrollPane
	
	JTable jtblMyJTbl = null;
	JScrollPane jscrlpneJTable;
	
	JPanel jpnl_jcbSortTableBy = new JPanel(new BorderLayout());
		JPanel jpnlSortOptions = new JPanel();
			JLabel jlblSortBy = new JLabel("Sort By..." ,  JLabel.LEFT);
			public JComboBox jcbSortTableBy = null;
			JCheckBox jcbSortInAscendingOrder = new JCheckBox("Sort in Ascending Order", true);
			public boolean sortInAscendingOrder = true;
			public JCheckBox jcbEnableGPS_Resolution = new JCheckBox("Enable GPS Resolution", true);
			
	public JPanel jpnlNumRows = new JPanel();
		JLabel jlblNumRows_Text = new JLabel("Num Rows Populated: " , JLabel.LEFT);
		JLabel jlblNumRows = new JLabel("0", JLabel.LEFT);
		
		public JButton jbtnRefresh = new JButton("Refresh");//Dummy button for this widget.  It's action performed will be caught by a different class chosen by the program. I know, nice huh???  //Solomon 2013-01-26
		public JButton jbtnDisconnectImplant = new JButton("Disconnect Selected Implant");//Dummy button for this widget.  It's action performed will be caught by a different class chosen by the program. I know, nice huh???  //Solomon 2013-01-26
		public JButton jbtnGoogleMap = new JButton("Google Map");//Dummy button for this widget.  It's action performed will be caught by a different class chosen by the program. I know, nice huh???  //Solomon 2013-01-26
		public JButton jbtnClear = new JButton("Clear and Refresh");
		
		private JPanel jpnlNorth_Heading = new JPanel(new GridLayout(2,1));
		public JLabel jlblHeading = null;
		
		public volatile boolean updateJTable = false;
		public Timer tmrUpdateJTable = new Timer(50, this);
		
		public Color clrDefaultBackground = Color.white;
		public Color clrDefaultForeground = Color.black;
		
	//Leaving out Sorting options for now
	
	//Leaving out Number of Rows and Cols populated for now
		
	Vector vctTblData;
	
	boolean acceptComponent = true;
	
	public boolean i_am_beacon_jtable = false;
	public boolean i_am_loggie_jtable = false;
	public boolean i_am_disconnection_jtable = false;
	
	/**
	 * null is acceptable for the Background color.. this simply means it will not be set to anything
	 * 
	 * @param vColNames
	 * @param vToolTips
	 * @param headingTitle
	 * @param titleForeground
	 * @param titleBackground
	 */
	public JTable_Solomon(Vector vColNames, Vector vToolTips, String headingTitle, Color titleForeground, Color titleBackground)
	{
		try
		{
			//this.setBackground(Color.RED);
			try
			{
				jlblHeading = new JLabel(headingTitle, JLabel.CENTER);				
				jlblHeading.setForeground(titleForeground);
				
				if(titleBackground != null)
				{
					jlblHeading.setOpaque(true);
					jlblHeading.setBackground(titleBackground);
				}
				jlblHeading.setFont(new Font("Courier", Font.BOLD, 18));
				//jpnlNorth_Heading.add(jlblHeading);
			}catch(Exception e){}
			
			initializeJTable(vColNames, vToolTips);
			initializeGUI(vColNames);
			
			//start a timer to update the jtable as well
			try{	tmrUpdateJTable.start();	}	catch(Exception e){}
			
		}//end entire try for constructor mtd
		catch(Exception e)
		{
			Drivers.eop("JTable_Solomon Constructor", strMyClassName, e, "Error on Constructor Method", false);
		}
		
	}//end constructor
	
	public boolean initializeGUI(Vector vColNm)
	{
		try
		{
			this.setLayout(new BorderLayout());
			
			//this.jscrlpneJTable = new JScrollPane(this.jtblMyJTbl);
			this.jcbSortTableBy = new JComboBox();
			
			//Populate JComboBox
			for(int i = 0; i < vColNm.size(); i++)
			{
				this.jcbSortTableBy.addItem((String)vColNm.get(i));
			}
			
			//Manual Resize Cols
			try
			{
				//this.jtblMyJTbl.getColumnModel().getColumn(0).setPreferredWidth(40);
				
			}
			catch(Exception e)
			{
				sop("Could not resize cols. Not a critical error");
			}
			
			
			//Add JTable
			this.jscrlpneJTable = new JScrollPane(this.jtblMyJTbl);
			this.add(this.jscrlpneJTable, BorderLayout.CENTER);
			
			
			//jscrlpneJTable.setPreferredSize(new Dimension(1000, 1000));
			
			//Spacing on the right
			//this.add(new Label("  LEFT"), BorderLayout.WEST);
			
			//MouseAdapters not yet added...
			
			//Add Sorting Options
			jpnlSortOptions.add(this.jlblSortBy);
			jpnlSortOptions.add(this.jcbSortTableBy);
			jpnlSortOptions.add(this.jcbSortInAscendingOrder);
			
			
			jpnl_jcbSortTableBy.add(BorderLayout.NORTH, jlblHeading);
			jpnl_jcbSortTableBy.add(BorderLayout.SOUTH, new JLabel("  "));
			jpnl_jcbSortTableBy.add(jpnlSortOptions, BorderLayout.WEST);
			this.add(this.jpnl_jcbSortTableBy, BorderLayout.NORTH);
			
			//Row Counter
			
			
			jpnlNumRows.add(jbtnDisconnectImplant);
			jpnlNumRows.add(jbtnGoogleMap);			
			jpnlNumRows.add(jbtnRefresh);
			jpnlNumRows.add(jbtnClear); jbtnClear.setVisible(false);
			
			jpnlNumRows.add(jcbEnableGPS_Resolution);
			jpnlNumRows.add(jlblNumRows_Text);
			jpnlNumRows.add(jlblNumRows);
			jpnl_jcbSortTableBy.add(jpnlNumRows, BorderLayout.EAST);
			
			//Validate GUI
			this.jtblMyJTbl.revalidate();
			this.jtblMyJTbl.repaint();
			this.validate();
			
			//Register Events
			this.jcbSortInAscendingOrder.addItemListener(this);
			this.jcbSortTableBy.addItemListener(this);
			this.jtblMyJTbl.getSelectionModel().addListSelectionListener(this);
			
			//Modify Widgets
			this.jlblSortBy.setForeground(new Color(0, 51, 255));
			this.jlblNumRows_Text.setForeground(new Color(0, 51, 255));
			
			

			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("initializeGUI", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean initializeJTable(Vector vctColNms, final Vector vctColToolTips)
	{
		try
		{
			dfltTblMdl = new DefaultTableModel(null, vctColNms);
			
			jtblMyJTbl = new JTable(dfltTblMdl)//create subclass to make each cell non-editable and add specified tooltips when mouse hovers over cells
			{
				/**
				 * @override to disable being able to edit each cell
				 */
				public boolean isCellEditable(int r, int c)
				{
					return false;
				}
				
				/**
				 * Implement Table Header Tooltips
				 * 
				 */
				protected JTableHeader createDefaultTableHeader()
				{
					
					return new JTableHeader(columnModel)
					{
						public String getToolTipText(MouseEvent me)
						{
							try
							{
								String tip = null;
								java.awt.Point mouseHoverPoint = me.getPoint();
								int colIndex_HoverPoint = columnModel.getColumnIndexAtX(mouseHoverPoint.x);
								int colIndex = columnModel.getColumn(colIndex_HoverPoint).getModelIndex();
								
								if(colIndex < vctColToolTips.size())
									return (String) vctColToolTips.get(colIndex);
									
									else
										return "";																	
							}
							
							catch(Exception e)
							{
								return "";
							}
						}
					};
				}
				
				public Component prepareRenderer(TableCellRenderer renderer, int row, int col)
				{
					
					
						Component c = null;
						acceptComponent = false;
						TableCellRenderer cellRenderer;
						
						try
						{
							c = super.prepareRenderer(renderer,  row, col);
							
							if(row < dfltTblMdl.getRowCount())//finally, only proceed if the new row is less than or equal to the rows in the jtable	//2013-02-04 solo edits						
								acceptComponent = true;
						}
						catch(Exception e)
						{
//Drivers.eop("prepareRenderer", strMyClassName, e, e.getLocalizedMessage(), false);// 2013-02-05 solo edits
						}
						
						if(acceptComponent)
						{
							//Component c = super.prepareRenderer(renderer, row, col);
							cellRenderer = renderer;
							//ndeTemp_THREE = null;
							
							if(dfltTblMdl.getValueAt(row,  0) != null && dfltTblMdl.getValueAt(row,  0).equals("*") && !isCellSelected(row, col))
							{
								try
								{
									//sop("here... this is for pg 6");
									
									/*if(false)//place holder)
									{
										c.setBackground(Color.yellow);
										c.setForeground(Color.red);
									}
									else
									{
										c.setBackground(Color.white);
										c.setForeground(Color.black);
									}*/
								
								}
								catch(Exception e)
								{
//Drivers.eop("prepareRenderer", strMyClassName, e, e.getLocalizedMessage(), false);// 2013-02-05 solo edits
								}
							}
							
							else if(isCellSelected(row, col))
							{
								c.setForeground(Color.black);
								//c.setBackground(Color.white);
							}
							
							else
							{
								c.setBackground(clrDefaultBackground);
								c.setForeground(clrDefaultForeground);
							}
							
							//Set Tooltip
							if(c instanceof JComponent && col == 0)
							{
								JComponent jc = (JComponent)c;
								jc.setToolTipText("Testing tool tip");
							}
							
							else if(c instanceof JComponent && col > 0)
							{
								try
								{
									JComponent jc = (JComponent)c;
									jc.setToolTipText("" + dfltTblMdl.getValueAt(row, col));
								}catch(Exception e){}
							}
							
						}// end if(acceptComponent)
						
						else
						{
							//sop("Component rejected");
						}
						
						return c;
					}// end prepareRenderer
				
				
			};//end JTable SubClass
			
			//JTable Options
			this.jtblMyJTbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.jtblMyJTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			this.jtblMyJTbl.setAutoCreateColumnsFromModel(false);
			this.jtblMyJTbl.setAutoCreateRowSorter(false);
			this.jtblMyJTbl.getTableHeader().setReorderingAllowed(false);//because the tooltips are setup by col, if the user changes the header, the tooltips are out of order; to prevent this, lock the headers from being able to move locations
			
			//Set Colors
			jtblMyJTbl.setOpaque(true);
			//this.jtblMyJTbl.getTableHeader().setBackground(SystemColor.blue);
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("initializeJTable", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public void valueChanged(ListSelectionEvent lse)
	{
		try
		{
			
			if(lse.getSource() == this.jtblMyJTbl.getSelectionModel())
			{
				//check if row is selected
				if(this.jtblMyJTbl.getSelectedRow() > -1)
				{
					//A row was selected
				}
			}
			
			
		}
		catch(Exception e)
		{
			Drivers.eop("valueChanged", strMyClassName, e, e.getLocalizedMessage(), false);
		}		
		
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource() == tmrUpdateJTable)
			{
				if(this.updateJTable)
				{
					this.updateJTable = false;
					//this entire ae section can be removed, however, to ensure thread safety, i'm implmenting a seperate update here
					if(this.i_am_beacon_jtable)
					{
						Drivers.updateBeaconImplants();
					}
					else if(this.i_am_loggie_jtable)
					{
						Drivers.updateLoggingAgents();
					}
					else
					{
						Drivers.updateConnectedImplants();	
					}
					
				}
			}
			
			
			//Just for keeper's sake:
			/*if(this.jtblMyJTbl.getSelectedRow() > -1)
			{
				String selectedValue = (String)this.jtblMyJTbl.getValueAt(this.jtblMyJTbl.getSelectedRow(), 0).toString();
				sop("Selected value: " + selectedValue);
			}*/
			
		}
		catch(Exception e)
		{
			Drivers.eop("actionPerformed", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}

	public void itemStateChanged(ItemEvent ie)
	{
		try
		{
			if(ie.getSource() == this.jcbSortTableBy)
			{
				if(this.dfltTblMdl.getRowCount() > 1)
				{
					sortJTable_ByRows(this.dfltTblMdl, this.jcbSortTableBy.getSelectedIndex(), this.sortInAscendingOrder);
				}
			}
			
			else if(ie.getSource() == this.jcbSortInAscendingOrder)
			{
				//toggle the sort flag
				this.sortInAscendingOrder = !this.sortInAscendingOrder;
				
				if(this.dfltTblMdl.getRowCount() > 1)
				{
					sortJTable_ByRows(this.dfltTblMdl, this.jcbSortTableBy.getSelectedIndex(), this.sortInAscendingOrder);
				}
			}
			
		}
		catch(Exception e)
		{
			Drivers.eop("itemStateChanged", strMyClassName, e, e.getLocalizedMessage(), false);
		}
	}
	
	public boolean addRow(Vector<String> vctInfoToPopulate)
	{
		try
		{
			//ensure vector passed in has proper number of elements as the column header for this class's jtable
			/*if(vctInfoToPopulate == null || vctInfoToPopulate.size() != this.vctColNames.size()))
			 * 	throw new Exception("Vector passed in does not contain proper number of elements");*/
			 
			if(vctInfoToPopulate == null) //2013-02-04 solo edits
				return false;
			
			//else, add the row!
			this.dfltTblMdl.addRow(vctInfoToPopulate.toArray());
			
			//this.jtblMyJTbl.add
			
			//check if we need to highlight the row
			this.dfltTblMdl.fireTableDataChanged();
			
			
			this.jlblNumRows.setText("" + this.dfltTblMdl.getRowCount());
			
			this.validate();
			this.jtblMyJTbl.validate();
			
			//this.updateJTable = true;
			
			return true;
		}
		
		catch(ArrayIndexOutOfBoundsException aiobe)
		{
			//dismiss this error for now!
			//2013-02-04 solo edits
		}
		
		catch(Exception e)
		{
			Drivers.eop("addRow", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}

	public boolean removeAllRows()
	{
		try
		{
			this.dfltTblMdl.getDataVector().removeAllElements();
			
			//unfortunately at this point, if the above doesn't work, we'll have to use a while model.getRowCont() > 0 model.deleteRow(0); to clear the rows
			
			//A for loop doesnt work to clear the entire table
			
			/*for(int i = 0; i < this.dfltTblMdl.getRowCount(); i++)
			{
				this.dfltTblMdl.removeRow(i);
				
				this.dfltTblMdl.fireTableRowsDeleted(0,  i);
				this.dfltTblMdl.fireTableRowsUpdated(0,  this.dfltTblMdl.getRowCount()-1);
			}*/
			
			this.dfltTblMdl.fireTableDataChanged();
			this.jtblMyJTbl.validate();
			
			this.jlblNumRows.setText("0");
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("removeAllRows", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}

	public boolean removeRow(int rowToRemove)
	{
		try
		{
			this.dfltTblMdl.removeRow(rowToRemove);
			
			this.jtblMyJTbl.revalidate();
			this.dfltTblMdl.fireTableRowsDeleted(rowToRemove, rowToRemove);//first row, last row
			this.dfltTblMdl.fireTableRowsUpdated(0,  this.dfltTblMdl.getRowCount()-1);
			
			this.dfltTblMdl.fireTableDataChanged();
			
			this.jlblNumRows.setText("" + this.dfltTblMdl.getRowCount());
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("removeRow", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;	
	}
	
	public String getSelectedCellContents()
	{
		try
		{
			return (String)this.jtblMyJTbl.getValueAt(this.getSelectedRowIndex(), this.getSelectedColIndex());
		}
		catch(Exception e)
		{
			Drivers.eop("getSelectedCellContents", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return "UNKNOWN";
	}
	
	public String getCellContents(int row, int col)
	{
		try
		{
			return (String)this.jtblMyJTbl.getValueAt(row, col);
		}
		
		catch(Exception e)
		{
			Drivers.eop("getCellContents", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return "UNKNOWN";
	}
	
	public int getSelectedRowIndex()
	{
		try
		{
			return this.jtblMyJTbl.getSelectedRow();
		}
		catch(Exception e)
		{
			Drivers.eop("getSelectedRowIndex", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return 0;
	}
	
	public int getSelectedColIndex()
	{
		try
		{
			return this.jtblMyJTbl.getSelectedColumn();
			
			//also think about this.jtblMyJTbl.getColumnModel().getColumnIndex(strColHeader; <-- bcs the col index can change based on user preference
		}
		catch(Exception e)
		{
			Drivers.eop("getSelectedColIndex", strMyClassName, e, e.getLocalizedMessage(), false);
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
		catch(Exception e)
		{
			Drivers.eop("sortJTable", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	public boolean sortJTable_ByRows(DefaultTableModel dfltTModel, int colToSort, boolean ascending)
	{
		try
		{
			//get data from the table
			vctTblData = dfltTModel.getDataVector();
			
			if(vctTblData == null || vctTblData.size() < 2)
			{
				return false;
			}
			
			//Sort the model
			Collections.sort(vctTblData, new ColumnSorter(colToSort, ascending));
			this.dfltTblMdl.fireTableStructureChanged();
			
			return true;
		}
		catch(Exception e)
		{
			Drivers.eop("sortJTable_ByRows", strMyClassName, e, e.getLocalizedMessage(), false);
		}
		
		return false;
	}
	
	/**
	 * This comparator class is used to sort vectors of data
	 * this class was taken from http://www.exampledepot.com/egs/javax.swing.table/sorter.html?1=rel and http://www.exampledepot.com/egs/javax.swing.table/SortCol.html
	 * 
	 */
	public class ColumnSorter implements Comparator
	{
		int colIndex = 0;
		boolean ascending = true;
		
		/**
		 * Constructor to take the colindex to sort and boolean if sorting in ascending order
		 */
		ColumnSorter(int colIndex, boolean ascending)
		{
			//ensure colIndex is > 0
			if(colIndex < 0)
				colIndex = 0;
			
			this.colIndex = colIndex;
			this.ascending = ascending;
		}
		
		public int compare(Object a, Object b)
		{
			Vector v1 = (Vector) a;
			Vector v2 = (Vector) b;
			Object o1 = v1.get(colIndex);
			Object o2 = v2.get(colIndex);
			
			//treat empty strains like nulls
			if(o1 instanceof String && ((String)o1).length() == 0)
			{
				o1 = null;
			}
			
			if(o2 instanceof String && ((String)o2).length() == 0)
			{
				o2 = null;
			}
			
			//sort nulls so they appear last, regardless of sort order
			if(o1 == null && o2 == null)
			{
				return 0;
			}
			
			else if(o1 == null)
			{
				return 1;
			}
			
			else if(o2 == null)
			{
				return -1;
			}
			
			else if(o1 instanceof Comparable)
			{
				if(ascending)
				{
					return ((Comparable)o1).compareTo(o2);
				}
				
				else
				{
					return ((Comparable)o2).compareTo(o1);
				}
			}
			
			else
			{
				if(ascending)
				{
					return o1.toString().compareTo(o2.toString());
				}
				
				else
				{
					return o2.toString().compareTo(o1.toString());
				}
			}//end else
			
		}//end compare mtd
		
	}//end class ColumnSorter
	
	public void sop(String out)
	{
		System.out.println(out);
	}
	
	
}//end JTable Class!



























