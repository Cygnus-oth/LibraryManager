package library;
import java.util.ArrayList;

public class Library {
	private ArrayList<Book> books = new ArrayList<>();

    // Add book
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book added successfully!");
    }

    public void deleteBook(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                books.remove(book);
                System.out.println("Book deleted successfully!");
                return;
            }
        }
        System.out.println("Book not found.");
    }

    public void searchBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                book.displayBook();
                return;
            }
        }
        System.out.println("Book not found.");
    }

    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }

        for (Book book : books) {
            book.displayBook();
        }
    }
}
