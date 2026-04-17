# 🏥 MedLink

> **MedLink** là hệ thống quản lý y tế toàn diện, kết nối bệnh nhân, bác sĩ và cơ sở y tế trên một nền tảng duy nhất.  
> Backend được xây dựng bằng **Spring Boot**, frontend bằng **Angular**, hỗ trợ xử lý bất đồng bộ qua **Apache Kafka** và bảo mật bằng **JWT (RSA)**.

---

## 📋 Mục lục

- [Tổng quan](#tổng-quan)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Kiến trúc hệ thống](#kiến-trúc-hệ-thống)
- [Tính năng chính](#tính-năng-chính)
- [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
- [Cài đặt & Chạy dự án](#cài-đặt--chạy-dự-án)
- [Cấu hình](#cấu-hình)
- [API Documentation](#api-documentation)
- [Cấu trúc dự án](#cấu-trúc-dự-án)

---

## Tổng quan

MedLink cung cấp các chức năng quản lý:

| Module | Mô tả |
|---|---|
| 🔐 **Auth** | Đăng ký, đăng nhập, phân quyền theo vai trò |
| 🏥 **Hospital** | Quản lý thông tin bệnh viện |
| 👨‍⚕️ **Doctor** | Hồ sơ bác sĩ, phân công phòng khám |
| 🧑‍🤝‍🧑 **Patient** | Hồ sơ bệnh nhân, dị ứng, bệnh mãn tính |
| 🎫 **Reception Tickets** | Đăng ký khám bệnh (Kafka async) |
| 📋 **Medical Records** | Hồ sơ bệnh án, lịch sử khám |
| 🧪 **Lab Orders & Results** | Chỉ định và kết quả xét nghiệm |
| 🚪 **Rooms** | Quản lý phòng khám, chuyên khoa |

---

## Công nghệ sử dụng

### Backend
| Công nghệ | Phiên bản | Mục đích |
|---|---|---|
| Java | 21 | Ngôn ngữ lập trình |
| Spring Boot | 3.5.6 | Framework chính |
| Spring Security | — | Xác thực & phân quyền |
| Spring Data JPA | — | ORM / Database |
| Spring Kafka | — | Message broker |
| PostgreSQL | — | Cơ sở dữ liệu chính |
| Redis | — | Token blacklist, caching |
| JWT (JJWT 0.11.5) | RSA 2048-bit | Bảo mật API |
| SpringDoc OpenAPI | 2.7.0 | Tài liệu API tự động |
| Lombok | — | Giảm boilerplate code |

### Frontend
| Công nghệ | Phiên bản | Mục đích |
|---|---|---|
| Angular | ~21.1 | Framework UI |
| TypeScript | ~5.9 | Ngôn ngữ lập trình |
| RxJS | ~7.8 | Reactive programming |

### Infrastructure
| Công nghệ | Mục đích |
|---|---|
| Apache Kafka | Xử lý đăng ký khám bệnh bất đồng bộ |
| Docker | Chạy Kafka + Zookeeper |

---

## Kiến trúc hệ thống

```
                          ┌────────────────────┐
                          │   Angular Frontend  │
                          └────────┬───────────┘
                                   │ HTTP / REST
                          ┌────────▼───────────┐
                          │  Spring Boot API    │
                          │  (Port 8080)        │
                          └──┬──────┬──────┬───┘
                             │      │      │
               ┌─────────────▼─┐  ┌─▼──┐  ┌─▼──────────┐
               │  PostgreSQL   │  │Redis│  │Apache Kafka│
               │  (medilink)   │  │     │  │ (Port 9092)│
               └───────────────┘  └─────┘  └────────────┘
```

### Luồng đăng ký khám bệnh (Kafka Async)

```
Client → POST /api/v1/.../reception-tickets
      → ReceptionTicketsService
      → Kafka Producer → [reception-ticket-topic]
                                  ↓
      Database ← Validation & Processing ← Kafka Consumer
```

---

## Tính năng chính

### 🔐 Bảo mật
- Xác thực bằng **JWT RSA 2048-bit** (asymmetric encryption)
- Phân quyền theo vai trò: `ADMIN`, `DOCTOR`, `PATIENT`, `TECHNICIAN`, `SUPER_ADMIN`
- Token blacklist lưu trên **Redis**
- CORS được cấu hình theo môi trường

### 🎫 Đăng ký khám bệnh (Asynchronous)
- Tạo phiếu khám không đồng bộ qua Kafka
- Tự động gán số thứ tự trong ngày
- Kiểm tra trùng lặp phiếu khám trong ngày
- Scale ngang bằng nhiều Kafka consumer instances

### 📖 API Documentation
- Swagger UI tại: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON tại: `http://localhost:8080/v3/api-docs`

---

## Yêu cầu hệ thống

| Phần mềm | Phiên bản tối thiểu |
|---|---|
| JDK | 21+ |
| Maven | 3.8+ |
| Node.js | 18+ |
| npm | 9+ |
| PostgreSQL | 14+ |
| Redis | 6+ |
| Docker | 20+ (để chạy Kafka) |

---

## Cài đặt & Chạy dự án

### 1. Clone repository

```bash
git clone https://github.com/hieunt2907/MedLink.git
cd MedLink
```

### 2. Khởi động Kafka bằng Docke

```bash
# Tạo file docker-compose.yml (nếu chưa có)
cat > docker-compose.yml << 'EOF'
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "2181:2181"

  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
EOF

docker-compose up -d
```

```bash
# Tạo Kafka topic
docker exec -it <kafka-container-id> \
  kafka-topics --create \
  --topic reception-ticket-topic \
  --bootstrap-server localhost:9092 \
  --partitions 3 \
  --replication-factor 1
```

### 3. Tạo database PostgreSQL

```sql
CREATE DATABASE postgres;
CREATE SCHEMA medilink;
```

### 4. Tạo JWT RSA Keys

```bash
mkdir -p src/main/resources/keys

# Tạo private key
openssl genrsa -out src/main/resources/keys/jwt-private.pem 2048

# Tạo public key
openssl rsa -in src/main/resources/keys/jwt-private.pem \
  -pubout -out src/main/resources/keys/jwt-public.pem
```

> ⚠️ **Quan trọng:** Thư mục `src/main/resources/keys/` đã được thêm vào `.gitignore`. Không bao giờ commit private key lên repository.

### 5. Cấu hình `application.properties`

Xem chi tiết tại mục [Cấu hình](#cấu-hình) bên dưới.

### 6. Chạy Backend

```bash
# Build và chạy
./mvnw spring-boot:run

# Hoặc build trước rồi chạy
./mvnw clean package -DskipTests
java -jar target/MedLink-0.0.1-SNAPSHOT.jar
```

Backend khởi động tại: `http://localhost:8080`

### 7. Chạy Frontend

```bash
cd frontend
npm install
npm start
```

Frontend khởi động tại: `http://localhost:4200`



---

## API Documentation

Sau khi backend đã chạy, truy cập Swagger UI tại:

```
http://localhost:8080/swagger-ui/index.html
```

### Các nhóm API chính

| Prefix | Vai trò | Ví dụ path |
|---|---|---|
| `/api/v1/public/auth` | Public | `POST /login`, `POST /` (register) |
| `/api/v1/admin/**` | Admin | Quản lý toàn bộ tài nguyên |
| `/api/v1/doctor/**` | Doctor | Xem/cập nhật hồ sơ, y lệnh |
| `/api/v1/patient/**` | Patient | Đặt lịch khám, xem hồ sơ bản thân |
| `/api/v1/technician/**` | Technician | Xử lý kết quả xét nghiệm |

### Ví dụ: Đăng nhập

```bash
curl -X POST http://localhost:8080/api/v1/public/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password"
  }'
```

### Ví dụ: Tạo phiếu khám (cần JWT)

```bash
curl -X POST http://localhost:8080/api/v1/patient/reception-tickets \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "hospitalId": 1,
    "patientId": 123,
    "specialtyId": 5,
    "doctorId": 10,
    "roomId": 3,
    "estimatedTime": "2025-12-14T10:00:00+07:00",
    "visitPayerType": "self_pay",
    "reason": "Khám định kỳ",
    "priority": "normal"
  }'
```

---

## Cấu trúc dự án

```
MedLink/
├── frontend/                         # Angular frontend
│   ├── src/
│   │   ├── app/
│   │   └── ...
│   ├── package.json
│   └── angular.json
│
├── src/
│   └── main/
│       ├── java/com/hieunt/medlink/
│       │   ├── MedLinkApplication.java
│       │   ├── config/               # Cấu hình Kafka, Redis, CORS
│       │   └── app/
│       │       ├── controllers/      # REST Controllers
│       │       │   ├── AuthController.java
│       │       │   ├── admin/        # Endpoints cho Admin
│       │       │   ├── doctor/       # Endpoints cho Doctor
│       │       │   ├── patitent/     # Endpoints cho Patient
│       │       │   └── technician/   # Endpoints cho Technician
│       │       ├── services/         # Business logic
│       │       │   ├── auth/
│       │       │   ├── kafka/        # Kafka Producer/Consumer
│       │       │   ├── reception_ticket/
│       │       │   ├── medical_record/
│       │       │   ├── lab_order/
│       │       │   └── ...
│       │       ├── entities/         # JPA Entities
│       │       ├── repositories/     # Spring Data JPA Repositories
│       │       ├── requests/         # Request DTOs
│       │       ├── responses/        # Response DTOs
│       │       ├── mappers/          # Entity ↔ DTO Mappers
│       │       ├── secure/           # JWT, Security Filter
│       │       ├── errors/           # Global Exception Handler
│       │       └── utils/            # Tiện ích
│       └── resources/
│           ├── application.properties
│           └── keys/                 # JWT RSA keys (gitignored)
│
├── pom.xml
├── docker-compose.yml
└── README.md
```