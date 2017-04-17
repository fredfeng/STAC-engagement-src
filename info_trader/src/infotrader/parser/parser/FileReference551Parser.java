/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import infotrader.parser.model.AbstractElement;
import infotrader.parser.model.FileReference;
import infotrader.parser.model.StringTree;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.parser.AbstractParser;
import infotrader.parser.parser.InfoTraderParser;
import infotrader.parser.parser.Tag;
import java.util.List;

class FileReference551Parser
extends AbstractParser<FileReference> {
    FileReference551Parser(InfoTraderParser gedcomParser, StringTree stringTree, FileReference loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    @Override
    void parse() {
        ((FileReference)this.loadInto).setReferenceToFile(new StringWithCustomTags(this.stringTree));
        List<StringTree> fileChildren = this.stringTree.getChildren();
        if (fileChildren != null) {
            for (StringTree fileChild : fileChildren) {
                if (Tag.FORM.equalsText(fileChild.getTag())) {
                    this.loadForm(fileChild);
                    continue;
                }
                if (Tag.TITLE.equalsText(fileChild.getTag())) {
                    ((FileReference)this.loadInto).setTitle(new StringWithCustomTags(fileChild));
                    continue;
                }
                this.unknownTag(fileChild, (AbstractElement)this.loadInto);
            }
        }
        if (((FileReference)this.loadInto).getFormat() == null) {
            this.addWarning("FORM tag not found under FILE reference on line " + this.stringTree.getParent().getLineNum() + " - technically required by spec");
        }
    }

    private void loadForm(StringTree form) {
        ((FileReference)this.loadInto).setFormat(new StringWithCustomTags(form.getValue()));
        List<StringTree> formChildren = form.getChildren();
        if (formChildren != null) {
            int typeCount = 0;
            for (StringTree formChild : formChildren) {
                if (Tag.TYPE.equalsText(formChild.getTag())) {
                    ((FileReference)this.loadInto).setMediaType(new StringWithCustomTags(formChild));
                    ++typeCount;
                    continue;
                }
                this.unknownTag(formChild, (AbstractElement)this.loadInto);
            }
            if (typeCount > 1) {
                this.addError("TYPE was specified more than once for the FORM tag on line " + form.getLineNum());
            }
        }
    }
}

