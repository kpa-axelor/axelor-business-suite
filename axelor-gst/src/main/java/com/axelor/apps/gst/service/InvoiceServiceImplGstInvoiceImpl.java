package com.axelor.apps.gst.service;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.repo.InvoiceRepository;
import com.axelor.apps.account.service.app.AppAccountService;
import com.axelor.apps.account.service.invoice.InvoiceLineService;
import com.axelor.apps.account.service.invoice.factory.CancelFactory;
import com.axelor.apps.account.service.invoice.factory.ValidateFactory;
import com.axelor.apps.account.service.invoice.factory.VentilateFactory;
import com.axelor.apps.base.service.PartnerService;
import com.axelor.apps.base.service.alarm.AlarmEngineService;
import com.axelor.apps.businessproject.service.InvoiceServiceProjectImpl;
import com.axelor.exception.AxelorException;
import com.google.inject.Inject;

public class InvoiceServiceImplGstInvoiceImpl extends InvoiceServiceProjectImpl {
	@Inject
	public InvoiceServiceImplGstInvoiceImpl(ValidateFactory validateFactory, VentilateFactory ventilateFactory,
			CancelFactory cancelFactory, AlarmEngineService<Invoice> alarmEngineService, InvoiceRepository invoiceRepo,
			AppAccountService appAccountService, PartnerService partnerService, InvoiceLineService invoiceLineService) {
		super(validateFactory, ventilateFactory, cancelFactory, alarmEngineService, invoiceRepo, appAccountService,
				partnerService, invoiceLineService);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Invoice compute(Invoice invoice) throws AxelorException {
		System.out.println("in InvoiceServiceImplGstInvoiceImpl in compute method");
		Invoice invoice2 = super.compute(invoice);

		int n = 0;
		BigDecimal netAmount = new BigDecimal(n);
		BigDecimal netIgst = new BigDecimal(n);
		BigDecimal netCgst = new BigDecimal(n);
		BigDecimal netSgst = new BigDecimal(n);
		BigDecimal grossAmount = new BigDecimal(n);
		netAmount = netAmount.add(netAmount);
		System.out.println(netAmount);

		List<InvoiceLine> invoiceLines = (List<InvoiceLine>) invoice.getInvoiceLineList();
		for (InvoiceLine line : invoiceLines) {
			netIgst = netIgst.add(line.getIgst());
			netCgst = netCgst.add(line.getCgst());
			netSgst = netSgst.add(line.getSgst());
		}
		invoice.setNetIgst(netIgst);
		invoice.setNetCgst(netCgst);
		invoice.setNetSgst(netSgst);
		return invoice;
	}
}
