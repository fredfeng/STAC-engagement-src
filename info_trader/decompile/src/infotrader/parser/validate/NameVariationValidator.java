/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.validate;

import infotrader.parser.model.AbstractNameVariation;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.validate.AbstractValidator;
import infotrader.parser.validate.InfoTraderValidator;

class NameVariationValidator
extends AbstractValidator {
    protected AbstractNameVariation nv;

    public NameVariationValidator(InfoTraderValidator rootValidator, AbstractNameVariation nv) {
        this.rootValidator = rootValidator;
        this.nv = nv;
    }

    @Override
    protected void validate() {
        if (this.nv == null) {
            this.addError("Name variation is null and cannot be validated");
            return;
        }
        this.checkCustomTags(this.nv);
        this.checkRequiredString(this.nv.getVariation(), "variation on a personal name", (Object)this.nv);
        this.checkOptionalString(this.nv.getVariationType(), "type of variation on a personal name", (Object)this.nv);
    }
}

