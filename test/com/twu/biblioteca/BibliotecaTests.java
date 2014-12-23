package com.twu.biblioteca;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.*;
import java.io.*;

public class BibliotecaTests {

    BibliotecaApp library;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    public void setUp(){
        library = new BibliotecaApp();
        library.addBook("Harry Potter and the Philosopher's Stone","JK Rowling","1997");
        library.addBook("World War Z","Max Brooks","2006");
        library.addBook("Artificial Intelligence","Peter Norvig and Stuart J. Russell","1994");
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void testWelcomeMessage(){
        setUp();
        library.welcomeUser();
        assertEquals("Welcome to Biblioteca\n", outContent.toString());
    }

    @Test
    public void testBooksInLibrary(){
        setUp();
        ArrayList<Book> correctList = new ArrayList<Book>();
        correctList.add(new Book("Harry Potter and the Philosopher's Stone","JK Rowling","1997"));
        correctList.add(new Book("World War Z","Max Brooks","2006"));
        correctList.add(new Book("Artificial Intelligence","Peter Norvig and Stuart J. Russell","1994"));
        int idx = 0;
        for(Book b:correctList){
            assertEquals(b.getTitle(), library.getBook(idx).getTitle());
            assertEquals(b.getAuthor(), library.getBook(idx).getAuthor());
            assertEquals(b.getYear(), library.getBook(idx).getYear());
            idx+=1;
        }
    }

    @Test
    public void testBookDetails(){
        setUp();
        Book chosenBook = library.getBook(1);
        assertEquals("World War Z",chosenBook.getTitle());
        assertEquals("Max Brooks",chosenBook.getAuthor());
        assertEquals("2006",chosenBook.getYear());
        assertEquals("Available",chosenBook.getStatus());
    }

    @Test
    public void testMenuOptions(){
        setUp();
        library.displayMenuOptions();
        assertEquals("1. List Books\n2. Checkout Book\n3. Return Book\nQ. Quit\n",outContent.toString());
    }

    @Test
    public void testValidMenuOptionListBooks(){
        setUp();
        library.selectOption("1");
        assertEquals(
                "   0 Harry Potter and the Philosopher's Stone           JK Rowling" +
                "                                         1997\n" +
                "   1 World War Z                                        Max Brooks" +
                        "                                         2006\n" +
                "   2 Artificial Intelligence                            Peter Norvig and Stuart J. Russell" +
                        "                 1994\n",outContent.toString());
    }

    @Test
    public void testInvalidMenuOption(){
        setUp();
        library.selectOption("123");
        assertEquals("Select a valid option!\n", outContent.toString());
    }

    @Test
    public void testQuit(){
        setUp();
        library.selectOption("Q");
        assertEquals("Quitting Biblioteca",outContent.toString());
    }

    @Test
    public void testCheckoutBook(){
        setUp();
        String data = "1";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        Book checkoutBook = library.checkout();
        assertEquals(new Book("World War Z","Max Brooks","2006"),checkoutBook);
        assertEquals("Unavailable",checkoutBook.getStatus());
        assertEquals(2,library.sizeAvailable());
        assertEquals("Select a book (id): 1 Thank you! Enjoy the book\n",outContent.toString());
    }

    @Test
    public void testCheckoutMultipleBooks(){
        setUp();
        String data = "1";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        library.checkout();
        assertEquals(2,library.sizeAvailable());
        assertEquals("Available",library.getBook(0).getStatus());
        assertEquals("Unavailable",library.getBook(1).getStatus());
        assertEquals("Available",library.getBook(2).getStatus());
        data = "2";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        library.checkout();
        assertEquals(1,library.sizeAvailable());
        assertEquals("Available",library.getBook(0).getStatus());
        assertEquals("Unavailable",library.getBook(1).getStatus());
        assertEquals("Unavailable",library.getBook(2).getStatus());
    }

    @Test
    public void testUnsuccessfulCheckout(){
        setUp();
        String data = "123";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        library.checkout();
        assertEquals("Select a book (id):  That book is not available.\n",outContent.toString());
    }

    @Test
    public void testReturnBook(){
        setUp();
        String data = "1";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        library.checkout();
        Book returnedBook = library.returnBook();
        assertEquals(new Book("World War Z","Max Brooks","2006"),returnedBook);
        assertEquals("Available",returnedBook.getStatus());
        assertEquals(3,library.sizeAvailable());
        assertEquals("Thank you! Enjoy the book\nThank you for returning the book.\n",outContent.toString());
    }

    @Test
    public void testReturnMultipleBooks(){
        setUp();
        Book checkBook1 = library.checkoutBook(1);
        Book checkBook2 = library.checkoutBook(2);
        String data = "1";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        Book returnedBook1 = library.returnBook();
        assertEquals("Available", returnedBook1.getStatus());
        assertEquals("Unavailable",checkBook2.getStatus());
        assertEquals(2,library.sizeAvailable());
        data = "2";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        Book returnedBook2 = library.returnBook();
        assertEquals("Available", returnedBook1.getStatus());
        assertEquals("Available",returnedBook1.getStatus());
        assertEquals(3,library.sizeAvailable());
    }

    @Test
    public void testUnsuccessfulReturn(){
        setUp();
        String data = "123";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        library.returnBook();
        assertEquals("Input a book (id):  That is not a valid book to return.\n", outContent.toString());
        assertEquals(3,library.sizeAvailable());
    }

    @Test
    public void testUnsuccessfulReturnNotBookId(){
        setUp();
        String data = "Not even a number";
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        library.returnBook();

        assertEquals("Input a book(id):  That is not a valid book to return.\n", outContent.toString());
        assertEquals(3,library.sizeAvailable());
    }
}
