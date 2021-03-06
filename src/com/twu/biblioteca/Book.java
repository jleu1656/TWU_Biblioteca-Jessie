package com.twu.biblioteca;

/**
 * Created by jessieleung on 23/12/14.
 */
public class Book extends MediaItem {

    private String author;
    private String currentBorrower;

    public Book(String bookTitle, String bookAuthor, String bookYear){
        super(bookTitle,bookYear);
        author = bookAuthor;
        currentBorrower = "None";
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Book){
            Book b = (Book) o;
            if(b.getTitle().equals(title) && b.getAuthor().equals(author) && b.getYear().equals(year)){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public void setCurrentBorrower(String libraryNumber){
        currentBorrower = libraryNumber;
    }

    public String getCurrentBorrower(){
        return currentBorrower;
    }

    public String getAuthor(){
        return author;
    }

}
