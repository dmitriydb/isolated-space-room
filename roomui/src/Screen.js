import React, { Component } from "react";
import FrontView from "./FrontView";
import Panel from "./Panel";
import "./Screen.css";

const NORMAL = 'img/frontview.png';
const HEAT = 'img/heat.png';
const FREEZE = 'img/freeze.png';

class Screen extends Component {

    constructor(props) {
        super(props);

        this.state = {
            img: NORMAL,
            stats: {
                temperature:0,
                vector:0,
                speed:0,
                roomState:'open'
            }
        }
        this.startListening();
    }

    startListening() {
        fetch('http://127.0.0.1:10005/stats')
            .then(response => response.json())
            .then(data => {
                this.processData(data);
                this.startListening();
            });
    }

    processData(data) {
        if (data.temperature < 0 && data.roomState == 'closed') {
            this.setState({img: FREEZE, stats: data});
        } else 
        if (data.temperature > 0 && data.roomState == 'closed') {
            this.setState({img: HEAT, stats: data});
        } else {
            this.setState({img: NORMAL, stats: data});
        }
    }

    render() {
        return (
            <div className="screenDiv">
                <FrontView img={this.state.img}/>
                <Panel stats={this.state.stats}/>
            </div>
        )
    }
}

export default Screen;