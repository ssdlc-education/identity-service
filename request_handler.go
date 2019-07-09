package main

import (
	"encoding/json"
	"html/template"
	"io/ioutil"
	"log"
	"net/http"
	"regexp"
	"strings"
)

//Profile Contains all the information about user's information
type Profile struct {
	Username    string `json:"username"`
	Firstname   string `json:"firstName"`
	Lastname    string `json:"lastName"`
	Email       string `json:"email"`
	Description string `json:"description"`
	Password    string `json:"password"`
	Verified    string `json:"verified"`
}

//UpdateDescription Struct to contain page information using to update normal information
type UpdateDescription struct {
	Username    string `json:"username"`
	Description string `json:"description"`
	Token       string `json:"token"`
}

//UpdateEmail Wrap up info to update email with CSRF token
type UpdateEmail struct {
	Username string `json:"username"`
	Email    string `json:"email"`
	Token    string `json:"token"`
}

//UpdatePassword Wrap up info to update password with CSRF token
type UpdatePassword struct {
	Username string `json:"username"`
	Password string `json:"password"`
	Token    string `json:"token"`
}

//PublicInfo Struct to store updated information
type PublicInfo struct {
	Username    string `json:"username"`
	Description string `json:"description"`
}

//LogInfo Struct to store public page information
type LogInfo struct {
	Username string `json:"username"`
	Password string `json:"password"`
}

//TokenInfo to store the token value and type
type TokenInfo struct {
	Value string `json:"value"`
	Type  string `json:"type"`
}

// Preload the information by fetching data from backend
func readProfile(cookieValue string) (*Profile, error) {
	apiURL := "http://localhost:8080/v1/accounts/@me"
	client := &http.Client{}
	request, err := http.NewRequest("GET", apiURL, nil)
	if err != nil {
		return nil, err
	}
	request.Header.Add("Cookie", cookieValue)
	resp, err := client.Do(request)
	if err != nil {
		log.Println("Cookie may expire ", err)
		return nil, err
	}
	defer resp.Body.Close()
	bodyBytes, err := ioutil.ReadAll(resp.Body)
	// Extract data from response and turn to pageInfo type
	var pageInfo Profile
	err = json.Unmarshal(bodyBytes, &pageInfo)
	if err != nil {
		log.Println(err)
		return nil, err
	}
	return &pageInfo, err
}

// Function to read public information without login and render the web pages
func readFromPublic(username string) (*PublicInfo, error) {
	link := "http://localhost:8080/v1/accounts/" + username
	resp, err := http.Get(link)
	if err != nil {
		log.Println("account does not exists", err)
		return nil, err
	}
	defer resp.Body.Close()
	bodyBytes, err := ioutil.ReadAll(resp.Body)
	var pageData PublicInfo
	err = json.Unmarshal(bodyBytes, &pageData)
	return &pageData, err
}

//
func accountsHandler(w http.ResponseWriter, r *http.Request, title string) {
	p, err := readFromPublic(title)
	if err != nil {
		log.Println("account does not exist", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	page := Profile{}
	page.Username = p.Username
	page.Description = p.Description
	renderTemplate(w, "public_profile", &page)
}

// Render the edit information page
func editHandler(w http.ResponseWriter, r *http.Request, title string) {
	// Get cookie
	cookie, err := r.Cookie("V")
	if err != nil || cookie == nil {
		log.Println("May log out or cookie expire", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	cookieValue := strings.TrimPrefix(cookie.Value, "cookie=")
	// Prefetch from backend API
	p, err := readProfile(cookieValue)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	client := &http.Client{}
	//
	token := TokenInfo{}
	token.Type = "STANDARD"
	token.Value = ""
	tokenData, err := json.Marshal(token)
	if err != nil {
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	tokenBuffer := string(tokenData)
	tokenPayload := strings.NewReader(tokenBuffer)
	// Get token request
	getTokenURL := "http://localhost:8080/v1/tokens"
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
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	updateNormal := UpdateDescription{}
	updateNormal.Username = p.Username
	updateNormal.Description = p.Description
	updateNormal.Token = token.Value
	renderTemplateDescription(w, "edit", &updateNormal)
}

// The Function to save the edited information
func saveEditedInfo(w http.ResponseWriter, r *http.Request, title string) {
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
	originalPage, err := readProfile(cookieValue)
	if err != nil {
		log.Println("cookie may expire and need to login again ", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	// Get the standard token to change the regular information
	client := &http.Client{}
	// Change the information from preloaded information
	updateInfo := PublicInfo{}
	updateInfo.Username = originalPage.Username
	updateInfo.Description = description
	// Json format transformation
	jsonData, err := json.Marshal(updateInfo)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	Data := string(jsonData)
	payload := strings.NewReader(Data)
	// Send http request
	link := "http://localhost:8080/v1/accounts/@me?token=" + token
	request, err := http.NewRequest("PUT", link, payload)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	request.Header.Add("Cookie", cookieValue)
	request.Header.Add("Content-Type", "application/json")
	_, err = client.Do(request)
	client.CloseIdleConnections()
	if err != nil {
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		log.Println(err)
		return
	}
	// After edited it redirect to the private information page
	http.Redirect(w, r, "/privatePage/", http.StatusFound)
}

func passwordHandler(w http.ResponseWriter, r *http.Request) {
	// Get cookie
	cookie, err := r.Cookie("V")
	cookieValue := strings.TrimPrefix(cookie.Value, "cookie=")
	// Prefetch info to render pages from backend API
	p, err := readProfile(cookieValue)
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
	tokenData, err := json.Marshal(token)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	tokenBuffer := string(tokenData)
	tokenPayload := strings.NewReader(tokenBuffer)
	// Get token request
	getTokenURL := "http://localhost:8080/v1/tokens"
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
	client.CloseIdleConnections()
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	updatePassword := UpdatePassword{}
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
		http.Redirect(w, r, "/home/", http.StatusFound)
		return
	}
	cookieValue := strings.TrimPrefix(cookie.Value, "cookie=")
	originalProfile, err := readProfile(cookieValue)
	if err != nil {
		log.Println("login expire, log in again", err)
		http.Redirect(w, r, "/home/", http.StatusFound)
		return
	}
	if newPassword != passwordConfirm {
		log.Println("password does not match")
		http.Redirect(w, r, "/password/", http.StatusFound)
		return
	}
	updatePassword := LogInfo{}
	updatePassword.Username = originalProfile.Username
	updatePassword.Password = newPassword
	client := &http.Client{}
	// Json format transformation
	jsonData, err := json.Marshal(updatePassword)

	Data := string(jsonData)
	payload := strings.NewReader(Data)
	// Send http request
	link := "http://localhost:8080/v1/accounts/@me?token=" + token
	request, err := http.NewRequest("PUT", link, payload)
	if err != nil {
		log.Println("request failed")
		http.Redirect(w, r, "/password/", http.StatusFound)
		return
	}
	request.Header.Add("Cookie", cookieValue)
	request.Header.Add("Content-Type", "application/json")
	response, err := client.Do(request)
	if err != nil {
		log.Println("request failed")
		http.Redirect(w, r, "/password/", http.StatusFound)
		return
	}
	if response.StatusCode == 500 {
		log.Println("Error Occur when changing password", err)
		http.Redirect(w, r, "/password/", http.StatusFound)
		return
	}
	client.CloseIdleConnections()
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/password/", http.StatusFound)
		return
	}
	http.Redirect(w, r, "/privatePage/", http.StatusFound)
	return
}

//
func loginHandler(w http.ResponseWriter, r *http.Request) {
	// Get user login in information
	username := r.FormValue("username")
	password := r.FormValue("password")
	// Encode the log in data to the json format payload
	user := LogInfo{}
	user.Username = username
	user.Password = password
	userData, err := json.Marshal(user)
	userString := string(userData)
	payload := strings.NewReader(userString)
	response, err := http.Post("http://localhost:8080/v1/sessions/", "application/json", payload)
	if err != nil {
		log.Println("login Failure", err)
		http.Redirect(w, r, "/loginError/", http.StatusFound)
		return
	}
	// Get token from login response from backend
	token := response.Header.Get("Set-Cookie")
	// Set the cookies to the browser
	Cookie := http.Cookie{Name: "V",
		Value:    token,
		Path:     "/",
		HttpOnly: true}
	http.SetCookie(w, &Cookie)
	http.Redirect(w, r, "/privatePage/", http.StatusFound)
	return
}

//
func registerHandler(w http.ResponseWriter, r *http.Request) {
	p := Profile{}
	renderTemplate(w, "register", &p)
}

//
func privateHandler(w http.ResponseWriter, r *http.Request) {
	// Read cookie from browser
	cookie, _ := r.Cookie("V")
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
	var pageInfo = Profile{}
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
	var pageinfo Profile
	pageinfo.Username = r.FormValue("username")
	pageinfo.Firstname = r.FormValue("firstname")
	pageinfo.Lastname = r.FormValue("lastname")
	pageinfo.Description = r.FormValue("description")
	pageinfo.Password = r.FormValue("password")
	pageinfo.Email = r.FormValue("email")
	pageinfo.Verified = "true"
	// Read information from frontend page
	newUser, err := json.Marshal(pageinfo)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/home/", http.StatusFound)
		return
	}
	Data := string(newUser)
	payload := strings.NewReader(Data)
	// Encode data to Json
	_, err = http.Post("http://localhost:8080/v1/accounts/", "application/json", payload)
	if err != nil {
		log.Println("error occur when creating account", err)
		http.Redirect(w, r, "/create/", http.StatusFound)
		return
	}
	// Send the request to create a new account

	user := LogInfo{}
	user.Username = pageinfo.Username
	user.Password = pageinfo.Password
	userData, err := json.Marshal(user)
	userString := string(userData)
	payload = strings.NewReader(userString)
	response, err := http.Post("http://localhost:8080/v1/sessions/", "application/json", payload)
	if err != nil {
		log.Println(err)
		http.Redirect(w, r, "/home/", http.StatusFound)
		return
	}
	// Log in with newly created account information
	// Get token from login response from backend
	token := response.Header.Get("Set-Cookie")
	// Set the cookie to the browser
	Cookie := http.Cookie{Name: "V",
		Value:    token,
		Path:     "/",
		HttpOnly: true}
	http.SetCookie(w, &Cookie)
	// After log in, redirect to the personal private page
	http.Redirect(w, r, "/privatePage/", http.StatusFound)
}

// Handle the login page
func homeHandler(w http.ResponseWriter, r *http.Request) {
	p := Profile{}
	renderTemplate(w, "login", &p)
}

// Handle error when having wrong password and let user to re-enter password
func errorPasswordHandler(w http.ResponseWriter, r *http.Request) {
	p := Profile{}
	renderTemplate(w, "loginError", &p)
}

// When user need to log out, this handler would erase the cookie to clean up the log in status.
func logoutHandler(w http.ResponseWriter, r *http.Request) {
	logOutCookie := http.Cookie{Name: "V",
		Path:   "/",
		MaxAge: -1}
	http.SetCookie(w, &logOutCookie)
	http.Redirect(w, r, "/home/", http.StatusFound)
}

// Load html template
var templates = template.Must(template.ParseFiles("template/edit.html", "template/accounts.html", "template/register.html", "template/login.html", "template/public_profile.html", "template/profile.html", "template/loginError.html", "template/changePassword.html"))

//
func renderTemplate(w http.ResponseWriter, tmpl string, p *Profile) {
	err := templates.ExecuteTemplate(w, tmpl+".html", p)
	if err != nil {
		log.Println(err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

func renderTemplateDescription(w http.ResponseWriter, tmpl string, p *UpdateDescription) {
	err := templates.ExecuteTemplate(w, tmpl+".html", p)
	if err != nil {
		log.Println(err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

func renderTemplatePassword(w http.ResponseWriter, tmpl string, p *UpdatePassword) {
	err := templates.ExecuteTemplate(w, tmpl+".html", p)
	if err != nil {
		log.Println(err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

// Regular expression to avoid illegal request
var validPath = regexp.MustCompile("^(/(edit|accounts|home)/([a-zA-Z0-9]+))|(/(login|home|create|privatePage|register|logout|save|loginError|password)/)$")

//
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

func makeHandlerNoParameter(fn func(http.ResponseWriter, *http.Request)) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		fn(w, r)
	}
}

//
func main() {
	http.HandleFunc("/accounts/", makeHandler(accountsHandler))
	http.HandleFunc("/edit/", makeHandler(editHandler))
	http.HandleFunc("/save/", makeHandler(saveEditedInfo))
	http.HandleFunc("/register/", makeHandlerNoParameter(registerHandler))
	http.HandleFunc("/create/", makeHandlerNoParameter(createHandler))
	http.HandleFunc("/login/", makeHandlerNoParameter(loginHandler))
	http.HandleFunc("/home/", makeHandlerNoParameter(homeHandler))
	http.HandleFunc("/privatePage/", makeHandlerNoParameter(privateHandler))
	http.HandleFunc("/logout/", makeHandlerNoParameter(logoutHandler))
	http.HandleFunc("/loginError/", makeHandlerNoParameter(errorPasswordHandler))
	http.HandleFunc("/password/", makeHandlerNoParameter(passwordHandler))
	http.HandleFunc("/passwordsave/", makeHandlerNoParameter(passwordSaveHandler))
	log.Println(http.ListenAndServe(":5000", nil))
}
