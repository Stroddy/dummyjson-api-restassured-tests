# DummyJSON API Automation Tests

Automated API tests for the public **DummyJSON API**, implemented using:

- **Java 17**
- **JUnit 5**
- **RestAssured**
- **Maven**

This project focuses on practicing REST API automation using a clean client-based approach,
POJO deserialization and readable assertions on real public endpoints.

---

📌 **Covered Functionality**

### Products Module

Implemented automated tests for:

- `GET /products`
- `GET /products/{id}`
- `GET /products/search?q=`
- `GET /products/category/{category}`
- `POST /products/add`
- `PUT /products/{id}`
- `DELETE /products/{id}`

Checks include:

- Request/response data validation
- POJO deserialization using Jackson
- Business logic checks (e.g. list size vs limit)
- Field validation (id, title, price, category)
- Checking boolean flags like `isDeleted` and `deletedOn`

---

📌 **Planned Additions**

- Add **Auth module** test coverage:
  - `POST /auth/login`
  - `GET /auth/me`
  - `POST /auth/refresh`
- Positive & negative cases (valid / invalid token)
- Introduce base client and shared configuration layer
- Extend project with another public API

---

📂 **Project Structure**

```text
src/
└── test/
    └── java/
        ├── config/
        │   └── ApiConfig.java
        └── products/
            ├── ProductsClient.java
            ├── ProductsTests.java
            ├── Product.java
            ├── ProductsResponse.java
            └── UpdateProductRequest.java
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
| **Jackson** | POJO serialization/deserialization |

---

## 🎯 Project Goals

- Practice real API automation with Java
- Build a clean, readable, portfolio-ready API testing project
- Apply REST Assured best practices (client layer, POJOs, assertions)
- Gradually extend coverage with Auth and additional APIs

---

## 👤 Author

**Ahmed**