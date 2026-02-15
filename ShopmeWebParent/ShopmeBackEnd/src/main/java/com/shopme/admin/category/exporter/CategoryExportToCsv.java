package com.shopme.admin.category.exporter;

import java.io.IOException;
import java.util.List;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstarctExporter;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class CategoryExportToCsv extends AbstarctExporter {

	
	public void export (List<Category>listCategories,HttpServletResponse response) throws IOException {
	
		super.setResponseHeader(response, "text/csv", ".csv","category_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader ={"Category ID","Category aName"};
		String[] fieldMapping = {"id","name"};
		
		csvWriter.writeHeader(csvHeader);
		
		for (Category category : listCategories) {
			category.setName(category.getName().replace("--", " "));
			csvWriter.write(category, fieldMapping);
		}
		csvWriter.close();
	}
}
