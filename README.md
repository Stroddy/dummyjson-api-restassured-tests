# DummyJSON API Automation Tests

Automated API tests for the public **DummyJSON API**, implemented using:

- **Java 17**
- **JUnit 5**
- **Rest Assured**
- **Maven**

The project demonstrates a clean API automation approach with:
client layer separation, POJO deserialization and readable assertions on real public endpoints.

---

## ✅ Implemented Functionality

### Auth Module

Implemented automated tests for:

- `POST /auth/login`
- `GET /auth/me`
- `POST /auth/refresh`

Includes:
- Positive test cases
- Full **smoke-flow**: `login → me → refresh → me`
- Token-based authorization
- Credentials management via environment variables (`.env`)
- Clear separation between clients and tests

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
- Business logic checks (e.g. list size vs limit)
- Field validation (`id`, `title`, `price`, `category`)
- Flags validation (`isDeleted`, `deletedOn`)

---

## 🧪 Planned Improvements

- Add **negative test cases** for:
  - Auth module (invalid credentials, invalid tokens, expired tokens)
  - Products module (invalid ids, invalid payloads)
- Introduce raw HTTP response handling for negative scenarios
- Use JsonPath for error validation

> Reporting tools (Allure, etc.) will be implemented in a separate project.

---

📂 **Project Structure**

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
        ├── products/   ← Products tests
        └── config/     ← Test credentials
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