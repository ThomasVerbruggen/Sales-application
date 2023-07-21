package janvdl.sales.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAME", nullable = false, length = 10)
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "PRICE", nullable = false)
	private Double price = 0.0D;

	@Column(name = "ACTIVE", nullable = false)
	private Boolean active = true;

	@Column(name = "DISPLAY_ORDER", nullable = false)
	private Integer displayOrder = 1;

}
