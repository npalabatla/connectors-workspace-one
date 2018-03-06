/*
 * Copyright © 2018 VMware, Inc. All Rights Reserved.
 * SPDX-License-Identifier: BSD-2-Clause
 */

package com.vmware.connectors.mock;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;


import java.io.IOException;
import java.net.URI;

public interface RequestHandler {
    ClientHttpResponse handle(ClientHttpRequest request) throws IOException;
}
