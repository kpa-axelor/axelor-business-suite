package com.axelor.apps.gst.web;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.InvoiceLineTax;
import com.axelor.apps.account.db.Tax;
import com.axelor.apps.account.db.repo.InvoiceRepository;
import com.axelor.apps.account.db.repo.TaxRepository;
import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Currency;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.repo.ProductRepository;
import com.axelor.apps.base.service.app.AppBaseService;
import com.axelor.inject.Beans;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
  @Inject ProductRepository productRepository;
  @Inject TaxRepository TaxRepository;

  @Transactional
  public void createInvoiceForm(ActionRequest request, ActionResponse response) {
    Invoice invoice1 = request.getContext().asType(Invoice.class);
    @SuppressWarnings("unchecked")
    List<Integer> ids = (List<Integer>) request.getContext().get("_id");
    List<InvoiceLine> invoiceLineList = new ArrayList<InvoiceLine>();
    List<InvoiceLineTax> invoiceLineTexList = new ArrayList<InvoiceLineTax>();
    InvoiceLineTax invoiceLineTax;
    Invoice invoice = new Invoice();
    Tax tax;
    System.out.println("itemids=>" + ids);
    System.out.println(request.getContext().get("partner"));
    Product product;
    InvoiceLine invoiceLine;
    BigDecimal qty = new BigDecimal(1);
    BigDecimal exTaxTotal = new BigDecimal(0);
    BigDecimal inTaxTotal = new BigDecimal(0);
    BigDecimal onlyTaxTotal = new BigDecimal(0);
    for (int i = 0; i < ids.size(); i++) {
      product = productRepository.all().filter("self.id = ?", ids.get(i)).fetchOne();
      System.out.println("product=>" + product);
      invoiceLine = new InvoiceLine();

      invoiceLine.setProduct(product);
      invoiceLine.setTypeSelect(0);
      invoiceLine.setProductName(product.getName());
      invoiceLine.setQty(qty);
      invoiceLine.setPrice(product.getSalePrice());
      System.out.println("ExTotal=>" + qty.multiply(invoiceLine.getPrice()));
      invoiceLine.setExTaxTotal(
          (qty.multiply(invoiceLine.getPrice())
              .setScale(AppBaseService.DEFAULT_NB_DECIMAL_DIGITS, RoundingMode.HALF_EVEN)));

      tax =
          TaxRepository.all()
              .filter(
                  "self.code = 'GST'  AND self.activeTaxLine != null AND self.activeTaxLine.value = ?",
                  product.getGstRate())
              .fetchOne();
      if (tax != null) {
        invoiceLineTax = new InvoiceLineTax();
        invoiceLine.setTaxLine(tax.getActiveTaxLine());
        invoiceLineTax.setTaxLine(tax.getActiveTaxLine());
        BigDecimal taxTotal =
            (invoiceLine.getExTaxTotal().multiply(invoiceLine.getTaxLine().getValue()))
                .setScale(AppBaseService.DEFAULT_NB_DECIMAL_DIGITS, RoundingMode.HALF_EVEN);
        invoiceLineTax.setTaxTotal(taxTotal);
        invoiceLineTax.setExTaxBase(invoiceLine.getExTaxTotal());
        onlyTaxTotal = onlyTaxTotal.add(taxTotal);

        invoice.addInvoiceLineTaxListItem(invoiceLineTax);
      } else {
        invoiceLine.setTaxLine(null);
      }

      exTaxTotal = exTaxTotal.add(invoiceLine.getExTaxTotal());
      invoice.setExTaxTotal(exTaxTotal);
      invoice.addInvoiceLineListItem(invoiceLine);
      invoice.setTaxTotal(onlyTaxTotal);
      invoice.setInTaxTotal(invoice.getExTaxTotal().add(onlyTaxTotal));
    }

    Partner partner = new Partner();
    Company company = new Company();
    company = invoice1.getCompany();

    partner = (Partner) request.getContext().get("partner");
    Currency currency = (Currency) request.getContext().get("currency");
    invoice.setCompany(company);
    invoice.setPartner(partner);
    invoice.setAddress(partner.getMainAddress());
    invoice.setCurrency(currency);
    invoice.setOperationTypeSelect(3);
    System.out.println("before save");
    InvoiceRepository invoiceRepositor = Beans.get(InvoiceRepository.class);
    invoiceRepositor.save(invoice);
    System.out.println("=>invoiceId" + invoice.getId());

    response.setView(
        ActionView.define("Create Invoice")
            .model(Invoice.class.getName())
            .add("form", "Invoice-form")
            .add("grid", "Invoice-grid")
            .context("_showRecord", invoice.getId())
            .map());
  }
}
