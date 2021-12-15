package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dao.CommentsDao;
import com.example.domain.Comment;
import com.example.domain.SysUser;
import com.example.service.CommentService;
import com.example.service.SysUserService;
import com.example.utils.UserThreadLocal;
import com.example.vo.CommentVo;
import com.example.vo.Result;
import com.example.vo.UserVo;
import com.example.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceimpl implements CommentService {

    /**
     * 评论列表功能
     *1.根据文章id 查询 评论列表 从comments表格中进行相关信息的查询
     *2.根据 作者id 查询到作者的信息
     *3. 判断 如果level 就是有二级评论 我们就要去看他有没有子评论
     * 4.如果有,根据之前的评论的id进行查询他的parent_id
     */
    @Autowired
    private CommentsDao commentsDao;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result CommentsByArticleId(Long ArticleId) {
        LambdaQueryWrapper<Comment> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,ArticleId);
        //先查找一级评论
        queryWrapper.eq(Comment::getLevel,1);

        List<Comment> comments = commentsDao.selectList(queryWrapper);
        List<CommentVo> commentVos= copylist(comments);
        return Result.success(commentVos);
    }

    /**
     * 添加评论
     * @param commentParam
     * @return
     */
    @Override
    public Result addComments(CommentParam commentParam) {
        //重新创建一个线程 , 评论功能设置 允许多个用户一起进行评论 设置多个线程 相互之间 不进行影响
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        //评论的作者(主人)是当前的SysUser
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentsDao.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> copylist(List<Comment> comments) {
        List<CommentVo> commentVos = new ArrayList<>();
        for (Comment comment : comments) {
            CommentVo commentVo = copy(comment);
            commentVos.add(commentVo);
        }
        return commentVos;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        //作者信息
        Long authorId = comment.getAuthorId();
        UserVo userVo = sysUserService.findAuthorById(authorId);
        commentVo.setAuthor(userVo);
        //找到子评论(二级评论)
        if(comment.getLevel()==1){
            //先拿到对应的评论的comments id
            Long id = comment.getId();

           List<CommentVo> childrenComments =findCommentsByParentsId(id);
           commentVo.setChildrens(childrenComments);
        }
        //to user 给哪一条评论进行的评论
        if(comment.getLevel()>1){
            Long toUid =comment.getToUid();
            UserVo toUserVo = new UserVo();
           toUserVo = sysUserService.findAuthorById(toUid);
           commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentsId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper  = new LambdaQueryWrapper();
        queryWrapper.eq(Comment::getParentId,id).eq(Comment::getLevel,2);
        List<Comment> comments = commentsDao.selectList(queryWrapper);
        return copylist(comments);

    }



}
