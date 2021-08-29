package desafio.bycoders.cnab_doc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OperacaoSinal {
  OPERACAO_SOMA("+"),
  OPERACAO_SUBTRACAO("-");

  private final String digito;
}
