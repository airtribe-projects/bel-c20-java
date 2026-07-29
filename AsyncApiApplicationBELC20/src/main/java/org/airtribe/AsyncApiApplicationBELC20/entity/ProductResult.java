package org.airtribe.AsyncApiApplicationBELC20.entity;

import java.util.List;


public class ProductResult {
  private List<ProductSubResult> products;

  public ProductResult(List<ProductSubResult> products) {
    this.products = products;
  }

  public List<ProductSubResult> getProducts() {
    return products;
  }

  public void setProducts(List<ProductSubResult> products) {
    this.products = products;
  }
}
