// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package main

import (
	"log"
	"net/http"
	"text/template"
)

var templates = template.Must(template.ParseGlob("./template/*.html"))

func renderTemplate(w http.ResponseWriter, tmplName string, p interface{}) {
	err := templates.ExecuteTemplate(w, tmplName, p)
	if err != nil {
		log.Printf("failed to execute template: %s", err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}
