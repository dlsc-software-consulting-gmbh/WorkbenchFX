package uk.co.senapt.desktop.shell.model;

import java.time.LocalDate;

public class LocalDateRange {

    private LocalDate start;
    private LocalDate end;

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    @Override
    public String toString() {
        if (end == null) {
            if (start == null) {
                return "";
            }

            return start + " - ";
        }

        if (start == null) {
            return " - " + end;
        }

        return start + " - " + end;
    }
}
