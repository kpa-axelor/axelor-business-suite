package com.axelor.apps.gst.service;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.service.AccountManagementAccountService;
import com.axelor.apps.account.service.AnalyticMoveLineService;
import com.axelor.apps.account.service.app.AppAccountService;
import com.axelor.apps.base.service.CurrencyService;
import com.axelor.apps.base.service.PriceListService;
import com.axelor.apps.purchase.service.PurchaseProductService;
import com.axelor.exception.AxelorException;
import com.google.inject.Inject;
import java.util.Map;

public class GstServiceImpl
    extends com.axelor.apps.supplychain.service.InvoiceLineSupplychainService {
  @Inject
  public GstServiceImpl(
      CurrencyService currencyService,
      PriceListService priceListService,
      AppAccountService appAccountService,
      AnalyticMoveLineService analyticMoveLineService,
      AccountManagementAccountService accountManagementAccountService,
      PurchaseProductService purchaseProductService) {
    super(
        currencyService,
        priceListService,
        appAccountService,
        analyticMoveLineService,
        accountManagementAccountService,
        purchaseProductService);
  }

  @Override
  public Map<String, Object> fillPriceAndAccount(
      Invoice invoice, InvoiceLine invoiceLine, boolean isPurchase) throws AxelorException {
    Map<String, Object> productInformation =
        super.fillPriceAndAccount(invoice, invoiceLine, isPurchase);
    //    System.out.println("IN GstServiceImpl");
    //    System.out.println(invoiceLine.getTaxLine().getValue());
    //    System.out.println(invoiceLine.getProduct().getGstRate());
    //
    //    BigDecimal exTaxTotal = invoiceLine.getExTaxTotal();
    //
    //    if
    // (!(invoice.getCompany().getAddress().getState()).equals(invoice.getAddress().getState())) {
    //      BigDecimal igst = ((invoiceLine.getProduct().getGstRate()).multiply(exTaxTotal));
    //      productInformation.put("igst", igst);
    //      System.out.println("igst->" + igst);
    //    } else {
    //      BigDecimal sgst = ((invoiceLine.getProduct().getGstRate()).multiply(exTaxTotal));
    //      BigDecimal cgst = ((invoiceLine.getProduct().getGstRate()).multiply(exTaxTotal));
    //      productInformation.put("cgst", cgst);
    //      productInformation.put("sgst", sgst);
    //    }

    return productInformation;
  }
}
