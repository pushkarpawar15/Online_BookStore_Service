package org.example.bookstore;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BookServiceImplTest {

    private ManagedChannel channel;
    private BookServiceGrpc.BookServiceBlockingStub blockingStub;

    @Before
    public void setup() {
        channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        blockingStub = BookServiceGrpc.newBlockingStub(channel);
    }

    @After
    public void teardown() {
        channel.shutdown();
    }

    @Test
    public void addBookTest() {
        Book book = Book.newBuilder()
                .setIsbn("1234")
                .setTitle("Test Book")
                .addAuthors("Author 1")
                .setPageCount(100)
                .build();
        AddBookRequest request = AddBookRequest.newBuilder().setBook(book).build();
        AddBookResponse response = blockingStub.addBook(request);
        assertTrue(response.getSuccess());
        assertEquals("Book added successfully", response.getMessage());
    }
    @Test
    public void addBookTest1() {
        Book book = Book.newBuilder()
                .setIsbn("12345")
                .setTitle("Test Book5")
                .addAuthors("Author 15")
                .setPageCount(100)
                .build();
        AddBookRequest request = AddBookRequest.newBuilder().setBook(book).build();
        AddBookResponse response = blockingStub.addBook(request);
        assertTrue(response.getSuccess());
        assertEquals("Book added successfully", response.getMessage());
    }

    @Test
    public void getBooksTest() {
        GetBooksRequest request = GetBooksRequest.newBuilder().build();
        GetBooksResponse response = blockingStub.getBooks(request);
        assertEquals(2, response.getBooksCount());
    }

    // Additional tests for update and delete
}
