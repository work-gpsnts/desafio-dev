package desafio.bycoders.cnab_doc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
  public static int indexOfValidValue(String regex, String target) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(target);

    if (matcher.find()) {
      return matcher.start();
    }

    return -1;
  }
}
