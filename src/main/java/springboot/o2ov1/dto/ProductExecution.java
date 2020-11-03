package springboot.o2ov1.dto;


import springboot.o2ov1.entity.Product;
import springboot.o2ov1.enums.ProductEnum;

import java.util.List;

public class ProductExecution {
    private int state;
    private String stateInfo;
    private int count;
    // 增删改所用到的实体
    private Product product;
    private List<Product> products;

    public ProductExecution(){}
    public ProductExecution(ProductEnum productEnum) {
        this.state = productEnum.getState();
        this.stateInfo = productEnum.getStateInfo();
    }
    public ProductExecution(ProductEnum productEnum, Product product) {
        this.state = productEnum.getState();
        this.stateInfo = productEnum.getStateInfo();
        this.product = product;
    }
    public ProductExecution(ProductEnum productEnum, List<Product> products) {
        this.state = productEnum.getState();
        this.stateInfo = productEnum.getStateInfo();
        this.products = products;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
