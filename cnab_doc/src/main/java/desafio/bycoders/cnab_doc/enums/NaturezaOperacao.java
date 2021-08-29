package desafio.bycoders.cnab_doc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NaturezaOperacao {
  ENTRADA("Entrada"),
  SAIDA("Saída");

  private final String descricaoNatureza;
}
