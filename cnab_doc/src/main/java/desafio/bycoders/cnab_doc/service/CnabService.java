package desafio.bycoders.cnab_doc.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import desafio.bycoders.cnab_doc.enums.OperacaoSinal;
import desafio.bycoders.cnab_doc.enums.TipoOperacao;
import desafio.bycoders.cnab_doc.model.DocumentoCNAB;
import desafio.bycoders.cnab_doc.repository.CnabRepository;
import desafio.bycoders.cnab_doc.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import desafio.bycoders.cnab_doc.util.OffsetUtil;
import desafio.bycoders.cnab_doc.util.Tuple2;

import static java.util.Map.entry;

@Service
public class CnabService {
	@Autowired
	CnabRepository repository;

	private DocumentoCNAB transformadorLinhaDto(String linha, Map<String, Tuple2<Integer, Integer>> limitadores) {
		Map<String, String> results = limitadores.entrySet()
				.stream()
				.collect(Collectors.toMap(
					Map.Entry::getKey,
					v -> linha.substring(v.getValue().getLeft() - 1, v.getValue().getRight() > linha.length()
						? linha.length()
						: v.getValue().getRight())
				));

		double valorNormalizado = Double.parseDouble(
			results.get("valor").substring(StringUtil.indexOfValidValue("[1-9]", results.get("valor")))
		);

		return DocumentoCNAB.builder()
				.tipo(Integer.parseInt(results.get("tipo")))
				.data(results.get("data"))
				.valor(valorNormalizado / 100.00)
				.cpf(results.get("cpf"))
				.cartao(results.get("cartao"))
				.hora(results.get("hora"))
				.donoLoja(results.get("donoLoja").stripTrailing())
				.nomeLoja(results.get("nomeLoja").stripTrailing())
				.build();
	}

	private String[] transformadorDocumentoString(MultipartFile documento) {
		String text = null;

		try {
			InputStream istream = new ByteArrayInputStream(documento.getBytes());
			text = new String(istream.readAllBytes(), StandardCharsets.UTF_8);
			istream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Objects.requireNonNull(text).split("\n");
	}

	private Map<String, Object> processaRetorno(List<DocumentoCNAB> documentosSalvos) {
		Map<String, List<DocumentoCNAB>> agrupamentoLojaOperacoesTotal = documentosSalvos.stream()
				.collect(Collectors.groupingBy(DocumentoCNAB::getNomeLoja));

		Map<String, Object> retornoOperacao = new HashMap<>();

		agrupamentoLojaOperacoesTotal.forEach((key, value) -> {
			double total = value.stream()
					.map(el -> TipoOperacao.mapeiaTipoParaTipoOperacao(el.getTipo()).getOperacao() == OperacaoSinal.OPERACAO_SOMA
							? el.getValor()
							: el.getValor() * -1
					)
					.reduce(0.0, Double::sum);

			List<String> operacoes = value.stream().map(el -> {
				TipoOperacao tipo = TipoOperacao.mapeiaTipoParaTipoOperacao(el.getTipo());
				String data = el.getData(), hora = el.getHora();

				return String.format(
						"Operação de %s com %s de R$%s%.2f na data %s %s",
						tipo.getDescricao(),
						tipo.getNatureza().getDescricaoNatureza(),
						tipo.getOperacao().getDigito(),
						el.getValor(),
						String.format("%s/%s/%s", data.substring(6), data.substring(4, 6), data.substring(0, 4)),
						String.format("%s:%s:%s", hora.substring(0, 2), hora.substring(2, 4), hora.substring(4))
				);
			}).collect(Collectors.toList());

			retornoOperacao.put(key, Map.ofEntries(
					entry("operacoes", operacoes),
					entry("total",  Math.floor(total * 100) / 100)) // "Truncade" no valor do Double
			);
		});

		return retornoOperacao;
	}

	public Map<String, Object> tratadorDocumentoCnab(MultipartFile documento) {
		Map<String, Tuple2<Integer, Integer>> limitadores =  OffsetUtil.offsetsPorCampo()
				.entrySet()
				.stream()
				.filter(x -> x.getValue() != null)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		String[] linhasDocumento = transformadorDocumentoString(documento);
		List<DocumentoCNAB> documentosSalvos = new ArrayList<>();

		
		for (String linha : linhasDocumento) {
			documentosSalvos.add(transformadorLinhaDto(linha, limitadores));
		}

		repository.saveAll(documentosSalvos);

		return processaRetorno(documentosSalvos);
	}

	public Map<String, Object> retornaDocumentosCadastrados() {
		List<DocumentoCNAB> docsRegistrados = StreamSupport.stream(
			repository.findAll().spliterator(), false).collect(Collectors.toList()
		);

		return processaRetorno(docsRegistrados);
	}
}
