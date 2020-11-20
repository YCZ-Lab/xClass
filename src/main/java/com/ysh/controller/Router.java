package com.ysh.controller;

import com.ysh.model.Reply;
import com.ysh.model.Topic;
import com.ysh.model.User;
import com.ysh.service.ReplyService;
import com.ysh.service.TopicService;
import com.ysh.service.UserService;
import com.ysh.utils.VerifyCode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Controller
public class Router {
    final UserService userService;
    final TopicService topicService;
    final ReplyService replyService;
    int size = 3;

    public Router(UserService userService, TopicService topicService, ReplyService replyService) {
        this.userService = userService;
        this.topicService = topicService;
        this.replyService = replyService;
    }

    @GetMapping("/forum")
    public ModelAndView doTopic(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "3") int size,
                                @RequestParam(defaultValue = "general") String region) {
        if (size <= 0) {
            size = 0;
        }
        this.size = size;
        int pages = topicService.getTopicPages(size, region);
        if (page > pages) {
            page = pages;
        }
        int start = (page - 1) * size;
        if (start <= 0) {
            start = 0;
        }
        List<Topic> topics = topicService.getTopicsByPage(start, size, region);
        ModelAndView mv = new ModelAndView();
        mv.addObject("topics", topics);
        mv.addObject("pages", pages);
        mv.addObject("page", page);
        mv.addObject("size", size);
//        if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
//            mv.addObject("logined", "false");
//        } else {
//            mv.addObject("logined", "true");
//        }
        mv.setViewName("topic");
        return mv;
    }

    @PostMapping("/submit/topic")
    public void saveTopic(String title, String content, HttpServletRequest req, HttpServletResponse resp,
                          String region) {
        Topic topic = new Topic();
        topic.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        topic.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
        topic.setTitle(title);
        topic.setContent(content);
        switch (region) {
            case "/vTopic":
                region = "vancouver";
                break;
            case "/cTopic":
                region = "california";
                break;
            case "/forum":
                region = "general";
                break;
        }
        topic.setRegion(region);
        topicService.save(topic);
        try {
            resp.sendRedirect(req.getHeader("Referer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/vTopic")
    public ModelAndView doVTopic(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "3") int size) {
        return doTopic(page, size, "vancouver");
    }

    @GetMapping("/cTopic")
    public ModelAndView doCTopic(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "3") int size) {
        return doTopic(page, size, "california");
    }

    @GetMapping("/reply")
    public ModelAndView doReply(int topicID, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
        if (size <= 0) {
            size = 3 / 0;
        }
        this.size = size;
        int pages = replyService.getReplyPagesByTopicId(topicID, size);
        if (pages <= 0) {
            pages = 1;
        }
        if (page > pages) {
            page = pages;
        }
        int start = (page - 1) * size;
        if (start <= 0) {
            start = 0;
        }
        Topic topic = topicService.getTopicById(topicID);
        ModelAndView mv = new ModelAndView();
        mv.addObject("topic", topic);
        List<Reply> replys = replyService.getReplysByTopicIdPage(topicID, start, size);
        mv.addObject("replys", replys);
        mv.addObject("pages", pages);
        mv.addObject("page", page);
        mv.addObject("size", size);
//        if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
//            mv.addObject("logined", "false");
//        } else {
//            mv.addObject("logined", "true");
//        }
        mv.setViewName("reply");
        return mv;
    }

    @PostMapping("/submit/reply")
    public void saveReply(int topicID, String content, HttpServletResponse resp) {
        Reply reply = new Reply();
        reply.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        reply.setTopicID(topicID);
        reply.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
        reply.setContent(content);
        replyService.save(reply);
        replyService.updateReplyNums(topicID);
        try {
            resp.sendRedirect("/reply?topicID=" + topicID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/register")
    public ModelAndView saveUser(String userName, String password, String email) {
        ModelAndView mv;
        User user = null;
        if (userName == null || userName.equals("")) {
            mv = new ModelAndView();
            mv.addObject("error", "Username can not be empty!");
            mv.setViewName("index");
            return mv;
        }
        try {
            user = (User) userService.loadUserByUsername(userName);
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(user);
        if (user != null) {
            mv = new ModelAndView();
            mv.addObject("error", user.getUserName() + " username already exist!");
            mv.setViewName("index");
        } else {
            mv = new ModelAndView("redirect:/login");
            User newUser = new User();
            newUser.setUserName(userName);
            newUser.setPassword(new BCryptPasswordEncoder(12).encode(password));
            newUser.setEmail(email);
            newUser.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
            newUser.setEnabled(true);
            newUser.setLocked(false);
            userService.save(newUser);
            userService.setRole(((User) userService.loadUserByUsername(userName)).getId());
        }
        return mv;
    }


    @GetMapping("/verifyCode")
    public void code(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        VerifyCode vc = new VerifyCode();
        BufferedImage image = vc.getImage();
        String verifyCode = vc.getText();
        HttpSession session = req.getSession();
        session.setAttribute("verifyCode", verifyCode);
        VerifyCode.output(image, resp.getOutputStream());
    }

//    @GetMapping("/login")
//    public ModelAndView login() {
//        ModelAndView mv = new ModelAndView("login");
//        return mv;
//    }
//
//    @GetMapping("/")
//    public ModelAndView root() {
//        ModelAndView mv = new ModelAndView("index");
//        return mv;
//    }
//
//    @GetMapping("/index")
//    public ModelAndView index() {
//        ModelAndView mv = new ModelAndView("index");
//        return mv;
//    }
//
//    @GetMapping("/jobs")
//    public ModelAndView jobs() {
//        ModelAndView mv = new ModelAndView("jobs");
//        return mv;
//    }
//
//    @GetMapping("/finances")
//    public ModelAndView finances() {
//        ModelAndView mv = new ModelAndView("fPodcasts");
//        return mv;
//    }
//
//    @GetMapping("/fArticles")
//    public ModelAndView fArticles() {
//        ModelAndView mv = new ModelAndView("fArticles");
//        return mv;
//    }
//
//    @GetMapping("/fVideos")
//    public ModelAndView fVideos() {
//        ModelAndView mv = new ModelAndView("fVideos");
//        return mv;
//    }
//
//    @GetMapping("/fPodcasts")
//    public ModelAndView fPodcasts() {
//        ModelAndView mv = new ModelAndView("fPodcasts");
//        return mv;
//    }
//
//    @GetMapping("/entrepreneurship")
//    public ModelAndView entrepreneurship() {
//        ModelAndView mv = new ModelAndView("ePodcasts");
//        return mv;
//    }
//
//    @GetMapping("/eArticles")
//    public ModelAndView eArticles() {
//        ModelAndView mv = new ModelAndView("eArticles");
//        return mv;
//    }
//
//    @GetMapping("/eVideos")
//    public ModelAndView eVideos() {
//        ModelAndView mv = new ModelAndView("eVideos");
//        return mv;
//    }
//
//    @GetMapping("/ePodcasts")
//    public ModelAndView ePodcasts() {
//        ModelAndView mv = new ModelAndView("ePodcasts");
//        return mv;
//    }
//
//    @GetMapping("/contact")
//    public ModelAndView contact() {
//        ModelAndView mv = new ModelAndView("contact");
//        return mv;
//    }
}
