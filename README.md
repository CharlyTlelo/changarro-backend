# Changarro Backend API

Node.js + Express + TypeScript + MongoDB (Mongoose)

## Setup

```bash
cd backend
cp .env.example .env   # edit MONGO_URI / JWT_SECRET if needed
npm install
npm run seed           # creates demo data
npm run dev            # starts on port 8080
```

## Scripts

| Script | Description |
|--------|-------------|
| `npm run dev` | Dev server with hot-reload (ts-node-dev) |
| `npm run build` | Compile TypeScript to dist/ |
| `npm start` | Run compiled JS (production) |
| `npm run seed` | Seed database with demo data |

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `MONGO_URI` | `mongodb://localhost:27017/changarro_db` | MongoDB connection string |
| `JWT_SECRET` | (dev fallback) | Secret for JWT signing |
| `PORT` | `8080` | Server port |

## Demo Users (after seed)

| Email | Password | Role |
|-------|----------|------|
| admin@changarro.mx | Admin123 | ADMIN |
| demo@changarro.mx | Demo123 | BUSINESS |
| maria@gmail.com | Cliente123 | CUSTOMER |

## API Endpoints

### Auth (no token required)
- `POST /api/auth/register-business` — Register business owner + business
- `POST /api/auth/register` — Register customer
- `POST /api/auth/login` — Login (any role)
- `POST /api/auth/admin/login` — Admin-only login

### Business (JWT required)
- `GET /api/businesses/mine` — Get my business profile
- `PATCH /api/businesses/mine` — Update business profile
- `PUT /api/businesses/mine/menu` — Replace full menu
- `PUT /api/businesses/mine/promo` — Set active promo
- `DELETE /api/businesses/mine/promo` — Remove active promo
- `GET /api/businesses/mine/analytics` — Get analytics/KPIs

### Admin (JWT + ADMIN role required)
- `GET /api/admin/businesses` — List all businesses
- `GET /api/admin/businesses/:id` — Get single business
- `GET /api/admin/users` — List all users

### Health
- `GET /api/health` — Health check

## Curl Examples

### Register a business
```bash
curl -X POST http://localhost:8080/api/auth/register-business \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Perez",
    "email": "juan@test.com",
    "password": "MiPass123",
    "businessName": "Tacos Don Juan",
    "phone": "5551234567",
    "address": "Calle 5 de Mayo 100",
    "description": "Tacos de canasta"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"demo@changarro.mx","password":"Demo123"}'
```

### Get my business (use token from login)
```bash
curl http://localhost:8080/api/businesses/mine \
  -H "Authorization: Bearer <TOKEN>"
```

### Update menu
```bash
curl -X PUT http://localhost:8080/api/businesses/mine/menu \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '[
    {"name":"Taco al Pastor","emoji":"🌮","price":"$25","orderCount":0},
    {"name":"Agua de Jamaica","emoji":"🥤","price":"$15","orderCount":0}
  ]'
```

### Set promo
```bash
curl -X PUT http://localhost:8080/api/businesses/mine/promo \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "title": "2x1 Martes",
    "label": "PROMO",
    "validUntil": "2026-12-31",
    "note": "Solo en sucursal centro",
    "bonusCoins": 5
  }'
```

### Get analytics
```bash
curl http://localhost:8080/api/businesses/mine/analytics \
  -H "Authorization: Bearer <TOKEN>"
```
