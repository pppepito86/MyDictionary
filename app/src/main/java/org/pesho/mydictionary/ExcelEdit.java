package org.pesho.mydictionary;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelEdit {

	private WritableWorkbook workbook = null;
	private WritableSheet sheet;
	
	public ExcelEdit() {
		initSheet();
	}

	private void initSheet() {
		try {
			WorkbookSettings workbookSettings = new WorkbookSettings();
			workbookSettings.setEncoding("Cp1252");
			File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			File file = new File(directory, "Test.xls");
			if (file.exists()) {
				Workbook tmp = Workbook.getWorkbook(file, workbookSettings);
				workbook = Workbook.createWorkbook(file, tmp);
			} else {
				workbook = Workbook.createWorkbook(file, workbookSettings);
				workbook.createSheet("dictionary", 0);
			}
			sheet = workbook.getSheet(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeAndClose() {
		try {
			write();
		} finally {
			close();
		}
	}
	
	public void write() {
		try {
			workbook.write();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			if (workbook != null) {
				workbook.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public WritableSheet getSheet() {
		return sheet;
	}

}
