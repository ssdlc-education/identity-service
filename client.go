package main

import (
	"os"
	"bufio"
	"strings"
	"html/template"
	"io/ioutil"
	"log"
	"net/http"
	"regexp"
	//"time"
)

type Page struct {
	Body string
}

type Profile struct {
	Username string
	FirstName string
	LastName string
	CreateTs string
	UpdateTs string
	Email string
	Description string
}

func check(err error){
	if err != nil {
		log.Fatal(err)
	}
}

func readLines(path string) ([]string, error){
	file, err := os.Open(path)
	check(err)

	defer file.Close()

	var lines []string
	scanner := bufio.NewScanner(file)
	for scanner.Scan(){
		lines = append(lines, scanner.Text())
	}

	return lines, scanner.Err()
}

func (p *Profile) save() error {
	filename := "fakedb/" + p.Username + ".txt"
	values := []string{p.Username, p.FirstName, p.LastName, p.Email, p.CreateTs, p.UpdateTs, p.Description}
	buffer := []byte(strings.Join(values,"\n"))

	return ioutil.WriteFile(filename, buffer, 0600)
}

func loadProfile(w http.ResponseWriter, r *http.Request, title string) (*Profile, error) {
	filename := "fakedb/" + title + ".txt"

	_, err := os.Stat("filename")

	if os.IsNotExist(err) {
		//time.Sleep(1 * time.Second)
	    //http.Redirect(w, r, "/register/" , http.StatusFound)
	    return nil, err
	}

	lines, err := readLines(filename)
	check(err)
	
	username, firstName, lastName, email, createTs, updateTs, description := lines[0], lines[1], lines[2], lines[3], lines[4], lines[5], lines[6]

	return &Profile{Username: username, FirstName: firstName, LastName: lastName, CreateTs: createTs, UpdateTs:updateTs, Email:email, Description:description}, nil
}

func accountsHandler(w http.ResponseWriter, r *http.Request, title string) {
	p, err := loadProfile(w, r, title)

	check(err)

	renderTemplate(w, "accounts", p)
}

func editHandler(w http.ResponseWriter, r *http.Request, title string) {
	p, err := loadProfile(w, r, title)
	check(err)

	renderTemplate(w, "edit", p)
}

func saveHandler(w http.ResponseWriter, r *http.Request, title string) {
	username := r.FormValue("username")
	firstName := r.FormValue("firstName")
	lastName := r.FormValue("lastName")
	createTs := r.FormValue("createTs")
	updateTs := r.FormValue("updateTs")
	email := r.FormValue("email")
	description := r.FormValue("description")

	p := &Profile{Username: username, FirstName: firstName, LastName: lastName, CreateTs: createTs, UpdateTs:updateTs, Email: email, Description:description}
	err := p.save()

	check(err)

	http.Redirect(w, r, "/accounts/" + username, http.StatusFound)
}

func logHandler(w http.ResponseWriter, r *http.Request, title string) {
	http.ServeFile(w, r, "template/login.html")
}

func regHandler(w http.ResponseWriter, r *http.Request, title string) {
	http.ServeFile(w, r, "template/register.html")
}

var templates = template.Must(template.ParseFiles("template/edit.html", "template/accounts.html"))

func renderTemplate(w http.ResponseWriter, tmpl string, p *Profile) {
	err := templates.ExecuteTemplate(w, tmpl+".html", p)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}

var validPath = regexp.MustCompile("^(/(edit|save|accounts)/([a-zA-Z0-9]+))|(/(login|register)/)|(/)$")

func makeHandler(fn func(http.ResponseWriter, *http.Request, string)) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		m := validPath.FindStringSubmatch(r.URL.Path)
		if m == nil {
			//http.NotFound(w, r)
			//time.Sleep(1 * time.Second)
			http.Redirect(w, r, "/register/", http.StatusFound)
			return
		}
		fn(w, r, m[2])
	}
}

func main() {
	http.HandleFunc("/accounts/", makeHandler(accountsHandler))
	http.HandleFunc("/edit/", makeHandler(editHandler))
	http.HandleFunc("/save/", makeHandler(saveHandler))
	http.HandleFunc("/register/", makeHandler(regHandler))
	http.HandleFunc("/login/", makeHandler(logHandler))
	http.HandleFunc("/", makeHandler(regHandler))

	log.Fatal(http.ListenAndServe(":8080", nil))
}
