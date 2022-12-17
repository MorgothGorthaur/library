import React, {useState, useEffect} from 'react';
import AuthorService from '../API/AuthorService';
import {Button, Modal} from 'react-bootstrap';
import AuthorForm from './AuthorForm';
import Loader from '../UI/Loader/Loader';

const AuthorList = () => {
    const [authors, setAuthors] = useState([]);
    const [modal, setModal] = useState(false);
    const [loading, setLoading] = useState(false);
    const add = (author) => {
        setAuthors([...authors, author]);
        setModal(false);
    }
    useEffect(() => {
        setLoading(true);
        setTimeout(() => {
            fetchAuthors();
            setLoading(false);
        }, 1000);
    }, []);

    async function fetchAuthors() {
        const response = await AuthorService.getAll();
        setAuthors(response);
    }
    return (
        <div>
            {loading ? (
                <div style={{display: 'flex', justifyContent: 'center'}}>
                    <Loader/>
                </div>
            ) : (
                <div className="participant_list">
                    {authors.length ? (
                        <div>
                            {authors.map(author =>
                                <div className="participant_item">
                                    <h1> {author.firstName} </h1>
                                    <h1> {author.lastName} </h1>
                                </div>
                            )}
                        </div>
                    ) : (
                        <h1> authors not found! </h1>
                    )}
                    <Button onClick={() => setModal(true)}> add </Button>
                    <Modal show={modal} onHide={setModal}> <AuthorForm CreateOrUpdate={add}/> </Modal>
                </div>
            )}
        </div>


    );
};
export default AuthorList;
