package org.example.bookstore;
import java.util.HashMap;
import java.util.Map;

import io.grpc.stub.StreamObserver;

public class BookServiceImpl extends BookServiceGrpc.BookServiceImplBase {

    private final Map<String, Book> bookStore = new HashMap<>();

    @Override
    public void addBook(AddBookRequest request, StreamObserver<AddBookResponse> responseObserver) {
        Book book = request.getBook();
        String isbn = book.getIsbn();
        if (bookStore.containsKey(isbn)) {
            AddBookResponse response = AddBookResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Book already exists")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }
        bookStore.put(isbn, book);
        AddBookResponse response = AddBookResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Book added successfully")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateBook(UpdateBookRequest request, StreamObserver<UpdateBookResponse> responseObserver) {
        String isbn = request.getIsbn();
        if (!bookStore.containsKey(isbn)) {
            UpdateBookResponse response = UpdateBookResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Book not found")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }
        bookStore.put(isbn, request.getBook());
        UpdateBookResponse response = UpdateBookResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Book updated successfully")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteBook(DeleteBookRequest request, StreamObserver<DeleteBookResponse> responseObserver) {
        String isbn = request.getIsbn();
        if (!bookStore.containsKey(isbn)) {
            DeleteBookResponse response = DeleteBookResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Book not found")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }
        bookStore.remove(isbn);
        DeleteBookResponse response = DeleteBookResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Book deleted successfully")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getBooks(GetBooksRequest request, StreamObserver<GetBooksResponse> responseObserver) {
        GetBooksResponse.Builder responseBuilder = GetBooksResponse.newBuilder();
        responseBuilder.addAllBooks(bookStore.values());
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
