import java.util.List;

public class BankProductsRoot {

    private String name;
    private List<CreditProduct> productList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CreditProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<CreditProduct> productList) {
        this.productList = productList;
    }
}
