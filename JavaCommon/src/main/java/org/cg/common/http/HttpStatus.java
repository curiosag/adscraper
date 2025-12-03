package org.cg.common.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum HttpStatus {

	SC_ACCEPTED, SC_BAD_GATEWAY, SC_BAD_REQUEST, SC_CONFLICT, SC_CONTINUE, SC_CREATED, SC_EXPECTATION_FAILED, SC_FAILED_DEPENDENCY, SC_FORBIDDEN, SC_GATEWAY_TIMEOUT, SC_GONE, SC_HTTP_VERSION_NOT_SUPPORTED, SC_INSUFFICIENT_SPACE_ON_RESOURCE, SC_INSUFFICIENT_STORAGE, SC_INTERNAL_SERVER_ERROR, SC_LENGTH_REQUIRED, SC_LOCKED, SC_METHOD_FAILURE, SC_METHOD_NOT_ALLOWED, SC_MOVED_PERMANENTLY, SC_MOVED_TEMPORARILY, SC_MULTIPLE_CHOICES, SC_MULTI_STATUS, SC_NO_CONTENT, SC_NON_AUTHORITATIVE_INFORMATION, SC_NOT_ACCEPTABLE, SC_NOT_FOUND, SC_NOT_IMPLEMENTED, SC_NOT_MODIFIED, SC_OK, SC_PARTIAL_CONTENT, SC_PAYMENT_REQUIRED, SC_PRECONDITION_FAILED, SC_PROCESSING, SC_PROXY_AUTHENTICATION_REQUIRED, SC_REQUESTED_RANGE_NOT_SATISFIABLE, SC_REQUEST_TIMEOUT, SC_REQUEST_TOO_LONG, SC_REQUEST_URI_TOO_LONG, SC_RESET_CONTENT, SC_SEE_OTHER, SC_SERVICE_UNAVAILABLE, SC_SWITCHING_PROTOCOLS, SC_TEMPORARY_REDIRECT, SC_UNAUTHORIZED, SC_UNPROCESSABLE_ENTITY, SC_UNSUPPORTED_MEDIA_TYPE, SC_USE_PROXY;

	static{
		createMap();
	}

	private final static Map<Integer, HttpStatus> statusMap = createMap();

	private static Map<Integer, HttpStatus> createMap() {
		final Map<Integer, HttpStatus> entries = new HashMap<Integer, HttpStatus>();
		
	    put(entries, 202, SC_ACCEPTED);
	    put(entries, 502, SC_BAD_GATEWAY);
	    put(entries, 400, SC_BAD_REQUEST);
	    put(entries, 409, SC_CONFLICT);
	    put(entries, 100, SC_CONTINUE);
	    put(entries, 201, SC_CREATED);
	    put(entries, 417, SC_EXPECTATION_FAILED);
	    put(entries, 424, SC_FAILED_DEPENDENCY);
	    put(entries, 403, SC_FORBIDDEN);
	    put(entries, 504, SC_GATEWAY_TIMEOUT);
	    put(entries, 410, SC_GONE);
	    put(entries, 505, SC_HTTP_VERSION_NOT_SUPPORTED);
	    put(entries, 419, SC_INSUFFICIENT_SPACE_ON_RESOURCE);
	    put(entries, 507, SC_INSUFFICIENT_STORAGE);
	    put(entries, 500, SC_INTERNAL_SERVER_ERROR);
	    put(entries, 411, SC_LENGTH_REQUIRED);
	    put(entries, 423, SC_LOCKED);
	    put(entries, 420, SC_METHOD_FAILURE);
	    put(entries, 405, SC_METHOD_NOT_ALLOWED);
	    put(entries, 301, SC_MOVED_PERMANENTLY);
	    put(entries, 302, SC_MOVED_TEMPORARILY);
	    put(entries, 300, SC_MULTIPLE_CHOICES);
	    put(entries, 207, SC_MULTI_STATUS);
	    put(entries, 204, SC_NO_CONTENT);
	    put(entries, 203, SC_NON_AUTHORITATIVE_INFORMATION);
	    put(entries, 406, SC_NOT_ACCEPTABLE);
	    put(entries, 404, SC_NOT_FOUND);
	    put(entries, 501, SC_NOT_IMPLEMENTED);
	    put(entries, 304, SC_NOT_MODIFIED);
	    put(entries, 200, SC_OK);
	    put(entries, 206, SC_PARTIAL_CONTENT);
	    put(entries, 402, SC_PAYMENT_REQUIRED);
	    put(entries, 412, SC_PRECONDITION_FAILED);
	    put(entries, 102, SC_PROCESSING);
	    put(entries, 407, SC_PROXY_AUTHENTICATION_REQUIRED);
	    put(entries, 416, SC_REQUESTED_RANGE_NOT_SATISFIABLE);
	    put(entries, 408, SC_REQUEST_TIMEOUT);
	    put(entries, 413, SC_REQUEST_TOO_LONG);
	    put(entries, 414, SC_REQUEST_URI_TOO_LONG);
	    put(entries, 205, SC_RESET_CONTENT);
	    put(entries, 303, SC_SEE_OTHER);
	    put(entries, 503, SC_SERVICE_UNAVAILABLE);
	    put(entries, 101, SC_SWITCHING_PROTOCOLS);
	    put(entries, 307, SC_TEMPORARY_REDIRECT);
	    put(entries, 401, SC_UNAUTHORIZED);
	    put(entries, 422, SC_UNPROCESSABLE_ENTITY);
	    put(entries, 415, SC_UNSUPPORTED_MEDIA_TYPE);
	    put(entries, 305, SC_USE_PROXY);
	    
		return entries;
	}

	private static void put(Map<Integer, HttpStatus> entries, Integer code, HttpStatus status) {
		entries.put(code, status);
	}

	public static Optional<HttpStatus> decode(int statusCode) {
		return Optional.ofNullable(statusMap.get(statusCode));
	}

	public static Optional<Integer> encode(HttpStatus httpStatus) {
		return statusMap.entrySet().stream().filter(e -> e.getValue().equals(httpStatus)).map(e -> e.getKey()).findFirst();
	}
}
