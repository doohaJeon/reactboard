package com.bit.springboard.controller;

import com.bit.springboard.dto.BoardDTO;
import com.bit.springboard.dto.ResponseDTO;
import com.bit.springboard.entity.Board;
import com.bit.springboard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController //@Controller + @ResponseBody
@RequestMapping("/api")
public class ApiContorller {
    private BoardService boardService;

    //생성자 주입
    @Autowired
    public ApiContorller(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/testApi")
    public Map<String, String> testApi() {
        Map<String, String> returnMap = new HashMap<String, String>();

        returnMap.put("first", "one");
        returnMap.put("second", "two");

        return returnMap;
    }

    @GetMapping("/board")
    public ResponseEntity<?> getBoard(int boardNo) {
        ResponseDTO<BoardDTO> responseDTO =
                new ResponseDTO<BoardDTO>();
        try {
            //responseDTO.setItem(boardService.getBoard(boardNo));

            return ResponseEntity.ok().body(responseDTO);
        } catch(Exception e) {
            responseDTO.setErrorMessage(e.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //글 등록
    @PostMapping("/board")
    public ResponseEntity<?> insertBoard(BoardDTO boardDTO) {
        ResponseDTO<Map<String, Object>> responseDTO
                    = new ResponseDTO<Map<String, Object>>();

        try {
            //boardService.insertBoard(boardDTO);

            Map<String, Object> returnMap =
                    new HashMap<String, Object>();

            returnMap.put("msg", "정상적으로 입력되었습니다.");

            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //글수정
    @PutMapping("/board")
    public ResponseEntity<?> updateBoard(BoardDTO boardDTO) {
        ResponseDTO<Map<String, Object>> responseDTO =
                new ResponseDTO<Map<String, Object>>();

        try {
            Map<String, Object> returnMap =
                new HashMap<String, Object>();
            if(boardService.getBoard(boardDTO.getBoardNo()) == null) {
                returnMap.put("msg", "해당 게시글이 존재하지 않습니다.");

                responseDTO.setItem(returnMap);

                responseDTO.setStatusCode(HttpStatus.OK.value());

                return ResponseEntity.ok().body(responseDTO);
            }

            //boardService.updateBoard(boardDTO);

            returnMap.put("msg", "정상적으로 수정되었습니다.");

            responseDTO.setItem(returnMap);

            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch(Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //글삭제
    @DeleteMapping("/board")
    public ResponseEntity<?> deleteBoard(BoardDTO boardDTO) {
        ResponseDTO<Map<String, Object>> responseDTO =
                new ResponseDTO<Map<String, Object>>();

        try {
            Map<String, Object> returnMap =
                    new HashMap<String, Object>();

            if(boardService.getBoard(boardDTO.getBoardNo()) == null) {
                returnMap.put("msg", "해당 게시글이 존재하지 않습니다.");

                responseDTO.setItem(returnMap);

                responseDTO.setStatusCode(HttpStatus.OK.value());

                return ResponseEntity.ok().body(responseDTO);
            }

            boardService.deleteBoard(boardDTO.getBoardNo());

            returnMap.put("msg", "정상적으로 삭제되었습니다.");

            responseDTO.setStatusCode(HttpStatus.OK.value());

            responseDTO.setItem(returnMap);

            return ResponseEntity.ok().body(responseDTO);
        } catch(Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }



    @GetMapping("/testApi2")
    public Map<String, Object> testApi2(int boardNo) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        returnMap.put("board", boardService.getBoard(boardNo));
        returnMap.put("boardList", boardService.getBoardList());
        List<Integer> intList = new ArrayList<Integer>();

        for(int i = 0; i < 100; i++) {
            intList.add(i);
        }
        returnMap.put("intList", intList);

        return returnMap;
    }
    
    //ResponseEntity를 이용한 RESTful API
    //응답코드, 에러메시지 등을 전송해주는 객체
    //응답 바디에 전송할 데이터도 추가할 수 있다.
    @GetMapping("/restfulapi")
    public ResponseEntity<?> restFulApi(int boardNo) {
        ResponseDTO<Map<String, Object>> responseDTO =
                new ResponseDTO<Map<String, Object>>();
        try {
            Map<String, Object> returnMap = new HashMap<String, Object>();

            returnMap.put("board", boardService.getBoard(boardNo));
            returnMap.put("boardList", boardService.getBoardList());
            List<Integer> intList = new ArrayList<Integer>();

            for(int i = 0; i < 100; i++) {
                intList.add(i);
            }
            returnMap.put("intList", intList);
            
            //응답 데이터
            responseDTO.setItem(returnMap);
            //요청 상태코드
            responseDTO.setStatusCode(HttpStatus.OK.value());
            //request status code == 200일때(정상)
            //요청이 정상적으로 끝나면 응답 데이터와 요청 상태코드를 갖는
            //객체를 응답으로 보내줌
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            //요청 상태코드
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            //에러가 발생하면 응답데이터 대신 에러메시지를 전송한다.
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/board-list")
    public ResponseEntity<?> getBoardList(@PageableDefault(page=0, size=10) Pageable pageable) {
        ResponseDTO<BoardDTO> responseDTO = new ResponseDTO<>();


        try {
            String searchCondition = "all";
            String searchKeyword = "";
            Page<Board> pageBoardList = boardService.getBoardList(pageable, searchCondition, searchKeyword);

            Page<BoardDTO> pageBoardDTOList = pageBoardList.map(pageBoard ->
                BoardDTO.builder()
                        .boardTitle(pageBoard.getBoardTitle())
                        .boardCnt(pageBoard.getBoardCnt())
                        .boardContent(pageBoard.getBoardContent())
                        .boardWriter(pageBoard.getBoardWriter())
                        .boardRegdate(pageBoard.getBoardRegdate().toString())
                        .boardNo(pageBoard.getBoardNo())
                        .build()
            );

            responseDTO.setPageItems(pageBoardDTOList);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDTO.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }


    }






}
