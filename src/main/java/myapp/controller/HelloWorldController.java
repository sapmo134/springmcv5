package myapp.controller;

import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import myapp.model.HelloWorld;
import myapp.model.MyForm;

@Controller
public class HelloWorldController {
    final Logger logger = LoggerFactory.getLogger(HelloWorldController.class);

    @Inject
    private HttpServletRequest request;

    @RequestMapping("/helloworld")
    public String handler(Model model) {

        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setMessage("Hello World Example Using Spring MVC 5!!!");
        helloWorld.setDateTime(LocalDateTime.now().toString());
        model.addAttribute("helloWorld", helloWorld);
        return "helloworld";
    }

    @ModelAttribute()
    public MyForm init() {
        logger.info("enter init");
        return new MyForm();
    }

    static class ErrorMap {
        HashMap<String, String> mapLocal = new HashMap<String, String>();
        HashMap<String, String> mapGlobal = new HashMap<String, String>();
    }

    @RequestMapping("/data")
    public String data(MyForm myForm, Model model, BindingResult result) {
        logger.info("enter data {}", myForm);
        myForm.setCode("processed!!");
        model.addAttribute("formData", myForm);

        // エラー情報はmodelに格納する。受け取り側はServletRequestから取得する
        ErrorMap emap = new ErrorMap();
        emap.mapGlobal.put("E001", "エラー from data");
        emap.mapLocal.put("F001", "フィールドエラー from data");
        model.addAttribute("myapp.errorMap", emap);

        logger.info("leave");
        return "forward:/out2";
    }

    @RequestMapping("/out")
    public String out(MyForm myForm, Model model, BindingResult result) {
        logger.info("enter out {}",  myForm);
        MyForm formData = (MyForm)request.getAttribute("formData");
//        MyForm formData = (MyForm)model.getAttribute("formData");
        logger.info("formData={}", formData);

//        FieldError fieldError = new FieldError(result.getObjectName(), "myname", "エラー");
//        result.addError(fieldError);
//        logger.info("objectName: {}", result.getObjectName());
        logger.info("target={}, objectName={}", result.getTarget(), result.getObjectName());
        result.reject("E001", "エラーメッセージ1");
        result.reject("E002", "エラーメッセージ2");
        result.rejectValue("userName", "E003", "フィールドエラー");
//        logger.info("target={}, objectName={}", result.getTarget(), result.getObjectName());

//        // requestの内容を表示
//        StringBuilder sb = new StringBuilder();
//        sb.append("[");
//        boolean isFirst = true;
//        for (Enumeration<String> en = request.getAttributeNames(); en.hasMoreElements();) {
//            if (!isFirst) {
//                sb.append(",");
//            } else {
//                isFirst = false;
//            }
//            String k = en.nextElement();
//            Object o = request.getAttribute(k);
//            sb.append("(").append(k).append(",").append(o).append(")");
//        }
//        sb.append("]");
//        logger.info("req params={}", sb.toString());

        logger.info("out");
        return "out";
    }

    @RequestMapping("/out2")
    public String out(MyForm myFormData, Model model) {
        logger.info("enter out2 {}",  myFormData);

        // エラー情報の追加
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(myFormData, "myForm");
        ErrorMap emap = (ErrorMap)request.getAttribute("myapp.errorMap");
        if (emap != null) {
            for (Entry<String, String> entry : emap.mapGlobal.entrySet()) {
                bindingResult.reject(entry.getKey(), entry.getValue());
            }
            for (Entry<String, String> entry : emap.mapLocal.entrySet()) {
                bindingResult.rejectValue("userName", entry.getKey(), entry.getValue());
            }
        } else {
            bindingResult.reject("E004", "エラーメッセージ4");
            bindingResult.reject("E005", "エラーメッセージ5");
            bindingResult.rejectValue("userName", "E006", "フィールドエラー6");
        }
        // 以下のキー名称はSpringの仕様
        model.addAttribute("org.springframework.validation.BindingResult.myForm", bindingResult);

        logger.info("out2");
        return "out";
    }
}