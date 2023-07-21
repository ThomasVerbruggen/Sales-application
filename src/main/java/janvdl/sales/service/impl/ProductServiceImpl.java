package janvdl.sales.service.impl;

import janvdl.sales.database.repository.ProductRepository;
import janvdl.sales.domain.Product;
import janvdl.sales.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository repository;

	@Override
	public void save(Product product) {
		repository.save(product);

	}

	@Override
	public void delete(Product product) {
		repository.delete(product);

	}

	@Override
	public List<Product> list() {
		return repository.findAllByOrderByDisplayOrderAsc();
	}

	@Override
	public Optional<Product> get(Long id) {
		return repository.findById(id);
	}

}
