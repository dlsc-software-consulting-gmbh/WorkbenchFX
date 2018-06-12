package uk.co.senapt.desktop.shell;

import javafx.scene.control.TextField;

public class RegexTextField extends TextField {

    private String regex;

    public RegexTextField() {
        this.regex = ".*";
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
    
    //----------------------------------------------------------------
    // Bei Ã„nderung des Inhalts des TextField Ã¼ber GUI wird Ã¼berprÃ¼ft
    // ACHTUNG: Bei Aufruf der setText()-Methode wird NICHT Ã¼berprÃ¼ft!
    //----------------------------------------------------------------
    @Override
    public void replaceText(int start, int end, String text) {
        // alten Text und Cursorposition merken
        String alterText = this.getText();
        int alteCursorPosition = this.getCaretPosition();

        // neuen Text zuweisen
        super.replaceText(start, end, text);

        // Ã¼berprÃ¼fen und evtl. zurÃ¼cksetzen
        ueberpruefen(alterText, alteCursorPosition);
    }

    @Override
    public void replaceSelection(String replacement) {
        // alten Text und Cursorposition merken
        String alterText = this.getText();
        int alteCursorPosition = this.getCaretPosition();

        // neuen Text zuweisen
        super.replaceSelection(replacement);

        // Ã¼berprÃ¼fen und evtl. zurÃ¼cksetzen
        ueberpruefen(alterText, alteCursorPosition);
    }

    private void ueberpruefen(String alterText, int alteCursorPosition) {
        String neuerText = this.getText();
        if (!neuerText.matches(regex)) {
            // alten Text und alte Cursorpoition wiederherstellen
            this.setText(alterText);
            this.positionCaret(alteCursorPosition);
        }
    }
}