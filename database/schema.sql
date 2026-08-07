CREATE DATABASE IF NOT EXISTS pustakaalay_db;

USE pustakaalay_db;

-- =========================
-- ROLES
-- =========================

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- =========================
-- USERS
-- =========================

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50),
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    membership_number VARCHAR(30) UNIQUE,
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login_at DATETIME,

    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- =========================
-- AUTHORS
-- =========================

CREATE TABLE authors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50),
    biography TEXT,
    nationality VARCHAR(50),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- CATEGORIES
-- =========================

CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- BOOKS
-- =========================

CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    publisher VARCHAR(150),
    publication_year INT,
    edition VARCHAR(50),
    language VARCHAR(50),
    description TEXT,
    cover_image_url VARCHAR(500),
    pages INT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =========================
-- BOOK AUTHORS
-- =========================

CREATE TABLE book_authors (
    book_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,

    PRIMARY KEY (book_id, author_id),

    CONSTRAINT fk_book_authors_book
        FOREIGN KEY (book_id) REFERENCES books(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_book_authors_author
        FOREIGN KEY (author_id) REFERENCES authors(id)
        ON DELETE CASCADE
);

-- =========================
-- BOOK CATEGORIES
-- =========================

CREATE TABLE book_categories (
    book_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,

    PRIMARY KEY (book_id, category_id),

    CONSTRAINT fk_book_categories_book
        FOREIGN KEY (book_id) REFERENCES books(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_book_categories_category
        FOREIGN KEY (category_id) REFERENCES categories(id)
        ON DELETE CASCADE
);

-- =========================
-- BOOK COPIES
-- =========================

CREATE TABLE book_copies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    barcode VARCHAR(50) NOT NULL UNIQUE,
    acquisition_date DATE,
    price DECIMAL(10,2),
    status ENUM(
        'AVAILABLE',
        'BORROWED',
        'RESERVED',
        'LOST',
        'DAMAGED',
        'MAINTENANCE'
    ) NOT NULL DEFAULT 'AVAILABLE',
    condition_status ENUM(
        'NEW',
        'GOOD',
        'FAIR',
        'DAMAGED'
    ) NOT NULL DEFAULT 'GOOD',
    location VARCHAR(100),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_book_copies_book
        FOREIGN KEY (book_id) REFERENCES books(id)
);

-- =========================
-- BORROWINGS
-- =========================

CREATE TABLE borrowings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_copy_id BIGINT NOT NULL,
    borrowed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_at DATETIME NOT NULL,
    returned_at DATETIME,
    status ENUM(
        'BORROWED',
        'RETURNED',
        'OVERDUE',
        'LOST'
    ) NOT NULL DEFAULT 'BORROWED',
    renewal_count INT NOT NULL DEFAULT 0,
    notes VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_borrowings_user
        FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT fk_borrowings_copy
        FOREIGN KEY (book_copy_id) REFERENCES book_copies(id)
);

-- =========================
-- RESERVATIONS
-- =========================

CREATE TABLE reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    reserved_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME,
    status ENUM(
        'ACTIVE',
        'FULFILLED',
        'CANCELLED',
        'EXPIRED'
    ) NOT NULL DEFAULT 'ACTIVE',
    queue_position INT,

    CONSTRAINT fk_reservations_user
        FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT fk_reservations_book
        FOREIGN KEY (book_id) REFERENCES books(id)
);

-- =========================
-- WISHLISTS
-- =========================

CREATE TABLE wishlists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_wishlist_user_book
        UNIQUE (user_id, book_id),

    CONSTRAINT fk_wishlist_user
        FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT fk_wishlist_book
        FOREIGN KEY (book_id) REFERENCES books(id)
);

-- =========================
-- REVIEWS
-- =========================

CREATE TABLE reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    rating TINYINT NOT NULL,
    review_text TEXT,
    status ENUM('PUBLISHED', 'HIDDEN') NOT NULL DEFAULT 'PUBLISHED',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_review_user_book
        UNIQUE (user_id, book_id),

    CONSTRAINT chk_review_rating
        CHECK (rating BETWEEN 1 AND 5),

    CONSTRAINT fk_reviews_user
        FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT fk_reviews_book
        FOREIGN KEY (book_id) REFERENCES books(id)
);

-- =========================
-- FINES
-- =========================

CREATE TABLE fines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    borrowing_id BIGINT NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL,
    reason VARCHAR(255),
    status ENUM('PENDING', 'PAID', 'WAIVED') NOT NULL DEFAULT 'PENDING',
    issued_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    paid_at DATETIME,

    CONSTRAINT fk_fines_user
        FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT fk_fines_borrowing
        FOREIGN KEY (borrowing_id) REFERENCES borrowings(id)
);

-- =========================
-- NOTIFICATIONS
-- =========================

CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    message VARCHAR(500) NOT NULL,
    type ENUM(
        'DUE_REMINDER',
        'OVERDUE',
        'RESERVATION_AVAILABLE',
        'FINE_CREATED',
        'SYSTEM'
    ) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_at DATETIME,

    CONSTRAINT fk_notifications_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =========================
-- INDEXES
-- =========================

CREATE INDEX idx_users_role
    ON users(role_id);

CREATE INDEX idx_books_title
    ON books(title);

CREATE INDEX idx_books_isbn
    ON books(isbn);

CREATE INDEX idx_book_copies_book
    ON book_copies(book_id);

CREATE INDEX idx_book_copies_status
    ON book_copies(status);

CREATE INDEX idx_borrowings_user
    ON borrowings(user_id);

CREATE INDEX idx_borrowings_status
    ON borrowings(status);

CREATE INDEX idx_borrowings_due_at
    ON borrowings(due_at);

CREATE INDEX idx_reservations_book
    ON reservations(book_id);

CREATE INDEX idx_reservations_user
    ON reservations(user_id);

CREATE INDEX idx_notifications_user
    ON notifications(user_id);

CREATE INDEX idx_notifications_read
    ON notifications(is_read);
