package com.example.synchronizationtestapp;

public class Person {
    private String first_name;
    private String last_name;
    private int age;
    private String phone_number;
    private String email;


    public Person(String first_name, String last_name, int age, String phone_number, String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
        this.phone_number = phone_number;
        this.email = email;
    }

    @Override
    public String toString() {
        return first_name + " " + last_name + " age: " + age + " Phone#: " + phone_number + " Email: " + email;

    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
