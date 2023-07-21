package janvdl.sales.service;

import janvdl.sales.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {

	void save(Product product);

	void delete(Product product);

	Optional<Product> get(Long id);

	List<Product> list();

}
