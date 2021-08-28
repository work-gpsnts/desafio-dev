package desafio.bycoders.cnab_doc.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import desafio.bycoders.cnab_doc.model.DocumentoCNAB;
import desafio.bycoders.cnab_doc.repository.CnabRepository;
import desafio.bycoders.cnab_doc.util.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import desafio.bycoders.cnab_doc.dto.DocumentCNABDto;
import desafio.bycoders.cnab_doc.util.OffsetUtil;
import desafio.bycoders.cnab_doc.util.Tuple2;

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

	public int tratadorDocumentoCnab(MultipartFile documento) {
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

		return documentosSalvos.size();
	}
}
