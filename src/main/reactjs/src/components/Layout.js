import React from 'react';
import styled from 'styled-components';
import {Link, Outlet} from 'react-router-dom';
import '../css/Layout.css';

const UserNav = styled.ul`
    display: flex;
    justify-content: space-between;
    align-items: center;
`;

const UserNavItem = styled.li`
    margin-left: 10px;
    font-size: 0.9rem;
`;

const Layout = ({isLogin}) => {
  return (
    <>
        <header>
            <h1>
                <Link to="/">홈</Link>
            </h1>
            <nav>
                <ul className="main-nav">
                    <li>
                        <Link to="/board-list">게시글 목록</Link>
                    </li>
                </ul>
            </nav>
            <nav>
                {
                    isLogin && sessionStorage.getItem("ACCESS_TOKEN") 
                    ?
                    <UserNav>
                        <UserNavItem>
                            <Link to="/mypage">마이페이지</Link>
                        </UserNavItem>
                        <UserNavItem>
                            <Link to="/logout">로그아웃</Link>
                        </UserNavItem>
                    </UserNav>
                    :
                    <UserNav>
                        <UserNavItem>
                            <Link to="/login">로그인</Link>
                        </UserNavItem>
                        <UserNavItem>
                            <Link to="/join">회원가입</Link>
                        </UserNavItem>
                    </UserNav>
                }
            </nav>
        </header>
        <main>
            <Outlet></Outlet>
        </main>
        <footer>
            <div>
                <p>copyright 비트캠프</p>
            </div>
        </footer>
    </>
  );
};

export default Layout;