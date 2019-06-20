package main

import (
	"net/http"
	"net/url"
	"testing"
	"fmt"
	//"html/template"
)


func TestUnitStart(t *testing.T){
	fmt.Println("Unit Test Here")
	t.Log("Unit Test Running")
}

func TestReadFiles(t *testing.T){
	_, err := readLines("fakedb/oscarwilde.txt")
	if err != nil {
		t.Fail()
	}
	t.Log("Read File successed")
}

func TestAccountHandler(t *testing.T){
	http.HandleFunc("/accounts/", makeHandler(accountsHandler))
	_, err := http.Get("http://localhost:5000/accounts/oscarwilde1")
	if err != nil {
		fmt.Println("here some trouble")
		t.Fail()
	}
	//if accountResponse == nil {
	//	t.Fail()
	//}gi

}

func TestSendHandler(t *testing.T){
	http.HandleFunc("/edit/",makeHandler(editHandler))
	_, err := http.PostForm("http://localhost:5000/Send", url.Values{"username":{"XimingC"},"firstname":{"Ximing"},"lastname":{"Chen"},"email":{" cxm@gmail.com"},"createTs":{"sjjoajo"},"updateTs":{"ashihf"}, "description":{"this is fine"}})
	if err != nil {
		fmt.Println("some trouble on send request")
		t.Fail()
	}

}


func TestEditHandler(t *testing.T){
	http.HandleFunc("/save/",makeHandler(saveHandler))
	_,err := http.PostForm("http://localhost:5000/Send", url.Values{"username":{"XimingC"},"firstname":{"Ximing"},"lastname":{"Chen"},"email":{" cxm@gmail.com"},"createTs":{"sjjoajo"},"updateTs":{"ashihf"}, "description":{"this is fine"}})
	if err != nil {
		fmt.Println("some trouble on save request")
		t.Fail()
	}
}
func TestLoadPage(t *testing.T){
	_, err := loadPage("oscarwilde1")
	if err != nil{
		t.Fail()
	}
}

