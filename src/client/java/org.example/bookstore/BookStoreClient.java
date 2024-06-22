package org.example.bookstore;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BookStoreClient {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        BookServiceGrpc.BookServiceBlockingStub blockingStub = BookServiceGrpc.newBlockingStub(channel);

        // Add a book
        Book book = Book.newBuilder()
                .setIsbn("1234")
                .setTitle("Test Book")
                .addAuthors("Author 1")
                .setPageCount(100)
                .build();
        AddBookRequest addRequest = AddBookRequest.newBuilder().setBook(book).build();
        AddBookResponse addResponse = blockingStub.addBook(addRequest);
        System.out.println(addResponse.getMessage());

        // Get all books
        GetBooksRequest getRequest = GetBooksRequest.newBuilder().build();
        GetBooksResponse getResponse = blockingStub.getBooks(getRequest);
        getResponse.getBooksList().forEach(System.out::println);

        // Update a book
        Book updatedBook = Book.newBuilder()
                .setIsbn("1234")
                .setTitle("Updated Test Book")
                .addAuthors("Author 1")
                .setPageCount(150)
                .build();
        UpdateBookRequest updateRequest = UpdateBookRequest.newBuilder()
                .setIsbn("1234")
                .setBook(updatedBook)
                .build();
        UpdateBookResponse updateResponse = blockingStub.updateBook(updateRequest);
        System.out.println(updateResponse.getMessage());

        // Delete a book
        DeleteBookRequest deleteRequest = DeleteBookRequest.newBuilder().setIsbn("1234").build();
        DeleteBookResponse deleteResponse = blockingStub.deleteBook(deleteRequest);
        System.out.println(deleteResponse.getMessage());

        channel.shutdown();
    }
}
