package com.example.proiect.controller;

import com.example.proiect.exception.InternalErrorException;
import com.example.proiect.model.Blog;
import com.example.proiect.model.CategoryEnum;
import com.example.proiect.model.Comment;
import com.example.proiect.model.User;
import com.example.proiect.service.BlogService;
import com.example.proiect.service.ImageService;
import com.example.proiect.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@Slf4j
public class BlogController {

    private final BlogService blogService;
    private final ImageService imageService;
    private final UserService userService;

    @GetMapping("/blog/{blogId}")
    public String showById(@PathVariable("blogId") Long blogId, Model model){
        model.addAttribute("blog", blogService.findById(blogId));
        model.addAttribute("lastPosted", blogService.findLastPosted());
        model.addAttribute("comment", new Comment());
        return "blog";
    }

    @GetMapping({"/", "/index", "/blog/list"})
    public ModelAndView blogsList(@RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);
        ModelAndView modelAndView = new ModelAndView("blogs");
        Page<Blog> blogPage = blogService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        modelAndView.addObject("blogPage", blogPage);
        modelAndView.addObject("lastPosted", blogService.findLastPosted());
        modelAndView.addObject("blogSorted", false);
        return modelAndView;
    }

    @GetMapping("/blog/list/sort")
    public ModelAndView sortBlogs(@RequestParam("page") Optional<Integer> page,
                                    @RequestParam("size") Optional<Integer> size,
                                    @RequestParam("sortBy") Optional<String> sortBy){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);
        ModelAndView modelAndView = new ModelAndView("blogs");
        Page<Blog> blogPage = blogService.findPaginatedAndSorted(PageRequest.of(currentPage - 1, pageSize, Sort.by(sortBy.orElse(""))));
        modelAndView.addObject("blogPage", blogPage);
        modelAndView.addObject("lastPosted", blogService.findLastPosted());
        modelAndView.addObject("blogSorted", true);
        return modelAndView;
    }

    @GetMapping("/blog/new")
    public String newBlog(Model model) {
        model.addAttribute("blog", new Blog());
        List<CategoryEnum> categoriesAll = List.of(CategoryEnum.values());
        model.addAttribute("categoriesAll", categoriesAll );
        return "blog-form";
    }

    @PostMapping("/blog")
    public String save(@Valid @ModelAttribute("blog") Blog blog,
                       BindingResult bindingResult,
                       @RequestParam("images") MultipartFile[] images) throws IOException {
        if (bindingResult.hasErrors()){
            return "blog-form";
        }
        blog.setDate(LocalDateTime.now());
        blog.setUser(userService.getAuthenticatedUser());
        Blog savedBlog = blogService.save(blog);
        imageService.saveImageFile(savedBlog.getBlogId(), images);
        log.info("Successfully added blog with id {} by user {}", savedBlog.getBlogId(),
                savedBlog.getUser().getUsername());
        return "redirect:/blog/list" ;
    }

    @GetMapping("/blog/edit/{blogId}")
    public ModelAndView editBlog(@PathVariable("blogId") Long blogId){
        User authenticatedUser = userService.getAuthenticatedUser();
        Blog blog = blogService.findById(blogId);
        if(!authenticatedUser.equals(blog.getUser())) {
            throw new InternalErrorException("Access denied");
        }
        ModelAndView modelAndView = new ModelAndView("edit-blog");
        modelAndView.addObject("blog", blog);
        return modelAndView;
    }

    @GetMapping("/blog/editPictures/{blogId}")
    public ModelAndView editPictures(@PathVariable("blogId") Long blogId){
        User authenticatedUser = userService.getAuthenticatedUser();
        Blog blog = blogService.findById(blogId);
        if(!authenticatedUser.equals(blog.getUser())) {
            throw new InternalErrorException("Access denied");
        }
        ModelAndView modelAndView = new ModelAndView("picture-edit");
        modelAndView.addObject("blog", blog);
        return modelAndView;
    }


    @PostMapping("/blog/edit")
    public String updateBlog(@ModelAttribute @Valid Blog blog,
                             BindingResult bindingResult,
                             @RequestParam("images") MultipartFile[] images) throws IOException {

        if (bindingResult.hasErrors()) {
            return "edit-blog";
        }
        Blog dbBlog = blogService.findById(blog.getBlogId());
        blog.setUser(dbBlog.getUser());
        blog.getPictures().addAll(dbBlog.getPictures());
        blog.setDate(LocalDateTime.now());
        Blog savedBlog = blogService.save(blog);
        imageService.saveImageFile(savedBlog.getBlogId(), images);
        log.info("Successfully edited blog with id {}", savedBlog.getBlogId());
        return "redirect:/blog/" + blog.getBlogId();
    }

    @PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @RequestMapping("/blog/delete/{blogId}")
    public String deleteById(@PathVariable("blogId") Long blogId,
                             @RequestParam("username") String username){
        blogService.deleteById(blogId);
        log.info("User {} successfully deleted blog with id {}", username, blogId);
        return "redirect:/blog/list";
    }

    @RequestMapping("/blog/search")
    public String searchBlog(@RequestParam("searchInput") String searchInput,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size,
                             Model model) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);
        Page<Blog> blogPage = blogService.findByTitle(searchInput, PageRequest.of(currentPage -1, pageSize));
        model.addAttribute("blogPage", blogPage);
        model.addAttribute("searchInput", searchInput);
        model.addAttribute("lastPosted", blogService.findLastPosted());
        return "blogs";
    }


}
