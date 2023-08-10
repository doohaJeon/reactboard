import axios from 'axios';
import React, { useCallback, useEffect, useState } from 'react';
import {Link, useNavigate, useParams} from 'react-router-dom';
import FileList from './FileList';

const Board = () => {
    const {boardNo} = useParams();
    const navi = useNavigate();

    //const uploadFiles = [];

    const [board, setBoard] = useState(null);
    const [boardFileList, setBoardFileList] = useState(null);
    const [originFileList, setOriginFileList] = useState([]);
    const [changeFileList, setChangeFileList] = useState([]);
    const [uploadFiles, setUploadFiles] = useState([]);

    useEffect(() => {
        const getBoard = async () => {
            try {
                const response = await axios.get(`http://localhost:9090/board/board/${boardNo}`, {
                    headers: {
                        Authorization: `Bearer ${sessionStorage.getItem("ACCESS_TOKEN")}`
                    }
                });

                console.log(response);
                if(response.data && response.data.item.board) {
                    setBoard(() => response.data.item.board);
                    setBoardFileList(() => response.data.item.boardFileList);
                }
            } catch(e) {
                console.log(e);
            }
        }

        getBoard();
    }, []);

    useEffect(() => {
        boardFileList && boardFileList.forEach(boardFile => {
            const originBoardFile = {
                boardNo: board.boardNo,
                boardFileNo: boardFile.boardFileNo,
                boardFileName: boardFile.boardFleName,
                boardFileStatus: "N"
            }

            setOriginFileList((prev) => prev.concat(originBoardFile));
        });
    }, [boardFileList]);

    const addFiles = useCallback((e) => {
        const fileList = Array.prototype.slice.call(e.target.files);
        
        setUploadFiles(() => e.target.files);
        
        fileList.forEach(file => {
            imageLoader(file);
        })
    }, []);

    const deleteBoard = useCallback((e) => {
        e.preventDefault();

        const deleteBoardAxios = async () => {
            try {
                const response = await axios.delete(`http://localhost:9090/board/board/${boardNo}`, {
                    headers: {
                        Authorization: `Bearer ${sessionStorage.getItem("ACCESS_TOKEN")}`
                    }
                });

                console.log(response);

                if(response.data && response.data.item.msg) {
                    alert(response.data.item.msg);
                    navi('/board-list');
                }
            } catch(e) {
                console.log(e);
            }
        }

        deleteBoardAxios();
    }, []);

    //미리보기 메소드
    //미리보기 영역에 들어갈 img 태그 생성 및 선택된 파일을
    //Base 64 인코딩된 문자열 형태로 변환하여 미리보기 구현
    const imageLoader = (file) => {

        let reader = new FileReader();

        reader.onload = (e) => {
          //이미지 표출할 img 태그 생성
          let img = document.createElement("img");
          img.setAttribute("style", "width: 100%; height: 100%; z-index: none;");

          //이미지 파일인지 아닌지 체크
          if(file.name.toLowerCase().match(/(.*?)\.(jpg|jpeg|png|gif|svg|bmp)$/)) {
            img.src = e.target.result;
          } else {
            img.src = 'images/defaultFileImg.png';
          }

          //미리보기 영역에 추가
          //미리보기 이미지 태그와 삭제 버튼 그리고 파일명을 표출하는 p태그
          //묶어주는 div를 만들어서 미리보기 영역에 추가
          document.getElementById('attZone').appendChild(makeDiv(img, file));
        }

        //파일을 BASE 64 인코딩 문자열로 변경
        reader.readAsDataURL(file);
      }

      //미리보기 영역에 들어갈 div 생성하는 메소드
      const makeDiv = (img, file) => {
        //div 태그 생성
        let div = document.createElement("div");

        div.setAttribute("style", "display: inline-block;"
                + " position: relative;"
                + " width: 150px; height: 120px;"
                + " margin: 5px; border: 1px solid #00f; z-index: 1");

        //잘못 올렸을 경우 삭제할 수 있는 버튼 생성
        let btn = document.createElement("input");
        btn.setAttribute("type", "button");
        btn.setAttribute("value", "x");
        btn.setAttribute("delFile", file.name);
        btn.setAttribute("style", "width: 30px; height: 30px;"
                + " position: absolute; right: 0px; bottom: 0px;"
                + " z-index: 999; background-color: rgba(255, 255, 255, 0.1);"
                + " color: #f00;");

        //버튼 이벤트 생성
        //버튼 클릭하면 해당 파일삭제 기능 구현
        btn.onclick = (e) => {
          //클릭된 버튼
          const ele = e.target;

          const delFile = ele.getAttribute("delFile");

          for(let i = 0; i < uploadFiles.length; i++) {
            //배열에 담겨있는 파일중 파일명이 같은 파일 삭제
            if(delFile === uploadFiles[i].name) {
              //배열에서 i번째 하나 제거
              uploadFiles.splice(i, 1);
            }
          }

          //버튼 클릭 시 btnAtt 인풋에서도 첨부된 파일 삭제
          //input type="file"은 첨부된 파일을 fileList의 형태로 관리
          //fileList에는 일반적인 File 객체를 넣을 수 없다.
          //DataTransfer라는 클래스를 이용해서 완전한 fileList 형태로 만들어서
          //input.files에 넣어줘야 한다.

          const dt = new DataTransfer();

          uploadFiles.forEach(file => dt.items.add(file));

          document.getElementById('btnAtt').files = dt.files;

          //해당 img 태그를 담고 있는 div 삭제
          const parentDiv = ele.parentNode;
          parentDiv.remove();
        }

        //파일명 표출할 p태그 생성
        const fName = document.createElement("p");
        fName.setAttribute("style", "display: inline-block; font-size: 8px;");
        fName.textContent = file.name;

        //div에 하나씩 추가
        div.appendChild(img);
        div.appendChild(btn);
        div.appendChild(fName);

        //완성된 div 리턴
        return div;
    }

    const addChangeFile = (file) => {
        setChangeFileList((prev) => prev.concat(file));
    }

    const changeOriginFile = (boardFileNo, status, newFileName) => {
        if(status === "U") {
            setOriginFileList(() => originFileList.map(originFile => 
                originFile.boardFileNo === boardFileNo ? {
                    ...originFile,
                    boardFileStatus: "U",
                    newFileName: newFileName
                } : originFile
            ))
        } else if(status === "D") {
            setOriginFileList(() => originFileList.map(originFile => 
                originFile.boardFileNo === boardFileNo ? {
                    ...originFile,
                    boardFileStatus: "D"
                } : originFile
            ))
        }
    }

    const updateBoard = useCallback((e) => {
        e.preventDefault();

        const formData = new FormData(e.target);

        const formDataObj = {};

        formData.forEach((value, key) => formDataObj[key] = value);

        const sendFormData = new FormData();

        sendFormData.append("boardDTO", new Blob([JSON.stringify(formDataObj)], {
            type: 'application/json'
        }));

        Array.from(uploadFiles).forEach(el => {
            sendFormData.append("uploadFiles", el);
        });

        Array.from(changeFileList).forEach(el => {
            sendFormData.append("changeFileList", el);
        });

        sendFormData.append("originFileList", JSON.stringify(originFileList));

        const updateBoardAxios = async () => {
            try {
                const response = await axios.put('http://localhost:9090/board/board', sendFormData, {
                    headers: {
                        Authorization: `Bearer ${sessionStorage.getItem("ACCESS_TOKEN")}`,
                        "Content-Type": "multipart/form-data"
                    }
                });

                console.log(response);

                if(response.data && response.data.item.board) {
                    alert("정상적으로 수정되었습니다.");
                    setBoard(() => response.data.item.board);
                    setBoardFileList(() => response.data.item.boardFileList);
                }
            } catch(e) {
                console.log(e);
            }
        }

        updateBoardAxios();
    }, [board, boardFileList, uploadFiles, originFileList, changeFileList]);

    const changeTitle = useCallback((e) => {
        setBoard((prev) => ({...prev, boardTitle: e.target.value}));
    }, []);

    const changeContent = useCallback((e) => {
        setBoard((prev) => ({...prev, boardContent: e.target.value}));
    }, []);
  return (
    <div style={{display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center'}}>
        <h3>게시글 상세</h3>
        <form id="updateForm" onSubmit={updateBoard}>
            <input type='hidden' name="boardNo" value={board ? board.boardNo : ''}></input>
            <input type='hidden' name='boardRegdate' value={board ? board.boardRegdate : ''}></input>
            <table style={{borderCollapse: 'collapse', border: '1px solid black'}}>
                <tr>
                    <td style={{background: 'skyblue', width: '70px'}}>
                        제목
                    </td>
                    <td style={{textAlign: 'left'}}>
                        <input type="text" name="boardTitle" id="boardTitle" value={board ? board.boardTitle : ''} onChange={changeTitle}></input>
                    </td>
                </tr>
                <tr>
                    <td style={{background: 'skyblue'}}>
                        작성자
                    </td>
                    <td style={{textAlign: 'left'}}>
                        <input type="text" name="boardWriter" id="boardWriter" readonly value={board ? board.boardWriter : ''}></input>
                    </td>
                </tr>
                <tr>
                    <td style={{background: 'skyblue'}}>
                        내용
                    </td>
                    <td style={{textAlign: 'left'}}>
                        <textarea name="boardContent" id="boardContent" cols="40" rows="10" value={board ? board.boardContent : ''} onChange={changeContent}></textarea>
                    </td>
                </tr>
                <tr>
                    <td style={{background: 'skyblue'}}>
                        작성일
                    </td>
                    <td style={{textAlign: 'left'}}>
                        {board ? board.boardRegdate : ''}
                    </td>
                </tr>
                <tr>
                    <td style={{background: 'skyblue'}}>
                        조회수
                    </td>
                    <td style={{textAlign: 'left'}}>
                        {board ? board.boardCnt : ''}
                    </td>
                </tr>
                <tr>
                    <td style={{background: 'skyblue', width: '70px'}}>
                        첨부파일
                    </td>
                    <td style={{textAlign: 'left'}}>
                        <div id="image_preview">
                            <input type="file" id="btnAtt" name="uploadFiles" multiple onChange={addFiles}></input>
                            <input type="file" id="changedFiles" name="changedFiles"
                                   style={{display: 'none'}} multiple></input>
                            <p style={{color: 'red', fontSize: '0.9rem'}}>
                                파일을 변경하려면 사진을 파일을 다운로드하려면 파일명을 클릭하세요.
                            </p>
                            <div id="attZone">
                                {boardFileList 
                                ? 
                                <FileList boardFileList={boardFileList} addChangeFile={addChangeFile} changeOriginFile={changeOriginFile}></FileList>
                                :
                                ''}
                            </div>
                        </div>
                    </td>
                </tr>
                <tr id="btnWrap">
                    <td colspan="2" style={{textAlign: 'center'}}>
                        <button type="submit" id="btnUpdate">수정</button>
                    </td>
                </tr>
            </table>
        </form>
        <hr/>
        <Link to="/insert-board">글 등록</Link>
        <Link to="#" id="btnDelete" onClick={deleteBoard}>글 삭제</Link>
        <Link to="/board-list">글 목록</Link>
    </div>
  );
};

export default Board;