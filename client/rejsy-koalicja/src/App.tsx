import React from 'react';
import logo from './logo.svg';
import './App.scss';
import BookCharter from "./features/charter/bookCharter/BookCharter";
import Price from "./features/charter/price/price";
import Discounts from "./features/charter/discounts/discounts";

function App() {
    return (
        <div className="App">
            <div className="title">
                <h1>This Page is only for testing back-end!</h1>
            </div>
            <div className="content">
                <BookCharter/>
                <Price/>
                <Discounts/>
            </div>
        </div>
    );
}

export default App;
