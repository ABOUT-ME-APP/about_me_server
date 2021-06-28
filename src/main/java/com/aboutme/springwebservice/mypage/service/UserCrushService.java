package com.aboutme.springwebservice.mypage.service;


import com.aboutme.springwebservice.board.entity.BoardInteraction;
import com.aboutme.springwebservice.board.entity.DefaultEnquiry;
import com.aboutme.springwebservice.board.entity.QnACategory;
import com.aboutme.springwebservice.board.entity.QnACategoryLevel;
import com.aboutme.springwebservice.board.repository.*;
import com.aboutme.springwebservice.domain.UserInfo;
import com.aboutme.springwebservice.entity.BasicResponse;
import com.aboutme.springwebservice.entity.CommonResponse;
import com.aboutme.springwebservice.entity.ErrorResponse;
import com.aboutme.springwebservice.mypage.model.response.ResponseCrushList;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class UserCrushService {
    private final BoardInteractionRepository boardInteractionRepository;
    private final QnACategoryLevelRepository qnACategoryLevelRepository;
    private final QnACommentRepository qnACommentRepository;
    private final QnACategoryRepository qnACategoryRepository;
    private final DefaultEnquiryRepository defaultEnquiryRepository;

    @Transactional
    public ResponseEntity<?extends BasicResponse>  crushLists(Long userId, String cursh,int color){
        int likeYn = 1, scarpYn = 1;
        int commentCount ;
        List<ResponseCrushList> responseBoardSeq  = new ArrayList<>();
        List<ResponseCrushList> responseCrushList = new ArrayList<>();
        List<BoardInteraction> boardInteractions;
        QnACategoryLevel qnACategoryLevel;
        QnACategory qnACategory;
        DefaultEnquiry defaultEnquiry;
        //TODO: 추후 엔티티를 JPA 형식으로 변환후 고려 너무 많은 repository 접속
        //수정예정   코드 테스트용 유저 삽입
        UserInfo userInfo=UserInfo.builder().seq(userId).build();

        if(cursh.equals("likes")){
             boardInteractions =  boardInteractionRepository.findByLikeUserAndLikeYn(userInfo,likeYn);

            for(BoardInteraction boardInteraction : boardInteractions){
                responseBoardSeq.add(this.convertBoardSeq(boardInteraction));
            }

            for(int i=0; i<responseBoardSeq.size(); i++) {
                qnACategoryLevel = qnACategoryLevelRepository.findBySeq(responseBoardSeq.get(i).getBoardSeq());
                commentCount     = qnACommentRepository.countByCategoryLevelId(responseBoardSeq.get(i).getBoardSeq());
                qnACategory      = qnACategoryRepository.findBySeq(qnACategoryLevel.getCategoryId());
                defaultEnquiry   = defaultEnquiryRepository.findBySeq(qnACategory.getTitle_id());
                if(qnACategory.getColor()==color||color==-1){
                    responseCrushList.add(this.convertList(qnACategoryLevel, commentCount,
                            convertColor(qnACategory.getColor()), defaultEnquiry.getQuestion(),responseBoardSeq.get(i)));
                }

            }

        }else if(cursh.equals("scrap")){
             boardInteractions   = boardInteractionRepository.findByLikeUserAndScrapYn(userInfo, scarpYn);

            for(BoardInteraction boardInteraction : boardInteractions){
                responseBoardSeq.add(this.convertBoardSeq(boardInteraction));
            }

            for(int i=0; i<responseBoardSeq.size(); i++) {
                qnACategoryLevel = qnACategoryLevelRepository.findBySeq(responseBoardSeq.get(i).getBoardSeq());
                commentCount = qnACommentRepository.countByCategoryLevelId(responseBoardSeq.get(i).getBoardSeq());
                qnACategory = qnACategoryRepository.findBySeq(qnACategoryLevel.getCategoryId());
                defaultEnquiry = defaultEnquiryRepository.findBySeq(qnACategory.getTitle_id());
                if (qnACategory.getColor() == color||color==-1) {
                    responseCrushList.add(this.convertList(qnACategoryLevel, commentCount,
                            convertColor(qnACategory.getColor()), defaultEnquiry.getQuestion(),responseBoardSeq.get(i)));

                }
            }

        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ErrorResponse("잘못된 접근입니다"));
        }

       return ResponseEntity.ok().body( new CommonResponse<List>(responseCrushList));
    }

    //좋아요 글번호추출
    private ResponseCrushList convertBoardSeq(BoardInteraction boardInteraction){
        return ResponseCrushList.builder().seq(boardInteraction.getBoard().getSeq())
                                          .hasliked(boardInteraction.getLikeYn())
                                          .hasscraped(boardInteraction.getScrapYn())
                                          .build();
    }

    //글번호로 리스트 변환
    private ResponseCrushList convertList(QnACategoryLevel qnACategoryLevel, int commentCount, String color, String question, ResponseCrushList responseCrushList){
        return new ResponseCrushList(qnACategoryLevel, commentCount, color, question , responseCrushList);
    }

    //색 문자열로 변환
    private String convertColor(int color){
        String colorString="";
        switch (color) {
            case 0: colorString = "red"; break;
            case 1: colorString = "yellow"; break;
            case 2: colorString = "green"; break;
            case 3: colorString = "pink"; break;
            case 4: colorString = "purple"; break;
        }
        return colorString;
    }
}
