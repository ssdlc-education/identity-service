package main

import (
	"encoding/json"
	"errors"
	"flag"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"net/url"
	"strings"
	"time"

	"github.com/gorilla/mux"
)

const (
	timeoutInSeconds = 10
	homeURLPath      = "/"

	accountPublicProfileURLPath  = "/account/view/{id:[^@]+}"
	accountPrivateProfileURLPath = "/account/view"
	accountLoginURLPath          = "/account/login"
	accountEditURLPath           = "/account/edit"
	accountRegisterURLPath       = "/account/register"
	accountLogoutURLPath         = "/account/logout"
	accountPasswordUpdateURLPath = "/account/update_password"
	accountEmailUpdateURLPath    = "/account/update_email"

	accountEditTemplate           = "edit.html"
	accountLoginTemplate          = "login.html"
	accountPrivateProfileTemplate = "profile.html"
	accountPublicProfileTemplate  = "publicProfile.html"
	accountRegisterTemplate       = "register.html"
)

type pageMeta struct {
	HomeURLPath                  string
	AccountPublicProfileURLPath  string
	AccountPrivateProfileURLPath string
	AccountLoginURLPath          string
	AccountEditURLPath           string
	AccountRegisterURLPath       string
	AccountLogoutURLPath         string
	AccountPasswordUpdateURLPath string
	AccountEmailUpdateURLPath    string
}

func NewPageMeta() *pageMeta {
	return &pageMeta{
		HomeURLPath:                  homeURLPath,
		AccountPublicProfileURLPath:  accountPublicProfileURLPath,
		AccountPrivateProfileURLPath: accountPrivateProfileURLPath,
		AccountLoginURLPath:          accountLoginURLPath,
		AccountEditURLPath:           accountEditURLPath,
		AccountRegisterURLPath:       accountRegisterURLPath,
		AccountLogoutURLPath:         accountLogoutURLPath,
		AccountPasswordUpdateURLPath: accountPasswordUpdateURLPath,
		AccountEmailUpdateURLPath:    accountEmailUpdateURLPath,
	}
}

type accountLoginPage struct {
	*pageMeta
	ErrorMessage string
}

func renderLoginPageWithError(w http.ResponseWriter, errMessage string) {
	page := accountLoginPage{pageMeta: NewPageMeta(), ErrorMessage: errMessage}
	renderTemplate(w, accountLoginTemplate, &page)
}

type accountRegisterPage struct {
	*pageMeta
	ErrorMessage string
	Username     string
	Firstname    string
	Lastname     string
	Email        string
	Description  string
}

func renderRegisterPageWithError(w http.ResponseWriter, p *profile, errMessage string) {
	page := accountRegisterPage{
		pageMeta:     NewPageMeta(),
		ErrorMessage: errMessage,
		Username:     *p.Username,
		Firstname:    *p.Firstname,
		Lastname:     *p.Lastname,
		Email:        *p.Email,
		Description:  *p.Description,
	}
	renderTemplate(w, accountRegisterTemplate, &page)
}

type accountProfilePage struct {
	*pageMeta
	Username    string
	Firstname   string
	Lastname    string
	Email       string
	Description string
}

type accountPublicProfilePage struct {
	*pageMeta
	Username    string
	Description string
}

type profile struct {
	Username    *string `json:"username,omitempty"`
	Firstname   *string `json:"firstName,omitempty"`
	Lastname    *string `json:"lastName,omitempty"`
	Email       *string `json:"email,omitempty"`
	Description *string `json:"description,omitempty"`
	Password    *string `json:"password,omitempty"`
}

type profileEditPage struct {
	*pageMeta
	Description  string
	Token        string
	ErrorMessage string
}

type publicInfo struct {
	Username    string `json:"username"`
	Description string `json:"description"`
}

type session struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

//TokenInfo to store the token value and type
type TokenInfo struct {
	Value string `json:"value"`
	Type  string `json:"type"`
}

var (
	backendURL string
	client     = &http.Client{
		Timeout: time.Second * timeoutInSeconds,
	}
)

func instanceToPayLoad(info interface{}) (*strings.Reader, error) {
	tokenData, err := json.Marshal(info)
	if err != nil {
		return nil, err
	}
	tokenBuffer := string(tokenData)
	return strings.NewReader(tokenBuffer), nil
}

func readMyProfile(cookie *http.Cookie) (*profile, error) {
	url := backendURL + "/accounts/@me"
	request, err := http.NewRequest("GET", url, nil)
	if err != nil {
		log.Println(err)
		return nil, err
	}
	request.AddCookie(cookie)
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
	backendUrl := backendURL + "/accounts/" + url.PathEscape(username)
	resp, err := client.Get(backendUrl)
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

func renderPublicProfile(w http.ResponseWriter, r *http.Request) {
	variables := mux.Vars(r)
	profile, err := readUserProfile(variables["id"])
	if err != nil {
		log.Println("account does not exist", err)
		http.Redirect(w, r, homeURLPath, http.StatusFound)
		return
	}

	page := accountPublicProfilePage{}
	page.Username = profile.Username
	page.Description = profile.Description
	renderTemplate(w, accountPublicProfileTemplate, &page)
}

// Render the edit information page
func renderProfileEdit(w http.ResponseWriter, r *http.Request) {
	cookie, err := r.Cookie("V")
	if err != nil || cookie == nil {
		log.Println("May log out or cookie expire", err)
		http.Redirect(w, r, accountLoginURLPath, http.StatusFound)
		return
	}
	// Prefetch from backend API
	profile, err := readMyProfile(cookie)
	if err != nil {
		log.Println("error happened when getting profile", err)
		http.Redirect(w, r, accountLoginURLPath, http.StatusFound)
		return
	}
	client := &http.Client{}
	//
	token := TokenInfo{"", "STANDARD"}
	tokenPayload, err := instanceToPayLoad(token)
	if err != nil {
		log.Println("error when decoding JSON", err)
		http.Redirect(w, r, accountLoginURLPath, http.StatusFound)
		return
	}
	// Get token request
	getTokenURL := backendURL + "/tokens/"
	request, err := http.NewRequest("POST", getTokenURL, tokenPayload)
	if err != nil {
		log.Println("error when building request ", err)
		http.Redirect(w, r, accountLoginURLPath, http.StatusFound)
		return
	}
	request.Header.Add("Content-Type", "application/json")
	request.AddCookie(cookie)
	response, err := client.Do(request)
	if err != nil {
		http.Redirect(w, r, accountLoginURLPath, http.StatusFound)
		return
	}
	defer response.Body.Close()
	bodyBytes, err := ioutil.ReadAll(response.Body)
	if err != nil {
		log.Println("error occurred when reading response", err)
		http.Redirect(w, r, accountLoginURLPath, http.StatusFound)
		return
	}
	err = json.Unmarshal(bodyBytes, &token)
	if err != nil {
		log.Println("Decode response error", err)
		http.Redirect(w, r, accountLoginURLPath, http.StatusFound)
		return
	}
	page := profileEditPage{
		pageMeta:    NewPageMeta(),
		Description: *profile.Description,
		Token:       token.Value,
	}
	renderTemplate(w, accountEditTemplate, &page)
}

// The Function to save the edited information
func submitProfileEdit(w http.ResponseWriter, r *http.Request) {
	editPage := profileEditPage{
		pageMeta:    NewPageMeta(),
		Description: r.FormValue("description"),
		Token:       r.FormValue("token"),
	}
	// Get cookie from browser
	cookie, err := r.Cookie("V")
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, accountLoginURLPath, http.StatusFound)
		return
	}
	// Get the standard token to change the regular information
	client := &http.Client{}

	profileToUpdate := profile{
		Description: &editPage.Description,
	}

	payload, err := instanceToPayLoad(profileToUpdate)
	if err != nil {
		log.Println(err)
		editPage.ErrorMessage = "Internal server error"
		renderTemplate(w, accountEditTemplate, editPage)
		return
	}
	// Send http request
	link := backendURL + "/accounts/@me?token=" + url.QueryEscape(editPage.Token)
	request, err := http.NewRequest("PUT", link, payload)
	if err != nil {
		log.Println(err)
		editPage.ErrorMessage = "Internal server error"
		renderTemplate(w, accountEditTemplate, editPage)
		return
	}
	request.Header.Add("Content-Type", "application/json")
	request.AddCookie(cookie)
	resp, err := client.Do(request)
	if err != nil || resp.StatusCode != http.StatusNoContent {
		log.Printf("Failed to update account: %s", err)
		editPage.ErrorMessage = "Internal server error"
		renderTemplate(w, accountEditTemplate, editPage)
		return
	}
	client.CloseIdleConnections()

	// After edited it redirect to the private information page
	http.Redirect(w, r, accountPrivateProfileURLPath, http.StatusFound)
}

func submitAccountLogin(w http.ResponseWriter, r *http.Request) {
	// Get user login in information
	username := r.FormValue("username")
	password := r.FormValue("password")
	// Encode the log in data to the json format payload
	user := session{username, password}
	payload, err := instanceToPayLoad(user)
	if err != nil {
		log.Println("Json decode error", err)
		renderLoginPageWithError(w, "Internal server error")
		return
	}
	response, err := http.Post(backendURL+"/sessions", "application/json", payload)
	if err != nil || response.StatusCode != http.StatusCreated {
		log.Println("login Failure", err)
		renderLoginPageWithError(w, "Account not found or incorrect password")
		return
	}
	// Get token from login response from backend
	var credential *http.Cookie
	if credential, err = getCookieByName(response.Cookies(), "V"); err != nil {
		log.Println("failed to get cookie from response", err)
		renderLoginPageWithError(w, "Internal server error")
		return
	}
	// Set the cookies to the browser
	http.SetCookie(w, convertCookieForResponse(credential))
	http.Redirect(w, r, accountPrivateProfileURLPath, http.StatusFound)
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

func renderAccountCreate(w http.ResponseWriter, r *http.Request) {
	p := accountRegisterPage{
		pageMeta: NewPageMeta(),
	}
	renderTemplate(w, accountRegisterTemplate, &p)
}

func renderPrivateProfile(w http.ResponseWriter, r *http.Request) {
	// Read cookie from browser
	cookie, err := r.Cookie("V")
	if err != nil {
		log.Println("no cookie is provided", err)
		http.Redirect(w, r, accountLoginURLPath, http.StatusFound)
		return
	}
	client := &http.Client{}
	req, err := http.NewRequest("GET", backendURL+"/accounts/@me", nil)
	if err != nil {
		log.Println("failed to build request", err)
		http.Redirect(w, r, accountLoginURLPath, http.StatusFound)
		return
	}
	req.AddCookie(cookie)
	resp, err := client.Do(req)
	if err != nil {
		log.Println("failed to get account", err)
		http.Redirect(w, r, accountLoginURLPath, http.StatusFound)
		return
	}
	defer resp.Body.Close()
	// Read personal profile data from backend and transform to our data format
	bodyBytes, err := ioutil.ReadAll(resp.Body)
	var profile = profile{}
	err = json.Unmarshal(bodyBytes, &profile)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, accountLoginURLPath, http.StatusFound)
		return
	}
	page := accountProfilePage{}
	page.pageMeta = NewPageMeta()
	page.Username = *profile.Username
	page.Firstname = *profile.Firstname
	page.Lastname = *profile.Lastname
	page.Email = *profile.Email
	page.Description = *profile.Description
	renderTemplate(w, accountPrivateProfileTemplate, &page)
}

func submitAccountCreate(w http.ResponseWriter, r *http.Request) {
	var profile profile
	username := r.FormValue("username")
	firstName := r.FormValue("firstname")
	lastName := r.FormValue("lastname")
	description := r.FormValue("description")
	password := r.FormValue("password")
	email := r.FormValue("email")

	profile.Username = &username
	profile.Firstname = &firstName
	profile.Lastname = &lastName
	profile.Description = &description
	profile.Password = &password
	profile.Email = &email

	payload, err := instanceToPayLoad(profile)
	if err != nil {
		log.Println(err)
		renderRegisterPageWithError(w, &profile, "Internal server error")
		return
	}
	resp, err := client.Post(backendURL+"/accounts", "application/json", payload)
	if err != nil || resp.StatusCode != http.StatusCreated {
		log.Println("error occur when creating account", err)
		if err == nil && resp.StatusCode == http.StatusBadRequest {
			renderRegisterPageWithError(w, &profile, "Invalid data")
		} else {
			renderRegisterPageWithError(w, &profile, "Internal server error")
		}
		return
	}
	// Log in with newly created account information
	// Get credential from login response of backend
	cookies := resp.Cookies()
	var credential *http.Cookie
	if credential, err = getCookieByName(cookies, "V"); err != nil {
		log.Println("Cannot find V cookie in the response: ", err)
		renderRegisterPageWithError(w, &profile, "Internal server error")
		return
	}

	// Set the cookie to the browser
	http.SetCookie(w, convertCookieForResponse(credential))

	// After log in, redirect to the personal private page
	http.Redirect(w, r, accountPrivateProfileURLPath, http.StatusFound)
}

// Handle the login page
func renderAccountLogin(w http.ResponseWriter, r *http.Request) {
	p := accountLoginPage{pageMeta: NewPageMeta()}
	renderTemplate(w, accountLoginTemplate, &p)
}

func renderHome(w http.ResponseWriter, r *http.Request) {
	http.Redirect(w, r, accountLoginURLPath, http.StatusFound)
}

// When user need to log out, this handler would erase the cookie to clean up the log in status.
func renderLogout(w http.ResponseWriter, r *http.Request) {
	logOutCookie := http.Cookie{Name: "V",
		Path:   "/",
		MaxAge: -1}
	http.SetCookie(w, &logOutCookie)
	http.Redirect(w, r, "/", http.StatusFound)
}

func main() {
	var backendAddr string
	var listenPort int
	flag.StringVar(&backendAddr, "backend", "identity-backend:8080", "backend IP and port")
	flag.IntVar(&listenPort, "port", 5000, "Port to listen")
	flag.Parse()
	backendURL = "http://" + backendAddr + "/v1"
	log.Printf("Backend URL: %s", backendURL)

	router := mux.NewRouter()

	router.HandleFunc(accountPublicProfileURLPath, renderPublicProfile).
		Methods("GET")
	router.HandleFunc(accountEditURLPath, renderProfileEdit).
		Methods("GET")
	router.HandleFunc(accountEditURLPath, submitProfileEdit).
		Methods("POST")
	router.HandleFunc(accountRegisterURLPath, renderAccountCreate).
		Methods("GET")
	router.HandleFunc(accountRegisterURLPath, submitAccountCreate).
		Methods("POST")
	router.HandleFunc(accountLoginURLPath, submitAccountLogin).
		Methods("POST")
	router.HandleFunc(accountLoginURLPath, renderAccountLogin).
		Methods("GET")
	router.HandleFunc(homeURLPath, renderHome).
		Methods("GET")
	router.HandleFunc(accountPrivateProfileURLPath, renderPrivateProfile).
		Methods("GET")
	router.HandleFunc(accountLogoutURLPath, renderLogout).
		Methods("GET")
	listenAddr := fmt.Sprintf(":%d", listenPort)
	log.Printf("Listening on http://0.0.0.0:%d ...", listenPort)
	log.Println(http.ListenAndServe(listenAddr, router))
}
