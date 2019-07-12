package main

import (
	"encoding/json"
	"errors"
	"flag"
	"html/template"
	"io/ioutil"
	"log"
	"net/http"
	"strings"
	"time"

	"github.com/gorilla/mux"
)

type profile struct {
	Username    string `json:"username"`
	Firstname   string `json:"firstName"`
	Lastname    string `json:"lastName"`
	Email       string `json:"email"`
	Description string `json:"description"`
	Password    string `json:"password"`
	Verified    string `json:"verified"`
}

type updateDescription struct {
	Username    string `json:"username"`
	Description string `json:"description"`
	Token       string `json:"token"`
}

type updateEmail struct {
	Username string `json:"username"`
	Email    string `json:"email"`
	Token    string `json:"token"`
}

type publicInfo struct {
	Username    string `json:"username"`
	Description string `json:"description"`
}

type logInfo struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

//UpdatePassword Wrap up info to update password with CSRF token
type updatePassword struct {
	Username string `json:"username"`
	Password string `json:"password"`
	Token    string `json:"token"`
}

//TokenInfo to store the token value and type
type TokenInfo struct {
	Value string `json:"value"`
	Type  string `json:"type"`
}

const (
	backendURI       = "/v1"
	timeoutInSeconds = 10
)

var (
	backendAddr, backendURL string
	client                  = &http.Client{
		Timeout: time.Second * timeoutInSeconds,
	}
	templates = template.Must(template.ParseGlob("./template/*.html"))
)

func instanceToPayLoad(info interface{}) (*strings.Reader, error) {
	tokenData, err := json.Marshal(info)
	if err != nil {
		return nil, err
	}
	tokenBuffer := string(tokenData)
	return strings.NewReader(tokenBuffer), nil
}

func renderTemplate(w http.ResponseWriter, tmplName string, p *profile) {
	err := templates.ExecuteTemplate(w, tmplName+".html", p)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}

func readMyProfile(cookieValue string) (*profile, error) {
	url := backendURL + "/accounts/@me"
	request, err := http.NewRequest("GET", url, nil)
	if err != nil {
		log.Println(err)
		return nil, err
	}
	request.Header.Add("Cookie", cookieValue)
	resp, err := client.Do(request)
	if err != nil {
		log.Println("Cookie may expire ", err)
		return nil, err
	}
	defer resp.Body.Close()
	bs, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return nil, err
	}
	var pageInfo profile
	err = json.Unmarshal(bs, &pageInfo)
	if err != nil {
		return nil, err
	}
	return &pageInfo, err
}

// Function to read public information without login and render the web pages
func readUserProfile(username string) (*publicInfo, error) {
	url := backendURL + "/accounts/" + username
	resp, err := client.Get(url)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	bs, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return nil, err
	}

	var pageData publicInfo
	err = json.Unmarshal(bs, &pageData)
	if err != nil {
		return nil, err
	}

	return &pageData, err
}

//
func accountsHandler(w http.ResponseWriter, r *http.Request) {
	variables := mux.Vars(r)
	p, err := readUserProfile(variables["id"])
	if err != nil {
		log.Println("account does not exist", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}

	page := profile{Username: p.Username,
		Description: p.Description}
	renderTemplate(w, "publicProfile", &page)
}

// Render the edit information page
func editHandler(w http.ResponseWriter, r *http.Request) {
	cookie, err := r.Cookie("V")
	if err != nil || cookie == nil {
		log.Println("May log out or cookie expire", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	cookieValue := strings.TrimPrefix(cookie.Value, "cookie=")
	// Prefetch from backend API
	p, err := readMyProfile(cookieValue)
	if err != nil {
		log.Println("error happened when getting profile", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	client := &http.Client{}
	//
	token := TokenInfo{"", "STANDARD"}
	tokenPayload, err := instanceToPayLoad(token)
	if err != nil {
		log.Println("error when decoding JSON", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	// Get token request
	getTokenURL := backendURL + "/tokens/"
	request, err := http.NewRequest("POST", getTokenURL, tokenPayload)
	if err != nil {
		log.Println("error when building request ", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	request.Header.Add("Content-Type", "application/json")
	request.Header.Add("Cookie", cookieValue)
	response, err := client.Do(request)
	if err != nil {
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	defer response.Body.Close()
	bodyBytes, err := ioutil.ReadAll(response.Body)
	if err != nil {
		log.Println("error occurred when reading response", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	err = json.Unmarshal(bodyBytes, &token)
	if err != nil {
		log.Println("Decode response error", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	updateNormal := updateDescription{p.Username, p.Description, token.Value}
	renderTemplateDescription(w, "edit", &updateNormal)
}

// The Function to save the edited information
func saveEditedInfo(w http.ResponseWriter, r *http.Request) {
	description := r.FormValue("description")
	token := r.FormValue("CSRFToken")
	// Get cookie from browser
	cookie, err := r.Cookie("V")
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	cookieValue := strings.TrimPrefix(cookie.Value, "cookie=")
	originalPage, err := readMyProfile(cookieValue)
	if err != nil {
		log.Println("cookie may expire and need to login again ", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	// Get the standard token to change the regular information
	client := &http.Client{}
	// Change the information from preloaded information
	updateInfo := updateDescription{Description: description,
		Username: originalPage.Username,
		Token:    ""}
	// Json format transformation
	payload, err := instanceToPayLoad(updateInfo)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	// Send http request
	link := backendURL + "/accounts/@me?token=" + token
	request, err := http.NewRequest("PUT", link, payload)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	request.Header.Add("Content-Type", "application/json")
	request.Header.Add("Cookie", cookieValue)
	_, err = client.Do(request)
	if err != nil {
		log.Println("empty response:", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	client.CloseIdleConnections()
	// After edited it redirect to the private information page
	http.Redirect(w, r, "/privatePage/", http.StatusFound)
}

func passwordHandler(w http.ResponseWriter, r *http.Request) {
	// Get cookie
	cookie, err := r.Cookie("V")
	if err != nil {
		log.Println("cookie may expire login again", err)
		// When refactoring the code, a more specific page may needed
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	cookieValue := strings.TrimPrefix(cookie.Value, "cookie=")
	// Prefetch info to render pages from backend API
	p, err := readMyProfile(cookieValue)
	if err != nil {
		log.Println("cookie may expire login again", err)
		// When refactoring the code, a more specific page may needed
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	client := &http.Client{}
	// Set token struct to get token
	token := TokenInfo{}
	token.Type = "CRITICAL"
	token.Value = ""
	tokenPayload, err := instanceToPayLoad(token)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	// Get token request
	getTokenURL := backendURL + "/tokens/"
	request, err := http.NewRequest("POST", getTokenURL, tokenPayload)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	request.Header.Add("Content-Type", "application/json")
	request.Header.Add("Cookie", cookieValue)
	response, err := client.Do(request)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	defer response.Body.Close()
	bodyBytes, err := ioutil.ReadAll(response.Body)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	err = json.Unmarshal(bodyBytes, &token)
	defer client.CloseIdleConnections()
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	updatePassword := updatePassword{}
	updatePassword.Username = p.Username
	updatePassword.Password = ""
	updatePassword.Token = token.Value
	renderTemplatePassword(w, "changePassword", &updatePassword)
}

func passwordSaveHandler(w http.ResponseWriter, r *http.Request) {
	newPassword := r.FormValue("newpassword")
	passwordConfirm := r.FormValue("passwordconfirm")
	token := r.FormValue("token")
	// Get cookie from browser
	cookie, err := r.Cookie("V")
	if err != nil {
		log.Println("login expire, log in again", err)
		http.Redirect(w, r, "/login", http.StatusFound)
		return
	}
	cookieValue := strings.TrimPrefix(cookie.Value, "cookie=")
	originalProfile, err := readMyProfile(cookieValue)
	if err != nil {
		log.Println("login expire, log in again", err)
		http.Redirect(w, r, "/login", http.StatusFound)
		return
	}
	if newPassword != passwordConfirm {
		log.Println("password does not match")
		http.Redirect(w, r, "/password/", http.StatusFound)
		return
	}
	updatePassword := updatePassword{
		Username: originalProfile.Username,
		Password: newPassword,
		Token:    token}
	client := &http.Client{}
	// Struct to JSON payload
	payload, err := instanceToPayLoad(updatePassword)
	if err != nil {
		log.Println("Json transfer failure", err)
		http.Redirect(w, r, "/password/", http.StatusFound)
		return
	}
	// Send http request
	link := backendURL + "/accounts/@me?token=" + token
	request, err := http.NewRequest("PUT", link, payload)
	if err != nil {
		log.Println("request failed", err)
		http.Redirect(w, r, "/password/", http.StatusFound)
		return
	}
	request.Header.Add("Cookie", cookieValue)
	request.Header.Add("Content-Type", "application/json")
	response, err := client.Do(request)
	if err != nil {
		log.Println("request failed", err)
		http.Redirect(w, r, "/password/", http.StatusFound)
		return
	}
	if response.StatusCode == 500 {
		log.Println("Error Occur when changing password", err)
		http.Redirect(w, r, "/password/", http.StatusFound)
		return
	}
	client.CloseIdleConnections()
	http.Redirect(w, r, "/privatePage/", http.StatusFound)
	return
}

//
func loginSubmitHandler(w http.ResponseWriter, r *http.Request) {
	// Get user login in information
	username := r.FormValue("username")
	password := r.FormValue("password")
	// Encode the log in data to the json format payload
	user := logInfo{username, password}
	payload, err := instanceToPayLoad(user)
	if err != nil {
		log.Println("Json decode error", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	response, err := http.Post("http://localhost:8080/v1/sessions/", "application/json", payload)
	if err != nil || response.StatusCode == 401 {
		log.Println("login Failure", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	// Get token from login response from backend
	var credential *http.Cookie
	if credential, err = getCookieByName(response.Cookies(), "V"); err != nil {
		log.Println("failed to get cookie from response", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	// Set the cookies to the browser
	Cookie := http.Cookie{Name: "V",
		Value:    credential.Value,
		Path:     "/",
		HttpOnly: true}
	http.SetCookie(w, &Cookie)
	http.Redirect(w, r, "/privatePage/", http.StatusFound)
	return
}

func getCookieByName(cookies []*http.Cookie, name string) (*http.Cookie, error) {
	for _, cookie := range cookies {
		if cookie.Name == name {
			return cookie, nil
		}
	}
	return nil, errors.New("No cookie with name \"" + name + "\"")
}

func editEmailHandler(w http.ResponseWriter, r *http.Request) {
	cookie, err := r.Cookie("V")
	if err != nil {
		log.Println("cookie may expire login again", err)
		// When refactoring the code, a more specific page may needed
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	cookieValue := strings.TrimPrefix(cookie.Value, "cookie=")
	// Prefetch info to render pages from backend API
	p, err := readMyProfile(cookieValue)
	if err != nil {
		log.Println("cookie may expire login again", err)
		// When refactoring the code, a more specific page may needed
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	client := &http.Client{}
	// Set token struct to get token
	token := TokenInfo{}
	token.Type = "CRITICAL"
	token.Value = ""
	tokenPayload, err := instanceToPayLoad(token)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	// Get token request
	getTokenURL := backendURL + "/tokens/"
	request, err := http.NewRequest("POST", getTokenURL, tokenPayload)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	request.Header.Add("Content-Type", "application/json")
	request.Header.Add("Cookie", cookieValue)
	response, err := client.Do(request)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	defer response.Body.Close()
	bodyBytes, err := ioutil.ReadAll(response.Body)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	err = json.Unmarshal(bodyBytes, &token)
	defer client.CloseIdleConnections()
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	updateEmail := updateEmail{p.Username, p.Email, token.Value}
	renderTemplateEmail(w, "editEmail", &updateEmail)
}

func emailSaveHandler(w http.ResponseWriter, r *http.Request) {
	newEmail := r.FormValue("email")
	token := r.FormValue("CSRFToken")
	// Get cookie from browser
	cookie, err := r.Cookie("V")
	if err != nil {
		log.Println("login expire, log in again", err)
		http.Redirect(w, r, "/home/", http.StatusFound)
		return
	}
	cookieValue := strings.TrimPrefix(cookie.Value, "cookie=")
	originalProfile, err := readMyProfile(cookieValue)
	if err != nil {
		log.Println("login expire, log in again", err)
		http.Redirect(w, r, "/home/", http.StatusFound)
		return
	}
	updateEmail := updateEmail{
		Username: originalProfile.Username,
		Email:    newEmail,
		Token:    ""}
	client := &http.Client{}
	// Struct to JSON payload
	payload, err := instanceToPayLoad(updateEmail)
	if err != nil {
		log.Println("Json transfer failure", err)
		http.Redirect(w, r, "/editEmail/", http.StatusFound)
		return
	}
	// Send http request
	link := backendURL + "/accounts/@me?token=" + token
	request, err := http.NewRequest("PUT", link, payload)
	if err != nil {
		log.Println("request failed", err)
		http.Redirect(w, r, "/editEmail/", http.StatusFound)
		return
	}
	request.Header.Add("Cookie", cookieValue)
	request.Header.Add("Content-Type", "application/json")
	response, err := client.Do(request)
	if err != nil {
		log.Println("request failed", err)
		http.Redirect(w, r, "/editEmail/", http.StatusFound)
		return
	}
	if response.StatusCode == 500 {
		log.Println("Error Occur when changing email", err)
		http.Redirect(w, r, "/editEmail/", http.StatusFound)
		return
	}
	client.CloseIdleConnections()
	http.Redirect(w, r, "/privatePage/", http.StatusFound)
	return

}

//
func registerHandler(w http.ResponseWriter, r *http.Request) {
	p := profile{}
	renderTemplate(w, "register", &p)
}

//
func privateHandler(w http.ResponseWriter, r *http.Request) {
	// Read cookie from browser
	cookie, err := r.Cookie("V")
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	client := &http.Client{}
	req, err := http.NewRequest("GET", "http://localhost:8080/v1/accounts/@me", nil)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	cookieValue := strings.TrimPrefix(cookie.Value, "cookie=")
	req.Header.Add("Cookie", cookieValue)
	resp, err := client.Do(req)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	defer resp.Body.Close()
	// Read personal profile data from backend and transform to our data format
	bodyBytes, err := ioutil.ReadAll(resp.Body)
	var pageInfo = profile{}
	err = json.Unmarshal(bodyBytes, &pageInfo)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/home/", http.StatusFound)
		return
	}
	renderTemplate(w, "profile", &pageInfo)
}

//
func createHandler(w http.ResponseWriter, r *http.Request) {
	var pageInfo profile
	pageInfo.Username = r.FormValue("username")
	pageInfo.Firstname = r.FormValue("firstname")
	pageInfo.Lastname = r.FormValue("lastname")
	pageInfo.Description = r.FormValue("description")
	pageInfo.Password = r.FormValue("password")
	pageInfo.Email = r.FormValue("email")
	pageInfo.Verified = "true"

	payload, err := instanceToPayLoad(pageInfo)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/login", http.StatusFound)
		return
	}
	// Encode data to Json
	_, err = client.Post(backendURL+"/accounts/", "application/json", payload)
	if err != nil {
		log.Println("error occur when creating account", err)
		http.Redirect(w, r, "/create/", http.StatusFound)
		return
	}
	// Send the request to create a new account
	user := logInfo{pageInfo.Username, pageInfo.Password}
	payload, err = instanceToPayLoad(user)
	response, err := client.Post(backendURL+"/sessions/", "application/json", payload)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/create/", http.StatusFound)
		return
	}
	// Log in with newly created account information
	// Get credential from login response of backend
	cookies := response.Cookies()
	var credential *http.Cookie
	if credential, err = getCookieByName(cookies, "V"); err != nil {
		log.Println("Cannot find V cookie in the response", err)
		http.Redirect(w, r, "/create/", http.StatusFound)
		return
	}

	// Set the cookie to the browser
	Cookie := http.Cookie{Name: "V",
		Value:    credential.Value,
		Path:     "/",
		HttpOnly: true}
	http.SetCookie(w, &Cookie)

	// After log in, redirect to the personal private page
	http.Redirect(w, r, "/privatePage/", http.StatusFound)
}

// Handle the login page
func loginHandler(w http.ResponseWriter, r *http.Request) {
	p := profile{}
	renderTemplate(w, "login", &p)
}

func homeHandler(w http.ResponseWriter, r *http.Request) {
	http.Redirect(w, r, "/login", http.StatusFound)
}

func renderTemplateDescription(w http.ResponseWriter, tmpl string, p *updateDescription) {
	err := templates.ExecuteTemplate(w, tmpl+".html", p)
	if err != nil {
		log.Println(err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

func renderTemplateEmail(w http.ResponseWriter, tmpl string, p *updateEmail) {
	err := templates.ExecuteTemplate(w, tmpl+".html", p)
	if err != nil {
		log.Println(err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

func renderTemplatePassword(w http.ResponseWriter, tmpl string, p *updatePassword) {
	err := templates.ExecuteTemplate(w, tmpl+".html", p)
	if err != nil {
		log.Println(err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

//
// Handle error when having wrong password and let user to re-enter password
func errorPasswordHandler(w http.ResponseWriter, r *http.Request) {
	p := profile{}
	renderTemplate(w, "loginError", &p)
}

// When user need to log out, this handler would erase the cookie to clean up the log in status.
func logoutHandler(w http.ResponseWriter, r *http.Request) {
	logOutCookie := http.Cookie{Name: "V",
		Path:   "/",
		MaxAge: -1}
	http.SetCookie(w, &logOutCookie)
	http.Redirect(w, r, "/", http.StatusFound)
}

//
func main() {
	flag.StringVar(&backendAddr, "backend", "localhost:8080", "backend IP and port")
	flag.Parse()
	backendURL = "http://" + backendAddr + backendURI

	router := mux.NewRouter()

	router.HandleFunc("/accounts/{id}", accountsHandler)
	router.HandleFunc("/edit", editHandler)
	router.HandleFunc("/save/", saveEditedInfo)
	router.HandleFunc("/register/", registerHandler)
	router.HandleFunc("/create/", createHandler)
	router.HandleFunc("/login", loginSubmitHandler).
		Methods("POST")
	router.HandleFunc("/login", loginHandler).
		Methods("GET")
	router.HandleFunc("/", homeHandler).
		Methods("GET")
	router.HandleFunc("/privatePage/", privateHandler)
	router.HandleFunc("/logout/", logoutHandler)
	router.HandleFunc("/loginError/", errorPasswordHandler)
	router.HandleFunc("/password/", passwordHandler)
	router.HandleFunc("/passwordsave/", passwordSaveHandler)
	router.HandleFunc("/editEmail/", editEmailHandler)
	router.HandleFunc("/emailSave/", emailSaveHandler)
	log.Println("Listening on port 5000...")
	log.Println(http.ListenAndServe(":5000", router))
}
