package com.vmware.connectors.servicenow;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
class RequestedItem {

    private final String sysId;
    private final String requestSysId;
    private final String shortDescription;
    private final String price;
    private final String quantity;

    RequestedItem(
            String sysId,
            String requestSysId,
            String shortDescription,
            String price,
            String quantity
    ) {
        this.sysId = sysId;
        this.requestSysId = requestSysId;
        this.shortDescription = shortDescription;
        this.price = price;
        this.quantity = quantity;
    }

    String getSysId() {
        return sysId;
    }

    String getRequestSysId() {
        return requestSysId;
    }

    String getShortDescription() {
        return shortDescription;
    }

    String getPrice() {
        return price;
    }

    String getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return Pojomatic.toString(this);
    }

}
