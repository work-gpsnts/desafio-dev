package desafio.bycoders.cnab_doc.output;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RetornoCnab {
  private final int contagem;
  private final String mensagem;
}
