# DummyJSON API Automation Tests

Automated API tests for the public **DummyJSON API** using:

- **Java 17**
- **JUnit 5**
- **Rest Assured**
- **Maven**
- **Jackson** (POJO serialization/deserialization)

The project demonstrates a clean API automation approach with:
client layer separation (raw vs happy-path POJOs) and readable assertions on real public endpoints.

---

## ✅ Implemented Functionality

### Auth Module

Implemented automated tests for:

- `POST /auth/login`
- `GET /auth/me`
- `POST /auth/refresh`

Includes:
- Positive test cases (POJO deserialization)
- Negative test cases using raw HTTP responses:
  - invalid / empty / missing credentials
  - missing / invalid access token
  - missing / invalid refresh token
- Full **smoke-flow**: `login → me → refresh → me`
- Credentials management via environment variables (`.env`)
- Clear separation between raw clients and happy-path POJO methods

---

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
- Request/response validation
- POJO deserialization (Jackson)
- Basic contract/business checks (e.g. list size vs limit / total vs returned size)
- Field validation (`id`, `title`, `price`, `category`)
- Delete flags validation (`isDeleted`, `deletedOn`)

---

## 📂 Project Structure

```text
src/
├── main/
│   └── java/
│       ├── auth/        ← API clients & models
│       ├── products/    ← API clients & models
│       └── config/      ← API configuration
└── test/
    └── java/
        ├── auth/        ← Auth tests
        ├── products/    ← Products tests
        └── config/      ← Test credentials
pom.xml
.gitignore
README.md
```

---

## 🚀 Running Tests
Prerequisites

Java 17 and Maven installed.

Setup credentials

Create a .env file in the project root (see .env.example):
DUMMYJSON_USERNAME=emilys
DUMMYJSON_PASSWORD=emilyspass


Run all tests:

```bash

mvn test

```

Run a specific test class:

```bash
mvn -Dtest=AuthTests test
mvn -Dtest=ProductsTests test
```

---

## 🧰 Tech Stack

| Tool | Purpose |
|------|----------|
| **RestAssured** | API requests & response handling |
| **JUnit 5** | Test framework |
| **Maven** | Build & dependency management |
| **Jackson** | POJO serialization/deserialization |

---

## 🎯 Project Goals

- Practice real API automation with Java
- Build a clean, readable, portfolio-ready API testing project
- Apply REST Assured best practices (client layer, POJOs, assertions)

---

## 👤 Author

**Ahmed**