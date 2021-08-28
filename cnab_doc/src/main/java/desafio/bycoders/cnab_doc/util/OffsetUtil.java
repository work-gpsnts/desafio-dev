package desafio.bycoders.cnab_doc.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import desafio.bycoders.cnab_doc.dto.DocumentCNABDto;
import desafio.bycoders.cnab_doc.model.DocumentoCNAB;

public class OffsetUtil {
	private static Tuple2<Integer, Integer> transformadorOffset(String campo) {
		Tuple2<Integer, Integer> tuple = null;
		
		try {
			Field campoSelecionado = DocumentoCNAB.class.getDeclaredField(campo);

			Offset offset = campoSelecionado.getAnnotation(Offset.class);

			if (offset != null) {
				tuple = new Tuple2<>(offset.from(), offset.to());
			}
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}

		return tuple;
	}

	public static Map<String, Tuple2<Integer, Integer>> offsetsPorCampo() {
		Field[] camposDocumento = DocumentoCNAB.class.getDeclaredFields();

		Map<String, Tuple2<Integer, Integer>> camposMapeados = new HashMap<>();

		for (Field campo : camposDocumento) {
			camposMapeados.put(campo.getName(), transformadorOffset(campo.getName()));
		}

		return camposMapeados;
	}
}
