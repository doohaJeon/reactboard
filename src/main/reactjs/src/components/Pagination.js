import React, { useEffect, useState } from 'react';
import {Link} from 'react-router-dom';
import '../css/Pagination.css';

const Pagination = ({totalPages, pageNumber, pageSize, clickPrevNext, changePage}) => {
    const [pageArr,setPageArr] = useState([]);
    
    useEffect(() => {
        const startPage = Math.floor(pageNumber / pageSize) * pageSize + 1;
        const tempEndPage = startPage + pageSize - 1;
        const endPage = tempEndPage > totalPages ? totalPages : tempEndPage;

        const tempArr = [];

        for(let i = startPage; i <= endPage; i++) {
            tempArr.push(i);
        }

        setPageArr(() => tempArr);
    }, [totalPages, pageNumber, pageSize]);

  return (
    <div style={{textAlign: 'center'}}>
        <ul className="pagination">
            <li className="pagination-btn">
                <Link onClick={(e) => {
                    e.preventDefault();
                    clickPrevNext(-1);
                }}>
                이전
                </Link>
            </li>

            <li className="pagination-btn">
                {pageArr && pageArr.map(
                    num => <Link onClick={(e) => {
                        e.preventDefault();
                        changePage(num);
                    }}>{num}</Link>
                )}
            </li>

            <li className="pagination-btn">
                <Link onClick={(e) => {
                    e.preventDefault();
                    clickPrevNext(1);
                }}>
                다음
                </Link>
            </li>
        </ul>
    </div>
  );
};

export default Pagination;