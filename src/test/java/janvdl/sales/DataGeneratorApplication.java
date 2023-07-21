package janvdl.sales;

import janvdl.sales.domain.Product;
import janvdl.sales.domain.Sale;
import janvdl.sales.domain.SaleDetail;
import janvdl.sales.service.ProductService;
import janvdl.sales.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static java.time.Month.JULY;
import static java.util.stream.Collectors.toList;
import static org.springframework.boot.SpringApplication.run;

public class DataGeneratorApplication {

    private static final int COUNT = 100;
    private static final LocalDateTime START_OF_SALES_1 = LocalDateTime.of(2017, JULY, 10, 16, 30);
    private static final LocalDateTime START_OF_SALES_2 = LocalDateTime.of(2017, JULY, 11, 12, 00);
    private static final int MAX_DELAY = 240; // max sec between 2 sales
    private static final int MAX_PRODUCT = 5;

    private final Random random = new Random();

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleService saleService;

    public static void main(String[] args) {
        run(DataGeneratorApplication.class, args).close();
    }

    @Bean
    public CommandLineRunner generateData() {
        return (args) -> {
            generateDataImpl(START_OF_SALES_1);
            generateDataImpl(START_OF_SALES_2);
        };
    }

    private void generateDataImpl(LocalDateTime startOfSales) {
        LocalDateTime saleDateTime = startOfSales;
        for (int i = 0; i < COUNT; i++) {
            createSale(saleDateTime);
            saleDateTime = saleDateTime.plusSeconds(random.nextInt(MAX_DELAY));
        }
    }

    private void createSale(LocalDateTime saleDateTime) {
        Sale sale = new Sale();
        sale.setDate(saleDateTime);

        System.out.println("Saving sale for " + saleDateTime);

        saleService.save(sale, createDetails());
    }

    private List<SaleDetail> createDetails() {

        return productService.list()
                .stream()
                .filter(Product::getActive)
                .map(this::createDetail)
                .collect(toList());
    }

    private SaleDetail createDetail(Product product) {
        return new SaleDetail(null, null, product, 1.0D * random.nextInt(MAX_PRODUCT), product.getPrice(), 0.0D,
                product.getDisplayOrder());
    }

}
