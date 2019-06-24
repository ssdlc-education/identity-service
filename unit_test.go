package main

import (
	"net/http"
	"net/url"
	"testing"
)



func TestCreateAccount(t *testing.T) {
	resp, err := http.PostForm("http://localhost:5000/create/", url.Values{"username":{"Henry123"},"firstname":{"Henry"},"lastname":{"Zhang"},"email":{"henryzhang@gmail.com"},"description":{"this is a new account"},"verified":{"true"},"password":{"PASSWORD"}})
	if err != nil && resp.StatusCode% 100 != 2{
		t.Fail()
	}
}
func TestLogin(t *testing.T) {
	resp, err := http.PostForm("http://localhost:5000/login/", url.Values{"username":{"Henry123"},"password":{"PASSWORD"}})
	if err != nil && resp.StatusCode% 100 != 2{
		t.Fail()
	}
}

func TestAccessProfile(t *testing.T) {
	link := "http://localhost:5000/privatePage/"
	resp, err := http.Get(link)
	if err != nil && resp.StatusCode% 100 != 2{
		t.Fail()
	}
}
func TestEdit(t *testing.T) {
	resp, err := http.PostForm("http://localhost:5000/save/Henry123", url.Values{"email": {"henry123@yahoo.com"}, "description": {"Hello from your new Email"}})
	if err != nil && resp.StatusCode% 100 != 2{
		t.Fail()
	}
}

func TestGetPublicProfile(t *testing.T) {
	resp, err := http.Get("http://localhost:5000/accounts/Henry123")
	if err != nil && resp.StatusCode% 100 != 2{
		t.Fail()
	}
}



