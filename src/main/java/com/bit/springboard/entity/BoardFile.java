package com.bit.springboard.entity;

import com.bit.springboard.dto.BoardFileDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_BOARD_FILE")
@Data
//boardNo가 FK, PK
//boardFileNo도 PK
//다중 PK 매핑방식
//1. Id클래스
//2. EmbededId
//다중 pk일 때 해당 엔티티에서 사용하는 Id 클래스를 지정
@IdClass(BoardFileId.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardFile {
    @Id
    //3) 관계지정: 일대일, 다대일, 일대다 등등
    @ManyToOne
    //2) FK로 가져올 컬럼지정
    @JoinColumn(name = "BOARD_NO")
    //1) boardNo를 가져올 객체를 멤버 변수로 선언
    private Board board;
    @Id
    private int boardFileNo;
    private String boardFileName;
    private String boardFilePath;
    private String boardFileOrigin;
    private String boardFileCate;

    @Transient
    private String boardFileStatus;

    @Transient
    private String newFileName;

    public BoardFileDTO EntityToDTO() {
        BoardFileDTO boardFileDTO = BoardFileDTO.builder()
                .boardNo(this.board.getBoardNo())
                .boardFileNo(this.boardFileNo)
                .boardFileName(this.boardFileName)
                .boardFilePath(this.boardFilePath)
                .boardFileOrigin(this.boardFileOrigin)
                .boardFileCate(this.boardFileCate)
                .build();

        return boardFileDTO;
    }
}
