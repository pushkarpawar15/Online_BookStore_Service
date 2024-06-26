package org.example.bookstore;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class BookServiceImplTest {

    private ManagedChannel channel;
    private BookServiceGrpc.BookServiceBlockingStub blockingStub;

    @Before
    public void setup() {
        channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        blockingStub = BookServiceGrpc.newBlockingStub(channel);

        Book book = Book.newBuilder()
                .setIsbn("test-isbn")
                .setTitle("Test Book")
                .addAuthors("Test Author")
                .setPageCount(123)
                .build();
        AddBookRequest addRequest = AddBookRequest.newBuilder().setBook(book).build();
        blockingStub.addBook(addRequest);
    }

    @After
    public void teardown() {
        try {
            DeleteBookRequest deleteRequest = DeleteBookRequest.newBuilder().setIsbn("test-isbn").build();
            blockingStub.deleteBook(deleteRequest);
        } catch (Exception e) {
            // Book might already be deleted, ignore
        }
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
    public void updateBookTest() {
        // Create an updated book
        Book updatedBook = Book.newBuilder()
                .setIsbn("test-isbn")
                .setTitle("Updated Test Book")
                .addAuthors("Updated Test Author")
                .setPageCount(456)
                .build();
        UpdateBookRequest updateRequest = UpdateBookRequest.newBuilder()
                .setIsbn("test-isbn")
                .setBook(updatedBook)
                .build();
        UpdateBookResponse updateResponse = blockingStub.updateBook(updateRequest);

        // Verify the update response
        assertTrue(updateResponse.getSuccess());
        assertEquals("Book updated successfully", updateResponse.getMessage());

        // Verify the updated book details
        GetBooksRequest getRequest = GetBooksRequest.newBuilder().build();
        GetBooksResponse getResponse = blockingStub.getBooks(getRequest);

        Book fetchedBook = getResponse.getBooksList().stream()
                .filter(book -> book.getIsbn().equals("test-isbn"))
                .findFirst()
                .orElse(null);

        assertEquals("Updated Test Book", fetchedBook.getTitle());
        assertEquals("Updated Test Author", fetchedBook.getAuthors(0));
        assertEquals(456, fetchedBook.getPageCount());
    }

    @Test
    public void deleteBookTest() {
        // Delete the test book
        DeleteBookRequest deleteRequest = DeleteBookRequest.newBuilder().setIsbn("test-isbn").build();
        DeleteBookResponse deleteResponse = blockingStub.deleteBook(deleteRequest);

        // Verify the delete response
        assertTrue(deleteResponse.getSuccess());
        assertEquals("Book deleted successfully", deleteResponse.getMessage());

        // Verify the book no longer exists
        GetBooksRequest getRequest = GetBooksRequest.newBuilder().build();
        GetBooksResponse getResponse = blockingStub.getBooks(getRequest);

        boolean bookExists = getResponse.getBooksList().stream()
                .anyMatch(book -> book.getIsbn().equals("test-isbn"));

        assertFalse(bookExists);
    }

    @Test
    public void getBooksTest() {
        GetBooksRequest request = GetBooksRequest.newBuilder().build();
        GetBooksResponse response = blockingStub.getBooks(request);
        assertEquals(true,response.getBooksCount()>0);
    }
}
