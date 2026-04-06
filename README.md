# Finance Data Processing and Access Control Backend

A backend system for a finance dashboard built with Java + Spring Boot.
It supports user role management, financial records, dashboard analytics,
and JWT-based access control.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.5 |
| Database | H2 (file mode) |
| Auth | JWT (jjwt 0.12.6) |
| Build Tool | Maven |

---

## How to Run

### Prerequisites
- Java 17 or higher
- Maven (or use the included `mvnw`)

### Steps
```bash

cd finance-backend


./mvnw spring-boot:run

```

The server starts on `http://localhost:8080`

---

## Default Users (auto-seeded on startup)

| Email | Password | Role |
|---|---|---|
| admin@finance.com | admin123 | ADMIN |
| analyst@finance.com | analyst123 | ANALYST |
| viewer@finance.com | viewer123 | VIEWER |

---

## Role Permissions

| Action | VIEWER | ANALYST | ADMIN |
|---|---|---|---|
| View transactions | ✅ | ✅ | ✅ |
| View dashboard | ✅ | ✅ | ✅ |
| Create transaction | ❌ | ✅ | ✅ |
| Update transaction | ❌ | ✅ | ✅ |
| Delete transaction | ❌ | ❌ | ✅ |
| Manage users | ❌ | ❌ | ✅ |

---

## API Endpoints

### Authentication
| Method | URL | Description | Auth |
|---|---|---|---|
| POST | `/api/auth/login` | Login and get JWT token | None |

### Users (Admin only)
| Method | URL | Description |
|---|---|---|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/{id}` | Update user |
| PATCH | `/api/users/{id}/toggle-status` | Toggle active/inactive |
| DELETE | `/api/users/{id}` | Delete user |

### Transactions
| Method | URL | Description | Auth |
|---|---|---|---|
| GET | `/api/transactions` | Get all (supports filters) | All roles |
| GET | `/api/transactions/{id}` | Get by ID | All roles |
| POST | `/api/transactions` | Create new | ANALYST, ADMIN |
| PUT | `/api/transactions/{id}` | Update | ANALYST, ADMIN |
| DELETE | `/api/transactions/{id}` | Delete | ADMIN only |

#### Filtering (query params)
```
GET /api/transactions?type=INCOME
GET /api/transactions?category=Food
GET /api/transactions?startDate=2026-01-01&endDate=2026-04-05
GET /api/transactions?type=EXPENSE&category=Rent
```

### Dashboard
| Method | URL | Description | Auth |
|---|---|---|---|
| GET | `/api/dashboard/summary` | Full summary | All roles |
| GET | `/api/dashboard/category-totals` | Per category totals | All roles |
| GET | `/api/dashboard/monthly-trends` | Monthly breakdown | All roles |
| GET | `/api/dashboard/recent-activity` | Last 10 transactions | All roles |

---

## How Authentication Works

1. Call `POST /api/auth/login` with email and password
2. Copy the `token` from the response
3. Add it to every request header:
```
Authorization: Bearer <your-token>
```

---

## Database

- Uses **H2 file-based database** — no installation needed
- DB file is saved at `./data/financedb`
- View DB at `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:file:./data/financedb`
    - Username: `sa`
    - Password: (empty)

---

## Assumptions Made

- `ddl-auto=create-drop` is used for demo purposes — tables are recreated on every restart with fresh seed data
- Passwords are stored as BCrypt hashes, never plain text
- JWT tokens expire after 24 hours
- A VIEWER can read all data but cannot modify anything
- An ANALYST can create and update transactions but cannot delete or manage users
- Only ADMIN has full access including user management and deletion

---

## Project Structure
```
src/main/java/com/finance/finance_backend/
├── config/         # DataSeeder, SwaggerConfig
├── controller/     # REST controllers (Auth, User, Transaction, Dashboard)
├── dto/            # Request/Response data shapes
├── exception/      # Global error handling
├── model/          # JPA entities (User, Transaction, Role, TransactionType)
├── repository/     # Database interfaces
├── security/       # JWT utility, filter, security config
└── service/        # Business logic (Auth, User, Transaction, Dashboard)
```