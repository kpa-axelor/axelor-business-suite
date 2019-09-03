package com.axelor.apps.gst.service;

import com.axelor.apps.ReportFactory;
import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.service.invoice.print.InvoicePrintServiceImpl;
import com.axelor.apps.report.engine.ReportSettings;
import com.axelor.exception.AxelorException;
import com.axelor.i18n.I18n;

public class InvoiceReportGstModule extends InvoicePrintServiceImpl {
	@Override
	public ReportSettings prepareReportSettings(Invoice invoice) throws AxelorException {
		System.out.println("this is my report");
		@SuppressWarnings("unused")
		String title = I18n.get("Invoice");
		String locale = ReportSettings.getPrintingLocale(invoice.getPartner());
		@SuppressWarnings("unused")
		ReportSettings reportSetting = ReportFactory.createReport("InvoiceAddGst.rptdesign", title + " - ${date}");
		return reportSetting.addParam("InvoiceId", invoice.getId()).addParam("Locale", locale);
	}

}
