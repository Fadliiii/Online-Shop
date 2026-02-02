package com.shopme.admin.user.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserCsvExporter extends AbstarctExporter{

	public void export(List<User>listUser, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);
		
		
		String[] csvHeader ={"User ID","Email","First Name","Last Name","Roles","Enabled"};
		String[] fieldMapping = {"id","email","firstName","lastName","roles","enabled"};
		
		csvWriter.writeHeader(csvHeader);
		for (User user : listUser) {
			csvWriter.write(user, fieldMapping);
		}
		
		csvWriter.close();
	}
}
