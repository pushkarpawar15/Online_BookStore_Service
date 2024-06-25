package org.example.bookstore;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class BookStoreClientInteractive {

    private final BookServiceGrpc.BookServiceBlockingStub blockingStub;

    public BookStoreClientInteractive(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = BookServiceGrpc.newBlockingStub(channel);
    }

    public void addBook(String isbn, String title, String author, int pageCount) {
        Book book = Book.newBuilder()
                .setIsbn(isbn)
                .setTitle(title)
                .addAuthors(author)
                .setPageCount(pageCount)
                .build();
        AddBookRequest request = AddBookRequest.newBuilder().setBook(book).build();
        AddBookResponse response = blockingStub.addBook(request);
        System.out.println("Add Book Response: " + response.getMessage());
    }

    public void updateBook(String isbn, String title, String author, int pageCount) {
        Book book = Book.newBuilder()
                .setIsbn(isbn)
                .setTitle(title)
                .addAuthors(author)
                .setPageCount(pageCount)
                .build();
        UpdateBookRequest request = UpdateBookRequest.newBuilder()
                .setIsbn(isbn)
                .setBook(book)
                .build();
        UpdateBookResponse response = blockingStub.updateBook(request);
        System.out.println("Update Book Response: " + response.getMessage());
    }

    public void deleteBook(String isbn) {
        DeleteBookRequest request = DeleteBookRequest.newBuilder().setIsbn(isbn).build();
        DeleteBookResponse response = blockingStub.deleteBook(request);
        System.out.println("Delete Book Response: " + response.getMessage());
    }

    public void getBooks() {
        GetBooksRequest request = GetBooksRequest.newBuilder().build();
        GetBooksResponse response = blockingStub.getBooks(request);
        response.getBooksList().forEach(book -> {
            System.out.println("ISBN: " + book.getIsbn());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Authors: " + String.join(", ", book.getAuthorsList()));
            System.out.println("Page Count: " + book.getPageCount());
            System.out.println("-----------------------------");
        });
    }

    public static void main(String[] args) {
        BookStoreClientInteractive client = new BookStoreClientInteractive("localhost", 8080);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an operation:");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Delete Book");
            System.out.println("4. Get Books");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter ISBN: ");
                    String isbnAdd = scanner.nextLine();
                    System.out.print("Enter Title: ");
                    String titleAdd = scanner.nextLine();
                    System.out.print("Enter Author: ");
                    String authorAdd = scanner.nextLine();
                    System.out.print("Enter Page Count: ");
                    int pageCountAdd = Integer.parseInt(scanner.nextLine());
                    client.addBook(isbnAdd, titleAdd, authorAdd, pageCountAdd);
                    break;
                case "2":
                    System.out.print("Enter ISBN: ");
                    String isbnUpdate = scanner.nextLine();
                    System.out.print("Enter Title: ");
                    String titleUpdate = scanner.nextLine();
                    System.out.print("Enter Author: ");
                    String authorUpdate = scanner.nextLine();
                    System.out.print("Enter Page Count: ");
                    int pageCountUpdate = Integer.parseInt(scanner.nextLine());
                    client.updateBook(isbnUpdate, titleUpdate, authorUpdate, pageCountUpdate);
                    break;
                case "3":
                    System.out.print("Enter ISBN: ");
                    String isbnDelete = scanner.nextLine();
                    client.deleteBook(isbnDelete);
                    break;
                case "4":
                    client.getBooks();
                    break;
                case "5":
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
