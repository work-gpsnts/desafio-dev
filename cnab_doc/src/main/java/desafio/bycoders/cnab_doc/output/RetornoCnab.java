package desafio.bycoders.cnab_doc.output;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RetornoCnab<T> {
  private final String mensagem;
  private final T dados;
}
