package com.axelor.apps.gst.web;

import java.util.ArrayList;
import java.util.List;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.repo.InvoiceRepository;
import com.axelor.apps.base.db.Currency;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.repo.ProductRepository;
import com.axelor.inject.Beans;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class ProductController {
	@Inject
	ProductRepository productRepository;

	@Transactional
	public void createInvoiceForm(ActionRequest request, ActionResponse response) {
		List<Integer> ids = (List<Integer>) request.getContext().get("_id");
		List<InvoiceLine> invoiceLineList = new ArrayList<InvoiceLine>();
		System.out.println("itemids=>" + ids);
		System.out.println(request.getContext().get("partner"));
		Product product;
		InvoiceLine invoiceLine;
		for (int i = 0; i < ids.size(); i++) {
			product = productRepository.all().filter("self.id = ?", ids.get(i)).fetchOne();
			System.out.println("product=>" + product);
			invoiceLine = new InvoiceLine();
			invoiceLine.setProduct(product);
			invoiceLine.setTaxRate(product.getGstRate());
			invoiceLine.setExTaxTotal(product.getSalePrice());
			invoiceLine.setPrice(product.getSalePrice());
			invoiceLine.setInTaxTotal(product.getSalePrice());
			invoiceLine.setProductName(product.getName());
			invoiceLineList.add(invoiceLine);
		}
		Invoice invoice = new Invoice();
		Partner partner = new Partner();
		partner = (Partner) request.getContext().get("partner");
		Currency currency = (Currency) request.getContext().get("currency");
		invoice.setPartner(partner);
		invoice.setAddress(partner.getMainAddress());
		invoice.setInvoiceLineList(invoiceLineList);
		invoice.setCurrency(currency);
		System.out.println("before save");
		InvoiceRepository invoiceRepositor = Beans.get(InvoiceRepository.class);
		invoiceRepositor.save(invoice);
		System.out.println("=>invoiceId" + invoice.getId());

		response.setView(ActionView.define("Create Invoice").model(Invoice.class.getName()).add("form", "Invoice-form")
				.add("grid", "Invoice-grid").context("_showRecord", invoice.getId()).context("_operationTypeSelect", 3)
				.map());

	}

}
//Map<String, Object> map = new HashMap<String, Object>();	
//map.put("partner",request.getContext().get("partner"));
//response.setValue("partner", request.getContext().get("partner"));
//Partner partner = new Partner();
//partner.setName(name);
//Address address = addressRepository.all().fetchOne();
//invoice.setParty(partner);
//invoice.setInvoiceAddress(address);
//invoice.setInvoiceItem(invoiceLineList);
//System.out.println("invooooo=>" + invoice.getParty());
//invoiceRepository.save(invoice);
//response.setValues(map);
//response.setValues(invoice);
//invoice.setInvoiceItem(invoiceLineList);