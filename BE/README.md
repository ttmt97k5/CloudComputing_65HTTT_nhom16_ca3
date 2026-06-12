# EduQuiz - Hệ thống thi trắc nghiệm online

**CloudComputing_65HTTT_nhom16_ca3**

## Tech Stack
- Backend: Java 17 + Spring Boot 3.2
- Database: MySQL (Google Cloud SQL)
- Auth: JWT
- Deploy: Google Cloud Run

## API Endpoints

### Auth
| Method | URL | Mô tả |
|--------|-----|-------|
| POST | `/api/auth/register` | Đăng ký tài khoản |
| POST | `/api/auth/login` | Đăng nhập, nhận JWT token |

### Student (cần token)
| Method | URL | Mô tả |
|--------|-----|-------|
| GET | `/api/student/exams` | Danh sách đề thi |
| GET | `/api/student/exams/{id}` | Chi tiết đề thi |
| POST | `/api/student/exams/submit` | Nộp bài |
| GET | `/api/student/results` | Kết quả đã thi |

### Teacher (cần token)
| Method | URL | Mô tả |
|--------|-----|-------|
| POST | `/api/teacher/exams` | Tạo đề thi mới |
| GET | `/api/teacher/exams` | Danh sách đề thi của mình |
| DELETE | `/api/teacher/exams/{id}` | Xóa đề thi |

## Chạy local

```bash
# Tạo database MySQL
CREATE DATABASE eduquiz;

# Set biến môi trường rồi chạy
DB_HOST=localhost DB_NAME=eduquiz DB_USER=root DB_PASS=password ./mvnw spring-boot:run
```

## Deploy Cloud Run

```bash
# Build image
docker build -t eduquiz .

# Push lên Artifact Registry
docker tag eduquiz asia-southeast1-docker.pkg.dev/PROJECT_ID/eduquiz/eduquiz:latest
docker push asia-southeast1-docker.pkg.dev/PROJECT_ID/eduquiz/eduquiz:latest

# Deploy
gcloud run deploy eduquiz \
  --image asia-southeast1-docker.pkg.dev/PROJECT_ID/eduquiz/eduquiz:latest \
  --platform managed \
  --region asia-southeast1 \
  --allow-unauthenticated \
  --set-env-vars DB_HOST=YOUR_DB_HOST,DB_NAME=eduquiz,DB_USER=YOUR_USER,DB_PASS=YOUR_PASS
```
