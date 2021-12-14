package com.renyujie.myeasyblog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.renyujie.myeasyblog.common.lang.Result;
import com.renyujie.myeasyblog.entity.Blog;
import com.renyujie.myeasyblog.service.BlogService;
import com.renyujie.myeasyblog.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author renyujie518
 * @since 2021-12-02
 */
@RestController
public class BlogController {
    @Autowired
    BlogService blogService;

    //当前页展示接口 前端传入"页数" 和  "当前页"
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        //1页有5条记录
        Page page = new Page(currentPage, pageSize);
        //按照生成时间倒序排
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        return Result.succ(pageData);
    }

    //博客详情页（点击打开） 前端传入id
    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        Blog blog = blogService.getById(id);
//        Assert.notNull(blog, "该微博已删除");
        if (blog == null) {
            return Result.fail("该微博已删除");
        }
        return Result.succ(blog);
    }

    //编辑（需要登录认证权限后访问）
    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {
        //blog有id是编辑状态 status = 1  没有id是添加状态
        Blog temp = null;
        if (blog.getId() != null) {//编辑状态，就要从数据库中查出来（给到temp），更新必要字段
            System.out.println("blog从前端接收:-----------"+blog.toString());
            temp = blogService.getById(blog.getId());
            System.out.println("temp被赋值前:----------"+temp.toString());
            //只能编辑自己的文章
            System.out.println(ShiroUtil.getProfile().getId());
            //通过数据库中存储的这篇blog的UserId与shiro中的Profile的id
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");
            //经过上面的断言的验证  可以修改temp的状态
            temp.setStatus(1);
        } else {
            //添加状态（status = 0） 新建
            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        }
        //把临时对象temp中的属性复制到blog对象中，忽略（固定信息 在编辑时 这些是不变的）没用信息
        // postman测试的时候只要title description  content字段
        //spring的包是前面赋给后面
        BeanUtils.copyProperties(blog, temp, "id", "userId", "created", "status");
        System.out.println("blog:++++++++"+blog.toString());
        System.out.println("temp被赋值后:++++++++"+temp.toString());
        //保存数据库
        blogService.saveOrUpdate(temp);
        return Result.succ(null);
    }

    //删除博客
    @RequiresAuthentication
    @GetMapping("/blogdel/{id}")
    public Result del(@PathVariable(name = "id") String id) {
        boolean delFalg = blogService.removeById(Integer.valueOf(id));
        if (delFalg) {
            return Result.succ("文章删除成功");
        } else {
            return Result.succ("文章删除失败");
        }

    }

}
