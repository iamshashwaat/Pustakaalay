import { useEffect, useState } from "react";
import { apiRequest } from "../services/api";
import { useAuth } from "../context/AuthContext";

const emptyBookForm = {
  title: "",
  isbn: "",
  publisher: "",
  publicationYear: "",
  edition: "",
  language: "English",
  description: "",
  pages: "",
};

const emptyCopyForm = {
  barcode: "",
  acquisitionDate: new Date().toISOString().slice(0, 10),
  price: "",
  status: "AVAILABLE",
  conditionStatus: "GOOD",
  location: "",
};

export default function BooksPage() {
  const { user } = useAuth();

  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [mode, setMode] = useState(null);
  const [selectedBook, setSelectedBook] = useState(null);

  const [bookForm, setBookForm] = useState(emptyBookForm);
  const [copyForm, setCopyForm] = useState(emptyCopyForm);

  const [saving, setSaving] = useState(false);
  const [formError, setFormError] = useState("");
  const [success, setSuccess] = useState("");

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

  function closeForm() {
    setMode(null);
    setSelectedBook(null);
    setBookForm(emptyBookForm);
    setCopyForm(emptyCopyForm);
    setFormError("");
  }

  function openAddBook() {
    setSelectedBook(null);
    setBookForm(emptyBookForm);
    setMode("add");
    setFormError("");
    setSuccess("");
  }

  function openEditBook(book) {
    setSelectedBook(book);

    setBookForm({
      title: book.title || "",
      isbn: book.isbn || "",
      publisher: book.publisher || "",
      publicationYear: book.publicationYear || "",
      edition: book.edition || "",
      language: book.language || "",
      description: book.description || "",
      pages: book.pages || "",
    });

    setMode("edit");
    setFormError("");
    setSuccess("");
  }

  function openAddCopy(book) {
    setSelectedBook(book);
    setCopyForm(emptyCopyForm);
    setMode("copy");
    setFormError("");
    setSuccess("");
  }

  function handleBookChange(event) {
    const { name, value } = event.target;

    setBookForm((current) => ({
      ...current,
      [name]: value,
    }));
  }

  function handleCopyChange(event) {
    const { name, value } = event.target;

    setCopyForm((current) => ({
      ...current,
      [name]: value,
    }));
  }

  function buildBookPayload() {
    return {
      title: bookForm.title.trim(),
      isbn: bookForm.isbn.trim(),
      publisher: bookForm.publisher.trim() || null,
      publicationYear: bookForm.publicationYear
        ? Number(bookForm.publicationYear)
        : null,
      edition: bookForm.edition.trim() || null,
      language: bookForm.language.trim() || null,
      description: bookForm.description.trim() || null,
      pages: bookForm.pages ? Number(bookForm.pages) : null,

      authorIds:
        selectedBook?.authors?.map((author) => author.id) || [],

      categoryIds:
        selectedBook?.categories?.map((category) => category.id) || [],
    };
  }

  async function handleBookSubmit(event) {
    event.preventDefault();

    try {
      setSaving(true);
      setFormError("");

      if (mode === "edit") {
        await apiRequest(`/books/${selectedBook.id}`, {
          method: "PUT",
          body: JSON.stringify(buildBookPayload()),
        });

        setSuccess("Book updated successfully.");
      } else {
        await apiRequest("/books", {
          method: "POST",
          body: JSON.stringify(buildBookPayload()),
        });

        setSuccess("Book added successfully.");
      }

      closeForm();
      await loadBooks();
    } catch (err) {
      setFormError(err.message || "Unable to save book");
    } finally {
      setSaving(false);
    }
  }

  async function handleAddCopy(event) {
    event.preventDefault();

    try {
      setSaving(true);
      setFormError("");

      await apiRequest("/book-copies", {
        method: "POST",
        body: JSON.stringify({
          bookId: selectedBook.id,
          barcode: copyForm.barcode.trim(),
          acquisitionDate: copyForm.acquisitionDate || null,
          price: copyForm.price ? Number(copyForm.price) : null,
          status: copyForm.status,
          conditionStatus: copyForm.conditionStatus,
          location: copyForm.location.trim() || null,
        }),
      });

      setSuccess(
        `Physical copy added for "${selectedBook.title}".`
      );

      closeForm();
    } catch (err) {
      setFormError(err.message || "Unable to add physical copy");
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete(book) {
    const confirmed = window.confirm(
      `Delete "${book.title}"?\n\nThis cannot be undone.`
    );

    if (!confirmed) {
      return;
    }

    try {
      setError("");
      setSuccess("");

      await apiRequest(`/books/${book.id}`, {
        method: "DELETE",
      });

      setSuccess(`"${book.title}" deleted successfully.`);
      await loadBooks();
    } catch (err) {
      setError(
        err.message ||
          "Unable to delete book. Remove its physical copies first."
      );
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
              onClick={openAddBook}
            >
              + Add Book
            </button>
          )}

          <button
            className="secondary-button"
            onClick={loadBooks}
          >
            Refresh
          </button>
        </div>
      </div>

      {success && (
        <div className="success-message">
          {success}
        </div>
      )}

      {(mode === "add" || mode === "edit") &&
        user?.role === "ADMIN" && (
          <div className="form-card">
            <div className="form-card-header">
              <div>
                <p className="page-eyebrow">ADMIN</p>
                <h2>
                  {mode === "edit"
                    ? "Edit Book"
                    : "Add Book"}
                </h2>
              </div>

              <button
                className="close-button"
                onClick={closeForm}
              >
                ×
              </button>
            </div>

            <form
              className="book-form"
              onSubmit={handleBookSubmit}
            >
              <div className="form-grid">
                <div>
                  <label>Title *</label>
                  <input
                    name="title"
                    value={bookForm.title}
                    onChange={handleBookChange}
                    required
                  />
                </div>

                <div>
                  <label>ISBN *</label>
                  <input
                    name="isbn"
                    value={bookForm.isbn}
                    onChange={handleBookChange}
                    required
                  />
                </div>

                <div>
                  <label>Publisher</label>
                  <input
                    name="publisher"
                    value={bookForm.publisher}
                    onChange={handleBookChange}
                  />
                </div>

                <div>
                  <label>Publication Year</label>
                  <input
                    name="publicationYear"
                    type="number"
                    min="1"
                    max="2100"
                    value={bookForm.publicationYear}
                    onChange={handleBookChange}
                  />
                </div>

                <div>
                  <label>Edition</label>
                  <input
                    name="edition"
                    value={bookForm.edition}
                    onChange={handleBookChange}
                  />
                </div>

                <div>
                  <label>Language</label>
                  <input
                    name="language"
                    value={bookForm.language}
                    onChange={handleBookChange}
                  />
                </div>

                <div>
                  <label>Pages</label>
                  <input
                    name="pages"
                    type="number"
                    min="1"
                    value={bookForm.pages}
                    onChange={handleBookChange}
                  />
                </div>
              </div>

              <div>
                <label>Description</label>
                <textarea
                  name="description"
                  rows="4"
                  value={bookForm.description}
                  onChange={handleBookChange}
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
                  onClick={closeForm}
                >
                  Cancel
                </button>

                <button
                  type="submit"
                  className="primary-button"
                  disabled={saving}
                >
                  {saving
                    ? "Saving..."
                    : mode === "edit"
                    ? "Save Changes"
                    : "Add Book"}
                </button>
              </div>
            </form>
          </div>
        )}

      {mode === "copy" &&
        user?.role === "ADMIN" &&
        selectedBook && (
          <div className="form-card">
            <div className="form-card-header">
              <div>
                <p className="page-eyebrow">
                  INVENTORY
                </p>

                <h2>Add Physical Copy</h2>

                <p>
                  {selectedBook.title}
                </p>
              </div>

              <button
                className="close-button"
                onClick={closeForm}
              >
                ×
              </button>
            </div>

            <form
              className="book-form"
              onSubmit={handleAddCopy}
            >
              <div className="form-grid">
                <div>
                  <label>Barcode *</label>
                  <input
                    name="barcode"
                    placeholder="e.g. AH-001"
                    value={copyForm.barcode}
                    onChange={handleCopyChange}
                    required
                  />
                </div>

                <div>
                  <label>Acquisition Date</label>
                  <input
                    name="acquisitionDate"
                    type="date"
                    value={copyForm.acquisitionDate}
                    onChange={handleCopyChange}
                  />
                </div>

                <div>
                  <label>Price</label>
                  <input
                    name="price"
                    type="number"
                    min="0"
                    step="0.01"
                    value={copyForm.price}
                    onChange={handleCopyChange}
                  />
                </div>

                <div>
                  <label>Status</label>
                  <select
                    name="status"
                    value={copyForm.status}
                    onChange={handleCopyChange}
                  >
                    <option value="AVAILABLE">
                      AVAILABLE
                    </option>
                    <option value="MAINTENANCE">
                      MAINTENANCE
                    </option>
                    <option value="DAMAGED">
                      DAMAGED
                    </option>
                    <option value="LOST">
                      LOST
                    </option>
                  </select>
                </div>

                <div>
                  <label>Condition</label>
                  <select
                    name="conditionStatus"
                    value={copyForm.conditionStatus}
                    onChange={handleCopyChange}
                  >
                    <option value="NEW">NEW</option>
                    <option value="GOOD">GOOD</option>
                    <option value="FAIR">FAIR</option>
                    <option value="DAMAGED">
                      DAMAGED
                    </option>
                  </select>
                </div>

                <div>
                  <label>Location</label>
                  <input
                    name="location"
                    placeholder="e.g. Shelf B-04"
                    value={copyForm.location}
                    onChange={handleCopyChange}
                  />
                </div>
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
                  onClick={closeForm}
                >
                  Cancel
                </button>

                <button
                  type="submit"
                  className="primary-button"
                  disabled={saving}
                >
                  {saving
                    ? "Adding..."
                    : "Add Physical Copy"}
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
          <strong>Something went wrong</strong>
          <p>{error}</p>
        </div>
      )}

      {!loading &&
        !error &&
        books.length === 0 && (
          <div className="state-card">
            <h3>No books found</h3>
          </div>
        )}

      {!loading &&
        !error &&
        books.length > 0 && (
          <div className="book-grid">
            {books.map((book) => (
              <article
                className="book-card"
                key={book.id}
              >
                <div className="book-cover">
                  <span>📖</span>
                </div>

                <div className="book-card-content">
                  <span className="book-id">
                    BOOK #{book.id}
                  </span>

                  <h2>{book.title}</h2>

                  <p className="book-author">
                    {book.authors?.length
                      ? book.authors
                          .map((author) =>
                            `${
                              author.firstName ?? ""
                            } ${
                              author.lastName ?? ""
                            }`.trim()
                          )
                          .join(", ")
                      : "No author linked"}
                  </p>

                  <div className="book-meta">
                    {book.publicationYear && (
                      <span>
                        {book.publicationYear}
                      </span>
                    )}

                    {book.language && (
                      <span>
                        {book.language}
                      </span>
                    )}

                    {book.pages && (
                      <span>
                        {book.pages} pages
                      </span>
                    )}
                  </div>

                  <div className="book-details">
                    <div>
                      <span>ISBN</span>
                      <strong>
                        {book.isbn || "N/A"}
                      </strong>
                    </div>

                    <div>
                      <span>Publisher</span>
                      <strong>
                        {book.publisher || "N/A"}
                      </strong>
                    </div>
                  </div>

                  {user?.role === "ADMIN" && (
                    <div className="book-admin-actions">
                      <button
                        onClick={() =>
                          openEditBook(book)
                        }
                      >
                        Edit
                      </button>

                      <button
                        onClick={() =>
                          openAddCopy(book)
                        }
                      >
                        + Copy
                      </button>

                      <button
                        className="danger-button"
                        onClick={() =>
                          handleDelete(book)
                        }
                      >
                        Delete
                      </button>
                    </div>
                  )}
                </div>
              </article>
            ))}
          </div>
        )}
    </div>
  );
}
