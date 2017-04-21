/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.validate;

import infotrader.parser.validate.Severity;

public class InfoTraderValidationFinding {
    private Object itemWithProblem;
    private String problemDescription;
    private Severity severity;

    InfoTraderValidationFinding(String description, Severity severity, Object obj) {
        this.problemDescription = description;
        this.severity = severity;
        this.itemWithProblem = obj;
    }

    public Object getItemWithProblem() {
        return this.itemWithProblem;
    }

    public String getProblemDescription() {
        return this.problemDescription;
    }

    public Severity getSeverity() {
        return this.severity;
    }

    public void setItemWithProblem(Object itemWithProblem) {
        this.itemWithProblem = itemWithProblem;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append((Object)this.severity).append(": ").append(this.problemDescription);
        if (this.itemWithProblem != null) {
            sb.append(" (").append(this.itemWithProblem).append(")");
        }
        return sb.toString();
    }
}

