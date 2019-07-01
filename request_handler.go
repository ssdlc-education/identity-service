// Copyright 2010 The Go Authors. All rights reserved.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package main

import (
	"encoding/json"
	"fmt"
	"html/template"
	"io/ioutil"
	"log"
	"net/http"
	"regexp"
	"strings"
)

type Profile struct {
	Username    string `json:"username"`
	Firstname   string `json:"firstName"`
	Lastname    string `json:"lastName"`
	Email       string `json:"email"`
	Description string `json:"description"`
	Password    string `json:"password"`
	Verified    string `json:"verified"`
}
// Struct to contain page information

type Response struct {
	Code    int    `json:"code"`
	Type    string `json:"type"`
	Message string `json:"message"`
}

// Struct to store http response

type UpdateInfo struct {
	Username    string `json:"username"`
	Email       string `json:"email"`
	Description string `json:"description"`
}

// Struct to store updated information

type PublicInfo struct {
	Username    string `json:"username"`
	Description string `json:"description"`
}

// Struct to store public page information

type LogInfo struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

// Struct to store login information
var token = ""

// initialize the token with an empty string at first

// Save the edited information to backend
func (p *Profile) saveToBackend() error {
	link := "http://localhost:8080/v1/accounts/@me?token=" + token
	client := &http.Client{}
	updateInfo := UpdateInfo{}
	updateInfo.Username = p.Username
	updateInfo.Email = p.Email
	updateInfo.Description = p.Description
	jsonData, err := json.Marshal(updateInfo)
	if err != nil {
		log.Println(err)
	}
	Data := string(jsonData)
	// Encode the information to byte payload
	payload := strings.NewReader(Data)
	request, err := http.NewRequest("PUT", link, payload)
	request.Header.Add("Content-Type", "application/json")
	_, err = client.Do(request)
	client.CloseIdleConnections()
	if err != nil {
		log.Println(err)
	}

	return err
}

// Preload the information by fetching data from backend
func readFromBackend(userid string) (*Profile, error) {
	apiUrl := "http://localhost:8080/v1/accounts/@me?token=" + token
	res, err := http.Get(apiUrl)
	if err != nil {
		log.Println(err)
	}
	defer res.Body.Close()
	bodyBytes, err := ioutil.ReadAll(res.Body)
	//var responseInfo Response
	var pageInfo Profile
	err = json.Unmarshal(bodyBytes, &pageInfo)
	if err != nil {
		log.Println(err)
	}
	// Extract the data from http response and generate data to render backend pages
	//data := responseInfo.Message
	//
	//err = json.Unmarshal([]byte(data), &pageInfo)
	//if err != nil {
	//	log.Fatal(err)
	//}
	return &pageInfo, err
}

//Function to read public information without login and render the web pages
func readFromPublic(username string) (*PublicInfo, error) {
	link := "http://localhost:8080/v1/accounts/" + username
	resp, err := http.Get(link)
	if err != nil {
		log.Fatal(err)
	}
	defer resp.Body.Close()
	bodyBytes, err := ioutil.ReadAll(resp.Body)
	var pageData PublicInfo
	err = json.Unmarshal(bodyBytes, &pageData)
	if err != nil {
		log.Println(err)
	}
	return &pageData, err
}

func accountsHandler(w http.ResponseWriter, r *http.Request, title string) {
	p, err := readFromPublic(title)
	if err != nil {
		log.Println(err)
	}
	page := Profile{}
	page.Username = p.Username
	page.Description = p.Description
	renderTemplate(w, "public_profile", &page)
}

// Render the edit information page

func editHandler(w http.ResponseWriter, r *http.Request, title string) {
	p, err := readFromBackend(title)
	if err != nil {
		log.Println(err)
	}
	renderTemplate(w, "edit", p)
}

// The Function to save the edited information

func saveHandler(w http.ResponseWriter, r *http.Request, title string) {
	email := r.FormValue("email")
	description := r.FormValue("description")
	originalPage, err := readFromBackend(title)
	if err != nil {
		log.Println(err)
	}
	// Change the information from preloaded information
	originalPage.Email = email
	originalPage.Description = description
	err = originalPage.saveToBackend()
	if err != nil {
		log.Println(err)
	}
	// After edited it redirect to the private information page
	http.Redirect(w, r, "/privatePage/", http.StatusFound)
}

var templates = template.Must(template.ParseFiles("template/edit.html", "template/accounts.html", "template/register.html", "template/login.html", "template/public_profile.html", "template/profile.html"))

func renderTemplate(w http.ResponseWriter, tmpl string, p *Profile) {
	err := templates.ExecuteTemplate(w, tmpl+".html", p)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}
// Regular expression to avoid illegal request
var validPath = regexp.MustCompile("^(/(edit|save|accounts|home)/([a-zA-Z0-9]+))|(/(login|home|create|privatePage|register)/)$")

func makeHandler(fn func(http.ResponseWriter, *http.Request, string)) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		m := validPath.FindStringSubmatch(r.URL.Path)
		if m == nil {
			http.NotFound(w, r)
			return
		}
		fn(w, r, m[3])
	}
}

func createHandler(w http.ResponseWriter, r *http.Request, title string){
	var pageinfo Profile
	pageinfo.Username = r.FormValue("username")
	pageinfo.Firstname = r.FormValue("firstname")
	pageinfo.Lastname = r.FormValue("lastname")
	pageinfo.Description = r.FormValue("description")
	pageinfo.Password = r.FormValue("password")
	pageinfo.Email = r.FormValue("email")
	pageinfo.Verified = "true"
	newUser, err := json.Marshal(pageinfo)
	if err != nil {
		log.Println(err)
	}
	Data := string(newUser)
	payload := strings.NewReader(Data)
	_, err = http.Post("http://localhost:8080/v1/accounts/", "application/json", payload)
	if err != nil {
		log.Println(err)
	}
	err = login(w, pageinfo.Username, pageinfo.Password)
	if err != nil {
		log.Println(err)
	}
	http.Redirect(w, r, "/privatePage/", http.StatusFound)
}

// Handle the login page

func homeHandler(w http.ResponseWriter, r *http.Request, title string) {
	p := Profile{}
	renderTemplate(w, "login", &p)
}

// Do the login job and set the token

func login(w http.ResponseWriter, username string, password string) error {
	user := LogInfo{}
	user.Username = username
	user.Password = password
	userData, err := json.Marshal(user)
	userString := string(userData)
	payload := strings.NewReader(userString)
	response, err := http.Post("http://localhost:8080/v1/sessions/", "application/json", payload)
	if err != nil {
		log.Println(err)
	}
	// Get token from login response from backend
	token = response.Header.Get("Set-Cookie")
	Cookie := http.Cookie{Name:user.Username,
		Value: token,
		HttpOnly:true}
	http.SetCookie(w, &Cookie)
	//fmt.Println(token)
	return err
}

func loginHandler(w http.ResponseWriter, r *http.Request, title string) {
	// Get user login in information
	username := r.FormValue("username")
	password := r.FormValue("password")
	//User information struct
	err := login(w,username, password)
	if err != nil {
		log.Println(err)
	}
	cookie,_ := r.Cookie(username)
	//fmt.Println(r.Cookie("Alice"))
	//fmt.Println(cookie)
	// After login, redirect to private page
	http.SetCookie(w, cookie)
	http.Redirect(w, r, "/privatePage/", http.StatusFound)
}

func registerHandler(w http.ResponseWriter, r *http.Request, title string) {
	p := Profile{}
	renderTemplate(w, "register", &p)
}

func privateHandler(w http.ResponseWriter, r *http.Request, title string) {
	fmt.Println("Cookie.......")
	cookie,_ := r.Cookie("Alice")
	fmt.Println(cookie)
	link := "http://localhost:8080/v1/accounts/@me?token=" + token
	resp, err := http.Get(link)
	if err != nil {
		log.Println(err)
	}
	defer resp.Body.Close()
	bodyBytes, err := ioutil.ReadAll(resp.Body)
	var responseInfo Response
	err = json.Unmarshal(bodyBytes, &responseInfo)
	if err != nil {
		log.Println(err)
	}
	data := responseInfo.Message
	var pageInfo Profile
	err = json.Unmarshal([]byte(data), &pageInfo)
	renderTemplate(w, "profile", &pageInfo)
}

func main() {
	http.HandleFunc("/accounts/", makeHandler(accountsHandler))
	http.HandleFunc("/edit/", makeHandler(editHandler))
	http.HandleFunc("/save/", makeHandler(saveHandler))
	http.HandleFunc("/register/", makeHandler(registerHandler))
	http.HandleFunc("/create/", makeHandler(createHandler))
	http.HandleFunc("/login/", makeHandler(loginHandler))
	http.HandleFunc("/home/", makeHandler(homeHandler))
	http.HandleFunc("/privatePage/", makeHandler(privateHandler))
	log.Println(http.ListenAndServe(":5000", nil))
}