package desafio.bycoders.cnab_doc.model;

import java.io.Serializable;

import javax.persistence.*;

import desafio.bycoders.cnab_doc.util.Offset;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "documento_cnab")
public class DocumentoCNAB implements Serializable {
	private static final long serialVersionUID = 2009941643048304315L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 1)
	@Offset(from = 1, to = 1)
	private Integer tipo;
	
	@Column(length = 8)
	@Offset(from = 2, to = 9)
	private String data;
	
	@Column(length = 10)
	@Offset(from = 10, to = 19)
	private Double valor;
	
	@Column(length = 11)
	@Offset(from = 20, to = 30)
	private String cpf;
	
	@Column(length = 12)
	@Offset(from = 31, to = 42)
	private String cartao;
	
	@Column(length = 6)
	@Offset(from = 43, to = 48)
	private String hora;
	
	@Column(length = 14)
	@Offset(from = 49, to = 62)
	private String donoLoja;
	
	@Column(length = 19)
	@Offset(from = 63, to = 81)
	private String nomeLoja;
}
