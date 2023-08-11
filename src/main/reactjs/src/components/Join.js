import React, {useCallback, useEffect, useState} from 'react';
import '../css/Join.css';
import {TextField} from '@mui/material';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Join = () => {
    const navi = useNavigate();

    const [userId, setUserId] = useState('');
    const [userPw, setUserPw] = useState('');
    const [userPwCheck, setUserPwCheck] = useState('');
    const [userName, setUserName] = useState('');
    const [userEmail, setUserEmail] = useState('');
    const [userTel, setUserTel] = useState('');

    const [checkId, setCheckId] = useState(false);
    const [pwValidation, setPwValidation] = useState(false);
    const [pwCheck, setPwCheck] = useState(false);

    //userId input 변경 시 작동하는 메소드
    const changeUserId = useCallback((e) => {
        const idCheckBtn = document.getElementById('btnIdCheck');
        setUserId(() => e.target.value);
        idCheckBtn.disabled = false;
    }, []);

    //userId state가 변경되면 checkId를 무조건 false
    useEffect(() => {
        setCheckId(() => false);
    }, [userId]);

    //id 중복체크 메소드
    const idCheck = useCallback((e) => {
        const userIdInput = document.getElementById('userId');

        const idCheckAxios = async () => {
            try {
                const response = await axios.post('/user/id-check', {userId: userId});

                console.log(response);

                if(response.data && response.data.item.idCheckMsg === 'idOk') {
                    alert('사용가능한 아이디입니다.');
                    setCheckId(() => true);
                    e.target.disabled = true;
                } else {
                    alert('중복된 아이디입니다.');
                    setCheckId(() => false);
                    userIdInput.focus();
                }

                console.log(checkId);
            } catch(e) {
                console.log(e);
            }
        }

        idCheckAxios();
    }, [userId]);

    //userPw input 변경 시 동작할 메소드
    const changeUserPw = useCallback((e) => {
        setUserPw(() => e.target.value);
    }, []);

    //비밀번호 유효성 검사 메소드
    const validatePassword = (character) => {
        return /^(?=.*[a-zA-Z])(?=.*[!@#$%^&*+=-])(?=.*[0-9]).{9,}$/.test(character);
    }

    //userPw state가 변경될 때마다 유효성 검사
    useEffect(() => {
        const pwValidationTag = document.getElementById('pwValidation');

        if(validatePassword(userPw)) {
            setPwValidation(() => true);
            pwValidationTag.style.display = 'none'; 
        } else {
            setPwValidation(() => false);
            pwValidationTag.style.display = 'block'; 
        }
    }, [userPw]);

    //userPwCheck input 변경 시 동작할 메소드
    const changeUserPwCheck = useCallback((e) => {
        setUserPwCheck(() => e.target.value);
    }, []);

    //userPwCheck state가 변경될 때마다 비밀번호 일치 여부 검사
    useEffect(() => {
        const pwCheckResult = document.getElementById('pwCheckResult');

        if(userPwCheck === userPw) {
            pwCheckResult.style.color = 'green';
            pwCheckResult.textContent = '비밀번호가 일치합니다.';
            setPwCheck(() => true);
        } else {
            pwCheckResult.style.color = 'red';
            pwCheckResult.textContent = '비밀번호가 일치하지 않습니다.';
            setPwCheck(() => false);
        }
    }, [userPw, userPwCheck]);

    const onSubmit = useCallback((e) => {
        e.preventDefault();

        console.log(checkId);

        if(!checkId) {
            alert("아이디 중복 체크를 진행하세요.");
            return;
        }

        if(!pwValidation) {
            alert("유효하지 않은 비밀번호입니다.");
            return;
        }

        if(!pwCheck) {
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }

        const join = async () => {
            const user = {
                userId: userId,
                userPw: userPw,
                userName: userName,
                userEmail: userEmail,
                userTel: userTel
            };

            try {
                const response = await axios.post('/user/join', user);

                console.log(response);

                if(response.data && response.data.item.id) {
                    alert("회원가입이 완료되었습니다.");
                    navi('/login');
                }
            } catch(e) {
                console.log(e);
            }
        }

        join();
    }, [userId, userPw, userName, userEmail, userTel, checkId, pwValidation, pwCheck]);

  return (
    <div className="form-wrapper">
        <form id="modifyForm" onSubmit={onSubmit}>
            <h3>회원가입</h3>
            <div className="label-wrapper">
                <label htmlFor="userId">아이디</label>
            </div>
            <div>
                <input type="text" id="userId" name="userId" required style={{width: 'auto'}} value={userId} onChange={changeUserId}></input>
                <button type="button" id="btnIdCheck" style={{width: '70px'}} onClick={idCheck}>중복체크</button>
            </div>
            <div className="label-wrapper">
                <label htmlFor="userPw">비밀번호</label>
            </div>
            <input type="password" id="userPw" name="userPw" required value={userPw} onChange={changeUserPw}></input>
            <p id="pwValidation" style={{color: 'red', fontSize: '0.8rem'}}>
            비밀번호는 영문자, 숫자, 특수문자 조합의 9자리 이상으로 설정해주세요.
            </p>
            <div className="label-wrapper">
                <label htmlFor="userPwCheck">비밀번호 확인</label>
            </div>
            <input type="password" id="userPwCheck" name="userPwCheck" required value={userPwCheck} onChange={changeUserPwCheck}></input>
            <p id="pwCheckResult" style={{fontSize: '0.8rem'}}></p>
            <div className="label-wrapper">
                <label htmlFor="userName">이름</label>
            </div>
            <input type="text" id="userName" name="userName" required value={userName}
            onChange={(e) => setUserName(() => e.target.value)}></input>
            <div className="label-wrapper">
                <label htmlFor="userEmail">이메일</label>
            </div>
            <input type="email" id="userEmail" name="userEmail" required value={userEmail} onChange={(e) => setUserEmail(() => e.target.value)}></input>
            <div className="label-wrapper">
                <label htmlFor="userTel">전화번호</label>
            </div>
            <input type="text" id="userTel" name="userTel" placeholder="숫자만 입력하세요." value={userTel} onChange={(e) => setUserTel(() => e.target.value)}></input>
            <div style={{display: 'block', margin: '20px auto'}}>
                <button type="submit">회원가입</button>
            </div>
        </form>
    </div>
  );
};

export default Join;