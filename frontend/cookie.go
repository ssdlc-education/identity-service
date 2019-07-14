package main

import "net/http"

func convertCookieForResponse(cookie *http.Cookie) *http.Cookie {
	return &http.Cookie{Name: "V",
		Value:    cookie.Value,
		Path:     "/",
		HttpOnly: true}
}
