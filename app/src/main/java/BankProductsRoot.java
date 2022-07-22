import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class BankProductsRoot implements Root {

    private List<CreditProduct> productList;

    public List<CreditProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<CreditProduct> productList) {
        this.productList = productList;
    }
}
