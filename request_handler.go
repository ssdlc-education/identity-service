



// Copyright 2010 The Go Authors. All rights reserved.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package main

import (
"bufio"
"html/template"
"io/ioutil"
"log"
"net/http"
"os"
"regexp"
"strings"
)

type Page struct {
	Username    string
	Firstname   string
	Lastname    string
	Email       string
	CreateTs    string
	UpdateTs    string
	Description string
}

func readLines(path string) ([]string, error) {
	file, err := os.Open(path)

	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()
	var lines []string
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		lines = append(lines, scanner.Text())
	}
	return lines, scanner.Err()
}

func (p *Page) save() error {
	filename := "fakedb/" + p.Username + ".txt"
	values := []string{p.Username, p.Firstname, p.Lastname, p.Email, p.CreateTs, p.UpdateTs, p.Description}
	buffer := []byte(strings.Join(values, "\n"))
	return ioutil.WriteFile(filename, buffer, 0600)
}

func loadPage(title string) (*Page, error) {
	filename := "fakedb/" + title + ".txt"
	lines, err := readLines(filename)
	if err != nil {
		log.Fatal(err)
	}
	username, firstname, lastname, email, createTs, updateTs, description := lines[0], lines[1], lines[2], lines[3], lines[4], lines[5], lines[6]
	return &Page{Username: username, Firstname: firstname, Lastname: lastname, Email: email, CreateTs: createTs, UpdateTs: updateTs, Description: description}, nil
}

func accountsHandler(w http.ResponseWriter, r *http.Request, title string) {
	p, err := loadPage(title)
	if err != nil {
		http.Redirect(w, r, "/accounts/oscarwilde", http.StatusFound)
		return
	}
	renderTemplate(w, "accounts", p)
}

func editHandler(w http.ResponseWriter, r *http.Request, title string) {
	p, err := loadPage(title)
	if err != nil {
		log.Fatal(err)
	}
	renderTemplate(w, "edit", p)
}

func saveHandler(w http.ResponseWriter, r *http.Request, title string) {
	username := r.FormValue("username")
	firstname := r.FormValue("firstname")
	lastname := r.FormValue("lastname")
	email := r.FormValue("email")
	createTs := r.FormValue("createTs")
	updateTs := r.FormValue("updateTs")
	description := r.FormValue("description")
	p := &Page{Username: username, Firstname: firstname, Lastname: lastname, Email: email, CreateTs: createTs, UpdateTs: updateTs, Description: description}
	err := p.save()
	if err != nil {
		log.Fatal(err)
	}
	http.Redirect(w, r, "/accounts/"+username, http.StatusFound)
}

var templates = template.Must(template.ParseFiles("template/edit.html", "template/accounts.html"))

func renderTemplate(w http.ResponseWriter, tmpl string, p *Page) {
	err := templates.ExecuteTemplate(w, tmpl+".html", p)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}

var validPath = regexp.MustCompile("^/(edit|save|accounts)/([a-zA-Z0-9]+)$")

func makeHandler(fn func(http.ResponseWriter, *http.Request, string)) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		m := validPath.FindStringSubmatch(r.URL.Path)
		if m == nil {
			http.NotFound(w, r)
			return
		}
		fn(w, r, m[2])
	}
}

func main() {
	http.HandleFunc("/accounts/", makeHandler(accountsHandler))
	http.HandleFunc("/edit/", makeHandler(editHandler))
	http.HandleFunc("/save/", makeHandler(saveHandler))
	log.Fatal(http.ListenAndServe(":5000", nil))
}

