package desafio.bycoders.cnab_doc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum TipoOperacao {
  TIPO_INVALIDO(-1, "Inválido", null, null),
  TIPO_1(1,"Débito", NaturezaOperacao.ENTRADA, OperacaoSinal.OPERACAO_SOMA),
  TIPO_2(2,"Boleto", NaturezaOperacao.SAIDA, OperacaoSinal.OPERACAO_SUBTRACAO),
  TIPO_3(3,"Financimento", NaturezaOperacao.SAIDA, OperacaoSinal.OPERACAO_SUBTRACAO),
  TIPO_4(4,"Crédito", NaturezaOperacao.ENTRADA, OperacaoSinal.OPERACAO_SOMA),
  TIPO_5(5,"Recebimento Empréstimo", NaturezaOperacao.ENTRADA, OperacaoSinal.OPERACAO_SOMA),
  TIPO_6(6,"Vendas", NaturezaOperacao.ENTRADA, OperacaoSinal.OPERACAO_SOMA),
  TIPO_7(7,"Recebimento TED", NaturezaOperacao.ENTRADA, OperacaoSinal.OPERACAO_SOMA),
  TIPO_8(8,"Recebimento DOC", NaturezaOperacao.ENTRADA, OperacaoSinal.OPERACAO_SOMA),
  TIPO_9(9,"Aluguel", NaturezaOperacao.SAIDA, OperacaoSinal.OPERACAO_SUBTRACAO);

  private final int num;
  private final String descricao;
  private final NaturezaOperacao natureza;
  private final OperacaoSinal operacao;

  public static TipoOperacao mapeiaTipoParaTipoOperacao(int tipo) {
    Supplier<Stream<TipoOperacao>> operacao = () -> Stream.of(TipoOperacao.values());

    if (operacao.get().anyMatch(el -> el.getNum() == tipo)) {
      Optional<TipoOperacao> resultado = operacao.get().filter(el -> el.getNum() == tipo).findFirst();

      if (resultado.isPresent()) {
        return resultado.get();
      }
    }

    return TipoOperacao.TIPO_INVALIDO;
  }
}
