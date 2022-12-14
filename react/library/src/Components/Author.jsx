import React, {useState, useEffect} from 'react';
import AuthorService from '../API/AuthorService';
import {Button, Modal} from 'react-bootstrap';
import AuthorForm from './AuthorForm';
import LoginService from '../API/LoginService';
import BookList from './BookList';
import Loader from "../UI/Loader/Loader";

const Author = ({tokens, setTokens, setModal}) => {
    const [author, setAuthor] = useState('');
    const [show, setShow] = useState(false);
    const [events, setEvents] = useState(false);
    const [loading, setLoading] = useState(false);
    useEffect(() => {
        setLoading(true);
        setTimeout(() => {
            fetchParticipant();
            setLoading(false);
        }, 1000);
    }, []);

    const change = (data) => {
        if (data) {
            window.location.reload(false);
        }
    };
    const remove = () => {
        AuthorService.delete(tokens).then(data => {
            console.log(data);
            if (data.hasError) {
                LoginService.refresh(tokens).then(data => {
                    if (data.hasError) {
                        alert("you must relogin")
                    } else {
                        setTokens(data, tokens.refresh_token);
                        AuthorService.delete(data);
                    }
                });
            }
        });
    };

    async function fetchParticipant() {
        const response = await AuthorService.getParticipant(tokens.access_token);
        if (response.hasError) {
            setModal(false);
        }
        setAuthor(response);
    };
    return (
        <div>
            {loading ? (
                <div style={{display: 'flex', justifyContent: 'center'}}>
                    <Loader/>
                </div>
            ) : (
                <div className="item">
                    <div style={{textAlign: 'left'}}>
                        <h1> {author.name} </h1>
                        <h1> {author.secondName} </h1>
                        <h1> {author.age} </h1>
                    </div>
                    <h3> {author.email} </h3>
                    <div style={{textAlign: 'center'}}>
                        <Modal show={show} onHide={setShow}> <AuthorForm CreateOrUpdate={change}
                                                                         author={author}
                                                                         tokens={tokens}
                                                                         setTokens={setTokens}/></Modal>
                        {
                            events ?
                                (
                                    <>
                                        <BookList tokens={tokens} setTokens={setTokens}/>
                                        <Button variant="dark" onClick={() => setEvents(false)}> close </Button>
                                    </>
                                ) : (
                                    <>
                                        <Button onClick={() => setShow(true)}> change </Button>
                                        <Button variant="danger" onClick={() => remove()}> delete </Button>
                                        <Button onClick={() => setEvents(true)}> books </Button>
                                        <br/>
                                    </>
                                )
                        }
                    </div>
                </div>
            )}
        </div>
    );
};
export default Author;
