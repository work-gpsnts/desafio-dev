package desafio.bycoders.cnab_doc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DocumentCNABDto {
	private Integer tipo;
	private String data;
	private Double valor;
	private String cpf;
	private String cartao;
	private String hora;
	private String donoLoja;
	private String nomeLoja;
}
