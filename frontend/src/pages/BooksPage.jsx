import { useEffect, useState } from "react";
import { apiRequest } from "../services/api";

export default function BooksPage() {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadBooks() {
    try {
      setLoading(true);
      setError("");

      const data = await apiRequest("/books");
      setBooks(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message || "Unable to load books");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadBooks();
  }, []);

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <p className="page-eyebrow">CATALOGUE</p>
          <h1>Books</h1>
          <p>Books currently available in Pustakaalay.</p>
        </div>

        <button className="secondary-button" onClick={loadBooks}>
          Refresh
        </button>
      </div>

      {loading && (
        <div className="state-card">
          Loading books...
        </div>
      )}

      {error && (
        <div className="state-card error-state">
          <strong>Could not load books</strong>
          <p>{error}</p>
        </div>
      )}

      {!loading && !error && books.length === 0 && (
        <div className="state-card">
          <h3>No books found</h3>
          <p>The catalogue is currently empty.</p>
        </div>
      )}

      {!loading && !error && books.length > 0 && (
        <div className="book-grid">
          {books.map((book) => (
            <article className="book-card" key={book.id}>
              <div className="book-cover">
                {book.coverImageUrl ? (
                  <img
                    src={book.coverImageUrl}
                    alt={book.title}
                    onError={(event) => {
                      event.currentTarget.style.display = "none";
                    }}
                  />
                ) : (
                  <span>📖</span>
                )}
              </div>

              <div className="book-card-content">
                <span className="book-id">BOOK #{book.id}</span>

                <h2>{book.title}</h2>

                <p className="book-author">
                  {book.authors?.length
                    ? book.authors
                        .map((author) =>
                          `${author.firstName ?? ""} ${author.lastName ?? ""}`.trim()
                        )
                        .join(", ")
                    : "Unknown author"}
                </p>

                <div className="book-meta">
                  {book.publicationYear && (
                    <span>{book.publicationYear}</span>
                  )}

                  {book.language && (
                    <span>{book.language}</span>
                  )}

                  {book.pages && (
                    <span>{book.pages} pages</span>
                  )}
                </div>

                {book.categories?.length > 0 && (
                  <div className="category-list">
                    {book.categories.map((category) => (
                      <span key={category.id}>
                        {category.name}
                      </span>
                    ))}
                  </div>
                )}

                <div className="book-details">
                  <div>
                    <span>ISBN</span>
                    <strong>{book.isbn || "N/A"}</strong>
                  </div>

                  <div>
                    <span>Publisher</span>
                    <strong>{book.publisher || "N/A"}</strong>
                  </div>
                </div>
              </div>
            </article>
          ))}
        </div>
      )}
    </div>
  );
}
