# RAMPSECURE - Ramp Safety and Equipment Control System

**Client:** MaintAir Aviation Services Ltd.
**Team:** Okeoghene Adamu Great

---

## Overview

RAMPSECURE is a web-based safety management system for aircraft ground support operations at MaintAir Aviation Services. It enables real-time tracking of ground support equipment (GSE), enforces safety protocols through digital pre-use inspection checklists, and provides role-based dashboards for operators, supervisors, and administrators.

---

## Tech Stack

- **Backend:** Java 21, Spring Boot 3.5, Spring Security, JWT
- **Database:** PostgreSQL 15
- **Frontend:** React 19, TypeScript, Vite, Tailwind CSS
- **Authentication:** JWT with role and station based access control

---

## Prerequisites

- Java 21
- Node.js 18+
- PostgreSQL 15
- Maven

---

## How to Run

### Database

```sql
CREATE DATABASE rampsecure;
```

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

Runs on `http://localhost:8080`

### Frontend

```bash
cd frontend
npm install --legacy-peer-deps
npm run dev
```

Runs on `http://localhost:5173`

---

## Test Accounts

| Role       | Username    | Password    |
| ---------- | ----------- | ----------- |
| Admin      | admin       | Admin@2026  |
| Supervisor | supervisor1 | password123 |
| Operator   | peter       | password123 |

---

## Features

### Release 1

- JWT authentication with role and station based access
- Equipment checkout and checkin with condition reporting
- Transaction history

### Release 2

- Pre-use safety inspection checklist per equipment type
- Critical failure blocks checkout and flags equipment for maintenance
- Supervisor dashboard with full equipment visibility
- Admin dashboard for user management
