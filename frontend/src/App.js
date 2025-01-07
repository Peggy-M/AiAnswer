import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Courses from './pages/Courses';
import Exams from './pages/Exams';
import Admin from './pages/Admin';

function App() {
    return (
        <Router>
            <div className="App">
                <Navbar />
                <Switch>
                    <Route exact path="/" component={Home} />
                    <Route path="/courses" component={Courses} />
                    <Route path="/exams" component={Exams} />
                    <Route path="/admin" component={Admin} />
                </Switch>
            </div>
        </Router>
    );
}

export default App; 