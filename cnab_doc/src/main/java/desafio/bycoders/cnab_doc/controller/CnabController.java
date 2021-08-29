package desafio.bycoders.cnab_doc.controller;

import desafio.bycoders.cnab_doc.output.RetornoCnab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import desafio.bycoders.cnab_doc.service.CnabService;

@RestController
@RequestMapping("/cnab")
public class CnabController {
	@Autowired CnabService cnabService;

	@GetMapping("/cadastrados")
	public ResponseEntity<?> retornaTodosCadastrados() {
		return ResponseEntity.ok(
			RetornoCnab.builder()
				.mensagem("Documentos salvos retornados com sucesso")
				.dados(cnabService.retornaDocumentosCadastrados())
				.build()
		);
	}

	@PostMapping("/envio")
	public ResponseEntity<?> envioDeArquivoCnab(@RequestPart MultipartFile documento) {
		return ResponseEntity.ok(
			RetornoCnab.builder()
				.mensagem("Documento registrado com sucesso")
				.dados(cnabService.tratadorDocumentoCnab(documento))
				.build()
		);
	}
}