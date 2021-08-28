package desafio.bycoders.cnab_doc.controller;

import desafio.bycoders.cnab_doc.output.RetornoCnab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import desafio.bycoders.cnab_doc.service.CnabService;

@RestController
@RequestMapping("/cnab")
public class CnabController {
	@Autowired CnabService cnabService;

	@PostMapping("/send")
	public ResponseEntity<?> envioDeArquivoCnab(@RequestPart MultipartFile documento) {
		return ResponseEntity.ok(
			RetornoCnab.builder()
				.contagem(cnabService.tratadorDocumentoCnab(documento))
				.mensagem("Documento registrado com sucesso")
				.build()
		);
	}
}