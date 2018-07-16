package uk.co.senapt.desktop.shell;

/**
 * Created by Gdiaz on 26/2/2018.
 */
public final class IntegerNumberField extends NumberField<Long> {

    public IntegerNumberField () {
        super(false);
    }

    @Override
    protected void setValueFromString(String text) {
        try {
            updatingFromText = true;
            Long val = Long.valueOf(text);
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
