package com.tanvis.hostel;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
public class Controllers {

    @Autowired
    public repo1 hostelRepo;

    @Autowired
    public repo2 complaintRepo;

    @Autowired
    public repo3 noticeRepo;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("occupiedCount", hostelRepo.count());
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session) {
        Hostel hostel = hostelRepo.findByUsernameAndPassword(username, password);
        if (hostel == null) return "redirect:/login";
        session.setAttribute("hostel", hostel);
        if (hostel.getRole().equals("ADMIN")) return "redirect:/admin/dashboard";
        return "redirect:/student/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ─── STUDENT ────────────────────────────────────────────

    @GetMapping("/student/dashboard")
    public String studentDashboard(HttpSession session, Model model) {
        Hostel hostel = (Hostel) session.getAttribute("hostel");
        if (hostel == null) return "redirect:/login";
        List<Notice> allNotices = noticeRepo.findAll();
        int fromIndex = Math.max(0, allNotices.size() - 3);
        model.addAttribute("hostel", hostel);
        model.addAttribute("latestNotices", allNotices.subList(fromIndex, allNotices.size()));
        return "student/dashboard";
    }

    @GetMapping("/student/profile")
    public String studentProfile(HttpSession session, Model model) {
        Hostel hostel = (Hostel) session.getAttribute("hostel");
        if (hostel == null) return "redirect:/login";
        model.addAttribute("hostel", hostel);
        return "student/profile";
    }

    @PostMapping("/student/profile/update")
    public String updateProfile(@RequestParam String email,
                                @RequestParam String phone,
                                @RequestParam String course,
                                @RequestParam String year,
                                @RequestParam String gender,
                                @RequestParam String program_level,
                                HttpSession session) {
        Hostel hostel = (Hostel) session.getAttribute("hostel");
        if (hostel == null) return "redirect:/login";
        hostel.setEmail(email);
        hostel.setPhone(phone);
        hostel.setCourse(course);
        hostel.setYear(year);
        hostel.setGender(gender);
        hostel.setProgram_level(program_level);
        hostelRepo.save(hostel);
        session.setAttribute("hostel", hostel);
        return "redirect:/student/profile";
    }

    @GetMapping("/student/complaints")
    public String studentComplaints(HttpSession session, Model model) {
        Hostel hostel = (Hostel) session.getAttribute("hostel");
        if (hostel == null) return "redirect:/login";
        model.addAttribute("hostel", hostel);
        model.addAttribute("complaints", complaintRepo.findByStudent(hostel));
        return "student/complaint";
    }

    @PostMapping("/student/complaints/submit")
    public String submitComplaint(@RequestParam String title,
                                  @RequestParam String description,
                                  @RequestParam String category,
                                  @RequestParam String priority,
                                  HttpSession session) {
        Hostel hostel = (Hostel) session.getAttribute("hostel");
        if (hostel == null) return "redirect:/login";
        Complaint complaint = new Complaint();
        complaint.setStudent(hostel);
        complaint.setTitle(title);
        complaint.setDescrp(description);
        complaint.setCateg(category);
        complaint.setPrio(priority);
        complaint.setStatus("OPEN");
        complaint.setCreatedOn(LocalDate.now().toString());
        complaintRepo.save(complaint);
        return "redirect:/student/complaints";
    }

    // ─── ADMIN ──────────────────────────────────────────────

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        Hostel hostel = (Hostel) session.getAttribute("hostel");
        if (hostel == null) return "redirect:/login";
        List<Complaint> allComplaints = complaintRepo.findAll();
        long openCount = allComplaints.stream().filter(c -> "OPEN".equals(c.getStatus())).count();
        model.addAttribute("hostel", hostel);
        model.addAttribute("countOpen", openCount);
        model.addAttribute("occupied", hostelRepo.count());
        model.addAttribute("noticeCount", noticeRepo.count());
        return "admin/dashboard";
    }

    @GetMapping("/admin/students")
    public String adminStudents(HttpSession session, Model model) {
        Hostel hostel = (Hostel) session.getAttribute("hostel");
        if (hostel == null) return "redirect:/login";
        model.addAttribute("hostel", hostel);
        model.addAttribute("students", hostelRepo.findAll());
        return "admin/students";
    }

    @PostMapping("/admin/students/add")
    public String addStudent(@RequestParam String full_name,
                             @RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String phone,
                             @RequestParam String password,
                             @RequestParam String course,
                             @RequestParam String year,
                             @RequestParam String gender,
                             @RequestParam String program_level,
                             HttpSession session) {
        Hostel adminUser = (Hostel) session.getAttribute("hostel");
        if (adminUser == null) return "redirect:/login";
        Hostel newStudent = new Hostel();
        newStudent.setFull_name(full_name);
        newStudent.setUsername(username);
        newStudent.setEmail(email);
        newStudent.setPhone(phone);
        newStudent.setPassword(password);
        newStudent.setCourse(course);
        newStudent.setYear(year);
        newStudent.setGender(gender);
        newStudent.setProgram_level(program_level);
        newStudent.setRole("STUDENT");
        hostelRepo.save(newStudent);
        return "redirect:/admin/students";
    }

    @GetMapping("/admin/students/delete/{id}")
    public String deleteStudent(@PathVariable int id, HttpSession session) {
        Hostel hostel = (Hostel) session.getAttribute("hostel");
        if (hostel == null) return "redirect:/login";
        hostelRepo.deleteById(id);
        return "redirect:/admin/students";
    }

    @GetMapping("/admin/complaints")
    public String adminComplaints(HttpSession session, Model model) {
        Hostel hostel = (Hostel) session.getAttribute("hostel");
        if (hostel == null) return "redirect:/login";
        List<Complaint> allComplaints = complaintRepo.findAll();
        model.addAttribute("hostel", hostel);
        model.addAttribute("complaints", allComplaints);
        model.addAttribute("countOpen", allComplaints.stream().filter(c -> "OPEN".equals(c.getStatus())).count());
        model.addAttribute("progCount", allComplaints.stream().filter(c -> "IN_PROGRESS".equals(c.getStatus())).count());
        model.addAttribute("resolvedCount", allComplaints.stream().filter(c -> "RESOLVED".equals(c.getStatus())).count());
        model.addAttribute("total", allComplaints.size());
        return "admin/complaint";
    }

    @GetMapping("/admin/complaints/progress/{id}")
    public String markInProgress(@PathVariable int id, HttpSession session) {
        Hostel hostel = (Hostel) session.getAttribute("hostel");
        if (hostel == null) return "redirect:/login";
        Complaint complaint = complaintRepo.findById(id).orElse(null);
        if (complaint != null) {
            complaint.setStatus("IN_PROGRESS");
            complaintRepo.save(complaint);
        }
        return "redirect:/admin/complaints";
    }

    @GetMapping("/admin/complaints/resolve/{id}")
    public String resolveComplaint(@PathVariable int id, HttpSession session) {
        Hostel hostel = (Hostel) session.getAttribute("hostel");
        if (hostel == null) return "redirect:/login";
        Complaint complaint = complaintRepo.findById(id).orElse(null);
        if (complaint != null) {
            complaint.setStatus("RESOLVED");
            complaintRepo.save(complaint);
        }
        return "redirect:/admin/complaints";
    }

    @GetMapping("/admin/notices")
    public String adminNotices(HttpSession session, Model model) {
        Hostel hostel = (Hostel) session.getAttribute("hostel");
        if (hostel == null) return "redirect:/login";
        model.addAttribute("hostel", hostel);
        model.addAttribute("notices", noticeRepo.findAll());
        return "admin/notice";
    }

    @PostMapping("/admin/notices/add")
    public String addNotice(@RequestParam String title,
                            @RequestParam String message,
                            HttpSession session) {
        Hostel hostel = (Hostel) session.getAttribute("hostel");
        if (hostel == null) return "redirect:/login";
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setMessage(message);
        notice.setPosted_on(LocalDate.now().toString());
        noticeRepo.save(notice);
        return "redirect:/admin/notices";
    }

    @GetMapping("/admin/notices/delete/{id}")
    public String deleteNotice(@PathVariable int id, HttpSession session) {
        Hostel hostel = (Hostel) session.getAttribute("hostel");
        if (hostel == null) return "redirect:/login";
        noticeRepo.deleteById(id);
        return "redirect:/admin/notices";
    }
}