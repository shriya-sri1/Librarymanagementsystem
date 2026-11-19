# Q2 – Library Management REST API

Built with Java 17, Spring Boot, Spring Web, Spring JDBC (JdbcTemplate), MySQL and Postman.

## 1. Create the database / configure credentials
The application creates `library_db` if it does not exist and runs `schema.sql` at startup.
Open `src/main/resources/application.properties` and replace `YOUR_MYSQL_PASSWORD` with your MySQL root password.

If your MySQL username is not `root`, update `spring.datasource.username` too.

## 2. Open in IntelliJ IDEA
1. IntelliJ IDEA → File → Open.
2. Select this folder (`Q2_LibraryManagement_SpringBoot`).
3. Trust the project and wait for Maven dependencies to download.
4. Set Project SDK to Java 17 (File → Project Structure → Project SDK).
5. Run `LibraryManagementApplication.java`.

Expected startup: `Tomcat started on port 8080` and `Started LibraryManagementApplication`.

## 3. Test with Postman
Base URL: `http://localhost:8080/api/books`

### POST – add a book
Method: POST | URL: `http://localhost:8080/api/books`
Header: `Content-Type: application/json`
Body → raw → JSON:
```json
{
  "id": 1,
  "title": "The Alchemist",
  "author": "Paulo Coelho",
  "category": "Fiction",
  "availableCopies": 5
}
```
Expected: 201 Created.

### GET – all books
GET `http://localhost:8080/api/books`
Expected: 200 OK with a JSON array.

### GET – one book
GET `http://localhost:8080/api/books/1`
Expected: 200 OK, or 404 Not Found if ID does not exist.

### PUT – update a book
PUT `http://localhost:8080/api/books/1`
Header: `Content-Type: application/json`
Body → raw → JSON:
```json
{
  "id": 1,
  "title": "The Alchemist - Updated",
  "author": "Paulo Coelho",
  "category": "Adventure",
  "availableCopies": 8
}
```
Expected: 200 OK, or 404 Not Found.

### DELETE – delete a book
DELETE `http://localhost:8080/api/books/1`
Expected: 200 OK, or 404 Not Found.

## 4. Database check
In MySQL Workbench:
```sql
USE library_db;
SELECT * FROM books;
```

## Notes
- This implementation uses `JdbcTemplate`, so SQL statements are explicit and it satisfies the JDBC + MySQL requirement.
- The `books` table uses `available_copies` in MySQL and maps it to Java's `availableCopies`.
- Use different IDs for separate POST tests. Repeating an existing ID returns HTTP 409 Conflict.
