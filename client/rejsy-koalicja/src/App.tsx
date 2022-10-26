import React from 'react';
import logo from './logo.svg';
import './App.scss';
import BookCharter from "./features/charter/bookCharter/BookCharter";
import Price from "./features/charter/price/price";

function App() {
    return (
        <div className="App">
            <h1>This Page is only for testing back-end!</h1>
            <BookCharter/>
            <Price/>
        </div>
    );
}

export default App;
