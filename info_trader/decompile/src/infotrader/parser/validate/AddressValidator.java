/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.validate;

import infotrader.parser.model.Address;
import infotrader.parser.model.StringWithCustomTags;
import infotrader.parser.validate.AbstractValidator;
import infotrader.parser.validate.InfoTraderValidator;
import java.util.List;

class AddressValidator
extends AbstractValidator {
    private final Address address;

    public AddressValidator(InfoTraderValidator rootValidator, Address address) {
        this.rootValidator = rootValidator;
        this.address = address;
    }

    @Override
    protected void validate() {
        this.checkStringList(this.address.getLines(), "address lines", false);
        this.checkOptionalString(this.address.getAddr1(), "line 1", (Object)this.address);
        this.checkOptionalString(this.address.getAddr2(), "line 2", (Object)this.address);
        this.checkOptionalString(this.address.getCity(), "city", (Object)this.address);
        this.checkOptionalString(this.address.getStateProvince(), "state/province", (Object)this.address);
        this.checkOptionalString(this.address.getPostalCode(), "postal code", (Object)this.address);
        this.checkOptionalString(this.address.getCountry(), "country", (Object)this.address);
    }
}

