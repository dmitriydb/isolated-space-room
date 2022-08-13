import React, {Component} from "react";
import "./Sensor.css"
import SensorLabel from "./SensorLabel";
import SensorValue from "./SensorValue";

class Sensor extends Component {

    render() {
        console.log("Rendering sendor");
        if (this.props.rv) {
            return (
                <div className="sensorDiv">
                    <SensorValue value={this.props.value} barColor={this.props.barColor}
                    withoutBorder={this.props.withoutBorder}/>
                     <SensorLabel label={this.props.label} textColor={this.props.textColor}/>
                </div> 
         )
        } else {
            return (
                <div className="sensorDiv">
                    <SensorLabel label={this.props.label} textColor={this.props.textColor}/>
                    <SensorValue value={this.props.value} barColor={this.props.barColor}
                    withoutBorder={this.props.withoutBorder}/>
                </div> 
         )
        }
    }
}

export default Sensor;