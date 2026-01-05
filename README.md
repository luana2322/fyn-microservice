# 💖 FYN - Nền Tảng Hẹn Hò & Kết Nối Xã Hội

Dự án **FYN** là một ứng dụng mạng xã hội và hẹn hò hiện đại, được xây dựng với kiến trúc **Microservices (Java Spring Boot)** và **Multi-platform Frontend (Flutter)**. Hệ thống sử dụng Kong Gateway làm API Gateway và giao tiếp bất đồng bộ giữa các service thông qua RabbitMQ.

![Java](https://img.shields.io/badge/Java-21-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-green) ![Flutter](https://img.shields.io/badge/Flutter-3.x-blue) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue) ![Kong](https://img.shields.io/badge/Kong-3.4-cyan) ![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3-ff6600) ![Docker](https://img.shields.io/badge/Docker-Enabled-blue)

---

## 🏗️ Kiến Trúc Tổng Quan

```mermaid
graph TB
    subgraph Client["📱 Client Layer"]
        FLUTTER[Flutter App<br/>Mobile / Web]
    end

    subgraph Gateway["🌐 API Gateway"]
        KONG[Kong Gateway<br/>:8080]
    end

    subgraph Services["⚙️ Microservices Layer"]
        AUTH[Auth Service<br/>:8081]
        USER[User Service<br/>:8082]
        MEDIA[Media Service<br/>:8083]
        POST[Post Service<br/>:8084]
        NOTIFY[Notification Service<br/>:8085]
        MSG[Messaging Service<br/>:8086]
        MEETUP[Meetup Service<br/>:8087]
        STORY[Story Service<br/>:8088]
        EVENT[Event Service<br/>:8089]
        SEARCH[Search Service<br/>:8090]
    end

    subgraph MessageBroker["📨 Message Broker"]
        RMQ[(RabbitMQ<br/>:5672)]
    end

    subgraph DataLayer["💾 Data Layer"]
        REDIS[(Redis<br/>Caching)]
        ES[(Elasticsearch<br/>Search Engine)]
        PG_AUTH[(PostgreSQL<br/>Auth DB :5432)]
        PG_USER[(PostgreSQL<br/>User DB :5433)]
        PG_MSG[(PostgreSQL<br/>Message DB :5434)]
        PG_POST[(PostgreSQL<br/>Post DB :5435)]
        PG_MEETUP[(PostgreSQL<br/>Meetup DB :5436)]
    end

    FLUTTER <-->|REST API| KONG
    KONG --> AUTH
    KONG --> USER
    KONG --> MEDIA
    KONG --> POST
    KONG --> NOTIFY
    KONG --> MSG
    KONG --> MEETUP
    KONG --> STORY
    KONG --> EVENT
    KONG --> SEARCH

    AUTH --> PG_AUTH
    USER --> PG_USER
    MSG --> PG_MSG
    POST --> PG_POST
    MEETUP --> PG_MEETUP
    EVENT --> PG_MEETUP
    STORY --> PG_AUTH
    NOTIFY --> PG_AUTH

    AUTH -.->|publish| RMQ
    MSG -.->|publish| RMQ
    POST -.->|publish| RMQ
    RMQ -.->|subscribe| USER
    RMQ -.->|subscribe| NOTIFY
    RMQ -.->|subscribe| SEARCH

    USER --> REDIS
    SEARCH --> ES
```

---

## 🔗 Sơ Đồ Liên Kết Giữa Các Service

Biểu đồ dưới đây mô tả chi tiết **luồng dữ liệu và sự kiện** giữa các microservices:

```mermaid
flowchart LR
    subgraph Core["🔐 Core Services"]
        AUTH["Auth Service<br/>━━━━━━━━━━━<br/>• Đăng ký/Đăng nhập<br/>• JWT Token<br/>• Refresh Token"]
        USER["User Service<br/>━━━━━━━━━━━<br/>• Profile Management<br/>• User Summary<br/>• Follow/Unfollow"]
    end

    subgraph Content["📝 Content Services"]
        POST["Post Service<br/>━━━━━━━━━━━<br/>• Tạo/Xóa bài viết<br/>• Like/Comment<br/>• Timeline"]
        STORY["Story Service<br/>━━━━━━━━━━━<br/>• Stories 24h<br/>• View Tracking"]
        MEDIA["Media Service<br/>━━━━━━━━━━━<br/>• Upload ảnh/video<br/>• File Storage"]
    end

    subgraph Social["🤝 Social Services"]
        MEETUP["Meetup Service<br/>━━━━━━━━━━━<br/>• Tạo buổi hẹn<br/>• Tham gia Meetup<br/>• GPS Location"]
        EVENT["Event Service<br/>━━━━━━━━━━━<br/>• Sự kiện cộng đồng<br/>• Join Event<br/>• Ticket Types"]
        MSG["Messaging Service<br/>━━━━━━━━━━━<br/>• Chat 1-1, Group<br/>• Conversations<br/>• WebSocket"]
    end

    subgraph Support["🔔 Support Services"]
        NOTIFY["Notification Service<br/>━━━━━━━━━━━<br/>• Push Notification<br/>• SSE Streaming<br/>• Mark as Read"]
        SEARCH["Search Service<br/>━━━━━━━━━━━<br/>• Full-text Search<br/>• Elasticsearch<br/>• Index Posts"]
    end

    %% Event-driven connections via RabbitMQ
    AUTH -->|"user.registered"| USER
    AUTH -->|"user.registered"| NOTIFY
    POST -->|"post.created"| SEARCH
    MSG -->|"message.sent"| NOTIFY

    %% HTTP Dependencies
    POST -.->|"Get User Info"| USER
    STORY -.->|"Get User Info"| USER
    MEETUP -.->|"Get User Info"| USER
    MSG -.->|"Get User Info"| USER

    POST -.->|"Upload Media"| MEDIA
    STORY -.->|"Upload Media"| MEDIA
    MSG -.->|"Upload Media"| MEDIA
```

---

## 📦 Chi Tiết Từng Service

### 🔐 Auth Service (Port: 8081)

**Chức năng:** Xác thực và phân quyền người dùng

| API Endpoint | Method | Mô tả |
|--------------|--------|-------|
| `/api/auth/login` | POST | Đăng nhập, trả về JWT Token |
| `/api/auth/register` | POST | Đăng ký tài khoản mới |

**Kết nối:**
- 📤 **Publish** event `user.registered` → RabbitMQ
- 📥 **Consumer:** User Service, Notification Service

---

### 👤 User Service (Port: 8082)

**Chức năng:** Quản lý thông tin người dùng và hồ sơ cá nhân

| API Endpoint | Method | Mô tả |
|--------------|--------|-------|
| `/api/users/{userId}/profile` | GET | Lấy thông tin profile |
| `/api/users/{userId}/profile` | PUT | Cập nhật profile |
| `/api/users/{userId}/summary` | GET | Lấy thông tin tóm tắt user |

**Kết nối:**
- 📥 **Subscribe** event `user.registered` từ Auth Service
- 🔄 **Caching:** Redis để cache user data
- 📍 Được gọi bởi: Post, Story, Meetup, Messaging Service

---

### 📷 Media Service (Port: 8083)

**Chức năng:** Quản lý upload và lưu trữ file media

| API Endpoint | Method | Mô tả |
|--------------|--------|-------|
| `/api/media/upload` | POST | Upload file (ảnh/video) |
| `/api/media/{id}` | GET | Lấy thông tin media |

**Kết nối:**
- 📍 Được gọi bởi: Post, Story, Messaging Service khi cần upload media

---

### 📝 Post Service (Port: 8084)

**Chức năng:** Quản lý bài viết và newsfeed

| API Endpoint | Method | Mô tả |
|--------------|--------|-------|
| `/api/posts` | POST | Tạo bài viết mới |
| `/api/posts` | GET | Lấy tất cả bài viết |
| `/api/posts/{id}` | GET | Lấy chi tiết bài viết |
| `/api/posts/author/{authorId}` | GET | Lấy bài viết theo tác giả |

**Kết nối:**
- 📤 **Publish** event `post.created` → RabbitMQ
- 📥 **Consumer:** Search Service (để index vào Elasticsearch)
- 🔗 **Gọi HTTP:** User Service, Media Service

---

### 🔔 Notification Service (Port: 8085)

**Chức năng:** Quản lý và gửi thông báo realtime

| API Endpoint | Method | Mô tả |
|--------------|--------|-------|
| `/api/notifications/stream/{userId}` | GET (SSE) | Stream thông báo realtime |
| `/api/notifications/user/{userId}` | GET | Lấy danh sách thông báo |
| `/api/notifications/{id}/read` | POST | Đánh dấu đã đọc |

**Kết nối:**
- 📥 **Subscribe** events từ Auth Service, Messaging Service
- 📡 **SSE Streaming** cho client

---

### 💬 Messaging Service (Port: 8086)

**Chức năng:** Nhắn tin realtime giữa users

| API Endpoint | Method | Mô tả |
|--------------|--------|-------|
| `/api/messages/conversations` | POST | Tạo cuộc hội thoại mới |
| `/api/messages/send` | POST | Gửi tin nhắn |
| `/api/messages/user/{userId}/conversations` | GET | Lấy danh sách hội thoại |
| `/api/messages/conversation/{id}` | GET | Lấy tin nhắn trong hội thoại |

**Kết nối:**
- 📤 **Publish** event `message.sent` → RabbitMQ
- 📥 **Consumer:** Notification Service
- 🔗 **Gọi HTTP:** User Service, Media Service

---

### ❤️ Meetup Service (Port: 8087)

**Chức năng:** Quản lý các buổi hẹn hò

| API Endpoint | Method | Mô tả |
|--------------|--------|-------|
| `/api/meetups` | POST | Tạo meetup mới |
| `/api/meetups` | GET | Lấy tất cả meetups |
| `/api/meetups/{id}` | GET | Chi tiết meetup |
| `/api/meetups/{id}/join` | POST | Tham gia meetup |

**Kết nối:**
- 🔗 **Gọi HTTP:** User Service để lấy thông tin người tạo/tham gia

---

### 📖 Story Service (Port: 8088)

**Chức năng:** Quản lý stories 24h

| API Endpoint | Method | Mô tả |
|--------------|--------|-------|
| `/api/stories` | POST | Tạo story mới |
| `/api/stories/user/{userId}` | GET | Lấy stories đang hoạt động |

**Kết nối:**
- 🔗 **Gọi HTTP:** User Service, Media Service

---

### 🎉 Event Service (Port: 8089)

**Chức năng:** Quản lý sự kiện cộng đồng

| API Endpoint | Method | Mô tả |
|--------------|--------|-------|
| `/api/events` | POST | Tạo sự kiện |
| `/api/events` | GET | Lấy tất cả sự kiện |
| `/api/events/{id}` | GET | Chi tiết sự kiện |
| `/api/events/{id}/join` | POST | Tham gia sự kiện (với loại vé) |

**Kết nối:**
- 📊 **Database:** Chia sẻ PostgreSQL Meetup DB (:5436)

---

### 🔍 Search Service (Port: 8090)

**Chức năng:** Tìm kiếm full-text với Elasticsearch

| API Endpoint | Method | Mô tả |
|--------------|--------|-------|
| `/api/search/posts` | GET | Tìm kiếm bài viết |

**Kết nối:**
- 📥 **Subscribe** event `post.created` từ Post Service
- 📊 **Index** dữ liệu vào Elasticsearch

---

## 🔄 Luồng Sự Kiện (Event Flow)

```mermaid
sequenceDiagram
    participant Client
    participant Kong as Kong Gateway
    participant Auth as Auth Service
    participant RMQ as RabbitMQ
    participant User as User Service
    participant Notify as Notification Service

    Note over Client,Notify: 🔐 Luồng Đăng Ký Tài Khoản
    
    Client->>Kong: POST /api/auth/register
    Kong->>Auth: Forward request
    Auth->>Auth: Tạo tài khoản mới
    Auth-->>RMQ: Publish "user.registered"
    Auth-->>Kong: Return JWT Token
    Kong-->>Client: Response với Token
    
    par Async Processing
        RMQ-->>User: Consume event
        User->>User: Tạo profile mặc định
    and
        RMQ-->>Notify: Consume event
        Notify->>Notify: Gửi notification chào mừng
    end
```

```mermaid
sequenceDiagram
    participant Client
    participant Kong as Kong Gateway
    participant Post as Post Service
    participant Media as Media Service
    participant RMQ as RabbitMQ
    participant Search as Search Service

    Note over Client,Search: 📝 Luồng Tạo Bài Viết

    Client->>Kong: POST /api/media/upload
    Kong->>Media: Forward file
    Media-->>Kong: Return media URL
    Kong-->>Client: Media uploaded

    Client->>Kong: POST /api/posts
    Kong->>Post: Forward request
    Post->>Post: Lưu bài viết vào DB
    Post-->>RMQ: Publish "post.created"
    Post-->>Kong: Return post data
    Kong-->>Client: Response

    RMQ-->>Search: Consume event
    Search->>Search: Index vào Elasticsearch
```

---

## 🛠️ Công Nghệ Sử Dụng

| Layer | Công nghệ |
|-------|-----------|
| **Backend** | Java 21, Spring Boot 3.4 |
| **API Gateway** | Kong 3.4 |
| **Message Broker** | RabbitMQ 3 |
| **Database** | PostgreSQL 15 (5 instances) |
| **Cache** | Redis Alpine |
| **Search** | Elasticsearch 8.11 |
| **Frontend** | Flutter 3 (Web, Android, iOS) |
| **Container** | Docker, Docker Compose |

---

## 🚀 Hướng Dẫn Cài Đặt

### Yêu Cầu
- Docker Desktop
- Java 21 SDK
- Maven 3.9+
- Flutter SDK 3.x

### 1️⃣ Khởi Chạy Infrastructure

```bash
docker-compose -f docker-compose.infra.yml up -d
```

Các services sẽ được khởi chạy:
| Service | URL |
|---------|-----|
| PostgreSQL Auth | `localhost:5432` |
| PostgreSQL User | `localhost:5433` |
| PostgreSQL Messages | `localhost:5434` |
| PostgreSQL Post | `localhost:5435` |
| PostgreSQL Meetup | `localhost:5436` |
| Redis | `localhost:6379` |
| RabbitMQ | `localhost:5672` |
| RabbitMQ UI | `http://localhost:15672` (guest/guest) |
| Elasticsearch | `http://localhost:9200` |
| Kong Gateway | `http://localhost:8080` |
| Kong Admin | `http://localhost:8001` |

### 2️⃣ Chạy Các Microservices

```bash
# Mở 10 terminal riêng hoặc chạy background
cd services/auth-service && mvn spring-boot:run -DskipTests &
cd services/user-service && mvn spring-boot:run -DskipTests &
cd services/media-service && mvn spring-boot:run -DskipTests &
cd services/post-service && mvn spring-boot:run -DskipTests &
cd services/notification-service && mvn spring-boot:run -DskipTests &
cd services/messaging-service && mvn spring-boot:run -DskipTests &
cd services/meetup-service && mvn spring-boot:run -DskipTests &
cd services/story-service && mvn spring-boot:run -DskipTests &
cd services/event-service && mvn spring-boot:run -DskipTests &
cd services/search-service && mvn spring-boot:run -DskipTests &
```

### 3️⃣ Chạy Flutter App

```bash
cd fyn-flutter-app
flutter pub get
flutter run
```

---

## 📁 Cấu Trúc Dự Án

```
fyn-microservice/
├── docker-compose.infra.yml    # Infrastructure containers
├── kong.yml                    # Kong Gateway config
├── fyn-common/                 # Shared libraries (DTOs, Utils)
├── fyn-flutter-app/            # Flutter frontend
└── services/
    ├── auth-service/           # Xác thực JWT
    ├── user-service/           # Quản lý user
    ├── media-service/          # Upload media
    ├── post-service/           # Bài viết
    ├── notification-service/   # Thông báo
    ├── messaging-service/      # Chat
    ├── meetup-service/         # Hẹn hò
    ├── story-service/          # Stories
    ├── event-service/          # Sự kiện
    └── search-service/         # Tìm kiếm
```

---

## 🔒 Tài Khoản Demo

| Role | Email | Password |
|------|-------|----------|
| Admin | `admin@fyn.vn` | `password` |
| User | `luan@gmail.com` | `password` |

---

Made with ❤️ by **FYN Team**
