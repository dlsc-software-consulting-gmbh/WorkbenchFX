package uk.co.senapt.desktop.shell;

/**
 * Created by Admin on 26/2/2018.
 */
public class DecimalNumberField extends NumberField<Double> {

    public DecimalNumberField () {
        super(true);
    }

    @Override
    protected void setValueFromString(String text) {
        try {
            updatingFromText = true;
            Double val = Double.valueOf(text);
            setValue(val);
        }
        catch (Exception e) {
            if (text == null || text.isEmpty()) {
                setValue(null);
            }
        }
        finally {
            updatingFromText = false;
        }
    }

}
