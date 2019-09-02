package com.axelor.apps.gst.web;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.exception.AxelorException;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;
import java.math.BigDecimal;

public class InvoiceLineController extends com.axelor.apps.account.web.InvoiceLineController {
  @Override
  public void computeAnalyticDistribution(ActionRequest request, ActionResponse response)
      throws AxelorException {
    super.computeAnalyticDistribution(request, response);
    System.out.println("In invoiceLine controller");

    Context context = request.getContext();
    InvoiceLine invoiceLine = context.asType(InvoiceLine.class);
    Invoice invoice = request.getContext().getParent().asType(Invoice.class);
    System.out.println(invoiceLine.getProduct().getGstRate());

    BigDecimal exTaxTotal = invoiceLine.getExTaxTotal();

    if (!(invoice.getCompany().getAddress().getState()).equals(invoice.getAddress().getState())) {
      BigDecimal igst = ((invoiceLine.getProduct().getGstRate()).multiply(exTaxTotal));
      response.setValue("igst", igst);
      System.out.println("igst->" + igst);
    } else {
      BigDecimal sgst = ((invoiceLine.getProduct().getGstRate()).multiply(exTaxTotal));
      BigDecimal cgst = ((invoiceLine.getProduct().getGstRate()).multiply(exTaxTotal));
      response.setValue("cgst", cgst);
      response.setValue("sgst", sgst);

      //      Context context1 = new Context(Invoice.class);
      //      response.setValues(new HashMap<String, Object>());
    }
  }
}
