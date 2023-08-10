package com.bit.springboard.repository;

import com.bit.springboard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//JpaRepository를 상속받으면 메소드를 구현하지 않아도
//제공되는 다양한 메소드들을 사용할 수 있다.
//List<T> findAll, List<T> findAll(Sort sort), saveAll, void flush,
//T findById....
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Optional<Board> findByBoardNo(int boardNo);

    Page<Board> findByBoardTitleContainingOrBoardContentContainingOrBoardWriterContaining(String searchKeyword, String searchKeyword1, String searchKeyword2, Pageable pageable);

    Page<Board> findByBoardTitleContaining(String searchKeyword, Pageable pageable);

    Page<Board> findByBoardContentContaining(String searchKeyword, Pageable pageable);

    Page<Board> findByBoardWriterContaining(String searchKeyword, Pageable pageable);
}
