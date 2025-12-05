# DummyJSON API Automation Tests

Automated API tests for the public **DummyJSON API**, implemented using:

- **Java 17**
- **JUnit 5**
- **RestAssured**
- **Maven**

This project focuses on practicing REST API automation through CRUD operations and negative cases on real public endpoints.

---

## 📌 Covered Functionality

### **Products Module**
Implemented automated tests for:

- `GET /products`
- `GET /products/{id}` (valid & invalid)
- `POST /products/add` (valid & invalid JSON)
- `PUT /products/{id}` (valid & invalid)
- `DELETE /products/{id}` (valid & invalid)

**Checks include:**

- Status codes (200, 201, 400, 404)
- JsonPath field extraction
- Request/response value validation
- Checking error messages
- Checking boolean flags like `isDeleted`

---

## 📌 Planned Additions

- Add **Auth module** test coverage:
    - `POST /auth/login`
    - `GET /auth/me`
    - `POST /auth/refresh`
    - Positive & negative cases (valid token / invalid token)

---

## 📂 Project Structure

```text
src/
└── test/
    └── java/
        └── dummyjson/
            └── ProductsTests.java
pom.xml
.gitignore
README.md
```

---

## 🚀 Running Tests

Run all tests:

```bash

mvn test

```

Run a specific test class:

```bash

mvn -Dtest=ProductsTests test

```

---

## 🧰 Tech Stack

| Tool | Purpose |
|------|----------|
| **RestAssured** | API requests & JSON parsing |
| **JUnit 5** | Test framework |
| **Maven** | Build system |
| **JsonPath** | Extracting JSON fields |

---

## 🎯 Project Goals

- Practice real API automation with Java
- Cover Products and Auth endpoints with clean positive/negative tests
- Build a simple, readable, portfolio-ready API testing project
- Improve the project later by introducing model classes and object-based request/response handling

---

## 👤 Author

**Ahmed**
