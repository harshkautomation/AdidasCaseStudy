package framework;

import jxl.*;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelReader class provides various methods to perform operations when working with excel document.
 * Methods include reading a cell value, get column data, get row data etc.
 *
 */
public class ExcelReader {

	public static Sheet sheet = null;
	
	/**
	 * Constructor that initializes Sheet variable with the location of excel file path. 
	 * @param filePath file path where excel document is located.
	 * @param sheetName Name of sheet in excel document
	 */
	public ExcelReader(String filePath, String sheetName) {
		try {
			Workbook wrk = Workbook.getWorkbook(new File(filePath));
			sheet = wrk.getSheet(sheetName);
		} catch (BiffException | IOException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to read excel sheet----> " + sheetName + "at file path: " + filePath);
		}
	}

	/**
	 * Get cell value of the corresponding sheet name by passing row and column values as input parameters.
	 * @param row row value of corresponding sheet
	 * @param column column value of corresponding sheet
	 * @return cell value as text
	 */
	public  String getCell(int row, int column) {
		Cell cell = sheet.getCell(column, row);
		String cellValue = cell.getContents();
		return cellValue;
	}

	/**
	 * Get cell value of the corresponding sheet name by passing row and column values as input parameters.
	 * @param row row value of corresponding sheet
	 * @param columnName column name of corresponding sheet
	 * @return cell value as text
	 */
	public  String getCell(int row, String columnName) {
		List<String> columns = getColumns();

		String cellValue = null;
		for (int i = 0; i < columns.size(); i++) {
			if (columns.get(i).equals(columnName)) {
				Cell cell = sheet.getCell(i, row);
				cellValue = cell.getContents();
			}
		}

		return cellValue;
	}

	/**
	 * Get a specified column data as collection from the corresponding sheet
	 * @param column column number of corresponding sheet
	 * @return collection of all the values of a column number passed as input parameter.
	 */
	public  List<String> getColumnData(int column) {
		List<String> colValue = new ArrayList<String>();
		Cell[] cols = sheet.getColumn(column);
		for (int i = 1; i < cols.length; i++) {
			// if (!cols[i].getContents().equals(""))
			colValue.add(cols[i].getContents());
		}
		return colValue;
	}

	/**
	 * Get a specified column data as collection from the corresponding sheet
	 * @param columnName column name of corresponding sheet
	 * @return collection of all the values of a column name passed as input parameter.
	 */
	public  List<String> getColumnData(String columnName) {
		List<String> colValue = new ArrayList<String>();
		List<String> columns = getColumns();
		for (int i = 1; i < columns.size(); i++) {
			if (columns.get(i).equals(columnName)) {
				Cell[] cols = sheet.getColumn(i);
				for (Cell col : cols) {
					if (!col.getContents().equals(""))
						colValue.add(col.getContents());
				}
			}
		}
		return colValue;
	}

	/**
	 * Get a specified row data as collection from the corresponding sheet
	 * @param row row number passed as input parameter.
	 * @return collection of row data as Map<Key,Value> pair with column name as Key and row data as Value. 
	 */
	public  Map<String, String> getRowData(int row) {
		Map<String, String> rowValue = new HashMap<String, String>();
		Cell[] rows = sheet.getRow(row);
		List<String> columns = getColumns();
		for (int i = 0; i < rows.length; i++)
			rowValue.put(columns.get(i), rows[i].getContents());

		return rowValue;
	}

	/**
	 * Get all the rows data as collection from the corresponding sheet.
	 * @return collection of all rows data as a List
	 */
	public  List<String> getAllRowsData() {
		List<String> rowData = new ArrayList<String>();
		int totalNoOfRows = sheet.getRows();
		int totalNoOfCols = sheet.getColumns();

		for (int row = 1; row < totalNoOfRows; row++) {
			StringBuilder builder = new StringBuilder();
			for (int col = 0; col < totalNoOfCols; col++) {
				String text = sheet.getCell(col, row).getContents();
				if (!text.equals(""))
					builder.append(text).append("-");
			}
			rowData.add(builder.toString());
		}
		
		return rowData;
	}
	
	/**
	 * Method to obtain row size.
	 * @return total number of rows returned as an integer.
	 */
	public int getRowSize(){
		return sheet.getRows();
	}

	/**
	 * Get all the columns present in the specified sheet as a collection.
	 * @return all the columns present in the specified sheet as a collection of List.
	 */
	public  List<String> getColumns() {
		int masterSheetColumnIndex = sheet.getColumns();
		List<String> columns = new ArrayList<String>();
		for (int x = 0; x < masterSheetColumnIndex; x++) {
			Cell celll = sheet.getCell(x, 0);
			String d = celll.getContents().trim();
			columns.add(d);
		}
		return columns;
	}

	/**
	 * Get the index of the row(s) matching a text value.
	 * @param text input text value to be checked in all the rows.
	 * @return collection of all the row indexes containing the matching text.
	 */
	public  List<Integer> getRowsIndexWhere(String text) {
		List<Integer> rows = new ArrayList<>();
		List<String> rowsData = getAllRowsData();
		for (int i = 0; i < rowsData.size(); i++) {

			if (rowsData.get(i).contains(text))
				rows.add(i);
		}
		return rows;
	}
}

