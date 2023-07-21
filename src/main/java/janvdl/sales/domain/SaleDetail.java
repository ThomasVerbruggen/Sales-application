package janvdl.sales.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SALE_DETAIL")
public class SaleDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;



	@ManyToOne
	@JoinColumn(name = "SALE_ID", nullable = false)
	private Sale sale;

	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	private Product product;

	@Column(name = "QUANTITY")
	private Double quantity;

	@Column(name = "UNIT_PRICE")
	private Double unitPrice;

	@Column(name = "AMOUNT")
	private Double amount;

	@Column(name = "DISPLAY_ORDER", nullable = false)
	private Integer displayOrder = 1;

	

}
