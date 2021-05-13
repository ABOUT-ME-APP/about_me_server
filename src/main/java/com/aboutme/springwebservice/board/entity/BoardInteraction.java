package com.aboutme.springwebservice.board.entity;

import com.aboutme.springwebservice.domain.UserInfo;
import com.aboutme.springwebservice.entity.BaseTimeEntity;
import io.swagger.models.auth.In;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "QnA_Like_Info")
public class BoardInteraction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @Column(name = "like_yn", columnDefinition = "TINYINT")
    private int likeYn;

    @Column(name = "scrap_yn", columnDefinition = "TINYINT")
    private int scrapYn;

    @ManyToOne(targetEntity = QnACategoryLevel.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_level_id",nullable = false)
    private QnACategoryLevel board;

    @ManyToOne(targetEntity = UserInfo.class, fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    @JoinColumn(name = "like_user", nullable = false)
    private UserInfo likeUser;

    @ManyToOne(targetEntity = UserInfo.class, fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    @JoinColumn(name = "author_id", nullable = false)
    private UserInfo authorId;

    @Builder
    public BoardInteraction(QnACategoryLevel board, UserInfo likeUser, int likeYn, UserInfo authorId){
        this.board=board;
        this.likeUser=likeUser;
        this.likeYn=likeYn;
        this.authorId=authorId;
    }

    public BoardInteraction(QnACategoryLevel board, UserInfo likeUser,UserInfo authorId, int scrapYn){
        this.board=board;
        this.likeUser=likeUser;
        this.authorId=authorId;
        this.scrapYn=scrapYn;
    }

    public void likeYes(){
        this.likeYn= 1;
    }
    public void likeNo(){
        this.likeYn=0;
    }
    public void scrapYes(){
        this.scrapYn=1;
    }
    public void scrapNo(){
        this.scrapYn=0;
    }

}


