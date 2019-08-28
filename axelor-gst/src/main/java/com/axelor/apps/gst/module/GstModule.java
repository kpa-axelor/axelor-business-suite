package com.axelor.apps.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.apps.account.service.AccountManagementServiceAccountImpl;
import com.axelor.apps.businessproject.service.InvoiceServiceProjectImpl;
import com.axelor.apps.gst.service.AccountManagementServiceAccountImplGstImlp;
import com.axelor.apps.gst.service.GstServiceImpl;
import com.axelor.apps.gst.service.InvoiceServiceImplGstInvoiceImpl;
import com.axelor.apps.supplychain.service.InvoiceLineSupplychainService;

public class GstModule extends AxelorModule {

  @Override
  protected void configure() {
    bind(InvoiceLineSupplychainService.class).to(GstServiceImpl.class);
    bind(AccountManagementServiceAccountImpl.class)
        .to(AccountManagementServiceAccountImplGstImlp.class);
    bind(InvoiceServiceProjectImpl.class).to(InvoiceServiceImplGstInvoiceImpl.class);
  }
}
