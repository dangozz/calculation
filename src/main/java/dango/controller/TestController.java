package dango.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dango.aop.test.MyComponent;
import dango.model.UserModel;
import dango.rabbit.ProducerService;
import dango.service.RoleService;
import dango.service.UserService;
import dango.service.impl.TestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class TestController {

    @Autowired
    private RoleService roleService;

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @RequestMapping("calculation.do")
    @ResponseBody
    public List Calculation(@RequestBody String json) {
        Map<String, String> map = new Gson().fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
//        logger.info(this.getClass().getName()+"入参___"+json);

        DecimalFormat df = new DecimalFormat("######0.00");

        UserModel userModel=new UserModel();
        userModel.setId(1);
        System.out.println(roleService.findUserRoles(userModel));

        System.out.println(map.get("in1"));
        Double price1 = Double.parseDouble(map.get("in1"));
        Double price2 = Double.parseDouble(map.get("in2"));
        Double price3 = Double.parseDouble(map.get("in3"));
        Double price4 = Double.parseDouble(map.get("in4"));
        Double price5 = Double.parseDouble(map.get("in5"));
        Double price6 = Double.parseDouble(map.get("in6"));
        Double price7 = Double.parseDouble(map.get("in7"));
        Double priceSum = price1 + price2 + price3 + price4 + price5 + price6 + price7;
        Double sum = Double.parseDouble(map.get("sum"));
        Double precent = sum / priceSum;
        List list = new ArrayList();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("out1", df.format(price1 * precent));
        resultMap.put("out2", df.format(price2 * precent));
        resultMap.put("out3", df.format(price3 * precent));
        resultMap.put("out4", df.format(price4 * precent));
        resultMap.put("out5", df.format(price5 * precent));
        resultMap.put("out6", df.format(price6 * precent));
        resultMap.put("out7", df.format(price7 * precent));
        list.add(resultMap);


        return list;
    }

    @RequestMapping("perfusion.do")
    @ResponseBody
    public List Perfusion(@RequestBody String json) {
//        logger.info(this.getClass().getName()+"入参___"+json);

        Map<String, String> map = new Gson().fromJson(json, new TypeToken<Map<String, String>>() {}.getType());

        int lv=Integer.parseInt(map.get("lv"));
        double price=Double.parseDouble(map.get("price"));

        DecimalFormat df = new DecimalFormat("######0.00");

        System.out.println(lv+price);

        double prices[]=new double[15];
        for(int i=lv-1;i<15;i++){
            prices[i]=Double.parseDouble(df.format(price));
            price=price*2+1.496;
        }
        List list = new ArrayList();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("prices", prices);
        resultMap.put("lv9",df.format(prices[8]/0.9));
        list.add(resultMap);
        return list;
    }


    @Value("#{config['mq.queue']}")
    private String queue_name;

    @Autowired
    private ProducerService producerService;

    @RequestMapping("1.do")
    @RequiresPermissions("test:1")
    public String Test1(){

//        logger.info(this.getClass().getName()+"入参___");

        Subject subject=SecurityUtils.getSubject();
        Map<String,Object> map=new HashMap<>();
        map.put("name",subject.getPrincipal());
        producerService.sendQueue(queue_name +"_exchange", queue_name +"_patt",map);

        return "1";
    }

    @Autowired
    private TestService testService;
    @Autowired
    private UserService userService;

    @RequestMapping("2.do")
    @RequiresPermissions("test:2")
    public String Test2(){


        List<UserModel> userModels=userService.findAllModel();
        System.out.println("**************************************************************");
        System.out.println(userModels);

        Page page=PageHelper.startPage(2,2,true);
        userModels=userService.findAllModel();
        System.out.println("**************************************************************---------------");
        System.out.println(userModels);

//        logger.info(this.getClass().getName()+"入参___");
        System.out.println(testService.test("DANGO_"));
        return "2";
    }

    @MyComponent
    @RequestMapping("3.do")
    @RequiresPermissions("test:3")
    public String Test3(){
//        logger.info(this.getClass().getName()+"入参___");
        System.out.println(userService.findUserByName("DANGO_"));
        return "3";
    }

    @MyComponent(desc = "desc1")
    @RequestMapping("4.do")
    @RequiresPermissions("test:4")
    public String Test4(){
        return "4";
    }

    @RequestMapping("5.do")
    @RequiresPermissions("test:1")
    public String Tes5(){
        System.out.println(testService.test2("DANGO_"));
        return "5";
    }
}
