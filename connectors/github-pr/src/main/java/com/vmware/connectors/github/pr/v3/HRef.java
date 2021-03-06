/*
 * Copyright © 2018 VMware, Inc. All Rights Reserved.
 * SPDX-License-Identifier: BSD-2-Clause
 */

package com.vmware.connectors.github.pr.v3;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
@SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
public class HRef {

    @JsonProperty("href")
    private String href;


    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }


    @Override
    public String toString() {
        return Pojomatic.toString(this);
    }

}
