package com.example.ash.reduxlmplementation;

/**
 * Created by Shevchuk Anton on 31.05.2016.
 */
public class Person {
    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public int getPhotoId() {
        return photoId;
    }

    String name;
    String age;
    int photoId;

   public Person(String name, String age, int photoId) {
        this.name = name;
        this.age = age;
        this.photoId = photoId;
    }
}
