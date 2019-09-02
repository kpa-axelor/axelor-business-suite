package com.axelor.apps.gst.service;

import com.axelor.apps.account.db.FiscalPosition;
import com.axelor.apps.account.db.Tax;
import com.axelor.apps.account.db.TaxLine;
import com.axelor.apps.account.db.repo.TaxLineRepository;
import com.axelor.apps.account.db.repo.TaxRepository;
import com.axelor.apps.account.service.AccountManagementServiceAccountImpl;
import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.service.tax.FiscalPositionService;
import com.axelor.apps.base.service.tax.TaxService;
import com.axelor.exception.AxelorException;
import com.axelor.exception.db.repo.TraceBackRepository;
import com.google.inject.Inject;
import java.time.LocalDate;

public class AccountManagementServiceAccountImplGstImlp
    extends AccountManagementServiceAccountImpl {
  @Inject TaxRepository TaxRepository;
  @Inject TaxLineRepository TaxLineRepository;

  @Inject
  public AccountManagementServiceAccountImplGstImlp(
      FiscalPositionService fiscalPositionService, TaxService taxService) {
    super(fiscalPositionService, taxService);
  }

  TaxService taxService;
  TaxLine taxLine;
  Tax tax;

  @Override
  public TaxLine getTaxLine(
      LocalDate date,
      Product product,
      Company company,
      FiscalPosition fiscalPosition,
      boolean isPurchase)
      throws AxelorException {

    super.getTaxLine(date, product, company, fiscalPosition, isPurchase);
    System.out.println(product.getGstRate());
    tax =
        TaxRepository.all()
            .filter(
                "self.code = 'GST'  AND self.activeTaxLine != null AND self.activeTaxLine.value = ?",
                product.getGstRate())
            .fetchOne();
    System.out.println(tax);
    System.out.println(
        TaxRepository.all()
            .filter("self.code = 'GST' AND self.activeTaxLine.value = ?", product.getGstRate()));
    if (tax == null) {
      throw new AxelorException(TraceBackRepository.CATEGORY_NO_VALUE, "please fill taxLine");
    }
    return tax.getActiveTaxLine();
  }
}

// tax = (Tax) TaxRepository.all().filter("self.code = GST").fetch();
// tax = TaxRepository.findByCode("GST");
// System.out.println(tax.getId());
// taxLine = TaxLineRepository.find((long) 28);
// taxLine = (TaxLine) TaxLineRepository.all().filter("self.id = ? AND
// self.value =
// ?",tax.getId(),product.getGstRate()).fetchOne();
// System.out.println(taxLine.getValue());
