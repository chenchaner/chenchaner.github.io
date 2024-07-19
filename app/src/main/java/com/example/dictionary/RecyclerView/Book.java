package com.example.dictionary.RecyclerView;

public class Book {
    String bookId;
    String name;
    String number;
    String courseNumber;
    public Book(String name,String number,String bookId,String courseNumber){
        this.name = name;
        this.number = number;
        this.bookId = bookId;
        this.courseNumber = courseNumber;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getName() {
        return name;
    }

    public String getBookId() {
        return bookId;
    }

    public String getNumber() {
        return number;
    }
}
