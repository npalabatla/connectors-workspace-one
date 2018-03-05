/*
 * Copyright © 2018 VMware, Inc. All Rights Reserved.
 * SPDX-License-Identifier: BSD-2-Clause
 */

package com.vmware.connectors.common.utils;

import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Flux;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public final class FluxUtil {
    public FluxUtil() {
        // util class
    }

    public static <R> Flux<R> skip404(Throwable throwable) {
        if (throwable instanceof HttpClientErrorException
                && HttpClientErrorException.class.cast(throwable).getStatusCode() == NOT_FOUND) {
            // If it's OK to request non-existent items, proceed; we just won't create a card.
            return Flux.empty();
        } else {
            // If the problem is not 404, let the problem bubble up
            return Flux.error(throwable);
        }
    }
}
