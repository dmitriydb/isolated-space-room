import React, {Component} from "react";
import "./Panel.css";
import LeftSensors from "./LeftSensors";
import RightSensors from "./RightSensors";
import Radar from "./Radar";

class Panel extends Component {

    render() {
        return (
            <div className="panelDiv">
                <LeftSensors stats={this.props.stats}/>
                <Radar temperature={this.props.stats.temperature}/>
                <RightSensors stats={this.props.stats}/>
            </div>
        )
    }
}

export default Panel;