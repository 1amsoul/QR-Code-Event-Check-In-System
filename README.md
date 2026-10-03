# QR Code Event Check-In System

A simple Spring Boot app for event organizers: create an event, get a scannable
QR code, and watch attendees check themselves in from their own phones — no app
install, no manual roll call.

## How it works

1. Organizer creates an event (`/admin.html`) → gets a unique QR code.
2. Attendees scan the QR code with their phone camera → opens a check-in form.
3. Attendee enters name + email → submitted to the backend → recorded instantly.
4. Organizer's dashboard auto-refreshes with the live attendee count and list.

Each event gets a random, unguessable token embedded in its QR code, and each
email can only check in once per event (enforced at the database level), so the
same person can't double-scan to inflate the count.

## Tech stack

- Java 17, Spring Boot 3
- Spring Web, Spring Data JPA
- H2 in-memory database (zero setup — swap for PostgreSQL in `application.properties`
  when you want persistence)
- ZXing for QR code generation
- Plain HTML/CSS/JS frontend (no build step needed)

## Running locally

```bash
./mvnw spring-boot:run
```

Then open:
- `http://localhost:8080/admin.html` — create events, view QR codes & attendees
- `http://localhost:8080/checkin.html?token=...` — opened automatically when
  someone scans the QR code

H2 console (to inspect the database directly): `http://localhost:8080/h2-console`
— JDBC URL `jdbc:h2:mem:qrcheckin`, user `sa`, blank password.

## API endpoints

| Method | Endpoint                          | Description                        |
|--------|------------------------------------|-------------------------------------|
| POST   | `/api/events`                     | Create a new event                  |
| GET    | `/api/events`                     | List all events                     |
| GET    | `/api/events/{id}`                | Get one event                       |
| GET    | `/api/events/{id}/qrcode`         | Get the event's QR code as a PNG    |
| GET    | `/api/events/{id}/checkins`       | List attendees checked in           |
| POST   | `/api/checkin`                    | Record a check-in (token + details) |

## Possible next steps

- Swap H2 for PostgreSQL + Docker Compose
- Add organizer login (Spring Security + JWT)
- Export attendee list to CSV
- Add a live-updating count via WebSockets instead of polling

---

Built as a learning project to practice Spring Boot, JPA, and REST API design.
