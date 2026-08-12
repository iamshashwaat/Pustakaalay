import { useEffect, useState } from "react";
import { apiRequest } from "../services/api";
import { useAuth } from "../context/AuthContext";

const emptyForm = {
  title: "",
  isbn: "",
  publisher: "",
  publicationYear: "",
  edition: "",
  language: "English",
  description: "",
  pages: "",
};

export default function BooksPage() {
  const { user } = useAuth();

  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState(emptyForm);
  const [saving, setSaving] = useState(false);
  const [formError, setFormError] = useState("");

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

  function handleChange(event) {
    const { name, value } = event.target;

    setForm((current) => ({
      ...current,
      [name]: value,
    }));
  }

  async function handleAddBook(event) {
    event.preventDefault();

    try {
      setSaving(true);
      setFormError("");

      await apiRequest("/books", {
        method: "POST",
        body: JSON.stringify({
          title: form.title.trim(),
          isbn: form.isbn.trim(),
          publisher: form.publisher.trim() || null,
          publicationYear: form.publicationYear
            ? Number(form.publicationYear)
            : null,
          edition: form.edition.trim() || null,
          language: form.language.trim() || null,
          description: form.description.trim() || null,
          pages: form.pages ? Number(form.pages) : null,
          authorIds: [],
          categoryIds: [],
        }),
      });

      setForm(emptyForm);
      setShowForm(false);

      await loadBooks();
    } catch (err) {
      setFormError(err.message || "Unable to add book");
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <p className="page-eyebrow">CATALOGUE</p>
          <h1>Books</h1>
          <p>Books currently available in Pustakaalay.</p>
        </div>

        <div className="page-actions">
          {user?.role === "ADMIN" && (
            <button
              className="primary-button"
              onClick={() => setShowForm(true)}
            >
              + Add Book
            </button>
          )}

          <button className="secondary-button" onClick={loadBooks}>
            Refresh
          </button>
        </div>
      </div>

      {showForm && user?.role === "ADMIN" && (
        <div className="form-card">
          <div className="form-card-header">
            <div>
              <p className="page-eyebrow">ADMIN</p>
              <h2>Add Book</h2>
            </div>

            <button
              className="close-button"
              onClick={() => {
                setShowForm(false);
                setFormError("");
              }}
            >
              ×
            </button>
          </div>

          <form className="book-form" onSubmit={handleAddBook}>
            <div className="form-grid">
              <div>
                <label>Title *</label>
                <input
                  name="title"
                  value={form.title}
                  onChange={handleChange}
                  required
                />
              </div>

              <div>
                <label>ISBN *</label>
                <input
                  name="isbn"
                  value={form.isbn}
                  onChange={handleChange}
                  required
                />
              </div>

              <div>
                <label>Publisher</label>
                <input
                  name="publisher"
                  value={form.publisher}
                  onChange={handleChange}
                />
              </div>

              <div>
                <label>Publication Year</label>
                <input
                  name="publicationYear"
                  type="number"
                  min="1"
                  max="2100"
                  value={form.publicationYear}
                  onChange={handleChange}
                />
              </div>

              <div>
                <label>Edition</label>
                <input
                  name="edition"
                  value={form.edition}
                  onChange={handleChange}
                />
              </div>

              <div>
                <label>Language</label>
                <input
                  name="language"
                  value={form.language}
                  onChange={handleChange}
                />
              </div>

              <div>
                <label>Pages</label>
                <input
                  name="pages"
                  type="number"
                  min="1"
                  value={form.pages}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div>
              <label>Description</label>
              <textarea
                name="description"
                rows="4"
                value={form.description}
                onChange={handleChange}
              />
            </div>

            {formError && (
              <div className="error-message">
                {formError}
              </div>
            )}

            <div className="form-actions">
              <button
                type="button"
                className="secondary-button"
                onClick={() => {
                  setShowForm(false);
                  setFormError("");
                }}
              >
                Cancel
              </button>

              <button
                type="submit"
                className="primary-button"
                disabled={saving}
              >
                {saving ? "Adding..." : "Add Book"}
              </button>
            </div>
          </form>
        </div>
      )}

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
                <span>📖</span>
              </div>

              <div className="book-card-content">
                <span className="book-id">BOOK #{book.id}</span>

                <h2>{book.title}</h2>

                <p className="book-author">
                  {book.authors?.length
                    ? book.authors
                        .map((author) =>
                          `${author.firstName ?? ""} ${
                            author.lastName ?? ""
                          }`.trim()
                        )
                        .join(", ")
                    : "No author linked"}
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
