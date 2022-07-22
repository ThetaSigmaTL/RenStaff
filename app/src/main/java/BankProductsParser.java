import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class BankProductsParser implements JsonParser{

    private static final String filename = "bank-products.json";
    private static final String TAG_RATES_ARRAY = "interestRates";
    private static final String TAG_NAME = "name";
    private static final String TAG_RATE = "rate";
    private static final String TAG_MIN_TERM = "minTerm";
    private static final String TAG_MAX_TERM = "maxTerm";

    @Override
    public Root parse() {

        BankProductsRoot bankProductsRoot = new BankProductsRoot();
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filename)) {
            JSONObject rootJsonObject = (JSONObject) jsonParser.parse(reader);
            // String name = (String) rootJsonObject.get(TAG_NAME);

            List<CreditProduct> productList = new ArrayList<>();
            JSONArray interestRates = (JSONArray) rootJsonObject.get(TAG_RATES_ARRAY);
            for (Object item : interestRates) {
                JSONObject rates = (JSONObject) item;
                String productName = (String) rates.get(TAG_NAME);
                Double rate = (Double) rates.get(TAG_RATE);
                long minTerm = (long) rates.get(TAG_MIN_TERM);
                long maxTerm = (long) rates.get(TAG_MAX_TERM);
                CreditProduct creditProduct = new CreditProduct(productName, rate, (int) minTerm, (int) maxTerm);
                productList.add(creditProduct);
            }
            bankProductsRoot.setProductList(productList);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return bankProductsRoot;
    }
}
