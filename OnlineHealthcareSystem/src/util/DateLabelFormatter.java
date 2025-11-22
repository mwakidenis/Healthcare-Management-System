package ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JFormattedTextField.AbstractFormatter;

public class DateLabelFormatter extends AbstractFormatter {

    private final String pattern = "yyyy-MM-dd";
    private final SimpleDateFormat sdf = new SimpleDateFormat(pattern);

    @Override
    public Object stringToValue(String text) throws ParseException {
        return sdf.parse(text);
    }

    @Override
    public String valueToString(Object value) {
        if (value != null) {
            return sdf.format(value);
        }
        return "";
    }
}
