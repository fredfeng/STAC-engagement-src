/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

enum Tag {
    ABBREVIATION("ABBR"),
    ADDRESS("ADDR"),
    ALIAS("ALIA"),
    ASSOCIATION("ASSO"),
    AUTHORS("AUTH"),
    BLOB("BLOB"),
    CALL_NUMBER("CALN"),
    CAUSE("CAUS"),
    CHARACTER_SET("CHAR"),
    CITY("CITY"),
    CONCATENATION("CONC"),
    CONTINUATION("CONT"),
    CORPORATION("CORP"),
    COUNTRY("CTRY"),
    DATA_FOR_CITATION("DATA"),
    DATA_FOR_SOURCE("DATA"),
    DATE("DATE"),
    EMAIL("EMAIL"),
    FILE("FILE"),
    FORM("FORM"),
    HEADER("HEAD"),
    LANGUAGE("LANG"),
    LATITUDE("LATI"),
    LONGITUDE("LONG"),
    MAP("MAP"),
    MEDIA("MEDI"),
    NAME("NAME"),
    NOTE("NOTE"),
    OBJECT_MULTIMEDIA("OBJE"),
    PAGE("PAGE"),
    PHONE("PHON"),
    PLACE("PLAC"),
    POSTAL_CODE("POST"),
    RECORD_ID_NUMBER("RIN"),
    REFERENCE("REFN"),
    RELATIONSHIP("RELA"),
    REPOSITORY("REPO"),
    ROLE("ROLE"),
    SOURCE("SOUR"),
    STATE("STAE"),
    SUBMITTER("SUBM"),
    TEXT("TEXT"),
    TITLE("TITL"),
    TRAILER("TRLR"),
    TYPE("TYPE"),
    VERSION("VERS"),
    WEB_ADDRESS("WWW");
    
    final String tagText;

    private Tag(String tagText) {
        this.tagText = tagText.intern();
    }

    public boolean equalsText(String s) {
        return this.tagText.equals(s);
    }

    public String toString() {
        return this.tagText;
    }
}

