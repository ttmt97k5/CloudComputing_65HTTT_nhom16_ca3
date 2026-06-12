# EduQuiz – Hệ thống thi trắc nghiệm online
**CloudComputing_65HTTT_nhom16_ca3**

## 👥 Phân công
| # | Thành viên | Nhiệm vụ |
|---|---|---|
| 1 (PM+DevOps) | ttmt97k5 | Trello, deploy Cloud Run, domain, REST API |
| 2 | — | Frontend – Học sinh |
| 3 | — | Frontend – Giáo viên |
| 4 | — | Database schema, seed data |
| 5 | — | Báo cáo PowerPoint + Video thuyết minh |

---

## ☁️ Google Cloud Infrastructure

| Thông tin | Giá trị |
|---|---|
| **Project ID** | `eduquiz-499214` |
| **Region** | `asia-southeast1` |
| **Artifact Registry** | `asia-southeast1-docker.pkg.dev/eduquiz-499214/eduquiz-repo` |

### Cloud SQL (MySQL 8.0)
| Thông tin | Giá trị |
|---|---|
| **Instance** | `eduquiz-db` |
| **Public IP** | `34.142.164.18` |
| **Database** | `eduquiz` |
| **User** | `eduquiz_user` |
| **Password** | `Eduquiz_User@2026` |
| **Port** | `3306` |

> ⚠️ **Người #4 (Database):** Dùng thông tin trên để kết nối và chạy schema SQL

---

## 🛠️ Tech Stack
| Layer | Công nghệ |
|---|---|
| Backend | Java 17 + Spring Boot 3.2 |
| Database | MySQL 8.0 (Google Cloud SQL) |
| Auth | JWT |
| Deploy | Google Cloud Run |
| Container Registry | Google Artifact Registry |
| Domain | eduquiz.to-do.live |

---

## 📁 Cấu trúc project
```
├── BE/          # Spring Boot backend (Java)
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── FE/          # Static HTML/CSS frontend
│   ├── index.html
│   ├── login.html
│   ├── register.html
│   ├── student.html
│   └── teacher.html
└── README.md
```

---

## 🔌 API Endpoints

### Auth
| Method | URL | Mô tả |
|--------|-----|-------|
| POST | `/api/auth/register` | Đăng ký tài khoản |
| POST | `/api/auth/login` | Đăng nhập, nhận JWT token |

### Student (cần Bearer token)
| Method | URL | Mô tả |
|--------|-----|-------|
| GET | `/api/student/exams` | Danh sách đề thi đang mở |
| GET | `/api/student/exams/{id}` | Chi tiết đề thi |
| POST | `/api/student/exams/submit` | Nộp bài |
| GET | `/api/student/results` | Kết quả đã thi |

### Teacher (cần Bearer token)
| Method | URL | Mô tả |
|--------|-----|-------|
| POST | `/api/teacher/exams` | Tạo đề thi mới |
| GET | `/api/teacher/exams` | Danh sách đề thi của mình |
| DELETE | `/api/teacher/exams/{id}` | Xóa đề thi |

---

## 🚀 Chạy BE local

```bash
cd BE

# Set biến môi trường
export DB_HOST=34.142.164.18
export DB_NAME=eduquiz
export DB_USER=eduquiz_user
export DB_PASS=Eduquiz_User@2026
export JWT_SECRET=eduquiz-secret-key-2026-nhom16

./mvnw spring-boot:run
```

## 🐳 Deploy Cloud Run (DevOps)

```bash
cd BE

# Build & push image
docker build -t asia-southeast1-docker.pkg.dev/eduquiz-499214/eduquiz-repo/eduquiz:latest .
docker push asia-southeast1-docker.pkg.dev/eduquiz-499214/eduquiz-repo/eduquiz:latest

# Deploy
gcloud run deploy eduquiz \
  --image asia-southeast1-docker.pkg.dev/eduquiz-499214/eduquiz-repo/eduquiz:latest \
  --platform managed \
  --region asia-southeast1 \
  --allow-unauthenticated \
  --set-env-vars DB_HOST=34.142.164.18,DB_NAME=eduquiz,DB_USER=eduquiz_user,DB_PASS=Eduquiz_User@2026,JWT_SECRET=eduquiz-secret-key-2026-nhom16
```

---

## 📋 Hướng dẫn người #4 (Database)

Kết nối MySQL với thông tin trên rồi chạy file `BE/src/main/resources/schema.sql` (Spring Boot tự tạo bảng qua JPA, nhưng cần seed data thì chạy thêm).

Hoặc kết nối thẳng:
```bash
mysql -h 34.142.164.18 -u eduquiz_user -p'Eduquiz_User@2026' eduquiz
```
