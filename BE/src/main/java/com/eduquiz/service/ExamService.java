package com.eduquiz.service;

import com.eduquiz.dto.ExamDto;
import com.eduquiz.dto.QuestionDto;
import com.eduquiz.model.*;
import com.eduquiz.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final ExamResultRepository resultRepository;
    private final UserRepository userRepository;

    // ===== TEACHER =====

    @Transactional
    public ExamDto.ExamResponse createExam(ExamDto.CreateExamRequest request, String teacherUsername) {
        User teacher = userRepository.findByUsername(teacherUsername)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Exam exam = new Exam();
        exam.setTitle(request.getTitle());
        exam.setDescription(request.getDescription());
        exam.setDurationMinutes(request.getDurationMinutes());
        exam.setTeacher(teacher);
        examRepository.save(exam);

        if (request.getQuestions() != null) {
            List<Question> questions = new ArrayList<>();
            for (int i = 0; i < request.getQuestions().size(); i++) {
                QuestionDto.CreateQuestionRequest qReq = request.getQuestions().get(i);
                Question q = new Question();
                q.setExam(exam);
                q.setContent(qReq.getContent());
                q.setOptionA(qReq.getOptionA());
                q.setOptionB(qReq.getOptionB());
                q.setOptionC(qReq.getOptionC());
                q.setOptionD(qReq.getOptionD());
                q.setCorrectAnswer(qReq.getCorrectAnswer());
                q.setOrderIndex(i + 1);
                questions.add(q);
            }
            questionRepository.saveAll(questions);
        }

        return toExamResponse(exam);
    }

    public List<ExamDto.ExamResponse> getMyExams(String teacherUsername) {
        User teacher = userRepository.findByUsername(teacherUsername)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        return examRepository.findByTeacher(teacher).stream()
                .map(this::toExamResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteExam(Long examId, String teacherUsername) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        if (!exam.getTeacher().getUsername().equals(teacherUsername)) {
            throw new RuntimeException("Unauthorized");
        }
        questionRepository.deleteByExamId(examId);
        examRepository.delete(exam);
    }

    // ===== STUDENT =====

    public List<ExamDto.ExamResponse> getAllActiveExams() {
        return examRepository.findByActiveTrue().stream()
                .map(this::toExamResponse)
                .collect(Collectors.toList());
    }

    public ExamDto.ExamDetailResponse getExamForStudent(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        List<Question> questions = questionRepository.findByExamIdOrderByOrderIndex(examId);

        ExamDto.ExamDetailResponse response = new ExamDto.ExamDetailResponse();
        response.setId(exam.getId());
        response.setTitle(exam.getTitle());
        response.setDescription(exam.getDescription());
        response.setDurationMinutes(exam.getDurationMinutes());
        response.setQuestions(questions.stream().map(q -> {
            QuestionDto.QuestionResponse qr = new QuestionDto.QuestionResponse();
            qr.setId(q.getId());
            qr.setContent(q.getContent());
            qr.setOptionA(q.getOptionA());
            qr.setOptionB(q.getOptionB());
            qr.setOptionC(q.getOptionC());
            qr.setOptionD(q.getOptionD());
            qr.setOrderIndex(q.getOrderIndex());
            // correctAnswer không trả về cho học sinh
            return qr;
        }).collect(Collectors.toList()));

        return response;
    }

    @Transactional
    public ExamDto.SubmitResponse submitExam(ExamDto.SubmitRequest request, String studentUsername) {
        User student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        List<Question> questions = questionRepository.findByExamIdOrderByOrderIndex(exam.getId());

        int score = 0;
        List<ExamDto.SubmitResponse.AnswerResult> details = new ArrayList<>();

        for (ExamDto.SubmitRequest.AnswerItem answer : request.getAnswers()) {
            Question question = questions.stream()
                    .filter(q -> q.getId().equals(answer.getQuestionId()))
                    .findFirst().orElse(null);

            if (question != null) {
                boolean correct = question.getCorrectAnswer().equalsIgnoreCase(answer.getSelectedAnswer());
                if (correct) score++;

                ExamDto.SubmitResponse.AnswerResult detail = new ExamDto.SubmitResponse.AnswerResult();
                detail.setQuestionId(question.getId());
                detail.setSelectedAnswer(answer.getSelectedAnswer());
                detail.setCorrectAnswer(question.getCorrectAnswer());
                detail.setCorrect(correct);
                details.add(detail);
            }
        }

        double percentage = questions.isEmpty() ? 0 : (double) score / questions.size() * 100;

        ExamResult result = new ExamResult();
        result.setStudent(student);
        result.setExam(exam);
        result.setScore(score);
        result.setTotalQuestion(questions.size());
        result.setPercentage(Math.round(percentage * 10.0) / 10.0);
        resultRepository.save(result);

        ExamDto.SubmitResponse response = new ExamDto.SubmitResponse();
        response.setScore(score);
        response.setTotalQuestion(questions.size());
        response.setPercentage(result.getPercentage());
        response.setDetails(details);
        return response;
    }

    public List<ExamResult> getMyResults(String studentUsername) {
        User student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return resultRepository.findByStudent(student);
    }

    // ===== HELPER =====

    private ExamDto.ExamResponse toExamResponse(Exam exam) {
        ExamDto.ExamResponse response = new ExamDto.ExamResponse();
        response.setId(exam.getId());
        response.setTitle(exam.getTitle());
        response.setDescription(exam.getDescription());
        response.setDurationMinutes(exam.getDurationMinutes());
        response.setTeacherName(exam.getTeacher().getFullName());
        response.setCreatedAt(exam.getCreatedAt());
        List<Question> questions = questionRepository.findByExamIdOrderByOrderIndex(exam.getId());
        response.setTotalQuestions(questions.size());
        return response;
    }
}
