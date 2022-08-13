import React, {Component} from "react";
import "./SensorLabel.css";

class SensorLabel extends Component {

    render() {

        var textStyle = {
            color: this.props.textColor,
            marginTop: 1,
            marginLeft: -2
        }

        return (
            <div className="sensorLabelDiv">
                <span style={textStyle}>{this.props.label}</span>
            </div>
        )
    }
}

export default SensorLabel;