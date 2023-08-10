package com.bit.springboard.service;

import com.bit.springboard.entity.Board;
import com.bit.springboard.entity.BoardFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {
    Board getBoard(int boardNo);

    List<Board> getBoardList();

    void insertBoard(Board board, List<BoardFile> uploadFileList);

    void updateBoard(Board board, List<BoardFile> uploadFileList);

    void deleteBoard(int boardNo);

    List<BoardFile> getBoardFileList(int boardNo);

    Page<Board> getBoardList(Pageable pageable, String searchCondition, String searchKeyword);
}
