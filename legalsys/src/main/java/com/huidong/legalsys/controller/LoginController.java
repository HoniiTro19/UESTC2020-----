package com.huidong.legalsys.controller;

import com.huidong.legalsys.domain.User;
import com.huidong.legalsys.enumeration.ErrorEnum;
import com.huidong.legalsys.exception.LegalsysException;
import com.huidong.legalsys.service.LoginService;
import com.huidong.legalsys.service.UploadService;
import com.huidong.legalsys.util.ValidateIdnoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @Description 用户注册登录的控制层
 */
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UploadService uploadService;

    /**
     * @Description 转入用户登录界面或主页
     * @param request Http请求
     * @return 系统登录界面
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null){
            return "redirect:/";
        }
        return "login";
    }

    /**
     * @Description 用户登录验证
     * @param phone 手机号
     * @param password 密码
     * @param request Http请求
     * @return 系统登录界面
     */
    @PostMapping("/login/verification")
    public String verification(@RequestParam("phone") String phone,
                               @RequestParam("password") String password,
                               HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = loginService.login(phone, password);
        session.setAttribute("user", user);
        return "redirect:/";
    }

    /**
     * @Description 用户登出法律咨询系统
     * @param request http请求
     * @return 主页
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        session.setAttribute("user", null);
        return "redirect:/";
    }

    /**
     * @Description 提供普通用户以及律师用户两种不同的注册通道
     * @return 相应的用户注册界面
     */
    @GetMapping("/register")
    public String register(){
        return "register/index";
    }

    /**
     * @Description 普通用户注册信息收集
     * @return 信息收集界面
     */
    @GetMapping("/register/normal")
    public String normal(){
        return "register/registerNormal";
    }

    /**
     * @Description 普通用户注册信息收集
     * @param phone 手机号
     * @param name 真实姓名
     * @param password 密码
     * @param idno 身份证号
     * @return 信息收集界面
     */
    @PostMapping("/register/normal/upload")
    public String normalUpload(@RequestParam("phone") String phone,
                               @RequestParam("name") String name,
                               @RequestParam("password") String password,
                               @RequestParam("verify") String verify,
                               @Valid @RequestParam("idno") String idno){
        if (!password.equals(verify)) {
            throw new LegalsysException(ErrorEnum.VERIFYNOTMATCH);
        }
        if (!phone.matches("^(13|15|18)\\d{9}$")) {
            throw new LegalsysException(ErrorEnum.PHONELEN_ERROR);
        }

        if (!ValidateIdnoUtil.isIdno(idno)) {
            throw new LegalsysException(ErrorEnum.IDNOLEN_ERROR);
        }
        User user = new User();
        user.setPhone(phone);
        user.setName(name);
        user.setPassword(password);
        user.setIdno(idno);
        loginService.register(phone, name, password, idno);
        return "redirect:/login";
    }

    /**
     * @Description 律师用户的信息收集
     * @return 信息收集界面
     */
    @GetMapping("/register/lawyer")
    public String lawyer(){
        return "register/registerLawyer";
    }

    /**
     * @Description 律师用户的信息收集
     * @param phone 手机号
     * @param name 真实姓名
     * @param password 密码
     * @param idno 身份证号
     * @param licensefile 律师执照
     * @param firmname 律所信息
     * @return 信息收集界面
     */
    @PostMapping("/register/lawyer/upload")
    public String lawyerUpload(@RequestParam("phone") String phone,
                               @RequestParam("name") String name,
                               @RequestParam("password") String password,
                               @RequestParam("verify") String verify,
                               @RequestParam("idno") String idno,
                               @RequestParam("firmname") String firmname,
                               @RequestParam("category") String category,
                               @RequestParam("description") String description,
                               @RequestParam("licensefile") MultipartFile licensefile,
                               MultipartHttpServletRequest request){
        if (!password.equals(verify)) {
            throw new LegalsysException(ErrorEnum.VERIFYNOTMATCH);
        }
        if (!phone.matches("^(13|15|18)\\d{9}$")) {
            throw new LegalsysException(ErrorEnum.PHONELEN_ERROR);
        }

        if (!ValidateIdnoUtil.isIdno(idno)) {
            throw new LegalsysException(ErrorEnum.IDNOLEN_ERROR);
        }
        String lincenseurl = uploadService.LicenseUpload(licensefile, request, phone);
        User user = new User();
        user.setPhone(phone);
        user.setName(name);
        user.setPassword(password);
        user.setIdno(idno);
        user.setLicenseurl(lincenseurl);
        user.setFirmname(firmname);
        user.setCategory(category);
        user.setDescription(description);
        loginService.registerLawyer(phone, name, password, idno, lincenseurl, firmname, category, description);
        return "redirect:/login";
    }

}
